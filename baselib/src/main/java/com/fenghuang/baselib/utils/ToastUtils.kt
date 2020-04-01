package com.fenghuang.baselib.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.StringRes
import es.dmoral.toasty.Toasty
import androidx.core.os.HandlerCompat.postDelayed


/**
 * 土司工具类，暂时没有进行线程切换，如果在子线程中，需要切换到主线程中调用
 */
object ToastUtils {

    /**
     * 展示一个吐司，短时间的吐司
     */
    fun showToast(msg: String?) {
        if (msg != null) {
            val toast = Toast.makeText(AppUtils.getContext(), msg, Toast.LENGTH_SHORT)
            toast.setText(msg)
            toast.show()
        }
    }


    /**
     * 展示一个吐司
     */
    fun showToast(@StringRes strId: Int) {
        val msg = AppUtils.getString(strId)
        showToast(msg)
    }


    /**
     * 长时间展示的吐司
     */
    fun showLong(msg: String?) {
        if (msg != null) {
            Toasty.normal(AppUtils.getContext(), msg, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 长时间展示的吐司
     */
    fun showLong(@StringRes strId: Int) {
        val msg = AppUtils.getString(strId)
        if (msg != null) {
            Toasty.normal(AppUtils.getContext(), msg, Toast.LENGTH_LONG).show()
        }
    }


    /**
     * 只在开发环境才会展示的吐司，用于开发调试使用
     */
    fun testToast(msg: String?) {
        if (DebugUtils.isDevModel() && msg != null) {
            Toasty.normal(AppUtils.getContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 成功提示
     */
    fun showSuccess(success: String?) {
        if (TextUtils.isEmpty(success)) {
            return
        }
//        Toasty.success(AppUtils.getContext(), success!!, Toast.LENGTH_SHORT, true).show()
//        Toasty.normal(AppUtils.getContext(), success!!, Toast.LENGTH_SHORT).show()
        show(success!!)
    }

    /**
     * 错误提示
     */
    fun showError(error: String?) {
        if (TextUtils.isEmpty(error)) {
            return
        }
//        Toasty.error(AppUtils.getContext(), error!!, Toast.LENGTH_SHORT, true).show()
//        Toasty.normal(AppUtils.getContext(), error!!, Toast.LENGTH_SHORT).show()
        show(error!!)
    }

    /**
     * 错误提示
     */
    fun showErrorLong(error: String?) {
        if (TextUtils.isEmpty(error)) {
            return
        }
//        Toasty.error(AppUtils.getContext(), error!!, Toast.LENGTH_LONG, true).show()
//        Toasty.normal(AppUtils.getContext(), error!!, Toast.LENGTH_SHORT).show()
        show(error!!)
    }


    fun showInfo(info: String?) {
        if (TextUtils.isEmpty(info)) {
            return
        }
//        Toasty.info(AppUtils.getContext(), info!!, Toast.LENGTH_SHORT, true).show()
//        Toasty.normal(AppUtils.getContext(), info!!, Toast.LENGTH_SHORT).show()
        show(info!!)
    }


    fun showWarning(warning: String?) {
        if (TextUtils.isEmpty(warning)) {
            return
        }
//        Toasty.warning(AppUtils.getContext(), warning!!, Toast.LENGTH_SHORT, true).show()
        show(warning!!)
    }

    fun showNormal(normal: String?) {
        if (TextUtils.isEmpty(normal)) {
            return
        }
//        Toasty.normal(AppUtils.getContext(), normal!!, Toast.LENGTH_SHORT).show()
        show(normal!!)
    }


    var isShow = true
    var toast: Toast? = null

    //短时间显示Toast
    @SuppressLint("ShowToast")
    fun show(message: String) {
        if (isShow) {
            toast = if (toast == null) {
                Toast.makeText(AppUtils.getContext(), message, Toast.LENGTH_SHORT)
            } else {
                toast?.cancel()//关闭吐司显示
                Toast.makeText(AppUtils.getContext(), message, Toast.LENGTH_SHORT)
            }
            toast?.show()
        }
    }


}