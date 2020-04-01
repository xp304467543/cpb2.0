package com.fenghuang.caipiaobao.widget.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.playerlibrary.AlivcLiveRoom.ScreenUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.data.LoginOut
import com.fenghuang.caipiaobao.ui.login.LoginActivity
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.dialog_login_tips.*
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.WindowManager


/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-21
 * @ Describe 未登录提示
 *
 */

class LoginTipsDialog(context: Context, val horizontal: Boolean) : Dialog(context) {


    init {
        window!!.setWindowAnimations(R.style.BaseDialogAnim)
        setContentView(R.layout.dialog_login_tips)
        val lp = window!!.attributes
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        lp.height = ViewUtils.dp2px(330)
        window!!.attributes = lp
        initEvent()
    }

    private fun initEvent() {
        imgDialogLoginClose.setOnClickListener {
            dismiss()
        }
        btDialogLogin.setOnClickListener {
            login(1)
        }
        btDialogRegister.setOnClickListener {
            login(2)
        }
    }


    private fun login(mode: Int) {
        dismiss()
        if (!isPort()){
            scanForActivity(context)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            setFullScreen(scanForActivity(context),false)
        }
        val intent = Intent(context, LoginActivity::class.java)
        when (mode) {
            2 -> {
                intent.putExtra("dialogLogin", 2)
            }
        }
        context.startActivity(intent)
    }


    override fun dismiss() {
        super.dismiss()
        RxBus.get().post(LoginOut(true))
        if (horizontal) {
            val decorView = ScreenUtils.getDecorView(scanForActivity(context))
            if (decorView != null) ScreenUtils.hideSysBar(scanForActivity(context), decorView)
        }
    }

    private fun scanForActivity(cont: Context?): Activity? {
        return when (cont) {
            null -> null
            is Activity -> cont
            is ContextWrapper -> scanForActivity(cont.baseContext)
            else -> null
        }

    }

    private fun isPort(): Boolean {
        return ViewUtils.getContext().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun setFullScreen(activity: Activity?, isFull: Boolean) {
        val orientation = if (isFull) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        activity?.requestedOrientation = orientation
    }

}