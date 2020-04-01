package com.fenghuang.caipiaobao.ui.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.caipiaobao.helper.SpHelper
import org.json.JSONObject


/**
 * 处理极光推送过来的消息的广播
 */
class PushMessageReceiver : BroadcastReceiver() {


    companion object {
        const val PUSH_ACTION = "action"
        const val PUSH_ACTION_MAIN = "main"               // 主页
    }


    /**
     * 广播接收消息
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val bundle = intent.extras
            when {

                JPushInterface.ACTION_REGISTRATION_ID == intent.action -> {
                    val registerId = bundle?.getString(JPushInterface.EXTRA_REGISTRATION_ID)
                    LogUtils.d("极光推送： ---- 设备ID = $registerId")
                    if (registerId != null) {
                        SpHelper.jPushId = registerId
                    }
                }

                JPushInterface.ACTION_MESSAGE_RECEIVED == intent.action -> {
                    LogUtils.d("极光推送： ---- 自定义消息 = ${bundle?.getString(JPushInterface.EXTRA_MESSAGE)}")
                }

                JPushInterface.ACTION_NOTIFICATION_RECEIVED == intent.action -> {
                    LogUtils.d("极光推送： ---- 收到通知 = ${bundle?.getString(JPushInterface.EXTRA_ALERT)}")
                }


                JPushInterface.ACTION_NOTIFICATION_OPENED == intent.action -> {
                    // 在这里可以自己写代码去定义用户点击后的行为
                    val extra = bundle?.getString(JPushInterface.EXTRA_EXTRA)
                    if (!TextUtils.isEmpty(extra)) {
                        performNotificationClick(context, extra!!)
                    }
                }

                JPushInterface.ACTION_CONNECTION_CHANGE == intent.action -> {
                    LogUtils.d("极光推送： ---- 网络变化")
                }
                else -> {
                    LogUtils.d("极光推送： ---- 未知类型 = " + intent.action)
                }
            }
        }
    }


    /**
     * 通知栏点击
     */
    private fun performNotificationClick(context: Context, extra: String) {
        val data = JSONObject(extra)
        when (data.optString(PUSH_ACTION)) {
//            PUSH_ACTION_MAIN -> LaunchUtils.startMain(context)
//            else -> {
//            }
        }

    }
}