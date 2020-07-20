package com.fenghuang.caipiaobao.ui.mine.children.report

import android.annotation.SuppressLint
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

    @SuppressLint("SetTextI18n")
    fun getCode() {
        getLevelList()
        MineApi.getCode {
            onSuccess {
                if (mView.isActive()) {
                    mView.state = it.status ?: "0"
                    when (it.status) {
                        "10" -> {
                            mView.tvGetReportCode.text = "去赚钱"
                            val n = it.invitee_num ?: 1.0
                            val x = it.next_level_invitee_num ?: 1.0
                            mView.pro_level.progress = ((n / x) * 100).toInt()
                            mView.tvStart.text = it.level_name
                            mView.tvEnd.text = it.next_level_name
                            mView.tvLevelContent.text = "当前邀请"+ it.invitee_num?.toInt()+"人，返佣比例"+(it.rebate?.times(100))+"%，距离下一等级还差"+it.next_level_diff+"人"
                            mView.initDialog(it.market_code.toString(),it.market_url.toString())
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

    private fun getLevelList(){
        MineApi.getLevelList {
            onSuccess {
                if (mView.isActive()){
                    mView.adapter?.clear()
                    mView.adapter?.addAll(it)
                }
            }
        }
    }
}