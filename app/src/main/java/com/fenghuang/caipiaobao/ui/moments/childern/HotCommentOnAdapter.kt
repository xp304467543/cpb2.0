package com.fenghuang.caipiaobao.ui.moments.childern

import android.app.Activity
import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.DynamicDrawableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.round.RoundRelativeLayout
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.moments.data.DavisCommentOnReplayResponse
import com.fenghuang.caipiaobao.ui.moments.data.DavisCommentOnResponse
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.chat.EmojiconHandler.*
import com.fenghuang.caipiaobao.widget.chat.bean.EmojiconSpan
import com.fenghuang.caipiaobao.widget.dialog.CommentsSuccessDialog
import com.fenghuang.caipiaobao.widget.dialog.LiveRoomChatInputDialog
import com.fenghuang.caipiaobao.widget.spanlite.SpanBuilder
import com.fenghuang.caipiaobao.widget.spanlite.SpanLite
import java.util.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-19
 * @ Describe 主播
 *
 */


//评论列表
class HotCommentOnListAdapter(context: Context, val dynamic_id: String) : BaseRecyclerAdapter<DavisCommentOnResponse>(context) {


     var replyAdapter: HotCommentOnReplyListAdapter?=null

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DavisCommentOnResponse> {
        return CommentOnListHolder(parent)
    }

    inner class CommentOnListHolder(parent: ViewGroup) : BaseViewHolder<DavisCommentOnResponse>(getContext(), parent, R.layout.holder_comment_list) {


        override fun onBindView(context: Context?) {
            context?.apply {
                val recycle = findView<RecyclerView>(R.id.rvReplyList)
                replyAdapter = HotCommentOnReplyListAdapter(this, dynamic_id)
                recycle.adapter = replyAdapter
                recycle.layoutManager = object : LinearLayoutManager(this, VERTICAL, false) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
            }
        }

        override fun onBindData(data: DavisCommentOnResponse) {
            ImageManager.loadImg(data.avatar, findView(R.id.imgCommentUserPhoto))
            setText(R.id.tvCommentUserName, data.nickname)
            setText(R.id.tvCommentContent, data.content)
            setText(R.id.tvCommentTime, data.created)
            setText(R.id.tvDianZan, data.like)
            setText(R.id.tvCommentTime, data.created)
            if (!data.reply.isNullOrEmpty()) {
                replyAdapter?.replayId = data.uid
                replyAdapter?.addAll(data.reply)
            } else setGone(findView<RoundRelativeLayout>(R.id.rlReply))
            if (data.is_like) {
                findView<ImageView>(R.id.imgDianZan).background = getDrawable(R.mipmap.ic_yidianzan)
            } else findView<ImageView>(R.id.imgDianZan).background = getDrawable(R.mipmap.ic_dianzan)
            findView<ImageView>(R.id.imgCommentUserType).background = when (data.user_type) {
                "2" -> getDrawable(R.mipmap.ic_live_anchor)
                else -> null
            }
            setOnClick(R.id.linDianZan)
            setOnClick(R.id.linReply)
            setOnClick(R.id.imgCommentUserPhoto)
        }

        override fun onClick(id: Int) {
            when (id) {
                //点赞
                R.id.linDianZan -> {
                    if (FastClickUtils.isFastClick()) {
                        //未登录
                        if (!UserInfoSp.getIsLogin()) {
                            GlobalDialog.notLogged(getContext() as Activity)
                            return
                        }
                        if (getData()?.is_like!!) {
                            getData()?.is_like = false
                            val zan = (getData()?.like?.toInt()?.minus(1)).toString()
                            getData()?.like = zan
                            findView<TextView>(R.id.tvDianZan).text = zan
                            findView<ImageView>(R.id.imgDianZan).background = getDrawable(R.mipmap.ic_dianzan)
                        } else {
                            getData()?.is_like = true
                            val zan = (getData()?.like?.toInt()?.plus(1)).toString()
                            getData()?.like = zan
                            findView<TextView>(R.id.tvDianZan).text = zan
                            findView<ImageView>(R.id.imgDianZan).background = getDrawable(R.mipmap.ic_yidianzan)
                        }

                        MomentsAnchorPresenter().clickZan("2", getData()?.comment_id.toString())
                    } else ToastUtils.show("请勿重复点击")
                }
                //回复
                R.id.linReply -> {
                    if (FastClickUtils.isFastClick()) {
                        //未登录
                        if (!UserInfoSp.getIsLogin()) {
                            GlobalDialog.notLogged(getContext() as Activity)
                            return
                        }
                        commentReplay(getContext()!!, dynamic_id, getData()?.comment_id.toString())
                    }

                }

                R.id.imgCommentUserPhoto -> {

                    if (FastClickUtils.isFastClick()) {
                        when(getData()?.user_type){
                            "1" -> LaunchUtils.startPersonalPage(getContext(),getData()?.uid!!,1)
                            "2" -> LaunchUtils.startPersonalPage(getContext(),getData()?.uid!!,2)
                        }
                    }
                }
            }
        }

    }

    //回复列表  CommentOnReplayResponse
    inner class HotCommentOnReplyListAdapter(context: Context, val dynamic_id: String, var replayId: String = "") : BaseRecyclerAdapter<DavisCommentOnReplayResponse>(context) {


        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DavisCommentOnReplayResponse> {
            return CommentOnReplyListHolder(parent)
        }

        inner class CommentOnReplyListHolder(parent: ViewGroup) : BaseViewHolder<DavisCommentOnReplayResponse>(getContext(), parent, R.layout.holder_reply_list) {
            override fun onBindData(data: DavisCommentOnReplayResponse) {

                val viewSpan = SpanLite.with(findView(R.id.tvReplyWords))

                when {
                    //回复楼主
                    data.p_uid == replayId -> {
                        //设置type
                        setUserType(viewSpan, data)
                        //回复名字
                        val str = " " + data.nickname + " : "
                        viewSpan.append(SpanBuilder.Builder(str)
                                .drawTextColor(getColor(R.color.color_5F84B0))
                                .drawTextSizeAbsolute(36).setOnClick(object : SpanBuilder.Listener() {
                                    override fun onClick(widget: View?) {
//                                        ToastUtils.show("回复名字")
                                        startPersonal(data.user_type,data.uid)
                                    }
                                }).build())
                        //回复内容 (判断是否有表情)
                        isHasEmote(viewSpan, data.content)

                    }
                    //回复评论
                    data.p_uid != replayId -> {
                        //设置type
                        setUserType(viewSpan, data)
                        //被回复评论名字
                        val str = " " + data.nickname
                        viewSpan.append(SpanBuilder.Builder(str)
                                .drawTextColor(getColor(R.color.color_5F84B0))
                                .drawTextSizeAbsolute(36).setOnClick(object : SpanBuilder.Listener() {
                                    override fun onClick(widget: View?) {
//                                        ToastUtils.show("被回复评论名字")
                                        startPersonal(data.user_type,data.uid)
                                    }
                                }).build())
                        viewSpan.append(SpanBuilder.Builder(" 回复 ")
                                .drawTextColor(getColor(R.color.color_333333))
                                .drawTextSizeAbsolute(36).build())
                        //设置type
                        when {
                            data.p_user_type == "2" -> {
                                //主播
                                viewSpan.append(SpanBuilder.Builder("1")
                                        .drawImageCenter(getContext(), R.mipmap.ic_zhubo)
                                        .build())
                            }
                        }
                        //被回复评论名字
                        viewSpan.append(SpanBuilder.Builder(data.p_nickname + " : ")
                                .drawTextColor(getColor(R.color.color_5F84B0))
                                .drawTextSizeAbsolute(36).setOnClick(object : SpanBuilder.Listener() {
                                    override fun onClick(widget: View?) {
//                                        ToastUtils.show("被回复评论名字")
                                        startPersonal(data.p_user_type,data.p_uid)
                                    }
                                }).build())
                        //被回复评论内容 (判断是否有表情)
                        isHasEmote(viewSpan, data.content)
                    }
                }
                viewSpan.active()
                if (getDataPosition() == getLastPosition()) {
                    findView<TextView>(R.id.tvReplyWords).setPadding(0, 0, 0, ViewUtils.dp2px(8))
                }
            }

            private fun setUserType(viewSpan: SpanLite, data: DavisCommentOnReplayResponse) {
                when {
                    data.user_type == "2" -> {
                        //主播
                        viewSpan.append(SpanBuilder.Builder("1")
                                .drawImageCenter(getContext(), R.mipmap.ic_zhubo)
                                .build())
                    }

                }
            }

            private fun isHasEmote(viewSpan: SpanLite, str: String) {
                val matcher = CAIPIAOBAO.matcher(str)
                val builder = SpannableStringBuilder(str)
                while (matcher.find()) {
                    val key = matcher.group()
                    if (fengHuangjisModifiedMap.containsKey(key)) {
                        val resId = fengHuangjisModifiedMap[key]
                        val emoticonSpan = EmojiconSpan(getContext(), resId!!, 50, DynamicDrawableSpan.ALIGN_CENTER, 50)
                        builder.setSpan(emoticonSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    if (caiPiaoBaoEmojisModifiedMap.containsKey(key)) {
                        val resId = caiPiaoBaoEmojisModifiedMap[key]
                        val emoticonSpan = EmojiconSpan(getContext(), resId!!, 50, DynamicDrawableSpan.ALIGN_CENTER, 50)
                        builder.setSpan(emoticonSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
                builder.setSpan(ForegroundColorSpan(getColor(R.color.color_333333)), 0, str.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                builder.setSpan(AbsoluteSizeSpan(13, true), 0, str.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                builder.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        if (FastClickUtils.isFastClick()) {
                            //未登录
                            if (!UserInfoSp.getIsLogin()) {
                                GlobalDialog.notLogged(getContext() as Activity)
                                return
                            }
                            commentReplay(getContext()!!, dynamic_id, getData()?.comment_id.toString())
                        }

                    }

                    override fun updateDrawState(ds: TextPaint) {
                        /**set textColor**/
                        // ds.setColor( ds.linkColor );
                        /**Remove the underline**/
                        ds.isUnderlineText = false
                    }

                }, 0, str.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                viewSpan.append(builder)
            }
        }
    }


    //回复框
    fun commentReplay(context: Context, dynamic_id: String, comment_id: String) {
        val chatPop = LiveRoomChatInputDialog(context, R.style.inputDialogBackGround)
        chatPop.showEmojiOrKeyBord(false)
        chatPop.setSendClickListener { result ->
            val dialogCommentSuccess = CommentsSuccessDialog(getContext())
            val presenter = HotCommentOnActivityPresenter()
            presenter.davisReply(dynamic_id, comment_id, result)
            presenter.setCommentSuccessListener {
                chatPop.dismiss()
                dialogCommentSuccess.show()
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        dialogCommentSuccess.dismiss()
                        this.cancel()
                    }

                }, 2000)
            }
            presenter.setCommentFailedListener {
                ToastUtils.show(it)
            }
        }
        chatPop.show()
    }

    fun startPersonal(type: String, id: String) {
        when (type) {
            "2" -> LaunchUtils.startPersonalPage(getContext(), id, 2)
            "1" -> LaunchUtils.startPersonalPage(getContext(), id, 1)
        }
    }

}