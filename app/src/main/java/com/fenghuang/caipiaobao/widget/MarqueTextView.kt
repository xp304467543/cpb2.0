package com.fenghuang.caipiaobao.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView



/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-28
 * @ Describe
 *
 */

class MarqueTextView : TextView {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    override fun isFocused(): Boolean {
        //就是把这里返回true即可
        return true
    }
}