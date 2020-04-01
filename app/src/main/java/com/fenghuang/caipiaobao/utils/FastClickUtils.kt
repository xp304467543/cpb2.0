package com.fenghuang.caipiaobao.utils

/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/26- 17:43
 * @ Describe
 *
 */

object FastClickUtils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private const val MIN_CLICK_DELAY_TIME = 600
    private var lastClickTime: Long = 0

    fun isFastClick(): Boolean {
        var flag = false
        val curClickTime = System.currentTimeMillis()
        if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
            flag = true
        }
        lastClickTime = curClickTime
        return flag
    }



    fun isFastClick1000(): Boolean {
        var flag = false
        val curClickTime = System.currentTimeMillis()
        if (curClickTime - lastClickTime >= 1000) {
            flag = true
        }
        lastClickTime = curClickTime
        return flag
    }

}