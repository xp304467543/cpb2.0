package com.fenghuang.caipiaobao.ui.moments.childern

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.SpUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.constant.UserConstant
import com.fenghuang.caipiaobao.ui.moments.data.MomentsApi
import kotlinx.android.synthetic.main.fragment_moments_hot_discuss.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe
 *
 */

class MomentsHotDiscussPresenter : BaseMvpPresenter<MomentsHotDiscussFragment>() {


    //热门讨论
    fun getHotDiscuss() {
        MomentsApi.getHotDiscuss("10", mView.page) {
            if (mView.isActive()) {
                onSuccess {
                    mView.setGone(mView.PlaceHolder)
                    if (it.isNotEmpty()) {
                        mView.rvDiscussAdapter?.addAll(it)
                    } else mView.rvDiscussAdapter?.isShowFooter = true
                    if (mView.smartRefreshLayoutHotDiscuss != null) {
                        mView.smartRefreshLayoutHotDiscuss.finishLoadMore()
                        mView.smartRefreshLayoutHotDiscuss.finishRefresh()
                    }
                }
                onFailed {
                    mView.setGone(mView.PlaceHolder)
                    if (mView.smartRefreshLayoutHotDiscuss != null) {
                        mView.smartRefreshLayoutHotDiscuss.finishLoadMore()
                        mView.smartRefreshLayoutHotDiscuss.finishRefresh()
                    }
                    if (mView.page != 1) mView.page--
                    ToastUtils.show("数据获取失败")
                }
            }
        }
    }

    //点赞
    fun  setZans(articleId: String){
        MomentsApi.getQuizArticleLikeResult(articleId) {
            onSuccess {
                //                mView.notifyQuizHolder(articleId, clickPosition)
//                ToastUtils.showSuccess(it)
            }
            onFailed {
//                ExceptionDialog.showExpireDialog(mView.requireContext(), it)
            }
        }
    }
}