package com.fenghuang.caipiaobao.ui.mine.children

import android.annotation.SuppressLint
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MinePassWordTime
import com.fenghuang.caipiaobao.utils.JsonUtils
import com.fenghuang.caipiaobao.widget.dialog.SuccessDialog
import kotlinx.android.synthetic.main.fragment_pay_pass_word.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-27
 * @ Describe
 *
 */

class MineSetPayPassWordPresenter : BaseMvpPresenter<MineSetPayPassWord>() {


    //验证旧密码
    @SuppressLint("SetTextI18n")
    fun verifyPass() {
        mView.showPageLoadingDialog()
        MineApi.verifyPayPass(mView.edit_pay_solid.text.toString()) {
            if (mView.isActive()){
                onSuccess {
                    mView.hidePageLoadingDialog()
                    mView.edit_pay_solid.clearText()
                    mView.tvTitlePass.text = "请输入新密码"
                    mView.tvSetError.text = ""
                }
                onFailed {
                    mView.hidePageLoadingDialog()
                    mView.edit_pay_solid.clearText()
                    if (it.getCode() == 1002) {
                        mView.tvSetError.text = "”旧支付密码错误，您还有" + JsonUtils.fromJson(it.getDataCode().toString(), MinePassWordTime::class.java).remain_times.toString() + "次机会“。"
                    } else mView.tvSetError.text = it.getMsg()
                }
            }
        }
    }


    //修改密码
    fun setPassWord(old:String) {
        mView.showPageLoadingDialog()
        MineApi.getSettingPayPassword(old,  mView.edit_pay_solid.text.toString()) {
            onSuccess {
                mView.hidePageLoadingDialog()
                val dialog = SuccessDialog(mView, "支付密码设置成功", R.mipmap.ic_dialog_success)
                dialog.setOnDismissListener { mView.finish() }
                dialog.show()
            }
            onFailed {
                mView.hidePageLoadingDialog()
                mView.tvSetError.text = it.getMsg()
            }
        }
    }
}