package com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter

import android.content.Context
import android.view.ViewGroup
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryBetHistoryResponse

/**
 *
 * @ Author  QinTian
 * @ Date  2020/4/20
 * @ Describe
 *
 */
class LiveRoomRecordAdapter(context: Context) : BaseRecyclerAdapter<LotteryBetHistoryResponse>(context) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LotteryBetHistoryResponse> {
        return LiveRoomRecordHolder(parent)
    }

    inner class LiveRoomRecordHolder(parent: ViewGroup) : BaseViewHolder<LotteryBetHistoryResponse>(getContext(), parent, R.layout.holder_bet_history) {
        override fun onBindData(data: LotteryBetHistoryResponse) {
            setText(R.id.tvBetTime, TimeUtils.longToDateString(data.play_bet_time ?: 0))
            setText(R.id.tvBetName, data.play_bet_lottery_name)
            setText(R.id.tvBetIssue, data.play_bet_issue + " 期")
            setText(R.id.tvBetCodeName, data.play_sec_name)
            setText(R.id.tvBetCode, data.play_class_name)
            setText(R.id.tvBetOdds, data.play_odds)
            setText(R.id.tvBetMoney, data.play_bet_score)
            if (data.play_bet_score?.contains("+")!!) {
                setTextColor(R.id.tvBetMoney, ViewUtils.getColor(R.color.color_FF513E))
            } else setTextColor(R.id.tvBetMoney, ViewUtils.getColor(R.color.color_333333))
        }
    }
}