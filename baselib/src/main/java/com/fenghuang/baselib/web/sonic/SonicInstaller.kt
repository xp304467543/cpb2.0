package com.fenghuang.baselib.web.sonic

import android.content.Context
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine

/**
 * Sonic初始化
 */
object SonicInstaller {


    /**
     * 初始化Sonic,涉及到数据库的读取，在子线程执行。
     */
    fun initSonic(context: Context) {
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(SonicRuntimeImpl(context), SonicConfig.Builder().build())
        }
    }

}