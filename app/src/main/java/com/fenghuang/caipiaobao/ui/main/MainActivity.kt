package com.fenghuang.caipiaobao.ui.main

import android.Manifest
import android.os.Bundle
import com.fenghuang.baselib.base.activity.BasePageActivity
import com.fenghuang.baselib.utils.AppUtils
import com.fenghuang.baselib.utils.DebugUtils
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.data.api.WebUrlProvider
import com.fenghuang.caipiaobao.function.doOnIOThread
import com.fenghuang.caipiaobao.helper.DestroyHelper
import com.fenghuang.caipiaobao.helper.RxPermissionHelper
import com.fenghuang.caipiaobao.socket.WsManager
import com.fenghuang.caipiaobao.socket.listener.WsStatusListener
import com.fenghuang.caipiaobao.ui.home.data.AllSocket
import com.fenghuang.caipiaobao.ui.home.data.LoginOut
import com.fenghuang.caipiaobao.ui.login.data.LoginSuccess
import com.fenghuang.caipiaobao.utils.view.PushToast
import com.fenghuang.caipiaobao.utils.view.TitleTextWindow
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import me.jessyan.autosize.utils.LogUtils
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.ByteString
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/25- 16:17
 * @ Describe
 *
 */

class MainActivity : BasePageActivity() {

    var client_id = "-1"


    override fun getPageFragment() = MainFragment()


    override fun isRegisterRxBus() = true


    override fun onBackPressedSupport() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressedSupport()
        } else {
            if (DebugUtils.isDevModel()) {
                super.onBackPressedSupport()
            } else {
                AppUtils.moveTaskToBack(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initPreData()
        super.onCreate(savedInstanceState)
        StatusBarUtils.setStatusBarForegroundColor(this, true)
        checkDialog()
        allSocket()
    }

    /***
     * 回到主页面弹出一些列的窗口
     */
    private fun checkDialog() {
        // 权限弹窗
        RxPermissionHelper.request(this,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW)

//
    }


    /**
     * 退出时取消网络相关的请求
     */
    override fun onDestroy() {
        DestroyHelper.onDestroy()
        mWsManager?.stopConnect()
        super.onDestroy()
    }

    /**
     * 初始化一些数据
     */

    private fun initPreData() {
        doOnIOThread {
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressedSupport()
        } else {
            moveTaskToBack(false)
        }
    }

    /**
     * 全局socket
     */
    var mWsManager: WsManager? = null
    private var mWsStatusListener: WsStatusListener? = null
    private var isReconnect = false
    private var mTimer: Timer? = null
    private fun allSocket() {
        initStatusListener()
        mWsManager = WsManager.Builder(this)
                .client(OkHttpClient().newBuilder()
                        .pingInterval(1000 * 50, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build())
                .needReconnect(true).wsUrl(WebUrlProvider.getALLBaseUrl())
                .build()
        mWsManager?.setWsStatusListener(mWsStatusListener)
        mWsManager?.startConnect()
    }

    private fun initStatusListener() {
        mWsStatusListener = object : WsStatusListener() {
            override fun onOpen(response: Response) {
                super.onOpen(response)
                LogUtils.d("AllWsManager-----onOpen response=$response")

            }

            override fun onMessage(text: String) {
                super.onMessage(text)
                LogUtils.d("AllWsManager-----onOpen text=$text")
                socketMessage(text)
            }

            override fun onMessage(bytes: ByteString) {
                super.onMessage(bytes)
                LogUtils.d("AllWsManager-----onMessage$bytes")
            }

            override fun onReconnect() {
                super.onReconnect()
                isReconnect = true
                LogUtils.d("AllWsManager-----onReconnect")
            }

            override fun onClosing(code: Int, reason: String) {
                super.onClosing(code, reason)
                LogUtils.d("AllWsManager-----onClosing")
            }

            override fun onClosed(code: Int, reason: String) {
                super.onClosed(code, reason)
                LogUtils.d("AllWsManager-----onClosed")
            }

            override fun onFailure(t: Throwable?, response: Response?) {
                super.onFailure(t, response)
                LogUtils.d("AllWsManager-----onFailure$response=$t")
                if (mTimer != null) {
                    mTimer?.cancel()
                    mTimer = null
                }
            }
        }
    }


    fun socketMessage(text: String) {
        if (text.isNotEmpty()) {
            val res = WebUrlProvider.getData<AllSocket>(text, AllSocket::class.java)
            when (res?.type) {
                "connected" -> {
                    mWsManager?.sendMessage(getLogin(res.client_id ?: "0"))
                    client_id = res.client_id ?: "-1"
                }
                "login" -> {
                    mWsManager?.sendMessage(ping(client_id))
                    if (mTimer == null) mTimer = Timer()
                    mTimer?.schedule(object : TimerTask() {
                        override fun run() {
                            mWsManager?.sendMessage(ping(client_id))
                            LogUtils.d("AllWsManager-----发送了心跳")

                        }
                    }, 0, 1000 * 54)
                }
                "ServerPush" -> {
                    if (res.dataType == "open_lottery_push") {
                            mWsManager?.sendMessage(makeSure(res.data?.msg_id))
                            systemDialog(res.data?.msg ?: "获取消息失败")
                    }
                }
            }
        }
    }

    var float: TitleTextWindow? = null
    var toast: PushToast? = null

    //    nQHAxp172
    private fun systemDialog(msg: String) {
//        if (WindowPermissionCheck.checkPermission(this)) {
//            if (float == null) float = TitleTextWindow(this)
//            if (float?.isShow == false) {
//                float?.show(msg)
//            }z
//        }
        if (toast == null ) {
            toast = PushToast()
            toast?.getToastInstance()?.init(this)
        }
        if (UserInfoSp.getIsLogin()){
            toast?.getToastInstance()?.createToast(msg)
        }
    }


    private fun getLogin(client_id: String, user_id: Int = UserInfoSp.getUserId()): String {
        val jsonObject = JSONObject()
        jsonObject.put("type", "login")
        jsonObject.put("client_id", client_id)
        jsonObject.put("user_id", user_id)
        return jsonObject.toString()
    }

    fun ping(client_id: String): String {
        val jsonObject = JSONObject()
        jsonObject.put("type", "ping")
        jsonObject.put("client_id", client_id)
        return jsonObject.toString()
    }

    private fun makeSure(msg_id: String?): String {
        val jsonObject = JSONObject()
        jsonObject.put("type", "confirm")
        jsonObject.put("event", "open_lottery_push")
        jsonObject.put("client_id", client_id)
        jsonObject.put("user_id", UserInfoSp.getUserId())
        jsonObject.put("msg_id", msg_id)
        return jsonObject.toString()
    }


    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun login(eventBean: LoginSuccess) {
        allSocket()
        mWsManager?.sendMessage(getLogin(client_id))
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun LoginOut(eventBean: LoginOut) {
        mWsManager?.stopConnect()
        allSocket()
    }

}