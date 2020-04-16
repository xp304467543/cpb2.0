package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.os.Bundle
import android.view.View
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.google.android.material.tabs.TabLayout

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe 规则
 *
 */

class LiveRoomBetToolsRulesFragment : BaseNormalFragment() {

    private var livBetTypeTab:TabLayout? = null

    override fun getLayoutRes() = R.layout.fragment_live_bet_tool_trend

    override fun initView(rootView: View?) {
        livBetTypeTab = rootView?.findViewById(R.id.livBetTypeTab)
        if (livBetTypeTab!=null){
            livBetTypeTab?.newTab()?.setText("规则")?.let { livBetTypeTab?.addTab(it) }
            livBetTypeTab?.newTab()?.setText("规则")?.let { livBetTypeTab?.addTab(it) }
        }
    }

    companion object {
        fun newInstance(anchorId: String): LiveRoomBetToolsRulesFragment {
            val fragment = LiveRoomBetToolsRulesFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", anchorId)
            fragment.arguments = bundle
            return fragment
        }
    }

}