package com.fenghuang.baselib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import java.lang.reflect.InvocationTargetException
import java.util.*

object ActivityUtils {

    /**
     * 某个app的某个activity是否存在
     */
    fun isActivityExists(pkg: String,
                         cls: String): Boolean {
        val intent = Intent()
        intent.setClassName(pkg, cls)
        return !(AppUtils.getContext().packageManager.resolveActivity(intent, 0) == null ||
                intent.resolveActivity(AppUtils.getContext().packageManager) == null ||
                AppUtils.getContext().packageManager.queryIntentActivities(intent, 0).size == 0)
    }

    /**
     * 启动一个Activity
     */
    fun startActivity(intent: Intent) {
        startActivity(intent, getActivityOrApp(), null)
    }

    /**
     * 启动一个Activity，携带数据
     */
    fun startActivity(intent: Intent,
                      options: Bundle) {
        startActivity(intent, getActivityOrApp(), options)
    }


    /**
     * 启动一个Activity，指定启动页面，携带数据
     */
    fun startActivity(activity: Activity,
                      intent: Intent) {
        startActivity(intent, activity, null)
    }

    /**
     * 启动一个Activity，指定启动页面，携带数据
     */
    fun startActivity(activity: Activity,
                      intent: Intent,
                      options: Bundle?) {
        startActivity(intent, activity, options)
    }

    /**
     * 启动页面，带有共享参数
     */
    fun startActivity(activity: Activity,
                      intent: Intent,
                      sharedElements: Array<View>) {
        startActivity(intent, activity, getOptionsBundle(activity, sharedElements))
    }


    /**
     * 启动多个Activity,集合中的Activity是反过来呈现的
     * 不携带任何参数
     */
    fun startActivities(intents: Array<Intent>) {
        startActivities(intents, getActivityOrApp(), null)
    }


    /**
     * 启动多个Activity,集合中的Activity是反过来呈现的
     */
    fun startActivities(intents: Array<Intent>,
                        options: Bundle?) {
        startActivities(intents, getActivityOrApp(), options)
    }


    /**
     * 启动多个Activity,集合中的Activity是反过来呈现的
     */
    fun startActivities(activity: Activity,
                        intents: Array<Intent>) {
        startActivities(intents, activity, null)
    }

    /**
     * 获取所有启动过的Activity集合
     */
    fun getActivityList(): LinkedList<Activity> {
        return AppUtils.getActivityList()
    }


    /**
     * 获取当前app中启动的activity
     */
    fun getLauncherActivity(): String {
        return getLauncherActivity(AppUtils.getContext().packageName)
    }

    /**
     * 获取指定app中启动的activity
     */
    fun getLauncherActivity(pkg: String): String {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pm = AppUtils.getContext().packageManager
        val info = pm.queryIntentActivities(intent, 0)
        for (aInfo in info) {
            if (aInfo.activityInfo.packageName == pkg) {
                return aInfo.activityInfo.name
            }
        }
        return "no $pkg"
    }

    /**
     * 获取app内栈顶的Activity
     */
    @SuppressLint("PrivateApi")
    fun getTopActivity(): Activity? {
        val topActivity = AppUtils.getActivityList().last
        if (topActivity != null) {
            return topActivity
        }
        // using reflect to get top activity
        try {
            @SuppressLint("PrivateApi")
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null)
            val activitiesField = activityThreadClass.getDeclaredField("mActivities")
            activitiesField.isAccessible = true
            val activities = activitiesField.get(activityThread) as Map<*, *>
            for (activityRecord in activities.values) {
                val activityRecordClass = activityRecord?.javaClass
                val pausedField = activityRecordClass?.getDeclaredField("paused")
                pausedField?.isAccessible = true
                if (pausedField?.getBoolean(activityRecord) == false) {
                    val activityField = activityRecordClass.getDeclaredField("activity")
                    activityField.isAccessible = true
                    val activity = activityField.get(activityRecord) as Activity
                    AppUtils.setTopActivity(activity)
                    return activity
                }
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getActivityOrApp(): Context {
        val topActivity = getTopActivity()
        return topActivity ?: AppUtils.getContext()
    }


    private fun startActivity(intent: Intent,
                              context: Context,
                              options: Bundle?) {
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivity(intent, options)
        } else {
            context.startActivity(intent)
        }
    }

    private fun startActivities(intents: Array<Intent>,
                                context: Context,
                                options: Bundle?) {
        if (context !is Activity) {
            for (intent in intents) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivities(intents, options)
        } else {
            context.startActivities(intents)
        }
    }

    private fun getOptionsBundle(context: Context,
                                 enterAnim: Int,
                                 exitAnim: Int): Bundle? {
        return ActivityOptionsCompat.makeCustomAnimation(context, enterAnim, exitAnim).toBundle()
    }

    private fun getOptionsBundle(activity: Activity,
                                 sharedElements: Array<View>): Bundle? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val len = sharedElements.size
            val pairs = arrayOfNulls<Pair<View, String>>(len)
            for (i in 0 until len) {
                pairs[i] = Pair.create(sharedElements[i], sharedElements[i].transitionName)
            }
            return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *pairs).toBundle()
        }
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, null, null).toBundle()
    }

}