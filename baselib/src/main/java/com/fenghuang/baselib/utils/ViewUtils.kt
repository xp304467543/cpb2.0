package com.fenghuang.baselib.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.drawable.DrawableCompat
import com.fenghuang.baselib.theme.UiUtils
import com.google.android.material.tabs.TabLayout


/**
 * 与View相关的工具类
 */
object ViewUtils {

    private val TAG: String = ViewUtils::class.java.simpleName

    /**
     * 获取全局的Context
     */
    fun getContext(): Context {
        return AppUtils.getContext()
    }

    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        wm.defaultDisplay.getSize(size)
        return size.x
    }

    /**
     * [TextView]
     * set TextView content. view is not null and content is not null.
     */
    fun setText(view: TextView?, content: String) {
        if (view != null) {
            if (TextUtils.isEmpty(content)) {
                setGone(view)
            } else {
                setVisible(view)
                view.text = content
            }
        } else {
            Log.e(TAG, "setText Warning： TextView is null ")
        }
    }

    /**
     * [View.VISIBLE]
     * safe set view visible.
     */
    fun setVisible(view: View?) {
        if (view != null) {
            if (view.visibility != View.VISIBLE) {
                setVisibility(view, View.VISIBLE)
            }
        } else {
            Log.e(TAG, "setVisible Warning： View is null ")
        }
    }

    /**
     * [View.GONE]
     * safe set view gone.
     */
    fun setGone(view: View?) {
        if (view != null) {
            if (view.visibility != View.GONE) {
                setVisibility(view, View.GONE)
            }
        } else {
            Log.e(TAG, "setGone Warning： View is null ")
        }
    }

    /**
     * [View.INVISIBLE]
     * safe set view invisible.
     */
    fun setInvisible(view: View?) {
        if (view != null) {
            setVisibility(view, View.INVISIBLE)
        } else {
            Log.e(TAG, "setInvisible Warning： View is null ")
        }
    }

    /**
     * safe set view visible,gone or invisible.
     */
    private fun setVisibility(view: View?, visibility: Int) {
        if (view != null) {
            if (visibility == View.VISIBLE) {
                if (view.visibility != View.VISIBLE) {
                    view.visibility = visibility
                }
            } else if (visibility == View.GONE) {
                if (view.visibility != View.GONE) {
                    view.visibility = visibility
                }
            } else {
                view.visibility = visibility
            }
        } else {
            Log.e(TAG, "setVisibility Warning： View is null ")
        }
    }

    fun isVisible(view: View?): Boolean {
        return view != null && view.visibility == View.VISIBLE
    }


    /**
     * set view visible or gone
     */
    fun setVisible(view: View?, isVisible: Boolean) {
        if (isVisible) {
            setVisible(view)
        } else {
            setGone(view)
        }
    }


    /**
     * set TextView content with CharSequence.
     */
    fun setText(view: TextView?, text: CharSequence) {
        if (view != null) {
            view.text = text
        } else {
            Log.e(TAG, "setText Warning： TextView is null ")
        }
    }

    /**
     * set TextView content with resId.
     */
    fun setText(view: TextView?, @StringRes resId: Int) {
        if (view != null && view.context != null) {
            view.text = view.context.getString(resId)
        } else {
            Log.e(TAG, "setText Warning： TextView is null ")
        }
    }

    /**
     * view is enabled.
     */
    fun isEnabled(view: View?): Boolean {
        return view != null && view.isEnabled
    }

    /**
     * view is selected.
     */
    fun isSelected(view: View?): Boolean {
        return view != null && view.isSelected
    }

    /**
     * view is not enabled.
     */
    fun isNotEnable(view: View?): Boolean {
        return view != null && !view.isEnabled
    }

    /**
     * set view is enabled or not isEnable.
     */
    fun setEnabled(view: View?, isEnable: Boolean) {
        if (view != null) {
            view.isEnabled = isEnable
        }
    }

    /**
     * 获取String资源集合
     */
    fun getStringArray(@ArrayRes id: Int): Array<String> {
        return AppUtils.getStringArray(id)
    }

    /**
     * 获取String资源集合
     */
    fun getString(@StringRes id: Int): String {
        return AppUtils.getString(id) ?: ""
    }

    /**
     * 获取Drawable对象
     */
    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return AppUtils.getDrawable(id)
    }


    /**
     * copy string to clipboard
     */
    fun copyText(text: String) {
        val clipboard = getContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val myClip: ClipData = ClipData.newPlainText("text", text)
        clipboard.setPrimaryClip(myClip)
    }

    /**
     * paste string to edit
     */
    fun pasteText(): String? {
        val clipboard = getContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        return if (clip != null && clip.itemCount > 0) {
            clip.getItemAt(0).coerceToText(getContext()).toString()
        } else null
    }

    /**
     * determine Activity is Port.
     */
    fun isPort(): Boolean {
        return getContext().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    fun isLand(): Boolean {
        return getContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * dp to px
     */
    fun dp2px(dipValue: Int): Int {
        val scale = getContext().resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    fun dp2px(dipValue: Float): Float {
        val scale = getContext().resources.displayMetrics.density
        return dipValue * scale + 0.5f
    }

    /**
     * px to dip
     */
    fun px2dp(pxValue: Int): Int {
        val scale = getContext().resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun px2dp(pxValue: Float): Float {
        val scale = getContext().resources.displayMetrics.density
        return pxValue / scale + 0.5f
    }

    /**
     * px to sp
     */
    fun px2sp(pxValue: Int): Int {
        val fontScale = getContext().resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun px2sp(pxValue: Float): Float {
        val fontScale = getContext().resources.displayMetrics.scaledDensity
        return pxValue / fontScale + 0.5f
    }

    /**
     * sp to px
     */
    fun sp2px(spValue: Int): Int {
        val fontScale = getContext().resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun sp2px(spValue: Float): Float {
        val fontScale = getContext().resources.displayMetrics.scaledDensity
        return spValue * fontScale + 0.5f
    }


    /**
     * 获取颜色
     */
    fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(getContext(), id)
    }

    /**
     * screen height
     */
    fun getScreenHeight(): Int {
        val height: Int
        val dm = DisplayMetrics()
        val windowMgr = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowMgr.defaultDisplay.getRealMetrics(dm)
        height = dm.heightPixels
        return height
    }

    /**
     * screen width
     */
    fun getScreenWidth(): Int {
        val width: Int
        val dm = DisplayMetrics()
        val windowMgr = getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowMgr.defaultDisplay.getRealMetrics(dm)
        width = dm.widthPixels
        return width
    }


    /**
     * 设置字体样式
     *
     * @param textView
     * @param path
     */
    fun setTypeFace(textView: TextView, path: String) {
        val typeface = Typeface.createFromAsset(textView.context.assets, path)
        textView.typeface = typeface
    }

    /**
     * 获取状态栏的高度
     */
    fun getStatusHeight(): Int {
        var statusBarHeight = -1
        val resourceId = getContext().resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = getContext().resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    /**
     * 底部虚拟按键栏的高度
     */
    fun getBottomNavigationBarHeight(activity: Activity): Int {
        val metrics = DisplayMetrics()
        // 这个方法获取可能不是真实屏幕的高度
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val usableHeight = metrics.heightPixels
        // 获取当前屏幕的真实高度
        activity.windowManager.defaultDisplay.getRealMetrics(metrics)
        val realHeight = metrics.heightPixels
        return if (realHeight > usableHeight) {
            realHeight - usableHeight
        } else {
            0
        }
    }


    // ---------------------- view的多次点击 --------------------

    private const val MIN_DELAY_TIME = 500
    private var lastClickTime = 0L
    /**
     * 是不是第一次点击，屏蔽多次点击
     */
    fun isFastClick(): Boolean {
        var flag = true
        val currentClickTime = System.currentTimeMillis()
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false
        }
        lastClickTime = currentClickTime
        return flag
    }


    // ---------------------------TabLayout的处理-------------------------
    fun setTabLayoutTextStyle(tabLayout: TabLayout?) {
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                updateTabLayoutView(tab, false)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                updateTabLayoutView(tab, true)
            }
        })
    }


    /**
     * 修正Tab选中时加粗
     */
    fun updateTabLayoutView(tab: TabLayout.Tab?, isSelect: Boolean) {
        tab?.apply {
            if (isSelect) {
                try {
                    val fieldView = tab.javaClass.getDeclaredField("view")
                    fieldView.isAccessible = true
                    val view = fieldView.get(tab) as View
                    val fieldTxt = view.javaClass.getDeclaredField("textView")
                    fieldTxt.isAccessible = true
                    val tabSelect = fieldTxt.get(view) as TextView
                    tabSelect.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    tabSelect.text = tab.text
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                try {
                    val fieldView = tab.javaClass.getDeclaredField("view")
                    fieldView.isAccessible = true
                    val view = fieldView.get(tab) as View
                    val fieldTxt = view.javaClass.getDeclaredField("textView")
                    fieldTxt.isAccessible = true
                    val tabSelect = fieldTxt.get(view) as TextView
                    tabSelect.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    tabSelect.text = tab.text
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 设置主题颜色的Drawable
     */
    fun setImageThemeDrawable(imageView: ImageView?, resId: Int, attrRes: Int) {
        var drawable = getDrawable(resId)?.mutate()
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable)
            imageView?.let {
                DrawableCompat.setTint(drawable, UiUtils.getThemeColor(imageView.context, attrRes))
                it.setImageDrawable(drawable)
            }
        }
    }

    /**
     * 获取主题颜色的Drawable
     */
    fun getImageThemeDrawable(context: Context?, resId: Int, attrRes: Int): Drawable? {
        var drawable = getDrawable(resId)?.mutate()
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable)
            context?.let {
                //                DrawableCompat.setTint(drawable, UiUtils.getThemeColor(context, attrRes))
            }
        }
        return drawable
    }

}