package com.fenghuang.caipiaobao.widget.trendview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fenghuang.baselib.utils.ViewUtils;
import com.fenghuang.caipiaobao.R;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.jessyan.autosize.utils.LogUtils;

/**
 * @ Author  QinTian
 * @ Date  2020/1/2- 15:36
 * @ Describe 10码的亚冠和走势
 */
public class TrendView extends View {
    private boolean isShow = true;
    //网格线画笔
    private Paint mLinePaint;
    //左边日期画笔
    private Paint mDatePaint;
    //遗漏画笔
    private Paint mYlPaint;
    //小球画笔
    private Paint mBallPaint;
    //小球号数画笔
    private Paint mNumPaint;
    //小球之间的连线画笔
    private Paint mLinkPaint;

    //网格的水平间距
    private float mDeltaX;

    //网格垂直间距
    private float mDeltaY;
    private OnItemSelectedListener listener;

    public void setItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void itemSelected(int position);
    }

    //红球和篮球分布数据集合
    private List<Map<String, Object>> mBallList = null;

    public TrendView(Context context) {
        this(context, null);
    }

    public TrendView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initSource() {
        //网格线画笔
        mLinePaint = new Paint();
        mLinePaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_F5F7FA));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(0.5f));
        //期数画笔
        mDatePaint = new Paint();
        mDatePaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_999999));
        mDatePaint.setAntiAlias(true);
        mDatePaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(0.5f));
        mDatePaint.setTextSize(ViewUtils.INSTANCE.sp2px(11));
        //遗漏画笔
        mYlPaint = new Paint();
        mYlPaint.setAntiAlias(true);
        mYlPaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(0.5f));
        mYlPaint.setTextSize(ViewUtils.INSTANCE.sp2px(11));
        //小球画笔
        mBallPaint = new Paint();
        mBallPaint.setAntiAlias(true);
        mBallPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_FF513E));
        mBallPaint.setStyle(Paint.Style.FILL);
        //小球号数画笔
        mNumPaint = new Paint();
        mNumPaint.setColor(Color.WHITE);
        mNumPaint.setTextSize(ViewUtils.INSTANCE.sp2px(11));
        mNumPaint.setAntiAlias(true);
        mNumPaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(0.5f));
        //小球之间连线画笔
        mLinkPaint = new Paint();
        mLinkPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_FF513E));
        mLinkPaint.setAntiAlias(true);
        mLinkPaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(1f));
        //设置单个网格的水平和垂直间距
        mDeltaX = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 14;
        mDeltaY = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 14;
    }

    public void upDateData(List<Map<String, Object>> dataList) {
        this.mBallList = dataList;
        initSource();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置测量View的大小:宽度和高度
        if (mBallList != null) {
            setMeasuredDimension(widthMeasureSpec, (int) (mBallList.size() * mDeltaY));
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawXYLine(canvas);
        drawDateText(canvas);
        if (isShow)
            drawYlText(canvas);
        drawLinkLine(canvas);
        drawRedBall(canvas);
        drawNumBall(canvas);

    }

    private float mStartY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float mEndY = event.getY();
                if (mStartY == mEndY) {
                    //点击的行数
                    int lineIndex = (int) (mStartY / getHeight() * mBallList.size());
                    if (null != listener) {
                        listener.itemSelected(lineIndex + 1);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return true;
    }

    /***
     * 绘制红球文字
     * @param canvas 画布
     */
    private void drawNumBall(Canvas canvas) {
        for (int i = 0; i < mBallList.size(); i++) {
            int red = (int) mBallList.get(i).get("red");
            float textWidth = mNumPaint.measureText(red + "");
            Paint.FontMetrics fontMetrics = mNumPaint.getFontMetrics();
            float y = mDeltaY / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
            canvas.drawText(red + "", mDeltaX * 3 + mDeltaX * red + (mDeltaX - textWidth) / 2, y + (i * mDeltaY), mNumPaint);
        }
    }

    public void setShow(boolean show) {
        isShow = show;
        invalidate();
    }

    /***
     * 绘制遗漏文字
     * @param canvas 画布
     */
    private void drawYlText(Canvas canvas) {
        for (int i = 0; i < mBallList.size(); i++) {
            Map<String, Object> map = mBallList.get(i);
            Paint.FontMetrics fontMetrics = mYlPaint.getFontMetrics();
            float y = (mDeltaY / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2) + (i * mDeltaY);
            for (int j = 0; j <= 10; j++) {
                if (j == 0) mYlPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_FF513E));
                else mYlPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_999999));
                canvas.drawText(map.get("num" + j) + "",
                        mDeltaX * 3 + j * mDeltaX + (mDeltaX - mYlPaint.measureText(map.get("num" + j) + "")) / 2,
                        y, mYlPaint);
            }
            int red = (int) map.get("red");
            //记录小球的坐标，连线时，直接使用
            map.put("x", mDeltaX * 3 + red * mDeltaX + mDeltaX / 2);
            map.put("y", mDeltaY / 2 + i * mDeltaY);
        }
    }

    /***
     * 绘制X轴和Y轴的网格线
     * @param canvas 画布
     */
    private void drawXYLine(Canvas canvas) {
        for (int i = 0; i <= mBallList.size(); i++) {
            canvas.drawLine(0, this.mDeltaY * i, getMeasuredWidth(), this.mDeltaY * i, mLinePaint);
        }
        for (int i = 3; i <= 13; i++) {
            canvas.drawLine(this.mDeltaX * i, 0, this.mDeltaX * i, getMeasuredHeight(), mLinePaint);
        }
    }

    private void drawDateText(Canvas canvas) {
        for (int i = 0; i < mBallList.size(); i++) {
            String date = Objects.requireNonNull(mBallList.get(i).get("date")).toString();
            float textWidth = mDatePaint.measureText(date);
            Paint.FontMetrics fontMetrics = mDatePaint.getFontMetrics();
            float y = mDeltaY / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
            canvas.drawText(date, (mDeltaY * 3 - textWidth) / 2, y + (i * mDeltaY), mDatePaint);
        }
    }

    /***
     * 绘制红球在网格中的分布图
     * @param canvas 画布
     */
    private void drawRedBall(Canvas canvas) {
        for (int i = 0; i < mBallList.size(); i++) {
            Map<String, Object> map = mBallList.get(i);
            int red = (int) map.get("red");
            canvas.drawCircle(mDeltaX * 3 + red * mDeltaX + mDeltaX / 2, mDeltaY / 2 + i * mDeltaY, (mDeltaX / 2 - 8.8f), mBallPaint);
            //记录小球的坐标，连线时，直接使用
            map.put("x", mDeltaX * 3 + red * mDeltaX + mDeltaX / 2);
            map.put("y", mDeltaY / 2 + i * mDeltaY);
        }
    }

    /**
     * 红球之间的连线
     *
     * @param canvas
     */
    private void drawLinkLine(Canvas canvas) {
        float startX = (float) mBallList.get(0).get("x");
        float startY = (float) mBallList.get(0).get("y");
        float endX = 0;
        float endY = 0;
        for (int i = 1; i < mBallList.size(); i++) {
            Map<String, Object> map = mBallList.get(i);
            if (i % 2 == 0) {
                startX = (float) map.get("x");
                startY = (float) map.get("y");
            } else {
                endX = (float) map.get("x");
                endY = (float) map.get("y");
            }
            canvas.drawLine(startX, startY, endX, endY, mLinkPaint);
        }
    }
}