package com.fenghuang.caipiaobao.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R


/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-31
 * @ Describe
 *
 */

class VerticalTextView : LinearLayout {
    private var mContext: Context? = null
    private var linearLayout: LinearLayout? = null

    constructor(context: Context) : super(context) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)

    }


    private fun init(context: Context) {
        this.mContext = context
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.holder_lottery_child_item_luzhu, this)
        linearLayout = findViewById(R.id.linLuZhuData)
    }

    fun setText(str: String) {

        val textView = getText()

        val color = when (str) {
            "大", "双", "1","龙","后" -> R.color.color_FF513E
            "红" -> R.color.text_red
            "绿" -> R.color.colorGreenPrimary
            "蓝" -> R.color.colorBluePrimary
            else -> R.color.color_999999
        }

        textView.setTextColor(ContextCompat.getColor(context, color))

        textView.text = when (str) {
            "0" -> "X"
            "1" -> "√️"
            else -> str
        }

        linearLayout!!.addView(textView)
    }

    fun setTextEmpty() {
        linearLayout!!.addView(getText())
    }


    private fun getText(): TextView {
        val textView = TextView(context)
        textView.textSize = 13f
        textView.height = ViewUtils.dp2px(25)
        textView.setTextColor(ContextCompat.getColor(context, R.color.color_999999))
        textView.gravity = Gravity.CENTER
        return textView
    }

}