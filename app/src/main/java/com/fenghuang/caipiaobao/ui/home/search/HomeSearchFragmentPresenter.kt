package com.fenghuang.caipiaobao.ui.home.search

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.home.data.HomeApi

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-07
 * @ Describe
 *
 */

class HomeSearchFragmentPresenter : BaseMvpPresenter<HomeSearchFragment>() {


    fun getAnchorPop() {
        HomeApi.getPopAnchor {
            onSuccess {
                if (mView.isActive()) {
                    mView.initAnchorPop(it)
                }
            }
        }
    }

    fun search(search_content: String) {
        mView.showPageLoadingDialog()
        HomeApi.getSearchAnchor(search_content) {
            onSuccess {
                mView.hidePageLoadingDialog()
                mView.upDateView(it)
            }
            onFailed {
                ToastUtils.show(it.getMsg().toString())
                mView.hidePageLoadingDialog()
            }
        }
    }

}