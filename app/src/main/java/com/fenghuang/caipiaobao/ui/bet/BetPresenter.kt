package com.fenghuang.caipiaobao.ui.bet

import android.annotation.SuppressLint
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.home.data.LineCheck
import com.fenghuang.caipiaobao.utils.NetPingManager
import kotlinx.android.synthetic.main.fragment_bet.*


/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/12- 12:21
 * @ Describe
 *
 */

class BetPresenter : BaseMvpPresenter<BetFragment>() {




    @SuppressLint("SetTextI18n")
    fun getUrl() {
        HomeApi.getLotteryUrl {
            onSuccess {
                if (mView.isActive()) {
                    mView.dataBean = it
                    mView.loadWeb()
                }
            }
            onFailed { }
        }
    }


}