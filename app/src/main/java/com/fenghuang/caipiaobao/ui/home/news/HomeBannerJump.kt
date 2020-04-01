package com.fenghuang.caipiaobao.ui.home.news

import android.widget.ImageView
import com.fenghuang.baselib.base.fragment.BaseNavFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.caipiaobao.R
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.fragment_home_banner_jump.*

/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/24- 15:24
 * @ Describe
 *
 */
class HomeBannerJump(val url: String, val title: String) : BaseNavFragment() {


    override fun getContentResID() = R.layout.fragment_home_banner_jump

    override fun getPageTitle() = title

    override fun isRegisterRxBus() = true

    override fun isShowBackIcon() = true

    override fun isShowBackIconWhite() = false

    override fun isOverridePage() = false

    override fun isSwipeBackEnable() = true

    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
        BannerSmartRefreshLayout.setEnablePureScrollMode(true)
        BannerSmartRefreshLayout.setEnableOverScrollDrag(true)
    }


    override fun initData() {
        BannerWebView.webViewClient = WebViewClient()
        BannerWebView.loadUrl(url)
    }


    override fun initEvent() {
        findView<ImageView>(R.id.ivTitleLeft).setOnClickListener {
            if (BannerWebView.canGoBack()) {
                BannerWebView.goBack()
            } else pop()
        }
    }


}