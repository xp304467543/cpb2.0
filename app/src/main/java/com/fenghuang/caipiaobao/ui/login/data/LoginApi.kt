package com.fenghuang.caipiaobao.ui.login.data

import android.content.Context
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.data.api.AllEmptySubscriber
import com.fenghuang.caipiaobao.data.api.AllSubscriber
import com.fenghuang.caipiaobao.data.api.ApiSubscriber
import com.fenghuang.caipiaobao.data.api.BaseApi
import com.fenghuang.caipiaobao.data.bean.BaseApiBean
import com.fenghuang.caipiaobao.utils.AESUtils
import com.fenghuang.caipiaobao.utils.HttpClient
import com.fenghuang.caipiaobao.utils.IpUtils
import com.google.gson.Gson
import okhttp3.Response
import java.io.IOException

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-15
 * @ Describe
 *
 */

object LoginApi : BaseApi {

    //登录
     const val LOGIN = "/v2/login"

    //登录信息，登录成功后获取
    private const val LOGIN_INFO = "/index/index"

    //获取验证码
    private const val GET_CODE = "/reg/send-sms"

    //注册
     const val REGISTER = "/v2/reg"

    //首冲
    private const val FIRST_RECHARGE = "/api/v1_1/Recharge/IsFirst"

    //找回登录密码
    private const val GET_LOGIN_PASS = "/home/retrieve-password"

    /**
     * 密码登录
     */
    fun userLoginWithPassWord(userName: String, passWord: String, loadMode: String,context: Context, function: AllSubscriber.() -> Unit) {
        val subscriber = AllSubscriber()
        subscriber.function()
        val map = hashMapOf<String, Any>()
        map["username"] = userName
        map["password"] = passWord
        map["mode"] = loadMode
        map["client_type"] = 3
        map["ip"] = IpUtils.getIPAddress(ViewUtils.getContext())
        AESUtils.encrypt(getBase64Key(), Gson().toJson(map))?.let {
//            getApiOther().post<BaseApiBean>(getApiOtherUserTest() + LOGIN)
//                    .params("datas", it)
//                    .subscribe(subscriber)
            val param = HashMap<String,String>()
            param["datas"] = it
            HttpClient.getInstance(context).post(LoginApi.getApiOtherUserTest() + LOGIN,param,object :HttpClient.MyCallback{
                override fun failed(e: IOException?) {

                }
                override fun success(res: Response?) {

                }

            })
        }
    }

    /**
     * 验证码登录
     */

    fun userLoginWithIdentify(phoneNum: String, captcha: String, isAutoLogin: Int, function: AllSubscriber.() -> Unit) {
        val subscriber = AllSubscriber()
        subscriber.function()
        val map = hashMapOf<String, Any>()
        map["phone"] = phoneNum
        map["captcha"] = captcha
        map["mode"] = 3
        map["client_type"] = 3
        map["ip"] = IpUtils.getIPAddress(ViewUtils.getContext())
        map["is_auto_login"] = isAutoLogin
        AESUtils.encrypt(getBase64Key(), Gson().toJson(map))?.let {
            getApiOther().post<BaseApiBean>(getApiOtherUserTest() + LOGIN)
                    .params("datas", it)
                    .subscribe(subscriber)
        }

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
                .params("client_type", 3)
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