package com.fenghuang.caipiaobao.widget.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.SoftInputUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.dialog_search.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/5
 * @ Describe
 *
 */

class SearchDialog (context: Context) : Dialog(context,R.style.dialog){

    private var mListener: ((str: String) -> Unit)? = null
    fun setConfirmClickListener(listener: (str: String) -> Unit) {
        mListener = listener
    }

    init {
        setContentView(R.layout.dialog_search)
        window?.setGravity(Gravity.TOP)
        val lp = window?.attributes
        lp?.width = ViewGroup.LayoutParams.MATCH_PARENT
        iniEvent()
    }

    private fun iniEvent() {
        tvCancel.setOnClickListener { dismiss() }
        etSearchName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
           if (!etSearchName.text.isNullOrEmpty()) mListener?.invoke(etSearchName.text.toString())

            }
            false
        }
    }

    override fun show() {
        super.show()
        SoftInputUtils.showSoftInput(etSearchName)
    }

    override fun dismiss() {
        disMissKeyBord()
        super.dismiss()

    }

    /**
     * 隐藏软键盘
     */
    private fun disMissKeyBord() {
        val view = currentFocus
        if (view is TextView) {
            val mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
        }
    }
}
