package com.fenghuang.caipiaobao.ui.lottery.adapter

import android.content.Context
import android.view.ViewGroup
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryExpertPaleyResponse
import com.fenghuang.caipiaobao.utils.LaunchUtils
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe
 *
 */

class LotteryExpertPlanFragmentAdapter(context: Context) : BaseRecyclerAdapter<LotteryExpertPaleyResponse>(context) {

    var isShowFooter = false

    internal enum class ITEM_TYPE {
        HEADER,
        FOOTER,
        NORMAL
    }

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LotteryExpertPaleyResponse> {

        return if (viewType === ITEM_TYPE.FOOTER.ordinal) {
            FooterHolder(parent)
        } else LotteryExpertPlanFragmentHolder(parent)
    }


    inner class LotteryExpertPlanFragmentHolder(parent: ViewGroup) : BaseViewHolder<LotteryExpertPaleyResponse>(getContext(), parent, R.layout.holder_lottery_child_expert_plan) {
        override fun onBindData(data: LotteryExpertPaleyResponse) {
            ImageManager.loadImg(data.avatar, findView(R.id.expertPlanAvatar))
            setText(R.id.rtvExpertWinPresent, "胜率  " + BigDecimal(data.hit_rate).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal(100)).stripTrailingZeros().toPlainString() + "%")
            setText(R.id.rtvExpertWinAdd, data.winning + "  连红")
            setText(R.id.rtvExpertWinProfit, "盈利  " + BigDecimal(data.profit_rate).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal(100)).stripTrailingZeros().toPlainString() + "%")
            setText(R.id.tvExpertPlanType, data.method)
            setText(R.id.tvExpertPlanName, data.nickname)
            setText(R.id.tvExpertIssue, data.issue + " 期")
            val str = StringBuilder()
            for (result in data.code.split(",")) {
                str.append("$result    ")
            }
            setText(R.id.tvTenList, str)
            if (data.last_10_games != null) {
                for ((index, it) in data.last_10_games!!.withIndex()) {
                    val color = if (it == "1") {
                        R.color.color_FF513E
                    } else R.color.color_999999
                    val text = if (it == "1") {
                        "胜"
                    } else "负"

                    when (index) {
                        0 -> {
                            setText(R.id.tvCode_0, text)
                            setTextColor(R.id.tvCode_0, ViewUtils.getColor(color))
                        }
                        1 -> {
                            setText(R.id.tvCode_1, text)
                            setTextColor(R.id.tvCode_1, ViewUtils.getColor(color))
                        }
                        2 -> {
                            setText(R.id.tvCode_2, text)
                            setTextColor(R.id.tvCode_2, ViewUtils.getColor(color))
                        }
                        3 -> {
                            setText(R.id.tvCode_3, text)
                            setTextColor(R.id.tvCode_3, ViewUtils.getColor(color))
                        }
                        4 -> {
                            setText(R.id.tvCode_4, text)
                            setTextColor(R.id.tvCode_4, ViewUtils.getColor(color))
                        }
                        5 -> {
                            setText(R.id.tvCode_5, text)
                            setTextColor(R.id.tvCode_5, ViewUtils.getColor(color))
                        }
                        6 -> {
                            setText(R.id.tvCode_6, text)
                            setTextColor(R.id.tvCode_6, ViewUtils.getColor(color))
                        }
                        7 -> {
                            setText(R.id.tvCode_7, text)
                            setTextColor(R.id.tvCode_7, ViewUtils.getColor(color))
                        }
                        8 -> {
                            setText(R.id.tvCode_8, text)
                            setTextColor(R.id.tvCode_8, ViewUtils.getColor(color))
                        }
                        9 -> {
                            setText(R.id.tvCode_9, text)
                            setTextColor(R.id.tvCode_9, ViewUtils.getColor(color))
                        }
                    }
                }
            }
            setOnClick(R.id.expertPlanAvatar)
        }

        override fun onClick(id: Int) {
            when (id) {
                R.id.expertPlanAvatar -> LaunchUtils.startPersonalPage(getContext(), getData()?.expert_id!!, 3, getData()?.lottery_id
                        ?: "-1")
            }
        }
    }

    inner class FooterHolder(parent: ViewGroup) : BaseViewHolder<LotteryExpertPaleyResponse>(getContext(), parent, R.layout.recycle_foot) {
        override fun onBindData(data: LotteryExpertPaleyResponse) {}
    }


    override fun getItemCount(): Int {
        return getAllData().size
    }


    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1 && isShowFooter) {
            //最后一个,应该加载Footer
            return ITEM_TYPE.FOOTER.ordinal
        }
        return ITEM_TYPE.NORMAL.ordinal
    }

}