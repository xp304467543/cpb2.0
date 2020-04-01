package com.fenghuang.caipiaobao.services.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.fenghuang.baselib.web.sonic.SonicInstaller

/**
 * 初始化数据的服务，可以添加数据加载到队列中，加载结束后会自动关闭服务
 */
class InitDataService : JobIntentService() {


    override fun onHandleWork(intent: Intent) {
        val jobType = intent.getIntExtra(JOB_TYPE, 0)
        if (jobType == JOB_INIT) {
            init()
        }
    }


    companion object {
        private const val JOB_ID = 1001
        private const val JOB_TYPE = "JOB_TYPE"

        const val JOB_INIT = 1002

        fun enqueueWork(context: Context, jobType: Int, intent: Intent = Intent()) {
            intent.putExtra(JOB_TYPE, jobType)
            enqueueWork(context, InitDataService::class.java, JOB_ID, intent)
        }
    }

    private fun init() {
        initConfig()
        initUpdate()
        initSonic()
        initUser()
    }

    /**
     * 用户相关的数据
     */
    private fun initUser() {
    }


    private fun initSonic() {
        SonicInstaller.initSonic(this)
    }

    private fun initUpdate() {
        //BuglyInstaller.checkUpdate(isManual = false, isSilence = false)
    }

    private fun initConfig() {

    }

}