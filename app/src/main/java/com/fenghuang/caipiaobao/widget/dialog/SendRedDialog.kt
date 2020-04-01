package com.fenghuang.caipiaobao.widget.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.utils.NavigationBarUtil
import kotlinx.android.synthetic.main.dialog_send_red.*
import java.util.regex.Pattern

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-09
 * @ Describe 送红包窗口
 *
 */

class SendRedDialog(context: Context, val isHor: Boolean) : Dialog(context) {



    init {
        window!!.setWindowAnimations(R.style.BaseDialogAnim)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0.2f)
        if (isHor) {  //是否横屏显示
            setContentView(R.layout.dialog_send_red_hor)
        } else setContentView(R.layout.dialog_send_red)

        initEvent()
    }

    fun initEvent() {
        tvRedSend.setOnClickListener {
            if (etRedEnvelopeTotal.text.toString().isEmpty()) {
                ToastUtils.show("请输入红包金额")
                return@setOnClickListener
            }
            if (etRedEnvelopeRedNumber.text.toString().isEmpty()) {
                ToastUtils.show("请输入红包个数")
                return@setOnClickListener
            }
            val content = if (etRedEnvelopeContent.text.toString().isEmpty()) "恭喜发财" else etRedEnvelopeContent.text.toString()
            mListener?.invoke(etRedEnvelopeTotal.text.toString(), etRedEnvelopeRedNumber.text.toString(), content)
        }

        etRedEnvelopeTotal.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString() != "" && p0.toString().toInt() > 2000) {
                    ToastUtils.showNormal("最大金额为2000元")
                    etRedEnvelopeTotal.setText("2000")
                }
                if (p0.toString() != "" && p0.toString().toInt() < 1) {
                    ToastUtils.showNormal("最小金额为1元")
                    etRedEnvelopeTotal.setText("1")
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        etRedEnvelopeRedNumber.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString() != "" && p0.toString().toInt() > 200) {
                    ToastUtils.showNormal("红包最多个数为200")
                    etRedEnvelopeRedNumber.setText("200")
                }
                if (p0.toString() != "" && p0.toString().toInt() < 1) {
                    ToastUtils.showNormal("红包最少个数为1")
                    etRedEnvelopeRedNumber.setText("1")
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        etRedEnvelopeContent.addTextChangedListener(object : TextWatcher {
            //记录输入的字数
            var wordNum: CharSequence? = null
            var selectionStart: Int = 0
            var selectionEnd: Int = 0
            override fun afterTextChanged(p0: Editable?) {
                selectionStart = etRedEnvelopeContent.selectionStart
                selectionEnd = etRedEnvelopeContent.selectionEnd
                if (wordNum!!.length > 10) {
                    p0?.delete(selectionStart - 1, selectionEnd)
                    etRedEnvelopeContent.text = p0
                    etRedEnvelopeContent.setSelection(etRedEnvelopeContent.text.length)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                wordNum = p0
            }

        })


        val inputFilter: InputFilter = object : InputFilter {
            var pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]")
            override fun filter(charSequence: CharSequence, i: Int, i1: Int, spanned: Spanned, i2: Int, i3: Int): CharSequence? {
                val matcher = pattern.matcher(charSequence)
                return if (!matcher.find()) {
                    null
                } else {
                    ToastUtils.showNormal("只能输入汉字,英文，数字")
                    ""
                }
            }
        }
        etRedEnvelopeContent.filters = arrayOf(inputFilter)
    }

    private var mListener: ((total: String, redNumber: String, redContent: String) -> Unit)? = null
    fun setOnSendClickListener(listener: (total: String, redNumber: String, redContent: String) -> Unit) {
        mListener = listener
    }
    override fun show() {
        NavigationBarUtil.focusNotAle(window)
        super.show()
        NavigationBarUtil.hideNavigationBar(window)
        NavigationBarUtil.clearFocusNotAle(window)
    }

}