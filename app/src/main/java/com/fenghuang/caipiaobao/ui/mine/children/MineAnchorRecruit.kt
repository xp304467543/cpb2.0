package com.fenghuang.caipiaobao.ui.mine.children

import com.fenghuang.baselib.base.fragment.BaseContentFragment
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.fragment_anchor_recruit.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-14
 * @ Describe  主播招募
 *
 */

class MineAnchorRecruit : BaseContentFragment() {


    override fun getLayoutResID() = R.layout.fragment_anchor_recruit

    override fun isSwipeBackEnable() = true

    override fun initEvent() {
        imgBack.setOnClickListener { pop() }
    }

}