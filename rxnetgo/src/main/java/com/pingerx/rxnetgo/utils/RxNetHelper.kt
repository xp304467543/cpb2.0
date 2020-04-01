package com.pingerx.rxnetgo.utils

import android.text.TextUtils
import com.pingerx.rxnetgo.convert.base.IConverter
import com.pingerx.rxnetgo.exception.ApiException
import com.pingerx.rxnetgo.exception.NetErrorEngine
import com.pingerx.rxnetgo.request.RequestType
import com.pingerx.rxnetgo.request.base.Request
import com.pingerx.rxnetgo.rxcache.rxCache
import com.pingerx.rxnetgo.rxcache.stategy.ICacheStrategy
import com.pingerx.rxnetgo.subscribe.base.RxSubscriber
import com.pingerx.rxnetgo.utils.RxNetHelper.cache
import com.pingerx.rxnetgo.utils.RxNetHelper.error
import com.pingerx.rxnetgo.utils.RxNetHelper.scheduler
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * @author Pinger
 * @since 18-12-5 下午4:39
 *
 * 网络请求工具类，提供请求相关的Rx操作
 * 线程切换：[scheduler]
 * 异常处理：[error]
 * 缓存：[cache]
 */
object RxNetHelper {

    /**
     * 订阅请求
     */
    fun <T> subscribe(request: Request<T>, subscriber: RxSubscriber<T>): Disposable {
        return rxAsync(request)
                .subscribeWith(subscriber)
    }

    /**
     * 异步请求
     */
    fun <T> rxAsync(request: Request<T>): Flowable<T> {
        return when (request.getMethod()) {
            RequestType.GET -> getAsync(request)
            RequestType.POST -> postAsync(request)
        }
    }

    /**
     * 同步请求
     */
    @Throws(Exception::class)
    fun <T> rxSync(request: Request<T>): T? {
        return when (request.getMethod()) {
            RequestType.GET -> getSync(request)
            RequestType.POST -> postSync(request)
        }
    }


    /**
     * Rx异步的Get请求
     */
    private fun <T> getAsync(request: Request<T>): Flowable<T> {
        return if (request.apiService != null) {
            request.apiService
                    .getAsync(request.url, request.getHeaders().getHeaderParams(), request.getParams().getUrlParams())
                    .flatMap { response ->
                        val data = request.getConverter().convertResponse(response)
                        if (data != null) Flowable.just(data)
                        else Flowable.empty()
                    }
                    .cache(request.getCacheKey(), request.getConverter(), request.getCacheStrategy())
                    .compose()
        } else request.flowable ?: (request.flowable
                ?: Flowable.error(ApiException(code = NetErrorEngine.REQUEST_ERROR)))
    }

    /**
     * Rx异步的Post请求
     * Flowable.empty() 会直接进入onComplete回调
     */
    private fun <T> postAsync(request: Request<T>): Flowable<T> {
        return if (request.apiService != null) {
            request.apiService
                    .postAsync(request.url, request.getHeaders().getHeaderParams(), request.getParams().getUrlParams(), request.generateRequestBody())
                    .flatMap { response ->
                        val data = request.getConverter().convertResponse(response)
                        if (data != null) Flowable.just(data)
                        else Flowable.empty()
                    }
                    .cache(request.getCacheKey(), request.getConverter(), request.getCacheStrategy())
                    .compose()
        } else request.flowable ?: (request.flowable
                ?: Flowable.error(ApiException(code = NetErrorEngine.REQUEST_ERROR)))
    }


    /**
     * get同步请求，不需要使用Rx
     */
    @Throws(Exception::class)
    private fun <T> getSync(request: Request<T>): T? {
        return if (request.apiService != null) {
            val response = request.apiService
                    .getSync(request.url, request.getHeaders().getHeaderParams(), request.getParams().getUrlParams())
                    .execute()
                    .body()
            request.getConverter().convertResponse(response)
        } else null
    }


    /**
     * post同步请求，不需要使用Rx
     */
    @Throws(Exception::class)
    private fun <T> postSync(request: Request<T>): T? {
        return if (request.apiService != null) {
            val response = request.apiService
                    .postSync(request.url, request.getHeaders().getHeaderParams(), request.getParams().getUrlParams(), request.generateRequestBody())
                    .execute()
                    .body()
            request.getConverter().convertResponse(response)
        } else null
    }

    /**
     * 生成组合线程切换，异常处理的Flowable
     */
    private fun <T> Flowable<T>.compose(): Flowable<T> {
        return scheduler().error()
    }


    /**
     * 异常处理
     */
    private fun <T> Flowable<T>.error(): Flowable<T> {
        return onErrorResumeNext(Function {
            Flowable.error<T> {
                val throwable = if (!TextUtils.isEmpty(it.message)) it
                else ApiException(it, code = NetErrorEngine.DATA_ERROR)
                NetErrorEngine.handleException(throwable)
            }
        })
    }

    /**
     * 线程切换
     */
    private fun <T> Flowable<T>.scheduler(): Flowable<T> {
        return subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    /**
     * 缓存
     */
    private fun <T> Flowable<T>.cache(cacheKey: String, subscriber: IConverter<T>, strategy: ICacheStrategy): Flowable<T> {
        return rxCache(cacheKey, subscriber.getType(), strategy).flatMap {
            if (it.data != null) Flowable.just(it.data)
            else Flowable.empty()
        }
    }
}