package com.fenghuang.caipiaobao.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.dialog_tips_confirm.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-17
 * @ Describe
 *
 */

class GlobalTipsDialog(context: Context, title: String, confirm: String, cancel: String, contentDes: String) : Dialog(context) {
    init {
        setContentView(R.layout.dialog_tips_confirm)
        window!!.setWindowAnimations(R.style.BaseDialogAnim)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window!!.attributes
        lp.width = ViewUtils.dp2px(316) // 宽度
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT  // 高度
//      lp.alpha = 0.7f // 透明度
        window!!.attributes = lp
        initText(title, confirm, cancel, contentDes)
    }

    private fun initText(content: String, confirm: String, cancel: String, contentDes: String) {
        if (!TextUtils.isEmpty(content)) {
            tvContent.visibility = View.VISIBLE
            tvContent.text = content
        }
        if (!TextUtils.isEmpty(confirm)) {
            tvConfirm.visibility = View.VISIBLE
            tvConfirm.text = confirm
        }
        if (!TextUtils.isEmpty(cancel)) {
            tvCancel.visibility = View.VISIBLE
            tvCancel.text = cancel
        }
        if (!TextUtils.isEmpty(contentDes)) {
            tvContentDescription.visibility = View.VISIBLE
            tvContentDescription.text = contentDes
        }
        if (tvConfirm !== null) {
            tvConfirm.setOnClickListener {
                dismiss()
                mListener?.invoke()
            }
        }
        if (tvCancel !== null) {
            tvCancel?.setOnClickListener {
                dismiss()
                mListenerCancel?.invoke()
            }
        }
    }

    private var mListener: (() -> Unit)? = null
    fun setConfirmClickListener(listener: () -> Unit) {
        mListener = listener
    }

    private var mListenerCancel: (() -> Unit)? = null
    fun setCanCelClickListerner(listener: () -> Unit) {
        mListenerCancel = listener
    }


    fun setEnable(enable: Boolean) {
        tvConfirm.isEnabled = enable
    }

}