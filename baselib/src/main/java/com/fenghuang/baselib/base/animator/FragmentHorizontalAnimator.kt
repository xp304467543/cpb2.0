package com.fenghuang.baselib.base.animator

import android.os.Parcelable
import com.fenghuang.baselib.R
import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 * Fragment默认的横向滑动动画
 */

class FragmentHorizontalAnimator : FragmentAnimator(), Parcelable {

    init {
        enter = R.anim.anim_slide_in_right
        exit = R.anim.anim_slide_out_right
        popEnter = R.anim.anim_no
        popExit = R.anim.anim_no
    }
}