package com.fenghuang.caipiaobao.ui.personal

import androidx.viewpager.widget.ViewPager
import com.fenghuang.baselib.base.adapter.BaseFragmentPageAdapter
import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.baselib.base.mvp.BaseMvpActivity
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserConstant
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.HomePresenter
import com.fenghuang.caipiaobao.ui.moments.childern.MomentsAnchorFragment
import com.fenghuang.caipiaobao.ui.personal.data.AnchorPageInfoBean
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_presonal_anchor.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-01
 * @ Describe 主播主页
 *
 */
class AnchorPersonalPage : BaseMvpActivity<AnchorPersonalPagePresenter>() {

    var attentionNum = 0

    override fun attachPresenter() = AnchorPersonalPagePresenter()

    override fun attachView() = mPresenter.attachView(this)

    override fun isSwipeBackEnable() = true

    override fun isOverride() = true

    override val layoutResID = R.layout.fragment_presonal_anchor

    override fun initContentView() {
        anchorPageTab.addTab(anchorPageTab.newTab().setText("资料"))
        anchorPageTab.addTab(anchorPageTab.newTab().setText("动态"))

    }

    override fun initData() {
        if (intent != null) {
            showPageLoadingDialog()
            mPresenter.getAnchorInfo(intent.getStringExtra(UserConstant.FOLLOW_ID)!!)
        }
    }

    fun initAnchor(data: AnchorPageInfoBean) {
        initViewPager(data)
        attentionNum = data.fans
        tvUserName.text = data.nickname
        tvAnchorAttention.text = data.follow_num.toString()
        tvAnchorFans.text = data.fans.toString()
        tvAnchorZan.text = data.zan.toString()
        tvUserDescription.text = data.sign
        if (data.sex == "1") {
            imgSex.background = ViewUtils.getDrawable(R.mipmap.ic_live_anchor_boy)
        } else imgSex.background = ViewUtils.getDrawable(R.mipmap.ic_live_anchor_girl)
        imgAge.text = data.age.toString()
        imgLevel.text = data.level
        ImageManager.loadImg(data.avatar, imgUserPhoto)
        if (data.liveStatus == "1") {
            circleWave.setInitialRadius(70f)
            circleWave.start()
        }
        if (data.isFollow) {
            btAttention.background = ViewUtils.getDrawable(R.drawable.button_grey_background)
            btAttention.setTextColor(ViewUtils.getColor(R.color.grey_e6))
            btAttention.text = "已关注"
        }
        hidePageLoadingDialog()
    }

    private fun initViewPager(data: AnchorPageInfoBean) {
        val list = arrayListOf<BaseFragment>(AnchorPersonalPageData(data), MomentsAnchorFragment(data.anchor_id, isChild = true))
        xViewPage.adapter = BaseFragmentPageAdapter(supportFragmentManager, list)

        anchorPageTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                xViewPage.currentItem = p0?.position!!
            }

        })
        xViewPage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                anchorPageTab.getTabAt(position)!!.select()
            }

        })

    }

    override fun initEvent() {
        btAttention.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(this, false)
                return@setOnClickListener
            }
            if (FastClickUtils.isFastClick()) {
                val presenter = HomePresenter()
                presenter.attention(intent.getStringExtra(UserConstant.FOLLOW_ID)!!, "")
                presenter.setSuccessClickListener {
                    if (!it) {
                        btAttention.background = ViewUtils.getDrawable(R.mipmap.ic_anchor_bt_bg)
                        btAttention.text = "+ 关注"
                        btAttention.setTextColor(ViewUtils.getColor(R.color.white))
                        if (attentionNum > 0) attentionNum -= 1
                        tvAnchorFans.text = attentionNum.toString()
                    } else {
                        btAttention.background = ViewUtils.getDrawable(R.drawable.button_grey_background)
                        btAttention.setTextColor(ViewUtils.getColor(R.color.grey_e6))
                        btAttention.text = "已关注"
                        attentionNum += 1
                        tvAnchorFans.text = attentionNum.toString()
                    }
                }
                presenter.setFailClickListener {
                    GlobalDialog.ShowError(this, it)
                }
            }
        }

        imgBack.setOnClickListener {
            finish()
        }
    }

}