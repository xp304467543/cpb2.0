package com.fenghuang.baselib.base.basic

import android.content.Context

/**
 * MVP的基类View
 */

interface IMvpView : IPageView {

    /**
     * 获取当前页面的Context
     */
    fun getContext(): Context?

    /**
     * View是否激活
     */
    fun isActive(): Boolean
}