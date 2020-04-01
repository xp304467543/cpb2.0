package com.pingerx.rxnetgo.subscribe

import com.pingerx.rxnetgo.convert.StringConvert
import com.pingerx.rxnetgo.subscribe.base.BaseSubscriber
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-10-19 上午11:41
 *
 *
 * 当请求返回的结果为String类型时，可以使用本订阅者，会handle请求过程中的异常。
 */
class StringSubscriber : BaseSubscriber<String>() {

    @Throws(Exception::class)
    override fun convertResponse(body: ResponseBody?): String? {
        val data = StringConvert().convertResponse(body)
        val convert = getConvert()
        return if (data != null && convert != null) {
            convert.invoke(data)
        } else data
    }

    override fun getType(): Type {
        return String::class.java
    }
}
