package com.fenghuang.caipiaobao.ui.home.live.room.bet

import android.view.View
import android.widget.ImageView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_live_bet_tool.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-13
 * @ Describe 助赢工具(直播间)
 *
 */

class LiveRoomBottomBetToolFragment : BaseNormalFragment() {


    override fun getLayoutRes() = R.layout.fragment_live_bet_tool

    private var betListen: ((int: Int) -> Unit)? = null

    fun setBetListen(listener: (int: Int) -> Unit) {
        betListen = listener
    }

    override fun initView(rootView: View?) {
        val livBetTab = rootView?.findViewById<TabLayout>(R.id.livBetTab)
        if (livBetTab != null) {
            livBetTab.removeAllTabs()
            livBetTab.addTab(livBetTab.newTab().setText("露珠走势"))
            livBetTab.addTab(livBetTab.newTab().setText("历史开奖"))
            livBetTab.addTab(livBetTab.newTab().setText("玩法规则"))
        }

        rootView?.findViewById<ImageView>(R.id.imgBetToolsBack)?.setOnClickListener {
            betListen?.invoke(1)
        }

    }


}