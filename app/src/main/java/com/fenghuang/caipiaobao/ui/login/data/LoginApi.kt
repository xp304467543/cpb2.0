package com.fenghuang.caipiaobao.ui.login.data

import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.data.api.AllEmptySubscriber
import com.fenghuang.caipiaobao.data.api.ApiSubscriber
import com.fenghuang.caipiaobao.data.api.BaseApi
import com.fenghuang.caipiaobao.data.bean.BaseApiBean

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-15
 * @ Describe
 *
 */

object LoginApi : BaseApi {

    //登录
    private const val LOGIN = "/login/index"
    //登录信息，登录成功后获取
    private const val LOGIN_INFO = "/index/index"
    //获取验证码
    private const val GET_CODE = "/reg/send-sms"
    //注册
    private const val REGISTER = "/reg/index"
    //首冲
    private const val FIRST_RECHARGE = "/api/v1/Recharge/IsFirst"
    //找回登录密码
    private const val GET_LOGIN_PASS = "/home/retrieve-password"

    /**
     * 密码登录
     */
    fun userLoginWithPassWord(userName: String, passWord: String, loadMode: String, function: ApiSubscriber<LoginResponse>.() -> Unit) {
        val subscriber = object : ApiSubscriber<LoginResponse>() {}
        subscriber.function()
        getApiOther().post<LoginResponse>(getApiOtherUserTest() + LOGIN)
                .params("username", userName)
                .params("password", passWord)
                .params("mode", loadMode)
                .subscribe(subscriber)
    }

    /**
     * 验证码登录
     */

    fun userLoginWithIdentify(phoneNum: String, captcha: String, isAutoLogin: Int, function: ApiSubscriber<LoginResponse>.() -> Unit) {
        val subscriber = object : ApiSubscriber<LoginResponse>() {}
        subscriber.function()
        getApiOther().post<LoginResponse>(getApiOtherUserTest() + LOGIN)
                .params("phone", phoneNum)
                .params("captcha", captcha)
                .params("mode", 3)
                .params("is_auto_login", isAutoLogin)
                .subscribe(subscriber)
    }

    /**
     * 登录信息，登录成功后获取
     */
    fun getLoginInfo(token: String, function: ApiSubscriber<LoginInfoResponse>.() -> Unit) {
        val subscriber = object : ApiSubscriber<LoginInfoResponse>() {}
        subscriber.function()
        getApiOther().get<LoginInfoResponse>(getApiOtherUserTest() + LOGIN_INFO)
                .headers("Authorization", token)
                .subscribe(subscriber)
    }

    /**
     * 登录后获取是否首冲
     */
    fun getIsFirstRecharge(userID: Int, function: ApiSubscriber<LoginFirstRecharge>.() -> Unit) {
        val subscriber = object : ApiSubscriber<LoginFirstRecharge>() {}
        subscriber.function()
        getApi().post<LoginFirstRecharge>(FIRST_RECHARGE)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", userID)
                .subscribe(subscriber)

    }

    /**
     * 获取验证码
     */
    fun userGetCode(phone: String, type: String, function: ApiSubscriber<RegisterCode>.() -> Unit) {
        val subscriber = object : ApiSubscriber<RegisterCode>() {}
        subscriber.function()
        getApiOther().post<RegisterCode>(getApiOtherUserTest() + GET_CODE)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .params("user_id", UserInfoSp.getUserId())
                .params("phone", phone)
                .params("type", type) //reg默认  login登录验证码
                .subscribe(subscriber)
    }

    /**
     * 注册
     */
    fun userRegister(phone: String, code: String, password: String, is_auto_login: String, function: ApiSubscriber<LoginInfoResponse>.() -> Unit) {
        val subscriber = object : ApiSubscriber<LoginInfoResponse>() {}
        subscriber.function()
        getApiOther().post<LoginInfoResponse>(getApiOtherUserTest() + REGISTER)
                .params("phone", phone)
                .params("password", password)
                .params("captcha", code)
                .params("mode", 3)
                .params("is_auto_login", is_auto_login)
                .subscribe(subscriber)
    }

    /**
     * 找回登录密码
     */
    fun getLoginPass(phone: String, captcha: String, new_pwd: String, function: AllEmptySubscriber.() -> Unit) {
        val subscriber = AllEmptySubscriber()
        subscriber.function()
        getApiOther().post<String>(getApiOtherUserTest() + GET_LOGIN_PASS)
                .params("phone", phone)
                .params("captcha", captcha)
                .params("new_pwd", new_pwd)
                .subscribe(subscriber)
    }
}