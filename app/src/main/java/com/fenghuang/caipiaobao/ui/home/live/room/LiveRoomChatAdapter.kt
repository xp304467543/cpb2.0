package com.fenghuang.caipiaobao.ui.home.live.room

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.facebook.drawee.view.SimpleDraweeView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveTwentyNewsResponse
import com.fenghuang.caipiaobao.ui.home.data.LiveCallPersonal
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.dialog.ForbiddenWordsDialog
import com.hwangjr.rxbus.RxBus


/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-25
 * @ Describe
 *
 */

class LiveRoomChatAdapter(context: Context) : BaseRecyclerAdapter<HomeLiveTwentyNewsResponse>(context) {


    val TYPE_NORMAL = 0
    val TYPE_GIFT = 1
    val TYPE_RED = 2


    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeLiveTwentyNewsResponse> {
        return when (viewType) {
            TYPE_NORMAL -> NormalHolder(parent)
            TYPE_RED -> RedHolder(parent)
            else -> GiftHolder(parent)
        }
    }

    override fun getItemCount(): Int {
        return getAllData().size
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            getAllData()[position].type == "publish" -> TYPE_NORMAL
            //礼物
            getAllData()[position].type == "gift" -> TYPE_GIFT
            //红包
            getAllData()[position].gift_type == "4" -> TYPE_RED
            else -> TYPE_GIFT
        }
    }


    inner class NormalHolder(parent: ViewGroup) : BaseViewHolder<HomeLiveTwentyNewsResponse>(getContext(), parent, R.layout.holder_fragment_chat_nomal) {
        override fun onBindData(data: HomeLiveTwentyNewsResponse) {
            //初始化消息--------------
            setText(R.id.tvChatUserName, data.userName)
            setText(R.id.tvChatTime, data.sendTimeTxt)
            setText(R.id.tvChatContent, data.text)
            ImageManager.loadImg(data.avatar, findView(R.id.imgChatUserPhoto))
            when {
                data.userType == "2" -> findView<ImageView>(R.id.imgVipLevel).setBackgroundResource(R.mipmap.ic_live_chat_manager)
                data.userType == "3" -> findView<ImageView>(R.id.imgVipLevel).setBackgroundResource(R.mipmap.ic_live_anchor)
                else -> when (data.vip) {
                    "1" -> findView<ImageView>(R.id.imgVipLevel).setBackgroundResource(R.mipmap.v1)
                    "2" -> findView<ImageView>(R.id.imgVipLevel).setBackgroundResource(R.mipmap.v2)
                    "3" -> findView<ImageView>(R.id.imgVipLevel).setBackgroundResource(R.mipmap.v3)
                    "4" -> findView<ImageView>(R.id.imgVipLevel).setBackgroundResource(R.mipmap.v4)
                    "5" -> findView<ImageView>(R.id.imgVipLevel).setBackgroundResource(R.mipmap.v5)
                    "6" -> findView<ImageView>(R.id.imgVipLevel).setBackgroundResource(R.mipmap.v6)
                    "7" -> findView<ImageView>(R.id.imgVipLevel).setBackgroundResource(R.mipmap.v7)
                    else -> {
                        setGone(R.id.imgVipLevel)
                        val text = findView<TextView>(R.id.tvChatUserName)
                        val layout = text.layoutParams as ConstraintLayout.LayoutParams
                        layout.marginStart = 0
                    }
                }
            }
            //初始化消息--END------------
            setOnClick(R.id.imgChatUserPhoto)
            findView<ConstraintLayout>(R.id.chatCons).setOnLongClickListener {
               RxBus.get().post(LiveCallPersonal(data.userName))
                true
            }
            findView<SimpleDraweeView>(R.id.imgChatUserPhoto).setOnLongClickListener {
                if (UserInfoSp.getUserType() == "2") {
                    val dialog = ForbiddenWordsDialog(getContext()!!, data)
                    dialog.show()
                }
                true
            }
        }

        override fun onClick(id: Int) {
            when (id) {
                R.id.imgChatUserPhoto -> {
                    when (getData()?.userType) {
                        "0" -> LaunchUtils.startPersonalPage(getContext(), getData()?.user_id!!, 1)
                        "1" -> LaunchUtils.startPersonalPage(getContext(), getData()?.user_id!!, 2)
                        "2" -> LaunchUtils.startPersonalPage(getContext(), getData()?.user_id!!, 3)
                    }

                }
            }
        }
    }

    inner class GiftHolder(parent: ViewGroup) : BaseViewHolder<HomeLiveTwentyNewsResponse>(getContext(), parent, R.layout.holder_fragment_chat_gift) {
        override fun onBindData(data: HomeLiveTwentyNewsResponse) {
            when (data.vip) {
                "1" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v1)
                "2" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v2)
                "3" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v3)
                "4" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v4)
                "5" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v5)
                "6" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v6)
                "7" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v7)
                else -> setGone(R.id.imgVip)
            }
            setText(R.id.tvUserName, data.userName)
            setText(R.id.tvGiftNum, "送给主播 " + data.gift_num + " 个 ")
            setText(R.id.tvGiftName, data.gift_name)
            ImageManager.loadImg(data.icon, findView(R.id.imgGift))
            setText(R.id.tvNum, "  x" + data.final_num.toString())
        }
    }

    inner class RedHolder(parent: ViewGroup) : BaseViewHolder<HomeLiveTwentyNewsResponse>(getContext(), parent, R.layout.holder_fragment_chat_gift) {
        override fun onBindData(data: HomeLiveTwentyNewsResponse) {
            setGone(findView<SimpleDraweeView>(R.id.imgGift))
            when (data.vip) {
                "1" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v1)
                "2" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v2)
                "3" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v3)
                "4" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v4)
                "5" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v5)
                "6" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v6)
                "7" -> findView<ImageView>(R.id.imgVip).setBackgroundResource(R.mipmap.v7)
                else -> setGone(R.id.imgVip)
            }
            setText(R.id.tvUserName, data.userName)
            setText(R.id.tvGiftNum, "发出一个 ")
            setText(R.id.tvGiftName, data.gift_price + "元红包")
            setText(R.id.tvDes, " 大家快去抢啊")
        }
    }

}


