package com.fenghuang.caipiaobao.ui.home.news

import androidx.viewpager.widget.ViewPager
import com.fenghuang.baselib.base.adapter.BaseFragmentPageAdapter
import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.caipiaobao.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.child_lottery_base.*
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_news.xViewPage

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-02
 * @ Describe
 *
 */

class NewsFragment : BaseMvpFragment<NewsFragmentPresenter>() {

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = NewsFragmentPresenter()

    override fun getContentResID() = R.layout.fragment_news

    override fun isShowBackIconWhite() = false

    override fun getPageTitle() = "最新资讯"

    override fun isSwipeBackEnable() = true

    override fun isOverridePage() = false

    override fun initContentView() {
        newsTab.addTab(newsTab.newTab().setText("综合"))
        newsTab.addTab(newsTab.newTab().setText("直播"))
        newsTab.addTab(newsTab.newTab().setText("活动"))
        newsTab.addTab(newsTab.newTab().setText("公告"))
        newsTab.addTab(newsTab.newTab().setText("咨询"))
        val list = arrayListOf<BaseFragment>(ChildNewsPublicFragment.newInstance(""),
                ChildNewsPublicFragment.newInstance("1"), ChildNewsPublicFragment.newInstance("2"),
                ChildNewsPublicFragment.newInstance("3"), ChildNewsPublicFragment.newInstance("4"))
        xViewPage.adapter = BaseFragmentPageAdapter(childFragmentManager, list)
        xViewPage.offscreenPageLimit = 5

    }

    override fun initEvent() {
        xViewPage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (newsTab.getTabAt(position) != null) newsTab.getTabAt(position)!!.select()
            }

        })
        newsTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                xViewPage.currentItem = p0?.position!!
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

        })
    }
}