package com.pingerx.rxnetgo.utils

import android.util.Log

/**
 * @author Pinger
 * @since 18-08-17 上午9:43
 *
 * NetGo框架的日志打印
 */
object NetLogger {

    var isDebug = true
        private set

    private var tag = "RxNetGo"

    fun debug(isEnable: Boolean) {
        debug(tag, isEnable)
    }

    fun debug(logTag: String, isEnable: Boolean) {
        tag = logTag
        isDebug = isEnable
    }

    fun v(msg: String) {
        v(tag, msg)
    }

    fun v(tag: String, msg: String) {
        if (isDebug) Log.v(tag, msg)
    }

    fun d(msg: String) {
        d(tag, msg)
    }

    fun d(tag: String, msg: String) {
        if (isDebug) Log.d(tag, msg)
    }

    fun i(msg: String) {
        i(tag, msg)
    }

    fun i(tag: String, msg: String) {
        if (isDebug) Log.i(tag, msg)
    }

    fun w(msg: String) {
        w(tag, msg)
    }

    fun w(tag: String, msg: String) {
        if (isDebug) Log.w(tag, msg)
    }

    fun e(msg: String) {
        e(tag, msg)
    }

    fun e(e: Throwable) {
        e("----> ${e.message}")
    }

    fun e(tag: String, msg: String) {
        if (isDebug) Log.e(tag, msg)
    }

    fun printStackTrace(t: Throwable?) {
        if (isDebug && t != null) t.printStackTrace()
    }

}
