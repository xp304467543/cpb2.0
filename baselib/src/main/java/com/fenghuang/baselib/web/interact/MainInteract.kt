package com.fenghuang.baselib.web.interact

import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.fenghuang.baselib.web.sonic.SonicSessionClientImpl

/**
 * 抽取子类Interact的实现方法，统一管理，不需要每个地方都写
 */

abstract class MainInteract(sessionClient: SonicSessionClientImpl?, intent: Intent?, context: Context?, webView: WebView?) : BaseJavaScriptInterface(sessionClient, intent, context, webView) {


    /**
     * 获取版本号
     */
    val versionCode: String
        @JavascriptInterface
        get() = "0001"


    /**
     * 获取电话号码
     */
    val phoneCode: String
        @JavascriptInterface
        get() = "13215623585"


    /**
     * 弹窗登录
     */
    @JavascriptInterface
    fun startLogin() {

    }


    /**
     * 绑定手机
     */
    @JavascriptInterface
    fun bindPhone() {

    }


}
