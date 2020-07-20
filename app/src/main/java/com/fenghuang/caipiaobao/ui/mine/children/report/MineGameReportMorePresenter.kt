package com.fenghuang.caipiaobao.ui.mine.children.report

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.mine.data.MineApi

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/2
 * @ Describe
 *
 */
class MineGameReportMorePresenter : BaseMvpPresenter<MineGameReportMoreAct>() {

    fun getInfo(is_bl_play: String = "0", start: String, end: String) {
        MineApi.getGameLotteryInfo(is_bl_play, start, end) {
            onSuccess {
                if (mView.isActive()){
                    mView.lotteryAdapter?.clear()
                    if (it.isNullOrEmpty()){
                        mView.lotteryAdapter?.notifyDataSetChanged()
                        mView.setVisible(R.id.place_holder)
                    }else{
                        mView.setGone(R.id.place_holder)
                        mView.lotteryAdapter?.addAll(it)
                    }

                }
            }
            onFailed {
                ToastUtils.show("获取失败")
            }
        }
    }
}