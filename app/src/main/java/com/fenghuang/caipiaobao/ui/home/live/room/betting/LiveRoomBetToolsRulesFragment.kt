package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.os.Bundle
import android.view.View
import android.widget.TabHost
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter.ViewPagerAdapter
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.widget.XViewPager

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe 规则 玩法
 *
 */

class LiveRoomBetToolsRulesFragment : BaseNormalFragment() {

    private var livBetTypeTab: TabLayout? = null

    private var vpLiveBet: XViewPager? = null

    private var pagerAdapter: ViewPagerAdapter? = null

    override fun getLayoutRes() = R.layout.fragment_live_bet_tool_trend

    override fun initView(rootView: View?) {
        livBetTypeTab = rootView?.findViewById(R.id.livBetTypeTab)
        vpLiveBet = rootView?.findViewById(R.id.vp_live_bet)
    }

    override fun initData() {
        val result = LotteryApi.getLotteryBetRule()
        result.onSuccess { op ->
            if (livBetTypeTab != null) {
                val fragments: ArrayList<Fragment> = arrayListOf()
                fragments.clear()
                for ((index,res) in op.withIndex()) {
                    livBetTypeTab?.newTab()?.setText(res.play_rule_type_name)?.let { livBetTypeTab?.addTab(it) }
                    fragments.add(LiveRoomBetToolsRulesChildFragment.newInstance(op,index))
                }
                pagerAdapter = ViewPagerAdapter(childFragmentManager, fragments)
                vpLiveBet?.adapter = pagerAdapter
                livBetTypeTab?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                    override fun onTabReselected(p0: TabLayout.Tab?) {}
                    override fun onTabUnselected(p0: TabLayout.Tab?) {}
                    override fun onTabSelected(p0: TabLayout.Tab?) {
                        vpLiveBet?.currentItem = p0?.position ?: 0
                    }
                })
                vpLiveBet?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {}
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageSelected(position: Int) {
                        if (livBetTypeTab?.getTabAt(position) != null) livBetTypeTab?.getTabAt(position)!!.select()
                    }

                })
            }
        }
        result.onFailed {
            ToastUtils.showError(it.getMsg())
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