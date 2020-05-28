package com.fenghuang.caipiaobao.widget.videoplayer.window;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

import com.example.playerlibrary.style.IStyleSetter;
import com.example.playerlibrary.style.StyleSetter;


/**
 * Created by Taurus on 2018/5/25.
 *
 * see also IWindow{@link IWindow}
 *
 */
@SuppressLint("ViewConstructor")
public class FloatWindow extends FrameLayout implements IWindow, IStyleSetter {

    private IStyleSetter mStyleSetter;

    private WindowHelper mWindowHelper;

    private OnWindowListener onWindowListener;

    public FloatWindow(Context context, View windowView, FloatWindowParams params) {
        super(context);
        if (windowView != null) {
            addView(windowView);
        }
        mStyleSetter = new StyleSetter(this);
        mWindowHelper = new WindowHelper(context, this, params);
        mWindowHelper.setOnWindowListener(mInternalWindowListener);
    }

    private OnWindowListener mInternalWindowListener =
            new OnWindowListener() {
                @Override
                public void onShow() {
                    if(onWindowListener!=null)
                        onWindowListener.onShow();
                }
                @Override
                public void onClose() {
                    resetStyle();
                    if(onWindowListener!=null)
                        onWindowListener.onClose();
                }
            };

    @Override
    public void setOnWindowListener(OnWindowListener onWindowListener) {
        this.onWindowListener = onWindowListener;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setRoundRectShape(float radius) {
        mStyleSetter.setRoundRectShape(radius);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setRoundRectShape(Rect rect, float radius) {
        mStyleSetter.setRoundRectShape(rect, radius);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setOvalRectShape() {
        mStyleSetter.setOvalRectShape();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setOvalRectShape(Rect rect) {
        mStyleSetter.setOvalRectShape(rect);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clearShapeStyle() {
        mStyleSetter.clearShapeStyle();
    }

    /**
     * set shadow.
     * @param elevation
     */
    public void setElevationShadow(float elevation) {
        setElevationShadow(Color.BLACK, elevation);
    }

    @Override
    public void setRotate(int rotate) {
      setRotationX(90);
    }

    /**
     * must setting a color when set shadow, not transparent.
     * @param backgroundColor
     * @param elevation
     */
    public void setElevationShadow(int backgroundColor, float elevation) {
        setBackgroundColor(backgroundColor);
        ViewCompat.setElevation(this, elevation);
    }

    @Override
    public void setDragEnable(boolean dragEnable) {
        mWindowHelper.setDragEnable(dragEnable);
    }

    @Override
    public boolean isWindowShow() {
        return mWindowHelper.isWindowShow();
    }

    @Override
    public void rotationView(int rotate) {
        setRotation(rotate);
    }

    @Override
    public void updateWindowViewLayout(int x, int y) {
        mWindowHelper.updateWindowViewLayout(x, y);
    }

    /**
     * add to WindowManager
     * @return
     */
    @Override
    public boolean show() {
        return mWindowHelper.show();
    }

    @Override
    public boolean show(Animator... items) {
        return mWindowHelper.show(items);
    }

    /**
     * remove from WindowManager
     *
     * @return
     */
    @Override
    public void close() {
        setElevationShadow(0);
        mWindowHelper.close();
    }

    @Override
    public void close(Animator... items) {
        setElevationShadow(0);
        mWindowHelper.close(items);
    }

    public void resetStyle() {
        setElevationShadow(0);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            clearShapeStyle();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mWindowHelper.onInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mWindowHelper.onTouchEvent(event) || super.onTouchEvent(event);
    }


    public void upDateSize(int width, int height){
        mWindowHelper.upDataSize(width, height);
    }

    public void upDateSizeRotate(int width, int height){
        mWindowHelper.upDataSizeRotate(width, height);
    }

    public void setOrientation(int orientation){
        setRotation(90);
    }

}
