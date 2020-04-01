package com.fenghuang.caipiaobao.widget.dialog

import android.app.Dialog
import android.content.Context
import com.fenghuang.caipiaobao.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-21
 * @ Describe 评论成功
 *
 */

class CommentsSuccessDialog(context: Context) : Dialog(context) {


    init {
        setContentView(R.layout.dialog_comment_success)
        window?.setBackgroundDrawableResource(com.fenghuang.baselib.R.color.transparent)
//        val lp = window!!.attributes
//        lp.width = ViewUtils.dp2px(210)
//        lp.height = ViewUtils.dp2px(194)
    }

}