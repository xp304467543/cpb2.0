package com.fenghuang.caipiaobao.ui.mine.children.recharge

import android.content.Intent
import com.fenghuang.baselib.base.activity.BaseNavActivity
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MineUpDateMoney
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.dialog.ReChargeDialog
import com.fenghuang.caipiaobao.widget.dialog.RechargeSuccessDialog
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.act_card_recharge.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/23
 * @ Describe 卡密充值
 *
 */
class MineCardRechargeAct : BaseNavActivity() {


    override fun getContentResID() = R.layout.act_card_recharge

    override fun isSwipeBackEnable() = true

    override fun getPageTitle() = "卡密充值"

    override fun isShowBackIconWhite() = false

//    override fun isRegisterRxBus() = true


    override fun initEvent() {
        bt_go_recharge.setOnClickListener {
            showPageLoadingDialog()
            if (FastClickUtils.isFastClick()) {
                if (et_code.text.length < 16) {
                    ToastUtils.show("请输入正确16位数账号")
                    return@setOnClickListener
                }
                MineApi.cardRecharge(et_code.text.toString(), et_pass.text.toString()) {
                    onSuccess {
                        et_code.setText("")
                        et_pass.setText("")
                        RechargeSuccessDialog(this@MineCardRechargeAct, "充值成功", R.mipmap.ic_dialog_success).show()
                        RxBus.get().post(MineUpDateMoney("",false, isDiamond = true))
                        hidePageLoadingDialog()
                    }
                    onFailed {
                        RechargeSuccessDialog(this@MineCardRechargeAct, it.getMsg()
                                ?: "充值失败", R.mipmap.recharge_failed).show()
                        hidePageLoadingDialog()
                    }

                }
            }
        }
        linContact.setOnClickListener {
            if (FastClickUtils.isFastClick()){
                startActivity(Intent(this,MineRechargeCardContactAct::class.java))
            }
        }
    }


}