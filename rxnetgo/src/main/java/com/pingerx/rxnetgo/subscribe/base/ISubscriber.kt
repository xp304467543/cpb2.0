package com.pingerx.rxnetgo.subscribe.base

import com.pingerx.rxnetgo.convert.base.IConverter
import com.pingerx.rxnetgo.exception.ApiException

/**
 * @author Pinger
 * @since 18-11-30 下午6:04
 *
 * 定义订阅者的回调方法，方便处理结果
 */
interface ISubscriber<T> : IConverter<T> {

    /**
     * 订阅开始
     */
    fun onStart(start: () -> Unit)

    /**
     * 订阅成功，data必不为空，如果为空的话会回调[onFailed]方法
     */
    fun onSuccess(success: (data: T) -> Unit)

    /**
     * 读取缓存成功
     */
    fun onCacheSuccess(cacheSuccess: (data: T) -> Unit)

    /**
     * 订阅失败，响应错误，数据解析错误等，都会回调该方法， UI线程
     */
    fun onFailed(error: (e: ApiException) -> Unit)

    /**
     * 订阅完成
     */
    fun onComplete(complete: () -> Unit)

    /**
     * 在子线程继续转换数据
     */
    fun onConvert(convert: (data: T) -> T)
}