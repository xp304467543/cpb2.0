package com.fenghuang.caipiaobao.ui.mine.children.report

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import kotlinx.android.synthetic.main.act_mine_game_report_more_info.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/3
 * @ Describe
 *
 */
class MineGameReportMoreInfoPresenter : BaseMvpPresenter<MineGameReportMoreInfoAct>() {

    fun getResponse(play_bet_state: Int,lotteryId:String,st:String="",et:String="") {
        val res = LotteryApi.getLotteryBetHistory(play_bet_state, mView.index,lotteryId,st,et)
        res.onSuccess {
            if (mView.isActive()) {
                mView.smBetRecord_1.finishLoadMore()
                mView.smBetRecord_1?.finishRefresh()
                if (!it.isNullOrEmpty()) {
                    ViewUtils.setVisible(mView.recordTop_1)
                    ViewUtils.setGone(mView.tvBetRecordHolder_1)
                    mView.adapter?.addAll(it)
                } else {
                    if (mView.index == 1) {
                        mView.adapter?.clear()
                        mView.rvGameReportInfo?.removeAllViews()
                        ViewUtils.setVisible(mView.tvBetRecordHolder_1)
                        mView.smBetRecord_1?.setEnableAutoLoadMore(false)
                        mView.smBetRecord_1?.setEnableRefresh(false)
                    } else {
                        mView.index--
                        mView.smBetRecord_1?.setEnableAutoLoadMore(false)
                    }
                }
            }
        }
        res.onFailed {
            if (mView.isActive()) {
                mView.smBetRecord_1?.finishLoadMore()
                mView.smBetRecord_1?.finishRefresh()
                ToastUtils.showError(it.getMsg())

            }
        }

    }
}