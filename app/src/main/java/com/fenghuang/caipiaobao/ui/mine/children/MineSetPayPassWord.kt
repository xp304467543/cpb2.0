package com.fenghuang.caipiaobao.ui.mine.children

import android.text.Editable
import android.text.TextWatcher
import com.fenghuang.baselib.base.mvp.BaseMvpActivity
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.LoginOut
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_pay_pass_word.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-27
 * @ Describe 支付密码设置
 *
 */

class MineSetPayPassWord : BaseMvpActivity<MineSetPayPassWordPresenter>() {


//    // 0x1 支付密码设置

    var firstPassWord = ""
    var newPassWord = ""
    var loadMode: Int = 1

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MineSetPayPassWordPresenter()

    override fun getContentResID() = R.layout.fragment_pay_pass_word

    override fun isShowBackIconWhite() = false

    override fun initContentView() {

        loadMode = intent.getIntExtra("loadMode", 1)

        if (loadMode == 1) {
            setPageTitle("支付密码设置")
            tvTitlePass.text = "请设置支付密码"
        } else {
            setPageTitle("支付密码修改")
            tvTitlePass.text = "请输入旧密码"
        }
    }

    override fun initEvent() {
        edit_pay_solid.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 6) {
                    if (loadMode != 1) {
                        when {
                            tvTitlePass.text == "请输入旧密码" -> {
                                firstPassWord = edit_pay_solid.text.toString()
                                mPresenter.verifyPass()
                            }
                            tvTitlePass.text == "请输入新密码" -> {
                                if (edit_pay_solid.text.toString() != firstPassWord) {
                                    newPassWord = edit_pay_solid.text.toString()
                                    edit_pay_solid.clearText()
                                    tvTitlePass.text = "请确认新密码"
                                } else {
                                    edit_pay_solid.clearText()
                                    ToastUtils.showInfo("新密码不能与旧密码相同")
                                }
                            }
                            tvTitlePass.text == "请确认新密码" -> {
                                if (newPassWord == edit_pay_solid.text.toString()) {
                                    mPresenter.setPassWord(firstPassWord)
                                } else {
                                    edit_pay_solid.clearText()
                                    ToastUtils.showInfo("两次密码输入不一致")
                                }

                            }
                        }
                    } else {
                        if (firstPassWord == "") {
                            firstPassWord = p0.toString()
                            edit_pay_solid.clearText()
                            tvTitlePass.text = "请确认支付密码"
                        } else {
                            if (firstPassWord == p0.toString()) {
                                mPresenter.setPassWord("")
                            } else tvSetError.text = "两次密码输入不一致"
                        }
                    }

                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tvSetError.text = ""
            }
        })
    }



}
