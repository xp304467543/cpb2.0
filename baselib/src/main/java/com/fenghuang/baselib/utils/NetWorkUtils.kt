package com.fenghuang.baselib.utils

import android.content.Context
import android.net.ConnectivityManager


/**
 * 网络状态工具类
 */
object NetWorkUtils {

    /**
     * 网络是否没有连接
     */
    fun isNetworkNotConnected(): Boolean {
        return !isNetworkConnected()
    }


    /**
     * 是否有网络连接
     */
    @Suppress("DEPRECATION")
    fun isNetworkConnected(): Boolean {
        val mConnectivityManager = AppUtils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable
        }
        return false
    }

    /**
     * 移动网络是否连接
     */
    @Suppress("DEPRECATION")
    fun isMobileConnected(): Boolean {
        val mConnectivityManager = AppUtils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mMobileNetworkInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable
        }
        return false
    }

    /**
     * Wifi是否连接
     */
    @Suppress("DEPRECATION")
    fun isWifiConnected(): Boolean {
        val mConnectivityManager = AppUtils.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mMobileNetworkInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable
        }
        return false
    }
}