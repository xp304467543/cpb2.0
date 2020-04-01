package com.fenghuang.caipiaobao.ui.mine.children

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import kotlinx.android.synthetic.main.fragment_them_skin.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-14
 * @ Describe
 *
 */

class MineFragmentThemSkinPresenter : BaseMvpPresenter<MineFragmentThemSkin>() {


    fun getSkinList() {
        MineApi.getThemSKin {
            if (mView.isActive()) {
                onSuccess {
                    mView.skinAdapter?.addAll(it)
                    mView.setGone(mView.SkinLoadView)
                }
                onFailed {
                    ToastUtils.show(it.getMsg().toString())
                }
            }
        }
    }
}