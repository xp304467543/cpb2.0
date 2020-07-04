package com.fenghuang.caipiaobao.ui.mine.children.report

import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.baselib.base.mvp.BaseMvpActivity
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.bet.BetFragment
import com.fenghuang.caipiaobao.ui.home.HomeFragment
import com.fenghuang.caipiaobao.ui.lottery.LotteryFragment
import com.fenghuang.caipiaobao.ui.mine.MineFragment
import com.fenghuang.caipiaobao.ui.moments.MomentsFragment
import kotlinx.android.synthetic.main.act_mine_report.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class MineReportAct : BaseMvpActivity<MineReportPresenter>() {

    override fun attachView() = mPresenter.attachView(this)

    override fun getPageTitle() = "团队统计"

    override fun attachPresenter() = MineReportPresenter()

    override fun isOverride() = false

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_mine_report

    private val mFragments = arrayListOf<BaseFragment>()

    override fun initContentView() {
        mFragments.add(ReportFragment1())
        mFragments.add(ReportFragment2())
        mFragments.add(ReportFragment3())
        mFragments.add(ReportFragment4())

        loadMultipleRootFragment(R.id.reportContainer, 0,
                mFragments[0], mFragments[1], mFragments[2], mFragments[3])
    }


    override fun initData() {
        mPresenter.getNew("0")
    }

    override fun initEvent() {
        tab1.setOnClickListener {
            setVisible(containerTop)
           setPageTitle("团队统计")
            mPresenter.getNew("0")
            showHideFragment(mFragments[0])
        }
        tab2.setOnClickListener {
            setVisible(containerTop)
            setPageTitle("会员报表")
            mPresenter.getNew("1")
            showHideFragment(mFragments[1])
        }
        tab3.setOnClickListener {
            setVisible(containerTop)
            setPageTitle("下级报表")
            mPresenter.getNew("2")
            showHideFragment(mFragments[2])
        }
        tab4.setOnClickListener {
            setGone(containerTop)
            setPageTitle("邀请")
            showHideFragment(mFragments[3])
        }
    }
}