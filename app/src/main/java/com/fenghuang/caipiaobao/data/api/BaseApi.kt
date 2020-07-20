package com.fenghuang.caipiaobao.data.api

import com.fenghuang.caipiaobao.data.api.ApiConstant.API_LOTTERY_BET_MAIN
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_LOTTERY_BET_TEST
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_MOMENTS_MAIN
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_MOMENTS_TEST
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV_Main
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV_OTHER
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV_OTHER_TEST
import com.fenghuang.caipiaobao.data.api.ApiConstant.MAIN_KEY
import com.fenghuang.caipiaobao.data.api.ApiConstant.TEST_KEY
import com.fenghuang.caipiaobao.data.api.ApiConstant.isTest
import com.pingerx.rxnetgo.RxNetGo

/**
 * 网络请求基类
 */
interface BaseApi {


    /**
     * 获取URL  davis
     */
    fun getBaseUrl(): String {
        return if (isTest) {
            API_URL_DEV
        } else API_URL_DEV_Main
    }

    /**
     * 获取URL  bill
     */
    fun getBaseUrlMe(): String {
        return if (isTest) {
            API_URL_DEV_OTHER_TEST
        } else API_URL_DEV_OTHER

    }

    fun getLotteryBet():String{
        return if (isTest) {
            API_LOTTERY_BET_TEST
        } else API_LOTTERY_BET_MAIN
    }

    /**
     * 圈子
     */
    fun getBaseUrlMoments(): String {
        return if (isTest) {
            API_MOMENTS_TEST
        } else API_MOMENTS_MAIN
    }

    fun getBase64Key():String{
        return if (isTest) {
            TEST_KEY
        } else MAIN_KEY
    }


    /**
     * 获取默认的Service
     * 需要子类绑定URL
     */
    fun getApi(): RxNetGo {
        return RxNetGo.getInstance().getRetrofitService(getBaseUrl())
    }

    /**
     * 登录其他的BaseUrl
     */
    fun getApiOther(): RxNetGo {
        return RxNetGo.getInstance().getRetrofitService(getBaseUrlMe())
    }


    /**
     * 圈子Api
     */
    fun getApiLottery(): RxNetGo {
        return RxNetGo.getInstance().getRetrofitService(getBaseUrlMoments())
    }

    /**
     * 开奖
     */
    fun getApiOpenLottery(): RxNetGo {
        return RxNetGo.getInstance().getRetrofitService(getLotteryBet())
    }

    /**
     *  其他的BaseUrl 是否是测试库
     */
    fun getApiOtherTest(): String = "forum"


    /**
     *  其他的User 是否是测试库
     */
    fun getApiOtherUserTest(): String = "userinfo"


}