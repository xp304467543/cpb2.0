package com.fenghuang.caipiaobao.data.api

import com.fenghuang.baselib.utils.DebugUtils
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.caipiaobao.function.isEmpty
import com.fenghuang.caipiaobao.utils.JsonUtils
import java.lang.reflect.Type

/**
 * Web页面的接口管理
 */
object WebUrlProvider {
//    生产环境： wss://www.cpbadmin.com/wss
//
//    测试环境： ws://www.cpbh5.com/wss
    private const val API_URL_WEB_SOCKET = "ws://www.cpbh5.com/wss"

    private const val API_URL_WEB_SOCKET_MAIN = "wss://www.cpbadmin.com/wss"

     fun getBaseUrl(): String {
        return if (ApiConstant.isTest) {
            API_URL_WEB_SOCKET
        } else {
            API_URL_WEB_SOCKET_MAIN
        }
    }

    fun <T> getData(message: String?, type: Type): T? {
        try {
            val content = message ?: ""
            return if (isEmpty(content)) null else {
                JsonUtils.fromJson(content, type)
            }
        } catch (e: Exception) {
            LogUtils.e(e)
        }
        return null
    }

}