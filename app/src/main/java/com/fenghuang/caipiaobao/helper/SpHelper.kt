package com.fenghuang.caipiaobao.helper

import com.fenghuang.baselib.utils.SpUtils

object SpHelper {

    private const val SP_FIRST_LAUNCH = "SP_FIRST_LAUNCH"
    private const val SP_JPUSH_ID = "SP_JPUSH_ID"
    private const val SP_SOFT_KEYBOARD_HEIGHT = "SP_SOFT_KEYBOARD_HEIGHT"


    /**
     * 第一次启动
     */
    var isFirstLaunch
        get() = SpUtils.getBoolean(SP_FIRST_LAUNCH)
        set(value) {
            SpUtils.putBoolean(SP_FIRST_LAUNCH, value)
        }


    /**
     * 极光唯一的ID
     */
    var jPushId
        get() = SpUtils.getString(SP_JPUSH_ID)
        set(value) {
            if (value != null) {
                SpUtils.putString(SP_JPUSH_ID, value)
            }
        }

    var softKeyboardHeight
        get() = SpUtils.getInt(SP_SOFT_KEYBOARD_HEIGHT)
        set(value) {
            SpUtils.putInt(SP_SOFT_KEYBOARD_HEIGHT, value)
        }
}