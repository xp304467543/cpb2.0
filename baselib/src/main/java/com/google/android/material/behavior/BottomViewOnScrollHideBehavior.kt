package com.google.android.material.behavior

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.animation.AnimationUtils

/**
 * @author Pinger
 * @since 18-12-11 下午2:26
 *
 * 底部控件滑动时的隐藏和展示，可以使用在BottomView,BottomFloatActionButton等底部控件
 */
class BottomViewOnScrollHideBehavior<V : View> : CoordinatorLayout.Behavior<V> {

    private var height = 0
    private var currentState = 2
    private var currentAnimator: ViewPropertyAnimator? = null

    constructor()

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onLayoutChild(parent: CoordinatorLayout, child: V, layoutDirection: Int): Boolean {
        this.height = child.measuredHeight
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == 2
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: V, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        if (this.currentState != STATE_SCROLLED_DOWN && dyConsumed > 0) {
            this.slideDown(child)
        } else if (this.currentState != STATE_SCROLLED_UP && dyConsumed < 0) {
            this.slideUp(child)
        }
    }

    protected fun slideUp(child: V) {
        if (this.currentAnimator != null) {
            this.currentAnimator!!.cancel()
            child.clearAnimation()
        }

        this.currentState = STATE_SCROLLED_UP
        this.animateChildTo(child, 0, ENTER_ANIMATION_DURATION.toLong(), AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)
    }

    protected fun slideDown(child: V) {
        if (this.currentAnimator != null) {
            this.currentAnimator!!.cancel()
            child.clearAnimation()
        }

        this.currentState = STATE_SCROLLED_DOWN
        this.animateChildTo(child, this.height + getMarginBottom(child), EXIT_ANIMATION_DURATION.toLong(), AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR)
    }

    private fun animateChildTo(child: V, targetY: Int, duration: Long, interpolator: TimeInterpolator) {
        this.currentAnimator = child.animate().translationY(targetY.toFloat()).setInterpolator(interpolator).setDuration(duration).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@BottomViewOnScrollHideBehavior.currentAnimator = null
            }
        })
    }


    /**
     * 高度还要加上距离底部的边距
     * 官方的并没有
     */
    private fun getMarginBottom(v: View): Int {
        var marginBottom = 0
        val layoutParams = v.layoutParams
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            marginBottom = layoutParams.bottomMargin
        }
        return marginBottom
    }

    companion object {

        private const val ENTER_ANIMATION_DURATION = 225
        private const val EXIT_ANIMATION_DURATION = 175
        private const val STATE_SCROLLED_DOWN = 1
        private const val STATE_SCROLLED_UP = 2
    }
}

