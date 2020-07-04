package com.fenghuang.caipiaobao.widget.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.dialog_data.*
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/29
 * @ Describe
 *
 */
class DataPickDialog(context: Context) : Dialog(context) {

    private var mListener: ((it: String) -> Unit)? = null

    private var selectData = ""

    fun setConfirmClickListener(listener: (it: String) -> Unit) {
        mListener = listener
    }


    init {
        setContentView(R.layout.dialog_data)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.CENTER or Gravity.CENTER)
        val lp = window!!.attributes
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT // 宽度
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT   // 高度
        lp.dimAmount = 0.3f
//      lp.alpha = 0.7f // 透明度
        window!!.attributes = lp
        // 设置已选的日期
        mCalendarView.selectDate = initDataCalendar()
        initDataRv()
    }

    private fun initDataCalendar(): List<String>? {
        val dates: MutableList<String> = ArrayList()
        val calendar = Calendar.getInstance(Locale.CHINA)
        val sdf = SimpleDateFormat(mCalendarView.dateFormatPattern, Locale.CHINA)
        sdf.format(calendar.time)
        dates.add(sdf.format(calendar.time))
        return dates
    }

    private fun initDataRv() {
        // 指定显示的日期, 如当前月的下个月
        val calendar: Calendar = mCalendarView.calendar
//        calendar.add(Calendar.MONTH, 1)
        mCalendarView.calendar = calendar
        // 设置字体
        mCalendarView.setTypeface(Typeface.SERIF)

        // 设置日期状态改变监听
        mCalendarView.setOnDateChangeListener { view, select, year, month, day ->
            if (select) {
//                ToastUtils.show("选中了：" + year + "年" + (month + 1) + "月" + day + "日")
                selectData = year.toString()+"-"+(month+1)+"-"+day
//            } else {
//                ToastUtils.show("取消选中了：" + year + "年" + (month + 1) + "月" + day + "日")
            }
        }
        // 设置是否能够改变日期状态
        mCalendarView.isChangeDateStatus = true

        // 设置是否能够点击
        mCalendarView.isClickable = true
        selectData = TimeUtils.getToday()
        setCurDate()
        imgLeft.setOnClickListener {
            mCalendarView.lastMonth()
            setCurDate()
        }

        imgRight.setOnClickListener {
            mCalendarView.nextMonth()
            setCurDate()
        }
        tvSure.setOnClickListener {
          if (selectData!="")  mListener?.invoke(selectData) else ToastUtils.show("请选择日期")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCurDate() {
        tvTopData.text = (mCalendarView.month+1).toString() + "月      " + mCalendarView.year.toString()
        tvTopData.typeface = Typeface.SERIF
    }
}