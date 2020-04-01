package com.fenghuang.caipiaobao.widget.viewpager

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.google.android.material.widget.XViewPager


/**
 * 解决滑动冲突的 ViewPager
 * 因为 ViewPager 和 SideslipDeleteLayout 都是水平方向滑动的控件。
 * 所以在一起使用时会有冲突，使用本控件(ResolveConflictViewPager)，可以在ViewPager的第一页使用左滑。在ViewPager的最后一页使用右滑菜单。
 */
open class ResolveConflictViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : XViewPager(
        context,
        attrs
) {

    private var mLastX: Int = 0
    private var mLastY: Int = 0

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val x = ev?.x?.toInt()
        val y = ev?.y?.toInt()
        var intercept = false
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> if (isHorizontalScroll(x!!, y!!)) {
                //除了在第一页的手指向右滑 ， 最后一页的左滑，其他时刻都是父控件需要拦截事件
                if (isReactFirstPage() && isScrollRight(x)) {
//                    Log.e("---------------#", "第一页的手指向右滑]")
                    intercept = false
                } else if ((isReachLastPage() && isScrollLeft(x))) {
                    intercept = false
//                    Log.e("---------------#", "最后一页的手指向左边滑]")
                }
            }
            MotionEvent.ACTION_UP -> {
            }
            else -> {
            }
        }

        if (x != null) {
            mLastX = x
        }
        if (y != null) {
            mLastY = y
        }

        return intercept || super.onInterceptTouchEvent(ev)
    }

    /**
     * 是否在水平滑动
     */
    private fun isHorizontalScroll(x: Int, y: Int): Boolean {
        return Math.abs(y - mLastY) < Math.abs(x - mLastX)
    }

    /**
     * 是否未到达最后一页
     */
    private fun isReachLastPage(): Boolean {
        val adapter = adapter
        return null != adapter && adapter.count - 1 == currentItem
    }

    /**
     * 是否在第一页
     */
    private fun isReactFirstPage(): Boolean {
        return currentItem == 0
    }

    /**
     * 是否左滑
     */
    private fun isScrollLeft(x: Int): Boolean {
        return x - mLastX < 0
    }

    /**
     * 是否右滑
     */
    private fun isScrollRight(x: Int): Boolean {
        return x - mLastX > 0
    }

}
