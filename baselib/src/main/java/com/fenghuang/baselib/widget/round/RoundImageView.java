package com.fenghuang.baselib.widget.round;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.fenghuang.baselib.R;


/**
 * 圆角图片，带有边框
 */
public class RoundImageView extends AppCompatImageView {

    private final PorterDuffXfermode xFermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    protected Path roundPath;
    protected Path borderPath;
    protected float borderWidth;
    protected int borderColor;
    private float leftTopRadius;
    private float rightTopRadius;
    private float rightBottomRadius;
    private float leftBottomRadius;
    private Paint mBitmapPaint;
    private Paint borderPaint;

    public RoundImageView(Context context) {
        this(context, null, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    protected void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.RoundImageView);
            borderWidth = ta.getDimension(R.styleable.RoundImageView_riv_borderWidth, 0);
            borderColor = ta.getColor(R.styleable.RoundImageView_riv_borderColor, 0);
            leftTopRadius = ta.getDimension(R.styleable.RoundImageView_riv_leftTopRadius, 0);
            rightTopRadius = ta.getDimension(R.styleable.RoundImageView_riv_rightTopRadius, 0);
            rightBottomRadius = ta.getDimension(R.styleable.RoundImageView_riv_rightBottomRadius, 0);
            leftBottomRadius = ta.getDimension(R.styleable.RoundImageView_riv_leftBottomRadius, 0);
            float radius = ta.getDimension(R.styleable.RoundImageView_riv_radius, 0);
            if (radius != 0) {
                leftTopRadius = radius;
                rightTopRadius = radius;
                rightBottomRadius = radius;
                leftBottomRadius = radius;
            }
            ta.recycle();
        }
    }

    private void init() {
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        roundPath = new Path();
        borderPath = new Path();

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStrokeWidth(borderWidth);

        setScaleType(ScaleType.CENTER_CROP);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            initBorderPath();
            initRoundPath();
        }
    }


    /**
     * 初始化边框Path
     */
    protected void initBorderPath() {
        roundPath.reset();
        final int width = getWidth();
        final int height = getHeight();
        leftTopRadius = Math.min(leftTopRadius, Math.min(width, height) * 0.5f);
        rightTopRadius = Math.min(rightTopRadius, Math.min(width, height) * 0.5f);
        rightBottomRadius = Math.min(rightBottomRadius, Math.min(width, height) * 0.5f);
        leftBottomRadius = Math.min(leftBottomRadius, Math.min(width, height) * 0.5f);

        RectF rect = new RectF(0, 0, width, height);
        roundPath.addRoundRect(rect,
                new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius,
                        rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius},
                Path.Direction.CW);
    }

    /**
     * 初始化图片区域Path
     */
    protected void initRoundPath() {
        borderPath.reset();
        /**
         * 乘以0.5会导致border在圆角处不能包裹原图
         */
        final float halfBorderWidth = borderWidth * 0.35f;
        final int width = getWidth();
        final int height = getHeight();
        leftTopRadius = Math.min(leftTopRadius, Math.min(width, height) * 0.5f);
        rightTopRadius = Math.min(rightTopRadius, Math.min(width, height) * 0.5f);
        rightBottomRadius = Math.min(rightBottomRadius, Math.min(width, height) * 0.5f);
        leftBottomRadius = Math.min(leftBottomRadius, Math.min(width, height) * 0.5f);

        RectF rect = new RectF(halfBorderWidth, halfBorderWidth,
                width - halfBorderWidth, height - halfBorderWidth);
        borderPath.addRoundRect(rect,
                new float[]{leftTopRadius, leftTopRadius, rightTopRadius, rightTopRadius,
                        rightBottomRadius, rightBottomRadius, leftBottomRadius, leftBottomRadius},
                Path.Direction.CW);
    }


    /**
     * 获取图片区域纯颜色Bitmap
     */
    protected Bitmap getRoundBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        canvas.drawPath(roundPath, paint);
        return bitmap;
    }

    private void drawBorder(Canvas canvas) {
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        canvas.drawPath(borderPath, borderPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawImage(canvas);
        drawBorder(canvas);
    }

    private void drawImage(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (!isInEditMode() && drawable != null) {
            try {
                Bitmap bitmap;
                if (drawable instanceof ColorDrawable) {
                    bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
                } else {
                    bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                }
                Canvas drawCanvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, drawCanvas.getWidth(), drawCanvas.getHeight());
                drawable.draw(drawCanvas);

                Bitmap roundBm = getRoundBitmap();
                mBitmapPaint.reset();
                mBitmapPaint.setFilterBitmap(false);
                mBitmapPaint.setXfermode(xFermode);
                drawCanvas.drawBitmap(roundBm, 0, 0, mBitmapPaint);
                mBitmapPaint.setXfermode(null);
                canvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


