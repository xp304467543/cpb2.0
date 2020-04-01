package com.pingerx.rxnetgo.exception

import android.text.TextUtils
import com.google.gson.JsonElement

/**
 * @author Pinger
 * @since 18-10-17 下午4:29
 *
 * 网络请求和数据发生的异常，自定义异常，将发生的其他异常转换，在onnError方法中回调
 */
class ApiException(error: Throwable? = null, private var code: Int = 0, private var msg: String? = null, private var dataCode: JsonElement? = null) : Exception(error) {

    /**
     * 获取异常消息时会先判断传入的[msg]字段是否有值，没有的话获取默认异常的msg
     */
    fun getMsg(): String? {
        return if (TextUtils.isEmpty(msg))
            super.message
        else msg
    }

    fun setMsg(msg: String) {
        this.msg = msg
    }

    fun setCode(code: Int) {
        this.code = code
    }

    fun getCode(): Int {
        return this.code
    }

    override val message: String?
        get() = msg

    fun getDataCode(): JsonElement? {
        return this.dataCode
    }

}