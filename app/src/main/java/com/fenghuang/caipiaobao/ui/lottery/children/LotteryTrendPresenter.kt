package com.fenghuang.caipiaobao.ui.lottery.children

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryCodeTrendResponse
import kotlinx.android.synthetic.main.child_fragment_trend.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-03
 * @ Describe
 *
 */

class LotteryTrendPresenter : BaseMvpPresenter<LotteryTrendFragment>() {

    fun getTrendData(lotteryId: String, num: String = "1", limit: String = "10", date: String = TimeUtils.getToday()) {
        mView.showPageLoadingDialog()
        LotteryApi.getLotteryTrend(lotteryId, num, limit, date) {
            if (mView.isActive()) {
                onSuccess {
                    if (!it.isNullOrEmpty()){
                        mView.setGone(mView.linTrendLoading)
                        mView.setGone(mView.tvHolder)
                      if (it.isNotEmpty())  mView.initLineChart(it, null)
                    } else {
                        mView.setGone(mView.linTrendLoading)
                        mView.setVisible(mView.tvHolder)
                        mView.hidePageLoadingDialog()
                    }

                }
                onFailed {
                    ToastUtils.show(it.getMsg().toString())
                    mView.setGone(mView.linTrendLoading)
                    mView.setVisible(mView.tvHolder)
                    mView.hidePageLoadingDialog()
                }
            }

        }
    }

    //形态走势  形态
    fun getFormData(lotteryId: String, num: String = "7", limit: String = "10", date: String = TimeUtils.getToday()) {
        LotteryApi.getLotteryTrend(lotteryId, num, limit, date) {
            if (mView.isActive()) {
                onSuccess {
                    if (!it.isNullOrEmpty()) {
                        when (num) {
                            "7" -> getComposeData(lotteryId, num = "10", limit = limit, date = date, result = it)
                            "8" -> getComposeData(lotteryId, num = "11", limit = limit, date = date, result = it)
                            "9" -> getComposeData(lotteryId, num = "12", limit = limit, date = date, result = it)
                        }

                    }
                }
                onFailed {
                    ToastUtils.show(it.getMsg().toString())
                    mView.setGone(mView.linTrendLoading)
                }
            }

        }
    }

    //形态走势  组合类型
    private fun getComposeData(lotteryId: String, num: String, limit: String, date: String, result: List<LotteryCodeTrendResponse>) {
        LotteryApi.getLotteryTrend(lotteryId, num, limit, date) {
            onSuccess {
                if (!it.isNullOrEmpty()) mView.initLineChart(result, it)
            }
            onFailed {
                ToastUtils.show(it.getMsg().toString())
                mView.setGone(mView.linTrendLoading)
            }
        }
    }

}