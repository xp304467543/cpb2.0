package com.fenghuang.caipiaobao.data.api

import com.fenghuang.caipiaobao.data.api.ApiConstant.API_LOTTERY_BET_MAIN
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_LOTTERY_BET_MAIN_S
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_LOTTERY_BET_TEST
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_MOMENTS_MAIN
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_MOMENTS_MAIN_S
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_MOMENTS_TEST
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV_Main
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV_Main_S
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV_OTHER
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV_OTHER_S
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
        } else {
            if (API_URL_DEV_Main_S.isNullOrEmpty()) {
                API_URL_DEV_Main
            } else "$API_URL_DEV_Main_S/"
        }
    }

    /**
     * 获取URL  bill
     */
    fun getBaseUrlMe(): String {
        return if (isTest) {
            API_URL_DEV_OTHER_TEST
        } else {
            if (API_URL_DEV_OTHER_S.isNullOrEmpty()) {
                API_URL_DEV_OTHER
            } else "$API_URL_DEV_OTHER_S/"
        }
    }

    fun getLotteryBet(): String {
        return if (isTest) {
            API_LOTTERY_BET_TEST
        } else {
            if (API_LOTTERY_BET_MAIN_S.isNullOrEmpty()) {
                API_LOTTERY_BET_MAIN
            }else "$API_LOTTERY_BET_MAIN_S/"
        }

    }

    /**
     * 圈子
     */
    fun getBaseUrlMoments(): String {
        return if (isTest) {
            API_MOMENTS_TEST
        } else {
            if (API_MOMENTS_MAIN_S.isNullOrEmpty()) {
                API_MOMENTS_MAIN
            }else "$API_MOMENTS_MAIN_S/"
        }
    }

    fun getBase64Key(): String {
        return if (isTest) {
            TEST_KEY
        } else MAIN_KEY
    }

    /**
     * 服务器地址获取
     */
    fun getSystemApi(): RxNetGo {
        return RxNetGo.getInstance().getRetrofitService("https://www.lgadmin561.com/")
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
//    fun getApiOtherTest(): String = "forum"


    /**
     *  其他的User 是否是测试库
     */
//    fun getApiOtherUserTest(): String = "userinfo"


}