package com.fenghuang.caipiaobao.socket.listener

import okhttp3.Response
import okio.ByteString

/**
 *  author : Peter
 *  date   : 2019/8/30 17:31
 *  desc   : 可用于监听ws连接状态并进一步拓展
 */
abstract class WsStatusListener {

    open fun onOpen(response: Response) {}

    open fun onMessage(text: String) {}

    open fun onMessage(bytes: ByteString) {}

    open fun onReconnect() {

    }

    open fun onClosing(code: Int, reason: String) {}


    open fun onClosed(code: Int, reason: String) {}

    open fun onFailure(t: Throwable?, response: Response?) {}
}
