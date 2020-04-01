package com.fenghuang.caipiaobao.ui.moments.childern

import android.view.View
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.moments.data.MomentsApi
import kotlinx.android.synthetic.main.fragment_moments_anchor_list.*
import kotlinx.android.synthetic.main.fragment_moments_anchor_list.PlaceHolder
import kotlinx.android.synthetic.main.fragment_moments_hot_discuss.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe
 *
 */

class MomentsAnchorPresenter : BaseMvpPresenter<MomentsAnchorFragment>() {


    //主播动态
    fun getAnchorList(anchor_id: String,boolean: Boolean) {
        MomentsApi.getAnchorMoments(anchor_id, "10", mView.page) {
            if (mView.isActive()) {
                onSuccess {
                    mView.setGone(mView.PlaceHolder)
                    if (it.isNotEmpty()) {
                        mView.anchorListAdapter?.addAll(it)
                        mView.tvNoData.visibility = View.GONE
                    } else if (boolean){
                        mView.anchorListAdapter?.isShowFooter = true
                        mView.tvNoData.visibility = View.VISIBLE
                    }
                    if (mView.smartRefreshLayoutAnchorList != null) {
                        mView.smartRefreshLayoutAnchorList.finishLoadMore()
                        mView.smartRefreshLayoutAnchorList.finishRefresh()
                    }
                }
                onFailed {
                    mView.setGone(mView.PlaceHolder)
                    if (mView.smartRefreshLayoutAnchorList != null) {
                        mView.smartRefreshLayoutAnchorList.finishLoadMore()
                        mView.smartRefreshLayoutAnchorList.finishRefresh()
                    }
                    if (mView.page != 1) mView.page--
                    ToastUtils.show("数据获取失败")
                }
            }
        }
    }

    fun getAnchorDynamic(anchor_id: String,boolean: Boolean) {
        MomentsApi.getAnchorDynamic(anchor_id, "10", mView.page) {
            if (mView.isActive()) {
                onSuccess {
                    mView.setGone(mView.PlaceHolder)
                    canRefresh()
                    if (it.isNotEmpty()) {
                        mView.anchorListAdapter?.addAll(it)
                        mView.tvNoData.visibility = View.GONE
                    } else if (boolean){
                        mView.anchorListAdapter?.isShowFooter = true
                        mView.tvNoData.visibility = View.VISIBLE
                    }
                    if (mView.smartRefreshLayoutAnchorList != null) {
                        mView.smartRefreshLayoutAnchorList.finishLoadMore()
                        mView.smartRefreshLayoutAnchorList.finishRefresh()
                    }
                }
                onFailed {
                    mView.setGone(mView.PlaceHolder)
                    canRefresh()
                    if (mView.smartRefreshLayoutAnchorList != null) {
                        mView.smartRefreshLayoutAnchorList.finishLoadMore()
                        mView.smartRefreshLayoutAnchorList.finishRefresh()
                    }
                    if (mView.page != 1) mView.page--
                    ToastUtils.show("数据获取失败")
                }
            }
        }
    }


    fun clickZan(type: String, like_id: String) {
        MomentsApi.clickZansDavis(type, like_id) {
            onSuccess { }
        }
    }

    private fun canRefresh(){
        mView.smartRefreshLayoutAnchorList.setEnableRefresh(true)
        mView.smartRefreshLayoutAnchorList.setEnableLoadMore(true)
    }

}