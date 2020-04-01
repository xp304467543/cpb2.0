package com.fenghuang.caipiaobao.ui.lottery.data

import com.fenghuang.caipiaobao.data.api.AllEmptySubscriber
import com.fenghuang.caipiaobao.data.api.ApiSubscriber
import com.fenghuang.caipiaobao.data.api.BaseApi
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.pingerx.rxnetgo.rxcache.CacheMode

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/1- 13:08
 * @ Describe
 *
 */

object LotteryApi : BaseApi {

    //彩种
    private const val LOTTERY_TYPE = "/idx/sort"
    //最新开奖号
    private const val LOTTERY_NEW_CODE = "/idx/indexNewOne"
    //历史开奖号
    private const val LOTTERY_HISTORY_CODE = "/idx/history"
    //露珠
    private const val LOTTERY_LU_ZHU = "/lottery/dewdrop"
    //走势
    private const val LOTTERY_TREND = "/lottery/trending"
    //专家计划
    private const val LOTTERY_EXPERT_PLAN = "/plan/index"

    /**
     * 获取彩种
     */
    fun getLotteryType(): ApiSubscriber<List<LotteryTypeResponse>> {
        val subscriber = object : ApiSubscriber<List<LotteryTypeResponse>>() {}
        getApiOpenLottery()
                .post<List<LotteryTypeResponse>>(LOTTERY_TYPE)
                .subscribe(subscriber)
        return subscriber
    }

    /**
     * 获取开奖号
     */
    fun getLotteryNewCode(lotteryId: String, function: ApiSubscriber<LotteryCodeNewResponse>.() -> Unit) {
        val subscriber = object : ApiSubscriber<LotteryCodeNewResponse>() {}
        subscriber.function()
        getApiOpenLottery()
                .post<LotteryCodeNewResponse>(LOTTERY_NEW_CODE)
                .cacheMode(CacheMode.NONE)
                .params("lottery_id", lotteryId)
                .subscribe(subscriber)
    }

    /**
     * 获取历史开奖号
     */
    fun getLotteryHistoryCode(lotteryId: String, date: String, page: Int, function: ApiSubscriber<List<LotteryCodeHistoryResponse>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<LotteryCodeHistoryResponse>>() {}
        subscriber.function()
        getApiOpenLottery()
                .post<List<LotteryCodeHistoryResponse>>(LOTTERY_HISTORY_CODE)
                .cacheMode(CacheMode.NONE)
                .params("lottery_id", lotteryId)
                .params("belong_date", date)
                .params("page", page)
                .params("nums", 10)
                .subscribe(subscriber)
    }

    /**
     * 获取 露珠
     */
    fun getLotteryLuZhu(lotteryId: String, type: String, date: String, function: AllEmptySubscriber.() -> Unit) {
        val subscriber = AllEmptySubscriber()
        subscriber.function()
        getApiLottery()
                .get<String>(HomeApi.getApiOtherTest() + LOTTERY_LU_ZHU)
                .cacheMode(CacheMode.NONE)
                .params("lottery_id", lotteryId)
                .params("belong_date", date)
                .params("rows", 20)
                .params("type", type)
                .params("reverse", 1)
                .subscribe(subscriber)
    }

    /**
     * 获取 走势
     */
    fun getLotteryTrend(lotteryId: String, num: String, limit: String, date: String, function: ApiSubscriber<List<LotteryCodeTrendResponse>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<LotteryCodeTrendResponse>>() {}
        subscriber.function()
        getApiLottery()
                .get<List<LotteryCodeTrendResponse>>(HomeApi.getApiOtherTest() + LOTTERY_TREND)
                .cacheMode(CacheMode.NONE)
                .params("lottery_id", lotteryId)
                .params("num", num)
                .params("belong_date", date)
                .params("limit", limit)
                .subscribe(subscriber)
    }
    /**
     * 获取 专家计划
     */
    fun getExpertPlan(lotteryId: String,issue: String,limit: String,page: Int,function: ApiSubscriber<List<LotteryExpertPaleyResponse>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<LotteryExpertPaleyResponse>>() {}
        subscriber.function()
        getApiLottery()
                .get<List<LotteryExpertPaleyResponse>>(HomeApi.getApiOtherTest() + LOTTERY_EXPERT_PLAN)
                .cacheMode(CacheMode.NONE)
                .params("lottery_id", lotteryId)
                .params("issue", issue)
                .params("limit", limit)
                .params("page", page)
                .subscribe(subscriber)
    }
}