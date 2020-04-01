package com.fenghuang.caipiaobao.ui.home.live.room

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.hubert.guide.NewbieGuide
import com.app.hubert.guide.model.GuidePage
import com.app.hubert.guide.model.HighLight
import com.example.playerlibrary.AlivcLiveRoom.ScreenUtils
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.NetWorkUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.home.data.*
import com.fenghuang.caipiaobao.ui.mine.MinePresenter
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MinePassWordTime
import com.fenghuang.caipiaobao.utils.*
import com.fenghuang.caipiaobao.widget.ObjectAnimatorViw
import com.fenghuang.caipiaobao.widget.dialog.LiveRoomChatInputDialog
import com.fenghuang.caipiaobao.widget.dialog.PassWordDialog
import com.fenghuang.caipiaobao.widget.dialog.RedPaperDialog
import com.fenghuang.caipiaobao.widget.dialog.SendRedDialog
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomGiftWindow
import com.fenghuang.caipiaobao.widget.gift.RewardAdapter
import com.fenghuang.caipiaobao.widget.gift.bean.SendGiftBean
import com.fenghuang.caipiaobao.widget.pop.LiveManageRoomPop
import com.fenghuang.caipiaobao.widget.spanlite.SpanBuilder
import com.fenghuang.caipiaobao.widget.spanlite.SpanLite
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.dialog_chat_bottom_gif.*
import kotlinx.android.synthetic.main.fragment_child_live_chat.*
import java.util.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-22
 * @ Describe 聊天室
 *
 */

class LiveRoomChatFragment(private val anchorId: String, val liveState: String = "", val name: String = "") : BaseMvpFragment<LiveRoomChatPresenter>() {

    //socket
    private lateinit var mNetWorkReceiver: NetWorkChangReceiver

    var isBottom = true
    lateinit var runnable: Runnable

    var isShowPop: Boolean = false
    var popMenuManager: LiveManageRoomPop? = null
    //聊天适配器
    var chatAdapter: LiveRoomChatAdapter? = null
    //红包弹窗
    private var mOpenRedPopup: RedPaperDialog? = null

    override fun getLayoutResID() = R.layout.fragment_child_live_chat

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LiveRoomChatPresenter(anchorId)

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        chatSmartRefresh.setEnableOverScrollBounce(true)//是否启用越界回弹
        chatSmartRefresh.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        chatSmartRefresh.setEnableRefresh(false)//是否启用下拉刷新功能
        chatSmartRefresh.setEnableLoadMore(false)//是否启用上拉加载功能
        if (UserInfoSp.getOpenWindow()) setVisible(floatButton) else setGone(floatButton)
        upDateSystemNotice()
        if (liveState != "1") {
            setGone(bottomChat)
            setVisible(tvNoLive)
        } else {
            setGone(tvNoLive)
            setVisible(bottomChat)
        }

        initRecycle()
        //初始化礼物界面
        rewardLayout.setGiftItemRes(R.layout.gift_animation_item)
        rewardLayout.setGiftAdapter(RewardAdapter(requireActivity()))
//        rewardLayout.setCallBack(this)

        //引导层
        NewbieGuide.with(this).setLabel("guide1").
                addGuidePage(GuidePage().
                        addHighLight(imgBuyLottery,HighLight.Shape.CIRCLE).setLayoutRes(R.layout.guide_live)).show()
    }

    override fun initData() {
        mPresenter.getAllData(anchorId)
        initSocket()
        mPresenter.homeLiveRedList(anchorId, false)
    }

    override fun initEvent() {
        floatButton.setOnClickListener {
            showOrCloseFloatView()
        }
        tvRoomBottomShowKeyBord.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            val chatPop = LiveRoomChatInputDialog(getPageActivity(), R.style.inputDialog)
            chatPop.showEmojiOrKeyBord(false)
            chatPop.setSendClickListener {
                mPresenter.sendMessage(it)
                chatPop.dismiss()
            }
        }
        imgRoomBottomShowEmoji.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            val chatPop = LiveRoomChatInputDialog(getPageActivity(), R.style.inputDialog)
            chatPop.showEmojiOrKeyBord(true)
            chatPop.setSendClickListener {
                mPresenter.sendMessage(it)
                chatPop.dismiss()
            }
        }
        //底部有新消息
        tvMoreInfo.setOnClickListener {
            chatAdapter?.let { it1 -> scrollToBottom(rvLiveRoomChat, it1) }
            setGone(tvMoreInfo)
        }
        //礼物弹框
        imgGift.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(getPageActivity())
                return@setOnClickListener
            }
            initGitWidow()

        }
        //充值
        imgRecharge.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
        }
        //发红包
        imgRed.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!UserInfoSp.getIsSetPayPassWord()) {
                mPresenter.getIsSetPayPassWord()
            } else sendRed()
        }

        //存款
        imgRecharge.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            LaunchUtils.startRechargePage(getPageActivity(), 0)
        }

        imgBuyLottery.setOnClickListener {
            RxBus.get().post(JumpToBuyLottery(true))
        }
    }


    /**
     * 发红包弹框
     */
    fun sendRed() {
        val dialog = SendRedDialog(requireActivity(), false)
        dialog.setOnSendClickListener { total, redNumber, redContent ->
            dialog.dismiss()
            initPassWordDialog(total, redNumber, redContent)
        }
        dialog.show()
    }

    /**
     * 密码框弹窗
     */
    private fun initPassWordDialog(total: String, redNumber: String, redContent: String) {
        val passWordDialog = PassWordDialog(context!!, ViewUtils.getScreenWidth(), ViewUtils.dp2px(156))
        passWordDialog.setTextWatchListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    passWordDialog.showOrHideLoading()
                    //验证支付密码
                    MineApi.verifyPayPass(s.toString()) {
                        onSuccess {
                            //发红包
                            mPresenter.homeLiveSendRedEnvelope(anchorId, total, redNumber, redContent, s.toString(), passWordDialog)
                        }
                        onFailed {
                            passWordDialog.showOrHideLoading()
                            if (it.getCode() == 1002) {
                                passWordDialog.showTipsText(it.getMsg().toString() + "," + "您还有" +
                                        JsonUtils.fromJson(it.getDataCode().toString(), MinePassWordTime::class.java).remain_times.toString() +
                                        "次机会")
                                passWordDialog.clearText()
                            } else {
                                passWordDialog.showTipsText(it.getMsg().toString())
                            }
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        passWordDialog.show()
    }


    /**
     * 有红包
     */
    private var showIt: Boolean = false
    private var homeLiveRedRoomBean: HomeLiveRedRoom? = null
    fun initRedDialog(homeLiveRedRoom: HomeLiveRedRoom, isOpen: Boolean) {
        homeLiveRedRoomBean = homeLiveRedRoom
        if (mOpenRedPopup == null) {
            mOpenRedPopup = RedPaperDialog(context!!)
        }
        //抢红包
        mOpenRedPopup?.setOnOpenClickListener {
            showIt = true
            mPresenter.getRed(homeLiveRedRoom.id, mOpenRedPopup!!)
        }
        mOpenRedPopup?.setOnDismissListener {
            if (showIt) {
                mPresenter.homeLiveRedList(anchorId, true)
                showIt = false
            }
            if (isFullScreen()) {
                val decorView = ScreenUtils.getDecorView(getPageActivity())
                if (decorView != null) ScreenUtils.hideSysBar(getPageActivity(), decorView)
            }
        }
        //点击小红包
        liveRedTips.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(requireActivity())
                    return@setOnClickListener
                }
                mOpenRedPopup?.show()
            }
        }
        if (isOpen) {
            if (!mOpenRedPopup?.isShowing!!) mOpenRedPopup?.show()
        }
    }

    //红包动画
    fun startRedAnimation() {
        setVisible(liveRedTips)
        val animator = ObjectAnimatorViw.nope(liveRedTips, 3f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.start()
        RxBus.get().post(RedTips(true))
    }

    //隐藏小红包和动画
    fun stopRedAnimation() {
        if (liveRedTips != null && liveRedTips.animation != null) {
            liveRedTips.clearAnimation()
        }
        setGone(liveRedTips)
        RxBus.get().post(RedTips(false))
    }

    /**
     * 礼物弹框
     */
    var bottomGiftWindow: BottomGiftWindow? = null

    private fun initGitWidow() {
        if (bottomGiftWindow == null) {
            bottomGiftWindow = BottomGiftWindow(requireContext())
            bottomGiftWindow?.show()
            mPresenter.getGiftList()
        } else bottomGiftWindow?.show()
        val presenter = MinePresenter()
        presenter.getUserDiamond()
        presenter.getUserDiamondSuccessListener {
            bottomGiftWindow?.tvDiamondTotal?.text = it
        }
        presenter.getUserDiamondFailedListener {
            GlobalDialog.ShowError(getPageActivity(), it)
        }


    }


    // ========= 公告 =========
    @SuppressLint("SetTextI18n")
    private fun upDateSystemNotice() {
        if (TextUtils.isEmpty(name)) mtvLiveRoom.text = "暂无公告" else mtvLiveRoom.text = "欢迎来到 $name 的直播间,喜欢就点关注吧。"
        mtvLiveRoom.setTextColor(getColor(R.color.color_333333))
    }

    //拖拽按钮
    private fun showOrCloseFloatView() {
        if (popMenuManager == null) {
            popMenuManager = LiveManageRoomPop(context!!)
            popMenuManager?.setSendClickListener {
                if (it) {
                    //收起
                    RxBus.get().post(LiveVideoClose(it))
                } else {
                    //展开
                    RxBus.get().post(LiveVideoClose(it))
                }
            }
            //清屏
            popMenuManager?.setClearClickListener {
                chatAdapter?.clear()
                chatAdapter?.notifyDataSetChanged()
            }
            //管理清屏
            popMenuManager?.setManagerClearClickListener {
                mPresenter.managerClear(anchorId)
            }

        }
        isShowPop = if (!isShowPop) {
            popMenuManager!!.showAtLocationBottom(findView(R.id.floatButton), 5f)
            true
        } else {
            popMenuManager!!.dismiss()
            false
        }


    }


    /**
     * 初始化socket
     */
    private fun initSocket() {
        //初始化socket
        mNetWorkReceiver = NetWorkChangReceiver()
        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        getPageActivity().registerReceiver(mNetWorkReceiver, filter)
    }

    /**
     * 初始化recycle
     */
    private fun initRecycle() {
        chatAdapter = LiveRoomChatAdapter(getPageActivity())
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvLiveRoomChat?.layoutManager = layoutManager
        rvLiveRoomChat?.adapter = chatAdapter
        // 获取列表的滑动事件，控制一键到底部
        rvLiveRoomChat?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isBottom = isStayBottom(rvLiveRoomChat!!, chatAdapter!!)
                }
            }
        })
    }


    //Socket
    inner class NetWorkChangReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (NetWorkUtils.isNetworkConnected()) {
                mPresenter.startWebSocketConnect()
            } else {
                LogUtils.d("WsManager----------网络不可用")
                mPresenter.stopConnect()
            }
        }
    }

    /**
     * 是否停留在列表底部
     */
    fun isStayBottom(recyclerView: RecyclerView, multiTypeAdapter: LiveRoomChatAdapter): Boolean {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
        return layoutManager?.findLastVisibleItemPosition() == multiTypeAdapter.itemCount - 1
    }

    /**
     * 滑动到最底部
     */
    fun scrollToBottom(recyclerView: RecyclerView, multiTypeAdapter: LiveRoomChatAdapter) {
        if (!isStayBottom(recyclerView, multiTypeAdapter)) {
            recyclerView.smoothScrollToPosition((recyclerView.layoutManager as? LinearLayoutManager)?.itemCount
                    ?: 0)
            setGone(tvMoreInfo)
        }
    }

    //首冲
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun IsFirstRecharge(eventBean: IsFirstRecharge) {
        val par = imgRecharge.layoutParams as LinearLayout.LayoutParams
        if (eventBean.res) {
            imgRecharge.setBackgroundResource(R.mipmap.ic_live_first_recharge)
            par.height = ViewUtils.dp2px(25)
            imgRecharge.layoutParams = par
            val animator = ObjectAnimatorViw.tada(imgRecharge, 1f)
            animator.repeatCount = ValueAnimator.INFINITE
            animator.start()
        } else {
            imgRecharge.clearAnimation()
            imgRecharge.setBackgroundResource(R.mipmap.ic_live_chat_recharge)
            par.height = ViewUtils.dp2px(25)
            par.width = ViewUtils.dp2px(20)
            imgRecharge.layoutParams = par
        }
    }

    /**
     * 横幅接收动画
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataMineUserDiamond(eventBean: HomeLiveAnimatorBean) {
        mPresenter.homeLiveSendGift(anchorId, eventBean.gift_id, eventBean.giftCount, eventBean)
    }


    /**
     * 横幅接收动画
     */
    fun showAnim(eventBean: HomeLiveAnimatorBean) {
        val time: Long
        //展示3秒
        when (eventBean.gift_id) {
            "18",
            "19",
            "20",
            "22",
            "24",
            "25",
            "26",
            "30",
            "31",
            "32",
            "33",
            "47",
            "48",
            "49",
            "50" -> time = 3000
            "16",     //烟花城堡
            "17",    //凤凰机车
            "21",   //口红
            "28",  //火凤凰
            "29"  //游艇
            -> time = 5000

            else -> time = 8000
        }
        try {
            val bean = SendGiftBean(eventBean.user_id.toInt(), eventBean.gift_id.toInt(),
                    eventBean.user_name, eventBean.git_name, eventBean.gift_icon,
                    eventBean.user_icon, eventBean.giftCount, time)
            rewardLayout.put(bean)
        } catch (e: Exception) {
        }
    }


    /**
     * 进场 vip 或者普通提醒
     */

    @SuppressLint("SetTextI18n")
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun enter(eventBean: EnterVip) {
        if (eventBean.vip == "0") {
            tvEnterContent.text = "欢迎 " + UserInfoSp.getUserNickName() + " 进入直播间"
            mPresenter.setVip(eventBean.vip, imgEnterImg)
            ObjectAnimatorViw.setShowAnimation(linEnter, 1000)
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (isSupportVisible && linEnter != null) {
                        ObjectAnimatorViw.setHideAnimation(linEnter, 1000)
                    }
                    this.cancel()
                }
            }, 3000)
        } else {
            SpanLite.with(tvVipEnter).append(SpanBuilder.Builder("VIP" + eventBean.vip).drawTextColor("#FF513E").drawTypeFaceBold().drawTypeFaceItalic().build())
                    .append(SpanBuilder.Builder(" 贵族 ").drawTextColor("#333333").build())
                    .append(SpanBuilder.Builder(UserInfoSp.getUserNickName()).drawTextColor("#D3904E").build())
                    .append(SpanBuilder.Builder(" 驾临直播间 ").drawTextColor("#333333").build()).active()
            AnimUtils.getInAnimation(context!!, tvVipEnter)
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (view != null) {
                        AnimUtils.getOutAnimation(context!!, tvVipEnter)
                        this.cancel()
                    }
                }
            }, 5000)
        }
    }


    //红包
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun Gift(eventBean: Gift) {
        eventBean.gift?.let { mPresenter.notifySocket(it) }
    }

    //横屏红包点击
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun RedPaperClick(eventBean: RedPaperClick) {
        if (homeLiveRedRoomBean != null) {
            initRedDialog(homeLiveRedRoomBean!!, true)
        }
    }

    //横屏发送弹幕
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun SendDanMu(eventBean: SendDanMu) {
        mPresenter.sendMessage(eventBean.content)
    }

    // @某个人
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun call(eventBean: LiveCallPersonal) {
        if (!UserInfoSp.getIsLogin()) {
            GlobalDialog.notLogged(requireActivity())
        } else {
            val chatPop = LiveRoomChatInputDialog(getPageActivity(), R.style.inputDialog)
            chatPop.showEmojiOrKeyBord(false)
            chatPop.initTextChat("@" + eventBean.name + " ")
            chatPop.setSendClickListener {
                mPresenter.sendMessage(it)
                chatPop.dismiss()
            }
        }
    }
//    //礼物连击结束后回调
//    override fun giftNum(num: GiftIdentify?) {
//        if (num?.isMeSend!!){
//            mPresenter.homeLiveSendGift(anchorId, num.theGiftId.toString(), num.theGiftCount.toString(),num)
//
//        }
//    }

    fun showToast() {
        if (isFullScreen()) {
            ToastUtils.show("发送成功")
        }
        if (bottomGiftWindow != null) {
            bottomGiftWindow?.hideLoading()
            val presenter = MinePresenter()
            presenter.getUserDiamond()
            presenter.getUserDiamondSuccessListener {
                bottomGiftWindow?.tvDiamondTotal?.text = it
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            chatAdapter?.let { scrollToBottom(rvLiveRoomChat, it) }
        }
    }


    override fun onPause() {
        super.onPause()
        if (rewardLayout != null) {
            rewardLayout.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (rewardLayout != null) {
            rewardLayout.onResume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        getPageActivity().unregisterReceiver(mNetWorkReceiver)
        mPresenter.stopConnect()
        if (rewardLayout != null) {
            rewardLayout.onDestroy()
        }
    }

    fun getScreenFull(): Boolean {
        return isFullScreen()
    }
}