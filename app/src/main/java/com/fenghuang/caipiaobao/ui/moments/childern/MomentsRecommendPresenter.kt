package com.fenghuang.caipiaobao.ui.moments.childern

import android.content.Context
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.caipiaobao.ui.moments.data.MomentsApi

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe
 *
 */

class MomentsRecommendPresenter : BaseMvpPresenter<MomentsRecommendFragment>() {


    fun getData(){

        MomentsApi.getRecommend {
            if (mView.isActive()){
                onSuccess {
                    mView.upDateRecycle(it)
                }
            }
        }

    }

}