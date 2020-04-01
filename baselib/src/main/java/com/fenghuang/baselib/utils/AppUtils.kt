package com.fenghuang.baselib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.provider.Settings.Secure.getString
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.fenghuang.baselib.R
import com.fenghuang.baselib.app.BaseApplication
import java.io.File
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*


/**
 * App级别的工具类，提供系统的Context和常用的工具类
 */
object AppUtils {

    /**
     *  全局的Handler，发出了消息一定要自己手动关闭掉
     */
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    private val mActivityList = LinkedList<Activity>()

    /**
     * 获取全局的Application
     * @return Application
     */
    fun getApplication(): Application {
        return BaseApplication.getApplication()
    }

    /**
     * 获取全局的Context
     * @return Context
     */
    fun getContext(): Context {
        return getApplication().applicationContext
    }

    /**
     * 获取String资源集合
     */
    fun getString(@StringRes id: Int): String? {
        return getContext().resources?.getString(id)
    }

    /**
     * 获取Drawable对象
     */
    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(getContext(), id)
    }

    /**
     * 获取String资源集合
     */
    fun getStringArray(@ArrayRes id: Int): Array<String> {
        return getContext().resources.getStringArray(id)
    }

    /**
     * 获取打包渠道
     * 使用该方法需要在manifest中配置CHANNEL的meta_data
     */
    fun getChannel(): String? {
        val appInfo: ApplicationInfo =
                getContext().packageManager.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA)
        return appInfo.metaData.get("CHANNEL")?.toString()
    }

    /**
     * 获取版本号
     */
    @Suppress("DEPRECATION")
    fun getVersionCode(): Int {
        return getPackageInfo().versionCode
    }

    /**
     * 获取版本名
     */
    fun getVersionName(): String {
        return getPackageInfo().versionName
    }

    /**
     * 获取应用程序名称
     */
    fun getAppName(): String {
        val labelRes = getPackageInfo().applicationInfo.labelRes
        return getContext().resources.getString(labelRes)
    }

    /**
     * 获取设置里展示的版本
     */
    fun getSettingVersion(): String {
        return when {
            DebugUtils.isDebugModel() -> // 开发模式
                getVersionName() + "_dev_" + getVersionCode()
            DebugUtils.isTestModel() -> // 测试模式
                getVersionName() + "_alpha_" + getVersionCode()
            DebugUtils.isBetaModel() -> // 灰度模式
                getVersionName() + "_beta_" + getVersionCode()
            // 正式模式
            else -> getVersionName() + "." + getVersionCode()
        }
    }


    /**
     * 获取当前应用的包名
     */
    fun getPackageName(): String {
        return getContext().packageName
    }


    /**
     * 获取包信息
     */
    fun getPackageInfo(): PackageInfo {
        return getContext().packageManager.getPackageInfo(getContext().packageName, 0)
    }

    /**
     * 手机设置厂商
     */
    fun getDeviceModel(): String {
        return Build.MODEL
    }

    /**
     * 获取唯一标识码
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getDeviceId(): String {
        val id = SpUtils.getString("DEVICE_ID")
        if (!TextUtils.isEmpty(id)) {
            return id!!
        } else {
            var uuid = UUID.randomUUID().toString()
            val androidId = getString(getContext().contentResolver, Settings.Secure.ANDROID_ID)
            try {
                if ("9774d56d682e549c" != androidId) {
                    uuid = UUID.nameUUIDFromBytes(androidId.toByteArray(Charset.defaultCharset())).toString()
                } else {
                    if (getContext().checkCallingOrSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        val deviceId = ((getContext().getSystemService(Context.TELEPHONY_SERVICE)) as TelephonyManager).imei
                        if (deviceId != null) {
                            uuid = UUID.nameUUIDFromBytes(deviceId.toByteArray(Charset.defaultCharset())).toString()
                        }
                    }
                }
            } catch (e: UnsupportedEncodingException) {
                LogUtils.e(e)
            }
            SpUtils.putString("DEVICE_ID", uuid)
            LogUtils.e("---------> device_id = $uuid")
            return uuid
        }
    }

    /**
     * 手机系统版本号，Android 6.0
     */
    fun getOsVersion(): String {
        return android.os.Build.VERSION.RELEASE
    }

    /**
     * 分享文字
     */
    fun shareText(text: String?) {
        if (TextUtils.isEmpty(text)) return
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        shareIntent.type = "text/plain"
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            getContext().startActivity(
                    Intent.createChooser(shareIntent, getString(R.string.app_share_to))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (e: ActivityNotFoundException) {
            ToastUtils.showWarning(getString(R.string.app_no_share_clients))
        }
    }

    /**
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     */
    fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.data =
                Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            getContext().startActivity(intent)
            true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            false
        }
    }

    /**
     * 设置栈顶Activity
     */
    fun setTopActivity(activity: Activity) {
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.addLast(activity)
            }
        } else {
            mActivityList.addLast(activity)
        }
    }

    /**
     * 获取所有启动过的Activity
     */
    fun getActivityList(): LinkedList<Activity> {
        return mActivityList
    }

    /**
     * Post一个Runnable
     */
    fun post(runnable: Runnable) {
        mHandler.removeCallbacks(null)
        mHandler.post(runnable)
    }

    /**
     * 延时执行一个Runnable
     */
    fun postDelayed(runnable: Runnable, delayMillis: Long) {
        mHandler.removeCallbacks(null)
        mHandler.postDelayed(runnable, delayMillis)
    }

    /**
     * 移除之前发起的Post
     */
    fun removeCallbacks() {
        mHandler.removeCallbacks(null)
    }

    /**
     * 启动手机的主页，让当前的app进入后台
     */
    fun moveTaskToBack() {
        try {
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            ActivityUtils.startActivity(homeIntent)
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }

    /**
     * 启动手机主页，这个方法更加的保险
     */
    fun moveTaskToBack(activity: Activity) {
        try {
            activity.moveTaskToBack(false)
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            ActivityUtils.startActivity(homeIntent)
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }

    /**
     * 打开应用市场
     */
    fun startAppMarket() {
        val uri = Uri.parse("market://details?id=" + getPackageName())
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            getContext().startActivity(
                    Intent.createChooser(intent, getString(R.string.app_open_market))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (e: ActivityNotFoundException) {
            ToastUtils.showWarning(getString(R.string.app_no_market_clients))
        }
    }

    /**
     * 跳转Activity，不带任何参数
     */
    fun startActivity(context: Context?, clazz: Class<*>) {
        startActivity(context ?: getContext(), Intent(context ?: getContext(), clazz))
    }

    /**
     * 跳转Activity
     */
    fun startActivity(context: Context?, intent: Intent) {
        context ?: getContext().startActivity(intent)
    }

    /**
     * 打开浏览器
     */
    fun startBrowser(url: String?) {
        try {
            if (!TextUtils.isEmpty(url)) {
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                getContext().startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 判断当前APP是否在前台运行
     */
    fun isAppForeground(): Boolean {
        val activityManager = getContext().getSystemService(
                Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        for (appProcess in appProcesses) {
            if (appProcess.processName == getPackageName() && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }


    /**
     * 安装app
     * 需要权限：<uses-permission android:name="android.permission.INSTALL_PACKAGES" />
     */
    fun installApp(filePath: String, authority: String) {
        installApp(getFileByPath(filePath), authority)
    }

    /**
     * 安装app
     * 需要权限：<uses-permission android:name="android.permission.INSTALL_PACKAGES" />
     */
    fun installApp(file: File?, authority: String) {
        if (!isFileExists(file)) return
        getContext().startActivity(IntentUtils.getInstallAppIntent(file, authority, true))
    }

    /**
     * 静默安装app
     * @param file apk文件
     * 需要权限：<uses-permission android:name="android.permission.INSTALL_PACKAGES" />
     */
    fun installAppSilent(file: File): Boolean {
        return installAppSilent(file, null)
    }


    /**
     * 静默安装app
     * @param filePath apk文件的路径
     * 需要权限：<uses-permission android:name="android.permission.INSTALL_PACKAGES" />
     */
    fun installAppSilent(filePath: String): Boolean {
        return installAppSilent(getFileByPath(filePath), null)
    }

    /**
     * 静默安装app
     * 需要权限：<uses-permission android:name="android.permission.INSTALL_PACKAGES" />
     * @param file apk文件
     * @param params 安装携带的参数
     * @return 是否安装成功
     */
    fun installAppSilent(file: File?, params: String?): Boolean {
        if (!isFileExists(file)) return false
        val isRoot = isDeviceRooted()
        val filePath = file!!.absolutePath
        val command = ("LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm install " +
                (if (params == null) "" else "$params ")
                + filePath)
        val commandResult = ShellUtils.execCmd(command, isRoot)
        return if (commandResult.successMsg != null && commandResult.successMsg?.toLowerCase()!!.contains("success")) {
            true
        } else {
            Log.e(
                    "AppUtils", "installAppSilent successMsg: " + commandResult.successMsg +
                    ", errorMsg: " + commandResult.errorMsg
            )
            false
        }
    }

    /**
     * 卸载指定包名的app
     * 需要权限：<uses-permission android:name="android.permission.DELETE_PACKAGES" />
     */
    fun uninstallApp(packageName: String) {
        if (isSpace(packageName)) return
        getContext().startActivity(IntentUtils.getUninstallAppIntent(packageName, true))
    }


    /**
     * 静默卸载app，不保存数据
     * 需要权限：<uses-permission android:name="android.permission.DELETE_PACKAGES" />
     */
    fun uninstallAppSilent(packageName: String): Boolean {
        return uninstallAppSilent(packageName, false)
    }

    /**
     * 静默卸载指定包名app
     * 需要权限：<uses-permission android:name="android.permission.DELETE_PACKAGES" />
     * @param isKeepData 是否保存卸载app的数据
     */
    fun uninstallAppSilent(packageName: String, isKeepData: Boolean): Boolean {
        if (isSpace(packageName)) return false
        val isRoot = isDeviceRooted()
        val command = ("LD_LIBRARY_PATH=/vendor/lib*:/system/lib* pm uninstall "
                + (if (isKeepData) "-k " else "")
                + packageName)
        val commandResult = ShellUtils.execCmd(command, isRoot, true)
        return if (commandResult.successMsg != null && commandResult.successMsg?.toLowerCase()!!.contains("success")) {
            true
        } else {
            Log.e(
                    "AppUtils", "uninstallAppSilent successMsg: " + commandResult.successMsg +
                    ", errorMsg: " + commandResult.errorMsg
            )
            false
        }
    }


    /**
     * 指定包名的应用是否安装
     */
    fun isAppInstalled(packageName: String): Boolean {
        return !isSpace(packageName) && IntentUtils.getLaunchAppIntent(packageName) != null
    }


    /**
     * 获取当前应用的信息
     */
    fun getAppInfo(): AppInfo? = getAppInfo(getContext().packageName)


    /**
     * 获取指定包名的应用信息
     * @param packageName 包名
     */
    fun getAppInfo(packageName: String): AppInfo? {
        return try {
            val pm = getContext().packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            getAppInfo(pm, pi)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }

    }

    /**
     * 获取所有的应用信息
     */
    fun getAppsInfo(): List<AppInfo> {
        val list = ArrayList<AppInfo>()
        val pm = getContext().packageManager
        val installedPackages = pm.getInstalledPackages(0)
        for (pi in installedPackages) {
            val ai = getAppInfo(pm, pi) ?: continue
            list.add(ai)
        }
        return list
    }

    @Suppress("DEPRECATION")
    private fun getAppInfo(pm: PackageManager?, pi: PackageInfo?): AppInfo? {
        if (pm == null || pi == null) return null
        val ai = pi.applicationInfo
        val packageName = pi.packageName
        val name = ai.loadLabel(pm).toString()
        val icon = ai.loadIcon(pm)
        val packagePath = ai.sourceDir
        val versionName = pi.versionName
        val versionCode = pi.versionCode
        val isSystem = ApplicationInfo.FLAG_SYSTEM and ai.flags != 0
        return AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem)
    }

    private fun isFileExists(file: File?): Boolean {
        return file != null && file.exists()
    }

    private fun getFileByPath(filePath: String): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }

    private fun isDeviceRooted(): Boolean {
        val su = "su"
        val locations = arrayOf(
                "/system/bin/",
                "/system/xbin/",
                "/sbin/",
                "/system/sd/xbin/",
                "/system/bin/failsafe/",
                "/data/local/xbin/",
                "/data/local/bin/",
                "/data/local/"
        )
        for (location in locations) {
            if (File(location + su).exists()) {
                return true
            }
        }
        return false
    }

    /**
     * The application's information.
     */
    class AppInfo(
            packageName: String, name: String, icon: Drawable, packagePath: String,
            versionName: String, versionCode: Int, isSystem: Boolean
    ) {

        var packageName: String? = null
        var name: String? = null
        var icon: Drawable? = null
        var packagePath: String? = null
        var versionName: String? = null
        var versionCode: Int = 0
        var isSystem: Boolean = false

        init {
            this.name = name
            this.icon = icon
            this.packageName = packageName
            this.packagePath = packagePath
            this.versionName = versionName
            this.versionCode = versionCode
            this.isSystem = isSystem
        }

        override fun toString(): String {
            return "pkg name: " + packageName +
                    "\napp icon: " + icon +
                    "\napp name: " + name +
                    "\napp path: " + packagePath +
                    "\napp v name: " + versionName +
                    "\napp v code: " + versionCode +
                    "\nis system: " + isSystem
        }
    }

}