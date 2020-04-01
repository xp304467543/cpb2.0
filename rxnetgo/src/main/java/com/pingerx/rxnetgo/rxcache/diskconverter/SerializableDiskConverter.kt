package com.pingerx.rxnetgo.rxcache.diskconverter

import com.pingerx.rxnetgo.utils.HttpUtils
import com.pingerx.rxnetgo.utils.NetLogger
import java.io.*
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 2019/2/21 19:56
 *
 * 默认序列化的转化器
 */
class SerializableDiskConverter : IDiskConverter {

    @Suppress("UNCHECKED_CAST")
    override fun <T> load(source: InputStream, type: Type): T? {
        var value: T? = null
        var oin: ObjectInputStream? = null
        try {
            oin = ObjectInputStream(source)
            value = oin.readObject() as T
        } catch (e: IOException) {
            NetLogger.e(e)
        } catch (e: ClassNotFoundException) {
            NetLogger.e(e)
        } finally {
            HttpUtils.close(oin)
        }
        return value
    }

    override fun writer(sink: OutputStream, data: Any): Boolean {
        var oos: ObjectOutputStream? = null
        try {
            oos = ObjectOutputStream(sink)
            oos.writeObject(data)
            oos.flush()
            return true
        } catch (e: IOException) {
            NetLogger.e(e)
        } finally {
            HttpUtils.close(oos)
        }
        return false
    }

}
