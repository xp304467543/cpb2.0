package com.fenghuang.caipiaobao.ui.main

import android.Manifest
import android.os.Bundle
import com.fenghuang.baselib.base.activity.BasePageActivity
import com.fenghuang.baselib.utils.AppUtils
import com.fenghuang.baselib.utils.DebugUtils
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.caipiaobao.function.doOnIOThread
import com.fenghuang.caipiaobao.helper.DestroyHelper
import com.fenghuang.caipiaobao.helper.RxPermissionHelper

/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/25- 16:17
 * @ Describe
 *
 */

class MainActivity : BasePageActivity() {


    override fun getPageFragment() = MainFragment()


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
    }

    /***
     * 回到主页面弹出一些列的窗口
     */
    private fun checkDialog() {
        // 权限弹窗
        RxPermissionHelper.request(this,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)

//        Manifest.permission.SYSTEM_ALERT_WINDOW,
    }


    /**
     * 退出时取消网络相关的请求
     */
    override fun onDestroy() {
        DestroyHelper.onDestroy()
        super.onDestroy()
    }

    /**
     * 初始化一些数据
     */

    private fun initPreData() {
        doOnIOThread {
        }
    }

}