package com.pingerx.rxnetgo.rxcache.diskconverter

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.pingerx.rxnetgo.utils.HttpUtils
import com.pingerx.rxnetgo.utils.NetLogger
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 2019/2/21 20:12
 *
 * 默认序列化的转化器
 */
class GsonDiskConverter constructor(private val gson: Gson = Gson()) : IDiskConverter {

    @Suppress("UNCHECKED_CAST")
    override fun <T> load(source: InputStream, type: Type): T? {
        var value: T? = null
        try {
            val adapter = gson.getAdapter(TypeToken.get(type))
            val jsonReader = gson.newJsonReader(InputStreamReader(source))
            value = adapter.read(jsonReader) as T
        } catch (e: JsonIOException) {
            NetLogger.e(e)
        } catch (e: JsonSyntaxException) {
            NetLogger.e(e)
        } catch (e: IOException) {
            NetLogger.e(e)
        } finally {
            HttpUtils.close(source)
        }
        return value
    }

    override fun writer(sink: OutputStream, data: Any): Boolean {
        try {
            val json = gson.toJson(data)
            val bytes = json.toByteArray()
            sink.write(bytes, 0, bytes.size)
            sink.flush()
            return true
        } catch (e: JsonIOException) {
            NetLogger.e(e)
        } catch (e: IOException) {
            NetLogger.e(e)
        } finally {
            HttpUtils.close(sink)
        }
        return false
    }
}
