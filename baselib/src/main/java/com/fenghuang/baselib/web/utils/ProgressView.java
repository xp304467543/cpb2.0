package com.fenghuang.baselib.web.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.fenghuang.baselib.R;
import com.fenghuang.baselib.utils.ViewUtils;

/**
 * @ Author  QinTian
 * @ Date  2020-02-15
 * @ Describe
 */
public class ProgressView extends View {
    private Paint mPaint;
    private int mWidth, mHeight;
    private int progress;//加载进度

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.colorOrangeAccent));
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, ow, oh);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, mWidth * progress / 100, mHeight, mPaint);
        super.onDraw(canvas);
    }

    /**
     * 设置新进度 重新绘制
     *
     * @param newProgress 新进度
     */
    public void setProgress(int newProgress) {
        this.progress = newProgress;
        invalidate();
    }

    /**
     * 设置进度条颜色
     *
     * @param color 色值
     */
    public void setColor(int color) {
        mPaint.setColor(color);
    }
}
