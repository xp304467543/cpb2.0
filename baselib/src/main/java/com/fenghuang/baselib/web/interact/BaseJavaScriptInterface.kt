/*
 * Tencent is pleased to support the open source community by making VasSonic available.
 *
 * Copyright (C) 2017 THL A29 Limited, a Tencent company. All rights reserved.
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 *
 */

package com.fenghuang.baselib.web.interact


import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.fenghuang.baselib.web.WebConstant
import com.fenghuang.baselib.web.sonic.SonicSessionClientImpl
import org.json.JSONObject

/**
 * Sonic javaScript Interface (Android API Level >= 17)
 */

abstract class BaseJavaScriptInterface {

    private var mContext: Context? = null
    private var mWebView: WebView? = null

    private val sessionClient: SonicSessionClientImpl?
    private var intent: Intent? = null

    val performance: String
        @JavascriptInterface
        get() {
            val clickTime = intent?.getLongExtra(WebConstant.KEY_WEB_CLICK_TIME, -1)
            val loadUrlTime = intent?.getLongExtra(WebConstant.KEY_WEB_LOAD_URL_TIME, -1)
            try {
                val result = JSONObject()
                result.put(WebConstant.KEY_WEB_CLICK_TIME, clickTime)
                result.put(WebConstant.KEY_WEB_LOAD_URL_TIME, loadUrlTime)
                return result.toString()
            } catch (e: Exception) {

            }

            return ""
        }

    abstract val name: String

    constructor(sessionClient: SonicSessionClientImpl?, intent: Intent?, context: Context?, webView: WebView?) {
        this.sessionClient = sessionClient
        this.intent = intent
        this.mContext = context
        this.mWebView = webView
    }

    constructor(sessionClient: SonicSessionClientImpl?, intent: Intent?, webView: WebView?) {
        this.sessionClient = sessionClient
        this.intent = intent
        this.mWebView = webView
    }

    @JavascriptInterface
    fun getDiffData() {
        // the callback function of demo page is hardcode as 'getDiffDataCallback'
        getDiffData2("getDiffDataCallback")
    }

    @JavascriptInterface
    private fun getDiffData2(jsCallbackFunc: String) {
        sessionClient?.getDiffData { resultData ->
            val callbackRunnable = Runnable {
                val jsCode = "javascript:" + jsCallbackFunc + "('" + toJsString(resultData) + "')"
                sessionClient.webView?.loadUrl(jsCode)
            }
            if (Looper.getMainLooper() == Looper.myLooper()) {
                callbackRunnable.run()
            } else {
                Handler(Looper.getMainLooper()).post(callbackRunnable)
            }
        }
    }

    /*
    * * From RFC 4627, "All Unicode characters may be placed within the quotation marks except
    * for the characters that must be escaped: quotation mark,
    * reverse solidus, and the control characters (U+0000 through U+001F)."
    */
    private fun toJsString(value: String?): String {
        if (value == null) {
            return "null"
        }
        val out = StringBuilder(1024)
        var i = 0
        val length = value.length
        while (i < length) {
            val c = value[i]

            when (c) {
                '"', '\\', '/' -> out.append('\\').append(c)

                '\t' -> out.append("\\t")

                '\b' -> out.append("\\b")

                '\n' -> out.append("\\n")

                '\r' -> out.append("\\r")

                else -> if (c.toInt() <= 0x1F) {
                    out.append(String.format("\\u%04x", c.toInt()))
                } else {
                    out.append(c)
                }
            }
        }
        return out.toString()
    }
}
