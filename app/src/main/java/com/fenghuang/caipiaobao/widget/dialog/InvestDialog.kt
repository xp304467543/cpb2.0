package com.fenghuang.caipiaobao.widget.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.utils.MoneyValueFilter
import kotlinx.android.synthetic.main.dialog_invest.*
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2019/10/24- 19:51
 * @ Describe 充值dialog
 *
 */

class InvestDialog(context: Context, title: String, confirm: String, cancel: String) : Dialog(context) {
    init {
        setContentView(com.fenghuang.caipiaobao.R.layout.dialog_invest)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window!!.attributes
        lp.width = ViewUtils.dp2px(316) // 宽度
        lp.height = ViewUtils.dp2px(238)  // 高度
//      lp.alpha = 0.7f // 透明度
        window!!.attributes = lp
        setCanceledOnTouchOutside(false)
        initDialog(title, confirm, cancel)


        etInvestMoney.filters = arrayOf<InputFilter>(MoneyValueFilter())
        etInvestMoney.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString() != "" && BigDecimal(p0.toString()).compareTo(BigDecimal(999999)) == 1) {
                    etInvestMoney.setText("999999")
                    etInvestMoney.setSelection(etInvestMoney.length())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun initDialog(title: String, confirm: String, cancel: String) {
        if (tvConfirm !== null) {
            tvConfirm.setOnClickListener {
                mListener?.invoke()
            }
        }
        if (tvCancel !== null) {
            tvCancel.setOnClickListener {
                dismiss()
            }
        }
        if (!TextUtils.isEmpty(title)) {
            tvInvestType.text = title
        }
        if (!TextUtils.isEmpty(confirm)) {
            tvConfirm.text = confirm
        }
        if (!TextUtils.isEmpty(cancel)) {
            tvCancel.text = cancel
        }
    }


    private var mListener: (() -> Unit)? = null
    fun setConfirmClickListener(listener: () -> Unit) {
        mListener = listener
    }

    fun getText(): String {
        return etInvestMoney.text.toString()
    }


    override fun dismiss() {
        disMissKeyBord()
        super.dismiss()
    }

    private fun disMissKeyBord() {
        val view = currentFocus
        if (view is TextView) {
            val mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
        }
    }
}