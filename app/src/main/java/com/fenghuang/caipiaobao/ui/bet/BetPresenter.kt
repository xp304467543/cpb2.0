package com.fenghuang.caipiaobao.ui.bet

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import kotlinx.android.synthetic.main.fragment_bet.*

/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/12- 12:21
 * @ Describe
 *
 */

class BetPresenter : BaseMvpPresenter<BetFragment>() {


    fun getUrl() {
        HomeApi.getLotteryUrl {
            onSuccess {
                if (mView.isActive()) {

                    mView.baseUrl = it.betting
                    mView.baseBetWebView.loadUrl(it.betting)
                }
            }
            onFailed { }
        }
    }


}