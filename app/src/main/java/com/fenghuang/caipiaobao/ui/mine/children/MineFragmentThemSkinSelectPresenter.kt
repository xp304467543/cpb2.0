package com.fenghuang.caipiaobao.ui.mine.children

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.mine.data.MineApi

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-14
 * @ Describe
 *
 */

class MineFragmentThemSkinSelectPresenter : BaseMvpPresenter<MineFragmentThemSkinSelect>() {


    fun getSkin(id: String) {
        MineApi.getThemSKinInfo(id = id) {
            if (mView.isActive()) {
                onSuccess {
                    mView.upDataBanner(it)
                }
                onFailed {
                    ToastUtils.show(it.getMsg().toString())
                }
            }
        }
    }
}