package com.fenghuang.caipiaobao.ui.lottery.data

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

data class LotteryJumpToLive(var lotteryId:String)

//投注
data class LotteryBet(var result:PlaySecData)


