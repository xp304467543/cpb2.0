package com.fenghuang.caipiaobao.ui.lottery.constant

import android.content.Context
import android.graphics.drawable.Drawable
import com.fenghuang.caipiaobao.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/2- 11:24
 * @ Describe
 *
 */
object CodeBackGroundUtil {

    // ===== 根据号码设置背景色 =====
    fun setBackGroundColor(context: Context, code: String, lotteryId: String): Drawable? {
        if (lotteryId != "8") return when (code) {
            "1" -> context.getDrawable(R.drawable.code_1)
            "2" -> context.getDrawable(R.drawable.code_2)
            "3" -> context.getDrawable(R.drawable.code_3)
            "4" -> context.getDrawable(R.drawable.code_4)
            "5" -> context.getDrawable(R.drawable.code_5)
            "6" -> context.getDrawable(R.drawable.code_6)
            "7" -> context.getDrawable(R.drawable.code_7)
            "8" -> context.getDrawable(R.drawable.code_8)
            "9" -> context.getDrawable(R.drawable.code_9)
            "10" -> context.getDrawable(R.drawable.code_10)
            else -> context.getDrawable(R.drawable.code_3)
        } else {
            return when (code) {
                "1" -> context.getDrawable(R.drawable.xcode_red)
                "2" -> context.getDrawable(R.drawable.xcode_red)
                "3" -> context.getDrawable(R.drawable.xcode_blue)
                "4" -> context.getDrawable(R.drawable.xcode_blue)
                "5" -> context.getDrawable(R.drawable.xcode_green)
                "6" -> context.getDrawable(R.drawable.xcode_green)
                "7" -> context.getDrawable(R.drawable.xcode_red)
                "8" -> context.getDrawable(R.drawable.xcode_red)
                "9" -> context.getDrawable(R.drawable.xcode_blue)
                "10" -> context.getDrawable(R.drawable.xcode_blue)

                "11" -> context.getDrawable(R.drawable.xcode_green)
                "12" -> context.getDrawable(R.drawable.xcode_red)
                "13" -> context.getDrawable(R.drawable.xcode_red)
                "14" -> context.getDrawable(R.drawable.xcode_blue)
                "15" -> context.getDrawable(R.drawable.xcode_blue)
                "16" -> context.getDrawable(R.drawable.xcode_green)
                "17" -> context.getDrawable(R.drawable.xcode_green)
                "18" -> context.getDrawable(R.drawable.xcode_red)
                "19" -> context.getDrawable(R.drawable.xcode_red)
                "20" -> context.getDrawable(R.drawable.xcode_blue)

                "21" -> context.getDrawable(R.drawable.xcode_green)
                "22" -> context.getDrawable(R.drawable.xcode_green)
                "23" -> context.getDrawable(R.drawable.xcode_red)
                "24" -> context.getDrawable(R.drawable.xcode_red)
                "25" -> context.getDrawable(R.drawable.xcode_blue)
                "26" -> context.getDrawable(R.drawable.xcode_blue)
                "27" -> context.getDrawable(R.drawable.xcode_green)
                "28" -> context.getDrawable(R.drawable.xcode_green)
                "29" -> context.getDrawable(R.drawable.xcode_red)
                "30" -> context.getDrawable(R.drawable.xcode_red)

                "31" -> context.getDrawable(R.drawable.xcode_blue)
                "32" -> context.getDrawable(R.drawable.xcode_green)
                "33" -> context.getDrawable(R.drawable.xcode_green)
                "34" -> context.getDrawable(R.drawable.xcode_red)
                "35" -> context.getDrawable(R.drawable.xcode_red)
                "36" -> context.getDrawable(R.drawable.xcode_blue)
                "37" -> context.getDrawable(R.drawable.xcode_blue)
                "38" -> context.getDrawable(R.drawable.xcode_green)
                "39" -> context.getDrawable(R.drawable.xcode_green)
                "40" -> context.getDrawable(R.drawable.xcode_red)

                "41" -> context.getDrawable(R.drawable.xcode_blue)
                "42" -> context.getDrawable(R.drawable.xcode_blue)
                "43" -> context.getDrawable(R.drawable.xcode_green)
                "44" -> context.getDrawable(R.drawable.xcode_green)
                "45" -> context.getDrawable(R.drawable.xcode_red)
                "46" -> context.getDrawable(R.drawable.xcode_red)
                "47" -> context.getDrawable(R.drawable.xcode_blue)
                "48" -> context.getDrawable(R.drawable.xcode_blue)
                "49" -> context.getDrawable(R.drawable.xcode_green)
                else -> context.getDrawable(R.drawable.xcode_red)
            }
        }
    }


}