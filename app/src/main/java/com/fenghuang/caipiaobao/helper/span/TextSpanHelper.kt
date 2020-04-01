package com.fenghuang.caipiaobao.helper.span

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import com.fenghuang.baselib.utils.AppUtils

/**
 *    author : Peter
 *    date   : 2019/7/3013:39
 *    desc   : 文字Span处理助手
 */
object TextSpanHelper {

    /**
     * 获取数字字体Span构造器
     */
    private fun getNumberFontTypeface(): Typeface {
        return Typeface.createFromAsset(AppUtils.getContext().assets, "font/BarlowCondensedMedium.ttf")
    }

    fun getNumberFontSpanBuilder(number: Int): SpannableStringBuilder {
        return getNumberFontSpanBuilder(number.toString())
    }

    fun getNumberFontSpanBuilder(text: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder(text)
        builder.setSpan(NumberTypefaceSpan("", getNumberFontTypeface()), 0, text.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        return builder
    }

    fun getColorSpanBuilder(text: String, start: Int, end: Int, color: Int): SpannableStringBuilder {
        val builder = SpannableStringBuilder(text)
        // 设置字体前景色
        builder.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return builder
    }

    // 设置字体大小的span, size: dp
    fun getAbsoluteSizeSpanBuilder(text: String, size: Int): SpannableStringBuilder {
        val builder = SpannableStringBuilder(text)
        builder.setSpan(AbsoluteSizeSpan(size, true), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return builder
    }

    // 设置字体大小与颜色
    fun getAbsoluteSiezAndColorSpanBuilder(text: String, size: Int, color: Int, start: Int, end: Int): SpannableStringBuilder {
        val builder = SpannableStringBuilder(text)
        builder.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.setSpan(AbsoluteSizeSpan(size, true), start, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return builder
    }
}