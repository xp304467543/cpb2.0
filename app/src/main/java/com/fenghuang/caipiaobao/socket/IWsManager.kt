package com.fenghuang.caipiaobao.socket

import okhttp3.WebSocket
import okio.ByteString

/**
 *  author : Peter
 *  date   : 2019/8/30 17:40
 *  desc   : 可用于监听ws连接状态并进一步拓展
 */
internal interface IWsManager {

    val webSocket: WebSocket

    val isWsConnected: Boolean

    var currentStatus: Int

    fun startConnect()

    fun stopConnect()

    fun sendMessage(msg: String): Boolean

    fun sendMessage(byteString: ByteString): Boolean
}
