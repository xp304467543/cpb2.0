package com.fenghuang.caipiaobao.ui.lottery.data

import android.os.Parcelable
import com.google.gson.JsonArray
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/1- 13:25
 * @ Describe
 *
 */


data class LotteryTypeResponse(var lottery_id: String = "", var cname: String = "", var logo_url: String = "", var video_url: String? = "")

data class LotteryCodeNewResponse(var lottery_id: String = "", var issue: String? = "", var code: String? = "", var next_lottery_time: String = "", var next_issue: String, var input_time: String? = "", var next_lottery_end_time: Long)

data class LotteryCodeHistoryResponse(var issue: String = "", var code: String = "", var input_time: String = "")

data class LotteryCodeLuZhuResponse(var code: Int = 0, var msg: String = "", var data: List<List<List<String>>>? = null, var total: JsonArray? = null)


data class LotteryCodeTrendResponse(var id: Int = 0, var lottery_id: String = "0", var property_id: String = "0",
                                    var issue: String = "", var open_code: String = "", var created: String = "", val trending: List<Int>? = null)


data class LotteryExpertPaleyResponse(var id: String = "0", var nickname: String = "0", var expert_id: String = "0",
                                      var lottery_id: String = "0", var method: String = "0", var issue: String = "0",
                                      var code: String = "0", var hit_rate: String = "0", var is_right: String = "0",
                                      var created: String = "0", var avatar: String = "0", var profit_rate: String = "0",
                                      var winning: String = "0", var last_10_games: List<String>? = null)


data class LotteryBetRuleResponse(
        var play_rule_type_id: Int? = 0,
        var play_rule_type_name: String? = null,
        var play_rule_type_data: List<PlayRuleTypeDataBean>? = null
) : Serializable

data class PlayRuleTypeDataBean(
        var play_rule_lottery_id: String? = null,
        var play_rule_lottery_name: String? = null,
        var play_rule_content_id: Int? = 0,
        var play_rule_content: String? = null
) : Serializable


/**
 * 玩法列表
 */
@Parcelize
data class LotteryPlayListResponse(
        val play_unit_data: List<PlayUnitData>,
        val play_unit_id: Int,
        val play_unit_name: String
) : Parcelable

@Parcelize
data class PlayUnitData(
        val play_sec_cname: String,
        val play_sec_combo: Int,
        val play_sec_data: List<PlaySecData>,
        val play_sec_id: Int,
        val play_sec_name: String
) : Parcelable

@Parcelize
data class PlaySecData(
        val play_class_cname: String,
        val play_class_id: Int,
        val play_sec_name: String,
        val play_class_name: String,
        val play_odds: Double,
        var playName: String = "",
        var isSelected: Boolean = false,
        var money: String = "0"
) : Parcelable

data class PlayMoneyData(
        val play_sum_id: Int,
        val play_sum_num: Int,
        val play_sum_name: String

)

data class BetBean(
        val play_sec_name: String,
        val play_class_name: String,
        val play_bet_sum: String

)

data class BetShareBean(
        val play_sec_cname: String,
        val play_bet_sum: String,
        val play_class_cname: String,
        val play_class_name: String,
        val play_odds: Double,
        val play_sec_name: String,
        var isShow: Boolean = false

)

data class LotteryBetHistoryResponse(var play_bet_time: Long?, var play_bet_lottery_id: String?, var play_bet_lottery_name: String?,
                                     var play_bet_issue: String?, var play_sec_id: String?, var play_sec_name: String?,
                                     var play_class_name: String?, var play_bet_sum: String?, var play_odds: String?,
                                     var play_bet_score: String?)