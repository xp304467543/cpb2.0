package com.fenghuang.caipiaobao.data.api

/**
 * 全局Api的域名管理，Path放到直接单独的Api文件中
 */
object ApiConstant {

    const val WEB_URL = "https://www.xiaomingsport.com"

    const val WEB_URL_DEV = "https://www.dev.xiaomingsport.com"


    /**
     * 正式
     */
    const val API_URL_DEV_Main = "https://www.cpbadmin.com" //davis

    const val API_URL_DEV_OTHER = "http://47.75.130.69:18306" //bill

    const val API_MOMENTS_MAIN = "http://47.75.130.69:18308"  //圈子
    /**
     * 测试
     */
    const val API_URL_DEV = "http://www.cpbh5.com"

    const val API_URL_DEV_OTHER_TEST = "http://121.127.228.235:18000"

    const val API_MOMENTS_TEST = "http:121.127.228.235:18000"

    const val isTest = true

}