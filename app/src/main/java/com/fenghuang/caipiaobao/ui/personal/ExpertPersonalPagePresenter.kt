package com.fenghuang.caipiaobao.ui.personal

import android.os.Handler
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.ui.personal.data.PersonalApi
import kotlinx.android.synthetic.main.fragment_presonal_expert.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-01
 * @ Describe
 */
class ExpertPersonalPagePresenter : BaseMvpPresenter<ExpertPersonalPage>() {


    fun getExpertInfo(expert_id: String,lottery_id: String) {
        PersonalApi.getExpertPage(expert_id,lottery_id) {
            if (mView.isActive()) {
                onSuccess {
                    mView.initExpert(it)
                    mView.hidePageLoadingDialog()
                }
                onFailed {
                    ToastUtils.show(it.getMsg().toString())
                    mView.hidePageLoadingDialog()
                }
            }
        }
    }

    fun getExpertHistory(expert_id: String, lottery_id: String, limit: String) {
        PersonalApi.getExpertPageHistory(expert_id, lottery_id, limit) {
            if (mView.isActive()) {
                onSuccess { mView.initExpertHistory(it) }
                onFailed {
                    ToastUtils.show(it.getMsg().toString())
                    mView.tvDescription.text = "暂无历史记录！"
                }
            }
        }
    }


    var handler: Handler? = null
    var runnable: Runnable? = null
    fun getNextTime(lottery_id: String) {
        LotteryApi.getLotteryNewCode(lottery_id) {
            if (mView.isActive()) {
                onSuccess {
                    if (it.next_lottery_time.toInt() > 1) {
                        mView.countDownTime(it.next_lottery_time, lottery_id)
                    } else {
                        if (mView.timer != null) mView.timer?.cancel()
                        handler = Handler()
                        runnable = Runnable {
                            getNextTime(it.lottery_id)
                        }
                        handler?.postDelayed(runnable, 5000)
                    }
                }
                onFailed { }
            }
        }
    }

}