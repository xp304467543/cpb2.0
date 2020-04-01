package com.pingerx.rxnetgo.subscribe.base

import com.pingerx.rxnetgo.exception.ApiException
import com.pingerx.rxnetgo.exception.NetErrorEngine

/**
 * @author Pinger
 * @since 2019/1/22 13:35
 */
class RxSubscriber<T> : ResourceSubscriber<T>() {

    private var mSubscriber: BaseSubscriber<T>? = null

    fun setSubscriber(subscriber: BaseSubscriber<T>) {
        this.mSubscriber = subscriber
    }

    /**
     * 转发请求成功结果
     * 可能会有多次请求返回，所以不要直接取消
     */
    override fun onNext(data: T) {
        mSubscriber?.getSuccess()?.invoke(data)
    }

    /**
     * 转发请求失败发生的异常
     * 发生异常自动取消订阅，如果要做重连，需要在onErrorResumeNext时重新请求
     */
    override fun onError(e: Throwable) {
        if (e is ApiException) {
            mSubscriber?.getFailed()?.invoke(e)
        } else {
            mSubscriber?.getFailed()?.invoke(ApiException(error = e, code = NetErrorEngine.UNKNOWN_CODE, msg = NetErrorEngine.UNKNOW_MSG))
        }
    }

    /**
     * 请求完成
     */
    override fun onComplete() {
        mSubscriber?.getComplete()?.invoke()
    }
}