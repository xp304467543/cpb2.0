package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter.ViewPagerAdapter
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.widget.XViewPager

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe  投注记录
 *
 */

class LiveRoomBetRecordFragment : BottomDialogFragment() {

    private var livBetTabRecord: TabLayout? = null

    private var vpRecord: XViewPager? = null

    private var pagerAdapter: ViewPagerAdapter? = null

    override val layoutResId: Int = R.layout.fragment_live_bet_record

    override val resetHeight: Int = 0

    override fun isShowTop(): Boolean = false

    override fun canceledOnTouchOutside(): Boolean = true

    override fun initView() {
        rootView?.findViewById<ImageView>(R.id.imgBetRecordBack)?.setOnClickListener {
            dismiss()
        }
        vpRecord = rootView?.findViewById(R.id.vpRecord)
        livBetTabRecord = rootView?.findViewById(R.id.livBetTabRecord)
        livBetTabRecord?.newTab()?.setText("未结算")?.let { livBetTabRecord?.addTab(it) }
        livBetTabRecord?.newTab()?.setText("已结算")?.let { livBetTabRecord?.addTab(it) }
        val fragments = arrayListOf<Fragment>(LiveRoomBetRecordFragment1(), LiveRoomBetRecordFragment2())
        pagerAdapter = ViewPagerAdapter(childFragmentManager, fragments)
        vpRecord?.adapter = pagerAdapter
        vpRecord?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (livBetTabRecord?.getTabAt(position) != null) livBetTabRecord?.getTabAt(position)!!.select()
            }
        })
        livBetTabRecord?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                vpRecord?.currentItem = p0?.position ?: 0
            }

        })

    }

    override fun initData() {
    }

    override fun initFragment() {
    }
}