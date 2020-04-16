package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.os.Bundle
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryConstant
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomDialogFragment

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe 助赢工具
 *
 */

class LiveRoomBetToolsFragment : BottomDialogFragment() {

    override val layoutResId: Int = R.layout.fragment_live_bet_tool

    override fun isShowTop(): Boolean = true

    override fun canceledOnTouchOutside(): Boolean = false

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initFragment() {
    }


    companion object{
        fun newInstance(lotteryID: String?): LiveRoomBetToolsFragment {
            val fragment = LiveRoomBetToolsFragment()
            val bundle = Bundle()
            bundle.putString("LOTTERY_ID", lotteryID)
            fragment.arguments = bundle
            return fragment
        }
    }
}