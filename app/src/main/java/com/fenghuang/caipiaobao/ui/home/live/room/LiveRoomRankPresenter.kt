package com.fenghuang.caipiaobao.ui.home.live.room

import android.view.View
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import kotlinx.android.synthetic.main.fragment_child_live_rank.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-27
 * @ Describe
 *
 */

class LiveRoomRankPresenter : BaseMvpPresenter<LiveRoomRankFragment>() {


    fun getAllData(anchorID: String) {
        HomeApi.getRankList(anchorID) {
            if (mView.isActive()) {
                onSuccess {
                    mView.setGone(mView.spLiveRankLoading)
                    if (it.isEmpty()) {
                        mView.tvRankHolder.visibility = View.VISIBLE
                        mView.tvRankHolder.text = "暂无打赏记录"
                    } else {
                        mView.tvRankHolder.visibility = View.GONE
                        mView.initRankRewardList(it)
                    }
                }
                onFailed {
                    mView.setGone(mView.spLiveRankLoading)
                    mView.tvRankHolder.visibility = View.VISIBLE
                    mView.tvRankHolder.text = "出错了~"
                }
            }

        }
    }
}

