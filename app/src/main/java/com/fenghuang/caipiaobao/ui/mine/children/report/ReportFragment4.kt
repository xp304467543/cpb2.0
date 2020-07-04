package com.fenghuang.caipiaobao.ui.mine.children.report

import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.fragment_report_4.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment4 : BaseMvpFragment<ReportFragment4P>() {

    var state = ""

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ReportFragment4P()

    override fun getLayoutResID() = R.layout.fragment_report_4

    override fun initData() {
        mPresenter.getCode()
    }

    override fun initEvent() {
        tvGetReportCode.setOnClickListener {
            when (state) {
                "10" -> {

                }
                "9" -> {

                }
                else -> {

                }
            }
        }
    }
}