package com.fenghuang.baselib.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * 软键盘相关工具类，键盘的弹出和隐藏
 */
object SoftInputUtils {

    private val mFilterViews = arrayListOf<View>()
    private var mTouchOutsideFunction: ((ev: MotionEvent) -> Unit)? = null

    /**
     * 是否隐藏输入框
     */
    fun isHideSoftInput(event: MotionEvent): Boolean {
        var hide = true
        for (view in mFilterViews) {
            val array = intArrayOf(0, 0)
            view.getLocationInWindow(array)
            val left = array[0]
            val top = array[1]
            val bottom = top + view.height
            val right = left + view.width
            if (event.x > left && event.x < right && event.y > top && event.y < bottom) {
                hide = false
                break
            }
        }
        return hide
    }

    /**
     * 设置软键盘展示或者不展示
     */
    fun setSoftInput(view: View?, isShow: Boolean) {
        view?.let {
            if (isShow) {
                showSoftInput(it)
            } else {
                hideSoftInput(it.context)
            }
        }
    }


    /**
     * 显示软键盘
     */
    fun showSoftInput(view: View?) {
        if (view == null || view.context == null) return
        try {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            view.requestFocus()
            view.postDelayed({ imm.showSoftInput(view, InputMethodManager.SHOW_FORCED) }, 200)
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }

    /**
     * 隐藏软键盘
     */
    fun hideSoftInput(context: Context?) {
        if (context == null || context !is Activity) return
        try {
            val view = context.window.decorView
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }

    /**
     * 添加过滤的View
     */
    fun addFilterView(vararg views: View) {
        for (view in views) {
            mFilterViews.add(view)
        }
    }

    fun getFilterView(): List<View> {
        return mFilterViews
    }

    fun clearFilterView() {
        mFilterViews.clear()
        mTouchOutsideFunction = null
    }

    /**
     * 激活触摸软键盘外部的监听
     */
    fun invokeOnTouchOutsideListener(context: Context, ev: MotionEvent) {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            hideSoftInput(context)
        }
        mTouchOutsideFunction?.invoke(ev)
    }

    /**
     * 设置触摸软键盘外部的监听
     */
    fun setOnTouchOutsideListener(function: (ev: MotionEvent) -> Unit) {
        mTouchOutsideFunction = function
    }

    /**
     * 软件盘是否有显示
     */
    fun isSoftShowing(activity: Activity): Boolean {
        val height = activity.window.decorView.height
        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        return height * 2 / 3 > rect.bottom
    }


    /**
     * 获取键盘的高度
     */
    private const val KEY_SOFT_KEYBOARD_HEIGHT = "SoftKeyboardHeight"

    fun getSoftKeyboardHeight(activity: Activity): Int {
        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        //屏幕当前可见高度，不包括状态栏
        val displayHeight = rect.bottom - rect.top
        //屏幕可用高度
        val availableHeight = getAvailableScreenHeight(activity)
        //用于计算键盘高度
        val softInputHeight = availableHeight - displayHeight - getStatusBarHeight(activity)
        Log.e("TAG-di", displayHeight.toString() + "")
        Log.e("TAG-av", availableHeight.toString() + "")
        Log.e("TAG-so", softInputHeight.toString())
        if (softInputHeight != 0) {
            // 因为考虑到用户可能会主动调整键盘高度，所以只能是每次获取到键盘高度时都将其存储起来
            SpUtils.putInt(KEY_SOFT_KEYBOARD_HEIGHT, softInputHeight)
        }
        return softInputHeight
    }

    /**
     * 返回屏幕可用高度
     * 当显示了虚拟按键时，会自动减去虚拟按键高度
     */
    private fun getAvailableScreenHeight(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }


    /**
     * 状态栏高度
     */
    fun getStatusBarHeight(activity: Activity): Int {
        val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        return activity.resources.getDimensionPixelSize(resourceId)
    }

}