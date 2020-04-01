package com.fenghuang.baselib.app

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.text.TextUtils
import androidx.multidex.MultiDex
import com.fenghuang.baselib.utils.DebugUtils
import com.fenghuang.baselib.utils.LogUtils
import me.yokeyword.fragmentation.Fragmentation


/**
 * 项目初始化的入口，这里在基类中封装，并且初始化公共类库的环境
 * 初始化公共库的其他类库，抽取方法给子类去继承
 */
abstract class BaseApplication : Application() {

    companion object {
        private lateinit var mApplication: BaseApplication
        fun getApplication(): BaseApplication {
            return mApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        mApplication = this
        dispatchProcess()
    }

    /**
     * 分发当前进程
     */
    private fun dispatchProcess() {
        val processName = getCurProcessName(getContext())
        if (!TextUtils.isEmpty(processName)) {
            if (TextUtils.equals(processName, getContext().packageName)) {
                initBaseMainProcess()
                initMainProcess()
                initThreadProcess()
                LogUtils.e("---------> main process : " + processName!!)
            } else {
                LogUtils.e("---------> other process : " + processName!!)
            }
        }
        initBaseMultiProcess()
        initMultiProcess()
    }

    /**
     * 父类自己初始化
     * 运行在主线程
     */
    private fun initBaseMainProcess() {
        initEnv()
        initFragmentation()
    }


    private fun initBaseMultiProcess() {

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }


    fun getContext(): Context {
        return applicationContext
    }

    /**
     * 初始化环境
     */
    private fun initEnv() {
        DebugUtils.setEnvLog(isEnvLog())
        DebugUtils.setEnvSwitch(isEnvSwitch())
        DebugUtils.setCurrentEnvModel(getCurrentEnvModel())
    }

    /**
     * 初始化类库[Fragmentation]
     */
    private fun initFragmentation() {
        Fragmentation.builder()
                // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
//                .stackViewMode(Fragmentation.SHAKE)
                .debug(DebugUtils.isDevModel())
                /**
                 * 可以获取到[me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning]
                 * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                 */
                .handleException {
                    // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                    // Bugtags.sendException(e);
                    LogUtils.e(it)
                }
                .install()
    }


    /**
     * 获取当前进程名字
     */
    private fun getCurProcessName(context: Context): String? {
        val pid = android.os.Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (activityManager.runningAppProcesses != null) {
            for (appProcess in activityManager.runningAppProcesses) {
                if (appProcess.pid == pid) {
                    return appProcess.processName
                }
            }
        }
        return null
    }


    /**
     * 获取当前的开发环境模式
     */
    abstract fun getCurrentEnvModel(): Int

    /**
     * 当前的环境是不是只提示给内部使用，由具体的application重写
     */
    abstract fun isEnvSwitch(): Boolean

    /**
     * 是否可以打印日志
     */
    abstract fun isEnvLog(): Boolean

    /**
     * 给子类初始化第三方的SDK
     * 运行在当前APP的进程
     */
    abstract fun initMainProcess()

    /**
     * 子线程初始数据
     */
    protected open fun initThreadProcess() {}

    /**
     * 给子类初始化
     * 运行在多个进程
     */
    protected open fun initMultiProcess() {}


}