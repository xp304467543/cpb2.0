package com.fenghuang.baselib.base.animator

import android.os.Parcelable
import com.fenghuang.baselib.R
import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 *  author : Peter
 *  date   : 2019/8/8 11:07
 *  desc   : 翻转动画
 */
class FragmentFlipAnimator : FragmentAnimator(), Parcelable {

    init {
        enter = R.anim.anim_flip_in
        exit = R.anim.anim_flip_out
        popEnter = R.anim.anim_flip_in
        popExit = R.anim.anim_flip_out
    }
}