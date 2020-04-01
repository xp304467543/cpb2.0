package com.fenghuang.caipiaobao.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.isVisible
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.utils.NavigationBarUtil
import kotlinx.android.synthetic.main.dialog_pass_word.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-27
 * @ Describe 密码输入
 *
 */


class PassWordDialog(context: Context, widthDia: Int, heightDia: Int) : Dialog(context) {

    init {
        setContentView(R.layout.dialog_pass_word)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.BOTTOM)
        setCanceledOnTouchOutside(false)
        val lp = window!!.attributes
        lp.width = widthDia
        lp.height = heightDia
        window!!.attributes = lp
        initDialog()
    }

    private fun initDialog() {
        relCloseDialog.setOnClickListener {
            disMissKeyBord()
            dismiss()
        }
    }

    fun setTextWatchListener(textWatcher: TextWatcher) {
        edtPassWord.addTextChangedListener(textWatcher)
    }

    fun showTipsText(string: String) {
        tvFalseTip.visibility = View.VISIBLE
        tvFalseTip.text = string
    }

    override fun dismiss() {
        disMissKeyBord()
        super.dismiss()

    }

    fun clearText() {
        edtPassWord.clearText()
    }

    fun showOrHideLoading() {
        if (edtPassWordLoadingContainer.isVisible) {
            edtPassWordLoadingContainer.visibility = View.GONE
        } else edtPassWordLoadingContainer.visibility = View.VISIBLE

    }


    private fun disMissKeyBord() {
        val view = currentFocus
        if (view is TextView) {
            val mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
        }
    }
}


