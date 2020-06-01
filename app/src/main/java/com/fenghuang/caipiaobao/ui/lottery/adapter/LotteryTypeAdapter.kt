package com.fenghuang.caipiaobao.ui.lottery.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.TextView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryTypeResponse

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/1- 13:49
 * @ Describe
 *
 */

class LotteryTypeAdapter(context: Context) : BaseRecyclerAdapter<LotteryTypeResponse>(context) {

    var clickPosition: Int = 0

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LotteryTypeResponse> {
        return LotteryTypeHolder(parent)
    }

    inner class LotteryTypeHolder(parent: ViewGroup) : BaseViewHolder<LotteryTypeResponse>(getContext(), parent, R.layout.holder_lottery_type) {
        override fun onBindData(data: LotteryTypeResponse) {
            if (clickPosition == getDataPosition()) {
                findView<TextView>(R.id.tvLotteryType).setTextColor(getColor(R.color.white))
                if (UserInfoSp.getSkinSelect() == 3) {
                    findView<TextView>(R.id.tvLotteryType).setBackgroundResource(R.drawable.button_green_background)
                } else findView<TextView>(R.id.tvLotteryType).setBackgroundResource(R.drawable.button_background)
                findView<TextView>(R.id.tvLotteryType).typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            } else {
                findView<TextView>(R.id.tvLotteryType).setTextColor(getColor(R.color.color_999999))
                findView<TextView>(R.id.tvLotteryType).setBackgroundResource(R.drawable.button_grey_background)
                findView<TextView>(R.id.tvLotteryType).typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            }


            setText(R.id.tvLotteryType, data.cname)
        }

    }


    fun changeBackground(position: Int) {
        clickPosition = position
        notifyDataSetChanged()
    }
}