package com.fenghuang.caipiaobao.ui.home.live

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.example.playerlibrary.AlivcLiveRoom.ScreenUtils
import com.example.playerlibrary.utils.WindowPermissionCheck
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.IntentConstant
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveEnterRoomResponse
import com.fenghuang.caipiaobao.widget.videoplayer.assist.*
import com.fenghuang.caipiaobao.widget.videoplayer.assist.DataInter.ReceiverKey.KEY_CONTROLLER_live_COVER
import com.fenghuang.caipiaobao.widget.videoplayer.cover.CloseCover
import com.fenghuang.caipiaobao.widget.videoplayer.cover.ControllerLiveCover
import com.fenghuang.caipiaobao.widget.videoplayer.cover.GestureCover
import com.fenghuang.caipiaobao.widget.videoplayer.cover.LiveStateCover
import com.fenghuang.caipiaobao.widget.videoplayer.entity.DataSource
import com.fenghuang.caipiaobao.widget.videoplayer.receiver.ReceiverGroup
import com.fenghuang.caipiaobao.widget.videoplayer.window.FloatWindow
import com.fenghuang.caipiaobao.widget.videoplayer.window.FloatWindowSingleton


/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-17
 * @ Describe 直播帮助类
 *
 */


class LiveRoomActivityHelper {

    lateinit var activity: LiveRoomActivity
    lateinit var videoContainer: FrameLayout

    var mVideoContainerH: Int = 0
    var mAssist: RelationAssist? = null
    var mReceiverGroup: ReceiverGroup? = null
    var mFloatWindow: FloatWindow? = null
    var isLandScape: Boolean = false
    var mWindowVideoContainer: FrameLayout? = null
    val VIEW_INTENT_FULL_SCREEN = 1
    val WINDOW_INTENT_FULL_SCREEN = 2
    var mWhoIntentFullScreen = 0


    var decorView: ViewGroup? = null

    var isFloatWindowEnter: Boolean = false

    var data: HomeLiveEnterRoomResponse? = null

    fun initComponent(activity: LiveRoomActivity, videoContainer: FrameLayout, isFloatWindowEnter: Boolean) {
        this.activity = activity
        this.videoContainer = videoContainer
        this.isFloatWindowEnter = isFloatWindowEnter
//        //判断是否是小窗进入
//        if (isFloatWindowEnter){
//            mAssist!!.attachContainer(videoContainer)
//        }
    }

    fun setDataLive(dates: HomeLiveEnterRoomResponse) {
        this.data = dates
        if (isFloatWindowEnter) initVideoPlayer()
    }

    //初始化
    private fun initVideoPlayer() {
        if (data != null) {
            decorView = ScreenUtils.getDecorView(activity)
            if (mAssist != null) mAssist?.destroy()
            mAssist = RelationAssistSingleton.getAssist(activity)
            FloatWindowSingleton.releaseFloatWindow()
            mFloatWindow = FloatWindowSingleton.getFloatWindow(activity)
            FloatWindowSingleton.anchorId = data?.anchor_id
            FloatWindowSingleton.type = data?.nickname
            FloatWindowSingleton.nickname = data?.nickname
            FloatWindowSingleton.liveState = data?.live_status
            FloatWindowSingleton.avatar = data?.avatar
            FloatWindowSingleton.lotteryId = data?.lottery_id
            videoContainer.post { mVideoContainerH = videoContainer.height }
            mAssist!!.attachContainer(videoContainer)
            mAssist!!.superContainer.setBackgroundColor(Color.BLACK)
            mAssist!!.setEventAssistHandler(eventHandler)
            activity.window.addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON)
            mReceiverGroup = ReceiverGroupManager.get().getLiveReceiverGroup(activity,
                    data!!.live_status,
                    data!!.avatar,
                    data!!.nickname,
                    data!!.anchor_id)
            mReceiverGroup!!.groupValue.putBoolean(DataInter.Key.KEY_NETWORK_RESOURCE, true)
            mAssist!!.receiverGroup = mReceiverGroup
            changeMode(false)
            val cover = mReceiverGroup?.getReceiver<ControllerLiveCover>(KEY_CONTROLLER_live_COVER)
            if (activity.isHasRed)
                cover?.startRedAnimation()
            else cover?.stopRedAnimation()
        } else {
            ToastUtils.show("直播间获取失败")
        }

    }

    fun startPlay(url: String) {
        RelationAssistSingleton.releaseAssist()
        initVideoPlayer()
        //http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8
        val dataSource = DataSource()
        dataSource.data = url
        dataSource.title = ""
        mAssist!!.setDataSource(dataSource)
        mAssist!!.attachContainer(videoContainer)
        mAssist!!.play()
        if (mFloatWindow != null && mFloatWindow!!.isWindowShow) {
            normalPlay()
        }

    }

    private fun changeMode(window: Boolean) {
        if (window) {
            mReceiverGroup!!.removeReceiver(DataInter.ReceiverKey.KEY_GESTURE_COVER)
            mReceiverGroup!!.addReceiver(DataInter.ReceiverKey.KEY_CLOSE_COVER, CloseCover(activity))
        } else {
            mReceiverGroup!!.removeReceiver(DataInter.ReceiverKey.KEY_CLOSE_COVER)
            mReceiverGroup!!.addReceiver(DataInter.ReceiverKey.KEY_GESTURE_COVER, GestureCover(activity))
        }
        mReceiverGroup!!.groupValue.putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, !window)
    }

    fun changeState(avatar: String) {
        initVideoPlayer()
        mAssist!!.attachContainer(videoContainer)
        mAssist!!.stop()
        mReceiverGroup!!.addReceiver(DataInter.ReceiverKey.KEY_STATE_COVER, LiveStateCover(activity, avatar))

    }

    private val eventHandler = object : OnAssistPlayEventHandler() {
        override fun onAssistHandle(assist: AssistPlay?, eventCode: Int, bundle: Bundle?) {
            super.onAssistHandle(assist, eventCode, bundle)
            when (eventCode) {
                DataInter.Event.EVENT_CODE_REQUEST_BACK -> activity.onBackPressed()
                DataInter.Event.EVENT_CODE_ERROR_SHOW -> mAssist!!.stop()
                //底部礼物
                DataInter.Event.EVENT_CODE_BOTTOM_GIFT -> {
                    activity.bottomGift()
                }
                DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN -> if (isLandScape) {
                    quitFullScreen()
                } else {
                    mWhoIntentFullScreen = if (mFloatWindow!!.isWindowShow)
                        WINDOW_INTENT_FULL_SCREEN
                    else
                        VIEW_INTENT_FULL_SCREEN
                    enterFullScreen()
                }
                DataInter.Event.EVENT_CODE_REQUEST_CLOSE -> {
                    releaseAssist()
                }
                DataInter.Event.EVENT_CODE_REQUEST_CLOSE_ENTER -> {
                    val intent = Intent(activity, LiveRoomActivity::class.java)
                    intent.putExtra("isWindowBack", true)
                    intent.putExtra(IntentConstant.LIVE_ROOM_ANCHOR_ID, FloatWindowSingleton.getAnchorId())
                    intent.putExtra(IntentConstant.LIVE_ROOM_NAME, FloatWindowSingleton.getRoomType())
                    intent.putExtra(IntentConstant.LIVE_ROOM_ANCHOR_STATUE, FloatWindowSingleton.getRoomLiveState())
                    intent.putExtra(IntentConstant.LIVE_ROOM_AVATAR, FloatWindowSingleton.getRoomAvatar())
                    intent.putExtra(IntentConstant.LIVE_ROOM_NICK_NAME, FloatWindowSingleton.getRoomNickName())
                    intent.putExtra(IntentConstant.LIVE_ROOM_ONLINE, FloatWindowSingleton.getRoomLiveOnline())
                    intent.putExtra(IntentConstant.LIVE_ROOM_LOTTERY_ID, FloatWindowSingleton.getLotteryId())
                    activity.startActivity(intent)
                    if (mFloatWindow != null && mFloatWindow!!.isWindowShow) {
                        FloatWindowSingleton.isLan = false
                        mAssist?.rotate(0)
                        normalPlay()
                    }
                }

                DataInter.Event.EVENT_CODE_BOTTOM_RED -> {
                    activity.initRedHor()
                }
                DataInter.Event.EVENT_CODE_ATTENTION -> {
                    activity.controlAttention()
                }
                DataInter.Event.EVENT_CODE_REDTIPS -> {
                    activity.showRedPaper()
                }
                DataInter.Event.EVENT_CODE_DAM_MU -> {
                    activity.sendDanMu()
                }
                DataInter.Event.EVENT_CODE_DAM_MU_SWITCH -> {
                    if (bundle != null && !bundle.getBoolean("DanMu")) {
                        activity.clearDanMu()
                    }
                }
                DataInter.Event.EVENT_CODE_RECHARGE -> {
                    activity.goReCharge()
                }
                DataInter.Event.EVENT_CODE_REQUEST_CHANGE -> {
                    val widthPixels = activity.resources.displayMetrics.widthPixels
                    if (FloatWindowSingleton.isLan) {
                        val height = when {
                            UserInfoSp.getVideoSize() == 1 -> {
                                UserInfoSp.getVideoSize(2)
                                (widthPixels * 0.8f).toInt()
                            }
                            UserInfoSp.getVideoSize() == 2 -> {
                                UserInfoSp.getVideoSize(3)
                                (widthPixels * 0.9f).toInt()
                            }
                            else -> {
                                UserInfoSp.getVideoSize(1)
                                (widthPixels * 0.6f).toInt()
                            }
                        }
                        val width = height * 9 / 16
                        FloatWindowSingleton.upDateSizeNormal(width, height)
                    } else {
                        val width = when {
                            UserInfoSp.getVideoSize() == 1 -> {
                                UserInfoSp.getVideoSize(2)
                                (widthPixels * 0.8f).toInt()
                            }
                            UserInfoSp.getVideoSize() == 2 -> {
                                UserInfoSp.getVideoSize(3)
                                (widthPixels * 0.9f).toInt()
                            }
                            else -> {
                                UserInfoSp.getVideoSize(1)
                                (widthPixels * 0.6f).toInt()
                            }
                        }
                        val height = width * 9 / 16
                        FloatWindowSingleton.upDateSizeNormal(width, height)
                    }

                }

                DataInter.Event.EVENT_CODE_REQUEST_ROTATE -> {
                    val widthPixels = activity.resources.displayMetrics.widthPixels
                    if (!FloatWindowSingleton.isLan) {
                        val height = when {
                            UserInfoSp.getVideoSize() == 1 -> {
                                (widthPixels * 0.6f).toInt()
                            }
                            UserInfoSp.getVideoSize() == 2 -> {
                                (widthPixels * 0.8f).toInt()
                            }
                            else -> {
                                (widthPixels * 0.9f).toInt()
                            }
                        }
                        val width = height * 9 / 16
                        FloatWindowSingleton.upDateSizeRotate(width, height)
                        mAssist?.rotate(90)
                    } else {
                        val width = when {
                            UserInfoSp.getVideoSize() == 1 -> {
                                (widthPixels * 0.6f).toInt()
                            }
                            UserInfoSp.getVideoSize() == 2 -> {
                                (widthPixels * 0.8f).toInt()
                            }
                            else -> {
                                (widthPixels * 0.9f).toInt()
                            }
                        }
                        val height = width * 9 / 16
                        FloatWindowSingleton.upDateSize(width, height)
                        mAssist?.rotate(0)
                    }
                }
            }
        }
    }

    //进入全屏
    @SuppressLint("SourceLockedOrientationActivity")
    private fun enterFullScreen() {
        activity.window.setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN)
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        if (decorView != null) ScreenUtils.hideSysBar(activity, decorView)
        if (mWhoIntentFullScreen == WINDOW_INTENT_FULL_SCREEN) {
            normalPlay()
        }
    }

    //退出全屏
    @SuppressLint("SourceLockedOrientationActivity")
    fun quitFullScreen() {
        activity.window.clearFlags(LayoutParams.FLAG_FULLSCREEN)
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (decorView != null) ScreenUtils.showSysBar(activity, decorView)
        if (mWhoIntentFullScreen == WINDOW_INTENT_FULL_SCREEN) {
            windowPlay()
        }
    }


    //正常播放
    fun normalPlay() {
        mReceiverGroup!!.getReceiver<ControllerLiveCover>(KEY_CONTROLLER_live_COVER).setIsShowControllerView(true)
        changeMode(false)
        closeWindow()
    }

    //播放切换
    fun switchWindowPlay() {
        if (mFloatWindow!!.isWindowShow) {
            normalPlay()
        } else {
            if (WindowPermissionCheck.checkPermission(activity)) {
                windowPlay()
                activity.finish()
            }
        }
    }


    //小窗播放
    private fun windowPlay() {
        if (mFloatWindow != null && !mFloatWindow!!.isWindowShow) {
            mReceiverGroup!!.getReceiver<ControllerLiveCover>(KEY_CONTROLLER_live_COVER).setIsShowControllerView(false)
            changeMode(true)
//            mFloatWindow!!.setElevationShadow(50f)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                mFloatWindow!!.setRoundRectShape(20f)
            mFloatWindow!!.show()
            if (FloatWindowSingleton.mWindowVideoContainer != null)
                mAssist!!.attachContainer(FloatWindowSingleton.mWindowVideoContainer)
        }
    }

    //关闭小窗
    private fun closeWindow() {
        FloatWindowSingleton.releaseFloatWindow()
    }

    //横竖屏切换
    fun configurationChanged(newConfig: Configuration) {
        isLandScape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        val params = videoContainer.layoutParams
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = mVideoContainerH
        }
        videoContainer.layoutParams = params
        mReceiverGroup!!.groupValue.putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, isLandScape)
    }


    //释放
    fun releaseAssist() {
        closeWindow()
        RelationAssistSingleton.releaseAssist()
    }

    //暂停播放
    fun pausePlay() {
        if (mAssist != null) mAssist?.pause()
    }

    //开始播放
    fun startPlay() {
        if (mAssist != null) mAssist?.rePlay(0)
    }
}