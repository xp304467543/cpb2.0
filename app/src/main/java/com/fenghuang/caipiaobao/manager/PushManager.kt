package com.fenghuang.caipiaobao.manager

import android.content.Context
import cn.jpush.android.api.JPushInterface
import com.fenghuang.baselib.utils.AppUtils
import com.fenghuang.baselib.utils.DebugUtils

/**
 * 极光推送封装
 */
object PushManager {

    /**
     * 初始化
     */
    fun init(context: Context) {
        JPushInterface.setDebugMode(DebugUtils.isDebugModel())
        JPushInterface.init(context)
        // 设置应用渠道
        JPushInterface.setChannel(context, AppUtils.getChannel())
        // 设置保留展示最多的消息条目
        JPushInterface.setLatestNotificationNumber(context, 3)
    }

    /**
     * 设置别名
     */
    fun setAlias(sequence: Int, alias: String) {
        JPushInterface.setAlias(getContext(), sequence, alias)
    }

    /**
     * 获取别名
     */
    fun getAlias(sequence: Int) {
        JPushInterface.getAlias(getContext(), sequence)
    }

    /**
     * 删除别名
     */
    fun deleteAlias(sequence: Int) {
        JPushInterface.deleteAlias(getContext(), sequence)
    }

    /**
     * 设置标签
     */
    fun setTags(sequence: Int, tags: Set<String>) {
        JPushInterface.setTags(getContext(), sequence, tags)
    }

    fun setTags(sequence: Int, tag: String) {
        setTags(sequence, setOf(tag))
    }

    /**
     * 添加标签
     */
    fun addTags(sequence: Int, tag: String) {
        addTags(sequence, setOf(tag))
    }

    fun addTags(sequence: Int, tags: Set<String>) {
        JPushInterface.addTags(getContext(), sequence, tags)
    }

    /**
     * 删除标签
     */
    fun deleteTags(sequence: Int, tags: Set<String>) {
        JPushInterface.deleteTags(getContext(), sequence, tags)
    }

    fun deleteTags(sequence: Int, tag: String) {
        deleteTags(sequence, setOf(tag))
    }

    fun cleanTags(sequence: Int) {
        JPushInterface.cleanTags(getContext(), sequence)
    }

    fun getAllTags(sequence: Int) {
        JPushInterface.getAllTags(getContext(), sequence)
    }

    /**
     * 获取极光推送唯一设备的注册ID
     */
    fun getRegistrationID(): String {
        return JPushInterface.getRegistrationID(getContext())
    }


    private fun getContext(): Context {
        return AppUtils.getContext()
    }
}