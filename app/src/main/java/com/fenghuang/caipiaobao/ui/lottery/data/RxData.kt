package com.fenghuang.caipiaobao.ui.lottery.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/2- 17:17
 * @ Describe
 *
 */

data class LotteryTypeSelect(var lotteryId: String?)

data class LotteryNewCode(var lotteryCodeNewResponse: LotteryCodeNewResponse?)

data class LotteryExpertPlay(var lotteryCodeNewResponse: LotteryCodeNewResponse?)

data class LotteryJumpToLive(var lotteryId: String)


@Parcelize
data class LotteryBet(var result: PlaySecData,var playName:String) : Parcelable

//投注确认
data class LotteryBetAccess(var result: ArrayList<LotteryBet>, var totalCount: Int,
                            var totalMoney: Int,var lotteryID:String,var issue:String,
                            var diamond:String,var lotteryName:String,var lotteryNameType:String,
                            var isFollow:Boolean =false,var followUserId:String = "0",var isBalanceBet:String = "1",var totalBalance:String = "0")

//重置
data class LotteryReset(var reset: Boolean)

//当前选择的
data class LotteryCurrent(var name: String,var size:Int)

//重置
data class LotteryResetDiamond(var reset: Boolean)

//钻石不足
data class LotteryDiamondNotEnough(var reset: Boolean)

//分享注单
data class LotteryShareBet(var reset: Boolean,var order: JSONObject)


