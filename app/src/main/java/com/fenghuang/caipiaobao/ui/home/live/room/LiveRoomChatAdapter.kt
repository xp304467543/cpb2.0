package com.fenghuang.caipiaobao.ui.home.live.room

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveTwentyNewsResponse
import com.fenghuang.caipiaobao.ui.home.data.LiveCallPersonal
import com.fenghuang.caipiaobao.ui.home.live.room.betting.LiveRoomBetAccessFragment
import com.fenghuang.caipiaobao.ui.lottery.data.BetShareBean
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryBet
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryBetAccess
import com.fenghuang.caipiaobao.ui.lottery.data.PlaySecData
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.JsonUtils
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.dialog.ForbiddenWordsDialog
import com.hwangjr.rxbus.RxBus
import org.json.JSONException
import org.json.JSONObject


/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-25
 * @ Describe
 *
 */

class LiveRoomChatAdapter(context: Context,var fragmentManager: FragmentManager) : BaseRecyclerAdapter<HomeLiveTwentyNewsResponse>(context) {


    val TYPE_NORMAL = 0
    val TYPE_GIFT = 1
    val TYPE_RED = 2
    val TYPE_SHARE_ORDER = 3

    var followList = arrayListOf<String>()

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeLiveTwentyNewsResponse> {
        return when (viewType) {
            TYPE_NORMAL -> NormalHolder(parent)
            TYPE_RED -> RedHolder(parent)
            TYPE_SHARE_ORDER -> ShareBetOrder(parent)
            else -> GiftHolder(parent)
        }
    }

    override fun getItemCount(): Int {
        return getAllData().size
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            getAllData()[position].type == "publish" -> {
                //分享注单
                if (getAllData()[position].event == "pushPlan") TYPE_SHARE_ORDER else TYPE_NORMAL
            }
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
            when (data.userType) {
                "2" -> findView<ImageView>(R.id.imgVipLevel).setBackgroundResource(R.mipmap.ic_live_chat_manager)
                "1" -> findView<ImageView>(R.id.imgVipLevel).setBackgroundResource(R.mipmap.ic_live_anchor)
                "0" -> when (data.vip) {
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

    inner class ShareBetOrder(parent: ViewGroup) : BaseViewHolder<HomeLiveTwentyNewsResponse>(getContext(), parent, R.layout.holder_share_bet_order) {

        private lateinit var recyclerView: RecyclerView

        private lateinit var childAdapter: ShareAdapter


        override fun onBindView(context: Context?) {
            context?.apply {
                recyclerView = findView(R.id.betLin)
                recyclerView.setItemViewCacheSize(50)
                childAdapter = ShareAdapter(this)
                recyclerView.layoutManager = object : LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                recyclerView.adapter = childAdapter
            }
        }


        override fun onBindData(data: HomeLiveTwentyNewsResponse) {
            ImageManager.loadImg(data.avatar, findView(R.id.imgOrderUserPhoto))
            if (data.orders != null) {
                try {
                    val tvShowMore = findView<TextView>(R.id.tvShowMore)
                    val followBt = findView<Button>(R.id.btFollow)
                    val result = JSONObject(data.orders!!.toString())
                    setText(R.id.tvOrderName, result.getString("lottery_cid"))
                    setText(R.id.tvOrderIssIue, result.getString("play_bet_issue") + "期")
                    if (!followList.contains(result.getString("play_bet_issue")+","+getDataPosition())){
                        followList.add(result.getString("play_bet_issue")+","+getDataPosition())
                    }
                    val arrayList = JsonUtils.fromJson(result.getString("order_detail"), Array<BetShareBean>::class.java)
                    setText(R.id.tv_t1, arrayList[0].play_sec_cname)
                    setText(R.id.tv_t2, arrayList[0].play_class_cname)
                    setText(R.id.tv_t3, arrayList[0].play_odds.toString())
                    setText(R.id.tv_t4, arrayList[0].play_bet_sum)
                    if (arrayList.size > 1) {
                        setVisible(tvShowMore)
                    } else setGone(tvShowMore)

                    if (getData()?.childIsShow == true) {
                        tvShowMore.text = "收起"
                        tvShowMore.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.mipmap.ic_order_up), null)
                        setVisible(R.id.betLin)
                        childAdapter.clear()
                        recyclerView.removeAllViews()
                        childAdapter.addAll(arrayList)
                    } else {
                        tvShowMore.text = "展开"
                        tvShowMore.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.mipmap.ic_order_down), null)
                        setGone(R.id.betLin)
                    }
                    if (data.canFollow) {
                        followBt.isEnabled = true
                        followBt.background = ViewUtils.getDrawable(R.drawable.button_background)
                        followBt.setTextColor(ViewUtils.getColor(R.color.white))
                    } else {
                        followBt.isEnabled = false
                        followBt.background = ViewUtils.getDrawable(R.drawable.button_grey_background)
                        followBt.setTextColor(ViewUtils.getColor(R.color.color_999999))
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            setOnClick(R.id.tvShowMore)
            setOnClick(R.id.btFollow)
        }

        override fun onClick(id: Int) {
            if (id == R.id.tvShowMore) {
                val view = findView<TextView>(R.id.tvShowMore)
                getData()?.childIsShow = view.text.toString() == "展开"
                notifyItemChanged(getDataPosition())
            } else if (id == R.id.btFollow) {
                betFollow(getData())
            }
        }

        //跟投
        private fun betFollow(data: HomeLiveTwentyNewsResponse?) {
            if (FastClickUtils.isFastClick()){
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(getContext() as Activity )
                    return
                }
                val result = JSONObject(data?.orders!!.toString())
                val array = JsonUtils.fromJson(result.getString("order_detail"), Array<BetShareBean>::class.java)
                val arrayList = arrayListOf<LotteryBet>()
                for (res in array) {
                    arrayList.add(LotteryBet(PlaySecData(play_class_cname = res.play_class_cname, play_class_id = 0, play_sec_name = res.play_sec_name,
                            play_class_name = res.play_class_name, play_odds = res.play_odds,money = "10"
                    ), res.play_sec_cname))
                }
                val liveRoomBetAccessFragment = LiveRoomBetAccessFragment.newInstance(LotteryBetAccess(arrayList, 1, 10*arrayList.size, result.getString("play_bet_lottery_id"),
                        result.getString("play_bet_issue"), "0x11", result.getString("lottery_cid"), "",true,data.user_id))
                liveRoomBetAccessFragment.show(fragmentManager, "liveRoomBetAccessFragment")
            }
        }

        inner class ShareAdapter(context: Context) : BaseRecyclerAdapter<BetShareBean>(context) {

            override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BetShareBean> {
                return ShareChildHolder(parent)
            }

            inner class ShareChildHolder(parent: ViewGroup) : BaseViewHolder<BetShareBean>(getContext(), parent, R.layout.holder_shaer_bet_order_child) {
                override fun onBindData(data: BetShareBean) {
                    if (getDataPosition() != 0) {
                        setText(R.id.tv_1, data.play_sec_cname)
                        setText(R.id.tv_2, data.play_class_cname)
                        setText(R.id.tv_3, data.play_odds.toString())
                        setText(R.id.tv_4, data.play_bet_sum)
                    } else {
                        val param = itemView.layoutParams
                        param.height = 0
                        param.width = 0
                        setGone(itemView)
                    }
                }
            }
        }
    }
}


