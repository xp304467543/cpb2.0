package com.fenghuang.caipiaobao.widget.videoplayer.window;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

/**
 *
 * Created by Taurus on 2018/5/27.
 *
 * Used for window playback. Automatically add and remove on WindowManager.
 * The default window type is TYPE_TOAST{@link WindowManager.LayoutParams#TYPE_TOAST}.
 * If you need to customize your window parameter, you can make relevant settings
 * through FloatWindowParams {@link FloatWindowParams} in Constructor.
 *
 * see also
 * {@link WindowVideoView#WindowVideoView(Context, FloatWindowParams)}
 * {@link FloatWindow#FloatWindow(Context, View, FloatWindowParams)}
 *
 * The window drag event is handled by default.If you do not need to,
 * you can set drag disable{@link IWindow#setDragEnable(boolean)}.
 *
 * When you don't need it, be sure to close it.
 *
 */
public interface IWindow {

    int MIN_MOVE_DISTANCE = 20;
    int DURATION_ANIMATION = 200;

    /**
     * set window listener to listen window state, show or close.
     * @param onWindowListener
     */
    void setOnWindowListener(OnWindowListener onWindowListener);

    /**
     * update window layout location.
     * @param x
     * @param y
     */
    void updateWindowViewLayout(int x, int y);

    /**
     * setting window drag enable. default true.
     * @param dragEnable
     */
    void setDragEnable(boolean dragEnable);

    /**
     * show window, default animation show.
     * if you want no animation, you can config it by FloatWindowParams.
     * @return
     */
    boolean show();

    /**
     * show window and play your setting animators.
     * @param items
     * @return
     */
    boolean show(Animator... items);

    /**
     * close window, default animation on window close, you window style setting will be clear.
     */
    void close();

    /**
     * close window and play your setting animators.
     * @param items
     */
    void close(Animator... items);

    /**
     * whether or not window show.
     * @return
     */
    boolean isWindowShow();

    interface OnWindowListener{
        void onShow();
        void onClose();
    }

    void rotationView(int rotate);
}
