package com.fenghuang.caipiaobao.widget.dialog.bottom

import android.content.Context
import android.graphics.Color
import android.view.View
import com.fenghuang.caipiaobao.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_bottom_web_select.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/24
 * @ Describe
 *
 */

class BottomWebSelect(context: Context) : BottomSheetDialog(context) {

    private var selectListener: ((it: Int) -> Unit)? = null

    init {
        setContentView(R.layout.dialog_bottom_web_select)
        val root = delegate?.findViewById<View>(R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(root)
        behavior.isHideable = false
        delegate?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundColor(Color.TRANSPARENT)
        initEvent()
    }

    private fun initEvent() {
        imgWebClose.setOnClickListener { dismiss() }
        imgWebCp.setOnClickListener {
            selectListener?.invoke(0)
            dismiss()
        }
        imgWebQp.setOnClickListener {
            selectListener?.invoke(1)
            dismiss()
        }
    }


    fun setSelectListener(SelectListener: ((it: Int) -> Unit)) {
        selectListener = SelectListener
    }
}