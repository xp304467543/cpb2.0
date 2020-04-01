package com.fenghuang.caipiaobao.ui.lottery.constant

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/3- 15:46
 * @ Describe
 *
 */
object LotteryTypeSelectUtil {

    // ===== 添加 号码 开奖数据 =====
    fun addOpenCode(context: Context, codeContainer: LinearLayout, result: List<String>, lotteryId: String) {
        codeContainer.removeAllViews()
        codeContainer.gravity = Gravity.CENTER_VERTICAL
        val size = (ViewUtils.getScreenWidth(context) - ViewUtils.dp2px(75)) / 10
        when (lotteryId) {
            "8" -> {
                for ((index, it) in result.withIndex()) {
                    val openText = TextView(context)
                    openText.gravity = Gravity.CENTER
                    openText.typeface = Typeface.DEFAULT_BOLD
                    openText.text = it
                    if (index != 6) {
                        openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                        openText.setTextColor(ViewUtils.getColor(R.color.white))
                        openText.background = CodeBackGroundUtil.setBackGroundColor(context, it, lotteryId)
                    } else {
                        openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
                        openText.setTextColor(ViewUtils.getColor(R.color.color_CCCCCC))
                    }
                    codeContainer.addView(openText)
                    val params = openText.layoutParams as LinearLayout.LayoutParams
                    params.height = size
                    params.width = size
                    params.leftMargin = ViewUtils.dp2px(5)
                    openText.layoutParams = params
                }
            }
            else -> {
                for (it in result) {
                    val openText = TextView(context)
                    openText.gravity = Gravity.CENTER
                    openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                    openText.typeface = Typeface.DEFAULT_BOLD
                    openText.text = it
                    openText.setTextColor(ViewUtils.getColor(R.color.white))
                    codeContainer.addView(openText)
                    val params = openText.layoutParams as LinearLayout.LayoutParams
                    params.height = size
                    params.width = size
                    params.leftMargin = ViewUtils.dp2px(5)
                    openText.layoutParams = params
                    if (result.size >= 10) {
                        openText.background = CodeBackGroundUtil.setBackGroundColor(context, it, lotteryId)
                    } else openText.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                }
            }
        }
    }

    // ===== 添加 大小 开奖数据 =====
    fun addOpenCodeBigger(context: Context, codeContainer: LinearLayout, result: List<String>,lotteryId: String) {
        codeContainer.removeAllViews()
        codeContainer.gravity = Gravity.CENTER_VERTICAL
        val size = (ViewUtils.getScreenWidth(context) - ViewUtils.dp2px(75)) / 10
        for (it in result) {
            val openText = TextView(context)
            openText.gravity = Gravity.CENTER
            openText.typeface = Typeface.DEFAULT_BOLD
            openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
            openText.setTextColor(ViewUtils.getColor(R.color.white))
            if (lotteryId == "10"  ||lotteryId =="1"){
                if (it.toInt() < 5) {
                    openText.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                    openText.text = "小"
                } else {
                    openText.background = ViewUtils.getDrawable(R.drawable.xcode_red)
                    openText.text = "大"
                }
            }else{
                if (it.toInt() <=5) {
                    openText.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                    openText.text = "小"
                } else {
                    openText.background = ViewUtils.getDrawable(R.drawable.xcode_red)
                    openText.text = "大"
                }
            }

            codeContainer.addView(openText)
            val params = openText.layoutParams as LinearLayout.LayoutParams
            params.height = size
            params.width = size
            params.leftMargin = ViewUtils.dp2px(5)
            openText.layoutParams = params
        }
    }


    // ===== 添加 单双 开奖数据 =====
    fun addOpenCodeSingle(context: Context, codeContainer: LinearLayout, result: List<String>) {
        codeContainer.removeAllViews()
        codeContainer.gravity = Gravity.CENTER_VERTICAL
        val size = (ViewUtils.getScreenWidth(context) - ViewUtils.dp2px(75)) / 10
        for (it in result) {
            val openText = TextView(context)
            openText.gravity = Gravity.CENTER
            openText.typeface = Typeface.DEFAULT_BOLD
            openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
            openText.setTextColor(ViewUtils.getColor(R.color.white))
            if (it.toInt() % 2 == 0) {
                openText.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                openText.text = "双"
            } else {
                openText.background = ViewUtils.getDrawable(R.drawable.xcode_red)
                openText.text = "单"
            }
            codeContainer.addView(openText)
            val params = openText.layoutParams as LinearLayout.LayoutParams
            params.height = size
            params.width = size
            params.leftMargin = ViewUtils.dp2px(5)
            openText.layoutParams = params
        }
    }


    // ===== 添加 总和/形态 开奖数据      =====
    fun addOpenCodeTypeAndSum(context: Context, codeContainer: LinearLayout, result: List<String>) {
        val type = LotteryComposeUtil.lotterySumAndType(result)
        codeContainer.removeAllViews()
        codeContainer.gravity = Gravity.CENTER_VERTICAL
        val size = (ViewUtils.getScreenWidth(context) - ViewUtils.dp2px(80)) / 7
        repeat(7) {
            val openText = TextView(context)
            openText.gravity = Gravity.CENTER
            openText.typeface = Typeface.DEFAULT_BOLD
            openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            when (it) {
                0 -> {
                    openText.text = LotteryComposeUtil.lotterySum(result)
                    openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                }
                1 -> {
                    if (LotteryComposeUtil.lotterySum(result).toInt() >= 23) {
                        openText.text = "大"
                        openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                    } else {
                        openText.text = "小"
                        openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                    }
                }
                2 -> {
                    when ((LotteryComposeUtil.lotterySum(result).toInt() % 2)) {
                        0 -> {
                            openText.text = "双"
                            openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                        }
                        else -> {
                            openText.text = "单"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                        }
                    }
                }
                3 -> {
                    when (LotteryComposeUtil.dragonOrTiger(result)) {
                        "龙" -> {
                            openText.text = "龙"
                            openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                        }
                        "虎" -> {
                            openText.text = "虎"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                        }
                        "和" -> {
                            openText.text = "和"
                            openText.setTextColor(ViewUtils.getColor(R.color.colorGreen))
                        }
                    }

                }
                4 -> {
                    openText.text = type[0]
                    openText.setTextColor(LotteryComposeUtil.getLotterySumAndTypeBack(type[0]))
                }
                5 -> {
                    openText.text = type[1]
                    openText.setTextColor(LotteryComposeUtil.getLotterySumAndTypeBack(type[1]))
                }
                6 -> {
                    openText.text = type[2]
                    openText.setTextColor(LotteryComposeUtil.getLotterySumAndTypeBack(type[2]))
                }
            }
            codeContainer.addView(openText)
            val params = openText.layoutParams as LinearLayout.LayoutParams
            params.height = ViewUtils.dp2px(35)
            params.width = size
            if (it != 0) params.leftMargin = ViewUtils.dp2px(10)
            openText.layoutParams = params
        }
    }


    // ===== 添加 亚冠和/龙虎 开奖数据      =====
    fun addSumAndDragonAndTiger(context: Context, codeContainer: LinearLayout, result: List<String>) {
        codeContainer.removeAllViews()
        codeContainer.gravity = Gravity.CENTER_VERTICAL
        repeat(8) {
            val openText = TextView(context)
            openText.gravity = Gravity.CENTER
            openText.typeface = Typeface.DEFAULT_BOLD
            openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
            when (it) {
                0 -> {
                    openText.text = LotteryComposeUtil.lotterySum(result.subList(0, 2))
                    openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                    openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                }
                1 -> {
                    if (LotteryComposeUtil.lotterySum(result.subList(0, 2)).toInt() > 11) {
                        openText.text = "大"
                        openText.setTextColor(ViewUtils.getColor(R.color.text_red))
                        openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                    } else {
                        openText.text = "小"
                        openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                        openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                    }
                }
                2 -> {
                    if (LotteryComposeUtil.lotterySum(result.subList(0, 2)).toInt() % 2 == 0) {
                        openText.text = "双"
                        openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                        openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                    } else {
                        openText.text = "单"
                        openText.setTextColor(ViewUtils.getColor(R.color.text_red))
                        openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
                    }
                }
                3 -> {
                    if (result[0].toInt() > result[9].toInt()) {
                        openText.text = "龙"
                        openText.background = ViewUtils.getDrawable(R.drawable.xcode_red)
                    } else {
                        openText.text = "虎"
                        openText.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                    }
                    openText.setTextColor(ViewUtils.getColor(R.color.white))
                }
                4 -> {
                    if (result[1].toInt() > result[8].toInt()) {
                        openText.text = "龙"
                        openText.background = ViewUtils.getDrawable(R.drawable.xcode_red)
                    } else {
                        openText.text = "虎"
                        openText.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                    }
                    openText.setTextColor(ViewUtils.getColor(R.color.white))
                }
                5 -> {
                    if (result[2].toInt() > result[7].toInt()) {
                        openText.text = "龙"
                        openText.background = ViewUtils.getDrawable(R.drawable.xcode_red)
                    } else {
                        openText.text = "虎"
                        openText.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                    }
                    openText.setTextColor(ViewUtils.getColor(R.color.white))
                }
                6 -> {
                    if (result[3].toInt() > result[6].toInt()) {
                        openText.text = "龙"
                        openText.background = ViewUtils.getDrawable(R.drawable.xcode_red)
                    } else {
                        openText.text = "虎"
                        openText.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                    }
                    openText.setTextColor(ViewUtils.getColor(R.color.white))
                }
                7 -> {
                    if (result[4].toInt() > result[5].toInt()) {
                        openText.text = "龙"
                        openText.background = ViewUtils.getDrawable(R.drawable.xcode_red)
                    } else {
                        openText.text = "虎"
                        openText.background = ViewUtils.getDrawable(R.drawable.xcode_blue)
                    }
                    openText.setTextColor(ViewUtils.getColor(R.color.white))
                }
            }
            codeContainer.addView(openText)
            val params = openText.layoutParams as LinearLayout.LayoutParams
            if (it == 0 || it == 1 || it == 2) {
                params.height = ViewUtils.dp2px(40)
                params.width = ViewUtils.dp2px(45)
            } else {
                val size = (ViewUtils.getScreenWidth(context) - ViewUtils.dp2px(155)) / 7
                params.height = size
                params.width = size
                if (it == 3) params.leftMargin = ViewUtils.dp2px(20) else params.leftMargin = ViewUtils.dp2px(10)
            }
            openText.layoutParams = params
        }
    }


    // ===== 添加 香港彩(六合彩) 总和 开奖数据   =====
    fun specialLotterySum(context: Context, codeContainer: LinearLayout, result: List<String>) {
        codeContainer.removeAllViews()
        codeContainer.gravity = Gravity.CENTER_VERTICAL
        val sum = result[0].toInt() + result[1].toInt() + result[2].toInt() + result[3].toInt() + result[4].toInt() + result[5].toInt() + result[6].toInt()
        repeat(4) {
            val openText = TextView(context)
            openText.gravity = Gravity.CENTER
            openText.typeface = Typeface.DEFAULT_BOLD
            openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            when (it) {
                0 -> {
                    openText.text = sum.toString()
                    openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                }
                1 -> {
                    if (sum < 175) {
                        openText.text = "小"
                        openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                    } else {
                        openText.text = "大"
                        openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                    }
                }
                2 -> {
                    if (sum % 2 == 0) {
                        openText.text = "双"
                        openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                    } else {
                        openText.text = "单"
                        openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                    }
                }
                3 -> {
                    //  0-红 1-蓝 2-绿 3-和
                    when (LotteryComposeUtil.getSumColor(result)) {
                        0 -> {
                            openText.text = "红"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                        }
                        1 -> {
                            openText.text = "蓝"
                            openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                        }
                        2 -> {
                            openText.text = "绿"
                            openText.setTextColor(ViewUtils.getColor(R.color.colorGreen))
                        }
                        3 -> {
                            openText.text = "和"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_333333))
                        }
                    }
                }
            }
            codeContainer.addView(openText)
            val params = openText.layoutParams as LinearLayout.LayoutParams
            params.height = ViewUtils.dp2px(35)
            params.width = ViewUtils.dp2px(35)
            if (it != 0) params.leftMargin = ViewUtils.dp2px(20) else params.leftMargin = ViewUtils.dp2px(10)
            openText.layoutParams = params
        }
    }

    // ===== 添加 香港彩(六合彩) 特码 开奖数据   =====
    fun lotterySpecialCode(context: Context, codeContainer: LinearLayout, result: List<String>) {
        codeContainer.removeAllViews()
        codeContainer.gravity = Gravity.CENTER_VERTICAL
        val specialCode = result[6].toInt()
        repeat(5) {
            val openText = TextView(context)
            openText.gravity = Gravity.CENTER
            openText.typeface = Typeface.DEFAULT_BOLD
            openText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            when (it) {
                0 -> {
                    when {
                        specialCode == 49 -> {
                            openText.text = "和"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_333333))
                        }
                        specialCode % 2 == 0 -> {
                            openText.text = "双"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                        }
                        else -> {
                            openText.text = "单"
                            openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                        }
                    }
                }
                1 -> {
                    when {
                        specialCode == 49 -> {
                            openText.text = "和"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_333333))
                        }
                        specialCode < 25 -> {
                            openText.text = "小"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                        }
                        specialCode in 25..48 -> {
                            openText.text = "大"
                            openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                        }
                    }
                }
                2 -> {
                    val a = result[6].toInt() % 10
                    val b = ((result[6].toInt() - a) / 10) % 10
                    when {
                        result[6].toInt() == 49 -> {
                            openText.text = "和局"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_333333))
                        }
                        (a + b) % 2 == 0 -> {
                            openText.text = "合双"
                            openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                        }
                        else -> {
                            openText.text = "合单"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                        }
                    }
                }
                3 -> {
                    val a = result[6].toInt() % 10
                    val b = ((result[6].toInt() - a) / 10) % 10
                    when {
                        result[6].toInt() == 49 -> {
                            openText.text = "和局"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_333333))
                        }
                        (a + b) in 1..6 -> {
                            openText.text = "合小"
                            openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                        }
                        (a + b) in 7..12 -> {
                            openText.text = "合大"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                        }
                    }
                }
                4 -> {
                    val a = result[6].toInt() % 10
                    when {
                        result[6].toInt() == 49 -> {
                            openText.text = "和局"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_333333))
                        }
                        a in 0..4 -> {
                            openText.text = "尾小"
                            openText.setTextColor(ViewUtils.getColor(R.color.colorBlue))
                        }
                        a in 5..9 -> {
                            openText.text = "尾大"
                            openText.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
                        }
                    }
                }
            }
            codeContainer.addView(openText)
            val params = openText.layoutParams as LinearLayout.LayoutParams
            params.height = ViewUtils.dp2px(35)
            params.width = ViewUtils.dp2px(35)
            if (it != 0) params.leftMargin = ViewUtils.dp2px(20) else params.leftMargin = ViewUtils.dp2px(10)
            openText.layoutParams = params
        }
    }
}