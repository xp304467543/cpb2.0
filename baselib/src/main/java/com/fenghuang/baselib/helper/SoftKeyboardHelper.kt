package com.fenghuang.baselib.helper

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import com.fenghuang.baselib.utils.ViewUtils


/**
 * 键盘弹起和收起助手
 */
class SoftKeyboardHelper : ViewTreeObserver.OnGlobalLayoutListener {

    private var lastSoftKeyboardHeightInPx = 0
    private var isSoftKeyboardOpened = false
    private var mRootView: View? = null
    private var onSoftKeyboardChanged: ((keyboardHeightInPx: Int) -> Unit)? = null
    private var onSoftKeyboardOpened: ((keyboardHeightInPx: Int) -> Unit)? = null
    private var onSoftKeyboardClosed: (() -> Unit)? = null
    private var mContext: Context? = null

    fun registerFragment(fragment: Fragment): SoftKeyboardHelper {
        mContext = fragment.activity
        return registerView(fragment.view)
    }

    fun registerActivity(activity: Activity): SoftKeyboardHelper {
        return registerView(activity.window.decorView.findViewById(android.R.id.content))
    }

    fun registerView(view: View?): SoftKeyboardHelper {
        mRootView = view
        view?.viewTreeObserver?.addOnGlobalLayoutListener(this)
        return this
    }

    fun unregisterView() {
        mRootView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        mRootView?.apply {
            val r = Rect()
            // r will be populated with the coordinates of your view that area still visible.
            this.getWindowVisibleDisplayFrame(r)

            var bottomHeight = 0
            var heightDiff = 0
            val context = mContext
            if (context is Activity) {
                bottomHeight = ViewUtils.getBottomNavigationBarHeight(context)
            }
            val screenHeight = (context as Activity).window.decorView.rootView.height
            heightDiff = screenHeight - r.bottom
            Log.i("mRootView", "Size: $heightDiff")
//            if (isSoftKeyboardOpened && bottomHeight < 100) {
//                heightDiff = this.rootView.height - r.bottom - StatusBarUtils.getStatusBarHeight() + bottomHeight
//            } else if (isSoftKeyboardOpened && heightDiff < 750) {
//                heightDiff = this.rootView.height - r.bottom - StatusBarUtils.getStatusBarHeight() + 80
//            } else {
//                heightDiff = this.rootView.height - r.bottom - StatusBarUtils.getStatusBarHeight() - 30
//            }
            onSoftKeyboardChanged?.invoke(heightDiff)
//            Log.i("mRootView", "r.top = " + r.top)
//            Log.i("mRootView", "StatusBarUtils = " + StatusBarUtils.getStatusBarHeight())
//            Log.i("mRootView", "bottomHeight = " + bottomHeight)
//            Log.i("mRootView", "r.bottom = " + r.bottom)
//            Log.i("mRootView", "rootView = " + rootView.height)
//            Log.i("mRootView", "heightDiff = " + heightDiff)
            if (!isSoftKeyboardOpened && heightDiff > 300) { // if more than 300 pixels, its probably a keyboard...
                isSoftKeyboardOpened = true
                onSoftKeyboardOpened?.invoke(heightDiff)
            } else if (isSoftKeyboardOpened && heightDiff < 300) {
                isSoftKeyboardOpened = false
                onSoftKeyboardClosed?.invoke()
            }
        }
    }

    fun setSoftKeyboardOpenListener(listener: (keyboardHeightInPx: Int) -> Unit): SoftKeyboardHelper {
        onSoftKeyboardOpened = listener
        return this
    }

    fun setSoftKeyboardClosedListener(listener: () -> Unit): SoftKeyboardHelper {
        onSoftKeyboardClosed = listener
        return this
    }

    fun setSoftKeyboardChangeListener(listener: (keyboardHeightInPx: Int) -> Unit): SoftKeyboardHelper {
        onSoftKeyboardChanged = listener
        return this
    }

    /**
     * Default value is zero (0)
     *
     * @return last saved keyboard height in px
     */
    fun getSoftKeyboardHeightInpx(): Int {
        return lastSoftKeyboardHeightInPx
    }

    /**
     * 软键盘是否在展示
     */
    fun isSoftKeyboardOpened(): Boolean {
        return isSoftKeyboardOpened
    }

    //获取是否存在NavigationBar
    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {

        }
        return hasNavigationBar

    }
}