package com.fenghuang.caipiaobao.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.TextView
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.utils.NavigationBarUtil

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-19
 * @ Describe 余额不足
 *
 */

class ReChargeDialog(context: Context,var isHor:Boolean) : Dialog(context) {

    init {
        window!!.setWindowAnimations(R.style.BaseDialogAnim)
        setContentView(R.layout.dialog_live_chat_recharge)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window!!.attributes
        lp.width = ViewUtils.dp2px(314) // 宽度
        lp.height = ViewUtils.dp2px(290)  // 高度
        window!!.attributes = lp

        findViewById<TextView>(R.id.tvRechargeCancel).setOnClickListener {
            dismiss()
        }
        findViewById<TextView>(R.id.tvGoRecharge).setOnClickListener {
            dismiss()
            mListener?.invoke()
        }
    }


    private var mListener: (() -> Unit)? = null
    fun setOnSendClickListener(listener: () -> Unit) {
        mListener = listener
    }


    override fun show() {
      if (isHor)  NavigationBarUtil.focusNotAle(window)
        super.show()
        if (isHor){
            NavigationBarUtil.hideNavigationBar(window)
            NavigationBarUtil.clearFocusNotAle(window)
        }
    }


}