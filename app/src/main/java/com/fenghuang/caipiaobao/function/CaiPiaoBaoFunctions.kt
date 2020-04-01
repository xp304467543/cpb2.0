package com.fenghuang.caipiaobao.function

import android.text.TextUtils
import com.fenghuang.caipiaobao.manager.RxHelper

/**
 * 最上层公共代码Kotlin抽离调用
 */

/** 检查不为空 */
fun checkNotNull(any: Any?): Boolean {
    return any != null
}

/** 文字是否是空的 */
fun isEmpty(text: String?): Boolean {
    return TextUtils.isEmpty(text)
}

/** 文字不是空的 */
fun isNotEmpty(text: String?): Boolean {
    return !isEmpty(text)
}

/** 运行在主线程 */
fun doOnUiThread(func: () -> Unit) {
    RxHelper.doOnUiThread { func.invoke() }
}

/** 运行在子线程 */
fun doOnIOThread(func: () -> Unit) {
    RxHelper.doOnIOThread { func.invoke() }
}

/** Post到主线程 */
fun post(task: () -> Unit) {
    doOnUiThread { task.invoke() }
}

/** Post到主线程 */
fun postDelay(time: Long, task: () -> Unit) {
    RxHelper.doDelay(time) { task.invoke() }
}






