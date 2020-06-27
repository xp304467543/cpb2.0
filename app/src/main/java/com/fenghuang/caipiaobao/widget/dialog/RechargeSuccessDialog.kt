package com.fenghuang.caipiaobao.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.view.Gravity
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.dialog_recharge_success.*
import kotlinx.android.synthetic.main.dialog_success.ivDialogImg
import kotlinx.android.synthetic.main.dialog_success.tvDialogText

/**
 *
 * @ Author  QinTian
 * @ Date  2019/10/25- 13:50
 * @ Describe 成功dialog
 *
 */

class RechargeSuccessDialog(context: Context, content: String, img: Int,title:String="",value: SpannableString?=null) : Dialog(context) {
    init {
        setContentView(R.layout.dialog_recharge_success)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window!!.attributes
//        lp.width = ViewUtils.dp2px(230) // 宽度
//        lp.height = ViewUtils.dp2px(150)  // 高度
//      lp.alpha = 0.7f // 透明度
        window!!.attributes = lp
        setCanceledOnTouchOutside(true)
        initText(title,content, img,value)
    }

    private fun initText(title:String,content: String, img: Int,value: SpannableString?) {
        tvDialogText.text = content
        titleTvRe.text = title
       if (value!=null) tvDialogText.text = value
        if (img!=0){
            ivDialogImg.setImageResource(img)
        }else ViewUtils.setGone(ivDialogImg)
    }
}