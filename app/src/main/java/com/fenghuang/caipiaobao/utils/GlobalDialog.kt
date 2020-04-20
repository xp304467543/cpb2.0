package com.fenghuang.caipiaobao.utils

import android.content.Context
import android.content.Intent
import com.fenghuang.baselib.utils.SpUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.mine.children.MineSetPayPassWord
import com.fenghuang.caipiaobao.widget.dialog.GlobalTipsDialog
import com.fenghuang.caipiaobao.widget.dialog.LoginTipsDialog
import com.pingerx.rxnetgo.exception.ApiException
import android.app.Activity
import com.fenghuang.caipiaobao.ui.home.data.HomeJumpToMine
import com.fenghuang.caipiaobao.widget.dialog.ReChargeDialog
import com.hwangjr.rxbus.RxBus


/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-19
 * @ Describe
 *
 */

object GlobalDialog {

    private var loginTipsDialog: LoginTipsDialog? = null

    //未登录
    fun notLogged(context: Activity, horizontal: Boolean = false) {
        loginTipsDialog = LoginTipsDialog(context, horizontal)
        spClear()
        if (!loginTipsDialog?.isShowing!!) {
            if (context.isFinishing) {
                return
            }
            loginTipsDialog?.show()
        }
    }

    //未设置支付密码
    fun noSetPassWord(context: Context) {
        val dialog = GlobalTipsDialog(context, "您暂未设置支付密码", "去设置", "我知道了", "")
        dialog.setConfirmClickListener {
            context.startActivity(Intent(context, MineSetPayPassWord::class.java))
            dialog.dismiss()
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }
    //余额不足
    private fun noMoney(activity: Activity,isHor:Boolean){
        val dialog = ReChargeDialog(activity,isHor)
        dialog.setOnSendClickListener {
            activity.finish()
            RxBus.get().post(HomeJumpToMine(true))
        }
        dialog.show()
    }


    //清除所有Sp保存的值，除去已经显示过的guide
    fun spClear() {
        val one = UserInfoSp.getMainGuide()
        val second = UserInfoSp.getOpenCodeGuide()
        val third = UserInfoSp.getMineGuide()
        val four = UserInfoSp.getRewardnGuide()
        val five = UserInfoSp.getAttentionGuide()
        val six = UserInfoSp.getLiveeGuide()
        SpUtils.clearAll()
        if (one) UserInfoSp.putMainGuide(true)
        if (second) UserInfoSp.putOpenCodeGuide(true)
        if (third) UserInfoSp.putMineGuide(true)
        if (four) UserInfoSp.putRewardnGuide(true)
        if (five) UserInfoSp.putAttentionGuide(true)
        if (six) UserInfoSp.putOpenWindow(true)
    }

    //对所有未登录处理
    fun showError(context: Activity, error: ApiException, horizontal: Boolean = false) {
        if (error.getCode() == 2001 || error.getCode() == 401 || error.getCode() == 2000 || error.getMsg().toString().contains("请登录")) {
            notLogged(context, horizontal)
        } else if (error.getCode() == 9) {
            noSetPassWord(context)
        }else if (error.getCode() == 2)  {
            noMoney(context,horizontal)
        }else ToastUtils.show(error.getMsg().toString())
    }

}