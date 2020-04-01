package com.fenghuang.caipiaobao.ui.moments.childern

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.moments.data.MomentsHotDiscussResponse
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.dialog.CommentsSuccessDialog
import com.fenghuang.caipiaobao.widget.dialog.LiveRoomChatInputDialog
import kotlinx.android.synthetic.main.fragment_comment_on.*
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-19
 * @ Describe 评论详情页面
 *
 */

class CommentOnFragment(val data: MomentsHotDiscussResponse?) : BaseMvpFragment<CommentOnFragmentPresenter>() {

    var page = 1

    var commentListAdapter: CommentOnListAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = CommentOnFragmentPresenter()

    override fun getPageTitle() = "评论"

    override fun isOverridePage() = false

    override fun getContentResID() = R.layout.fragment_comment_on

    override fun isSwipeBackEnable() = true

    override fun isShowBackIconWhite() = false

    override fun onSupportVisible() {
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }

    override fun initContentView() {
        smartRefreshCommentOn.setEnableLoadMore(false)
        initSmartRefresh()
        initTitleView()
        initCommentList()
    }

    override fun initData() {
        mPresenter.getCommentOnList(data?.id!!, page)
    }

    private fun initTitleView() {
        if (data == null) return
        val spannableString = SpannableString("评论 (" + data.comment_nums + ")")
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#999999")), 2, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvPl.text = spannableString
        ImageManager.loadImg(data.avatar, findView(R.id.commentAnchorPhoto))
        commentAnchorContent.text = data.title
        commentAnchorTime.text = TimeUtils.longToDateString(data.created).toString()
        commentAnchorName.text = data.nickname
        val layoutManager = GridLayoutManager(requireActivity(), 3)
        val adapter = CommentOnAdapter(requireActivity(), layoutManager)
        commentAnchorImg.layoutManager = layoutManager
        commentAnchorImg.adapter = adapter
        if (!data.images.isNullOrEmpty()) {
            adapter.addAll(data.images)
        }
    }

    private fun initSmartRefresh() {
        smartRefreshCommentOn.setOnRefreshListener {
            page = 1
            mPresenter.getCommentOnList(data?.id!!, page)
        }
        smartRefreshCommentOn.setOnLoadMoreListener {
            mPresenter.getCommentOnList(data?.id!!, page)
        }
    }

    private fun initCommentList() {
        commentListAdapter = CommentOnListAdapter(requireActivity(), data?.user_id!!)
        commentOnList.adapter = commentListAdapter
        commentOnList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    override fun initEvent() {
        etCommentChat.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                //未登录
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(getPageActivity())
                } else {
                    val chatPop = LiveRoomChatInputDialog(getPageActivity(), R.style.inputDialog)
                    chatPop.showEmojiOrKeyBord(false)
                    chatPop.setSendClickListener { result ->
                        val dialogCommentSuccess = CommentsSuccessDialog(context!!)
                        mPresenter.commentReply(data?.id.toString(), "0", result)
                        mPresenter.setCommentSuccessListener {
                            chatPop.dismiss()
                            dialogCommentSuccess.show()
                            Timer().schedule(object : TimerTask() {
                                override fun run() {
                                    dialogCommentSuccess.dismiss()
                                    this.cancel()
                                }

                            }, 2000)
                        }
                        mPresenter.setCommentFailedListener {
                            ToastUtils.show(it)
                        }
                    }
                }
            }

        }
        imgCommentEmoji.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                //未登录
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(getPageActivity())
                } else {
                    val chatPop = LiveRoomChatInputDialog(getPageActivity(), R.style.inputDialog)
                    chatPop.showEmojiOrKeyBord(true)
                    chatPop.setSendClickListener { result ->
                        val dialogCommentSuccess = CommentsSuccessDialog(context!!)
                        mPresenter.commentReply(data?.id.toString(), "0", result)
                        mPresenter.setCommentSuccessListener {
                            chatPop.dismiss()
                            dialogCommentSuccess.show()
                            Timer().schedule(object : TimerTask() {
                                override fun run() {
                                    dialogCommentSuccess.dismiss()
                                    this.cancel()
                                }
                            }, 2000)
                        }
                        mPresenter.setCommentFailedListener { ToastUtils.show(it) }
                    }
                }
            }
        }



        commentAnchorPhoto.setOnClickListener {
            LaunchUtils.startPersonalPage(getPageActivity(), data?.user_id!!,1)
        }
    }

}