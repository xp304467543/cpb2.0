package com.fenghuang.caipiaobao.widget.dialog

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.mine.children.report.MineReportPosterAct
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_bottom_report.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/6
 * @ Describe
 *
 */
class ReportBottomDialog(context: Context, var inviteNum: String, var inviteUrl: String) : BottomSheetDialog(context) {

    init {
        setContentView(R.layout.dialog_bottom_report)
        val root = delegate?.findViewById<View>(R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(root)
        behavior.isHideable = false
        delegate?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundColor(Color.TRANSPARENT)
        initViews()
        iniEvent()
    }

    private fun iniEvent() {
        tv_report_1.setOnClickListener {
            ViewUtils.copyText(inviteUrl)
            ToastUtils.show("链接 已复制到剪贴板")
        }
        tv_report_2.setOnClickListener {
            ViewUtils.copyText(inviteNum)
            ToastUtils.show("邀请码 已复制到剪贴板")
        }
        tv_report_3.setOnClickListener {
            val intent = Intent(context, MineReportPosterAct::class.java)
            intent.putExtra("inviteUrl", inviteUrl)
            intent.putExtra("inviteNum", inviteNum)
            context.startActivity(intent)
            dismiss()
        }
    }

    private fun initViews() {
        tvInviteNum.text = inviteNum
    }
}