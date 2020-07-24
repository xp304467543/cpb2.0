package com.fenghuang.caipiaobao.ui.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.data.api.ApiConstant
import com.fenghuang.caipiaobao.data.api.WebUrlProvider
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.main.MainActivity
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.LaunchUtils
import kotlinx.android.synthetic.main.activity_splash.*
import me.jessyan.autosize.internal.CancelAdapt


/**
 *
 * 开屏页面，启动时会有图片背景，不需要使用适配
 */
@SuppressLint("SetTextI18n")
class SplashActivity : Activity(), CancelAdapt {
    // ===== 倒计时 =====
//    var timer: CountDownTimer? = null

//    var isTurn = true

    var goToURL = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        StatusBarUtils.setStatusBarByFlags(this)
        initContent()
    }

    private fun initContent() {
        btEnter.setOnClickListener {
            if (FastClickUtils.isFastClick1000()) {
                startActivity(Intent(baseContext, MainActivity::class.java))
                finish()
            }
        }
        startImg.setOnClickListener {
            if (goToURL != "") {
                LaunchUtils.starGlobalWeb(this, "", goToURL)
            }
        }
//        tvDaoJiShi.setOnClickListener {
//            if (FastClickUtils.isFastClick1000()) {
//                isTurn = false
//                timer?.cancel()
//                startActivity(Intent(baseContext, MainActivity::class.java))
//                finish()
//            }
//        }
//        timer = object : CountDownTimer(4000, 1000) {
//            override fun onFinish() {
//                if (isTurn) {
//                    startActivity(Intent(baseContext, MainActivity::class.java))
//                    finish()
//                }
//            }
//
//            override fun onTick(millisUntilFinished: Long) {
//                //文字显示3秒跳转
//                tvDaoJiShi.text = (millisUntilFinished / 1000).toString() + "秒跳转"
//            }
//        }
        initSysTemUrl()
        initSome()
    }

    private fun initSysTemUrl() {
        HomeApi.getSystemUrl {
            onSuccess {
                ViewUtils.setGone(btWaite)
                ViewUtils.setVisible(btEnter)
                ApiConstant.API_URL_DEV_Main_S = it.live_api
                ApiConstant.API_URL_DEV_OTHER_S = it.user_api
                ApiConstant.API_MOMENTS_MAIN_S = it.forum_api
                ApiConstant.API_LOTTERY_BET_MAIN_S = it.lottery_api
                WebUrlProvider.ALL_URL_WEB_SOCKET_MAIN_S = it.notice_url
                WebUrlProvider.API_URL_WEB_SOCKET_MAIN_S = it.chat_url
            }

        }
    }


    private fun initSome() {
        HomeApi.getLotteryUrl {
            onSuccess {
                UserInfoSp.putCustomer(it.customer)
                ImageManager.loadImg(it.app_start_banner?.image_url, startImg)
//                timer?.start()
                goToURL = it.app_start_banner?.url ?: ""
            }
            onFailed { }
        }

    }
}