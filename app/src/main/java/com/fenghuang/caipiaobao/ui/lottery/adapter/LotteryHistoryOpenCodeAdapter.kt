package com.fenghuang.caipiaobao.ui.lottery.adapter

import android.content.Context
import android.view.ViewGroup
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryConstant
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryTypeSelectUtil
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryCodeHistoryResponse
import java.util.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/3- 12:09
 * @ Describe 历史开奖配器
 *
 */

class LotteryHistoryOpenCodeAdapter(context: Context, val lotteryId: String, type: String) : BaseRecyclerAdapter<LotteryCodeHistoryResponse>(context) {


    private var typeSelect = type

    var isShowFooter = false

    internal enum class ITEM_TYPE {
        HEADER,
        FOOTER,
        NORMAL
    }

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LotteryCodeHistoryResponse> {
        return if (viewType === ITEM_TYPE.FOOTER.ordinal) {
            FooterHolder(parent)
        } else HomeMoreLiveHolder(parent)
    }

    inner class HomeMoreLiveHolder(parent: ViewGroup) : BaseViewHolder<LotteryCodeHistoryResponse>(getContext(), parent, R.layout.holder_lottery_history) {
        override fun onBindData(data: LotteryCodeHistoryResponse) {
            if (data.issue != "") setText(R.id.tvOpenCount, data.issue + "期")
            if (data.input_time != "" && data.input_time != "0") setText(R.id.tvOpenTime, TimeUtils.initData(data.input_time))
            when (lotteryId) {
                "8" -> {
                    val tbList = data.code.split(",") as ArrayList<String>
                    tbList.add(6, "+")
                    LotteryTypeSelectUtil.addOpenCode(getContext()!!, findView(R.id.codeContainer), tbList, lotteryId)
                    when (typeSelect) {
                        LotteryConstant.TYPE_5 -> LotteryTypeSelectUtil.specialLotterySum(getContext()!!, findView(R.id.codeContainer), data.code.split(","))
                        LotteryConstant.TYPE_6 -> LotteryTypeSelectUtil.lotterySpecialCode(getContext()!!, findView(R.id.codeContainer), data.code.split(","))
                    }
                }
                else -> {
                    when (typeSelect) {
                        LotteryConstant.TYPE_1 -> LotteryTypeSelectUtil.addOpenCode(getContext()!!, findView(R.id.codeContainer), data.code.split(","), lotteryId)
                        LotteryConstant.TYPE_2 -> LotteryTypeSelectUtil.addOpenCodeBigger(getContext()!!, findView(R.id.codeContainer), data.code.split(","), lotteryId)
                        LotteryConstant.TYPE_3 -> LotteryTypeSelectUtil.addOpenCodeSingle(getContext()!!, findView(R.id.codeContainer), data.code.split(","))
                        LotteryConstant.TYPE_4 -> LotteryTypeSelectUtil.addSumAndDragonAndTiger(getContext()!!, findView(R.id.codeContainer), data.code.split(","))
                        LotteryConstant.TYPE_7 -> LotteryTypeSelectUtil.addOpenCodeTypeAndSum(getContext()!!, findView(R.id.codeContainer), data.code.split(","))
                    }
                }
            }
        }
    }

    inner class FooterHolder(parent: ViewGroup) : BaseViewHolder<LotteryCodeHistoryResponse>(getContext(), parent, R.layout.recycle_foot) {
        override fun onBindData(data: LotteryCodeHistoryResponse) {
        }
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


    fun changeDiffType(types: String) {
        this.typeSelect = types
        notifyDataSetChanged()
    }


}