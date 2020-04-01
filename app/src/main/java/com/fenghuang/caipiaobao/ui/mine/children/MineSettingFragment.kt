package com.fenghuang.caipiaobao.ui.mine.children

import android.content.Intent
import com.fenghuang.baselib.base.fragment.BaseNavFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.home.data.LoginOut
import com.fenghuang.caipiaobao.ui.mine.children.recharge.MineModifyPassWord
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.dialog.GlobalTipsDialog
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-16
 * @ Describe
 *
 */

class MineSettingFragment : BaseNavFragment() {

    var mode = 0

    override fun getContentResID() = R.layout.fragment_setting

    override fun isOverridePage() = false

    override fun isSwipeBackEnable() = true

    override fun getPageTitle() = getString(R.string.ic_mine_setting)

    override fun isRegisterRxBus() = true

    override fun isShowBackIconWhite() = false

    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
        videoSwitch.isChecked = UserInfoSp.getOpenWindow()
    }

    override fun initData() {
        if (!UserInfoSp.getIsSetPayPassWord()) getIsSetPayPassWord() else tvPayPassWordSet.text = "支付密码修改"
    }

    override fun initEvent() {
        btExitLogin.setOnClickListener {
            val dialog = GlobalTipsDialog(context!!, "是否要退出登录!", "确定", "取消", "")
            dialog.setConfirmClickListener {
                GlobalDialog.spClear()
                RxBus.get().post(LoginOut(true))
                dialog.dismiss()

            }
            dialog.show()
        }

        linSetPayPassWord.setOnClickListener {
            //            LaunchUtils.startFragment(requireContext(), MineSetPayPassWord(mode))
            val intent = Intent(getPageActivity(), MineSetPayPassWord::class.java)
            intent.putExtra("loadMode", mode)
            startActivity(intent)
        }

        setPassWord.setOnClickListener {
            LaunchUtils.startFragment(getPageActivity(), MineModifyPassWord())
        }

        videoSwitch.setOnCheckedChangeListener { _, isChecked ->
            UserInfoSp.putOpenWindow(isChecked)
        }
    }


    //查询是否设置支付密码
    private fun getIsSetPayPassWord() {
        showPageLoadingDialog()
        MineApi.getIsSetPayPass {
            onSuccess {
                hidePageLoadingDialog()
                UserInfoSp.putIsSetPayPassWord(true)
                tvPayPassWordSet.text = "支付密码修改"
            }
            onFailed {
                hidePageLoadingDialog()
                mode = 1
                if (tvPayPassWordNotSet != null) tvPayPassWordNotSet.text = "暂未设置支付密码"
            }
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun LoginOut(eventBean: LoginOut) {
        pop()
    }

}