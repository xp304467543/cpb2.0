package com.fenghuang.caipiaobao.ui.login

import android.widget.TextView
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.data.api.BaseApi
import com.fenghuang.caipiaobao.ui.login.data.*
import com.fenghuang.caipiaobao.utils.AESUtils
import com.fenghuang.caipiaobao.utils.CountDownTimerUtils
import com.fenghuang.caipiaobao.utils.JsonUtils
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.activity_login.*

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
        LoginApi.userLoginWithPassWord(phone, passWord, loadMode) {
            if (mView.isActive()) {
                onSuccess {
                    it.data?.asJsonObject?.get("user_type")?.asString?.let { it1 -> UserInfoSp.setUserType(it1) }
                    val str = AESUtils.decrypt(getBase64Key(), it.encryption)
                    val res = str?.let { it1 -> JsonUtils.fromJson(it1, LoginResponse::class.java) }
                    res?.let { result ->
                        UserInfoSp.putToken(result.token)
                        getLoginInfo("Bearer ${result.token}")
                        UserInfoSp.putRandomStr(result.random_str)
                    }
                }
                onFailed {
                    mView.hidePageLoadingDialog()
                    ToastUtils.showError(it.getMsg())
                    mView.hidePageLoadingDialog()
                }
            }
        }
    }


    /**
     * 验证码登录
     */
    fun userLoginWithIdentify(phone: String, code: String, isAutoLogin: Int) {
        LoginApi.userLoginWithIdentify(phone, code, isAutoLogin) {
            if (mView.isActive()) {
                onSuccess {
                    it.data?.asJsonObject?.get("user_type")?.asString?.let { it1 -> UserInfoSp.setUserType(it1) }
                    val str = AESUtils.decrypt(getBase64Key(), it.encryption)
                    val res = str?.let { it1 -> JsonUtils.fromJson(it1, LoginResponse::class.java) }
                    res?.let { result ->
                        UserInfoSp.putToken(result.token)
                        getLoginInfo("Bearer ${result.token}")
                        UserInfoSp.putRandomStr(result.random_str)
                    }
                }

                onFailed {
                    mView.hidePageLoadingDialog()
                    ToastUtils.showError(it.getMsg())
                }
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
        mView.showPageLoadingDialog()
        LoginApi.userRegister(phone, code, password, is_auto_login) {
            if (mView.isActive()) {
                onSuccess {
                    UserInfoSp.putToken(it.token)
                    getLoginInfo("Bearer ${it.token}", true)
                }
                onFailed {
                    mView.hidePageLoadingDialog()
                    ToastUtils.showError(it.getMsg())
                }
            }
        }
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
