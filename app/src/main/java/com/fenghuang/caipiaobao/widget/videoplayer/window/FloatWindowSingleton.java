package com.fenghuang.caipiaobao.widget.videoplayer.window;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.fenghuang.caipiaobao.R;
import com.fenghuang.caipiaobao.constant.UserInfoSp;

/**
 * @ Author  QinTian
 * @ Date  2020-01-21
 * @ Describe
 */
public class FloatWindowSingleton {
    @SuppressLint("StaticFieldLeak")
    private static FloatWindow floatWindow;
    @SuppressLint("StaticFieldLeak")
    public static FrameLayout mWindowVideoContainer;
    public static String anchorId = "";
    public static String lotteryId = "1";
    public static String type = "";
    public static String nickname = "";
    public static String liveState = "";
    public static String avatar = "";
    public static Integer online = 0;
    public static  Boolean isLan = false;


    public static FloatWindow getFloatWindow(Activity activity) {
        if (floatWindow == null) {
            synchronized (FloatWindowSingleton.class) {
                if (floatWindow == null) {
                    int widthPixels = activity.getResources().getDisplayMetrics().widthPixels;
                    int width;
                    if (UserInfoSp.INSTANCE.getVideoSize() == 1) {
                        width = (int) (widthPixels * 0.6f);
                    } else if (UserInfoSp.INSTANCE.getVideoSize() == 2) {
                        width = (int) (widthPixels * 0.8f);
                    } else  {
                        width = (int) (widthPixels * 0.9f);
                    }
                    int type;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0+
                        type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                    } else {
                        type = WindowManager.LayoutParams.TYPE_PHONE;
                    }
                    mWindowVideoContainer = new FrameLayout(activity);
                    floatWindow = new FloatWindow(activity, mWindowVideoContainer,
                            new FloatWindowParams().setWindowType(type)
                                    .setX(100)
                                    .setY(400)
                                    .setWidth(width)
                                    .setHeight(width * 9 / 16));

                    floatWindow.setBackgroundColor(Color.BLACK);

                }
            }
        }
        return floatWindow;
    }


    public static void releaseFloatWindow() {
        isLan = false;
        if (floatWindow != null) {
            floatWindow.close();
            floatWindow = null;
        }
    }

    public static void upDateSizeNormal(int width, int height){
        if (floatWindow != null) {
            floatWindow.upDateSize(width, height);
        }
    }

    public static void upDateSize(int width, int height){
        if (floatWindow != null) {
            isLan = false;
            floatWindow.upDateSize(width, height);
        }
    }
    public static void upDateSizeRotate(int width, int height){
        if (floatWindow != null) {
            isLan = true;
            floatWindow.upDateSizeRotate(width, height);
        }
    }
    public static  void rotateVideo(Context context){
//        mWindowVideoContainer.setRotation(90);
//        ViewGroup.LayoutParams layoutParams = mWindowVideoContainer.getLayoutParams();
//        layoutParams.height =floatWindow.getWidth();
//        layoutParams.width = floatWindow.getHeight();
//        mWindowVideoContainer.setLayoutParams(layoutParams);
    }

    public static String getAnchorId() {
        return anchorId;
    }

    public static String getLotteryId() {
        return lotteryId;
    }

    public static String getRoomType() {
        return type;
    }

    public static String getRoomNickName() {
        return nickname;
    }

    public static String getRoomAvatar() {
        return avatar;
    }

    public static String getRoomLiveState() {
        return liveState;
    }

    public static Integer getRoomLiveOnline() {
        return online;
    }
}
