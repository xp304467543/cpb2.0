package com.fenghuang.baselib.utils

import com.fenghuang.baselib.app.EnvModel


/**
 * app的环境控制
 */
object DebugUtils {


    private const val SP_DEBUG_EnvModel = "SP_DEBUG_EnvModel"

    /**
     * 是否是内部使用的一些东西，发布到外界需要设置为false
     */
    private var isEnvLog = false

    /**
     * 是否能切换环境，默认不能切换环境
     */
    private var isEnvSwitch = false

    /**
     * 当前的开发环境模式，默认是线上
     */
    private var currentDevModel: Int = EnvModel.RELEASE.value

    /**
     * @return 是否可以打印日志
     */
    fun isEnvLog(): Boolean {
        return isEnvLog
    }

    /**
     * 设置是否可以打印日志
     */
    fun setEnvLog(isEnvLog: Boolean) {
        DebugUtils.isEnvLog = isEnvLog
    }


    /**
     * 是否可以切换app环境
     */
    fun isEnvSwitch(): Boolean {
        return isEnvSwitch
    }

    /**
     * 设置当前的开发环境
     */
    fun setEnvSwitch(canSwitchEnv: Boolean) {
        isEnvSwitch = canSwitchEnv
    }


    /**
     * @return 调试模式，测试或者开发
     */
    fun isDebugModel(): Boolean {
        return isDevModel() || isTestModel()
    }

    /**
     * @return 开发模式
     */
    fun isDevModel(): Boolean {
        return getCurrentEnvModel() == EnvModel.DEV.value
    }

    /**
     * @return 测试模式
     */
    fun isTestModel(): Boolean {
        return getCurrentEnvModel() == EnvModel.TEST.value
    }

    /**
     * @return 灰度模式
     */
    fun isBetaModel(): Boolean {
        return getCurrentEnvModel() == EnvModel.BETA.value
    }

    /**
     * @return 正式环境
     */
    fun isReleaseModel(): Boolean {
        return getCurrentEnvModel() == EnvModel.RELEASE.value
    }

    /**
     * 获取当前的模式
     */
    private fun getCurrentEnvModel(): Int {
        return if (isEnvSwitch()) {
            val model = SpUtils.getInt(SP_DEBUG_EnvModel)
            if (model == 0)
                currentDevModel
            else model
        } else {
            currentDevModel
        }
    }


    /**
     * 设置当前的模式
     * 供测试人员手动切换app的环境
     */
    fun setCurrentEnvModel(model: Int) {
        SpUtils.putInt(SP_DEBUG_EnvModel, model)
    }

    fun setCurrentEnvModel(model: EnvModel) {
        val index = when (model) {
            EnvModel.BETA -> 2
            EnvModel.TEST -> 3
            EnvModel.DEV -> 4
            else -> 1
        }
        SpUtils.putInt(SP_DEBUG_EnvModel, index)
    }
}