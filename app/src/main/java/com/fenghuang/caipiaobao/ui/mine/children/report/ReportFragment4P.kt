package com.fenghuang.caipiaobao.ui.mine.children.report

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import kotlinx.android.synthetic.main.fragment_report_4.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment4P : BaseMvpPresenter<ReportFragment4>() {

    fun getCode() {
        MineApi.getCode {
            onSuccess {
                if (mView.isActive()) {
                    mView.state = it.status?:"0"
                    when (it.status) {
                        "10" -> {
                            mView.tvGetReportCode.text = "去赚钱"
                        }
                        "9" -> {
                            mView.tvGetReportCode.text = "审核中"
                        }
                        else -> {
                            mView.tvGetReportCode.text = "申请推广员"
                        }
                    }
                }
            }
            onFailed { ToastUtils.show("获取数据失败") }
        }
    }
}