package com.fenghuang.caipiaobao.widget.dialog.bottom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.zyyoona7.picker.OptionsPickerView
import kotlinx.android.synthetic.main.dialog_lottery_select.*

/**
 *
 * @ Author  daniel
 * @ Describe 彩种选择器
 *
 */

class BottomLotterySelectDialog(context: Context, val title: ArrayList<String>) : Dialog(context) {

    init {
        setContentView(R.layout.dialog_lottery_select)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.BOTTOM)
        val lp = window!!.attributes
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT // 宽度
        lp.dimAmount = 0.3f
        window!!.attributes = lp
        val mOptionsPickerView = findViewById<OptionsPickerView<String>>(R.id.lotteryPickerView)
        mOptionsPickerView.setData(title)
        mOptionsPickerView.setTextSize(18f, true)
        mOptionsPickerView.setShowDivider(true)
        mOptionsPickerView.setVisibleItems(6)
        mOptionsPickerView.setDividerColor(ViewUtils.getColor(R.color.grey_dd))
        mOptionsPickerView.setLineSpacing(80f)
        mOptionsPickerView.setSelectedItemTextColor(ViewUtils.getColor(R.color.black))
        mOptionsPickerView.setNormalItemTextColor(ViewUtils.getColor(R.color.grey_e6))
        mOptionsPickerView.setOnOptionsSelectedListener { _, opt1Data, _, _, _, _ ->
            if (!opt1Data.isNullOrEmpty()) tvLotteryWheelSure.isEnabled = true
        }
        mOptionsPickerView.setOnPickerScrollStateChangedListener {
            tvLotteryWheelSure.isEnabled = false
        }
        tvWheelCancel.setOnClickListener {
            dismiss()
        }
    }

}