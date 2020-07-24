package com.fenghuang.caipiaobao.ui.mine.children.report

import android.content.Intent
import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.baselib.base.mvp.BaseMvpActivity
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.widget.dialog.SearchDialog
import kotlinx.android.synthetic.main.act_mine_report.*
import kotlinx.android.synthetic.main.my_top_bar.*

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
        setVisible(ivTitleRight)
        ivTitleRight.setBackgroundResource(R.mipmap.ic_search)
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
            tvTopTitle1.text = "会员人数"
            mPresenter.getNew("0")
            showHideFragment(mFragments[0])
        }
        tab2.setOnClickListener {
            setVisible(containerTop)
            setPageTitle("会员报表")
            tvTopTitle1.text = "会员人数"
            mPresenter.getNew("1")
            showHideFragment(mFragments[1])
            setVisible(ivTitleRight)

        }
        tab3.setOnClickListener {
            setVisible(containerTop)
            setPageTitle("会员下级报表")
            tvTopTitle1.text = "团队人数"
            mPresenter.getNew("2")
            showHideFragment(mFragments[2])
        }
        tab4.setOnClickListener {
            setGone(containerTop)
            setPageTitle("邀请")
            tvTopTitle1.text = "会员人数"
            showHideFragment(mFragments[3])
            setGone(ivTitleRight)
        }
        ivTitleRight.setOnClickListener {
            val dialog = SearchDialog(this)
            dialog.setConfirmClickListener {
                val intent = Intent(this, MineReportSearchAct::class.java)
                intent.putExtra("searchName", it)
                startActivity(intent)
                dialog.dismiss()
            }
            dialog.show()
        }
    }
}