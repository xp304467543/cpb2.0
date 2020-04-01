package com.fenghuang.baselib.base.recycler.decorate

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 线性分割线，只试用与数据一次性填充的列表，不适用与聊天室这种数据一条一条添加的情景
 */
class VerticalItemSpaceDecoration(
        private val itemSpace: Int,
        private val topAndBottomSpace: Int = itemSpace,
        private val startAndEndSpace: Int = itemSpace
) : RecyclerView.ItemDecoration() {


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        when (itemPosition) {
            0 -> {  // 第一个
                outRect.top = topAndBottomSpace
                outRect.bottom = itemSpace / 2
            }
            state.itemCount - 1 -> {  // 最后一个
                outRect.top = itemSpace / 2
                outRect.bottom = topAndBottomSpace
            }
            else -> {
                outRect.top = itemSpace / 2
                outRect.bottom = itemSpace / 2
            }
        }
        outRect.left = startAndEndSpace
        outRect.right = startAndEndSpace
    }

}