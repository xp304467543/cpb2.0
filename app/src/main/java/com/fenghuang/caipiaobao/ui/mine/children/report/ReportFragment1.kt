package com.fenghuang.caipiaobao.ui.mine.children.report

import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.widget.dialog.DataPickDialog
import kotlinx.android.synthetic.main.fragment_report_1.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment1 : BaseMvpFragment<ReportFragment1P>() {

    private var dataPickDialog: DataPickDialog? = null


    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ReportFragment1P()

    override fun getLayoutResID() = R.layout.fragment_report_1


    override fun initContentView() {
        val today = TimeUtils.getToday()
        tv_data1.text = today
        tv_data2.text = today

    }

    override fun initData() {

        mPresenter.getReport()
    }

    override fun initEvent() {
        tv_data1.setOnClickListener {
            dataPickDialog = DataPickDialog(getPageActivity())
            dataPickDialog?.setConfirmClickListener {
                    tv_data1.text = it
                dataPickDialog?.dismiss()
            }
            dataPickDialog?.show()
        }
        tv_data2.setOnClickListener {
            dataPickDialog = DataPickDialog(getPageActivity())
            dataPickDialog?.setConfirmClickListener {
                tv_data2.text = it
                dataPickDialog?.dismiss()
            }
            dataPickDialog?.show()
        }



        tvCheck.setOnClickListener {
            if (tv_data1.text.isNullOrEmpty() && tv_data2.text.isNullOrEmpty()) {
                ToastUtils.show("请选择日期")
                return@setOnClickListener
            }
            val t1 = tv_data1.text.toString().trim()
            val t2 = tv_data2.text.toString().trim()
            if (t1 == t2){
                mPresenter.getReport(t1,t2)
            }else{
                val boolean = TimeUtils.compareDate(tv_data1.text.toString(), tv_data2.text.toString())
                if (boolean) {
                    mPresenter.getReport(t1,t2)
                }else ToastUtils.show("起始时间必须大于截止时间!")
            }

        }
    }
}