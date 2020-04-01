package com.pingerx.rxnetgo.subscribe

import com.pingerx.rxnetgo.convert.JsonConvert
import com.pingerx.rxnetgo.subscribe.base.BaseSubscriber
import okhttp3.ResponseBody
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-11-30 下午6:19
 *
 * 返回结果需要转为对象的Bean，可以使用本观察者，但是并没有对数据实体进行上层的额封装，生成的是整个数据实体
 * 如果需要对数据实体进行二次封装，可以在onNext方法中进行处理
 */
open class JsonSubscriber<T>(
        private var type: Type? = null,
        private val clazz: Class<T>? = null
) : BaseSubscriber<T>() {

    @Throws(Exception::class)
    override fun convertResponse(body: ResponseBody?): T? {
        return convertData(JsonConvert<T>(getType()).convertResponse(body))
    }

    /**
     * 数据转换
     */
    protected fun convertData(data: T?): T? {
        val convert = getConvert()
        return if (convert != null && data != null) {
            convert.invoke(data)
        } else {
            data
        }
    }

    override fun getType(): Type {
        val type = type
        return type ?: if (clazz == null) {
            val genType = javaClass.genericSuperclass
            (genType as ParameterizedType).actualTypeArguments[0]
        } else clazz
    }
}