package com.fenghuang.caipiaobao.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.Html
import android.view.Gravity
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.utils.LaunchUtils
import kotlinx.android.synthetic.main.dialog_system_notice.*
import kotlinx.android.synthetic.main.dialog_version.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-25
 * @ Describe 系统公告
 *
 */

class SystemNoticeDialog(context: Context) : Dialog(context){

    init {
        window!!.setWindowAnimations(R.style.BaseDialogAnim)
        setContentView(R.layout.dialog_system_notice)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window!!.attributes
//      lp.alpha = 0.7f // 透明度
        window!!.attributes = lp
        setCanceledOnTouchOutside(true)
        btNotice.setOnClickListener {
            dismiss()
        }
    }


    fun setContent(string: String) {
        noticeContent.text = Html.fromHtml(string)
    }

}