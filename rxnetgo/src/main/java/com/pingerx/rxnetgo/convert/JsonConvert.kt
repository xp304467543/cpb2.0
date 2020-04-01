package com.pingerx.rxnetgo.convert

import android.text.TextUtils
import com.pingerx.rxnetgo.convert.base.IConverter
import com.pingerx.rxnetgo.exception.ApiException
import com.pingerx.rxnetgo.exception.NetErrorEngine
import com.pingerx.rxnetgo.utils.JsonUtils
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-12-3 下午3:47
 *
 * Json数据实体转换器，这里提供一个简单的版本，如果解析失败，请自定义解析方法
 * 而且每种数据结构不同，可能有些需要做封装，可以继承本类，重写[convertResponse]方法，重新解析数据
 * 使用请查看[com.pingerx.rxnetgo.subscribe.JsonSubscriber]
 */
class JsonConvert<T>(private var type: Type? = null, private val clazz: Class<T>? = null) : IConverter<T> {

    @Suppress("UNCHECKED_CAST")
    @Throws(Exception::class)
    override fun convertResponse(body: ResponseBody?): T? {
        val json = body?.string() ?: ""
        body?.close()

        if (TextUtils.isEmpty(json)) {
            throw ApiException(code = NetErrorEngine.DATA_ERROR)
        } else {
            if (clazz != null) {
                return parseClass(json, clazz)
            }
            val type = getType()
            return when (type) {
                is Class<*> -> parseClass(json, type as Class<T>)
                else -> parseType(json, type)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(Exception::class)
    private fun parseClass(result: String, rawType: Class<T>): T {
        return when (rawType) {
            String::class.java -> result as T
            JSONObject::class.java -> JSONObject(result) as T
            JSONArray::class.java -> JSONArray(result) as T
            else -> JsonUtils.fromJson(result, rawType)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(Exception::class)
    private fun parseType(result: String, type: Type?): T {
        if (type == null) throw ApiException(code = NetErrorEngine.PARSE_ERROR_CODE)
        // 泛型格式如下： JsonCallback<任意JavaBean>(this)
        return JsonUtils.fromJson(result, type)
    }

    override fun getType(): Type {
        val type = type
        return if (type == null) {
            val genType = javaClass.genericSuperclass
            (genType as ParameterizedType).actualTypeArguments[0]
        } else type
    }

}