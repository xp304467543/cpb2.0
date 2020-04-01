package com.fenghuang.caipiaobao.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.Html
import android.view.Gravity
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.utils.LaunchUtils
import kotlinx.android.synthetic.main.dialog_version.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-25
 * @ Describe 版本跟新dialog
 *
 */

class VersionDialog(context: Context) : Dialog(context){

    init {
        window!!.setWindowAnimations(R.style.BaseDialogAnim)
        setContentView(R.layout.dialog_version)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window!!.attributes
//      lp.alpha = 0.7f // 透明度
        window!!.attributes = lp
        setCanceledOnTouchOutside(false)
    }


    fun setContent(string: String) {
        upDateContent.text = Html.fromHtml(string)
    }

    fun setJum(url: String) {
        btUpDate.setOnClickListener {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val contentUrl = Uri.parse(url)//此处填链接
            intent.data = contentUrl
            LaunchUtils.startActivity(context, intent)
        }
    }
}