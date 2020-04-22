package com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.data.PlaySecData

/**
 *
 * @ Author  QinTian
 * @ Date  2020/4/22
 * @ Describe
 *
 */
class GuessPlayChild2Adapter(context: Context) : BaseRecyclerAdapter<PlaySecData>(context) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<PlaySecData> {
        return GuessPlayChild2Holder(parent)
    }

    inner class GuessPlayChild2Holder(parent: ViewGroup) : BaseViewHolder<PlaySecData>(getContext(), parent, R.layout.item_guess_child2) {
        override fun onBindData(data: PlaySecData) {
            if (data.isSelected) {
                findView<LinearLayout>(R.id.gc_layout).setBackgroundResource( R.drawable.item_5corners_selected)
                setTextColor(R.id.tv_gc_name, ViewUtils.getColor(R.color.color_FF513E))
                setTextColor(R.id.tv_gc_odds, ViewUtils.getColor(R.color.color_FF513E))
            } else {
                findView<LinearLayout>(R.id.gc_layout).setBackgroundResource( R.drawable.item_5corners)
                setTextColor(R.id.tv_gc_name, ViewUtils.getColor(R.color.color_333333))
                setTextColor(R.id.tv_gc_odds, ViewUtils.getColor(R.color.color_999999))
            }
            setText(R.id.tv_gc_name, data.play_class_cname)
            setText(R.id.tv_gc_odds, data.play_odds.toString())
        }
    }
}