package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryPlayListResponse
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryReset
import com.fenghuang.caipiaobao.ui.lottery.data.PlayUnitData
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.google.android.material.tabs.TabLayout
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.fragment_guess_play.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/4/22
 * @ Describe 投注列表
 *
 */

class LiveRoomBetGuessFragment : BaseNormalFragment() {

    private var play: LotteryPlayListResponse? = null

    override fun getLayoutRes() = R.layout.fragment_guess_play

    companion object {
        fun newInstance(play: LotteryPlayListResponse) = LiveRoomBetGuessFragment().apply {
            arguments = Bundle(1).apply {
                putParcelable("play", play)
            }
        }
    }


    override fun initView(rootView: View?) {
    }

    override fun initData() {
        arguments?.let {
            play = it.getParcelable("play")
        }
        play?.let {
            val fragment = arrayListOf<LiveRoomBetGuessFragment1>()
            for ( res in it.play_unit_data){
                fragment.add(LiveRoomBetGuessFragment1.newInstance(res))
            }
            val adapter = GuessPlayChildAdapter(childFragmentManager, it.play_unit_data)
            live_bet_child_viewpager.adapter = adapter
            live_bet_child_tabLayout.setupWithViewPager(live_bet_child_viewpager)
            live_bet_child_viewpager.offscreenPageLimit = it.play_unit_data.size
            for (i in it.play_unit_data.indices) {
                val tab = live_bet_child_tabLayout.getTabAt(i)
                if (tab != null) {
                    tab.setCustomView(R.layout.item_guess_tab)
                    if (tab.customView != null) {
                        val textView = tab.customView?.findViewById(R.id.tab_text) as TextView
                        textView.text = it.play_unit_data[i].play_sec_cname//设置tab上的文字
                        if (i == 0) {
                            textView.setBackgroundResource(R.drawable.guess_tab_background)
                            textView.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                            textView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                        } else {
                            textView.setBackgroundResource(R.drawable.guess_tab_background1)
                            textView.setTextColor(ViewUtils.getColor(R.color.color_333333))
                            textView.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                        }
                    }
                }
            }
            live_bet_child_tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    live_bet_child_viewpager.currentItem = tab.position
                    if (tab.customView != null) {
                        val textView = tab.customView?.findViewById(R.id.tab_text) as TextView
                        if (textView.text == "一中一" || textView.text == "一中二" || textView.text == "一中三" || textView.text == "一中四" || textView.text == "一中五"
                                || textView.text == "二中二" || textView.text == "三中三") {
                            RxBus.get().post(LotteryReset(true))
                        }
                        textView.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                        textView.setBackgroundResource(R.drawable.guess_tab_background)
                        textView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    if (tab.customView != null) {
                        val textView = tab.customView?.findViewById(R.id.tab_text) as TextView
                        textView.setBackgroundResource(R.drawable.guess_tab_background1)
                        textView.setTextColor(ViewUtils.getColor(R.color.color_333333))
                        textView.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }
            })
        }
    }

    inner class GuessPlayChildAdapter(fm: FragmentManager, private val play_unit_data: List<PlayUnitData>) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment? {
            return LiveRoomBetGuessFragment1.newInstance(play_unit_data[position])
        }

        override fun getCount(): Int {
            return play_unit_data.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return play_unit_data[position].play_sec_cname
        }
    }

}