package com.fenghuang.caipiaobao.ui.lottery.constant

import android.annotation.SuppressLint
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryCodeTrendResponse
import com.fenghuang.caipiaobao.widget.trendview.TrendViewHeadType
import com.fenghuang.caipiaobao.widget.trendview.TrendViewType
import com.google.gson.JsonArray


/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/3- 18:55
 * @ Describe
 *
 */
object LotteryComposeUtil {


    //  总和/形态 算法
    fun lotterySumAndType(result: List<String>): List<String> {
        val data = arrayListOf<Int>()
        for (it in result) {
            data.add(it.toInt())
        }
        //从小到大排序
        data.sort()
        //循环3次拿到 前 中 后
        val str = arrayListOf<String>()
        repeat(3) {
            val arr = data.subList(it, it + 3)
            if (arr[0] == arr[1] && arr[1] == arr[2]) {
                str.add("豹子")     //豹子
            } else if ((arr[0] + 1) == arr[1] && (arr[1] + 1) == arr[2]) {
                str.add("顺子")   //顺子
            } else if (arr[0] == arr[1] || arr[1] == arr[2]) {
                str.add("对子")   //对子
            } else if ((arr[0] + 1) == arr[1] || (arr[1] + 1) == arr[2]) {
                str.add("半顺")   //半顺
            } else str.add("杂六")    //杂六
        }
        return str
    }

    //  总和/形态 背景色
    fun getLotterySumAndTypeBack(str: String): Int {
        return when (str) {
            "豹子" -> ViewUtils.getColor(R.color.colorBlue)
            "顺子" -> ViewUtils.getColor(R.color.colorBlue)
            "对子" -> ViewUtils.getColor(R.color.color_FF513E)
            "半顺" -> ViewUtils.getColor(R.color.colorBlue)
            "杂六" -> ViewUtils.getColor(R.color.colorBlue)
            else -> ViewUtils.getColor(R.color.colorBlue)
        }
    }


    //计算总和
    fun lotterySum(result: List<String>): String {
        var lotterySum = 0
        val data = arrayListOf<Int>()
        for (it in result) {
            data.add(it.toInt())
        }
        for (sum in data) {
            lotterySum += sum
        }
        return lotterySum.toString()
    }


    //计算龙虎
    fun dragonOrTiger(result: List<String>): String {
        return when {
            result[0].toInt() > result.last().toInt() -> "龙"
            result[0].toInt() < result.last().toInt() -> "虎"
            else -> "和"
        }
    }

    //计算总和色波
    fun getSumColor(result: List<String>): Int {
        var red = 0.0
        var blue = 0.0
        var green = 0.0
        repeat(7) {
            when (it) {
                6 -> {
                    when (getCodeColor(result[it].toInt())) {
                        0 -> red += 1.5
                        1 -> blue += 1.5
                        2 -> green += 1.5
                    }
                }
                else -> {
                    when (getCodeColor(result[it].toInt())) {
                        0 -> red += 1
                        1 -> blue += 1
                        2 -> green += 1
                    }
                }
            }
        }
        //先判断是不是 和局
        if (red == 1.5 && green == 3.0 && blue == 3.0) return 3
        if (green == 1.5 && red == 3.0 && blue == 3.0) return 3
        if (blue == 1.5 && red == 3.0 && blue == 3.0) return 3

        var max = if (red > blue) red else blue
        max = if (max > green) max else green
        return when (max) {
            red -> 0
            blue -> 1
            else -> 2
        }
    }


    //查询是 红 绿 蓝 哪个值  0-红 1-蓝 2-绿
    private fun getCodeColor(code: Int): Int {
        return if (code == 1 || code == 2 || code == 7 ||
                code == 8 || code == 12 || code == 13 ||
                code == 18 || code == 19 || code == 23 ||
                code == 24 || code == 29 || code == 30 ||
                code == 34 || code == 35 || code == 40 ||
                code == 45 || code == 46)
            0
        else if (code == 3 || code == 4 || code == 9 ||
                code == 10 || code == 14 || code == 15 ||
                code == 20 || code == 25 || code == 26 ||
                code == 31 || code == 36 || code == 37 ||
                code == 41 || code == 42 || code == 47 ||
                code == 48)
            1
        else
            2
    }

    //----------------------------露珠------------------------------------------------------------------
    @SuppressLint("SetTextI18n")
    fun initTitle(textView: TextView, typeTextView: TextView, lotteryID: String, type: String, total: JsonArray?, position: Int) {

        if (total != null) {
            when (type) {
                LotteryConstant.TYPE_LUZHU_2, LotteryConstant.TYPE_LUZHU_15 -> {
                    val da = if (total[position].asJsonObject.get("da") == null) "0" else total[position].asJsonObject.get("da").asString
                    val xiao = if (total[position].asJsonObject.get("xiao") == null) "0" else total[position].asJsonObject.get("xiao").asString
                    if (lotteryID == "8") textView.text = "累计：大 ($da)    小 ($xiao)"
                    else textView.text = "今日：大 ($da)    小 ($xiao)"
                }
                LotteryConstant.TYPE_LUZHU_3, LotteryConstant.TYPE_LUZHU_16 -> {
                    val dan = if (total[position].asJsonObject.get("dan") == null) "0" else total[position].asJsonObject.get("dan").asString
                    val shuang = if (total[position].asJsonObject.get("shuang") == null) "0" else total[position].asJsonObject.get("shuang").asString
                    if (lotteryID == "8") textView.text = "累计：单 ($dan)    双 ($shuang)"
                    else textView.text = "今日：单 ($dan)    双 ($shuang)"
                }
                LotteryConstant.TYPE_LUZHU_5 -> {
                    when (position) {
                        0 -> {
                            val da = if (total[position].asJsonObject.get("da") == null) "0" else total[position].asJsonObject.get("da").asString
                            val xiao = if (total[position].asJsonObject.get("xiao") == null) "0" else total[position].asJsonObject.get("xiao").asString
                            if (lotteryID == "8") textView.text = "累计：大 ($da)    小 ($xiao)"
                            else textView.text = "今日：大 ($da)    小 ($xiao)"
                        }
                        1 -> {
                            val dan = if (total[position].asJsonObject.get("dan") == null) "0" else total[position].asJsonObject.get("dan").asString
                            val shuang = if (total[position].asJsonObject.get("shuang") == null) "0" else total[position].asJsonObject.get("shuang").asString
                            if (lotteryID == "8") textView.text = "累计：单 ($dan)    双 ($shuang)"
                            else textView.text = "今日：单 ($dan)    双 ($shuang)"
                        }
                    }
                }
                LotteryConstant.TYPE_LUZHU_8 -> {
                    val long = if (total[position].asJsonObject.get("long") == null) "0" else total[position].asJsonObject.get("long").asString
                    val hu = if (total[position].asJsonObject.get("hu") == null) "0" else total[position].asJsonObject.get("hu").asString
                    when (lotteryID) {
                        "8" -> textView.text = "累计：龙 ($long)    虎 ($hu)"
                        "10","1" -> {
                            val he = if (total[position].asJsonObject.get("he") == null) "0" else total[position].asJsonObject.get("he").asString
                            textView.text = "今日：龙 ($long)    虎 ($hu)    和 ($he)"
                        }
                        else -> textView.text = "今日：龙 ($long)    虎 ($hu)"
                    }
                }
                LotteryConstant.TYPE_LUZHU_9 -> {
                    val qian = if (total[position].asJsonObject.get("qian") == null) "0" else total[position].asJsonObject.get("qian").asString
                    val hou = if (total[position].asJsonObject.get("hou") == null) "0" else total[position].asJsonObject.get("hou").asString
                    if (lotteryID == "8") textView.text = "累计：前 ($qian)    后 ($hou)"
                    else textView.text = "今日：前 ($qian)    后 ($hou)"
                }
                LotteryConstant.TYPE_LUZHU_10 -> {
                    when (position) {
                        0 -> {
                            val da = if (total[position].asJsonObject.get("da") == null) "0" else total[position].asJsonObject.get("da").asString
                            val xiao = if (total[position].asJsonObject.get("xiao") == null) "0" else total[position].asJsonObject.get("xiao").asString
                            if (lotteryID == "8") textView.text = "累计：大 ($da)    小 ($xiao)"
                            else textView.text = "今日：大 ($da)    小 ($xiao)"
                        }
                        1 -> {
                            val dan = if (total[position].asJsonObject.get("dan") == null) "0" else total[position].asJsonObject.get("dan").asString
                            val shuang = if (total[position].asJsonObject.get("shuang") == null) "0" else total[position].asJsonObject.get("shuang").asString
                            if (lotteryID == "8") textView.text = "累计：单 ($dan)    双 ($shuang)"
                            else textView.text = "今日：单 ($dan)    双 ($shuang)"
                        }
                    }
                }
                LotteryConstant.TYPE_LUZHU_11 -> {
                    val zonglai = if (total[position].asJsonObject.get("_1") == null) "0" else total[position].asJsonObject.get("_1").asString
                    val meilai = if (total[position].asJsonObject.get("_0") == null) "0" else total[position].asJsonObject.get("_0").asString
                    if (lotteryID == "8") textView.text = "累计：总来 ($zonglai)    没来 ($meilai)"
                    else textView.text = "今日：总来 ($zonglai)    没来 ($meilai)"
                }
                LotteryConstant.TYPE_LUZHU_12 -> {
                    val hong = if (total[position].asJsonObject.get("hong") == null) "0" else total[position].asJsonObject.get("hong").asString
                    val lv = if (total[position].asJsonObject.get("lv") == null) "0" else total[position].asJsonObject.get("lv").asString
                    val lan = if (total[position].asJsonObject.get("lan") == null) "0" else total[position].asJsonObject.get("lan").asString
                    if (lotteryID == "8") textView.text = "累计：红 ($hong)    绿 ($lv)    蓝 ($lan)"
                    else textView.text = "今日：红 ($hong)    绿 ($lv)    蓝 ($lan)"
                }
            }
        }


        typeTextView.text = when (lotteryID) {
            "8" -> {
                if (type != LotteryConstant.TYPE_LUZHU_5) {
                    if (position != 6) "正码" + getNumChinese(position) else "特码"
                } else "总和"

            }
            "1", "10" -> {
                when (type) {
                    LotteryConstant.TYPE_LUZHU_5 -> "总 和"
                    LotteryConstant.TYPE_LUZHU_8 -> "龙 虎"
                    LotteryConstant.TYPE_LUZHU_11 -> "号 码 $position"
                    else -> "第" + getNumChinese(position) + "球"
                }

            }
            else -> {
                when (type) {
                    LotteryConstant.TYPE_LUZHU_9 -> "第" + (position + 1) + "名"
                    LotteryConstant.TYPE_LUZHU_10 -> "亚冠和"
                    else -> when (position) {
                        0 -> "冠军"
                        1 -> "亚军"
                        else -> "第" + getNumChinese(position) + "名"
                    }
                }
            }
        }
    }

    private fun getNumChinese(num: Int): String {
        return when (num) {
            0 -> "一"
            1 -> "二"
            2 -> "三"
            3 -> "四"
            4 -> "五"
            5 -> "六"
            6 -> "七"
            7 -> "八"
            8 -> "九"
            else -> "十"
        }
    }

    //----------------------------走势------------------------------------------------------------------
    //号码走势
    fun getNumTrendMap(type: String, data: List<LotteryCodeTrendResponse>?, dataFrom: List<LotteryCodeTrendResponse>?, trendViewHead: TrendViewHeadType?, trendViewContent: TrendViewType?, trendContainer: LinearLayout?, loadingContainer: LinearLayout?) {

        if (data != null && !data.isNullOrEmpty()) {

            val list: MutableList<MutableMap<String, Any>> = ArrayList()
            when (type) {
                LotteryConstant.TYPE_17 -> {
                    for (it in data) {
                        val map = mutableMapOf<String, Any>()
                        map["red"] = it.trending!!.indexOf(0)
                        map["date"] = it.issue
                        map["num0"] = it.trending.indexOf(0) + 1
                        if (it.trending.isNotEmpty()) {
                            map["num1"] = it.trending[0]
                            map["num2"] = it.trending[1]
                            map["num3"] = it.trending[2]
                            map["num4"] = it.trending[3]
                            map["num5"] = it.trending[4]
                            map["num6"] = it.trending[5]
                            map["num7"] = it.trending[6]
                            map["num8"] = it.trending[7]
                            map["num9"] = it.trending[8]
                            map["num10"] = it.trending[9]
                            list.add(map)
                        }
                    }
                }
                LotteryConstant.TYPE_18 -> {
                    for (it in data) {
                        val map = mutableMapOf<String, Any>()
                        if (it.trending!!.isEmpty()) return
                        map["red"] = it.trending.indexOf(0)
                        map["date"] = it.issue.substring(4, it.issue.length)
                        map["num0"] = it.trending.indexOf(0)
                        map["num1"] = it.trending[1]
                        map["num2"] = it.trending[2]
                        map["num3"] = it.trending[3]
                        map["num4"] = it.trending[4]
                        map["num5"] = it.trending[5]
                        map["num6"] = it.trending[6]
                        map["num7"] = it.trending[7]
                        map["num8"] = it.trending[8]
                        map["num9"] = it.trending[9]
                        map["num10"] = it.trending[10]
                        map["num11"] = it.trending[11]
                        map["num12"] = it.trending[12]
                        map["num13"] = it.trending[13]
                        map["num14"] = it.trending[14]
                        map["num15"] = it.trending[15]
                        map["num16"] = it.trending[16]
                        list.add(map)
                    }
                }
                LotteryConstant.TYPE_19 -> {
                    for (it in data) {
                        val map = mutableMapOf<String, Any>()
                        if (it.trending!!.isEmpty()) return
                        map["red"] = it.trending.indexOf(0)
                        map["date"] = it.issue
                        map["num0"] = it.trending.indexOf(0)
                        map["num1"] = it.trending[0]
                        map["num2"] = it.trending[1]
                        map["num3"] = it.trending[2]
                        map["num4"] = it.trending[3]
                        map["num5"] = it.trending[4]
                        map["num6"] = it.trending[5]
                        map["num7"] = it.trending[6]
                        map["num8"] = it.trending[7]
                        map["num9"] = it.trending[8]
                        map["num10"] = it.trending[9]
                        list.add(map)
                    }
                }
                LotteryConstant.TYPE_20, "前三形态", "中三形态", "后三形态" -> {
                    if (dataFrom != null) {
                        for ((index, it) in data.withIndex()) {
                            if (it.trending!!.isEmpty()) return
                            if (dataFrom[index].trending!!.isEmpty()) return
                            val map = mutableMapOf<String, Any>()
                            map["red"] = it.trending.indexOf(0)
                            map["blue"] = dataFrom[index].trending!!.indexOf(0)
                            map["date"] = it.issue
                            map["num0"] = it.open_code.split(",")[0]
                            map["num1"] = it.open_code.split(",")[1]
                            map["num2"] = it.open_code.split(",")[2]
                            map["num3"] = it.trending[0]
                            map["num4"] = it.trending[1]
                            map["num5"] = it.trending[2]
                            map["num6"] = it.trending[3]
                            map["num7"] = it.trending[4]
                            map["num8"] = dataFrom[index].trending!![0]
                            map["num9"] = dataFrom[index].trending!![1]
                            map["num10"] = dataFrom[index].trending!![2]
                            list.add(map)
                        }
                    }
                }
                LotteryConstant.TYPE_21 -> {
                    for ((index, it) in data.withIndex()) {
                        if (it.trending!!.isEmpty()) return
                        if (data[index].trending!!.isEmpty()) return
                        val map = mutableMapOf<String, Any>()
                        map["red"] = it.trending.indexOf(0)
                        map["date"] = it.issue
                        map["num0"] = it.open_code
                        map["num1"] = it.trending[0]
                        map["num2"] = it.trending[1]
                        map["num3"] = it.trending[2]
                        list.add(map)
                    }
                }
            }
            if (trendContainer?.isVisible == true) ViewUtils.setGone(trendContainer)
            if (list.size > 0) {
                trendViewContent?.upDateData(list, type)
                trendViewHead?.setType(type)
                trendViewHead?.requestLayout()
                trendViewContent?.requestLayout()
            }
            //全部绘制完成再显示
            trendViewContent?.viewTreeObserver?.addOnGlobalLayoutListener {
                ViewUtils.setVisible(trendContainer)
                ViewUtils.setGone(loadingContainer)
            }
        }
    }
}