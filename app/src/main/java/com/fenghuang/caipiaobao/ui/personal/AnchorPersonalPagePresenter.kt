package com.fenghuang.caipiaobao.ui.personal

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.personal.data.PersonalApi

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-01
 * @ Describe
 *
 */

class AnchorPersonalPagePresenter : BaseMvpPresenter<AnchorPersonalPage>() {


    fun getAnchorInfo(anchor_id: String) {
        PersonalApi.getAnchorPage(anchor_id) {
            if (mView.isActive()) {
                onSuccess { mView.initAnchor(it) }
                onFailed { ToastUtils.show(it.getMsg().toString()) }
            }
        }
    }

}