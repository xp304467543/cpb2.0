package com.fenghuang.caipiaobao.ui.moments

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.fenghuang.baselib.base.adapter.BaseFragmentPageAdapter
import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.round.RoundTextView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.mine.data.ChangeSkin
import com.fenghuang.caipiaobao.ui.moments.childern.MomentsAnchorFragment
import com.fenghuang.caipiaobao.ui.moments.childern.MomentsHotDiscussFragment
import com.fenghuang.caipiaobao.ui.moments.childern.MomentsRecommendFragment
import com.fenghuang.caipiaobao.ui.moments.data.MomentsTopBannerResponse
import com.google.android.material.tabs.TabLayout
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.pingerx.banner.BannerView
import com.pingerx.banner.holder.BannerHolderCreator
import com.pingerx.banner.holder.BaseBannerHolder
import kotlinx.android.synthetic.main.fragment_moments.*
import kotlinx.android.synthetic.main.my_top_bar.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe 圈子
 *
 */

class MomentsFragment : BaseMvpFragment<MomentsPresenter>() {

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MomentsPresenter()

    override fun isRegisterRxBus() = true

    override fun isShowBackIcon() = false

    override fun isOverridePage() = false


    override fun getLayoutResID() = R.layout.fragment_moments

    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }

    override fun initContentView() {
        tvTitle.text = "圈子"
        setStatusBarHeight(stateViewMoment)
        //皮肤
        when (UserInfoSp.getSkinSelect()) {
            1 -> {
                skin()
            }
            2 -> {
                skin2()
            }
            3 -> {
                skin3()
            }
        }
        momentsTab.addTab(momentsTab.newTab().setText("热门讨论"))
        momentsTab.addTab(momentsTab.newTab().setText("主播"))
        val tab = momentsTab.newTab()
        tab.setCustomView(R.layout.tab_red)
        val redBall = tab.customView?.findViewById<RoundTextView>(R.id.iv_tab_red)
        val textTab = tab.customView?.findViewById<TextView>(R.id.tv_tab_title)
        textTab?.text = "精品推荐"
        tab.text = "精品推荐"
        momentsTab.addTab(tab)
        if (!UserInfoSp.getIsShowRed()) setGone(redBall)
        xViewPageMoments.adapter = BaseFragmentPageAdapter(childFragmentManager, arrayListOf<BaseFragment>(
                MomentsHotDiscussFragment(),
                MomentsAnchorFragment.newInstance(""),
                MomentsRecommendFragment()
        ))
        xViewPageMoments.currentItem = 0
        xViewPageMoments.offscreenPageLimit = 3
        mPresenter.getMomentsData()

        //ViewPage监听
        xViewPageMoments.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (momentsTab.getTabAt(position) != null) momentsTab.getTabAt(position)!!.select()
            }
        })
        //TabLayout监听
        momentsTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                textTab?.setTextColor(ViewUtils.getColor(R.color.color_999999))
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0?.text) {

                    "热门讨论" -> xViewPageMoments.currentItem = 0
                    "主播" -> xViewPageMoments.currentItem = 1
                    "精品推荐" -> {
                        textTab?.setTextColor(ViewUtils.getColor(R.color.black))
                        xViewPageMoments.currentItem = 2
                        if (UserInfoSp.getIsShowRed()) {
                            UserInfoSp.putIsShowRed(false)
                            setGone(p0.customView?.findViewById<RoundTextView>(R.id.iv_tab_red))
                        }
                    }
                }
            }
        })
    }


    // ========= banner =========
    fun upDateBanner(data: List<MomentsTopBannerResponse>) {
        val mBannerView = findView<BannerView<MomentsTopBannerResponse>>(R.id.mMomentsBannerViews)
        mBannerView.setPages(data, object : BannerHolderCreator<MomentsTopBannerResponse, MomentsBannerHolder> {
            override fun onCreateBannerHolder(): MomentsBannerHolder {
                return MomentsBannerHolder(getPageActivity())
            }
        })
    }

    inner class MomentsBannerHolder(val context: Context) : BaseBannerHolder<MomentsTopBannerResponse> {
        override fun getHolderResId(): Int {
            return R.layout.holder_banner
        }

        override fun onBindData(itemView: View, data: MomentsTopBannerResponse) {
            if (TextUtils.isEmpty(data.img)) {
                itemView.findViewById<ImageView>(R.id.imageView).setImageResource(R.mipmap.ic_placeholder)
            } else {
                ImageManager.loadImg(data.img, itemView.findViewById(R.id.imageView))
            }
        }

        override fun onPageClick(itemView: View, position: Int, data: MomentsTopBannerResponse) {
            if (data.url != "") {
//            LaunchUtils.startFragment(context, HomeBannerJump(data.url, data.title))
            }
        }
    }


    //换肤
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        when (eventBean.id) {
            1 -> {
                skin()
            }
            2 -> {
                skin2()

            }
            3 -> {
                skin3()
            }
        }

    }

    private fun skin() {
        setGone(skinMoment)
        tvTitle.setTextColor(getColor(R.color.black))
        skinMoment.background = getDrawable(R.color.white)
    }

    private fun skin2() {
        setVisible(skinMoment)
        momentsTab.setSelectedTabIndicatorColor(ViewUtils.getColor(R.color.text_red))
        tvTitle.setTextColor(getColor(R.color.white))
        skinMoment.background = getDrawable(R.mipmap.ic_skin_new_year)
    }

    private fun skin3() {
        setVisible(skinMoment)
        momentsTab.setSelectedTabIndicatorColor(ViewUtils.getColor(R.color.alivc_green))
        tvTitle.setTextColor(getColor(R.color.white))
        skinMoment.background = getDrawable(R.mipmap.ic_skin_d5)
    }
}