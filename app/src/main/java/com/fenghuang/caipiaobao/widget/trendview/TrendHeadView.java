package com.fenghuang.caipiaobao.widget.trendview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.fenghuang.baselib.utils.ViewUtils;
import com.fenghuang.caipiaobao.R;

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/2- 15:36
 * @ Describe 10码的亚冠和走势
 *
 */

public class TrendHeadView extends View {
    private String[] str = {"号码","0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    //网格线画笔
    private Paint mLinePaint;
    //文字画笔
    private Paint mTextPaint;
    //头部的高度为一个网格的高度
    private float mDeltaY;

    public TrendHeadView(Context context) {
        this(context, null);
    }

    public TrendHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrendHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSource();
    }

    private void initSource() {
        //网格线画笔
        mLinePaint = new Paint();
        mLinePaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_EEEEEE));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px( 1));
        //号数画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_333333));
        mTextPaint.setTextSize(ViewUtils.INSTANCE.sp2px( 10));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px( 0.5f));
        mDeltaY = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 14;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, (int) mDeltaY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#F5F7FA"));
        drawDateText(canvas);
        drawLine(canvas);
        drawText(canvas);
    }

    private void drawLine(Canvas canvas) {
        for (int i = 3; i <= 13; i++) {
            canvas.drawLine(mDeltaY * i, 0, mDeltaY * i, getMeasuredHeight(), mLinePaint);
        }
    }

    private void drawText(Canvas canvas) {
        for (int i = 0; i < str.length; i++) {
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float y = (mDeltaY / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2);
            canvas.drawText(str[i], mDeltaY * 3 + +mDeltaY * i + (mDeltaY - mTextPaint.measureText(str[i])) / 2, y, mTextPaint);
        }
    }

    private void drawDateText(Canvas canvas) {
        String date = "期号";
        float textWidth = mTextPaint.measureText(date);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float y = mDeltaY / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
        canvas.drawText(date, (mDeltaY*3 - textWidth )/2, y, mTextPaint);
    }
}
