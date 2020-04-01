package com.fenghuang.baselib.theme

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.fenghuang.baselib.R


/**
 * 系统UI处理工具类，包括主题，图标等等⑽⑼⑹㈤
 */

object UiUtils {

    private var mCurrentTheme: AppTheme? = null


    /**
     * 设置App的主题
     * 默认是来上一次的主题
     */
    fun setAppTheme(context: Context, theme: AppTheme) {
        mCurrentTheme = theme
        when (theme) {
            AppTheme.White -> context.setTheme(R.style.WhiteTheme)
            AppTheme.Red -> context.setTheme(R.style.RedTheme)
            AppTheme.Black -> context.setTheme(R.style.BlackTheme)
        }
    }


    /**
     * 根据Theme获取Style
     */
    fun getThemeStyle(theme: AppTheme): Int {
        return when (theme) {
            AppTheme.Red -> R.style.RedTheme
            else -> R.style.WhiteTheme
        }
    }

    /**
     * 获取某一个属性的主题颜色
     */
    fun getThemeColor(context: Context, attrRes: Int = R.attr.colorPrimary): Int {
        val theme = context.theme
        val typedArray = theme.obtainStyledAttributes(intArrayOf(attrRes))
        val color = typedArray.getColor(0, Color.LTGRAY)
        typedArray.recycle()
        return color
    }

    /**
     * 获取当前的主题颜色
     */
    fun getCurrentThemeColor(context: Context): Int {
        return getThemeColor(context, R.attr.colorPrimary)
    }

    private fun getColor(context: Context, color: Int): Int {
        return ContextCompat.getColor(context, color)
    }
}