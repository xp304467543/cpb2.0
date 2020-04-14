package com.fenghuang.caipiaobao.ui.home.live.room.bet

import android.view.View
import android.widget.ImageView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.google.android.material.tabs.TabLayout

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-14
 * @ Describe
 *
 */

class LiveRoomBottomBetRecordFragment : BaseNormalFragment(){


    override fun getLayoutRes() = R.layout.fragment_live_bet_record

    private var betListen: ((int: Int) -> Unit)? = null

    fun setBetListen(listener: (int: Int) -> Unit) {
        betListen = listener
    }


    override fun initView(rootView: View?) {
        rootView?.findViewById<ImageView>(R.id.imgBetRecordBack)?.setOnClickListener {
            betListen?.invoke(1)
        }
        val livBetTab = rootView?.findViewById<TabLayout>(R.id.livBetTabRecord)
        if (livBetTab != null) {
            livBetTab.removeAllTabs()
            livBetTab.addTab(livBetTab.newTab().setText("未结算"))
            livBetTab.addTab(livBetTab.newTab().setText("已结算"))
        }
    }


}