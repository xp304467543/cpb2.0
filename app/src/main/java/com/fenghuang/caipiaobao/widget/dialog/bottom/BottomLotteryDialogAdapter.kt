package com.fenghuang.caipiaobao.widget.dialog.bottom

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.TextView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.caipiaobao.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-07
 * @ Describe 底部弹框
 *
 */

class BottomDialogAdapter(context: Context) : BaseRecyclerAdapter<BottomDialogBean>(context) {


    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BottomDialogBean> {

        return BottomDialogHolder(parent)
    }

    inner class BottomDialogHolder(parent: ViewGroup) : BaseViewHolder<BottomDialogBean>(getContext(), parent, R.layout.holder_lottery_child_rank) {
        override fun onBindData(data: BottomDialogBean) {

            if (data.isSelect) {
                findView<TextView>(R.id.tvLotteryRank).setTextColor(getColor(R.color.white))
                findView<TextView>(R.id.tvLotteryRank).background = getDrawable(R.drawable.button_background)
                findView<TextView>(R.id.tvLotteryRank).typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            } else {
                findView<TextView>(R.id.tvLotteryRank).setTextColor(getColor(R.color.color_333333))
                findView<TextView>(R.id.tvLotteryRank).background =getDrawable(R.drawable.button_grey_background)
                findView<TextView>(R.id.tvLotteryRank).typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            }
            setText(R.id.tvLotteryRank, data.str)

        }

    }

}

data  class  BottomDialogBean(var str: String,var isSelect:Boolean = true)