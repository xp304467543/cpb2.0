package com.fenghuang.caipiaobao.ui.home.live.room.betting

import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomDialogFragment
import com.fenghuang.caipiaobao.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020/4/24
 * @ Describe 投注确认
 *
 */

class LiveRoomBetAccessFragment() : BottomDialogFragment() {

    override val layoutResId: Int = R.layout.dialog_fragment_bet_access

    override val resetHeight: Int =  ViewUtils.getScreenHeight() * 2 / 3 - ViewUtils.dp2px(150)

    override fun isShowTop() = false

    override fun canceledOnTouchOutside() = true

    override fun initView() {
    }

    override fun initData() {
    }


    override fun initFragment() {
    }


}