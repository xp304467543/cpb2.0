package com.pingerx.rxnetgo.request.base

import android.text.TextUtils
import com.pingerx.rxnetgo.RxNetGo
import com.pingerx.rxnetgo.convert.base.IConverter
import com.pingerx.rxnetgo.model.HttpHeaders
import com.pingerx.rxnetgo.model.HttpParams
import com.pingerx.rxnetgo.request.RequestType
import com.pingerx.rxnetgo.rxcache.CacheMode
import com.pingerx.rxnetgo.rxcache.stategy.*
import com.pingerx.rxnetgo.subscribe.base.BaseSubscriber
import com.pingerx.rxnetgo.subscribe.base.RxSubscriber
import com.pingerx.rxnetgo.utils.HttpUtils
import com.pingerx.rxnetgo.utils.RxNetHelper
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import okhttp3.RequestBody

/**
 * @author Pinger
 * @since 18-10-23 上午11:03
 *
 * 请求基类，封装请求，包括配置请求参数，请求头，缓存，和订阅请求等等
 *
 * 数据转换使用Converter的方式，如果是异步请求，回调继承[BaseSubscriber]实现converter方法即可
 * 如果没有回调，则需要使用[Request.converter]方法，传入转换器
 *
 */
abstract class Request<T>(
        // 请求的path或者全路径
        internal val url: String,
        // retrofit的service，用户自定义service时为null
        internal val apiService: ApiService?,
        // service中定义的观察者，如果使用默认的service则为null
        internal val flowable: Flowable<T>?) {

    // 请求参数
    private val mParams = HttpParams()
    //　请求头
    private val mHeaders = HttpHeaders()
    // 订阅者
    private var mSubscriber: BaseSubscriber<T>? = null
    // 缓存
    private var mCacheKey: String? = null
    private var mCacheMode: CacheMode? = null
    private var mCacheTime: Long = 0
    // 转换器，等级比BaseSubscriber高
    private var mConverter: IConverter<T>? = null
    // RxNetGo
    private val mRxNetGo = RxNetGo.getInstance()

    init {
        // 默认添加 Accept-Language
        val acceptLanguage = HttpHeaders.acceptLanguage
        if (!TextUtils.isEmpty(acceptLanguage))
            headers(HttpHeaders.HEAD_KEY_ACCEPT_LANGUAGE, acceptLanguage)

        // 默认添加 User-Agent
        val userAgent = HttpHeaders.userAgent
        if (!TextUtils.isEmpty(userAgent)) headers(HttpHeaders.HEAD_KEY_USER_AGENT, userAgent)

        // 添加公共请求参数，根据baseUrl获取参数
        if (apiService != null) {
            val httpParams = mRxNetGo.getCommonParams()
            val httpHeaders = mRxNetGo.getCommonHeaders()
            if (httpParams?.getUrlParams()?.isNotEmpty() == true)
                params(httpParams)
            if (httpHeaders?.getHeaderParams()?.isNotEmpty() == true)
                headers(httpHeaders)
        }

        mRxNetGo.setRetryCount(25)
        // 添加缓存模式
//        mCacheMode = mRxNetGo.getCacheMode()
//        mCacheTime = mRxNetGo.getCacheTime()


    }


    /**
     * 子类提供请求方式，调用[RxNetGo.get]或者[RxNetGo.post]
     */
    abstract fun getMethod(): RequestType

    /**
     * 根据不同的请求方式和参数，生成不同的RequestBody
     * 如果发起的请求中要自定义请求体，可以继承[BodyRequest]实现
     */
    abstract fun generateRequestBody(): RequestBody?

    /**
     * 获取请求参数对象
     */
    fun getParams(): HttpParams {
        return mParams
    }


    /**
     * 获取请求头对象
     * 获取的请求头包括了公共请求头
     * 添加公共请求头请使用[RxNetGo.addCommonHeaders]
     */
    fun getHeaders(): HttpHeaders {
        return mHeaders
    }

    /**
     * 暂时提供四种缓存策略[CacheMode]
     */
    fun getCacheStrategy(): ICacheStrategy {
        return when (mCacheMode) {
            CacheMode.FIRST_CACHE_NONE_REMOTE -> FirstCacheNoneRemoteStrategy()
            CacheMode.FIRST_CACHE_THEN_REMOTE -> FirstCacheThenRemoteStrategy()
            CacheMode.FIRST_REMOTE_THEN_CACHE -> FirstRemoteThenCacheStrategy()
            CacheMode.FIRST_CACHE_AND_REMOTE -> FirstCacheAndRemoteStrategy()
            CacheMode.ONLY_CACHE -> OnlyCacheStrategy()
            CacheMode.ONLY_REMOTE -> OnlyRemoteStrategy()
            else -> NoneStrategy()
        }
    }


    /**
     * 请求唯一的key
     */
    fun getCacheKey(): String {
        return mCacheKey ?: HttpUtils.appendUrlParams(url, mParams.getUrlParams())
    }

    /**
     * 数据转换器，异步方法的回调对象默认也是转换器，或者通过[converter]方法传入
     *
     * 异步请求可以通过[BaseSubscriber.convertResponse]方法解析数据
     * 同步请求一定要调用[converter]传入解析器，否则解析不了数据
     */
    fun getConverter(): IConverter<T> {
        // converter 优先级高于 subscriber
        if (mConverter == null) mConverter = mSubscriber
        HttpUtils.checkNotNull(mConverter, "converter == null, do you forget to call Request#converter(Converter<T>) ?")
        return mConverter!!
    }

    //---------------------------------------------------------------------
    //------------------------ 订阅请求的API --------------------------------
    //---------------------------------------------------------------------
    /**
     * 同步请求，会阻塞线程
     *
     * 注意：
     *  １．必须在子线程中调用，如果要更新UI，需要手动切换线程。
     *  ２．必须手动捕获异常。
     */
    @Throws(Exception::class)
    fun subscribe(): T? {
        return RxNetHelper.rxSync(this)
    }


    /**
     * 带缓存的同步请求，有缓存会直接读取缓存，没有缓存会请求数据
     */
    @Throws(Exception::class)
    fun subscribeWithCache(): T? {
        val cache = mRxNetGo.loadCacheSync<T>(getCacheKey(), getConverter().getType())
        // 如果有缓存直接返回缓存
        if (cache != null) return cache
        val data = RxNetHelper.rxSync(this)
        // 保存缓存
        mRxNetGo.saveCache(getCacheKey(), data)
        return data
    }

    /**
     * 订阅异步请求，在回调中处理结果，订阅者可以自定义继承[BaseSubscriber]就可以手动去处理数据
     * 返回回调方法[BaseSubscriber.onSuccess]中，数据T一定不为空，如果解析出数据为null则会回调[BaseSubscriber.onFailed]方法
     *
     * 本默认会将发起的订阅加入管理器中，如果要取消订阅可以调用[RxNetGo.dispose]方法
     * 加入管理器后，回调的[BaseSubscriber.onFailed]和[BaseSubscriber.onComplete]方法可以统一取消订阅
     * 如果需要自己处理订阅生命周期，可以调用[subscribeWith]方法
     */
    fun subscribe(subscriber: BaseSubscriber<T>) {
        this.mSubscriber = subscriber
        val rxSubscriber = RxSubscriber<T>()
        rxSubscriber.setSubscriber(subscriber)
        val disposable = RxNetHelper.subscribe(this, rxSubscriber)
        mRxNetGo.addSubscription(disposable)
    }

    /**
     * 订阅异步请求，返回[Disposable]对象，需要自己手动处理请求生命周期
     * 如果自己处理订阅，回调中的方法都不会去取消订阅，如果自己不去处理，有可能会发生内存泄露
     *
     * 推荐使用[subscribe]方法，管理生命周期更加灵活
     */
    fun subscribeWith(subscriber: BaseSubscriber<T>): Disposable {
        this.mSubscriber = subscriber
        val rxSubscriber = RxSubscriber<T>()
        rxSubscriber.setSubscriber(subscriber)
        return RxNetHelper.subscribe(this, rxSubscriber)
    }

    /**
     * 异步请求获取Flowable对象
     */
    fun flowable(): Flowable<T> {
        return RxNetHelper.rxAsync(this)
    }

    //---------------------------------------------------------------------
    //------------------------ 请求配置的API --------------------------------
    //---------------------------------------------------------------------
    fun headers(headers: HttpHeaders): Request<T> {
        this.mHeaders.put(headers)
        return this
    }

    fun headers(key: String, value: String?): Request<T> {
        this.mHeaders.put(key, value)
        return this
    }

    fun removeHeader(key: String): Request<T> {
        this.mHeaders.remove(key)
        return this
    }

    fun removeAllHeaders(): Request<T> {
        this.mHeaders.clear()
        return this
    }

    fun params(params: HttpParams): Request<T> {
        this.mParams.put(params)
        return this
    }

    fun params(params: Map<String, String>): Request<T> {
        this.mParams.put(params)
        return this
    }


    fun params(key: String, value: String): Request<T> {
        this.mParams.put(key, value)
        return this
    }

    fun params(key: String, value: Int): Request<T> {
        this.mParams.put(key, value)
        return this
    }


    fun params(key: String, value: Boolean): Request<T> {
        this.mParams.put(key, value)
        return this
    }


    fun params(key: String, value: Float): Request<T> {
        this.mParams.put(key, value)
        return this
    }


    fun params(key: String, value: Double): Request<T> {
        this.mParams.put(key, value)
        return this
    }

    fun params(key: String, value: Long): Request<T> {
        this.mParams.put(key, value)
        return this
    }

    fun removeParam(key: String): Request<T> {
        this.mParams.remove(key)
        return this
    }

    fun removeAllParams(): Request<T> {
        this.mParams.clear()
        return this
    }

    fun converter(converter: IConverter<T>): Request<T> {
        HttpUtils.checkNotNull(converter, "converter == null")
        this.mConverter = converter
        return this
    }

    fun cacheMode(cacheMode: CacheMode): Request<T> {
        this.mCacheMode = cacheMode
        return this
    }

    fun cacheKey(cacheKey: String): Request<T> {
        this.mCacheKey = cacheKey
        return this
    }

    fun cacheTime(cacheTime: Long): Request<T> {
        this.mCacheTime = cacheTime
        return this
    }
}