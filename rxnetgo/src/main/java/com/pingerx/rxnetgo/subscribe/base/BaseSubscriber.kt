package com.pingerx.rxnetgo.subscribe.base

import com.pingerx.rxnetgo.exception.ApiException

/**
 * @author Pinger
 * @since 18-10-19 上午11:08
 *
 * 订阅者基类，抽离出来，应对其他的变化
 *
 * 可以自定义订阅者，实现本基类，自定义数据结构
 * 不同的数据结构可以实现不同的订阅者，这样就可以处理不同的数据了
 *
 */
abstract class BaseSubscriber<T> : ISubscriber<T> {

    private var mStart: (() -> Unit)? = null
    private var mSuccess: ((data: T) -> Unit)? = null
    private var mCacheSuccess: ((data: T) -> Unit)? = null
    private var mFailed: ((e: ApiException) -> Unit)? = null
    private var mComplete: (() -> Unit)? = null
    private var mConvert: ((data: T) -> T)? = null

    override fun onStart(start: () -> Unit) {
        this.mStart = start
    }

    override fun onSuccess(success: (data: T) -> Unit) {
        this.mSuccess = success
    }

    override fun onCacheSuccess(cacheSuccess: (data: T) -> Unit) {
        this.mCacheSuccess = cacheSuccess
    }

    override fun onFailed(error: (e: ApiException) -> Unit) {
        this.mFailed = error
    }

    override fun onComplete(complete: () -> Unit) {
        this.mComplete = complete
    }

    override fun onConvert(convert: (data: T) -> T) {
        this.mConvert = convert
    }

    internal fun getStart(): (() -> Unit)? {
        return mStart
    }

    internal fun getSuccess(): ((data: T) -> Unit)? {
        return mSuccess
    }

    internal fun getCacheSuccess(): ((data: T) -> Unit)? {
        return mCacheSuccess
    }

    internal fun getFailed(): ((e: ApiException) -> Unit)? {
        return mFailed
    }

    internal fun getComplete(): (() -> Unit)? {
        return mComplete
    }

    internal fun getConvert(): ((data: T) -> T)? {
        return mConvert
    }
}
