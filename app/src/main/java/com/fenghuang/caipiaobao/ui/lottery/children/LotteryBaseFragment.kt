package com.fenghuang.caipiaobao.ui.lottery.children

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.fenghuang.baselib.base.adapter.BaseFragmentPageAdapter
import com.fenghuang.baselib.base.fragment.BaseContentFragment
import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryCodeHistoryResponse
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryExpertPlay
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryNewCode
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryTypeSelect
import com.fenghuang.caipiaobao.ui.mine.data.ChangeSkin
import com.google.android.material.tabs.TabLayout
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.child_fragment_lu_zhu.*
import kotlinx.android.synthetic.main.child_lottery_base.*
import kotlinx.android.synthetic.main.fragment_moments.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/2- 16:28
 * @ Describe
 *
 */

class LotteryBaseFragment : BaseContentFragment() {


    override fun getContentResID() = R.layout.child_lottery_base

    override fun isRegisterRxBus() = true

    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }


    private var lotteryHistoryOpenFragment: LotteryHistoryOpenFragment? = null
    var lotteryLuZhuFragment: LotteryLuZhuFragment? = null
    var lotteryTrendFragment: LotteryTrendFragment? = null
    var lotteryExpertPlanFragment: LotteryExpertPlanFragment? = null


    override fun initContentView() {
        when (arguments?.getString("lotteryId")!!) {
            "-999" -> {
            }
            else -> initView(arguments?.getString("lotteryId")!!)
        }
        //皮肤
        when (UserInfoSp.getSkinSelect()) {
            1 -> {
                lotteryTab.setSelectedTabIndicatorColor(ViewUtils.getColor(R.color.text_red))
            }
            2 -> {
                lotteryTab.setSelectedTabIndicatorColor(ViewUtils.getColor(R.color.text_red))
            }
            3 -> {
                lotteryTab.setSelectedTabIndicatorColor(ViewUtils.getColor(R.color.alivc_green))
            }
        }
    }

    private fun initView(lotteryId: String) {
        xViewPage?.removeAllViews()
        val adapter: BaseFragmentPageAdapter
        val fragments: ArrayList<BaseFragment>
        when (lotteryId) {
            "8" -> {
                lotteryHistoryOpenFragment = LotteryHistoryOpenFragment.newInstance(lotteryId)
                lotteryLuZhuFragment = LotteryLuZhuFragment.newInstance(lotteryId)
                lotteryExpertPlanFragment = LotteryExpertPlanFragment.newInstance(lotteryId)
                //初始化Tab
                if (lotteryTab != null) {
                    lotteryTab.removeAllTabs()
                    lotteryTab.addTab(lotteryTab.newTab().setText("历史开奖"))
                    lotteryTab.addTab(lotteryTab.newTab().setText("路珠"))
                    lotteryTab.addTab(lotteryTab.newTab().setText("专家计划"))
                }
                //初始化viewPager
                fragments = arrayListOf(
                        lotteryHistoryOpenFragment!!,
                        lotteryLuZhuFragment!!,
                        lotteryExpertPlanFragment!!
                )
                adapter = BaseFragmentPageAdapter(childFragmentManager, fragments)

            }
            else -> {
                lotteryHistoryOpenFragment = LotteryHistoryOpenFragment.newInstance(lotteryId)
                lotteryLuZhuFragment = LotteryLuZhuFragment.newInstance(lotteryId)
                lotteryTrendFragment = LotteryTrendFragment.newInstance(lotteryId)
                lotteryExpertPlanFragment = LotteryExpertPlanFragment.newInstance(lotteryId)
                //初始化Tab
                if (lotteryTab != null) {
                    lotteryTab.removeAllTabs()
                    lotteryTab.addTab(lotteryTab.newTab().setText("历史开奖"))
                    lotteryTab.addTab(lotteryTab.newTab().setText("路珠"))
                    lotteryTab.addTab(lotteryTab.newTab().setText("走势"))
                    lotteryTab.addTab(lotteryTab.newTab().setText("专家计划"))
                }
                //初始化viewPager
                fragments = arrayListOf(
                        lotteryHistoryOpenFragment!!,
                        lotteryLuZhuFragment!!,
                        lotteryTrendFragment!!,
                        lotteryExpertPlanFragment!!
                )
                adapter = BaseFragmentPageAdapter(childFragmentManager, fragments)
            }
        }
        xViewPage?.adapter = adapter
        xViewPage?.currentItem = 0
        xViewPage?.offscreenPageLimit = fragments.size
        //ViewPage监听
        xViewPage?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (lotteryTab.getTabAt(position) != null) lotteryTab.getTabAt(position)!!.select()
            }
        })
        //TabLayout监听
        if (lotteryTab != null) {
            lotteryTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                }

                override fun onTabSelected(p0: TabLayout.Tab?) {
                    xViewPage?.currentItem = p0?.position!!
                }
            })
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun onLotteryTypeSelect(eventBean: LotteryTypeSelect) {
        if (eventBean.lotteryId != null && eventBean.lotteryId != "") {
            initView(eventBean.lotteryId!!)
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun onLotteryTypeSelect(eventBean: LotteryNewCode) {
        if (lotteryHistoryOpenFragment != null) {
            lotteryHistoryOpenFragment!!.addItem(LotteryCodeHistoryResponse(
                    issue = eventBean.lotteryCodeNewResponse!!.issue?:"0",
                    code = eventBean.lotteryCodeNewResponse!!.code?:"0",
                    input_time = eventBean.lotteryCodeNewResponse!!.input_time ?:"0"))
        }
        if (lotteryLuZhuFragment != null) lotteryLuZhuFragment!!.upDateLuZhu()
        if (lotteryTrendFragment != null) lotteryTrendFragment!!.getTypeTrendData()
        if (lotteryExpertPlanFragment != null) lotteryExpertPlanFragment!!.getExpertPlan(eventBean.lotteryCodeNewResponse?.lottery_id!!,
                eventBean.lotteryCodeNewResponse?.issue!!)
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun onLotteryExpertPlan(eventBean: LotteryExpertPlay) {
        if (lotteryExpertPlanFragment != null) lotteryExpertPlanFragment!!.getExpertPlan(eventBean.lotteryCodeNewResponse?.lottery_id!!,
                eventBean.lotteryCodeNewResponse?.issue!!)
    }


    companion object {
        fun newInstance(anchorId: String, issue: String): LotteryBaseFragment {
            val fragment = LotteryBaseFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", anchorId)
            bundle.putString("issue", issue)
            fragment.arguments = bundle
            return fragment
        }
    }

    //换肤
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        when (eventBean.id) {

            1 -> {
                lotteryTab.setSelectedTabIndicatorColor(ViewUtils.getColor(R.color.text_red))
            }
            2 -> {
                lotteryTab.setSelectedTabIndicatorColor(ViewUtils.getColor(R.color.text_red))
            }
            3 -> {
                lotteryTab.setSelectedTabIndicatorColor(ViewUtils.getColor(R.color.alivc_green))
            }
        }
        lotteryLuZhuFragment?.luZhuRecycleAdapter?.notifyDataSetChanged()
    }
}