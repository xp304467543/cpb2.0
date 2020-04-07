package com.fenghuang.caipiaobao.ui.moments.childern

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.caipiaobao.ui.moments.data.MomentsApi
import kotlinx.android.synthetic.main.fragment_comment_on.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-19
 * @ Describe
 *
 */
class HotCommentOnActivityPresenter : BaseMvpPresenter<HotCommentOnActivity>() {


    fun getCommentOnList(dym_id: String, page: Int) {
        MomentsApi.getCommentList(dym_id, page) {
            if (mView.isActive()) {
                onSuccess {
                    if (!it.isNullOrEmpty() ) {
                        if (mView.page == 1){
                            mView.commentListAdapter?.clear()
                            mView.commentListAdapter?.replyAdapter?.clear()
                            mView.commentListAdapter?.addAll(it)
                        }else{
                            mView.commentListAdapter?.addAll(it)
                            mView.commentListAdapter?.notifyDataSetChanged()
                        }
                    }else mView.page--
                    if (mView.smartRefreshCommentOn != null) {
                        mView.smartRefreshCommentOn.setEnableLoadMore(true)
                        mView.smartRefreshCommentOn.finishLoadMore()
                        mView.smartRefreshCommentOn.finishRefresh()
                    }
                }
                onFailed {

                }
            }
        }
    }


    private var commentSuccessListener: ((it: String) -> Unit)? = null
    private var commentFailedListener: ((it: String) -> Unit)? = null


    fun setCommentSuccessListener(CommentSuccessListener: ((it: String) -> Unit)) {
        commentSuccessListener = CommentSuccessListener
    }

    fun setCommentFailedListener(CommentFailedListener: ((it: String) -> Unit)) {
        commentFailedListener = CommentFailedListener
    }

    //评论
    fun davisReply(dynamic_id: String, comment_id: String, comment_text: String) {
        MomentsApi.setDavisCommentReplay(dynamic_id, comment_id, comment_text) {
            onSuccess {
                commentSuccessListener?.invoke(it)
            }
            onFailed {
                commentFailedListener?.invoke(it.getMsg().toString())
            }
        }
    }

}