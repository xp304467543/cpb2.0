package com.fenghuang.caipiaobao.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.dialog_wheel_view.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-04
 * @ Describe
 *
 */

class WheelViewDialog(context: Context, var list: List<String>, var title: String) : Dialog(context) {

    private var mListener: ((it: String) -> Unit)? = null


    init {
        setContentView(R.layout.dialog_wheel_view)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.BOTTOM or Gravity.BOTTOM)
        val lp = window!!.attributes
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT // 宽度
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT   // 高度
        lp.dimAmount = 0.3f
//      lp.alpha = 0.7f // 透明度
        window!!.attributes = lp
        tvCenterText.text = title

        //设置数据
        wheelView.data = list

        //尽请使用各种方法
        wheelView.setTextSize(18f, true)
        wheelView.isShowDivider = true
        wheelView.dividerColor = ViewUtils.getColor(R.color.grey_dd)
        wheelView.lineSpacing = 80F
        wheelView.selectedItemPosition = list.size / 2
        wheelView.selectedItemTextColor = ViewUtils.getColor(R.color.black)
        wheelView.normalItemTextColor = ViewUtils.getColor(R.color.grey_e6)

        tvWheelCancel.setOnClickListener {
            dismiss()
        }
        tvWheelSure.setOnClickListener {
            mListener?.invoke(wheelView.selectedItemData.toString())
        }
    }

    fun setConfirmClickListener(listener: (it: String) -> Unit) {
        mListener = listener
    }
}