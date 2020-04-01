package com.fenghuang.caipiaobao.data.api

import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV_Main
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV_OTHER
import com.fenghuang.caipiaobao.data.api.ApiConstant.API_URL_DEV_OTHER_TEST
import com.fenghuang.caipiaobao.data.api.ApiConstant.isTest
import com.pingerx.rxnetgo.RxNetGo

/**
 * 网络请求基类
 */
interface BaseApi {


    /**
     * 获取URL  admin
     */
    fun getBaseUrl(): String {
        return API_URL_DEV_Main
    }

    /**
     * 获取URL  Me
     */
    fun getBaseUrlMe(): String {
        return API_URL_DEV_OTHER
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
        return RxNetGo.getInstance().getRetrofitService("http://47.75.130.69:18308")
    }

    /**
     * 开奖
     */
    fun getApiOpenLottery(): RxNetGo {
        return RxNetGo.getInstance().getRetrofitService("http://156.227.88.24:18306")
    }

    /**
     *  其他的BaseUrl 是否是测试库
     */
    fun getApiOtherTest(): String {
        return if (isTest) {
            "forum"
        } else ""

    }

    /**
     *  其他的User 是否是测试库
     */
    fun getApiOtherUserTest(): String {
        return if (isTest) {
            "userinfo"
        } else ""
    }

    /**
     *  其他的User 是否是测试库
     */
    fun getApiOtherUserTestIs(): String {
        return if (isTest) {
            "/userinfo"
        } else ""
    }

}