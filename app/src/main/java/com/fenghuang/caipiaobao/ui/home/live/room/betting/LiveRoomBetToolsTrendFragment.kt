package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter.ViewPagerAdapter
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.widget.XViewPager

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe 露珠走势
 *
 */

class LiveRoomBetToolsTrendFragment : BaseNormalFragment() {

    private var livBetTypeTab: TabLayout? = null

    private var vpLiveBet: XViewPager? = null

    private var pagerAdapter: ViewPagerAdapter? = null

    private var liveRoomBetToolsTrendFragment1: LiveRoomBetToolsTrendFragment1? = null

    private var liveRoomBetToolsTrendFragment2: LiveRoomBetToolsTrendFragment2? = null

    override fun getLayoutRes() = R.layout.fragment_live_bet_tool_trend

    override fun initView(rootView: View?) {
        livBetTypeTab = rootView?.findViewById(R.id.livBetTypeTab)
        vpLiveBet = rootView?.findViewById(R.id.vp_live_bet)
        if (livBetTypeTab != null) {
            livBetTypeTab?.newTab()?.setText("露珠")?.let { livBetTypeTab?.addTab(it) }
            livBetTypeTab?.newTab()?.setText("走势")?.let { livBetTypeTab?.addTab(it) }
        }
        vpLiveBet?.removeAllViews()
        liveRoomBetToolsTrendFragment1 = LiveRoomBetToolsTrendFragment1.newInstance(arguments?.getString("lotteryId")
                ?: "1")
        liveRoomBetToolsTrendFragment2 = LiveRoomBetToolsTrendFragment2.newInstance(arguments?.getString("lotteryId")
                ?: "1")
        val fragments: ArrayList<Fragment> = arrayListOf(
                liveRoomBetToolsTrendFragment1!!,
                liveRoomBetToolsTrendFragment2!!
        )
        pagerAdapter = ViewPagerAdapter(childFragmentManager, fragments)
        vpLiveBet?.adapter = pagerAdapter
        vpLiveBet?.currentItem = 0
        vpLiveBet?.offscreenPageLimit = fragments.size

        vpLiveBet?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (livBetTypeTab?.getTabAt(position) != null) livBetTypeTab?.getTabAt(position)!!.select()
            }

        })
        livBetTypeTab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                vpLiveBet?.currentItem = p0?.position ?: 0
            }

        })
    }

    companion object {
        fun newInstance(anchorId: String): LiveRoomBetToolsTrendFragment {
            val fragment = LiveRoomBetToolsTrendFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", anchorId)
            fragment.arguments = bundle
            return fragment
        }
    }

}