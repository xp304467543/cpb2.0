package com.fenghuang.baselib.base.basic

import androidx.annotation.StringRes

/**
 * 页面展示的一些接口
 */
interface IPageView {

    fun showPageLoadingDialog(msg: String? = null)
    fun hidePageLoadingDialog()

    fun hidePageLoading()

    fun showPageEmpty(msg: String? = null)
    fun showPageError(msg: String? = null)

    fun showPageContent()

    fun showToast(content: String?)
    fun showToast(@StringRes resId: Int)
}