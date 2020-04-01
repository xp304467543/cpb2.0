package com.fenghuang.caipiaobao.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.fenghuang.baselib.utils.ViewUtils
import kotlin.math.abs
import kotlin.math.min


/**
 * 自定义文本跑马灯效果
 */
class MarqueeTextView : View {
    /**
     * Set the text scrolling speed, if the value < 0, set to the default is 0
     *
     * @param speed If this value is 0, then stop scrolling
     */
    private var textSpeed = 2.0f
        set(speed) {
            field = (if (speed < 0) 0 else speed) as Float
            invalidate()
        } //The default text scroll speed
    private var isScroll = true
        set(isScroll) {
            field = isScroll
            invalidate()
        } //The default set as auto scroll
    private var mContext: Context? = null
    private var mPaint: Paint? = null
    private var mText: String? = null//This is to display the content
    private var mTextSize: Float = 0.toFloat()//This is text size
    private var mTextColor: Int = 0 //This is text color

    private var mCoordinateX: Float = 0.toFloat()//Draw the starting point of the X coordinate
    private var mCoordinateY: Float = 0.toFloat()//Draw the starting point of the Y coordinate
    private var mTextWidth: Float = 0.toFloat() //This is text width
    private var mViewWidth: Int = 0 //This is View width

    constructor(context: Context) : super(context) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)

    }

    /**
     * Initializes the related parameters
     *
     * @param context
     */
    private fun init(context: Context) {
        this.mContext = context

        if (TextUtils.isEmpty(mText)) {
            mText = ""
        }
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.textSize = ViewUtils.sp2px(12f)
    }


    fun setText(text: String) {
        mText = text
        if (TextUtils.isEmpty(mText)) {
            mText = ""
        }
        requestLayout()
        invalidate()
    }

    /**
     * Set the text size, if this value is < 0, set to the default size
     *
     * @param textSize
     */
    fun setTextSize(textSize: Float) {
        this.mTextSize = textSize
        mPaint!!.textSize = if (mTextSize <= 0) ViewUtils.sp2px(12f) else mTextSize
        requestLayout()
        invalidate()
    }

    fun setTextColor(textColor: Int) {
        this.mTextColor = textColor
        mPaint!!.color = mTextColor
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mTextWidth = mPaint!!.measureText(mText)
        mCoordinateX = paddingLeft.toFloat()
        mCoordinateY = paddingTop + abs(mPaint!!.ascent())
        mViewWidth = measureWidth(widthMeasureSpec)
        val mViewHeight = measureHeight(heightMeasureSpec)

        //If you do not call this method, will be thrown "IllegalStateException"
        setMeasuredDimension(mViewWidth, mViewHeight)
    }


    private fun measureWidth(measureSpec: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = (mPaint!!.measureText(mText) + paddingLeft
                    + paddingRight).toInt()
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }

        return result
    }

    private fun measureHeight(measureSpec: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = (mPaint!!.textSize + paddingTop
                    + paddingBottom).toInt()
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawText(mText!!, mCoordinateX, mCoordinateY, mPaint!!)

        if (!this.isScroll) {
            return
        }

        mCoordinateX -= textSpeed

        if (abs(mCoordinateX) > mTextWidth && mCoordinateX < 0) {
            mCoordinateX = mViewWidth.toFloat()
        }

        invalidate()

    }

}
