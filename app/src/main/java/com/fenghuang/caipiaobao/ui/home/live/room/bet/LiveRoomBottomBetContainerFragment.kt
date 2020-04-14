package com.fenghuang.caipiaobao.ui.home.live.room.bet

import android.widget.ImageView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomDialogFragment

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-07
 * @ Describe 直播间投注
 *
 */

class LiveRoomBottomBetContainerFragment(override val layoutResId: Int = R.layout.dialog_live_bet_container) : BottomDialogFragment() {


    var liveRoomBottomBetFragment:LiveRoomBottomBetFragment?=null

    override fun initView() {
        rootView?.findViewById<ImageView>(R.id.imgBetCLose)?.setOnClickListener {
            dismiss()
        }
    }


    override fun initData() {

    }

    override fun initFragment() {
        if (liveRoomBottomBetFragment == null) liveRoomBottomBetFragment = LiveRoomBottomBetFragment()
        childFragmentManager.beginTransaction().add(R.id.containerBet, liveRoomBottomBetFragment!!)
                .commit()
        childFragmentManager.beginTransaction().show(liveRoomBottomBetFragment!!)
        val tran = childFragmentManager.beginTransaction()
        if (liveRoomBottomBetFragment==null) {
            liveRoomBottomBetFragment = LiveRoomBottomBetFragment()
            tran.add(R.id.containerBet, liveRoomBottomBetFragment!!)
        }else{
            tran.show(liveRoomBottomBetFragment!!)
        }
        tran.commit()
    }
}