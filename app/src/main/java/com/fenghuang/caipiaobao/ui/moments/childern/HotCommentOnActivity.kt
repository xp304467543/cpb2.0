package com.fenghuang.caipiaobao.ui.moments.childern

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpActivity
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.moments.data.MomentsAnchorListResponse
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
 * @ Describe 评论详情页面  davis
 *
 */

class HotCommentOnActivity() : BaseMvpActivity<HotCommentOnActivityPresenter>() {

    var page = 1

    lateinit var data: MomentsAnchorListResponse

    var commentListAdapter: HotCommentOnListAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = HotCommentOnActivityPresenter()

    override fun getPageTitle() = "评论"


    override fun getContentResID() = R.layout.fragment_comment_on

    override fun isSwipeBackEnable() = true

    override fun isShowBackIconWhite() = false


    override fun initContentView() {
        data = intent.getSerializableExtra("responseAnchor") as MomentsAnchorListResponse
        smartRefreshCommentOn.setEnableLoadMore(false)
        initSmartRefresh()
        initTitleView()
        initCommentList()
    }

    override fun initData() {
        mPresenter.getCommentOnList(data.dynamic_id, page)
    }

    private fun initTitleView() {
        val spannableString = SpannableString("评论 (" + data.pls + ")")
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#999999")), 2, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvPl.text = spannableString
        ImageManager.loadImg(data.avatar, findView(R.id.commentAnchorPhoto))
        commentAnchorContent.text = data.text
        commentAnchorTime.text = TimeUtils.longToDateString(data.create_time).toString()
        commentAnchorName.text = data.nickname
        val layoutManager = GridLayoutManager(this, 3)
        val adapter = CommentOnAdapter(this, layoutManager)
        commentAnchorImg.layoutManager = layoutManager
        commentAnchorImg.adapter = adapter
        if (!data.media.isNullOrEmpty()) {
            adapter.addAll(data.media)
        }
        if (data.live_status == "1") {
            circleWave.setInitialRadius(50f)
            circleWave.start()
        }
        if (data.sex == 1) {
            findView<ImageView>(R.id.imgAnchorSex).setBackgroundResource(R.mipmap.ic_live_anchor_boy)
        } else if (data.sex == 0) {
            findView<ImageView>(R.id.imgAnchorSex).setBackgroundResource(R.mipmap.ic_live_anchor_girl)
        }
    }

    private fun initSmartRefresh() {
        smartRefreshCommentOn.setOnRefreshListener {
            page = 1
            mPresenter.getCommentOnList(data.dynamic_id, page)
        }
        smartRefreshCommentOn.setOnLoadMoreListener {
            page++
            mPresenter.getCommentOnList(data.dynamic_id, page)
        }
    }

    private fun initCommentList() {
        commentListAdapter = HotCommentOnListAdapter(this, data.dynamic_id)
        commentOnList.adapter = commentListAdapter
        commentOnList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }


    override fun initEvent() {
        etCommentChat.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                //未登录
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(this)
                } else {
                    val chatPop = LiveRoomChatInputDialog(this, R.style.inputDialog)
                    chatPop.showEmojiOrKeyBord(false)
                    chatPop.setSendClickListener { result ->
                        val dialogCommentSuccess = CommentsSuccessDialog(this)
                        mPresenter.davisReply(data?.dynamic_id.toString(), "", result)
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
                    GlobalDialog.notLogged(this)
                } else {
                    val chatPop = LiveRoomChatInputDialog(this, R.style.inputDialog)
                    chatPop.showEmojiOrKeyBord(true)
                    chatPop.setSendClickListener { result ->
                        val dialogCommentSuccess = CommentsSuccessDialog(this)
                        mPresenter.davisReply(data.dynamic_id, "", result)
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
            if (data.live_status == "1" && data.isToLive) {
                LaunchUtils.startLive(this, data.anchor_id, data.live_status,
                        "", data.avatar, data.nickname, 0,"1")
            } else LaunchUtils.startPersonalPage(this, data.anchor_id, 2)
        }
    }

}