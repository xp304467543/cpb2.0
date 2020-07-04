package com.fenghuang.caipiaobao.ui.mine.children.report

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import kotlinx.android.synthetic.main.act_mine_report.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class MineReportPresenter : BaseMvpPresenter<MineReportAct>() {

    fun getNew(range:String){
        MineApi.getTeamReportLast(range) {
            onSuccess {
                if (mView.isActive()) {
                    mView.tv_r_t1.text = it.invitee_num
                    mView.tv_r_t2.text = it.recharge
                    mView.tv_r_t3.text = it.brokerage
                }
            }
        }
    }
}