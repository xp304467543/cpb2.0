package com.fenghuang.baselib.base.recycler.header.material

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.Animation
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import java.util.*

/**
 * @author Pinger
 * @since 18-12-10 下午2:59
 */
class MaterialProgressDrawable(private val mParent: View) : Drawable(), Animatable {
    /**
     * The list of animators operating on this drawable.
     */
    private val mAnimators = ArrayList<Animation>()

    /**
     * The indicator ring, used to manage animation state.
     */
    private val mRing = Ring()

    /**
     * Canvas rotation in degrees.
     */
    private var mRotation: Float = 0.toFloat()
    private var mAnimation: Animation? = null
    internal var mRotationCount: Float = 0.toFloat()
    private var mWidth: Float = 0.toFloat()
    private var mHeight: Float = 0.toFloat()
    internal var mFinishing: Boolean = false

    @IntDef(LARGE.toInt(), DEFAULT.toInt())
    annotation class ProgressDrawableSize

    init {
        setColorSchemeColors(*COLORS)
        updateSizes(DEFAULT.toInt())
        setupAnimators()
    }

    private fun setSizeParameters(progressCircleWidth: Int, progressCircleHeight: Int,
                                  centerRadius: Float, strokeWidth: Float, arrowWidth: Float, arrowHeight: Float) {
        val metrics = Resources.getSystem().displayMetrics
        val screenDensity = metrics.density

        mWidth = progressCircleWidth * screenDensity
        mHeight = progressCircleHeight * screenDensity
        mRing.setColorIndex(0)
        mRing.mPaint.strokeWidth = strokeWidth * screenDensity
        mRing.mStrokeWidth = strokeWidth * screenDensity
        mRing.mRingCenterRadius = (centerRadius * screenDensity).toDouble()
        mRing.mArrowWidth = (arrowWidth * screenDensity).toInt()
        mRing.mArrowHeight = (arrowHeight * screenDensity).toInt()
        mRing.setInsets(mWidth.toInt(), mHeight.toInt())
        val thisDrawable = this
        thisDrawable.invalidateSelf()
    }

    /*
     * Set the overall size for the progress spinner. This updates the radius
     * and stroke width of the ring.
     */
    fun updateSizes(@ProgressDrawableSize size: Int) {
        if (size == LARGE.toInt()) {
            setSizeParameters(CIRCLE_DIAMETER_LARGE.toInt(), CIRCLE_DIAMETER_LARGE.toInt(), CENTER_RADIUS_LARGE,
                    STROKE_WIDTH_LARGE, ARROW_WIDTH_LARGE.toFloat(), ARROW_HEIGHT_LARGE.toFloat())
        } else {
            setSizeParameters(CIRCLE_DIAMETER.toInt(), CIRCLE_DIAMETER.toInt(), CENTER_RADIUS, STROKE_WIDTH,
                    ARROW_WIDTH.toFloat(), ARROW_HEIGHT.toFloat())
        }
    }

    /*
     * @param show Set to true to display the arrowhead on the progress spinner.
     */
    fun showArrow(show: Boolean) {
        if (mRing.mShowArrow != show) {
            mRing.mShowArrow = show
            val thisDrawable = this
            thisDrawable.invalidateSelf()
        }
    }

    /*
     * @param scale Set the scale of the arrowhead for the spinner.
     */
    fun setArrowScale(scale: Float) {
        if (mRing.mArrowScale != scale) {
            mRing.mArrowScale = scale
            val thisDrawable = this
            thisDrawable.invalidateSelf()
        }
    }

    /*
     * Set the start and end trim for the progress spinner arc.
     *
     * @param startAngle start angle
     * @param endAngle end angle
     */
    fun setStartEndTrim(startAngle: Float, endAngle: Float) {
        mRing.mStartTrim = startAngle
        mRing.mEndTrim = endAngle
        val thisDrawable = this
        thisDrawable.invalidateSelf()
    }

    /*
     * Set the amount of rotation to apply to the progress spinner.
     *
     * @param rotation Rotation is from [0..1]
     */
    fun setProgressRotation(rotation: Float) {
        mRing.mRotation = rotation
        val thisDrawable = this
        thisDrawable.invalidateSelf()
    }

    /*
     * Update the background color of the circle image view.
     */
    fun setBackgroundColor(@ColorInt color: Int) {
        mRing.mBackgroundColor = color
    }

    /*
     * Set the colors used in the progress animation from color resources.
     * The first color will also be the color of the bar that grows in response
     * to a user swipe gesture.
     *
     * @param colors
     */
    fun setColorSchemeColors(@ColorInt vararg colors: Int) {
        mRing.mColors = colors
        mRing.setColorIndex(0)
    }

    override fun getIntrinsicHeight(): Int {
        return mHeight.toInt()
    }

    override fun getIntrinsicWidth(): Int {
        return mWidth.toInt()
    }

    override fun draw(c: Canvas) {
        val thisDrawable = this
        val bounds = thisDrawable.bounds
        val saveCount = c.save()
        c.rotate(mRotation, bounds.exactCenterX(), bounds.exactCenterY())
        mRing.draw(c, bounds)
        c.restoreToCount(saveCount)
    }

    override fun setAlpha(alpha: Int) {
        mRing.mAlpha = alpha
    }

    override fun getAlpha(): Int {
        return mRing.mAlpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mRing.mPaint.colorFilter = colorFilter
        val thisDrawable = this
        thisDrawable.invalidateSelf()
    }

    internal fun setRotation(rotation: Float) {
        mRotation = rotation
        val thisDrawable = this
        thisDrawable.invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun isRunning(): Boolean {
        val animators = mAnimators
        val size = animators.size
        for (i in 0 until size) {
            val animator = animators[i]
            if (animator.hasStarted() && !animator.hasEnded()) {
                return true
            }
        }
        return false
    }

    override fun start() {
        mAnimation!!.reset()
        mRing.storeOriginals()
        // Already showing some part of the ring
        if (mRing.mEndTrim != mRing.mStartTrim) {
            mFinishing = true
            mAnimation!!.duration = (ANIMATION_DURATION / 2).toLong()
            mParent.startAnimation(mAnimation)
        } else {
            mRing.setColorIndex(0)
            mRing.resetOriginals()
            mAnimation!!.duration = ANIMATION_DURATION.toLong()
            mParent.startAnimation(mAnimation)
        }
    }

    override fun stop() {
        mParent.clearAnimation()
        mRing.setColorIndex(0)
        mRing.resetOriginals()
        showArrow(false)
        setRotation(0f)
    }

    internal fun getMinProgressArc(ring: Ring): Float {
        return Math.toRadians(
                ring.mStrokeWidth / (2.0 * Math.PI * ring.mRingCenterRadius)).toFloat()
    }

    // Adapted from ArgbEvaluator.java
    private fun evaluateColorChange(fraction: Float, startValue: Int, endValue: Int): Int {
        val startA = startValue shr 24 and 0xff
        val startR = startValue shr 16 and 0xff
        val startG = startValue shr 8 and 0xff
        val startB = startValue and 0xff

        val endA = endValue shr 24 and 0xff
        val endR = endValue shr 16 and 0xff
        val endG = endValue shr 8 and 0xff
        val endB = endValue and 0xff

        return (startA + (fraction * (endA - startA)).toInt() shl 24
                or (startR + (fraction * (endR - startR)).toInt() shl 16)
                or (startG + (fraction * (endG - startG)).toInt() shl 8)
                or startB + (fraction * (endB - startB)).toInt())
    }

    /*
     * Update the ring color if this is within the last 25% of the animation.
     * The new ring color will be a translation from the starting ring color to
     * the next color.
     */
    internal fun updateRingColor(interpolatedTime: Float, ring: Ring) {
        if (interpolatedTime > COLOR_START_DELAY_OFFSET) {
            // scale the interpolatedTime so that the full
            // transformation from 0 - 1 takes place in the
            // remaining time
            ring.mCurrentColor = evaluateColorChange((interpolatedTime - COLOR_START_DELAY_OFFSET) / (1.0f - COLOR_START_DELAY_OFFSET), ring.startingColor,
                    ring.nextColor)
        }
    }

    internal fun applyFinishTranslation(interpolatedTime: Float, ring: Ring) {
        // shrink back down and complete a full rotation before
        // starting other circles
        // Rotation goes between [0..1].
        updateRingColor(interpolatedTime, ring)
        val targetRotation = (Math.floor((ring.mStartingRotation / MAX_PROGRESS_ARC).toDouble()) + 1f).toFloat()
        val minProgressArc = getMinProgressArc(ring)
        val startTrim = ring.mStartingStartTrim + (ring.mStartingEndTrim - minProgressArc - ring.mStartingStartTrim) * interpolatedTime
        setStartEndTrim(startTrim, ring.mStartingEndTrim)
        val rotation = ring.mStartingRotation + (targetRotation - ring.mStartingRotation) * interpolatedTime
        setProgressRotation(rotation)
    }

    private fun setupAnimators() {
        val ring = mRing
        val animation = object : Animation() {
            public override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (mFinishing) {
                    applyFinishTranslation(interpolatedTime, ring)
                } else {
                    // The minProgressArc is calculated from 0 to create an
                    // angle that matches the stroke width.
                    val minProgressArc = getMinProgressArc(ring)
                    val startingEndTrim = ring.mStartingEndTrim
                    val startingTrim = ring.mStartingStartTrim
                    val startingRotation = ring.mStartingRotation

                    updateRingColor(interpolatedTime, ring)

                    // Moving the start trim only occurs in the first 50% of a
                    // single ring animation
                    if (interpolatedTime <= START_TRIM_DURATION_OFFSET) {
                        // scale the interpolatedTime so that the full
                        // transformation from 0 - 1 takes place in the
                        // remaining time
                        val scaledTime = interpolatedTime / (1.0f - START_TRIM_DURATION_OFFSET)
                        ring.mStartTrim = startingTrim + (MAX_PROGRESS_ARC - minProgressArc) * MATERIAL_INTERPOLATOR
                                .getInterpolation(scaledTime)
                    }

                    // Moving the end trim starts after 50% of a single ring
                    // animation completes
                    if (interpolatedTime > END_TRIM_START_DELAY_OFFSET) {
                        // scale the interpolatedTime so that the full
                        // transformation from 0 - 1 takes place in the
                        // remaining time
                        val minArc = MAX_PROGRESS_ARC - minProgressArc
                        val scaledTime = (interpolatedTime - START_TRIM_DURATION_OFFSET) / (1.0f - START_TRIM_DURATION_OFFSET)
                        ring.mEndTrim = startingEndTrim + minArc * MATERIAL_INTERPOLATOR.getInterpolation(scaledTime)
                    }

                    setProgressRotation(startingRotation + 0.25f * interpolatedTime)

                    val groupRotation = FULL_ROTATION / NUM_POINTS * interpolatedTime + FULL_ROTATION * (mRotationCount / NUM_POINTS)
                    setRotation(groupRotation)
                }
            }
        }
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.RESTART
        animation.interpolator = LINEAR_INTERPOLATOR
        animation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {
                mRotationCount = 0f
            }

            override fun onAnimationEnd(animation: Animation) {
                // do nothing
            }

            override fun onAnimationRepeat(animation: Animation) {
                ring.storeOriginals()
                ring.goToNextColor()
                ring.mStartTrim = ring.mEndTrim
                if (mFinishing) {
                    // finished closing the last ring from the swipe gesture; go
                    // into progress mode
                    mFinishing = false
                    animation.duration = ANIMATION_DURATION.toLong()
                    showArrow(false)
                } else {
                    mRotationCount = (mRotationCount + 1) % NUM_POINTS
                }
            }
        })
        mAnimation = animation
    }

    inner class Ring internal constructor() {
        private val mTempBounds = RectF()
        internal val mPaint = Paint()
        private val mArrowPaint = Paint()

        internal var mStartTrim = 0.0f
        internal var mEndTrim = 0.0f
        internal var mRotation = 0.0f
        internal var mStrokeWidth = 5.0f
        private var mStrokeInset = 2.5f

        internal var mColors: IntArray? = null
        // mColorIndex represents the offset into the available mColors that the
        // progress circle should currently display. As the progress circle is
        // animating, the mColorIndex moves by one to the next available color.
        private var mColorIndex: Int = 0
        internal var mStartingStartTrim: Float = 0.toFloat()
        internal var mStartingEndTrim: Float = 0.toFloat()
        internal var mStartingRotation: Float = 0.toFloat()
        internal var mShowArrow: Boolean = false
        private var mArrow: Path? = null
        internal var mArrowScale: Float = 0.toFloat()
        internal var mRingCenterRadius: Double = 0.toDouble()
        internal var mArrowWidth: Int = 0
        internal var mArrowHeight: Int = 0
        internal var mAlpha: Int = 0
        private val mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        internal var mBackgroundColor: Int = 0
        internal var mCurrentColor: Int = 0

        /*
         * @return int describing the next color the progress spinner should use when drawing.
         */
        val nextColor: Int
            get() = mColors!![nextColorIndex]

        private val nextColorIndex: Int
            get() = (mColorIndex + 1) % mColors!!.size

        val startingColor: Int
            get() = mColors!![mColorIndex]

        init {
            mPaint.strokeCap = Paint.Cap.SQUARE
            mPaint.isAntiAlias = true
            mPaint.style = Paint.Style.STROKE

            mArrowPaint.style = Paint.Style.FILL
            mArrowPaint.isAntiAlias = true
        }

        /*
         * Draw the progress spinner
         */
        fun draw(c: Canvas, bounds: Rect) {
            val arcBounds = mTempBounds
            arcBounds.set(bounds)
            arcBounds.inset(mStrokeInset, mStrokeInset)

            val startAngle = (mStartTrim + mRotation) * 360
            val endAngle = (mEndTrim + mRotation) * 360
            val sweepAngle = endAngle - startAngle

            if (sweepAngle != 0f) {
                mPaint.color = mCurrentColor
                c.drawArc(arcBounds, startAngle, sweepAngle, false, mPaint)
            }

            drawTriangle(c, startAngle, sweepAngle, bounds)

            if (mAlpha < 255) {
                mCirclePaint.color = mBackgroundColor
                mCirclePaint.alpha = 255 - mAlpha
                c.drawCircle(bounds.exactCenterX(), bounds.exactCenterY(), (bounds.width() / 2).toFloat(),
                        mCirclePaint)
            }
        }

        private fun drawTriangle(c: Canvas, startAngle: Float, sweepAngle: Float, bounds: Rect) {
            if (mShowArrow) {
                if (mArrow == null) {
                    mArrow = Path()
                    mArrow!!.fillType = Path.FillType.EVEN_ODD
                } else {
                    mArrow!!.reset()
                }

                // Adjust the position of the triangle so that it is inset as
                // much as the arc, but also centered on the arc.
                val inset = mStrokeInset.toInt() / 2 * mArrowScale
                val x = (mRingCenterRadius * Math.cos(0.0) + bounds.exactCenterX()).toFloat()
                val y = (mRingCenterRadius * Math.sin(0.0) + bounds.exactCenterY()).toFloat()

                // Update the path each time. This works around an issue in SKIA
                // where concatenating a rotation matrix to a scale matrix
                // ignored a starting negative rotation. This appears to have
                // been fixed as of API 21.
                mArrow!!.moveTo(0f, 0f)
                mArrow!!.lineTo(mArrowWidth * mArrowScale, 0f)
                mArrow!!.lineTo(mArrowWidth * mArrowScale / 2, mArrowHeight * mArrowScale)
                mArrow!!.offset(x - inset, y)
                mArrow!!.close()
                // draw a triangle
                mArrowPaint.color = mCurrentColor
                c.rotate(startAngle + sweepAngle - ARROW_OFFSET_ANGLE, bounds.exactCenterX(),
                        bounds.exactCenterY())
                c.drawPath(mArrow!!, mArrowPaint)
            }
        }

        /*
         * @param index Index into the color array of the color to display in
         *            the progress spinner.
         */
        fun setColorIndex(index: Int) {
            mColorIndex = index
            mCurrentColor = mColors!![mColorIndex]
        }

        /*
         * Proceed to the next available ring color. This will automatically
         * wrap back to the beginning of colors.
         */
        fun goToNextColor() {
            setColorIndex(nextColorIndex)
        }

        fun setInsets(width: Int, height: Int) {
            val minEdge = Math.min(width, height).toFloat()
            val insets: Float
            insets = if (mRingCenterRadius <= 0 || minEdge < 0) {
                Math.ceil((mStrokeWidth / 2.0f).toDouble()).toFloat()
            } else {
                (minEdge / 2.0f - mRingCenterRadius).toFloat()
            }
            mStrokeInset = insets
        }

        /*
         * If the start / end trim are offset to begin with, store them so that
         * animation starts from that offset.
         */
        fun storeOriginals() {
            mStartingStartTrim = mStartTrim
            mStartingEndTrim = mEndTrim
            mStartingRotation = mRotation
        }

        /*
         * Reset the progress spinner to default rotation, start and end angles.
         */
        fun resetOriginals() {
            mStartingStartTrim = 0f
            mStartingEndTrim = 0f
            mStartingRotation = 0f
            mStartTrim = 0.toFloat()
            mEndTrim = 0.toFloat()
            mRotation = 0.toFloat()
        }
    }

    companion object {

        private val LINEAR_INTERPOLATOR = LinearInterpolator()
        internal val MATERIAL_INTERPOLATOR: Interpolator = FastOutSlowInInterpolator()

        private const val FULL_ROTATION = 1080.0f

        // Maps to ProgressBar.Large style
        const val LARGE: Byte = 0
        // Maps to ProgressBar default style
        const val DEFAULT: Byte = 1

        // Maps to ProgressBar default style
        private const val CIRCLE_DIAMETER: Byte = 40
        private const val CENTER_RADIUS = 8.75f //should add up to 10 when + stroke_width
        private const val STROKE_WIDTH = 2.5f

        // Maps to ProgressBar.Large style
        private const val CIRCLE_DIAMETER_LARGE: Byte = 56
        private const val CENTER_RADIUS_LARGE = 12.5f
        private const val STROKE_WIDTH_LARGE = 3f

        private val COLORS = intArrayOf(Color.BLACK)

        /**
         * The value in the linear interpolator for animating the drawable at which
         * the color transition should start
         */
        private const val COLOR_START_DELAY_OFFSET = 0.75f
        private const val END_TRIM_START_DELAY_OFFSET = 0.5f
        private const val START_TRIM_DURATION_OFFSET = 0.5f

        /**
         * The duration of a single progress spin in milliseconds.
         */
        private const val ANIMATION_DURATION = 1332

        /**
         * The number of points in the progress "star".
         */
        private const val NUM_POINTS: Byte = 5

        /**
         * Layout info for the arrowhead in dp
         */
        private const val ARROW_WIDTH: Byte = 10
        private const val ARROW_HEIGHT: Byte = 5
        private const val ARROW_OFFSET_ANGLE = 5f

        /**
         * Layout info for the arrowhead for the large spinner in dp
         */
        private const val ARROW_WIDTH_LARGE: Byte = 12
        private const val ARROW_HEIGHT_LARGE: Byte = 6
        private const val MAX_PROGRESS_ARC = .8f
    }
}