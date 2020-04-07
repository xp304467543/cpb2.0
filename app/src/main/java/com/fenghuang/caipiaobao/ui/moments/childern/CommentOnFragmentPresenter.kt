package com.fenghuang.caipiaobao.ui.moments.childern

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.caipiaobao.ui.moments.data.MomentsApi
import kotlinx.android.synthetic.main.fragment_comment_on.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-19
 * @ Describe
 *
 */
class CommentOnFragmentPresenter : BaseMvpPresenter<CommentOnFragment>() {


    fun getCommentOnList(articleId: String, page: Int) {
        MomentsApi.getCommentOnList(articleId, page) {
            if (mView.isActive()) {
                onSuccess {
                    if (!it.isNullOrEmpty()) {
                        if (mView.page == 1) {
                            mView.commentListAdapter?.clear()
                            mView.commentListAdapter?.replyAdapter?.clear()
                            mView.commentListAdapter?.addAll(it)
                        } else {
                            mView.commentListAdapter?.addAll(it)
                            mView.commentListAdapter?.notifyDataSetChanged()
                        }
                    } else mView.page--
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

    //评论点赞
    fun commentZan(comment_id: String) {
        MomentsApi.setCommentZan(comment_id) {
            onSuccess { }
            onFailed { LogUtils.e(it.getMsg()) }
        }
    }

    private var commentSuccessListener: ((it: String) -> Unit)? = null
    private var commentFailedListener: ((it: String) -> Unit)? = null

    //评论
    fun commentReply(article_id: String, reply_id: String, content: String) {
        MomentsApi.setCommentReply(article_id, reply_id, content) {
            onSuccess {
                commentSuccessListener?.invoke(it)
            }
            onFailed {
                commentFailedListener?.invoke(it.getMsg().toString())
            }
        }
    }

    fun setCommentSuccessListener(CommentSuccessListener: ((it: String) -> Unit)) {
        commentSuccessListener = CommentSuccessListener
    }

    fun setCommentFailedListener(CommentFailedListener: ((it: String) -> Unit)) {
        commentFailedListener = CommentFailedListener
    }
}