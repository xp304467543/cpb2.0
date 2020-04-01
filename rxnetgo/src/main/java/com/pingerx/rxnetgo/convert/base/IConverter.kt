package com.pingerx.rxnetgo.convert.base

import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-10-23 上午11:29
 *
 *
 * 自定义转换器，提供给子类转换数据格式
 * 如果使用自定义的Retrofit的Service，则不需要转换数据
 */
interface IConverter<T> {

    /**
     * 将服务器返回的Response，抓换成想要的数据实体
     * 解析回来的数据有可能会是空的，因为如果类型没匹配上，或者发生了被捕获的异常
     * 无论如何，后端返回的数据一定有可能是空的
     */
    @Throws(Exception::class)
    fun convertResponse(body: ResponseBody?): T?


    /**
     * 获取需要解析的数据类型
     */
    fun getType(): Type
}
