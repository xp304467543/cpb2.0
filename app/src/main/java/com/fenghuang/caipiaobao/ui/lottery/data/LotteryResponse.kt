package com.fenghuang.caipiaobao.ui.lottery.data

import com.google.gson.JsonArray
import java.io.Serializable

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/1- 13:25
 * @ Describe
 *
 */


data class LotteryTypeResponse(var lottery_id: String = "", var cname: String = "", var logo_url: String = "", var video_url: String? = "")

data class LotteryCodeNewResponse(var lottery_id: String = "", var issue: String? = "", var code: String? = "", var next_lottery_time: String = "", var input_time: String? = "", var next_lottery_end_time: Long)

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


data class LotteryBetHistoryResponse(var play_bet_time: Long?, var play_bet_lottery_id: String?, var play_bet_lottery_name: String?,
                                     var play_bet_issue: String?, var play_sec_id: String?, var play_sec_name: String?,
                                     var play_class_name: String?, var play_bet_sum: String?, var play_odds: String?,
                                     var play_bet_score: String? )