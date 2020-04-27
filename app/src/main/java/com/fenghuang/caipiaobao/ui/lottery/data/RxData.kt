package com.fenghuang.caipiaobao.ui.lottery.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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
data class LotteryBetAccess(var result: ArrayList<LotteryBet>, var totalCount: Int, var totalMoney: Int,var lotteryID:String,var issue:String,var diamond:String)

//重置
data class LotteryReset(var reset: Boolean)


//重置
data class LotteryResetDiamond(var reset: Boolean)

//钻石不足
data class LotteryDiamondNotEnough(var reset: Boolean)

