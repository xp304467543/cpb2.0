package com.fenghuang.caipiaobao.ui.mine.children.report

import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.caipiaobao.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment3 : BaseMvpFragment<ReportFragment3P>() {
    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ReportFragment3P()

    override fun getLayoutResID() = R.layout.fragment_report_3
}