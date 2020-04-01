package com.fenghuang.caipiaobao.widget

import android.animation.Keyframe
import android.animation.PropertyValuesHolder
import android.animation.ObjectAnimator
import android.view.View
import com.fenghuang.caipiaobao.R
import android.view.animation.AlphaAnimation
import android.view.animation.Animation


/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-24
 * @ Describe
 *
 */

object ObjectAnimatorViw {

    fun tada(view: View, shakeFactor: Float): ObjectAnimator {
        val pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        )

        val pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        )

        val pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0f)
        )

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).setDuration(1000)
    }

    fun nope(view: View, shakeFactor: Float): ObjectAnimator {

        val pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0f)
        )

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhRotate).setDuration(700)
    }


    /**
     * View渐现动画效果
     */
    var mShowAnimation: AlphaAnimation? = null
    var mHideAnimation: AlphaAnimation? = null
    fun setShowAnimation(view: View?, duration: Long) {
        view?.visibility = View.VISIBLE
        if (null == view || duration < 0) {
            return
        }
        mShowAnimation?.cancel()
        mShowAnimation = AlphaAnimation(0.0f, 1.0f)
        mShowAnimation?.duration = duration
        mShowAnimation?.fillAfter = true
        view.startAnimation(mShowAnimation)
    }


    /**
     * View渐隐动画效果
     */
    fun setHideAnimation(view: View?, duration: Long) {
        if (null == view || duration < 0) {
            return
        }

        if (null != mHideAnimation) {
            mHideAnimation?.cancel()
        }
        // 监听动画结束的操作
        mHideAnimation = AlphaAnimation(1.0f, 0.0f)
        mHideAnimation?.duration = duration
        mHideAnimation?.fillAfter = true
        mHideAnimation?.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(arg0: Animation) {

            }

            override fun onAnimationRepeat(arg0: Animation) {

            }

            override fun onAnimationEnd(arg0: Animation) {
                view.visibility = View.GONE
            }
        })
        view.startAnimation(mHideAnimation)
    }
}