package com.fenghuang.caipiaobao.widget

/**
 * Created by leo
 * on 2019/7/9.
 * 阴影控件
 */

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R


class ShadowLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var mBackGroundColor: Int = 0
    private var mBackGroundColorClicked: Int = 0
    private var mShadowColor: Int = 0
    private var mShadowLimit: Float = 0.toFloat()
    private var mCornerRadius: Float = 0.toFloat()
    private var mDx: Float = 0.toFloat()
    private var mDy: Float = 0.toFloat()
    private var leftShow: Boolean = false
    private var rightShow: Boolean = false
    private var topShow: Boolean = false
    private var bottomShow: Boolean = false
    private var shadowPaint: Paint? = null
    private var paint: Paint? = null

    private var leftPacing: Int = 0
    private var topPading: Int = 0
    private var rightPading: Int = 0
    private var bottomPading: Int = 0
    //阴影布局子空间区域
    private val rectf = RectF()


    init {
        initView(context, attrs)
    }

    //动态设置x轴偏移量
    fun setMDx(mDx: Float) {
        if (Math.abs(mDx) > mShadowLimit) {
            if (mDx > 0) {
                this.mDx = mShadowLimit
            } else {
                this.mDx = -mShadowLimit
            }
        } else {
            this.mDx = mDx
        }
        setPading()
    }

    //动态设置y轴偏移量
    fun setMDy(mDy: Float) {
        if (Math.abs(mDy) > mShadowLimit) {
            if (mDy > 0) {
                this.mDy = mShadowLimit
            } else {
                this.mDy = -mShadowLimit
            }
        } else {
            this.mDy = mDy
        }
        setPading()
    }


    fun getmCornerRadius(): Float {
        return mCornerRadius
    }

    //动态设置 圆角属性
    fun setmCornerRadius(mCornerRadius: Int) {
        this.mCornerRadius = mCornerRadius.toFloat()
        if (width != 0 && height != 0) {
            setBackgroundCompat(width, height)
        }
    }

    fun getmShadowLimit(): Float {
        return mShadowLimit
    }

    //动态设置阴影扩散区域
    fun setmShadowLimit(mShadowLimit: Int) {
        this.mShadowLimit = mShadowLimit.toFloat()
        setPading()
    }

    //动态设置阴影颜色值
    fun setmShadowColor(mShadowColor: Int) {
        this.mShadowColor = mShadowColor
        if (width != 0 && height != 0) {
            setBackgroundCompat(width, height)
        }
    }


    fun setLeftShow(leftShow: Boolean) {
        this.leftShow = leftShow
        setPading()
    }

    fun setRightShow(rightShow: Boolean) {
        this.rightShow = rightShow
        setPading()
    }

    fun setTopShow(topShow: Boolean) {
        this.topShow = topShow
        setPading()
    }

    fun setBottomShow(bottomShow: Boolean) {
        this.bottomShow = bottomShow
        setPading()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            setBackgroundCompat(w, h)
        }
    }


    private fun initView(context: Context, attrs: AttributeSet?) {
        initAttributes(attrs)
        shadowPaint = Paint()
        shadowPaint!!.isAntiAlias = true
        shadowPaint!!.style = Paint.Style.FILL


        //矩形画笔
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint!!.style = Paint.Style.FILL
        paint!!.color = mBackGroundColor

        setPading()
    }


    fun setPading() {
        val xPadding = (mShadowLimit + Math.abs(mDx)).toInt()
        val yPadding = (mShadowLimit + Math.abs(mDy)).toInt()

        if (leftShow) {
            leftPacing = xPadding
        } else {
            leftPacing = 0
        }

        if (topShow) {
            topPading = yPadding
        } else {
            topPading = 0
        }


        if (rightShow) {
            rightPading = xPadding
        } else {
            rightPading = 0
        }

        if (bottomShow) {
            bottomPading = yPadding
        } else {
            bottomPading = 0
        }

        setPadding(leftPacing, topPading, rightPading, bottomPading)
    }


    @SuppressLint("ObsoleteSdkInt")
    private fun setBackgroundCompat(w: Int, h: Int) {
        //判断传入的颜色值是否有透明度
        isAddAlpha(mShadowColor)
        val bitmap = createShadowBitmap(w, h, mCornerRadius, mShadowLimit, mDx, mDy, mShadowColor, Color.TRANSPARENT)
        val drawable = BitmapDrawable(bitmap)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(drawable)
        } else {
            background = drawable
        }
    }


    private fun initAttributes(attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout) ?: return

        try {
            //默认是显示
            leftShow = attr.getBoolean(R.styleable.ShadowLayout_hl_leftShow, true)
            rightShow = attr.getBoolean(R.styleable.ShadowLayout_hl_rightShow, true)
            bottomShow = attr.getBoolean(R.styleable.ShadowLayout_hl_bottomShow, true)
            topShow = attr.getBoolean(R.styleable.ShadowLayout_hl_topShow, true)
            mCornerRadius = attr.getDimension(R.styleable.ShadowLayout_hl_cornerRadius, resources.getDimension(R.dimen.dp_0))
            //默认扩散区域宽度
            mShadowLimit = attr.getDimension(R.styleable.ShadowLayout_hl_shadowLimit, resources.getDimension(R.dimen.dp_5))

            //x轴偏移量
            mDx = attr.getDimension(R.styleable.ShadowLayout_hl_dx, 0f)
            //y轴偏移量
            mDy = attr.getDimension(R.styleable.ShadowLayout_hl_dy, 0f)
            mShadowColor = attr.getColor(R.styleable.ShadowLayout_hl_shadowColor, ViewUtils.getColor(R.color.default_shadow_color))
            mBackGroundColor = attr.getColor(R.styleable.ShadowLayout_hl_shadowBackColor, ViewUtils.getColor(R.color.default_shadowback_color))
            mBackGroundColorClicked = attr.getColor(R.styleable.ShadowLayout_hl_shadowBackColorClicked, ViewUtils.getColor(R.color.default_shadowback_color))
            if (mBackGroundColorClicked != -1) {
                isClickable = true
            }
        } finally {
            attr.recycle()
        }
    }


    private fun createShadowBitmap(shadowWidth: Int, shadowHeight: Int, cornerRadius: Float, shadowRadius: Float,
                                   dx: Float, dy: Float, shadowColor: Int, fillColor: Int): Bitmap {
        var shadowWidth = shadowWidth
        var shadowHeight = shadowHeight
        var cornerRadius = cornerRadius
        var shadowRadius = shadowRadius
        var dx = dx
        var dy = dy
        //优化阴影bitmap大小,将尺寸缩小至原来的1/4。
        dx /= 4
        dy /= 4
        shadowWidth /= 4
        shadowHeight /= 4
        cornerRadius /= 4
        shadowRadius /= 4

        val output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_4444)
        val canvas = Canvas(output)

        val shadowRect = RectF(
                shadowRadius,
                shadowRadius,
                shadowWidth - shadowRadius,
                shadowHeight - shadowRadius)

        if (dy > 0) {
            shadowRect.top += dy
            shadowRect.bottom -= dy
        } else if (dy < 0) {
            shadowRect.top += Math.abs(dy)
            shadowRect.bottom -= Math.abs(dy)
        }

        if (dx > 0) {
            shadowRect.left += dx
            shadowRect.right -= dx
        } else if (dx < 0) {

            shadowRect.left += Math.abs(dx)
            shadowRect.right -= Math.abs(dx)
        }


        shadowPaint!!.color = fillColor
        if (!isInEditMode) {
            shadowPaint!!.setShadowLayer(shadowRadius, dx, dy, shadowColor)
        }

        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint!!)
        return output
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectf.left = leftPacing.toFloat()
        rectf.top = topPading.toFloat()
        rectf.right = (width - rightPading).toFloat()
        rectf.bottom = (height - bottomPading).toFloat()
        val trueHeight = (rectf.bottom - rectf.top).toInt()
        if (mCornerRadius > trueHeight / 2) {
            //画圆角矩形
            canvas.drawRoundRect(rectf, (trueHeight / 2).toFloat(), (trueHeight / 2).toFloat(), paint!!)
            //            canvas.drawRoundRect(rectf, trueHeight / 2, trueHeight / 2, paintStroke);
        } else {
            canvas.drawRoundRect(rectf, mCornerRadius, mCornerRadius, paint!!)
            //            canvas.drawRoundRect(rectf, mCornerRadius, mCornerRadius, paintStroke);
        }
    }


    fun isAddAlpha(color: Int) {
        //获取单签颜色值的透明度，如果没有设置透明度，默认加上#2a
        if (Color.alpha(color) == 255) {
            var red = Integer.toHexString(Color.red(color))
            var green = Integer.toHexString(Color.green(color))
            var blue = Integer.toHexString(Color.blue(color))

            if (red.length == 1) {
                red = "0$red"
            }

            if (green.length == 1) {
                green = "0$green"
            }

            if (blue.length == 1) {
                blue = "0$blue"
            }
            val endColor = "#2a$red$green$blue"
            mShadowColor = convertToColorInt(endColor)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mBackGroundColorClicked != -1) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    paint!!.color = mBackGroundColorClicked
                    postInvalidate()
                }

                MotionEvent.ACTION_UP -> {
                    paint!!.color = mBackGroundColor
                    postInvalidate()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    companion object {


        @Throws(IllegalArgumentException::class)
        fun convertToColorInt(argb: String): Int {
            var argb = argb

            if (!argb.startsWith("#")) {
                argb = "#$argb"
            }
            return Color.parseColor(argb)
        }
    }

}

