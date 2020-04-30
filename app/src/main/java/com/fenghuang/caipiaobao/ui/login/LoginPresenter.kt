package com.fenghuang.caipiaobao.ui.login

import android.os.Looper
import android.widget.TextView
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.data.api.BaseApi
import com.fenghuang.caipiaobao.data.bean.BaseApiBean
import com.fenghuang.caipiaobao.ui.login.data.*
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.utils.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.Response
import java.io.IOException

/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/26- 14:08
 * @ Describe
 *
 */

class LoginPresenter : BaseMvpPresenter<LoginActivity>(), BaseApi {


    /**
     * 倒计时
     */

    fun time(textView: TextView) {
        val mCountDownTimerUtils = CountDownTimerUtils(textView, 120000, 1000)
        mCountDownTimerUtils.start()
    }


    /**
     * 获取验证码
     */

    fun userGetCode(textView: TextView, phone: String, type: String) {
        LoginApi.userGetCode(phone, type) {
            if (mView.isActive()) {
                onSuccess {
                    time(textView)
                    ToastUtils.show("验证码已发送")
                    if (type == "login") mView.isGetIdentify = true
                    if (type == "reg") mView.isGetRegisterIdentify = true
                }
                onFailed {
                    ToastUtils.showError(it.getMsg())
                }
            }
        }
    }

    /**
     * 密码登录
     */
    fun userLoginWithPassWord(phone: String, passWord: String, loadMode: String) {
//        LoginApi.userLoginWithPassWord(phone, passWord, loadMode) {
//            if (mView.isActive()) {
//                onSuccess {
//                    it.data?.asJsonObject?.get("user_type")?.asString?.let { it1 -> UserInfoSp.setUserType(it1) }
//                    val str = AESUtils.decrypt(getBase64Key(), it.encryption)
//                    val res = str?.let { it1 -> JsonUtils.fromJson(it1, LoginResponse::class.java) }
//                    res?.let { result ->
//                        UserInfoSp.putToken(result.token)
//                        getLoginInfo("Bearer ${result.token}")
//                        UserInfoSp.putRandomStr(result.random_str)
//                    }
//                }
//                onFailed {
//                    mView.hidePageLoadingDialog()
//                    ToastUtils.showError(it.getMsg())
//                    mView.hidePageLoadingDialog()
//                }
//            }
//        }
        val map = hashMapOf<String, Any>()
        map["username"] = phone
        map["password"] = passWord
        map["mode"] = loadMode
        map["client_type"] = 3
        map["ip"] = IpUtils.getIPAddress(ViewUtils.getContext())
        AESUtils.encrypt(LoginApi.getBase64Key(), Gson().toJson(map))?.let {
            val param = HashMap<String, String>()
            param["datas"] = it
            HttpClient.getInstance(mView).post(MineApi.getBaseUrlMe() + "/" + LoginApi.getApiOtherUserTest() + LoginApi.LOGIN, param, object : HttpClient.MyCallback {
                override fun failed(e: IOException?) {

                }

                override fun success(res: Response?) {
                    if (mView.isActive()) {
                        Looper.prepare()
                        val bean = res?.body()?.string()?.let { it1 -> JsonUtils.fromJson(it1, BaseApiBean::class.java) }
                        if (bean?.code == 1) {
                            bean.data?.asJsonObject?.get("user_type")?.asString?.let { it1 -> UserInfoSp.setUserType(it1) }
                            val str = AESUtils.decrypt(getBase64Key(), bean.encryption)
                            val res = str?.let { it1 -> JsonUtils.fromJson(it1, LoginResponse::class.java) }
                            res?.let { result ->
                                UserInfoSp.putToken(result.token)
                                getLoginInfo("Bearer ${result.token}")
                                UserInfoSp.putRandomStr(result.random_str)
                            }
                        } else {
                            ToastUtils.show(bean?.msg.toString())
                            mView.hidePageLoadingDialog()
                        }
                        Looper.loop()
                    }
                }
            })
        }
    }

    /**
     * 验证码登录
     */
    fun userLoginWithIdentify(phoneNum: String, captcha: String, isAutoLogin: Int) {
//        LoginApi.userLoginWithIdentify(phone, code, isAutoLogin) {
//            if (mView.isActive()) {
//                onSuccess {
//                    it.data?.asJsonObject?.get("user_type")?.asString?.let { it1 -> UserInfoSp.setUserType(it1) }
//                    val str = AESUtils.decrypt(getBase64Key(), it.encryption)
//                    val res = str?.let { it1 -> JsonUtils.fromJson(it1, LoginResponse::class.java) }
//                    res?.let { result ->
//                        UserInfoSp.putToken(result.token)
//                        getLoginInfo("Bearer ${result.token}")
//                        UserInfoSp.putRandomStr(result.random_str)
//                    }
//                }
//
//                onFailed {
//                    mView.hidePageLoadingDialog()
//                    ToastUtils.showError(it.getMsg())
//                }
//            }
//        }
        if (mView.isActive()) {
            val map = hashMapOf<String, Any>()
            map["phone"] = phoneNum
            map["captcha"] = captcha
            map["mode"] = 3
            map["client_type"] = 3
            map["ip"] = IpUtils.getIPAddress(ViewUtils.getContext())
            map["is_auto_login"] = isAutoLogin
            AESUtils.encrypt(LoginApi.getBase64Key(), Gson().toJson(map))?.let {
                val param = HashMap<String, String>()
                param["datas"] = it
                HttpClient.getInstance(mView).post(MineApi.getBaseUrlMe() + "/" + LoginApi.getApiOtherUserTest() + LoginApi.LOGIN, param, object : HttpClient.MyCallback {
                    override fun failed(e: IOException?) {}
                    override fun success(res: Response?) {
                        if (mView.isActive()) {
                            Looper.prepare()
                            val op = res?.body()?.string()
                            LogUtils.e("-------?????----???--->" + op)
                            val bean = op?.let { it1 -> JsonUtils.fromJson(it1, BaseApiBean::class.java) }
                            if (bean?.code == 1) {
                                bean.data?.asJsonObject?.get("user_type")?.asString?.let { it1 -> UserInfoSp.setUserType(it1) }
                                val str = AESUtils.decrypt(getBase64Key(), bean.encryption)
                                val last = str?.let { it1 -> JsonUtils.fromJson(it1, LoginResponse::class.java) }
                                last?.let { result ->
                                    UserInfoSp.putToken(result.token)
                                    getLoginInfo("Bearer ${result.token}")
                                    UserInfoSp.putRandomStr(result.random_str)
                                }
                            } else {
                                ToastUtils.show(bean?.msg.toString())
                                mView.hidePageLoadingDialog()
                            }
                            Looper.loop()
                        }
                    }
                })
            }
        }
    }

    /**
     * 登录成功信息获取
     */
    private fun getLoginInfo(token: String, isReg: Boolean = false) {
        LoginApi.getLoginInfo(token) {
            if (mView.isActive()) {
                onSuccess {
                    mView.hidePageLoadingDialog()
                    mView.setUserInfo(it)
                    if (isReg) RxBus.get().post(RegisterSuccess(true))
                    RxBus.get().post(LoginSuccess(""))
                    mView.finish()
                }
                onFailed {
                    mView.hidePageLoadingDialog()
                    ToastUtils.showError(it.getMsg())
                }
            }
        }
    }


    /**
     * 注册
     */
    fun userRegister(phone: String, code: String, password: String, is_auto_login: String) {
        if (mView.isActive()) {
            val map = hashMapOf<String, Any>()
            map["phone"] = phone
            map["password"] = password
            map["captcha"] = code
            map["mode"] = 3
            map["client_type"] = 3
            map["ip"] = IpUtils.getIPAddress(ViewUtils.getContext())
            map["is_auto_login"] = is_auto_login
            AESUtils.encrypt(LoginApi.getBase64Key(), Gson().toJson(map))?.let {
                val param = HashMap<String, String>()
                param["datas"] = it
                HttpClient.getInstance(mView).post(MineApi.getBaseUrlMe() + "/" + LoginApi.getApiOtherUserTest() + LoginApi.REGISTER, param, object : HttpClient.MyCallback {
                    override fun failed(e: IOException?) {
                    }
                    override fun success(res: Response?) {
                        Looper.prepare()
                        val op = res?.body()?.string()
                        LogUtils.e("-------?????----???--->" + op)
                        val bean = op?.let { it1 -> JsonUtils.fromJson(it1, BaseApiBean::class.java) }
                        if (bean?.code == 1) {
                            bean.data?.asJsonObject?.get("user_type")?.asString?.let { it1 -> UserInfoSp.setUserType(it1) }
                            val str = AESUtils.decrypt(getBase64Key(), bean.encryption)
                            val last = str?.let { it1 -> JsonUtils.fromJson(it1, LoginResponse::class.java) }
                            last?.let { result ->
                                UserInfoSp.putToken(result.token)
                                getLoginInfo("Bearer ${result.token}", true)
                            }
                        } else {
                            ToastUtils.show(bean?.msg.toString())
                            mView.hidePageLoadingDialog()
                        }
                        Looper.loop()
                    }
                })
            }
        }

//        LoginApi.userRegister(phone, code, password, is_auto_login) {
//            if (mView.isActive()) {
//                onSuccess {
//                    UserInfoSp.putToken(it.token)
//                    getLoginInfo("Bearer ${it.token}", true)
//                }
//                onFailed {
//                    mView.hidePageLoadingDialog()
//                    ToastUtils.showError(it.getMsg())
//                }
//            }
//        }
    }

    /**
     * 找回密码
     */
    fun getPass(phone: String, captcha: String, new_pwd: String) {
        LoginApi.getLoginPass(phone, captcha, new_pwd) {
            if (mView.isActive()) {
                onSuccess {
                    mView.etChange1.setText("")
                    mView.etChange2.setText("")
                    mView.etForgetPassWord.setText("")
                    mView.etForgetPhone.setText("")
                    mView.registerMode = 0
                    mView.tvRegister.text = "立即注册"
                    mView.setVisible(mView.containerLogin)
                    mView.setGone(mView.containerLoginRegister)
                    mView.setGone(mView.containerForget)
                    mView.setGone(mView.containerChange)

                }
                onFailed { ToastUtils.show(it.getMsg().toString()) }
            }
        }
    }
}
