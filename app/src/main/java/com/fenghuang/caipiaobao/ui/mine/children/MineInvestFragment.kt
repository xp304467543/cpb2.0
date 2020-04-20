package com.fenghuang.caipiaobao.ui.mine.children

import android.content.Intent
import android.net.Uri
import com.fenghuang.baselib.base.activity.BaseNavActivity
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.IntentConstant.MINE_INVEST_AMOUNT
import com.fenghuang.caipiaobao.constant.IntentConstant.MINE_RECHARGE_ID
import com.fenghuang.caipiaobao.constant.IntentConstant.MINE_RECHARGE_URL
import com.fenghuang.caipiaobao.data.api.BaseApi
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.fragment_invest.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-04
 * @ Describe 充值Web
 *
 */

class MineInvestFragment : BaseNavActivity(), BaseApi {


    override fun getPageTitle() = "在线充值"

    override fun getContentResID() = R.layout.fragment_invest

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun isOverride() = false

    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(this, true)


        investWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {
                    if (url.startsWith("weixin://") || url.startsWith("alipays://") ||
                            url.startsWith("mailto://") || url.startsWith("tel://")) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        return true
                    }//其他自定义的scheme
                } catch (e: Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false
                }

                return false
            }
        }
    }

    override fun initData() {
        val money = intent.getFloatExtra(MINE_INVEST_AMOUNT, 0F)
        val id = intent.getIntExtra(MINE_RECHARGE_ID,0)
        val url =intent.getStringExtra(MINE_RECHARGE_URL)
        if (intent.getBooleanExtra("isRen",false)) investWebView.loadUrl(url) else getInvestUrl(money, id, url)
    }

    override fun onDestroy() {
        super.onDestroy()
        StatusBarUtils.setStatusBarForegroundColor(this, false)
        if (investWebView != null) {
            investWebView.destroy()
        }
    }


    override fun onBackPressed() {
         if (investWebView.canGoBack()) {
            investWebView.goBack()
        } else {
            finish()
        }
    }


    private fun getInvestUrl(amount: Float, channels: Int, route: String) {
        showPageLoadingDialog()
        MineApi.getToPayUrl(amount, channels, route) {
            onSuccess {
                    investWebView.loadUrl(it.url.replace("\\", "/"))
                    hidePageLoadingDialog()
            }
            onFailed {
                GlobalDialog.showError(this@MineInvestFragment, it)
                hidePageLoadingDialog()
            }
        }
    }
}