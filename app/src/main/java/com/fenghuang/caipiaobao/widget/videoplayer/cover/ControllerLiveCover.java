package com.fenghuang.caipiaobao.widget.videoplayer.cover;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.playerlibrary.PLog;
import com.example.playerlibrary.touch.OnTouchGestureListener;
import com.example.playerlibrary.utils.TimeUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fenghuang.baselib.utils.ViewUtils;
import com.fenghuang.baselib.widget.round.RoundLinearLayout;
import com.fenghuang.baselib.widget.round.RoundTextView;
import com.fenghuang.caipiaobao.R;
import com.fenghuang.caipiaobao.constant.UserInfoSp;
import com.fenghuang.caipiaobao.manager.ImageManager;
import com.fenghuang.caipiaobao.widget.ObjectAnimatorViw;
import com.fenghuang.caipiaobao.widget.videoplayer.assist.DataInter;
import com.fenghuang.caipiaobao.widget.videoplayer.entity.DataSource;
import com.fenghuang.caipiaobao.widget.videoplayer.event.EventKey;
import com.fenghuang.caipiaobao.widget.videoplayer.event.OnPlayerEventListener;
import com.fenghuang.caipiaobao.widget.videoplayer.player.IPlayer;
import com.fenghuang.caipiaobao.widget.videoplayer.player.OnTimerUpdateListener;
import com.fenghuang.caipiaobao.widget.videoplayer.receiver.BaseCover;
import com.fenghuang.caipiaobao.widget.videoplayer.receiver.IReceiverGroup;

/**
 * @ Author  QinTian
 * @ Date  2020-01-21
 * @ Describe
 */
public class ControllerLiveCover extends BaseCover implements OnTimerUpdateListener, OnTouchGestureListener {

    private final int MSG_CODE_DELAY_HIDDEN_CONTROLLER = 102;

    private View mTopContainer;
    private View mBottomContainer;
    private ImageView mBackIcon;
    private ImageView mStateIcon, cover_player_controller_refresh;
    private ImageView mSwitchScreen, imgRedTips;
    //横屏
    private RoundLinearLayout containerAttention;
    private LinearLayout containerHor, containerLand;

    private ImageView bottomPlayOrPause, bottomRefresh, bottomDanMu, bottomRecharge, bottomRed, bottomGift;
    private RoundTextView bottomInput, topAttention;
    private SimpleDraweeView imgAvatar;
    private TextView tvAnchorName, tvOnlinePeople, tvBottomRoomID, topName, topOnLine, tvRoomId;


    private Boolean isShowControllerView = true;

    private boolean mTimerUpdateProgressEnable = true;
    private int PlayerState = 111;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CODE_DELAY_HIDDEN_CONTROLLER:
                    PLog.d(getTag().toString(), "msg_delay_hidden...");
                    setControllerState(false);
                    break;
            }
        }
    };

    private boolean mGestureEnable = true;

    private String mTimeFormat;

    private boolean mControllerTopEnable;
    private ObjectAnimator mBottomAnimator;
    private ObjectAnimator mTopAnimator;
    private Context context;
    public  String liveState, avatar, nickName, anchorId;

    private interface OnRestartListener {
        void restart();
    }

    public void setOnRestartListener(OnRestartListener listener) {
        this.onRestartListener = listener;
    }

    private OnRestartListener onRestartListener;

    public ControllerLiveCover(Context context, String liveState, String avatar, String nickName, String anchorId) {
        super(context);
        this.context = context;
        this.liveState = liveState;
        this.avatar = avatar;
        this.nickName = nickName;
        this.anchorId = anchorId;
    }

    public void setIsShowControllerView(Boolean isShow) {
        this.isShowControllerView = isShow;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onReceiverBind() {
        super.onReceiverBind();


        mTopContainer = findViewById(R.id.cover_player_controller_top_container);
        mBottomContainer = findViewById(R.id.cover_player_controller_bottom_container);
        mBackIcon = findViewById(R.id.cover_player_controller_image_view_back_icon);
        mStateIcon = findViewById(R.id.cover_player_controller_image_view_play_state);
        mSwitchScreen = findViewById(R.id.cover_player_controller_image_view_switch_screen);
        cover_player_controller_refresh = findViewById(R.id.cover_player_controller_refresh);

        //横屏 竖屏 控件
        containerAttention = findViewById(R.id.containerAttention);
        containerHor = findViewById(R.id.containerHor);
        containerLand = findViewById(R.id.containerLand);

        tvAnchorName = findViewById(R.id.tvAnchorName);
        tvOnlinePeople = findViewById(R.id.tvOnlinePeople);
        tvBottomRoomID = findViewById(R.id.tvBottomRoomID);
        bottomPlayOrPause = findViewById(R.id.bottomPlayOrPause);
        bottomRefresh = findViewById(R.id.bottomRefresh);
        bottomDanMu = findViewById(R.id.bottomDanMu);
        bottomRed = findViewById(R.id.bottomRed);
        bottomGift = findViewById(R.id.bottomGift);
        bottomInput = findViewById(R.id.bottomInput);
        topAttention = findViewById(R.id.topAttention);
        imgAvatar = findViewById(R.id.imgAvatar);
        topName = findViewById(R.id.topName);
        topOnLine = findViewById(R.id.topOnLine);
        tvRoomId = findViewById(R.id.tvRoomId);
        imgRedTips = findViewById(R.id.imgRedTips);
        bottomRecharge = findViewById(R.id.bottomRecharge);

        bottomRefresh.setOnClickListener(v -> requestRetry(null));

        cover_player_controller_refresh.setOnClickListener(v -> requestRetry(null));

        mBackIcon.setOnClickListener(v -> notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_BACK, null));

        mSwitchScreen.setOnClickListener(v -> notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN, null));

        imgRedTips.setOnClickListener(v -> notifyReceiverEvent(DataInter.Event.EVENT_CODE_REDTIPS, null));

        bottomRecharge.setOnClickListener(v -> notifyReceiverEvent(DataInter.Event.EVENT_CODE_RECHARGE, null));

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bottomRecharge.getLayoutParams();
        if (UserInfoSp.INSTANCE.getIsFirstRecharge()){
            bottomRecharge.setBackgroundResource(R.mipmap.ic_live_first_recharge);
            lp.width = ViewUtils.INSTANCE.dp2px(34);
            lp.height =  ViewUtils.INSTANCE.dp2px(30);
            lp.setMargins(ViewUtils.INSTANCE.dp2px(5),0,ViewUtils.INSTANCE.dp2px(5),0);
            bottomRecharge.setLayoutParams(lp);
        }else {
            bottomRecharge.setBackgroundResource(R.mipmap.ic_live_chat_recharge);
            lp.width = ViewUtils.INSTANCE.dp2px(50);
            lp.height =  ViewUtils.INSTANCE.dp2px(40);
            lp.setMargins(ViewUtils.INSTANCE.dp2px(5),0,ViewUtils.INSTANCE.dp2px(5),0);
            bottomRecharge.setLayoutParams(lp);
        }


        //初始化状态
        if (UserInfoSp.INSTANCE.getDanMuSwitch()) {
            bottomDanMu.setImageResource(R.mipmap.ic_player_danmu);
        } else {
            bottomDanMu.setImageResource(R.mipmap.ic_player_wudanmu);
        }


        if (liveState.equals("1")) {
            bottomInput.getDelegate().setBackgroundColor(ViewUtils.INSTANCE.getColor(R.color.ffffff));
            bottomInput.setTextColor(ViewUtils.INSTANCE.getColor(R.color.color_DDDDDD));
            bottomInput.setGravity(Gravity.CENTER_VERTICAL);
            bottomInput.setOnClickListener(v -> notifyReceiverEvent(DataInter.Event.EVENT_CODE_DAM_MU, null));
        } else {
            bottomInput.getDelegate().setBackgroundColor(ViewUtils.INSTANCE.getColor(R.color.color_EEEEEE));
            bottomInput.setTextColor(ViewUtils.INSTANCE.getColor(R.color.grey_e6));
            bottomInput.setText("未开播禁止发言");
            bottomInput.setGravity(Gravity.CENTER);
        }


        //bottom
        tvAnchorName.setText(nickName);
        tvBottomRoomID.setText("房间ID " + anchorId);
        //top
        ImageManager.INSTANCE.loadImg(avatar, imgAvatar);  //头像
        topName.setText(nickName);
        tvRoomId.setText("房间ID " + anchorId);


        //监听
        bottomDanMu.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if (UserInfoSp.INSTANCE.getDanMuSwitch()) {
                bottomDanMu.setImageResource(R.mipmap.ic_player_wudanmu);
                UserInfoSp.INSTANCE.putDanMuSwitch(false);
                bundle.putBoolean("DanMu", false);
                notifyReceiverEvent(DataInter.Event.EVENT_CODE_DAM_MU_SWITCH, bundle);
            } else {
                UserInfoSp.INSTANCE.putDanMuSwitch(true);
                bottomDanMu.setImageResource(R.mipmap.ic_player_danmu);
                bundle.putBoolean("DanMu", true);
                notifyReceiverEvent(DataInter.Event.EVENT_CODE_DAM_MU_SWITCH, bundle);
            }
        });

        bottomGift.setOnClickListener(v -> {
            toggleController();
            notifyReceiverEvent(DataInter.Event.EVENT_CODE_BOTTOM_GIFT, null);
        });

        containerAttention.setOnClickListener(v -> notifyReceiverEvent(DataInter.Event.EVENT_CODE_ATTENTION, null));

        bottomRed.setOnClickListener(v -> {
            toggleController();
            notifyReceiverEvent(DataInter.Event.EVENT_CODE_BOTTOM_RED, null);
        });

        mStateIcon.setOnClickListener(v -> {
            if (PlayerState == OnPlayerEventListener.PLAYER_EVENT_ON_START
                    || PlayerState == OnPlayerEventListener.PLAYER_EVENT_ON_RESUME) {
                requestPause(null);

            } else if (PlayerState == OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE
                    || PlayerState == OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED
                    || PlayerState == OnPlayerEventListener.PLAYER_EVENT_ON_STOP) {
                requestReplay(null);

            }

            boolean selected = mStateIcon.isSelected();
            if (selected) {
                requestRetry(null);
                setSwitchPlayPause(true);
            } else {
                requestPause(null);
                setSwitchPlayPause(false);
            }
            mStateIcon.setSelected(!selected);
        });

        bottomPlayOrPause.setOnClickListener(v -> {
            if (PlayerState == OnPlayerEventListener.PLAYER_EVENT_ON_START
                    || PlayerState == OnPlayerEventListener.PLAYER_EVENT_ON_RESUME) {
                requestPause(null);

            } else if (PlayerState == OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE
                    || PlayerState == OnPlayerEventListener.PLAYER_EVENT_ON_PREPARED
                    || PlayerState == OnPlayerEventListener.PLAYER_EVENT_ON_STOP) {
                requestReplay(null);

            }

            boolean selected = mStateIcon.isSelected();
            if (selected) {
                requestRetry(null);
                bottomPlayOrPause.setImageResource(R.mipmap.ic_video_player_btn_pause);
            } else {
                requestPause(null);
                setSwitchPlayPause(false);
                bottomPlayOrPause.setImageResource(R.mipmap.ic_video_player_btn_play);
            }

        });


        sendDelayHiddenMessage();
        getGroupValue().registerOnGroupValueUpdateListener(mOnGroupValueUpdateListener);

    }


    @Override
    protected void onCoverAttachedToWindow() {
        super.onCoverAttachedToWindow();
        boolean topEnable = getGroupValue().getBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, true);
        mControllerTopEnable = topEnable;
        if (!topEnable) {
            setTopContainerState(false);
        }
        if (!isShowControllerView) {
            mBottomContainer.setVisibility(View.GONE);
        }
        boolean screenSwitchEnable = getGroupValue().getBoolean(DataInter.Key.KEY_CONTROLLER_SCREEN_SWITCH_ENABLE, true);
        setScreenSwitchEnable(screenSwitchEnable);
    }

    @Override
    protected void onCoverDetachedToWindow() {
        super.onCoverDetachedToWindow();
        mTopContainer.setVisibility(View.GONE);
        mBottomContainer.setVisibility(View.GONE);
        mStateIcon.setVisibility(View.GONE);
        removeDelayHiddenMessage();
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();
        cancelTopAnimation();
        cancelBottomAnimation();
        getGroupValue().unregisterOnGroupValueUpdateListener(mOnGroupValueUpdateListener);
        removeDelayHiddenMessage();
    }

    private IReceiverGroup.OnGroupValueUpdateListener mOnGroupValueUpdateListener =
            new IReceiverGroup.OnGroupValueUpdateListener() {
                @Override
                public String[] filterKeys() {
                    return new String[]{
                            DataInter.Key.KEY_COMPLETE_SHOW,
                            DataInter.Key.KEY_TIMER_UPDATE_ENABLE,
                            DataInter.Key.KEY_DATA_SOURCE,
                            DataInter.Key.KEY_IS_LANDSCAPE,
                            DataInter.Key.KEY_CONTROLLER_TOP_ENABLE,
                    };
                }

                @Override
                public void onValueUpdate(String key, Object value) {
                    if (key.equals(DataInter.Key.KEY_COMPLETE_SHOW)) {
                        boolean show = (boolean) value;
                        if (show) {
                            setControllerState(false);
                        }
                        setGestureEnable(!show);
                    } else if (key.equals(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE)) {
                        mControllerTopEnable = (boolean) value;
                        if (!mControllerTopEnable) {
                            setTopContainerState(false);
                        }
                    } else if (key.equals(DataInter.Key.KEY_IS_LANDSCAPE)) {
                        //横竖屏
                        changeScreen((boolean) value);
                    } else if (key.equals(DataInter.Key.KEY_TIMER_UPDATE_ENABLE)) {
                        mTimerUpdateProgressEnable = (boolean) value;
                    } else if (key.equals(DataInter.Key.KEY_DATA_SOURCE)) {
//                        DataSource dataSource = (DataSource) value;
//                        setTitle(dataSource);
                    }
                }
            };


    private void setSwitchPlayPause(boolean isPause) {
        mStateIcon.setImageResource(isPause ? R.mipmap.ic_video_player_btn_pause : R.mipmap.ic_video_player_btn_play);
    }


    private void setScreenSwitchEnable(boolean screenSwitchEnable) {
        mSwitchScreen.setVisibility(screenSwitchEnable ? View.VISIBLE : View.GONE);
    }

    private void setGestureEnable(boolean gestureEnable) {
        this.mGestureEnable = gestureEnable;
    }

    private void cancelTopAnimation() {
        if (mTopAnimator != null) {
            mTopAnimator.cancel();
            mTopAnimator.removeAllListeners();
            mTopAnimator.removeAllUpdateListeners();
        }
    }

    private void setTopContainerState(final boolean state) {
        if (mControllerTopEnable) {
            mTopContainer.clearAnimation();
            cancelTopAnimation();
            mTopAnimator = ObjectAnimator.ofFloat(mTopContainer,
                    "alpha", state ? 0 : 1, state ? 1 : 0).setDuration(300);
            mTopAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    if (state) {
                        mTopContainer.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (!state) {
                        mTopContainer.setVisibility(View.GONE);
                    }
                }
            });
            mTopAnimator.start();
        } else {
            mTopContainer.setVisibility(View.GONE);
        }
    }

    private void cancelBottomAnimation() {
        if (mBottomAnimator != null) {
            mBottomAnimator.cancel();
            mBottomAnimator.removeAllListeners();
            mBottomAnimator.removeAllUpdateListeners();
        }
    }

    private void setBottomContainerState(final boolean state) {
        mBottomContainer.clearAnimation();
        cancelBottomAnimation();
        mBottomAnimator = ObjectAnimator.ofFloat(mBottomContainer,
                "alpha", state ? 0 : 1, state ? 1 : 0).setDuration(300);
        mBottomAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (state) {
                    mBottomContainer.setVisibility(View.VISIBLE);
                    mStateIcon.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!state) {
                    mBottomContainer.setVisibility(View.GONE);
                    mStateIcon.setVisibility(View.GONE);
                }
            }
        });
        mBottomAnimator.start();
        if (state) {
            PLog.d(getTag().toString(), "requestNotifyTimer...");
            requestNotifyTimer();
        } else {
            PLog.d(getTag().toString(), "requestStopTimer...");
            requestStopTimer();
        }
    }

    private void setControllerState(boolean state) {
        if (state) {
            sendDelayHiddenMessage();
        } else {
            removeDelayHiddenMessage();
        }
        setTopContainerState(state);
        setBottomContainerState(state);
    }

    private boolean isControllerShow() {
        return mBottomContainer.getVisibility() == View.VISIBLE;
    }

    private void toggleController() {
        if (isControllerShow()) {
            setControllerState(false);
        } else {
            setControllerState(true);
        }
    }

    private void sendDelayHiddenMessage() {
        removeDelayHiddenMessage();
        mHandler.sendEmptyMessageDelayed(MSG_CODE_DELAY_HIDDEN_CONTROLLER, 5000);
    }

    private void removeDelayHiddenMessage() {
        mHandler.removeMessages(MSG_CODE_DELAY_HIDDEN_CONTROLLER);
    }


    @Override
    public void onTimerUpdate(int curr, int duration, int bufferPercentage) {
        if (!mTimerUpdateProgressEnable)
            return;
        if (mTimeFormat == null) {
            mTimeFormat = TimeUtil.getFormat(duration);
        }

    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
                mTimeFormat = null;
                DataSource data = (DataSource) bundle.getSerializable(EventKey.SERIALIZABLE_DATA);
                getGroupValue().putObject(DataInter.Key.KEY_DATA_SOURCE, data);
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_STATUS_CHANGE:
                int status = bundle.getInt(EventKey.INT_DATA);
                if (status == IPlayer.STATE_PAUSED) {
                    mStateIcon.setSelected(true);
                } else if (status == IPlayer.STATE_STARTED) {
                    mStateIcon.setSelected(false);
                }
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE:
                mTimerUpdateProgressEnable = true;
                break;
        }
    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public Bundle onPrivateEvent(int eventCode, Bundle bundle) {
        return null;
    }

    @Override
    public View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_controller_live_cover, null);
    }

    @Override
    public int getCoverLevel() {
        return levelLow(1);
    }

    @Override
    public void onSingleTapUp(MotionEvent event) {
        if (!mGestureEnable)
            return;
        if (isShowControllerView) {
            toggleController();
        } else {
            notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_CLOSE_ENTER, null);
        }
    }

    @Override
    public void onDoubleTap(MotionEvent event) {
    }

    @Override
    public void onDown(MotionEvent event) {
    }

    @Override
    public void onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (!mGestureEnable)
            return;
    }

    @Override
    public void onEndGesture() {
    }


    //横竖屏切换   true 横屏  false 竖屏
    private void changeScreen(Boolean isLandscape) {
        if (!isLandscape) {
            containerAttention.setVisibility(View.GONE);
            containerHor.setVisibility(View.GONE);
            containerLand.setVisibility(View.VISIBLE);
            tvRoomId.setVisibility(View.GONE);
            isLandscapeChange = false;
            if (imgRedTips.getVisibility() == View.VISIBLE) {
                if (imgRedTips.getAnimation() != null) {
                    imgRedTips.clearAnimation();
                }
                if (imgRedTips != null) {
                    imgRedTips.setVisibility(View.GONE);
                }
            }
        } else {
            containerAttention.setVisibility(View.VISIBLE);
            containerHor.setVisibility(View.VISIBLE);
            containerLand.setVisibility(View.GONE);
            tvRoomId.setVisibility(View.VISIBLE);
            isLandscapeChange = true;
            if (isHasRed && imgRedTips.getVisibility() == View.GONE) {
                imgRedTips.setVisibility(View.VISIBLE);
                ObjectAnimator animator = ObjectAnimatorViw.INSTANCE.nope(imgRedTips, 3f);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.start();
            }
        }


    }

    //主播间人数
    public void setOnLne(String onLne) {
        if (onLne == null) return;
        if (tvOnlinePeople.getText().toString().equals(onLne)) return;
        tvOnlinePeople.setText(onLne);
        topOnLine.setText(onLne);
    }

    //关注
    public void setAttention(Boolean isAttention) {
        if (isAttention) {
            topAttention.getDelegate().setBackgroundColor(ViewUtils.INSTANCE.getColor(R.color.ffffff));
            topAttention.setTextColor(ViewUtils.INSTANCE.getColor(R.color.DADADA));
            topAttention.setText("已关注");
        } else {
            topAttention.getDelegate().setBackgroundColor(ViewUtils.INSTANCE.getColor(R.color.color_FF513E));
            topAttention.setTextColor(ViewUtils.INSTANCE.getColor(R.color.white));
            topAttention.setText("+ 关注");
        }
    }

    //小红包提示
    //当前横竖屏  true 横屏  false 竖屏
    private boolean isLandscapeChange = false;
    //当前是否有红包
    private boolean isHasRed = false;

    //红包动画
    public void startRedAnimation() {
        isHasRed = true;
        if (isLandscapeChange) {
            imgRedTips.setVisibility(View.VISIBLE);
            ObjectAnimator animator = ObjectAnimatorViw.INSTANCE.nope(imgRedTips, 3f);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.start();
        }
    }

    //隐藏小红包和动画
    public void stopRedAnimation() {
        isHasRed = false;
        if (imgRedTips.getAnimation() != null) {
            imgRedTips.clearAnimation();
        }
        if (imgRedTips != null) {
            imgRedTips.setVisibility(View.GONE);
        }
    }

    //更新信息
    public  void  upDateView(String avatars,String nickNames,String anchorIds){
        ImageManager.INSTANCE.loadImg(avatars, imgAvatar);  //头像
        topName.setText(nickNames);
        tvRoomId.setText("房间ID " + anchorIds);
        tvAnchorName.setText(nickName);
    }

}
