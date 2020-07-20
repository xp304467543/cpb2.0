package com.fenghuang.caipiaobao.ui.home.live

import android.content.Intent
import android.content.res.Configuration
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.playerlibrary.AlivcLiveRoom.ScreenUtils
import com.example.playerlibrary.utils.WindowPermissionCheck
import com.fenghuang.baselib.base.adapter.BaseFragmentPageAdapter
import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.baselib.base.mvp.BaseMvpActivity
import com.fenghuang.baselib.utils.StatusBarUtils.setStatusBarHeight
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.IntentConstant
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.home.HomePresenter
import com.fenghuang.caipiaobao.ui.home.data.*
import com.fenghuang.caipiaobao.ui.home.live.room.LiveRoomAdvanceFragment
import com.fenghuang.caipiaobao.ui.home.live.room.LiveRoomAnchorFragment
import com.fenghuang.caipiaobao.ui.home.live.room.LiveRoomChatFragment
import com.fenghuang.caipiaobao.ui.home.live.room.LiveRoomRankFragment
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryDiamondNotEnough
import com.fenghuang.caipiaobao.ui.mine.MinePresenter
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MinePassWordTime
import com.fenghuang.caipiaobao.utils.*
import com.fenghuang.caipiaobao.widget.dialog.LiveRoomChatInputDialog
import com.fenghuang.caipiaobao.widget.dialog.PassWordDialog
import com.fenghuang.caipiaobao.widget.dialog.SendRedDialog
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomHorGiftWindow
import com.fenghuang.caipiaobao.widget.videoplayer.assist.DataInter
import com.fenghuang.caipiaobao.widget.videoplayer.cover.ControllerLiveCover
import com.google.android.material.tabs.TabLayout
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.activity_live_room.*
import kotlinx.android.synthetic.main.dialog_chat_bottom_gif.*
import me.yokeyword.fragmentation.SwipeBackLayout

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-16
 * @ Describe
 */
class LiveRoomActivity : BaseMvpActivity<LiveRoomPresenter>() {

    private lateinit var liveRoomActivityHelper: LiveRoomActivityHelper

    private lateinit var liveRoomDanMuHelper: LiveRoomDanMuHelper

    var onLine = 0

    lateinit var svgaUtils: SvgaUtils

    var liveState = false


    override val layoutResID = R.layout.activity_live_room

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LiveRoomPresenter()

    override fun isOverride() = true

    override fun isRegisterRxBus() = true

    override fun isSwipeBackEnable() = true


    override fun initContentView() {
        setStatusBarHeight(statusView)
        onLine = intent.getIntExtra(IntentConstant.LIVE_ROOM_ONLINE, 0)
        liveRoomActivityHelper = LiveRoomActivityHelper()
        liveRoomDanMuHelper = LiveRoomDanMuHelper()
        liveRoomActivityHelper.initComponent(this, videoContainer, intent.getBooleanExtra("isWindowBack", false))
        liveRoomDanMuHelper.initDanMu(viewDanMu, this)
        initTab()
        initSvga()
    }

    override fun initData() {
        showPageLoadingDialog()
        mPresenter.getAllData(intent.getStringExtra(IntentConstant.LIVE_ROOM_ANCHOR_ID)!!)
    }

    private fun initTab() {
        tabLiveRoom.addTab(tabLiveRoom.newTab().setText("聊天"))
        tabLiveRoom.addTab(tabLiveRoom.newTab().setText("主播"))
        tabLiveRoom.addTab(tabLiveRoom.newTab().setText("排行"))
        tabLiveRoom.addTab(tabLiveRoom.newTab().setText("预告"))
        initViewPager()
    }

    private fun initViewPager() {
        //初始化viewPager
        val anchorID = intent?.getStringExtra(IntentConstant.LIVE_ROOM_ANCHOR_ID) ?: "0"
        val fragments = ArrayList<BaseFragment>()
        fragments.add(LiveRoomChatFragment.newInstance(anchorID,
                intent?.getStringExtra(IntentConstant.LIVE_ROOM_ANCHOR_STATUE) ?: "",
                intent?.getStringExtra(IntentConstant.LIVE_ROOM_NICK_NAME)
                        ?: "", intent?.getStringExtra(IntentConstant.LIVE_ROOM_LOTTERY_ID) ?: "1"))
        fragments.add(LiveRoomAnchorFragment.newInstance(anchorID, intent?.getStringExtra(IntentConstant.LIVE_ROOM_ANCHOR_STATUE)
                ?: "0"))
        fragments.add(LiveRoomRankFragment.newInstance(anchorID))
        fragments.add(LiveRoomAdvanceFragment.newInstance(anchorID, intent?.getStringExtra(IntentConstant.LIVE_ROOM_NAME)
                ?: ""))
        val adapter = BaseFragmentPageAdapter(supportFragmentManager, fragments)
        xViewPageRoomLive.adapter = adapter
        xViewPageRoomLive.currentItem = 0
        xViewPageRoomLive.offscreenPageLimit = fragments.size
    }


    fun initAttention(data: HomeLiveEnterRoomResponse) {
        liveRoomActivityHelper.setDataLive(data)
        liveState = (data.live_status == "1")
        if (!intent.getBooleanExtra("isWindowBack", false)) {
            if (liveState) {
//            liveRoomActivityHelper.startPlay("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8")
                data.liveInfo?.get(2)?.liveUrl?.originPullUrl?.let { liveRoomActivityHelper.startPlay(it) }
            } else liveRoomActivityHelper.changeState(avatar = data.avatar)
        }
        attention(data.isFollow)
        //关注，取关
        tvAttention.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(this)
                return@setOnClickListener
            }
            val presenter = HomePresenter()
            presenter.attention(data.anchor_id, "")
            presenter.setSuccessClickListener {
                attention(it)
            }
            presenter.setFailClickListener {
                GlobalDialog.showError(this, it)
            }
        }
        val cover = liveRoomActivityHelper.mReceiverGroup?.getReceiver<ControllerLiveCover>(DataInter.ReceiverKey.KEY_CONTROLLER_live_COVER)
        cover?.liveState = data.live_status
        cover?.anchorId = data.anchor_id
        cover?.avatar = data.avatar
        cover?.nickName = data.nickname
        cover?.upDateView(data.avatar, data.nickname, data.anchor_id)
    }

    //关注取关
    fun attention(boolean: Boolean) {
        if (boolean) {
            setGone(imgAttention)
            tvAttention.text = "已关注"
            setTextColor(tvAttention, ViewUtils.getColor(R.color.grey_97))
            linAttention.setBackgroundResource(R.color.color_EEEEEE)
        } else {
            setVisible(imgAttention)
            tvAttention.text = "关注"
            setTextColor(tvAttention, ViewUtils.getColor(R.color.white))
            linAttention.setBackgroundResource(R.drawable.background_gradient)
        }
        val cover = liveRoomActivityHelper.mReceiverGroup?.getReceiver<ControllerLiveCover>(DataInter.ReceiverKey.KEY_CONTROLLER_live_COVER)
        cover?.setAttention(boolean)
    }

    //控制器点击的关注
    fun controlAttention() {
        if (!UserInfoSp.getIsLogin()) {
            GlobalDialog.notLogged(this)
            return
        }
        val presenter = HomePresenter()
        presenter.attention(intent.getStringExtra(IntentConstant.LIVE_ROOM_ANCHOR_ID)!!, "")
        presenter.setSuccessClickListener {
            attention(it)
        }
        presenter.setFailClickListener {
            GlobalDialog.showError(this, it, true)
        }
    }


    override fun initEvent() {
        //TabLayout监听
        tabLiveRoom.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0?.text) {
                    "聊天" -> xViewPageRoomLive.currentItem = 0
                    "主播" -> xViewPageRoomLive.currentItem = 1
                    "排行" -> xViewPageRoomLive.currentItem = 2
                    "预告" -> xViewPageRoomLive.currentItem = 3
                }
            }
        })
        //ViewPage监听
        xViewPageRoomLive.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (tabLiveRoom.getTabAt(position) != null) tabLiveRoom.getTabAt(position)!!.select()
            }
        })

        swipeBackLayout?.addSwipeListener(object : SwipeBackLayout.OnSwipeListener {
            override fun onEdgeTouch(oritentationEdgeFlag: Int) {
            }

            override fun onDragScrolled(scrollPercent: Float) {
            }

            override fun onDragStateChange(state: Int) {
                if (state == 3) {
                    if (liveState) liveRoomActivityHelper.switchWindowPlay()
                }
            }
        })

    }


    /**
     * 初始化svga
     */
    private fun initSvga() {
        svgaUtils = SvgaUtils(this, svgaImage)
        svgaUtils.initAnimator()
    }


    /**
     * 礼物接收动画
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataAnim(eventBean: HomeLiveBigAnimatorBean) {
        if (bottomHorGiftWindow != null) {
            bottomHorGiftWindow?.hideLoading()
        }
        if (UserInfoSp.getIsShowAnim()) {
            when (eventBean.gift_id) {
                "16" -> svgaUtils.startAnimator("烟花城堡", svgaImage)
                "15" -> svgaUtils.startAnimator("兰博基尼", svgaImage)
                "17" -> svgaUtils.startAnimator("凤凰机车", svgaImage)

                "21" -> svgaUtils.startAnimator("口红", svgaImage)
                "23" -> svgaUtils.startAnimator("LOVE", svgaImage)

                "29" -> svgaUtils.startAnimator("游艇一号", svgaImage)
                "28" -> svgaUtils.startAnimator("火凤凰", svgaImage)
                "27" -> svgaUtils.startAnimator("帝王花车", svgaImage)

                "51" -> svgaUtils.startAnimator("天灯祈福", svgaImage)
                "52" -> svgaUtils.startAnimator("新春大鼓", svgaImage)
                "53" -> svgaUtils.startAnimator("年年有余", svgaImage)
                "54" -> svgaUtils.startAnimator("鞭炮齐鸣", svgaImage)

                "41" -> svgaUtils.startAnimator("电动棒", svgaImage)
                "42" -> svgaUtils.startAnimator("黄瓜", svgaImage)
                "43" -> svgaUtils.startAnimator("茄子", svgaImage)
                "44" -> svgaUtils.startAnimator("皮鞭", svgaImage)
                "45" -> svgaUtils.startAnimator("滴蜡", svgaImage)
                "46" -> svgaUtils.startAnimator("为所欲为", svgaImage)
            }
        }
    }

    /**
     * 收起视频 展开视频
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun videoState(eventBean: LiveVideoClose) {
        if (eventBean.closeOrOpen) {
            HiddenAnimUtils.newInstance(this, videoContainer, null, 0).toggle()
            liveRoomActivityHelper.pausePlay()
        } else {
            HiddenAnimUtils.newInstance(this, videoContainer, null, 0).toggle()
            liveRoomActivityHelper.startPlay()
        }
    }

    /**
     * 横屏底部礼物栏
     */
    var bottomHorGiftWindow: BottomHorGiftWindow? = null

    fun bottomGift() {
        if (!UserInfoSp.getIsLogin()) {
            GlobalDialog.notLogged(this, true)
            return
        }

        if (bottomHorGiftWindow == null) {
            bottomHorGiftWindow = BottomHorGiftWindow(this@LiveRoomActivity)
            bottomHorGiftWindow?.show()
            mPresenter.getGiftList()
        } else bottomHorGiftWindow?.show()
        val presenter = MinePresenter()
        presenter.getUserDiamond()
        presenter.getUserDiamondSuccessListener {
            bottomHorGiftWindow?.tvDiamondTotal?.text = it
        }
        presenter.getUserDiamondFailedListener {
            GlobalDialog.showError(this, it, true)
        }
    }

    /**
     * 横屏红包
     */
    fun initRedHor() {
        if (!UserInfoSp.getIsLogin()) {
            GlobalDialog.notLogged(this, true)
            return
        }
        val dialog = SendRedDialog(this, true)
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
        val passWordDialog = PassWordDialog(this, ViewGroup.LayoutParams.MATCH_PARENT, ViewUtils.dp2px(136))
        passWordDialog.setTextWatchListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    //验证支付密码
                    MineApi.verifyPayPass(s.toString()) {
                        onSuccess {
                            passWordDialog.showOrHideLoading()
                            //发红包
                            mPresenter.homeLiveSendRedEnvelope(intent.getStringExtra(IntentConstant.LIVE_ROOM_ANCHOR_ID)!!, total, redNumber, redContent, s.toString(), passWordDialog)
                        }
                        onFailed {
                            if (it.getCode() == 1002) {
                                passWordDialog.showOrHideLoading()
                                passWordDialog.showTipsText(it.getMsg().toString() + "," + "您还有" +
                                        JsonUtils.fromJson(it.getDataCode().toString(), MinePassWordTime::class.java).remain_times.toString() +
                                        "次机会")
                                passWordDialog.clearText()
                                passWordDialog.showOrHideLoading()
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
        passWordDialog.setOnDismissListener {
            val decorView = ScreenUtils.getDecorView(this)
            if (decorView != null) ScreenUtils.hideSysBar(this, decorView)
        }
        passWordDialog.show()
    }


    /**
     * 横屏小红包点击
     */
    fun showRedPaper() {
        if (!UserInfoSp.getIsLogin()) {
            GlobalDialog.notLogged(this)
            return
        }
        RxBus.get().post(RedPaperClick(true))
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        liveRoomActivityHelper.configurationChanged(newConfig)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        WindowPermissionCheck.onActivityResult(this, requestCode, resultCode, data, null)
    }

    override fun onBackPressed() {
        if (liveRoomActivityHelper.isLandScape) {
            liveRoomActivityHelper.quitFullScreen()
            return
        }
        if (liveState) liveRoomActivityHelper.switchWindowPlay()
        super.onBackPressed()
    }

    override fun onPause() {
        if (liveRoomActivityHelper.mFloatWindow != null &&
                !liveRoomActivityHelper.mFloatWindow!!.isWindowShow) liveRoomActivityHelper.mAssist!!.pause()
        if (liveRoomDanMuHelper.mDanmakuView != null && liveRoomDanMuHelper.mDanmakuView!!.isPrepared) {
            liveRoomDanMuHelper.mDanmakuView!!.pause()
        }
        super.onPause()
    }

    override fun onResume() {
        if (liveRoomActivityHelper.mAssist != null) liveRoomActivityHelper.mAssist!!.resume()
        if (liveRoomDanMuHelper.mDanmakuView != null && liveRoomDanMuHelper.mDanmakuView!!.isPrepared &&
                liveRoomDanMuHelper.mDanmakuView!!.isPaused) {
            liveRoomDanMuHelper.mDanmakuView!!.resume()
        }
        super.onResume()

    }

    override fun onDestroy() {
        if (liveRoomDanMuHelper.mDanmakuView != null) {
            // dont forget release!
            liveRoomDanMuHelper.mDanmakuView!!.release()
            liveRoomDanMuHelper.mDanmakuView = null
        }
        super.onDestroy()
//        liveRoomActivityHelper.closeWindow()
//        liveRoomActivityHelper. mAssist!!.destroy()
    }


    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun enter(eventBean: OnLineInfo) {
        val cover = liveRoomActivityHelper.mReceiverGroup?.getReceiver<ControllerLiveCover>(DataInter.ReceiverKey.KEY_CONTROLLER_live_COVER)
        val num = eventBean.online?.plus(onLine).toString()
        cover?.setOnLne("$num 人")
    }


    //横屏小红包动画
    var isHasRed = false

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun showRed(eventBean: RedTips) {
        isHasRed = eventBean.boolean
        val cover = liveRoomActivityHelper.mReceiverGroup?.getReceiver<ControllerLiveCover>(DataInter.ReceiverKey.KEY_CONTROLLER_live_COVER)
        if (eventBean.boolean)
            cover?.startRedAnimation()
        else cover?.stopRedAnimation()
    }

    //收弹幕
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun showRed(eventBean: DanMu) {
        if (isFullScreen() && UserInfoSp.getDanMuSwitch()) {
            liveRoomDanMuHelper.addDanmaKuShowTextAndImage(eventBean, true)
        }
    }

    //更新横屏钻石
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataHorDiamon(eventBean: UpDataHorDiamon) {
        if (isFullScreen() && bottomHorGiftWindow != null) {
            val presenter = MinePresenter()
            presenter.getUserDiamond()
            presenter.getUserDiamondSuccessListener {
                bottomHorGiftWindow?.tvDiamondTotal?.text = it
            }
            presenter.getUserDiamondFailedListener {
                GlobalDialog.showError(this, it, true)
            }
        }
    }

    //跳转购彩
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun jumpToBuyLottery(eventBean: JumpToBuyLottery) {
        if (liveState) liveRoomActivityHelper.switchWindowPlay()
    }

    //更新关注
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun UpDateAttention(eventBean: UpDateAttention) {
        if (eventBean.boolean) {
            setGone(imgAttention)
            tvAttention.text = "已关注"
            setTextColor(tvAttention, ViewUtils.getColor(R.color.grey_97))
            linAttention.setBackgroundResource(R.color.color_EEEEEE)
        } else {
            setVisible(imgAttention)
            tvAttention.text = "关注"
            setTextColor(tvAttention, ViewUtils.getColor(R.color.white))
            linAttention.setBackgroundResource(R.drawable.background_gradient)
        }
    }


    //投注 钻石不足 退出
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryDiamondNotEnough(eventBean: HomeJumpToMine) {
        if (liveState) liveRoomActivityHelper.switchWindowPlay()
    }

    //动画效果

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun isShowAnim(eventBean: LiveAnimClose) {
        if (eventBean.closeOrOpen) {
            setVisible(svgaImage)
        } else setGone(svgaImage)
    }

    //发弹幕
    fun sendDanMu() {
        if (!UserInfoSp.getIsLogin()) {
            GlobalDialog.notLogged(this, true)
            return
        }
        val liveRoomChatInputDialog = LiveRoomChatInputDialog(this, R.style.inputDialog)
        liveRoomChatInputDialog.showEmojiOrKeyBord(false)
        liveRoomChatInputDialog.setOnDismissListener {
            val decorView = ScreenUtils.getDecorView(this)
            if (decorView != null)
                ScreenUtils.hideSysBar(this, decorView)
        }
        liveRoomChatInputDialog.setSendClickListener {
            RxBus.get().post(SendDanMu(it))
            liveRoomChatInputDialog.dismiss()
        }
        liveRoomChatInputDialog.show()
    }

    //清除弹幕
    fun clearDanMu() {
        liveRoomDanMuHelper.clear()
    }

    //充值
    fun goReCharge() {
        if (!UserInfoSp.getIsLogin()) {
            GlobalDialog.notLogged(this, true)
            return
        }
        liveRoomActivityHelper.quitFullScreen()
        LaunchUtils.startRechargePage(this, 0)
    }

}