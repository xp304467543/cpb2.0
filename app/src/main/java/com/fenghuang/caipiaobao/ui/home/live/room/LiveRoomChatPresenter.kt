package com.fenghuang.caipiaobao.ui.home.live.room

import android.annotation.SuppressLint
import android.os.Handler
import android.view.View
import android.widget.ImageView
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.data.api.WebUrlProvider
import com.fenghuang.caipiaobao.socket.WsManager
import com.fenghuang.caipiaobao.socket.listener.WsStatusListener
import com.fenghuang.caipiaobao.ui.home.data.*
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.JsonUtils
import com.fenghuang.caipiaobao.widget.ObjectAnimatorViw
import com.fenghuang.caipiaobao.widget.dialog.PassWordDialog
import com.fenghuang.caipiaobao.widget.dialog.RedPaperDialog
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.fragment_child_live_chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.jessyan.autosize.utils.LogUtils
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.ByteString
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-25
 * @ Describe
 *
 */

class LiveRoomChatPresenter(private val anchorId: String) : BaseMvpPresenter<LiveRoomChatFragment>() {

    private val uiScope = CoroutineScope(Dispatchers.Main)

    var mWsManager: WsManager? = null
    private var mTimer: Timer? = null
    private var mWsStatusListener: WsStatusListener? = null
    private var isReconnect = false


    //获取直播间信息
    fun getAllData(anchorId: String) {
        if (mView.isActive()) {
            uiScope.launch {
                val getTwentyNews = async {
                    HomeApi.getTwentyNews(anchorId)
                }
                val getTwentyNewsResult = getTwentyNews.await()
                getTwentyNewsResult.onSuccess {
                    mView.chatAdapter!!.addAll(it)
                    if (mView.isSupportVisible && mView.rvLiveRoomChat != null) mView.scrollToBottom(mView.rvLiveRoomChat, mView.chatAdapter!!)
                }
            }
        }
    }


    //查询是否设置支付密码
    fun getIsSetPayPassWord() {
        mView.showPageLoadingDialog()
        MineApi.getIsSetPayPass {
            onSuccess {
                mView.hidePageLoadingDialog()
                UserInfoSp.putIsSetPayPassWord(true)
                mView.sendRed()

            }
            onFailed {
                mView.hidePageLoadingDialog()
                GlobalDialog.ShowError(mView.requireActivity(), it)
            }
        }
    }


    //发红包
    fun homeLiveSendRedEnvelope(anchorId: String, amount: String, num: String, text: String, password: String, passWordDialog: PassWordDialog) {
        HomeApi.homeLiveSendRedEnvelope(anchorId, amount, num, text, password) {
            onSuccess {
                //通知socket
                mWsManager?.sendMessage(LiveRoomChaPresenterHelper.getGifParams(anchorId, "4", it.rid, "", amount, num, text, "", ""))
                passWordDialog.showOrHideLoading()
                passWordDialog.dismiss()
                ToastUtils.show("红包发送成功")
            }
            onFailed {
                passWordDialog.showOrHideLoading()
                passWordDialog.dismiss()
                GlobalDialog.ShowError(mView.requireActivity(), it)
            }
        }
    }

    //获取直播间红包队列
    fun homeLiveRedList(anchorId: String, isOpen: Boolean) {
        HomeApi.homeLiveRedList(anchorId) {
            if (mView.isActive()) {
                onSuccess {
                    if (it.isNullOrEmpty()) {
                        mView.stopRedAnimation()
                    } else {
                        mView.startRedAnimation()
                        mView.initRedDialog(it[it.size - 1], isOpen) //展示右下角红包
                    }
                }
                onFailed { ToastUtils.show(it.getMsg().toString()) }
            }
        }
    }

    //抢红包
    fun getRed(rid: String, redPaperDialog: RedPaperDialog) {
        mView.showPageLoadingDialog()
        HomeApi.homeGetRed(rid) {
            if (mView.isActive()) {
                onSuccess {
                    //开红包
                    mView.stopRedAnimation()
                    redPaperDialog.showGetRed(it.send_user_name, it.send_text, it.amount, it.send_user_avatar)
                    mView.hidePageLoadingDialog()
                }
                onFailed {
                    if (it.getCode() == 2) {
                        // 红包被抢完了
                        mView.stopRedAnimation()
                        val bean = JsonUtils.fromJson(it.getDataCode().toString(), HomeLiveRedReceiveBean::class.java)
                        redPaperDialog.noGetRed(bean.send_user_name, bean.send_text, bean.send_user_avatar)
                        mView.hidePageLoadingDialog()
                    } else GlobalDialog.ShowError(mView.requireActivity(), it, mView.getScreenFull())
                }
            }
        }
    }


    //获取礼物列表
    fun getGiftList() {
        HomeApi.getGiftList {
            if (mView.isActive()) {
                onSuccess { result ->
                    val json = JsonParser().parse(result).asJsonObject
                    val typeList = json.get("typeList").asJsonObject
                    val data = json.get("data").asJsonObject
                    val type = arrayListOf<String>()
                    val content = ArrayList<List<HomeLiveGiftList>>()
                    repeat(typeList.size()) {
                        val realPosition = (it + 1).toString()
                        type.add(typeList.get(realPosition).asString)
                        val res = data.get(realPosition).asJsonArray
                        val bean = arrayListOf<HomeLiveGiftList>()
                        for (op in res) {
                            val beanData = JsonUtils.fromJson(op, HomeLiveGiftList::class.java)
                            bean.add(beanData)
                        }
                        content.add(bean)
                    }
                    if (mView.bottomGiftWindow != null) {
                        mView.bottomGiftWindow!!.setData(type, content)
                    }
                }
                onFailed { }
            }
        }
    }

    // 送礼物
    fun homeLiveSendGift(anchorId: String, gift_id: String, gift_num: String, bean: HomeLiveAnimatorBean) {
        HomeApi.setGift(   UserInfoSp.getUserId(), anchorId, gift_id, gift_num) {
            if (mView.isActive()) {
                onSuccess {
                    //通知scoket
                    notifySocket(LiveRoomChaPresenterHelper.getGifParams(anchorId, "1", "", bean.git_name, "",
                            bean.giftCount, "", bean.gift_id, bean.gift_icon))
                    mView.showToast()
                    if (mView.getScreenFull()) RxBus.get().post(UpDataHorDiamon(true))
                }
                onFailed {
                    GlobalDialog.ShowError(mView.requireActivity(), it, mView.getScreenFull())
                    mView.hidePageLoadingDialog()
                    if (mView.bottomGiftWindow != null) {
                        mView.bottomGiftWindow?.hideLoading()
                    }
                }
            }
        }
    }

    //管理员清屏
    fun managerClear(anchor_id: String) {
        HomeApi.managerClear(anchor_id) {
            onSuccess {
                if (mWsManager?.isWsConnected!!) {
                    mWsManager?.sendMessage(LiveRoomChaPresenterHelper.getManagerCommend(anchor_id))
                } else {
                    mWsManager?.startConnect()
                    ToastUtils.show("请重试！")
                }
            }
            onFailed { ToastUtils.show("清屏失败") }
        }
    }

    //初始化socket
    fun startWebSocketConnect() {
        if (mView.isActive()) {
            mView.showPageLoadingDialog("聊天室连接中..")
            initStatusListener()
            mWsManager = WsManager.Builder(mView.requireActivity())
                    .client(OkHttpClient().newBuilder()
                            .pingInterval(1000 * 50, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .build())
                    .needReconnect(true).wsUrl(WebUrlProvider.API_URL_WEB_SOCKET)
                    .build()
            mWsManager?.setWsStatusListener(mWsStatusListener)
            mWsManager?.startConnect()
        }
    }


    /**
     * 释放wobSocket
     */
    fun stopConnect() {
        if (null != mWsManager) {
            mWsManager?.stopConnect()
            mWsManager = null
        }
    }

    /**
     * 发送一条聊天消息
     */
    fun sendMessage(content: String) {
        mWsManager?.sendMessage(LiveRoomChaPresenterHelper.getPublishParams(anchorId, content))
    }

    /**
     * 通知socket
     */
    fun notifySocket(content: String) {
        mWsManager?.sendMessage(content)
    }

    private fun initStatusListener() {
        mWsStatusListener = object : WsStatusListener() {
            override fun onOpen(response: Response) {
                super.onOpen(response)
                mView.hidePageLoadingDialog()
                LogUtils.d("WsManager-----onOpen response=$response")
                mWsManager?.sendMessage(LiveRoomChaPresenterHelper.getSubscribeParams(isReconnect, anchorId))
                if (mTimer == null) mTimer = Timer()
                mTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        mWsManager?.sendMessage(LiveRoomChaPresenterHelper.getPingParams(anchorId))
                        LogUtils.d("WsManager-----发送了心跳")
                    }
                }, 0, 1000 * 54)
            }

            override fun onMessage(text: String) {
                super.onMessage(text)
                if (mView.isActive()) initChatText(text)
                LogUtils.e("WsManager-----c$text")
            }

            override fun onMessage(bytes: ByteString) {
                super.onMessage(bytes)
                LogUtils.d("WsManager-----onMessage$bytes")
            }

            override fun onReconnect() {
                super.onReconnect()
                isReconnect = true
                LogUtils.d("WsManager-----onReconnect")

            }

            override fun onClosing(code: Int, reason: String) {
                super.onClosing(code, reason)
                LogUtils.d("WsManager-----onClosing")
            }

            override fun onClosed(code: Int, reason: String) {
                super.onClosed(code, reason)
                LogUtils.d("WsManager-----onClosed")
            }

            override fun onFailure(t: Throwable?, response: Response?) {
                super.onFailure(t, response)
                LogUtils.d("WsManager-----onFailure$response=$t")
                if (mTimer != null) {
                    mTimer?.cancel()
                    mTimer = null
                }
            }
        }
    }

    //信息接收
    @SuppressLint("SetTextI18n")
    private fun initChatText(text: String) {
        val data = WebUrlProvider.getData<HomeLiveChatBeanNormal>(text, HomeLiveChatBeanNormal::class.java)
        LogUtils.d("WsManager-----111onMessage${data.toString()}")
        if (data != null) {
            if (mView.isActive()) {
                //接收消息
                when (data.type) {
                    //聊天内容
                    LiveRoomChaPresenterHelper.TYPE_PUBLISH -> {
                        val res = HomeLiveTwentyNewsResponse(data.type, data.room_id, data.user_id,
                                data.userName, data.text, data.vip,
                                data.userType, data.avatar, data.sendTime,
                                data.sendTimeTxt)
                        mView.chatAdapter?.add(res)
                        RxBus.get().post(DanMu(data.userName, data.userType, data.text, data.vip))
                        scrollBottom()//滑动到底部
                    }
                    //进场提示
                    LiveRoomChaPresenterHelper.TYPE_SUBSCRIBE -> {
                        if (!data.isMe) {
                            //普通进场
                            if (data.vip == "0" || data.vip == "") {
                                val text = "欢迎 " + data.userName + " 进入直播间"
                                mView.tvEnterContent.text = text
                                setVip(data.vip, mView.imgEnterImg)
                                ObjectAnimatorViw.setShowAnimation(mView.linEnter, 1000)
                                Timer().schedule(object : TimerTask() {
                                    override fun run() {
                                        if (mView.isSupportVisible && mView.linEnter != null) {
                                            ObjectAnimatorViw.setHideAnimation(mView.linEnter, 1000)
                                        }
                                        this.cancel()
                                    }
                                }, 5000)
                            } else {
                                //vip进场 进入vip队列
                                LiveRoomVipEnterTask(mView.requireContext(), data.vip + "," + data.userName, mView.tvVipEnter).enqueue()
                            }
                        }

                    }
                    //主播长龙 队列
                    LiveRoomChaPresenterHelper.TYPE_SERVER_PUSH -> {
                        if (data.data == null) return
                        when {
                            data.dataType == "long_dragon" -> for ((index, res) in data.data?.asJsonArray!!.withIndex()) {
                                val bean = Gson().fromJson(res, HomeLiveChatChildBean::class.java)
                                if (mView.isSupportVisible) {
                                    if (index % 2 == 0) {
                                        LiveRoomChatTask(mView.requireContext(), bean, mView.tvAnchorOpenPrise).enqueue()
                                    } else LiveRoomChatTask(mView.requireContext(), bean, mView.tvAnchorOpenPrise2).enqueue()
                                    //                                                .setDuration(5000) //设置了时间，代表这个任务时间是确定的，如果不确定，则不用设置
                                    //                                                .setPriority(TaskPriority.DEFAULT) //设置优先级，默认是DEFAULT
                                    //                                                .enqueue(); //入队
                                }
                            }
                            data.dataType == "update_online" -> {
                                RxBus.get().post(OnLineInfo(data.data!!.asJsonObject.get("online").asInt))
                            }
                        }
                    }
                    //管理员清屏
                    LiveRoomChaPresenterHelper.TYPE_COMMEND -> {
                        if (data.commend == "clear") {
                            mView.chatAdapter?.clear()
                            mView.chatAdapter?.notifyDataSetChanged()
                        }
                    }
                    //礼物
                    LiveRoomChaPresenterHelper.TYPE_GIFT -> {
                        if (data.type == "gift") {
                            when {
                                //礼物
                                data.gift_type == "1" -> {
                                    showAnim(data)//显示动画
                                    if (data.sendTime - showBean.sendTime <= 10 && data.gift_id == showBean.gift_id && data.user_id == showBean.user_id && data.gift_num == showBean.gift_num) {
                                        for ((index, res) in mView.chatAdapter?.getAllData()!!.withIndex()) {
                                            if (res.sendTime == showBean.sendTime) {
                                                showBean = HomeLiveTwentyNewsResponse(res.type, gift_name = res.gift_name, userType = res.userType,
                                                        gift_num = res.gift_num, icon = res.icon, userName = res.userName, gift_id = res.gift_id,
                                                        vip = res.vip, user_id = res.user_id, sendTime = res.sendTime, final_num = res.final_num + 1
                                                )
                                                mView.chatAdapter!!.update(showBean, index)
                                            }
                                        }
                                    } else {
                                        showBean = HomeLiveTwentyNewsResponse(data.type, gift_name = data.gift_name, userType = data.userType,
                                                gift_num = data.gift_num, icon = data.icon, userName = data.userName, gift_id = data.gift_id,
                                                vip = data.vip, user_id = data.user_id, sendTime = data.sendTime, final_num = 1
                                        )
                                        mView.chatAdapter?.add(showBean)
                                    }
                                    scrollBottom()
                                }
                                //红包
                                data.gift_type == "4" -> {
                                    val bean = HomeLiveTwentyNewsResponse(gift_type = data.gift_type,
                                            gift_num = data.gift_num, userName = data.userName,
                                            vip = data.vip, gift_price = data.gift_price, userType = data.userType
                                    )
                                    mView.chatAdapter?.add(bean)
                                    mView.startRedAnimation()
                                    val result = HomeLiveRedRoom(id = data.r_id, text = data.gift_text, userName = data.userName, avatar = data.avatar)
                                    mView.initRedDialog(result, true)
                                }
                            }
                        }
                        scrollBottom()
                    }
                    LiveRoomChaPresenterHelper.TYPE_ERROE -> {
                        ToastUtils.show(data.msg)
                    }
                }
            }
        }
    }

    var showBean = HomeLiveTwentyNewsResponse()
    var handler = Handler()

    fun setVip(vip: String, imageView: ImageView) {
        val id = when (vip) {
            "1" -> R.mipmap.v1
            "2" -> R.mipmap.v2
            "3" -> R.mipmap.v3
            "4" -> R.mipmap.v4
            "5" -> R.mipmap.v5
            "6" -> R.mipmap.v6
            "7" -> R.mipmap.v7
            else -> 0
        }
        if (id != 0) {
            mView.setVisible(mView.imgEnterImg)
            mView.setImageResource(imageView, id)
        } else mView.setGone(mView.imgEnterImg)
    }

    private fun scrollBottom() {
        if (mView.isBottom) {
            mView.scrollToBottom(mView.rvLiveRoomChat, mView.chatAdapter!!)
        } else {
            mView.tvMoreInfo.visibility = View.VISIBLE
        }
    }

    private fun showAnim(data: HomeLiveChatBeanNormal) {
        val bean = HomeLiveAnimatorBean(data.gift_id, data.gift_name, data.icon, data.user_id, data.avatar, data.userName, data.gift_num)
        mView.showAnim(bean)
//        when (data.gift_id) {
//            "16", "15", "17", "21", "23", "29", "28", "27", "51", "52", "53", "54", "41", "42", "43", "44", "45", "46"
//            -> {
        val ben = HomeLiveBigAnimatorBean(data.gift_id, data.gift_name, data.icon, data.user_id, data.avatar, data.userName, data.gift_num)
        RxBus.get().post(ben)
//            }
//        }
    }
}