package com.fenghuang.caipiaobao.ui.login

import android.graphics.Color
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.fenghuang.baselib.base.mvp.BaseMvpActivity
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.login.data.LoginApi
import com.fenghuang.caipiaobao.ui.login.data.LoginInfoResponse
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.dialog.LoginWebDialog
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.activity_login.*


/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/26- 14:07
 * @ Describe
 *
 */

class LoginActivity : BaseMvpActivity<LoginPresenter>() {


    var isReport = true //是否必须填邀请码

    var loadMode = 0 //  0-验证码登录  1-密码登录

    var registerMode = 0 // 0-注册 1-登录

    var isGetIdentify = false //是否获取过登录验证码

    var isGetRegisterIdentify = false //是否获取过登录验证码

    private var isGetSingCode = false

    override val layoutResID = R.layout.activity_login

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LoginPresenter()

    override fun isOverride() = true

    override fun isStatusBarTranslate() = false

    override fun isRegisterRxBus() = true


    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(this, true)
        val spannableString = SpannableString("登录或注册即表示您同意《乐购直播协议》")
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#5393f8")), 11, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvLoginDescription.text = spannableString


        if (intent != null) {

            if (intent.getIntExtra("dialogLogin", 0) == 1) {
                loginType()
            } else if (intent.getIntExtra("dialogLogin", 0) == 2) {
                loginOrRegister()
            }
        }
        if (isReport) {
            setVisible(etRegisterInviteNum)
            setVisible(linLast2)
        } else {
            setVisibility(etRegisterInviteNum, false)
            setVisibility(linLast2, false)
        }
    }


    override fun initEvent() {
        tvForgetWithMode.setOnClickListener {
            LaunchUtils.startCustomer(this)
        }
        tvChangeInWithMode.setOnClickListener {
            LaunchUtils.startCustomer(this)
        }
        tvLContactCustomer.setOnClickListener {
            LaunchUtils.startCustomer(this)
        }
        imgLoginClose.setOnClickListener {
            finish()
        }
        //协议
        tvLoginDescription.setOnClickListener {
            LoginWebDialog(getContext()!!).show()

        }

        // 登录-----
        tvLoginIn.setOnClickListener {

            if (etLoginPhone.text.isEmpty()) {
                ToastUtils.show("请输入手机号")
                return@setOnClickListener
            }
            if (etLoginPhone.text.length < 11) {
                ToastUtils.show("请输入正确11位手机号码")
                return@setOnClickListener
            }
            if (loadMode == 0) {
                if (etLoginPassWord.text.isEmpty()) {
                    ToastUtils.show("请输入密码")
                    return@setOnClickListener
                }
                if (etLoginPassWord.text.length < 6) {
                    ToastUtils.show("密码长度不得小于6位")
                    return@setOnClickListener
                }
            } else {
                if (!isGetIdentify) {
                    ToastUtils.show("请先获取验证码")
                    return@setOnClickListener
                }
                if (etLoginPassWord.text.isEmpty() || etLoginPassWord.text.length < 4) {
                    ToastUtils.show("请输入4位验证码")
                    return@setOnClickListener
                }
            }
            showPageLoadingDialog("登录中..")
            if (loadMode == 0) {
                mPresenter.userLoginWithPassWord(etLoginPhone.text.toString(), etLoginPassWord.text.toString(), "1")
            } else mPresenter.userLoginWithIdentify(etLoginPhone.text.toString(), etLoginPassWord.text.toString(), 1)
        }

        tvLoginWithMode.setOnClickListener {
            etLoginPassWord.setText("")
            loginType()

        }
        tvForgetPassWord.setOnClickListener {
            if (loadMode == 0) {
                //忘记密码
                setVisible(containerForget)
                setGone(containerLoginRegister)
                setGone(containerLogin)
                registerMode = 1
                tvRegister.text = "立即登录"
            } else {
                if (etLoginPhone.text.isEmpty()) {
                    ToastUtils.show("请输入手机号")
                    return@setOnClickListener
                }
                if (etLoginPhone.text.length < 11) {
                    ToastUtils.show("请输入正确11位手机号码")
                    return@setOnClickListener
                }
                mPresenter.userGetCode(tvForgetPassWord, etLoginPhone.text.toString(), "login")
            }
        }

        //登录Or注册
        tvRegister.setOnClickListener {
            loginOrRegister()
        }
        //注册--验证码---
        tvRegisterIdentify.setOnClickListener {
            if (etRegisterPhone.text.isEmpty()) {
                ToastUtils.show("请输入手机号")
                return@setOnClickListener
            }
            if (etRegisterPhone.text.length < 11) {
                ToastUtils.show("请输入正确11位手机号码")
                return@setOnClickListener
            }
            mPresenter.userGetCode(tvRegisterIdentify, etRegisterPhone.text.toString(), "reg")
        }

        tvRegisterIn.setOnClickListener {
            if (etRegisterPhone.text.isEmpty()) {
                ToastUtils.show("请输入手机号")
                return@setOnClickListener
            }
            if (etRegisterPhone.text.length < 11) {
                ToastUtils.show("请输入正确11位手机号码")
                return@setOnClickListener
            }
            if (!isGetRegisterIdentify) {
                ToastUtils.show("请先获取验证码")
                return@setOnClickListener
            }
            if (etRegisterIdentify.text.isEmpty() || etRegisterIdentify.text.length < 4) {
                ToastUtils.show("请输入4位验证码")
                return@setOnClickListener
            }
            if (etRegisterPassWord.text.isEmpty()) {
                ToastUtils.show("请输入密码")
                return@setOnClickListener
            }
            if (etRegisterPassWord.text.length < 6) {
                ToastUtils.show("密码长度不得小于6位")
                return@setOnClickListener
            }
            if (isReport) {
                if (etRegisterInviteNum.text.isEmpty()) {
                    ToastUtils.show("请输入邀请码")
                    return@setOnClickListener
                }
            }
            if (isReport) {
                mPresenter.checkMarkCode(etRegisterPhone.text.toString(), etRegisterIdentify.text.toString(), etRegisterInviteNum.text.toString())
            } else {
                mPresenter.userRegister(etRegisterPhone.text.toString(), etRegisterIdentify.text.toString(), etRegisterPassWord.text.toString(), "1")
            }
        }

        /**
         * 忘记密码
         */
        tvForget.setOnClickListener {
            if (isMobileNumber(etForgetPhone.text.toString())) {
                LoginApi.userGetCode(etForgetPhone.text.toString(), "retrieve_pwd") {
                    onSuccess {
                        isGetSingCode = true
                        mPresenter.time(tvForget)
                    }
                    onFailed { ToastUtils.showError(it.getMsg()) }
                }
            } else ToastUtils.showError("请输入正确11位手机号码")
        }

        tvForgetIn.setOnClickListener {
            if (isMobileNumber(etForgetPhone.text.toString())) {
                if (isGetSingCode) {
                    if (!TextUtils.isEmpty(etForgetPassWord.text.toString())) {
                        setGone(containerForget)
                        setVisible(containerChange)
                    } else ToastUtils.showError("请输入验证码")
                } else ToastUtils.showError("请获取验证码")
            } else ToastUtils.showError("请输入正确11位手机号码")
        }
        /**
         * 找回登录密码
         */
        tvChangeIn.setOnClickListener {
            if (TextUtils.isEmpty(etChange1.text.toString())) {
                ToastUtils.showWarning("请填写新密码")
                return@setOnClickListener
            }
            if (etChange1.text.length < 6) {
                ToastUtils.showWarning("密码长度最少为6位")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(etChange2.text.toString())) {
                ToastUtils.showWarning("请确认新密码")
                return@setOnClickListener
            }
            if (etChange1.text.toString() != etChange2.text.toString()) {
                ToastUtils.showWarning("两次新密码输入不一致")
                return@setOnClickListener
            }
            mPresenter.getPass(etForgetPhone.text.toString(), etForgetPassWord.text.toString(), etChange1.text.toString())
        }
    }


    //登录方式
    private fun loginType() {
        if (loadMode == 0) {
            loadMode = 1
            etLoginPassWord.hint = getString(R.string.login_profile)
            tvForgetPassWord.text = getString(R.string.login_get_profile)
            etLoginPassWord.inputType = InputType.TYPE_CLASS_NUMBER
            //选中显示密码
            etLoginPassWord.transformationMethod = HideReturnsTransformationMethod.getInstance()
            tvForgetPassWord.setTextColor(ContextCompat.getColor(this, R.color.color_FF513E))
            tvLoginWithMode.text = "使用账号密码登录"
        } else {
            loadMode = 0
            etLoginPassWord.hint = getString(R.string.login_password)
            tvForgetPassWord.text = getString(R.string.login_forget_password)
            etLoginPassWord.inputType = InputType.TYPE_CLASS_TEXT
            //隐藏密码
            etLoginPassWord.transformationMethod = PasswordTransformationMethod.getInstance()
            tvForgetPassWord.setTextColor(ContextCompat.getColor(this, R.color.color_999999))
            tvLoginWithMode.text = "使用验证码登录"
        }
    }

    //登录Or注册
    private fun loginOrRegister() {
        if (registerMode == 0) {
            registerMode = 1
            tvRegister.text = "立即登录"
            setGone(containerLogin)
            setVisible(containerLoginRegister)
            setGone(containerForget)
        } else {
            registerMode = 0
            tvRegister.text = "立即注册"
            setGone(containerLoginRegister)
            setVisible(containerLogin)
            setGone(containerForget)
        }
    }


    fun setUserInfo(result: LoginInfoResponse) {
        RxBus.get().post(result)
        result.user_id?.let { UserInfoSp.putUserId(it) }
        UserInfoSp.putUserUniqueId(result.unique_id)
        UserInfoSp.putUserPhone(result.phone)
        UserInfoSp.putUserSex(result.gender)
        UserInfoSp.putUserNickName(result.nickname)
        UserInfoSp.putUserName(result.username)
        UserInfoSp.putUserProfile(result.profile)
        UserInfoSp.putUserPhoto(result.avatar)
        UserInfoSp.putIsLogin(true)//更新登录状态
        UserInfoSp.putUserFans(result.following + "," + result.followers + "," + result.like)
    }

    /**
     * 验证手机号码是否合法
     * 176, 177, 178;
     * 180, 181, 182, 183, 184, 185, 186, 187, 188, 189;
     * 145, 147;
     * 130, 131, 132, 133, 134, 135, 136, 137, 138, 139;
     * 150, 151, 152, 153, 155, 156, 157, 158, 159;
     *
     * "13"代表前两位为数字13,
     * "[0-9]"代表第二位可以为0-9中的一个,
     * "[^4]" 代表除了4
     * "\\d{8}"代表后面是可以是0～9的数字, 有8位。
     */
    private fun isMobileNumber(mobiles: String): Boolean {
        val telRegex = "^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$"
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex.toRegex())
    }
}