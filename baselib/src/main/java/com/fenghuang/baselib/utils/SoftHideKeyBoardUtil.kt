package com.fenghuang.baselib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.widget.FrameLayout

/**
 *  author : Peter
 *  date   : 2019/9/3 17:11
 *  desc   : 解决键盘挡住输入框
 */
class SoftHideKeyBoardUtil {

    private lateinit var mChildOfContent: View
    private var usableHeightPrevious: Int = 0
    // 为适应华为小米等手机键盘上方出现黑条或不适配
    private lateinit var frameLayoutParams: FrameLayout.LayoutParams
    // 获取setContentView本来view的高度
    private var contentHeight: Int = 0
    // 只用获取一次
    private var isfirst = true
    // 状态栏高度
    private val statusBarHeight: Int = 0

    fun init(activity: Activity) {
        val content = activity.findViewById<View>(android.R.id.content) as FrameLayout
        mChildOfContent = content.getChildAt(0)
        mChildOfContent.viewTreeObserver.addOnGlobalLayoutListener {
            if (isfirst) {
                contentHeight = mChildOfContent.height
                isfirst = false
            }
            // 5､当前布局发生变化时，对Activity的xml布局进行重绘
            possiblyResizeChildOfContent()
        }
        frameLayoutParams = mChildOfContent.layoutParams as FrameLayout.LayoutParams
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            val usableHeightSansKeyboard = mChildOfContent.rootView.height
            val heightDifference = usableHeightSansKeyboard - usableHeightNow
            if (heightDifference > usableHeightSansKeyboard / 4) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + StatusBarUtils.getStatusBarHeight()
                } else {
                    frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
                }
            } else {
                frameLayoutParams.height = contentHeight
            }  //7､ 重绘Activity的xml布局
            mChildOfContent.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        val r = Rect()
        // 全屏模式下：直接返回r.bottom，r.top其实是状态栏的高度
        mChildOfContent.getWindowVisibleDisplayFrame(r)


        return r.bottom - r.top
    }


}