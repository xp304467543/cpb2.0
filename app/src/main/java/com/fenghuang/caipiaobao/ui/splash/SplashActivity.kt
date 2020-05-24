package com.fenghuang.caipiaobao.ui.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.SpUtils
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.main.MainActivity
import com.fenghuang.caipiaobao.utils.FastClickUtils
import kotlinx.android.synthetic.main.activity_splash.*
import me.jessyan.autosize.internal.CancelAdapt


/**
 *
 * 开屏页面，启动时会有图片背景，不需要使用适配
 */
@SuppressLint("SetTextI18n")
class SplashActivity : Activity(), CancelAdapt {
    // ===== 倒计时 =====
    var timer: CountDownTimer? = null

    var isTurn = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        StatusBarUtils.setStatusBarByFlags(this)
        initContent()
    }

    private fun initContent() {
        tvDaoJiShi.setOnClickListener {
            if (FastClickUtils.isFastClick1000()) {
                isTurn = false
                timer?.cancel()
                startActivity(Intent(baseContext, MainActivity::class.java))
                finish()
            }
        }
        timer = object : CountDownTimer(4000, 1000) {
            override fun onFinish() {
                if (isTurn) {
                    startActivity(Intent(baseContext, MainActivity::class.java))
                    finish()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                //文字显示3秒跳转
                tvDaoJiShi.text = (millisUntilFinished / 1000).toString() + "秒跳转"
            }
        }
        initSome()
    }

    private fun initSome() {
        HomeApi.getLotteryUrl {
            onSuccess {
                UserInfoSp.putCustomer(it.customer)
                ImageManager.loadImg(it.app_start_banner?.image_url, startImg)
                timer?.start()
            }
            onFailed { }
        }

    }
}