package com.fenghuang.caipiaobao.ui.mine.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-13
 * @ Describe
 *
 */

// 用户信息
data class MineUserInfoResponse(var username: String = "",
                                var nickname: String = "",
                                var profile: String = "",
                                var avatar: String = "",
                                var gender: Int = 0,
                                var phone: String = "",
                                var following: String?, var followers: String?, var like: String?)

// 获取皮肤列表
data class MineThemSkinResponse(var id: String = "", var type: String = "",
                                var cover: String = "", var users: String = "",
                                var name: String = "", var isSelect: Boolean)

//获取皮肤详情
data class MineThemSkinInfoResponse(var id: String = "", var name: String = "",
                                    var type: String = "", var cover: String = "",
                                    var score: String = "", var price: String = "",
                                    var des: String = "", var bg_image: String = "", var images: List<String>? = null)

//获取vip等级
data class MineUserVipType(var vip: String = "0")

//用户余额
data class MineUserBalance(var balance: BigDecimal = BigDecimal(0.00))

//用户钻石
data class MineUserDiamond(var diamond: String)

//密码输入错误此时
data class MinePassWordTime(var remain_times: Int)

//支付通道列表
data class MinePayTypeList(var id: Int, var channels_type: String, var low_money: String,
                           var high_money: String, var icon: String, var apiroute: String, var pay_type: String = "")

//银行卡列表
data class MineBankList(var name: String, var img: String, var code: String)

//更新用户选择的银行卡
data class MineUpDateBank(var isUpdate: Boolean)

//更新余额
data class MineUpDateMoney(var money: String, var isUpdate: Boolean, var isDiamond: Boolean = false)

//Rx存储用户选择的银行卡
data class MineSaveBank(var data: MineUserBankList)

//用户银行卡里列表
data class MineUserBankList(var id: Int, var bank_id: Int, var realname: String, var card_num: String,
                            var province: String, var city: String, var bank_img: String, var bank_name: String)

//支付Url
data class MinePayUrl(var url: String)


//关注 用户、主播 bean
data class MineUserAttentionBean(var id: String = "", var type: String = "", var nickname: String = "", var anchor_id: String = ""
                                 , var user_id: String = "", var live_status: String = "", var lottery_id: String? = "",
                                 var avatar: String = "", var sign: String = "")

//关注 专家 bean
data class MineExpertBean(var id: String = "", var user_id: String = "", var expert_id: String = "", var nickname: String = "",
                          var avatar: String = "", var profile: String = "", var created: String = "")

//余额记录
data class MineBillResponse(val title: String = "", val bean: Array<MineBillBean>)

//投注记录
//data class MineBetRecordResponse(val )

data class MineBillBean(var id: String = "", var date: String = "", var time: String = "", var create_time: String = "",
                        var amount: String = "", var type: String = "", var get_money: String = "", var nickname: String = "", val issue: String?,
                        var giftname: String = "", var gift_num: String = "", var avatar: String = "", var lottery_name: String?, var method_name: String?
                        , var code: String?)

data class MineMessageCenter(var msg_id: String, var msg_type: String, var content: String, var create_time: String, var createtime_txt: String
                             , var media: String, var dynamic_id: String, var apiType: String, var comment_id: String, var userType: String
                             , var nickname: String, var avatar: String) : Serializable

//新消息提醒
data class MineNewMsg(var msgCount: Int, var countList: MineNewBean)
data class MineNewBean(@SerializedName("0") var `_$0`: String, @SerializedName("2") var `_$2`: String, @SerializedName("3") var `_$3`: String)

//换肤
data class ChangeSkin(var id: Int)

//官方群
data class MineGroup(var title: String, var icon: String, var url: String)

//钻石充值联系方式
data class MineRechargeDiamond(val id: String, val name: String, val quota: String, val contact: List<MineRechargeDiamondChild>)
data class MineRechargeDiamondChild(val title: String?, val value: String?, val icon: String?)

//团队报表
data class MineTeamReport(val invitee_num: String?, val recharge_user_num: String?, val exchange: String?, val recharge: String?,
                          val brokerage: String?, val sub_brokerage: String?, val reg_num: String?)

data class MineTeamReportLast(val invitee_num: String?, val recharge: String?, val brokerage: String?)

data class MineGameReport(val amount: String?, val prize: String?, val count: String?, val bl_amount: String?, val bl_prize: String?, val bl_count: String?)

data class MineGameReportInfo(val lottery_id: String?, val lottery_name: String?, val amount: String?, val prize: String?, val count: String?, val lottery_icon: String?)

data class MineReportCode(val market_id: String?, val user_id: String?, val nickname: String?, val market_code: String?
                          , val market_url: String?, val status: String?, val status_cn: String?, val created_at: String?
                          , val reviewed_at: String?, val level_id: String?, val level: String?, val level_name: String?
                          , val next_level: String?, val next_level_name: String?, val next_level_invitee_num: Double?, val next_level_diff: String?
                          , val invitee_num: Double?, val rebate: Double?)

data class MineLevelList(val level_id: String?, val level: String?, val name: String?, val invitee_num: String?
                         , val cost: Double?, val reward: Double?, val rebate: Double?, val sub_rebate: Double?
                         , val created_at: String?)

data class MineVipList(val avatar: String?, val nickname: String?, val created: Long?, val user_id: String?
                       , val level_id: String?, val level: String?, val level_name: String?, val recharge: String?
                       , val exchange: String?, val brokerage: String?, val invitee_num :String?)