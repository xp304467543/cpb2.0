package com.fenghuang.caipiaobao.ui.mine.children.report

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import kotlinx.android.synthetic.main.fragment_report_2.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment2P : BaseMvpPresenter<ReportFragment2>() {

    fun getVip(page: Int) {
        MineApi.getVipLevel(page = page) {
            onSuccess {
                if (mView.isActive()) {
                    if (page == 1) {
                        if (it.isNullOrEmpty()) {
                            mView.adapter?.clear()
                            mView.rvVip.removeAllViews()
                            mView.setVisible(mView.vipHolder)
                        } else {
                            mView.setGone(mView.vipHolder)
                            mView.adapter?.clear()
                            mView.adapter?.addAll(it)
                            mView.page++
                        }
                    }else{
                        if (!it.isNullOrEmpty()) {
                            mView.adapter?.addAll(it)
                            mView.page++
                        }
                    }
                    mView.smartVip.finishRefresh()
                    mView.smartVip.finishLoadMore()
                }
            }
            onFailed {
                if (mView.isActive()){
                    mView.smartVip.finishRefresh()
                    mView.smartVip.finishLoadMore()
                   if (mView.page == 1){
                       mView.setVisible(mView.vipHolder)
                       mView.vipHolder.text = "暂无会员信息"
                   }
                }
            }
        }
    }
}