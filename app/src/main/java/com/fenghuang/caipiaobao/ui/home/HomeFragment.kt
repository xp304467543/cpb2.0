package com.fenghuang.caipiaobao.ui.home

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.data.bean.BaseApiBean
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.adapter.*
import com.fenghuang.caipiaobao.ui.home.data.*
import com.fenghuang.caipiaobao.ui.home.live.children.LiveAdvanceFragment
import com.fenghuang.caipiaobao.ui.home.live.children.LiveAnchorFragment
import com.fenghuang.caipiaobao.ui.home.news.NewsFragment
import com.fenghuang.caipiaobao.ui.home.search.HomeSearchFragment
import com.fenghuang.caipiaobao.ui.login.LoginActivity
import com.fenghuang.caipiaobao.ui.login.data.RegisterSuccess
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryJumpToLive
import com.fenghuang.caipiaobao.ui.mine.children.MineAnchorRecruit
import com.fenghuang.caipiaobao.ui.mine.children.MineMessageCenterFragment
import com.fenghuang.caipiaobao.ui.mine.data.ChangeSkin
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.JsonUtils
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.dialog.RegisterSuccessDialog
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomGiftAdapter
import com.fenghuang.caipiaobao.widget.pagegridview.HomePageGridView
import com.google.android.material.tabs.TabLayout
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.pingerx.banner.BannerView
import com.pingerx.banner.holder.BannerHolderCreator
import com.pingerx.rxnetgo.rxcache.CacheMode
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.my_top_bar.*


/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/26- 14:47
 * @ Describe
 *
 */

class HomeFragment : BaseMvpFragment<HomePresenter>() {

    //新消息
    var msg1 = ""
    var msg2 = ""
    var msg3 = ""

    //彩票直播数据
    var homeHotLiveResponse = arrayListOf<HomeTypeListResponse>()

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = HomePresenter()

    override fun isShowTitleRightLogo() = true

    override fun isShowTitleLeftLogo() = true

    override fun isShowTitleRightSecondLogo() = true

    override fun isShowBackIcon() = false

    override fun isRegisterRxBus() = true


    override fun getLayoutResID() = R.layout.fragment_home


    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
        if (UserInfoSp.getIsLogin()) {
            ImageManager.loadImg(UserInfoSp.getUserPhoto(), ivTitleLeft)
            mPresenter.getNewMsg()
        } else ImageManager.loadImg("", ivTitleLeft)
        homeHotLiveResponse.clear()
        mPresenter.getAllData(CacheMode.NONE)
    }

    override fun initContentView() {
        setStatusBarHeight(stateView)
        tvTitle.text = getString(R.string.app_name)
        setVisible(ivTitleLeft)
        setVisible(ivTitleRight)
        setVisible(ivTitleRightSecond)
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
        ImageManager.loadImg(UserInfoSp.getUserPhoto(), ivTitleLeft)
        ivTitleRight.setBackgroundResource(R.mipmap.ic_home_top_notice)
        ivTitleRightSecond.setBackgroundResource(R.mipmap.ic_search)
        homeSmartRefreshLayout.setOnRefreshListener {
            homeHotLiveResponse.clear()
            mPresenter.getAllData(CacheMode.NONE)
        }
        //加载默认视图
        initBaseView()
        //皮肤
        if (UserInfoSp.getSkinSelect() != 1) {
            skin2()
        }
    }

    private fun initBaseView() {
        upDateBanner(arrayListOf())
        upDateSystemNotice(arrayListOf())
        upDateHotLive()
        livePreView()
        news()
        anchorRecommend()
        expertRed()
    }


    override fun initEvent() {
        ivTitleLeft.setOnClickListener {
            if (UserInfoSp.getIsLogin()) {
                RxBus.get().post(HomeJumpToMine(true))
            } else LaunchUtils.startActivity(getPageActivity(), LoginActivity::class.java)
        }
        //咨询
        tvNewsMore.setOnClickListener {
            LaunchUtils.startFragment(getPageActivity(), NewsFragment())
        }

        ivTitleRight.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                ivTitleRight.setBackgroundResource(R.mipmap.ic_home_top_notice)
                LaunchUtils.startFragment(requireActivity(), MineMessageCenterFragment.newInstance(msg1, msg2, msg3))
            }
        }

        ivTitleRightSecond.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                LaunchUtils.startFragment(requireActivity(), HomeSearchFragment())
            }
        }

        tvPreMore.setOnClickListener {
            LaunchUtils.startFragment(context, LiveAdvanceFragment())
        }
        tvAnchorMore.setOnClickListener {
            LaunchUtils.startFragment(context, LiveAnchorFragment())
        }
        //首页AD
        imgAd.setOnClickListener {
            LaunchUtils.startFragment(getPageActivity(), MineAnchorRecruit())
        }
    }


    override fun initData() {
        mPresenter.getAllData(CacheMode.NONE)
    }


    // ========= banner =========
    fun upDateBanner(data: List<HomeBannerResponse>) {
        val result: List<HomeBannerResponse>
        if (data.isEmpty()) {
            result = ArrayList()
            for (index in 1..3) {
                result.add(HomeBannerResponse())
            }
        } else result = data
        val mBannerView = findView<BannerView<HomeBannerResponse>>(R.id.mHomeBannerViews)
        mBannerView.setPages(result, object : BannerHolderCreator<HomeBannerResponse, HomeBannerHolder> {
            override fun onCreateBannerHolder(): HomeBannerHolder {
                return HomeBannerHolder(mBannerView.getPageMode() == BannerView.PageMode.FAR, getPageActivity())
            }
        })
    }

    // ========= 公告 =========
    fun upDateSystemNotice(data: List<HomeSystemNoticeResponse>) {
        val sb = StringBuffer()
        if (data.isNotEmpty()) {
            data.forEachIndexed { index, value ->
                val s = (index + 1).toString() + "." + value.content + "        "
                sb.append(s)
            }
        } else sb.append("暂无公告。")
        tvNoticeMassages.setText(sb.toString())
        tvNoticeMassages.setTextColor(getColor(R.color.color_notice_message))
    }

    // ========= 彩种 --- 彩票 =========
    private var viewList = arrayListOf<HomePageGridView>()
    fun upDateLotteryType(json: BaseApiBean) {
        tabLottery.removeAllTabs()
        vpLottery.removeAllViews()
        viewList.clear()
        val typeList = json.typeList?.asJsonObject
        val data = json.data?.asJsonObject
        val type = arrayListOf<String>()
        val content = ArrayList<List<HomeTypeListResponse>>()
        if (!typeList?.isJsonNull!!) {
            for (entry in typeList.entrySet()!!) {
                type.add(entry.value.asString)
                val res = data?.get(entry.key)?.asJsonArray
                val bean = arrayListOf<HomeTypeListResponse>()
                for (op in res!!) {
                    val beanData = JsonUtils.fromJson(op, HomeTypeListResponse::class.java)
                    bean.add(beanData)
                    if (beanData.type == "1") {
                        homeHotLiveResponse.add(beanData)
                    }
                }
                content.add(bean)
            }
        }
        if (!type.isNullOrEmpty() && !content.isNullOrEmpty()) {
            for ((index, tabText) in type.withIndex()) {
                tabLottery.addTab(tabLottery.newTab().setText(tabText))
                val view = HomePageGridView(context)
                view.setData(content[index])
                view.setOnItemClickListener { _, HomeTypeListResponse ->
                    if (HomeTypeListResponse.anchor_id == null) {
                        ToastUtils.show("此彩种暂无直播间")
                        return@setOnItemClickListener
                    }
                    LaunchUtils.startLive(getPageActivity(), HomeTypeListResponse.anchor_id!!, HomeTypeListResponse.live_status!!,
                            HomeTypeListResponse.name!!, HomeTypeListResponse.image!!, HomeTypeListResponse.live_intro!!,
                            HomeTypeListResponse.online!!)
                }
                viewList.add(view)
            }
            val pagerAdapter = BottomGiftAdapter(viewList)
            vpLottery?.adapter = pagerAdapter
            vpLottery?.offscreenPageLimit = 10
            vpLottery?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    if (tabLottery.getTabAt(position) != null) tabLottery.getTabAt(position)!!.select()
                }

            })
            tabLottery.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {}
                override fun onTabUnselected(p0: TabLayout.Tab?) {}
                override fun onTabSelected(p0: TabLayout.Tab?) {
                    vpLottery?.currentItem = type.indexOf(p0?.text.toString())
                }

            })
        }

    }


    // ========= 热门推荐 =========
    var hotLiveAdapter: HomeHotLiveAdapter? = null

    private fun upDateHotLive() {
        val it: List<HomeHotLiveResponse>
        it = ArrayList()
        for (index in 1..6) {
            it.add(HomeHotLiveResponse(name = "加载中...", nickname = "加载中...", live_intro = "加载中...", online = 0, red_paper_num = 0, daxiu = false))
        }
        hotLiveAdapter = HomeHotLiveAdapter(getPageActivity())
        val gridLayoutManager = object : GridLayoutManager(context, 2) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        hotLiveAdapter?.addAll(it)
        rvHotLive.adapter = hotLiveAdapter
        rvHotLive.layoutManager = gridLayoutManager

    }

    // ========= 直播预告 =========
    var preLiveAdapter: HomeLivePreviewAdapter? = null

    private fun livePreView() {
        val it: List<HomeLivePreResponse>
        it = ArrayList()
        for (index in 1..6) {
            it.add(HomeLivePreResponse(nickname = "加载中..", name = "加载中..", starttime = "0", endtime = "0", livestatus = "0", isFollow = false))
        }
        rvLivePreview.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.HORIZONTAL, false)
        preLiveAdapter = HomeLivePreviewAdapter(getPageActivity())
        rvLivePreview.adapter = preLiveAdapter
        preLiveAdapter!!.addAll(it)
    }

    // ========= 最新资讯 =========
    var newsAdapter: HomeNewsAdapter? = null

    private fun news() {
        val it: List<HomeNewsResponse>
        it = ArrayList()
        for (index in 1..3) {
            it.add(HomeNewsResponse(title = "加载中...", timegap = "加载中..."))
        }
        rvNews.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        newsAdapter = HomeNewsAdapter(getPageActivity())
        rvNews.adapter = newsAdapter
        newsAdapter!!.addAll(it)
    }

    // ========= 主播推荐 =========
    var anchorRecommendAdapter: HomeHotLiveAdapter? = null

    private fun anchorRecommend() {
        val it: List<HomeHotLiveResponse>
        it = ArrayList()
        for (index in 1..6) {
            it.add(HomeHotLiveResponse(name = "加载中...", nickname = "加载中...", live_intro = "加载中...", online = 0, red_paper_num = 0,daxiu = false))
        }
        anchorRecommendAdapter = HomeHotLiveAdapter(getPageActivity())
        val gridLayoutManager = object : GridLayoutManager(context, 2) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        anchorRecommendAdapter?.addAll(it)
        rvAnchorRecommend.adapter = anchorRecommendAdapter
        rvAnchorRecommend.layoutManager = gridLayoutManager
    }

    // ========= 专家红单 =========
    var expertHotAdapter: ExpertHotAdapter? = null

    private fun expertRed() {
        val it: List<HomeExpertList>
        it = ArrayList()
        for (index in 1..4) {
            it.add(HomeExpertList(nickname = "加载中...", win_rate = "加载中...", profit_rate = "0", winning = "加载中...", lottery_name = "加载中...", last_10_games = null))
        }
        expertHotAdapter = ExpertHotAdapter(getPageActivity())
        rvExpertHot.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        rvExpertHot.adapter = expertHotAdapter
        expertHotAdapter?.addAll(it)

    }


    //登录成功dialog
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun LoginInfoResponse(eventBean: RegisterSuccess) {
        if (eventBean.isShowDialog) {
            RegisterSuccessDialog(requireActivity()).show()
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun LoginOut(eventBean: LoginOut) {
        ImageManager.loadBitmap(ivTitleLeft, R.mipmap.ic_base_user)
        mPresenter.upDatePreView()
    }

    //更新用户头像
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataUserAvatar(eventBean: UpDateUserPhoto) {
        ImageManager.loadImg(eventBean.img, ivTitleLeft)
    }

    //更新预告
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataUserAvatar(eventBean: UpDatePreView) {
        mPresenter.upDatePreView()
    }



    //开奖小视频
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryTypeSelect(eventBean: LotteryJumpToLive) {
        if (homeHotLiveResponse.isNotEmpty()) {
            for (data in homeHotLiveResponse) {
                if (eventBean.lotteryId == data.lottery_id) {
                    if (data.anchor_id == null) {
                        ToastUtils.show("此彩种暂无直播间")
                        return
                    }
                    LaunchUtils.startLive(getPageActivity(), data.anchor_id!!, data.live_status!!, data.name!!, "", "", data.online!!)
                }
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
            else -> {}
        }

    }

    //默认
    private fun skin() {
        setGone(imgSkin)
        tvTitle.setTextColor(getColor(R.color.black))
        imgSkin.background = getDrawable(R.mipmap.ic_mine_top)
        ivTitleRightSecond.background = getDrawable(R.mipmap.ic_search)
        ivTitleRight.background = getDrawable(R.mipmap.ic_home_top_notice)
        tvHot.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        tvPre.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        tvNews.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        tuiJi.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        expertHot.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        setVisible(rtv1)
        setVisible(rtv2)
        setVisible(rtv3)
        setVisible(rtv4)
        setVisible(rtv5)
    }

    private fun skin2() {
        setVisible(imgSkin)
        tvTitle.setTextColor(getColor(R.color.white))
        imgSkin.background = getDrawable(R.mipmap.ic_skin_new_year)
        ivTitleRightSecond.background = getDrawable(R.mipmap.search_white)
        ivTitleRight.background = getDrawable(R.mipmap.ic_home_top_notice_white)
        tvHot.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.ic_skin_home_1), null, null, null)
        tvPre.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.ic_skin_home_2), null, null, null)
        tvNews.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.ic_skin_home_3), null, null, null)
        tuiJi.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.ic_skin_home_4), null, null, null)
        expertHot.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.ic_skin_home_5), null, null, null)
        setGone(rtv1)
        setGone(rtv2)
        setGone(rtv3)
        setGone(rtv4)
        setGone(rtv5)
    }

}