package com.fenghuang.caipiaobao.ui.personal.data

import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.data.api.ApiSubscriber
import com.fenghuang.caipiaobao.data.api.BaseApi
import com.pingerx.rxnetgo.rxcache.CacheMode

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-01
 * @ Describe
 *
 */
object PersonalApi : BaseApi {
    //用户个人页
    private const val USER_PAGE = "api/v1_1/user/user_owner/"
    //主播个人页
    private const val ANCHOR_PAGE = "api/v1_1/live/get_anchor_info/"
    //专家个人页
    private const val EXPERT_PAGE = "plan/expert"
    //专家历史
    private const val EXPERT_HISTORY = "plan/history"

    /**
     * 获取用户个人页
     */
    fun getUserPage(follow_user_id: String, function: ApiSubscriber<UserPageResponse>.() -> Unit) {
        val subscriber = object : ApiSubscriber<UserPageResponse>() {}
        subscriber.function()
        getApi()
                .get<UserPageResponse>(USER_PAGE)
                .cacheMode(CacheMode.NONE)
                .params("user_id", UserInfoSp.getUserId())
                .params("follow_user_id", follow_user_id)
                .params("gift_num", "")
                .subscribe(subscriber)
    }



    /**
     * 获取主播个人页
     */
    fun getAnchorPage(anchor_id: String, function: ApiSubscriber<AnchorPageInfoBean>.() -> Unit) {
        val subscriber = object : ApiSubscriber<AnchorPageInfoBean>() {}
        subscriber.function()
        getApi()
                .get<AnchorPageInfoBean>(ANCHOR_PAGE)
                .cacheMode(CacheMode.NONE)
                .params("user_id", UserInfoSp.getUserId())
                .params("anchor_id", anchor_id)
                .params("gift_num", "")
                .subscribe(subscriber)
    }


    /**
     * 专家详情主页
     */
    fun getExpertPage(expert_id:String,lotteryID:String,function: ApiSubscriber<ExpertPageInfo>.() -> Unit){
        val subscriber = object : ApiSubscriber<ExpertPageInfo>() {}
        subscriber.function()
        getApiLottery()
                .get<ExpertPageInfo>( EXPERT_PAGE)
                .cacheMode(CacheMode.NONE)
                .params("user_id", UserInfoSp.getUserId())
                .params("expert_id", expert_id)
                .params("lottery_id", lotteryID)
                .subscribe(subscriber)

    }

    /**
     * 专家详情历史
     */
    fun getExpertPageHistory(expert_id:String,lottery_id:String,limit:String,function: ApiSubscriber<List<ExpertPageHistory>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<ExpertPageHistory>>() {}
        subscriber.function()
        getApiLottery()
                .get<List<ExpertPageHistory>>( EXPERT_HISTORY)
                .cacheMode(CacheMode.NONE)
                .params("user_id", UserInfoSp.getUserId())
                .params("expert_id", expert_id)
                .params("lottery_id", lottery_id)
                .params("limit", limit)
                .subscribe(subscriber)

    }

}