package com.fenghuang.caipiaobao.ui.personal.data

import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAnchorLiveRecordBean
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAnchorLotteryBean
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAnchorTagListBean
import java.io.Serializable

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-01
 * @ Describe
 *
 */


data class UserPageResponse(var gift: List<UserPageGift>? = null, var gender: String = "", var created: String = "",
                            var vip: String = "", var fans: Int?=0, var follow: Int?=0,
                            var all_gift: String = "", var zan: String = "", var profile: String = "",
                            var avatar: String = "", var nickname: String = "", var unique_id: String = "", var is_follow: Boolean)

data class UserPageGift(var gift_name: String = "", var num: String = "", var icon: String = "")

data class AnchorPageInfoBean(var anchor_id: String = "",
                              var avatar: String,
                              var zan: Int = 0,
                              var duration: String = "",
                              var fans: Int = 0,
                              var follow_num: Int = 0,
                              var giftList: List<UserPageGift>,
                              var giftNum: String = "",
                              var isFollow: Boolean,
                              var level: String = "",
                              var liveStatus: String = "",
                              var live_record: List<HomeLiveAnchorLiveRecordBean>,
                              var lottery: List<HomeLiveAnchorLotteryBean>,
                              var nickname: String = "",
                              var sex: String = "",
                              var age: Int = 0,
                              var liveStartTime: Long = 0,
                              var liveEndTime: Long = 0,
                              var sign: String = "",
                              var tagList: List<HomeLiveAnchorTagListBean>) : Serializable


data class ExpertPageInfo(var id: String = "", var nickname: String = "", var avatar: String = "",
                          var profile: String = "", var following: String = "", var followers: String = "",
                          var win_rate: String = "", var profit_rate: String = "", var winning: String = "",
                          var like: String = "", var speciality: String = "", var created: String = "",
                          var win_rate_mul_100: String = "", var profit_rate_mul_100: String = "", var is_followed: String = "",
                          var lottery_id: String = "", var lottery_name: String = "")

data class ExpertPageHistory(var id: String = "", var lottery_id: String = "", var method: String = "", var issue: String = "",
                             var open_code: String = "", var code: String = "", var is_right: String = "", var created: String = "")