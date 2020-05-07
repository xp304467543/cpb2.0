package com.fenghuang.caipiaobao.widget.trendview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.fenghuang.baselib.utils.ViewUtils;
import com.fenghuang.caipiaobao.R;
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryConstant;

/**
 * @ Author  QinTian
 * @ Date  2020/1/2- 15:36
 * @ Describe 10码的亚冠和走势
 */

public class TrendViewHeadType extends View {

    private String type;


    private int lineIndex;

    private String[] str;
    //网格线画笔
    private Paint mLinePaint;
    //文字画笔
    private Paint mTextPaint;
    //头部的高度为一个网格的高度
    private float mDeltaY;

    public TrendViewHeadType(Context context) {
        this(context, null);
    }

    public TrendViewHeadType(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrendViewHeadType(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setType(String type) {
        this.type = type;
        initSource(type);
    }

    private void initSource(String type) {
        //网格线画笔
        mLinePaint = new Paint();
        mLinePaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_EEEEEE));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(0.5f));
        //号数画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_333333));
        mTextPaint.setTextSize(ViewUtils.INSTANCE.sp2px(10));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(0.5f));
        switch (type) {
            case LotteryConstant.TYPE_17:
                mDeltaY = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 14;
                lineIndex = 13;
                str = new String[]{"号码", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
                break;
            case LotteryConstant.TYPE_18:
                mDeltaY = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 21;
                lineIndex = 20;
                str = new String[]{"号码","3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"};
                break;
            case LotteryConstant.TYPE_19:
                mDeltaY = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 14;
                lineIndex = 13;
                str = new String[]{"号码", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
                break;
            case "前三形态":
            case "中三形态":
            case "后三形态":
            case LotteryConstant.TYPE_20:
                mDeltaY = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 14;
                lineIndex = 13;
                break;
            case LotteryConstant.TYPE_21:
                mDeltaY = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 7;
                break;
        }
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (type == null) return;
        if (type.equals(LotteryConstant.TYPE_20) || type.equals("前三形态") || type.equals("中三形态") || type.equals("后三形态")) {
            setMeasuredDimension(widthMeasureSpec, (int) mDeltaY * 2);
        } else if (type.equals(LotteryConstant.TYPE_21)) {
            setMeasuredDimension(widthMeasureSpec, ViewUtils.INSTANCE.getScreenWidth(getContext()) / 14);
        } else {
            setMeasuredDimension(widthMeasureSpec, (int) mDeltaY);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTextPaint == null || mLinePaint == null) return;
        canvas.drawColor(Color.parseColor("#F5F7FA"));
        switch (type) {
            case LotteryConstant.TYPE_20:
            case "前三形态":
            case "中三形态":
            case "后三形态":
                drawFormDateText(canvas);
                drawForm(canvas);
                drawFormLine(canvas);
                break;
            case LotteryConstant.TYPE_21:
                drawTigerDateText(canvas);
                break;
            case LotteryConstant.TYPE_17:
            case LotteryConstant.TYPE_18:
            case LotteryConstant.TYPE_19:
                drawDateText(canvas);
                drawLine(canvas);
                drawText(canvas);
                break;
        }

    }

    //形态走势---------------------------------------------------------------------------------------------------------------------------
    private void drawFormDateText(Canvas canvas) {
        String date = "期号";
        float textWidth = mTextPaint.measureText(date);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float y = mDeltaY + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
        canvas.drawText(date, (mDeltaY * 3 - textWidth) / 2, y, mTextPaint);
    }

    private void drawForm(Canvas canvas) {
        String str_1 = "第";
        String[] str_2;
        if (type.equals("前三形态") || type.equals(LotteryConstant.TYPE_20)) {
            str_2 = new String[]{"一", "二", "三"};
        } else if (type.equals("中三形态")) {
            str_2 = new String[]{"二", "三", "四"};
        } else {
            str_2 = new String[]{"三", "四", "五"};
        }
        String str_3 = "球";
        for (int i = 0; i < 3; i++) {
            Paint.FontMetrics fontMetrics1 = mTextPaint.getFontMetrics();
            float y1 = (mDeltaY / 2 + (Math.abs(fontMetrics1.ascent) - fontMetrics1.descent) / 2);
            canvas.drawText(str_1, mDeltaY * 3 + +mDeltaY * i + (mDeltaY - mTextPaint.measureText(str_1)) / 2, y1, mTextPaint);

            Paint.FontMetrics fontMetrics2 = mTextPaint.getFontMetrics();
            float y2 = (mDeltaY + (Math.abs(fontMetrics2.ascent) - fontMetrics2.descent) / 2);
            canvas.drawText(str_2[i], mDeltaY * 3 + +mDeltaY * i + (mDeltaY - mTextPaint.measureText(str_2[i])) / 2, y2, mTextPaint);

            Paint.FontMetrics fontMetrics3 = mTextPaint.getFontMetrics();
            float y3 = (mDeltaY * 1.6f + (Math.abs(fontMetrics3.ascent) - fontMetrics3.descent) / 2);
            canvas.drawText(str_3, mDeltaY * 3 + +mDeltaY * i + (mDeltaY - mTextPaint.measureText(str_3)) / 2, y3, mTextPaint);
        }

        String[] str_4 = new String[]{"豹子", "顺子", "对子", "半顺", "杂六", "组三", "组六", "豹子"};
        for (int i = 0; i < str_4.length; i++) {
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float y = (mDeltaY * 1.5f + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2);
            canvas.drawText(str_4[i], mDeltaY * 6 + +mDeltaY * i + (mDeltaY - mTextPaint.measureText(str_4[i])) / 2, y, mTextPaint);
        }

        String[] str_5 = new String[]{"形态", "组选类型"};
        for (int i = 0; i < str_5.length; i++) {
            int length;
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float y = mDeltaY / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
            if (i == 0) length = 8;
            else length = 11;
            canvas.drawText(str_5[i], mDeltaY * length + +mDeltaY * i + (mDeltaY - mTextPaint.measureText(str_5[i])) / 2, y, mTextPaint);
        }
    }


    private void drawFormLine(Canvas canvas) {
        for (int i = 3; i <= 8; i++) {
            if (i <= 6) {
                canvas.drawLine(mDeltaY * i, 0, mDeltaY * i, getMeasuredHeight(), mLinePaint);
            } else if (i == 7) {
                canvas.drawLine(mDeltaY * 11, 0, mDeltaY * 11, getMeasuredHeight(), mLinePaint);
            } else {
                canvas.drawLine(mDeltaY * 14, 0, mDeltaY * 14, getMeasuredHeight(), mLinePaint);
            }
        }
        canvas.drawLine(mDeltaY * 6, mDeltaY, getMeasuredWidth(), mDeltaY, mLinePaint);
        for (int i = 6; i <= 14; i++) {
            canvas.drawLine(mDeltaY * i, mDeltaY, mDeltaY * i, getMeasuredHeight(), mLinePaint);
        }
    }

    //形态走势end---------------------------------------------------------------------------------------------------------------------------


    //龙虎走势---------------------------------------------------------------------------------------------------------------------------
    private void drawTigerDateText(Canvas canvas) {
        float height = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 14;
        String date = "期号";
        float textWidth = mTextPaint.measureText(date);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float y = height / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
        canvas.drawText(date, (mDeltaY * 2 - textWidth) / 2, y, mTextPaint);
        canvas.drawText("开奖号码", (mDeltaY * 6 - mTextPaint.measureText("开奖号码")) / 2, y, mTextPaint);
        String[] str = new String[]{"龙", "虎", "和"};
        for (int i = 0; i < str.length; i++) {
            canvas.drawText(str[i], mDeltaY * 4 + mDeltaY * i + (mDeltaY - mTextPaint.measureText(str[i])) / 2, y, mTextPaint);
            canvas.drawLine(mDeltaY * 4 + mDeltaY * i, 0, mDeltaY * 4 + mDeltaY * i, getMeasuredHeight(), mLinePaint);
        }
        canvas.drawLine(mDeltaY * 2, 0, mDeltaY * 2, getMeasuredHeight(), mLinePaint);
        canvas.drawLine(mDeltaY * 6, 0, mDeltaY * 6, getMeasuredHeight(), mLinePaint);
    }


    //龙虎走势end---------------------------------------------------------------------------------------------------------------------------
    private void drawLine(Canvas canvas) {
        for (int i = 3; i <= lineIndex; i++) {
            canvas.drawLine(mDeltaY * i, 0, mDeltaY * i, getMeasuredHeight(), mLinePaint);
        }
    }

    private void drawText(Canvas canvas) {
        if (str == null) return;
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
        canvas.drawText(date, (mDeltaY * 3 - textWidth) / 2, y, mTextPaint);
    }
}
