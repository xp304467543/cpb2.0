package com.fenghuang.caipiaobao.ui.lottery.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.widget.round.RoundTextView
import com.fenghuang.caipiaobao.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/1- 13:49
 * @ Describe
 *
 */

class LotteryChildTypeAdapter(context: Context) : BaseRecyclerAdapter<String>(context) {

    var clickPosition: Int = 0

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
        return LotteryTypeHolder(parent)
    }

    inner class LotteryTypeHolder(parent: ViewGroup) : BaseViewHolder<String>(getContext(), parent, R.layout.holder_lottery_child_type) {
        override fun onBindData(data: String) {
            if (clickPosition == getDataPosition()) {
                findView<RoundTextView>(R.id.tvLotteryType).setTextColor(getColor(R.color.color_FF513E))
                findView<RoundTextView>(R.id.tvLotteryType).delegate.backgroundColor = getColor(R.color.color_FFECE8)
                findView<RoundTextView>(R.id.tvLotteryType).typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            } else {
                findView<RoundTextView>(R.id.tvLotteryType).setTextColor(getColor(R.color.color_333333))
                findView<RoundTextView>(R.id.tvLotteryType).delegate.backgroundColor = getColor(R.color.color_F5F7FA)
                findView<RoundTextView>(R.id.tvLotteryType).typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            }
            setText(R.id.tvLotteryType, data)
        }

    }


    fun changeBackground(position: Int) {
        clickPosition = position
        notifyDataSetChanged()
    }
}