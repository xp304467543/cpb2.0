package com.fenghuang.baselib.base.recycler.decorate

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * 竖直的瀑布列表
 */
class GridItemSpaceDecoration(
        private val spanCount: Int,
        private val itemSpace: Int,
        private val topAndBottomSpace: Int = itemSpace,
        private val startAndEndSpace: Int = itemSpace
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildLayoutPosition(view) // item position
        val column = position % spanCount // item column
        if (position < spanCount) { // top edge
            // 第一行
            outRect.top = topAndBottomSpace
        } else {
            outRect.top = itemSpace / 2
        }

        // 判断


        if (isLastSpan(parent, position)) {
            // 最后一行
            outRect.bottom = topAndBottomSpace // item bottom
        } else {
            outRect.bottom = itemSpace / 2 // item bottom
        }

        if (position % spanCount == 0) {
            // 第一列
            outRect.left = startAndEndSpace
        } else {
            outRect.left = itemSpace - column * itemSpace / spanCount // spacing - column * ((1f / spanCount) * spacing)
        }

        if (position % spanCount == spanCount - 1) {
            // 最后一列
            outRect.right = startAndEndSpace
        } else {
            outRect.right = (column + 1) * itemSpace / spanCount // (column + 1) * ((1f / spanCount) * spacing)
        }
    }

    private fun isLastSpan(parent: RecyclerView, position: Int): Boolean {
        val row = position / spanCount
        val itemCount = parent.adapter?.itemCount ?: 0
        return itemCount / spanCount == row
    }
}