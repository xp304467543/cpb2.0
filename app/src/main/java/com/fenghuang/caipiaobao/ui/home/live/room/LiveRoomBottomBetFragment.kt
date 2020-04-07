package com.fenghuang.caipiaobao.ui.home.live.room

import android.widget.ImageView
import android.widget.TextView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.widget.dialog.bottom.BaseBottomSheetFragment


/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-07
 * @ Describe 直播间投注
 *
 */

class LiveRoomBottomBetFragment(override val layoutResId: Int = R.layout.dialog_live_bet) : BaseBottomSheetFragment() {


    override fun initView() {
        rootView?.findViewById<ImageView>(R.id.imgBetCLose)?.setOnClickListener {
            close(true)
        }
    }


    override fun initData() {

    }


}