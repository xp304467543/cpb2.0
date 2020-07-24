package com.pingerx.rxnetgo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.pingerx.rxnetgo.cookie.CookieJarImpl
import com.pingerx.rxnetgo.cookie.store.PersistentCookieStore
import com.pingerx.rxnetgo.exception.NetErrorEngine
import com.pingerx.rxnetgo.https.HttpsUtils
import com.pingerx.rxnetgo.interceptor.LogInterceptor
import com.pingerx.rxnetgo.model.HttpHeaders
import com.pingerx.rxnetgo.model.HttpParams
import com.pingerx.rxnetgo.request.GetRequest
import com.pingerx.rxnetgo.request.PostRequest
import com.pingerx.rxnetgo.request.base.ApiService
import com.pingerx.rxnetgo.rxcache.CacheMode
import com.pingerx.rxnetgo.rxcache.core.RxCache
import com.pingerx.rxnetgo.rxcache.data.CacheResult
import com.pingerx.rxnetgo.rxcache.diskconverter.GsonDiskConverter
import com.pingerx.rxnetgo.subscribe.base.BaseSubscriber
import com.pingerx.rxnetgo.subscribe.base.RxSubscriber
import com.pingerx.rxnetgo.utils.HttpUtils
import com.pingerx.rxnetgo.utils.NetLogger
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.logging.Level

/**
 * @author Pinger
 * @since 18-08-16 上午10:52
 *
 * 网络类库封装：RxJava2 + Retrofit2封装，支持多种不同的baseurl,支持缓存，支持生命周期管理
 *
 * 网络默认配置：
 *  １．各类超时时间统一默认：10秒
 *  ２．缓存模式默认：先请求后缓存，网络异常时读取缓存
 *  ３．默认支持Https
 *  ４．Cookie默认长久保存在SP中
 *loadCache
 * 发起网络请求：
 * [get] get请求
 * [post] post请求
 *
 * 手动缓存数据：
 * [loadCache] 读取缓存
 * [saveCache] 保存缓存
 *
 * 生命周期管理
 * [addSubscription]添加请求订阅
 * [dispose] 取消请求订阅
 *
 */
class RxNetGo {

    private var mContext: Context? = null               // 全局上下文
    private var mClient: OkHttpClient? = null           // okhttp请求的客户端

    private var mRetryCount: Int = 0                // 全局超时重试次数
    private var mCacheMode: CacheMode = CacheMode.NONE       // 全局缓存模式
    private var mCacheTime: Long = 0                // 全局缓存过期时间,默认永不过期
    private var mCacheVersion: Int = 0              // 全局缓存版本，如果版本升级，则会清空之前的所有缓存

    // 当有多个baseurl时，需要build多个Retrofit实例，这里缓存 起来
    private val mRetrofitMap = HashMap<String, Retrofit>()
    private val mServiceMap = HashMap<String, ApiService>()

    private var mHeadersMap = HashMap<String, HttpHeaders>()  // 全局公共请求头
    private var mParamsMap = HashMap<String, HttpParams>()  // 全局公共请求参数

    private var mService: ApiService? = null
    private var mBaseUrl: String? = null

    // 发起的请求使用集合存储，方便取消
    private val mDisposables: CompositeDisposable = CompositeDisposable()

    private val mHandler: Handler = Handler(Looper.getMainLooper())

    companion object {

        private val TAG = RxNetGo::class.java.simpleName

        const val DEFAULT_MILLISECONDS: Long = 50000     // 默认的超时时间,5秒
        const val CACHE_NEVER_EXPIRE: Long = -1           // 缓存永不过期

        fun getInstance(): RxNetGo = NetGoHolder.holder
    }

    @SuppressLint("StaticFieldLeak")
    private object NetGoHolder {
        val holder = RxNetGo()
    }


    /**
     * 生成默认的配置，如果想更新配置，可以使用内部的Builder更新
     */
    private fun initNetGo() {
        mRetryCount = 25
        mCacheTime = CACHE_NEVER_EXPIRE
        // 默认不使用缓存
        mCacheMode = CacheMode.NONE
        // 缓存版本如果不修改，则缓存永远进行覆盖
        mCacheVersion = 2

        val builder = OkHttpClient.Builder()

        // 日志打印
        val loggingInterceptor = LogInterceptor(TAG)
        loggingInterceptor.setPrintLevel(LogInterceptor.Level.BODY)
        loggingInterceptor.setColorLevel(Level.INFO)
        builder.addInterceptor(loggingInterceptor)


        // 超时
        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)

        // https
        val sslParams = HttpsUtils.sslSocketFactory
        builder.sslSocketFactory(sslParams.sSLSocketFactory!!, sslParams.trustManager!!)
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier)

        // cookie
        builder.cookieJar(CookieJarImpl(PersistentCookieStore(getContext())))

        mClient = builder.build()

        // RxCache
        val rxCache = RxCache.Builder()
                .appVersion(mCacheVersion)           // 当版本号改变,缓存路径下存储的所有数据都会被清除掉
                .diskDir(HttpUtils.getCacheFile(getContext()))
                .diskConverter(GsonDiskConverter())  // 支持Serializable、Json(GsonDiskConverter)
                .memoryMax(2 * 1024 * 1024)       // 2M内存缓存
                .diskMax((100 * 1024 * 1024).toLong())       // 100M硬盘缓存
                .build()
        RxCache.initializeDefault(rxCache)
    }


    /**
     * 必须在全局Application先调用，获取context上下文
     */
    fun init(app: Application): RxNetGo {
        mContext = app.applicationContext
        initNetGo()
        return this
    }


    /**
     * 是否开发模式
     */
    fun debug(debug: Boolean): RxNetGo {
        NetLogger.debug(TAG, debug)
        return this
    }

    /**
     * 获取全局的Handler
     */
    fun getHandler(): Handler {
        return mHandler
    }

    /**
     * 获取全局公共请求参数
     */
    fun getCommonParams(): HttpParams? {
        return mParamsMap[mBaseUrl]
    }

    /**
     * 获取全局公共请求头
     */
    fun getCommonHeaders(): HttpHeaders? {
        return mHeadersMap[mBaseUrl]
    }


    /**
     * 获取全局上下文
     */
    fun getContext(): Context {
        HttpUtils.checkNotNull(mContext, "please call RxNetGo.newInstance().init() first in application!")
        return mContext!!
    }


    /**
     * 获取OkHttpClient
     */
    fun getOkHttpClient(): OkHttpClient {
        HttpUtils.checkNotNull<Any>(mClient, "please call RxNetGo.newInstance().init() first in application!")
        return mClient!!
    }

    /**
     * 获取已经生成的Retrofit的集合
     */
    fun getRetrofits(): Map<String, Retrofit> {
        return mRetrofitMap
    }

    /**
     * 获取底层URL
     */
    fun getBaseUrl(): String? {
        return mBaseUrl
    }


    /**
     * 获取全局的cookie实例
     */
    fun getCookieJar(): CookieJarImpl {
        return getOkHttpClient().cookieJar() as CookieJarImpl
    }

    /**
     * 手动设置OkHttpClient
     */
    fun setOkHttpClient(client: OkHttpClient): RxNetGo {
        HttpUtils.checkNotNull<Any>(client, "client can not be null")
        this.mClient = client
        return this
    }

    /**
     * 如果使用Retrofit，首先就要生成Service
     * 根据url生成对应的Service,Service可能会有多个，所有这里用Map集合存起来，避免重复创建
     * 这里使用默认的[ApiService]，可以直接调用get和post请求网络
     */
    fun getRetrofitService(baseUrl: String): RxNetGo {
        if (mServiceMap[baseUrl] == null) {
            mService = getRetrofitService(baseUrl, ApiService::class.java)
            mServiceMap[baseUrl] = mService!!
        } else {
            mService = mServiceMap[baseUrl]
            mBaseUrl = baseUrl
        }
        return this
    }

    /**
     * 获取用户自定义的Retrofit的Service，使用自定义的Service时，需要手动传入Service生成的Flowable
     * [get(flowable)]
     */
    fun <S> getRetrofitService(baseUrl: String, service: Class<S>): S {
        return getRetrofit(baseUrl).create(service)
    }

    /**
     * 根据baseurl获取Retrofit的实例
     */
    private fun getRetrofit(baseUrl: String): Retrofit {
        HttpUtils.checkNotNull<Any>(baseUrl, "baseUrl can not be null")

        if (getRetrofits()[baseUrl] != null) {
            return getRetrofits().getValue(baseUrl)
        }

        val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        val retrofit = builder.build()
        mRetrofitMap[baseUrl] = retrofit
        mBaseUrl = baseUrl
        return retrofit
    }

    /**
     * 超时重试次数
     */
    fun setRetryCount(retryCount: Int): RxNetGo {
        require(retryCount >= 0) { "retryCount must > 0" }
        mRetryCount = retryCount
        return this
    }

    /**
     * 超时重试次数
     */
    fun getRetryCount(): Int {
        return mRetryCount
    }

    /**
     * 全局的缓存模式
     */
    fun setCacheMode(cacheMode: CacheMode): RxNetGo {
        mCacheMode = cacheMode
        return this
    }

    /**
     * 获取全局的缓存模式
     */
    fun getCacheMode(): CacheMode? {
        return mCacheMode
    }

    /**
     * 全局的缓存过期时间
     */
    fun setCacheTime(cacheTime: Long): RxNetGo {
        if (cacheTime <= -1) mCacheTime = CACHE_NEVER_EXPIRE
        mCacheTime = cacheTime
        return this
    }

    fun setCacheVersion(cacheVersion: Int): RxNetGo {
        this.mCacheVersion = cacheVersion
        return this
    }


    /**
     * 获取全局的缓存过期时间
     */
    fun getCacheTime(): Long {
        return mCacheTime
    }

    /**
     * 添加全局公共请求参数，每次添加都会将之前的请求参数移除
     */
    fun addCommonParams(params: HttpParams): RxNetGo {
        if (mBaseUrl != null) {
            mParamsMap[mBaseUrl!!] = params
        }
        return this
    }

    /**
     * 添加全局公共请求头，每次添加都会将之前的请求头移除
     */
    fun addCommonHeaders(headers: HttpHeaders): RxNetGo {
        if (mBaseUrl != null) {
            mHeadersMap[mBaseUrl!!] = headers
        }
        return this
    }

    /**
     * 清除网络数据缓存
     */
    fun clearCache() {
        try {
            RxCache.getDefault().clear()
        } catch (e: Exception) {
            NetLogger.e(e)
        }
    }


    //=======================Request API=======================
    //=======================Request API=======================
    //=======================Request API=======================
    /**
     * get请求,传入的可以是全路径url，也可以是其中的path
     * 当传入全路径url时会使用整个url访问
     */
    fun <T> get(url: String = ""): GetRequest<T> {
        return GetRequest(url, mService, null)
    }


    /**
     * get请求，由外部传入Retrofit组织好的Flowable进来
     */
    fun <T> get(flowable: Flowable<T>): GetRequest<T> {
        return GetRequest("", null, flowable)
    }


    /**
     * post请求，需要先初始化对应的Service
     */
    fun <T> post(url: String = ""): PostRequest<T> {
        return PostRequest(url, mService, null)
    }

    /**
     * post请求，由用户自定义Service
     */
    fun <T> post(flowable: Flowable<T>): PostRequest<T> {
        return PostRequest("", null, flowable)
    }


    //=======================Cache API=======================
    //=======================Cache API=======================
    //=======================Cache API=======================

    /**
     * 手动异步读取缓存
     */
    fun <T> loadCache(key: String, subscriber: BaseSubscriber<T>) {
        val callback = RxSubscriber<T>()
        callback.setSubscriber(subscriber)
        RxCache.getDefault()
                .load<T>(key, subscriber.getType())
                .map(CacheResult.MapFunc())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(Function { Flowable.error(NetErrorEngine.handleException(it)) })
                .subscribe(callback)
    }

    /**
     * 同步读取缓存
     */
    fun <T> loadCacheSync(key: String, type: Type): T? {
        return RxCache.getDefault().loadSync<T>(key, type)?.data
    }


    /**
     * 手动保存缓存到内存和磁盘
     * 子线程保存
     */
    fun <T> saveCache(key: String, data: T) {
        RxCache.getDefault().save(key, data).subscribeOn(Schedulers.io()).subscribe()
    }


    //=======================LifeCycle API=======================
    //=======================LifeCycle API=======================
    //=======================LifeCycle API=======================
    /**
     * 添加一个请求
     */
    fun addSubscription(disposable: Disposable) {
        mDisposables.add(disposable)
    }


    /**
     * 取消指定请求
     */
    fun dispose(disposable: Disposable) {
        mDisposables.delete(disposable)
    }


    /**
     * 取消所有的请求
     */
    fun dispose() {
        mDisposables.clear()
    }
}
