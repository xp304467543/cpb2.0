package com.fenghuang.caipiaobao.ui.mine.children.report

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import kotlinx.android.synthetic.main.fragment_report_3.*
import kotlinx.android.synthetic.main.fragment_report_3.vipHolder

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment3P : BaseMvpPresenter<ReportFragment3>() {

    fun getNextVip(page: Int) {
        MineApi.getVipLevel(is_sub = 1, page = page) {
            onSuccess {
                if (mView.isActive()) {
                    if (page == 1) {
                        if (it.isNullOrEmpty()) {
                            mView.adapter?.clear()
                            mView.rvReport3.removeAllViews()
                            mView.setVisible(mView.vipHolder)
                        } else {
                            mView.setGone(mView.vipHolder)
                            mView.adapter?.clear()
                            mView.adapter?.addAll(it)
                            mView.page++
                        }
                    } else {
                        if (!it.isNullOrEmpty()) {
                            mView.adapter?.addAll(it)
                            mView.page++
                        }
                    }
                    mView.smartVip3.finishRefresh()
                    mView.smartVip3.finishLoadMore()
                }
            }
            onFailed {
                if (mView.isActive()) {
                        mView.smartVip3.finishRefresh()
                        mView.smartVip3.finishLoadMore()
                    if (page == 1){
                        mView.adapter?.clear()
                        mView.rvReport3.removeAllViews()
                        mView.setVisible(mView.vipHolder)
                        mView.vipHolder.text = "暂无下级信息"
                    }
                }
            }
        }
    }
}