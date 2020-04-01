package com.fenghuang.caipiaobao.utils

import android.content.Context
import android.view.TextureView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.TextView
import com.fenghuang.caipiaobao.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-28
 * @ Describe
 *
 */

object AnimUtils {


    /**
     * 获取Vip入场动画
     *
     */
    fun getInAnimation(context: Context, view: View) {
        view.visibility = View.VISIBLE
        val anim = loadAnimation(context, R.anim.vip_in)
        view.startAnimation(anim)

    }

    /**
     * 获取Vip出场动画
     *
     */
    fun getOutAnimation(context: Context, view: View) {
        val anim = loadAnimation(context, R.anim.vip_out) as AnimationSet
        anim.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
            }
            override fun onAnimationStart(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
            }
        })
        view.startAnimation(anim)
    }





    /**
     * 获取直播主播开奖入场动画
     *
     */
    fun getLotteryInAnimation(context: Context, view: View) {
        view.visibility = View.VISIBLE
        val anim = loadAnimation(context, R.anim.live_lottery_in)
        view.startAnimation(anim)

    }

    /**
     * 获取直播主播开奖出场动画
     *
     */
    fun getLotteryOutAnimation(context: Context, view: View) {
        val anim = loadAnimation(context, R.anim.live_lottery_out) as AnimationSet
        anim.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
            }
            override fun onAnimationStart(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
            }
        })
        view.startAnimation(anim)
    }
}