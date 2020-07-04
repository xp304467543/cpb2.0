package com.fenghuang.caipiaobao.ui.mine.children.report

import android.annotation.SuppressLint
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import kotlinx.android.synthetic.main.act_mine_game_report.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class MineGameReportPresenter : BaseMvpPresenter<MineGameReportAct>() {

    @SuppressLint("SetTextI18n")
    fun getReport(start: String, end: String) {
        MineApi.getGameReportLast(start, end) {
            onSuccess {
                if (mView.isActive()) {
                    mView.tvTotal1.text = it.amount
                    mView.tvTotal_1.text = it.prize
                    mView.tvTotal2.text = it.bl_amount
                    mView.tvTotal_2.text = it.bl_prize
                }
                onFailed {
                    ToastUtils.show("获取数据失败")
                }
            }
        }

        MineApi.getGameLottery(start, end) {
            onSuccess {
                if (mView.isActive()) {
                    mView.tv_lottery_1.text = it.count
                    mView.tv_lottery_2.text = it.amount
                    mView.tv_lottery_3.text = "￥ "+ it.prize
                    mView.tv_lottery_4.text = it.bl_count
                    mView.tv_lottery_5.text = it.bl_amount
                    mView.tv_lottery_6.text = "￥ "+it.bl_prize
                }
            }
        }
    }

}