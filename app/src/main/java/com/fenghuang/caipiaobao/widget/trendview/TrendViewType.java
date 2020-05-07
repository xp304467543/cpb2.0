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

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ Author  QinTian
 * @ Date  2020/1/2- 15:36
 * @ Describe 10码的亚冠和走势
 */
public class TrendViewType extends View {

    private String type;

    private int index;

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
    //蓝球之间的连线画笔
    private Paint mLinkPaintBlue;
    //蓝色小球画笔
    private Paint mBlueBallPaint;
    //网格的水平间距
    private float mDeltaX;
    //网格垂直间距
    private float mDeltaY;


    //红球和篮球分布数据集合
    private List<Map<String, Object>> mBallList = null;

    public TrendViewType(Context context) {
        this(context, null);
    }

    public TrendViewType(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrendViewType(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initSource(String type) {
        //网格线画笔
        mLinePaint = new Paint();
        mLinePaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_F5F7FA));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(0.5f));
        //期数画笔
        mDatePaint = new Paint();
        mDatePaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_333333));
        mDatePaint.setAntiAlias(true);
        mDatePaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(0.5f));
        if (type.equals(LotteryConstant.TYPE_18)) {
            mDatePaint.setTextSize(ViewUtils.INSTANCE.sp2px(8));
        } else
            mDatePaint.setTextSize(ViewUtils.INSTANCE.sp2px(10));
        //遗漏画笔
        mYlPaint = new Paint();
        mYlPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_333333));
        mYlPaint.setAntiAlias(true);
        mYlPaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(0.5f));
        mYlPaint.setTextSize(ViewUtils.INSTANCE.sp2px(10));
        //小球画笔
        mBallPaint = new Paint();
        mBallPaint.setAntiAlias(true);
        mBallPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_FF513E));
        mBallPaint.setStyle(Paint.Style.FILL);
        //小球号数画笔
        mNumPaint = new Paint();
        mNumPaint.setColor(Color.WHITE);
        mNumPaint.setTextSize(ViewUtils.INSTANCE.sp2px(10));
        mNumPaint.setAntiAlias(true);
        mNumPaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(0.5f));
        //小球之间连线画笔
        mLinkPaint = new Paint();
        mLinkPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_FF513E));
        mLinkPaint.setAntiAlias(true);
        mLinkPaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(1f));
        if (type.equals(LotteryConstant.TYPE_20) || type.equals("前三形态") || type.equals("中三形态") || type.equals("后三形态")) {
            //蓝色小球之间连线画笔
            mLinkPaintBlue = new Paint();
            mLinkPaintBlue.setColor(ViewUtils.INSTANCE.getColor(R.color.colorBlue));
            mLinkPaintBlue.setAntiAlias(true);
            mLinkPaintBlue.setStrokeWidth(ViewUtils.INSTANCE.dp2px(1f));
            //蓝色小球画笔
            mBlueBallPaint = new Paint();
            mBlueBallPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.colorBlue));
            mBlueBallPaint.setAntiAlias(true);
            mBlueBallPaint.setStrokeWidth(ViewUtils.INSTANCE.dp2px(1f));
        }
        //设置单个网格的水平和垂直间距
        switch (type) {
            case LotteryConstant.TYPE_17:
            case LotteryConstant.TYPE_19:
            case LotteryConstant.TYPE_20:
            case "前三形态":
            case "中三形态":
            case "后三形态":
                mDeltaX = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 14;
                mDeltaY = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 14;
                index = 10;
                break;
            case LotteryConstant.TYPE_18:
                mDeltaX = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 21;
                mDeltaY = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 21;
                index = 17;
                break;
            case LotteryConstant.TYPE_21:
                mDeltaY = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 14;
                mDeltaX = ViewUtils.INSTANCE.getScreenWidth(getContext()) / 7;
                break;
        }

    }

    public void upDateData(List<Map<String, Object>> dataList, String type) {
        if (this.mBallList != null) this.mBallList.clear();
        this.type = type;
        this.mBallList = dataList;
        initSource(type);
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
        if (mLinePaint != null && mDatePaint != null && mYlPaint != null && mBallPaint != null && mNumPaint != null && mLinkPaint != null) {
            if (type.equals(LotteryConstant.TYPE_21)) {
                drawTigerView(canvas);
            } else drawView(canvas);
        }
    }

    /***
     * 绘制view  【号码走势_TYPE_17】  【亚冠走势_TYPE_18】 【基本走势_TYPE_19】 【形态走势_TYPE_20】
     */
    private void drawView(Canvas canvas) {
        //  绘制X轴和Y轴的网格线
        for (int i = 0; i <= mBallList.size(); i++) {
            canvas.drawLine(0, this.mDeltaY * i, getMeasuredWidth(), this.mDeltaY * i, mLinePaint);
        }
        for (int i = 3; i <= index + 3; i++) {
            canvas.drawLine(this.mDeltaX * i, 0, this.mDeltaX * i, getMeasuredHeight(), mLinePaint);
        }

        //先去绘制 红球之间的连线
        drawLinkLine(canvas);


        for (int i = 0; i < mBallList.size(); i++) {
            Map<String, Object> map = mBallList.get(i);
            //记录小球的坐标，连线时，直接使用
            int red = (int) map.get("red");
            //绘制期数
            String date = Objects.requireNonNull(mBallList.get(i).get("date")).toString();
            float textWidth = mDatePaint.measureText(date);
            Paint.FontMetrics fontMetricsDate = mDatePaint.getFontMetrics();
            float yDate = mDeltaY / 2 + (Math.abs(fontMetricsDate.ascent) - fontMetricsDate.descent) / 2;
            canvas.drawText(date, (mDeltaY * 3 - textWidth) / 2, yDate + (i * mDeltaY), mDatePaint);


            //绘制遗漏文字
            Paint.FontMetrics fontMetrics = mYlPaint.getFontMetrics();
            float y = (mDeltaY / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2) + (i * mDeltaY);
            for (int j = 0; j <= index; j++) {
                if (j == 0 && (type.equals(LotteryConstant.TYPE_17) || type.equals(LotteryConstant.TYPE_19) || type.equals(LotteryConstant.TYPE_18)))
                    mYlPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_FF513E));
                else mYlPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_999999));
                canvas.drawText(map.get("num" + j) + "",
                        mDeltaX * 3 + j * mDeltaX + (mDeltaX - mYlPaint.measureText(map.get("num" + j) + "")) / 2,
                        y, mYlPaint);
            }
            //绘制红球文字

            Paint.FontMetrics fontMetricsRed = mNumPaint.getFontMetrics();
            float yRed = mDeltaY / 2 + (Math.abs(fontMetricsRed.ascent) - fontMetricsRed.descent) / 2;

            //绘制红球在网格中的分布图
            switch (type) {
                case LotteryConstant.TYPE_17:
                    float textWidthRed_17 = mNumPaint.measureText(red + 1 + "");
                    canvas.drawCircle(mDeltaX * 4 + red * mDeltaX + mDeltaX / 2, mDeltaY / 2 + i * mDeltaY, (mDeltaX / 2 - 8.8f), mBallPaint);
                    canvas.drawText(red + 1 + "", mDeltaX * 4 + mDeltaX * red + (mDeltaX - textWidthRed_17) / 2, yRed + (i * mDeltaY), mNumPaint);
                    break;
                case LotteryConstant.TYPE_19:
                    float textWidthRed = mNumPaint.measureText(red + "");
                    canvas.drawCircle(mDeltaX * 4 + red * mDeltaX + mDeltaX / 2, mDeltaY / 2 + i * mDeltaY, (mDeltaX / 2 - 8.8f), mBallPaint);
                    canvas.drawText(red + "", mDeltaX * 4 + mDeltaX * red + (mDeltaX - textWidthRed) / 2, yRed + (i * mDeltaY), mNumPaint);
                    break;
                case LotteryConstant.TYPE_18:
                    float textWidthRedRed = mNumPaint.measureText(red + 3 + "");
                    canvas.drawCircle(mDeltaX * 4 + red * mDeltaX + mDeltaX / 2, mDeltaY / 2 + i * mDeltaY, (mDeltaX / 2 - 6f), mBallPaint);
                    canvas.drawText(red + 3 + "", mDeltaX * 4 + mDeltaX * red + (mDeltaX - textWidthRedRed) / 2, yRed + (i * mDeltaY), mNumPaint);
                    break;
                case "前三形态":
                case "中三形态":
                case "后三形态":
                case LotteryConstant.TYPE_20:
                    int blue = (int) map.get("blue");
                    mNumPaint.setTextSize(ViewUtils.INSTANCE.sp2px(8));
                    String[] str = new String[]{"豹子", "顺子", "对子", "半顺", "杂六"};
                    String[] str2 = new String[]{"组三", "组六", "豹子"};
                    float textWidthFormRed = mNumPaint.measureText(str[red]);
                    float textWidthFormBlue = mNumPaint.measureText(str2[blue]);
                    canvas.drawCircle(mDeltaX * 6 + red * mDeltaX + mDeltaX / 2, mDeltaY / 2 + i * mDeltaY, (mDeltaX / 2 - 6f), mBallPaint);
                    canvas.drawText(str[red], mDeltaY * 6 + mDeltaX * red + (mDeltaX - textWidthFormRed) / 2, yRed + (i * mDeltaY), mNumPaint);
                    canvas.drawCircle(mDeltaX * 11 + blue * mDeltaX + mDeltaX / 2, mDeltaY / 2 + i * mDeltaY, (mDeltaX / 2 - 6f), mBlueBallPaint);
                    canvas.drawText(str2[blue], mDeltaY * 11 + mDeltaX * blue + (mDeltaX - textWidthFormBlue) / 2, yRed + (i * mDeltaY), mNumPaint);
                    break;
            }


        }

    }


    /**
     * 红球之间的连线
     */
    private void drawLinkLine(Canvas canvas) {
        for (int i = 0; i < mBallList.size(); i++) {
            Map<String, Object> map = mBallList.get(i);
            //记录小球的坐标，连线时，直接使用
            int red = (int) map.get("red");
            switch (type) {
                case LotteryConstant.TYPE_17:
                case LotteryConstant.TYPE_19:
                case LotteryConstant.TYPE_21:
                case LotteryConstant.TYPE_18:
                    map.put("x", mDeltaX * 4 + red * mDeltaX + mDeltaX / 2);
                    map.put("y", mDeltaY / 2 + i * mDeltaY);
                    break;
                case "前三形态":
                case "中三形态":
                case "后三形态":
                case LotteryConstant.TYPE_20:
                    //记录蓝球坐标，连线时，直接使用
                    int blue = (int) map.get("blue");
                    map.put("x", mDeltaX * 6 + red * mDeltaX + mDeltaX / 2);
                    map.put("y", mDeltaY / 2 + i * mDeltaY);
                    map.put("xBlue", mDeltaX * 11 + blue * mDeltaX + mDeltaX / 2);
                    map.put("yBlue", mDeltaY / 2 + i * mDeltaY);
                    break;
            }
        }

        float startX = (float) mBallList.get(0).get("x");
        float startY = (float) mBallList.get(0).get("y");
        float endX = 0;
        float endY = 0;
        if (type.equals(LotteryConstant.TYPE_20) || type.equals("前三形态") || type.equals("中三形态") || type.equals("后三形态")) {
            float startBlueX = (float) mBallList.get(0).get("xBlue");
            float startBlueY = (float) mBallList.get(0).get("yBlue");
            float endBlueX = 0;
            float endBlueY = 0;
            for (int i = 1; i < mBallList.size(); i++) {
                Map<String, Object> map = mBallList.get(i);
                if (i % 2 == 0) {
                    startX = (float) map.get("x");
                    startY = (float) map.get("y");
                    startBlueX = (float) map.get("xBlue");
                    startBlueY = (float) map.get("yBlue");
                } else {
                    endX = (float) map.get("x");
                    endY = (float) map.get("y");
                    endBlueX = (float) map.get("xBlue");
                    endBlueY = (float) map.get("yBlue");
                }
                canvas.drawLine(startX, startY, endX, endY, mLinkPaint);
                canvas.drawLine(startBlueX, startBlueY, endBlueX, endBlueY, mLinkPaintBlue);
            }
        } else {
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

    /***
     * 绘制view  【龙虎走势_TYPE_21】
     */
    private void drawTigerView(Canvas canvas) {


        //  绘制X轴和Y轴的网格线
        for (int i = 0; i <= mBallList.size(); i++) {
            canvas.drawLine(0, this.mDeltaY * i, getMeasuredWidth(), this.mDeltaY * i, mLinePaint);
        }
        canvas.drawLine(mDeltaX * 2, 0, mDeltaX * 2, getMeasuredHeight(), mLinePaint);
        canvas.drawLine(mDeltaX * 6, 0, mDeltaX * 6, getMeasuredHeight(), mLinePaint);
        for (int i = 0; i < 3; i++) {
            canvas.drawLine(mDeltaX * 4 + mDeltaX * i, 0, mDeltaX * 4 + mDeltaX * i, getMeasuredHeight(), mLinePaint);
        }
        //  红球之间连线
        drawLinkLine(canvas);
        for (int i = 0; i < mBallList.size(); i++) {
            Map<String, Object> map = mBallList.get(i);
            //记录小球的坐标，连线时，直接使用
            int red = (int) map.get("red");
            //绘制期数
            String date = Objects.requireNonNull(mBallList.get(i).get("date")).toString();
            float textWidth = mDatePaint.measureText(date);
            Paint.FontMetrics fontMetricsDate = mDatePaint.getFontMetrics();
            float yDate = mDeltaY / 2 + (Math.abs(fontMetricsDate.ascent) - fontMetricsDate.descent) / 2;
            canvas.drawText(date, (mDeltaX * 2 - textWidth) / 2, yDate + (i * mDeltaY), mDatePaint);
            //绘制遗漏文字
            Paint.FontMetrics fontMetrics = mYlPaint.getFontMetrics();
            float y = (mDeltaY / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2) + (i * mDeltaY);
            for (int j = 1; j < 4; j++) {
                mYlPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_333333));
                canvas.drawText(map.get("num" + j) + "",
                        mDeltaX * 3 + j * mDeltaX + (mDeltaX - mYlPaint.measureText(map.get("num" + j) + "")) / 2,
                        y, mYlPaint);
            }
            //绘制开奖号码
            String[] numStr = Objects.requireNonNull(map.get("num0")).toString().split(",");
            for (int a = 0; a < numStr.length; a++) {
                if (a == 0 || a == numStr.length - 1)
                    mYlPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_FF513E));
                else mYlPaint.setColor(ViewUtils.INSTANCE.getColor(R.color.color_333333));
                canvas.drawText(numStr[a], mDeltaX * 2 + mDeltaX / 3.8f * a + (mDeltaX - mYlPaint.measureText(numStr[a])) / 2, y, mYlPaint);
            }

            //绘制红球文字
            try {
                String[] numText = new String[]{"龙", "虎", "和"};
                float textWidthRed = mNumPaint.measureText(numText[red]);
                Paint.FontMetrics fontMetricsRed = mNumPaint.getFontMetrics();
                float yRed = mDeltaY / 2 + (Math.abs(fontMetricsRed.ascent) - fontMetricsRed.descent) / 2;
                canvas.drawCircle(mDeltaX * 4 + red * mDeltaX + mDeltaX / 2, mDeltaY / 2 + i * mDeltaY, (mDeltaY / 2 - 8f), mBallPaint);
                canvas.drawText(numText[red], mDeltaX * 4 + mDeltaX * red + (mDeltaX - textWidthRed) / 2, yRed + (i * mDeltaY), mNumPaint);
            } catch (Exception ignored) {

            }

        }
    }


}