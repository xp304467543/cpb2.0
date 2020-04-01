package com.fenghuang.baselib.base.recycler.decorate

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.*
import java.util.*

/**
 * 线性分割线
 */
class DividerItemDecoration(
        color: Int = Color.GRAY,
        private var height: Int = 2,
        private val paddingLeft: Int = 0,
        private val paddingRight: Int = 0,
        backgroundColor: Int = Color.TRANSPARENT
) : RecyclerView.ItemDecoration() {

    private val mBackgroundColorDrawable: ColorDrawable = ColorDrawable(backgroundColor)
    private var mDividerColorDrawable: ColorDrawable = ColorDrawable(color)
    private var mDrawLastItem = true
    private var mDrawHeaderFooter = false
    private var mDividerFilterList: MutableList<DividerFilter>? = null

    private var mDrawFirstDivider = false
    private var mDrawLastDivider = false

    fun setDrawFirstDivider(isDraw: Boolean) {
        this.mDrawFirstDivider = isDraw
    }

    fun setDrawLatDivider(isDraw: Boolean) {
        this.mDrawLastDivider = isDraw
    }

    fun setDrawLastItem(mDrawLastItem: Boolean) {
        this.mDrawLastItem = mDrawLastItem
    }

    fun setDrawHeaderFooter(mDrawHeaderFooter: Boolean) {
        this.mDrawHeaderFooter = mDrawHeaderFooter
    }

    fun addDividerFilter(filter: DividerFilter?) {
        if (filter == null) {
            return
        }
        if (mDividerFilterList == null) {
            mDividerFilterList = ArrayList()
        }
        mDividerFilterList!!.add(filter)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        var orientation = 0

        val layoutManager = parent.layoutManager
        when (layoutManager) {
            is StaggeredGridLayoutManager -> orientation = layoutManager.orientation
            is GridLayoutManager -> orientation = layoutManager.orientation
            is LinearLayoutManager -> orientation = layoutManager.orientation
        }
        val drawHeight = getDrawHeight(position)
        if (position >= 0 && position < parent.adapter!!.itemCount || mDrawHeaderFooter) {
            if (orientation == OrientationHelper.VERTICAL) {
                if (position == 0 && mDrawFirstDivider) {
                    outRect.top = drawHeight
                }
                if (position != parent.adapter!!.itemCount - 1 || mDrawLastDivider) {
                    outRect.bottom = drawHeight
                }
            } else {
                if (position == 0 && mDrawFirstDivider) {
                    outRect.left = drawHeight
                }
                if (position != parent.adapter!!.itemCount - 1 || mDrawLastDivider) {
                    outRect.right = drawHeight
                }
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        if (parent.adapter == null) {
            return
        }

        var orientation = 0
        val dataCount: Int = parent.adapter!!.itemCount

        val layoutManager = parent.layoutManager
        //数据项除了最后一项
        //数据项最后一项
        when (layoutManager) {
            is StaggeredGridLayoutManager -> orientation = layoutManager.orientation
            is GridLayoutManager -> orientation = layoutManager.orientation
            is LinearLayoutManager -> orientation = layoutManager.orientation
        }
        val start: Int
        val end: Int
        if (orientation == OrientationHelper.VERTICAL) {
            start = parent.paddingLeft + paddingLeft
            end = parent.width - parent.paddingRight - paddingRight
        } else {
            start = parent.paddingTop + paddingLeft
            end = parent.height - parent.paddingBottom - paddingRight
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            val drawHeight = getDrawHeight(position)

            if (position >= 0 && position < dataCount - 1//数据项除了最后一项
                    || position == dataCount - 1 && mDrawLastItem//数据项最后一项
                    || position !in 0..(dataCount - 1) && mDrawHeaderFooter
            ) {

                if (orientation == OrientationHelper.VERTICAL) {
                    val params = child.layoutParams as RecyclerView.LayoutParams
                    val top = child.bottom + params.bottomMargin
                    val bottom = top + drawHeight
                    if (start > 0) {
                        mBackgroundColorDrawable.setBounds(0, top, start, bottom)
                        mBackgroundColorDrawable.draw(c)
                    }

                    mDividerColorDrawable.setBounds(start, top, end, bottom)
                    mDividerColorDrawable.draw(c)
                    if (end > 0) {
                        mBackgroundColorDrawable.setBounds(end, top, parent.width, bottom)
                        mBackgroundColorDrawable.draw(c)
                    }
                } else {
                    val params = child.layoutParams as RecyclerView.LayoutParams
                    val left = child.right + params.rightMargin
                    val right = left + drawHeight
                    if (start > 0) {
                        mBackgroundColorDrawable.setBounds(left, 0, right, end)
                        mBackgroundColorDrawable.draw(c)
                    }
                    mDividerColorDrawable.setBounds(left, start, right, end)
                    mDividerColorDrawable.draw(c)
                    if (end > 0) {
                        mBackgroundColorDrawable.setBounds(left, end, right, parent.height)
                        mBackgroundColorDrawable.draw(c)
                    }
                }
            }
        }
    }


    private fun getDrawHeight(position: Int): Int {
        // 分割线过滤，被过滤的位置不绘制分割线
        var drawHeight = height
        if (mDividerFilterList != null) {
            var skip = false
            for (filter in mDividerFilterList!!) {
                if (filter.skipDraw(position)) {
                    skip = true
                    break
                }
            }
            drawHeight = if (skip) 0 else height
        }
        return drawHeight
    }

    interface DividerFilter {
        fun skipDraw(position: Int): Boolean
    }

}