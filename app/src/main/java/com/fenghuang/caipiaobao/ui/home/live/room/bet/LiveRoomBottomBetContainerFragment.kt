package com.fenghuang.caipiaobao.ui.home.live.room.bet

import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.ToastUtils
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

    var currentFragment = 1

    private var liveRoomBottomBetFragment: LiveRoomBottomBetFragment? = null  //投主页

    private var liveRoomBottomBetToolFragment: LiveRoomBottomBetToolFragment? = null //助赢工具

    private var liveRoomBottomBetRecordFragment: LiveRoomBottomBetRecordFragment? = null //投注记录

    private var transaction: FragmentTransaction? = null

    override fun initView() {

        rootView?.findViewById<ImageView>(R.id.imgBetCLose)?.setOnClickListener {
            dismiss()
        }
    }

    override fun initData() {

    }

    override fun initFragment() {
        changeFragment(currentFragment)
    }

    private fun changeFragment(index: Int) {
        transaction = childFragmentManager.beginTransaction()
        hideFragments()
        when (index) {
            1 -> {
                if (liveRoomBottomBetFragment == null) {
                    liveRoomBottomBetFragment = LiveRoomBottomBetFragment()
                    liveRoomBottomBetFragment?.setBetToolsListen {
                                changeFragment(it)
                    }
                    transaction?.add(R.id.containerBet, liveRoomBottomBetFragment!!)
                } else {
                    transaction?.show(liveRoomBottomBetFragment!!)
                }
            }
            2 -> {
                if (liveRoomBottomBetToolFragment == null) {
                    liveRoomBottomBetToolFragment = LiveRoomBottomBetToolFragment()
                    liveRoomBottomBetToolFragment?.setBetListen{
                        changeFragment(it)
                    }
                    transaction?.add(R.id.containerBet, liveRoomBottomBetToolFragment!!)
                } else {
                    transaction?.show(liveRoomBottomBetToolFragment!!)
                }
            }
            3 -> {
                if (liveRoomBottomBetRecordFragment == null) {
                    liveRoomBottomBetRecordFragment = LiveRoomBottomBetRecordFragment()
                    liveRoomBottomBetRecordFragment?.setBetListen{
                        changeFragment(it)
                    }
                    transaction?.add(R.id.containerBet, liveRoomBottomBetRecordFragment!!)
                } else {
                    transaction?.show(liveRoomBottomBetRecordFragment!!)
                }
            }
        }
        transaction?.commit()
    }

    /**
     * 隐藏片段
     *
     */
    private fun hideFragments() {
        if (liveRoomBottomBetFragment != null) {
            transaction?.hide(liveRoomBottomBetFragment!!)
        }
        if (liveRoomBottomBetToolFragment != null) {
            transaction?.hide(liveRoomBottomBetToolFragment!!)
        }
        if (liveRoomBottomBetRecordFragment != null) {
            transaction?.hide(liveRoomBottomBetRecordFragment!!)
        }
    }

}