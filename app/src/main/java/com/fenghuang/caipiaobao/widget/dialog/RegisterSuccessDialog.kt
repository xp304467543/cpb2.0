package com.fenghuang.caipiaobao.widget.dialog

import android.app.Dialog
import android.content.Context
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.dialog_register_success.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-16
 * @ Describe
 *
 */

class RegisterSuccessDialog(context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.dialog_register_success)
        window?.setBackgroundDrawableResource(com.fenghuang.baselib.R.color.transparent)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        tvRegisterKnow.setOnClickListener {
            dismiss()
        }
    }
}