package com.pingerx.rxnetgo.convert

import com.pingerx.rxnetgo.convert.base.IConverter
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-12-3 下午3:41
 *
 * 字符串转换器，直接获取ResponseBody的字符串返回即可
 */
class StringConvert : IConverter<String> {

    @Throws(Exception::class)
    override fun convertResponse(body: ResponseBody?): String? {
        val result = body?.string()
        body?.close()
        return result
    }

    override fun getType(): Type {
        return String::class.java
    }
}