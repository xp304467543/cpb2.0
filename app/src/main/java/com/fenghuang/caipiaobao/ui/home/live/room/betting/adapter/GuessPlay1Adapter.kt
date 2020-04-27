package com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.data.PlaySecData

/**
 *
 * @ Author  QinTian
 * @ Date  2020/4/22
 * @ Describe
 *
 */
class GuessPlay1Adapter(val list: List<PlaySecData>) : BaseQuickAdapter<PlaySecData, BaseViewHolder>(R.layout.item_guess_child2, list) {

    override fun convert(helper: BaseViewHolder, item: PlaySecData) {
        if (item.isSelected) {
            helper.setBackgroundRes(R.id.gc_layout, R.drawable.item_5corners_selected)
                    .setTextColor(R.id.tv_gc_name, ContextCompat.getColor(mContext, R.color.color_FF513E))
                    .setTextColor(R.id.tv_gc_odds, ContextCompat.getColor(mContext, R.color.color_FF513E))
        } else {
            helper.setBackgroundRes(R.id.gc_layout, R.drawable.item_5corners)
                    .setTextColor(R.id.tv_gc_name, ContextCompat.getColor(mContext, R.color.color_333333))
                    .setTextColor(R.id.tv_gc_odds, ContextCompat.getColor(mContext, R.color.color_999999))
        }
        helper.setText(R.id.tv_gc_name, item.play_class_cname)
        helper.setText(R.id.tv_gc_odds, item.play_odds.toString())
    }
}


