package com.fenghuang.caipiaobao.app

import com.facebook.fresco.helper.Phoenix
import com.facebook.fresco.helper.config.PhoenixConfig
import com.facebook.imagepipeline.listener.RequestListener
import com.facebook.imagepipeline.listener.RequestLoggingListener
import com.fenghuang.baselib.app.BaseApplication
import com.fenghuang.baselib.utils.DebugUtils
import com.fenghuang.caipiaobao.constant.AppConfigConstant
import com.fenghuang.caipiaobao.function.OkHttpNetworkFetcher
import com.fenghuang.caipiaobao.function.doOnIOThread
import com.fenghuang.caipiaobao.manager.PushManager
import com.fenghuang.caipiaobao.services.service.InitDataService
import com.fenghuang.caipiaobao.widget.videoplayer.config.PlayerLibrary
import com.pingerx.rxnetgo.RxNetGo
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.smtt.sdk.QbSdk
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.HashSet


/**
 * 项目启动初始化
 */

class CaiPiaoBaoApplication : BaseApplication() {


    companion object {
        private lateinit var mInstance: CaiPiaoBaoApplication
        fun getInstance(): BaseApplication {
            return mInstance
        }
    }

    override fun getCurrentEnvModel() = AppConfigConstant.ENV_DEVELOP

    override fun isEnvSwitch() = AppConfigConstant.ENV_SWITCH

    override fun isEnvLog() = AppConfigConstant.ENV_LOG

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        initVideoViewManager()
    }

    override fun initMainProcess() {
        initNetWork()
        initJPush()
        initStatistic()
        startInitDataService()
        // 测试工具初始化
        initTestTools()
        // 微信X5
        initX5Web()
        // Fresco 图片加载
        initFresco()

        CrashReport.initCrashReport(this, "ec2ead8f6d", false)
    }


    override fun initThreadProcess() {
        doOnIOThread { }
    }


    private fun initNetWork() {
        RxNetGo.getInstance().init(this).debug(DebugUtils.isDebugModel())
    }

    private fun initJPush() {
        PushManager.init(getContext())
    }

    private fun initStatistic() {
    }

    private fun startInitDataService() {
        // 启动服务去后台加载一些数据
        InitDataService.enqueueWork(getContext(), InitDataService.JOB_INIT)
    }

    private fun initFresco() {
        val requestListeners = HashSet<RequestListener>()
        requestListeners.add(RequestLoggingListener())
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        val imagePipelineConfig = PhoenixConfig.Builder(this)
                .setNetworkFetcher(OkHttpNetworkFetcher(okHttpClient))
                .setRequestListeners(requestListeners)
                .build()
        Phoenix.init(this,  imagePipelineConfig) // this-->Context
    }


    private fun initTestTools() {
        //enabledStrictMode()
    }


    private fun initVideoViewManager() {
        PlayerLibrary.init(this)
    }


    private fun initX5Web() {

        //  预加载X5内核
        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                //初始化完成回调
            }

            override fun onCoreInitFinished() {
            }
        })
    }


}