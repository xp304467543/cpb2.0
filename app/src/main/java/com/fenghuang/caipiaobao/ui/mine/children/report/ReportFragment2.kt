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
class ReportFragment2 : BaseMvpFragment<ReportFragment2P>() {
    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ReportFragment2P()

    override fun getLayoutResID() = R.layout.fragment_report_2
}