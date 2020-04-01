package com.fenghuang.caipiaobao.ui.moments

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.caipiaobao.ui.moments.data.MomentsApi

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe
 *
 */

class MomentsPresenter : BaseMvpPresenter<MomentsFragment>() {


    fun getMomentsData() {
        MomentsApi.getTopBanner {
            onSuccess {
                mView.upDateBanner(it)
            }
        }
    }

}