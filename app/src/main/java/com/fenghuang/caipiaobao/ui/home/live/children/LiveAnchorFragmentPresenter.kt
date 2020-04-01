package com.fenghuang.caipiaobao.ui.home.live.children

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.home.data.HomeHotLiveResponse
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAnchor
import com.fenghuang.caipiaobao.utils.JsonUtils
import kotlinx.android.synthetic.main.fragment_advance.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-09
 * @ Describe
 *
 */

class LiveAnchorFragmentPresenter : BaseMvpPresenter<LiveAnchorFragment>() {


    fun getAll(page: Int, type: String, boolean: Boolean) {

        HomeApi.getAllAnchor(page, type) {
            if (mView.isActive()) {
                onSuccess {
                    if (boolean && !it.typeList?.isJsonNull!!) {
                        val bean = JsonUtils.fromJson(it.typeList, Array<HomeLiveAnchor>::class.java)
                        mView.initTitle(bean)
                    }
                    if (!it.data?.isJsonNull!!) {

                        mView.page++
                        val content = JsonUtils.fromJson(it.data, Array<HomeHotLiveResponse>::class.java)
                        mView.initAdvanceRecycle(content)
                    }
                    mView.hidePageLoadingDialog()
                }
                onFailed {
                    mView.hidePageLoadingDialog()
                    ToastUtils.show(it.getMsg().toString())
                }
            }
        }
    }
}