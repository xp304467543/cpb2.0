package com.fenghuang.caipiaobao.ui.home.live.room

import android.view.View
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import kotlinx.android.synthetic.main.fragment_child_live_anchor.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-29
 * @ Describe
 *
 */

class LiveRoomAnchorPresenter : BaseMvpPresenter<LiveRoomAnchorFragment>() {


    fun getAnchorInfo(anchorID: String) {
        HomeApi.getLiveAnchorInfo(anchorID) {
            if (mView.isActive()) {
                onSuccess {
                    mView.initAnchorInfo(it)
                    getAnchorDynamic(anchorID)
                }
            }
        }
    }

    //主播动态
    private fun getAnchorDynamic(anchorID: String) {
        HomeApi.getAnchorDynamic(anchorId = anchorID) {
            onSuccess {
                if (mView.isActive()){
                    mView.spLiveAnchorLoading.visibility = View.GONE
                    mView.initAnchorNews(it)
                }
            }
        }
    }

}