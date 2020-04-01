package com.fenghuang.baselib.base.recycler.decorate

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalItemSpaceDecoration(
        private val itemSpace: Int,
        private val startAndEndSpace: Int = itemSpace,
        private val topAndBottom: Int = itemSpace
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        when (itemPosition) {
            0 -> {  // 第一个
                outRect.left = startAndEndSpace
                outRect.right = itemSpace / 2
            }
            state.itemCount - 1 -> {  // 最后一个
                outRect.left = itemSpace / 2
                outRect.right = startAndEndSpace
            }
            else -> {
                outRect.left = itemSpace / 2
                outRect.right = itemSpace / 2
            }
        }
        outRect.top = topAndBottom
        outRect.bottom = topAndBottom
    }

}