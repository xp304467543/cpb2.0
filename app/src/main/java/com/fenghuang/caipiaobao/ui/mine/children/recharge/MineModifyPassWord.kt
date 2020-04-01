package com.fenghuang.caipiaobao.ui.mine.children.recharge
import android.text.TextUtils
import android.widget.TextView
import com.fenghuang.baselib.base.fragment.BaseNavFragment
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.home.data.LoginOut
import com.fenghuang.caipiaobao.ui.login.LoginActivity
import com.fenghuang.caipiaobao.ui.login.data.LoginApi
import com.fenghuang.caipiaobao.ui.mine.data.MineApi.modifyPassWord
import com.fenghuang.caipiaobao.ui.mine.data.MinePassWordTime
import com.fenghuang.caipiaobao.utils.*
import com.fenghuang.caipiaobao.widget.dialog.SuccessDialog
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.fragment_modify_pass_word.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-18
 * @ Describe 登陆密码修改
 *
 */

class MineModifyPassWord : BaseNavFragment() {

    private var isGetSingCode = false

    override fun getPageTitle() = "密码修改"

    override fun getContentResID() = R.layout.fragment_modify_pass_word

    override fun isSwipeBackEnable() = true

    override fun isShowBackIconWhite() = false


    override fun initEvent() {
        modifyByPhone.setOnClickListener {
            setGone(mode_1)
            setVisible(mode_2)
        }
        modifyByPass.setOnClickListener {
            setGone(mode_2)
            setVisible(mode_1)
        }



        btModify.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                if (TextUtils.isEmpty(tvNewPassOld.text.toString())) {
                    ToastUtils.showWarning("请填写旧密码")
                    return@setOnClickListener
                }
                if (tvNewPassOld.text.length < 6) {
                    ToastUtils.showWarning("密码长度最少为6位")
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(tvNewPass1.text.toString())) {
                    ToastUtils.showWarning("请填写新密码")
                    return@setOnClickListener
                }
                if (tvNewPass1.text.length < 6) {
                    ToastUtils.showWarning("密码长度最少为6位")
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(tvNewPass2.text.toString())) {
                    ToastUtils.showWarning("请确认新密码")
                    return@setOnClickListener
                }
                if (tvNewPass2.text.length < 6) {
                    ToastUtils.showWarning("密码长度最少为6位")
                    return@setOnClickListener
                }
                if (tvNewPassOld.text.toString() == tvNewPass2.text.toString() || tvNewPassOld.text.toString() == tvNewPass1.text.toString()) {
                    ToastUtils.showWarning("新密码与旧密码不能相同")
                    return@setOnClickListener
                }
                if (tvNewPass1.text.toString() == tvNewPass2.text.toString()) {
                    showPageLoadingDialog()
                    modifyPassWord(tvNewPassOld.text.toString(), tvNewPass1.text.toString()) {
                        onSuccess {
                            hidePageLoadingDialog()
                            val dialog = SuccessDialog(getPageActivity(), "修改成功", R.mipmap.ic_dialog_success)
                            dialog.setOnDismissListener {
                                GlobalDialog.spClear()
                                RxBus.get().post(LoginOut(true))
                                pop()
                                LaunchUtils.startActivity(getPageActivity(), LoginActivity::class.java)
                            }
                            dialog.show()
                        }
                        onFailed {
                            if (it.getCode() == 1002) {
                                ToastUtils.showError(it.getMsg().toString() + "," + "您还有" + JsonUtils.fromJson(it.getDataCode().toString(), MinePassWordTime::class.java).remain_times.toString() + "次机会")
                            } else ToastUtils.showError(it.getMsg())
                            hidePageLoadingDialog()
                        }
                    }
                } else ToastUtils.showNormal("两次新密码输入不一致")
            }
        }

        /**
         * 手机验证码修改
         */
        tvGetIdentifyCodePass.setOnClickListener {
            if (isMobileNumber(etPhonePass.text.toString())) {
                if (UserInfoSp.getUserPhone() == etPhonePass.text.toString()) {
                    LoginApi.userGetCode(etPhonePass.text.toString(), "chg_pwd") {
                        onSuccess {
                            time(tvGetIdentifyCodePass)
                            isGetSingCode = true
                        }
                        onFailed { ToastUtils.showError(it.getMsg()) }
                    }
                } else ToastUtils.showError("该手机号与当前用户不匹配")
            } else ToastUtils.showError("请输入正确11位手机号码")

        }

        btNext.setOnClickListener {
            if (!TextUtils.isEmpty(etPhonePass.text)) {
                if (UserInfoSp.getUserPhone() == etPhonePass.text.toString()) {
                    if (isGetSingCode) {
                        if (!TextUtils.isEmpty(etPhoneSignNum.text)) {
                                setGone(mode_1)
                                setGone(mode_2)
                                setVisible(mode_3)
                        } else ToastUtils.showError("请输验证码")
                    } else ToastUtils.showError("请先获取验证码")
                } else ToastUtils.showError("请确认手机号是否正确")
            } else ToastUtils.showError("请输手机号")
        }


        btSetPassWord.setOnClickListener {

            if (TextUtils.isEmpty(etPhoneNewPass11.text.toString())) {
                ToastUtils.showWarning("请输入新密码")
                return@setOnClickListener
            }
            if (etPhoneNewPass11.text.length < 6) {
                ToastUtils.showWarning("密码长度最少为6位")
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(etNewPass11Sure.text.toString())) {
                ToastUtils.showWarning("请输入新密码")
                return@setOnClickListener
            }
            if (etNewPass11Sure.text.length < 6) {
                ToastUtils.showWarning("密码长度最少为6位")
                return@setOnClickListener
            }
            if (etNewPass11Sure.text.toString() != etPhoneNewPass11.text.toString()) {
                ToastUtils.showWarning("两次新密码输入不一致")
                return@setOnClickListener
            }
            showPageLoadingDialog()
            modifyPassWord(etPhonePass.text.toString(),etPhoneSignNum.text.toString(), etPhoneNewPass11.text.toString(), 1) {
                onSuccess {
                    val dialog = SuccessDialog(getPageActivity(), "修改成功", R.mipmap.ic_dialog_success)
                    dialog.setOnDismissListener {
                        GlobalDialog.spClear()
                        RxBus.get().post(LoginOut(true))
                        pop()
                        LaunchUtils.startActivity(getPageActivity(), LoginActivity::class.java)
                    }
                    dialog.show()
                    hidePageLoadingDialog()
                }
                onFailed {
                    ToastUtils.showError("修改失败:${it.getMsg()}")
                    hidePageLoadingDialog()
                }
            }
        }
    }




    /**
     * 倒计时
     */

    fun time(textView: TextView) {
        val mCountDownTimerUtils = CountDownTimerUtils(textView, 120000, 1000)
        mCountDownTimerUtils.start()
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