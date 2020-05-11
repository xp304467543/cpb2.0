package com.fenghuang.caipiaobao.ui.mine.children

import androidx.viewpager.widget.ViewPager
import com.fenghuang.baselib.base.activity.BaseNavActivity
import com.fenghuang.baselib.base.adapter.BaseFragmentPageAdapter
import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.baselib.base.fragment.BaseNavFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.data.HomeJumpToMine
import com.fenghuang.caipiaobao.ui.home.data.JumpToBuyLottery
import com.google.android.material.tabs.TabLayout
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_attention.*
import kotlinx.android.synthetic.main.fragment_main.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-04
 * @ Describe 我的关注
 *
 */

class MineAttentionFragment : BaseNavActivity() {


    override fun getContentResID() = R.layout.fragment_attention


    override fun isSwipeBackEnable() = true

    override fun getPageTitle() = "我的关注"

    override fun isShowBackIconWhite() = false

    override fun isRegisterRxBus() = true




    override fun initContentView() {
        attentionTab.addTab(attentionTab.newTab().setText("主播"))
        attentionTab.addTab(attentionTab.newTab().setText("用户"))
        attentionTab.addTab(attentionTab.newTab().setText("专家"))
        val fragmentList = arrayListOf<BaseFragment>(MineAttentionFragmentChild(1),MineAttentionFragmentChild(2),
                MineAttentionFragmentChild(3))
        xViewPageAttention.offscreenPageLimit = 3
        xViewPageAttention.adapter = BaseFragmentPageAdapter(supportFragmentManager,fragmentList)
    }

    override fun initEvent() {
        attentionTab.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                xViewPageAttention.currentItem = p0?.position!!
            }

        })
        xViewPageAttention.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (attentionTab.getTabAt(position) != null) attentionTab.getTabAt(position)!!.select()
            }

        })
    }

    /**
     * 跳转购彩
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun jumpToBuyLottery(eventBean: JumpToBuyLottery) {
        finish()
    }

    /**
     * 跳转mine
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun onClickMine(clickMine: HomeJumpToMine) {
            finish()
    }
}