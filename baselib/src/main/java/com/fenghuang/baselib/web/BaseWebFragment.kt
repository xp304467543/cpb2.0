package com.fenghuang.baselib.web

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import com.fenghuang.baselib.R
import com.fenghuang.baselib.base.fragment.BaseNavFragment
import com.fenghuang.baselib.utils.NetWorkUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.web.sonic.SonicSessionClientImpl
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.tencent.sonic.sdk.SonicEngine
import com.tencent.sonic.sdk.SonicSession
import com.tencent.sonic.sdk.SonicSessionConfig

/**
 * 加载网页的Fragment基类
 * 提供两种加载展示方法，默认使用进度条的方法
 */
abstract class BaseWebFragment : BaseNavFragment() {

    private var mSonicSession: SonicSession? = null
    private var mSonicSessionClient: SonicSessionClientImpl? = null
    private var mWebUrl: String? = null
    private var mWebTitle: String? = null

    private lateinit var mIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        init()
        initSonic()
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun init() {
        if (Build.VERSION.SDK_INT > 19) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        mIntent = getIntent(arguments)
//        mWebUrl = getBaseUrl()
        mWebTitle = mIntent.getStringExtra(WebConstant.KEY_WEB_TITLE)
        if (isCleanCache()) {
            SonicEngine.getInstance().cleanCache()
        }
    }

    override fun initContentView() {
        ViewUtils.setVisible(getProgressBar(), isProgressBarLoading())
        getSmartRefreshLayout()?.setEnablePureScrollMode(true)
        getSmartRefreshLayout()?.setEnableOverScrollDrag(true)
        initWebView()
    }

    override fun initData() {
//        loadUrl(mWebUrl)
    }

    private fun initSonic() {
        // if it's sonic mode , startup sonic session at first time
        if (isUseSonic()) { // sonic mode
            val sessionConfigBuilder = SonicSessionConfig.Builder()
            sessionConfigBuilder.setSupportLocalServer(true)

            // create sonic session and run sonic flow
            if (TextUtils.isEmpty(mWebUrl)) {
                return
            }
            mSonicSession = SonicEngine.getInstance().createSession(mWebUrl!!, sessionConfigBuilder.build())
            if (mSonicSession != null) {
                mSonicSessionClient = SonicSessionClientImpl()
                mSonicSession!!.bindClient(mSonicSessionClient)
            }
        }
    }

    /**
     * init WebView
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val webView = getWebView()
        if (webView != null) {
            webView.isLongClickable = true
            webView.overScrollMode = View.OVER_SCROLL_NEVER
            webView.isVerticalScrollBarEnabled = false
            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    this@BaseWebFragment.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    if (mSonicSession != null) {
                        mSonicSession!!.sessionClient.pageFinish(url)
                    }
                    if (!TextUtils.isEmpty(view?.title)) {
                        setPageTitle(view?.title)
                    }
                    this@BaseWebFragment.onPageFinished(view, url)
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        this@BaseWebFragment.shouldOverrideUrlLoading(view, request?.url?.toString())
                    } else {
                        super.shouldOverrideUrlLoading(view, request)
                    }
                }
            }

            // Android　4.0版本没有　WebChromeClient.FileChooserParams　这个类，所有要单独处理
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        if (isProgressBarLoading()) {
                            getProgressBar()?.progress = newProgress
                            if (newProgress >= 100) {
                                ViewUtils.setGone(getProgressBar())
                            }
                        }
                    }

                    //  5.0及以上调用
                    override fun onShowFileChooser(webView: WebView,
                                                   filePathCallback: ValueCallback<Array<Uri>>,
                                                   fileChooserParams: FileChooserParams): Boolean {
                        this@BaseWebFragment.onShowFileChooser(webView, filePathCallback)
                        return true
                    }

                    override fun onPermissionRequest(request: PermissionRequest) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            request.grant(request.resources)
                        }
                    }

                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        super.onReceivedTitle(view, title)
                        if (!TextUtils.isEmpty(title)) {
                            setPageTitle(title)
                        }
                    }
                }
            }

            webView.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack() && !isOriginalUrl()) {
                        webView.goBack()
                        return@setOnKeyListener true
                    }
                }
                false
            }
            val webSettings = webView.settings

            // add java script interface
            // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
            // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
            // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
            webSettings.javaScriptEnabled = true
            webView.removeJavascriptInterface("searchBoxJavaBridge_")
            mIntent.putExtra(WebConstant.KEY_WEB_LOAD_URL_TIME, System.currentTimeMillis())

            // add interface
            addWebJsInteract(mSonicSessionClient, mIntent, webView)

            // init webview settings
            webSettings.allowContentAccess = true
            webSettings.loadsImagesAutomatically = true
            webSettings.useWideViewPort = true
            webSettings.loadWithOverviewMode = false
            webSettings.databaseEnabled = true
            webSettings.domStorageEnabled = true
            webSettings.setSupportZoom(true)
            webSettings.setAppCacheEnabled(true)
            webSettings.setSupportMultipleWindows(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            CookieManager.getInstance().setAcceptCookie(true)
        }
    }


    /**
     * 加载Url
     */
    open fun loadUrl(url: String?) {
        if (checkEnableLoad()) {
            // webview is ready now, just tell session client to bind
            if (mSonicSessionClient != null && !isCanBack()) {
                mSonicSessionClient!!.bindWebView(getWebView()!!)
                mSonicSessionClient!!.clientReady()
            } else { // default mode
                getWebView()?.loadUrl(url)
            }
        }
    }


    /**
     * 重新加载
     */
    protected open fun onReload() {
        if (checkEnableLoad()) {
            if (isProgressBarLoading()) {
                setVisible(getProgressBar())
            }
            getWebView()?.reload()
        }
    }

    override fun onBackClick() {
        if (getWebView()?.canGoBack() == true && !isOriginalUrl() && isCanBack()) {
            getWebView()?.goBack()
        } else {
            getPageActivity().onBackPressedSupport()
        }
    }

    /**
     * on loading start
     *
     * @param view
     * @param url
     * @param favicon
     */
    protected open fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        if (!isProgressBarLoading()) {
            showPageLoading()
        }
    }

    /**
     * on loading release
     *
     * @param view
     * @param url
     */
    protected open fun onPageFinished(view: WebView?, url: String?) {
        showPageContent()
    }

    protected open fun setBaseUrl(url: String) {
        this.mWebUrl = url
    }


    /**
     * get web title
     */
    protected open fun getWebTitle(): String? {
        return mWebTitle
    }

    /**
     * get web url
     */
    protected open fun getWebUrl(): String? {
        return mWebUrl
    }


    /**
     * H5内部是否可以返回，默认不可以返回
     */
    protected open fun isCanBack(): Boolean {
        return mIntent.getBooleanExtra(WebConstant.KEY_WEB_BACK, true)
    }

    /**
     * add javascript interface
     *
     * @param sonicSessionClient
     * @param intent
     * @param webView
     */
    protected open fun addWebJsInteract(sonicSessionClient: SonicSessionClientImpl?, intent: Intent?, webView: WebView?) {}

    /**
     * override url
     *
     * @param view
     * @param url
     * @return
     */
    protected open fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }

    /**
     * show file chooser
     *
     * @param webView
     * @param filePathCallback
     */
    protected open fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>) {}

    /**
     * show file chooser
     *
     * @param filePathCallback
     */
    protected open fun onShowFileChooser(filePathCallback: ValueCallback<Uri>) {}

    /**
     * get web view
     *
     * @return
     */
    abstract fun getWebView(): WebView?

    /**
     * get progressbar
     */
    abstract fun getProgressBar(): ProgressBar?

    /**
     * get smart refresh layout
     */
    abstract fun getSmartRefreshLayout(): SmartRefreshLayout?

    /**
     * is clean sonic cache
     *
     * @return
     */
    protected open fun isCleanCache(): Boolean = false

    /**
     * 是否使用sonic加载h5
     */
    protected open fun isUseSonic(): Boolean = true


    /**
     * 是不是第一个URl
     */
    protected open fun isOriginalUrl(): Boolean = getWebView()?.copyBackForwardList()?.getItemAtIndex(0)?.url == getWebView()?.copyBackForwardList()?.currentItem?.url


    /**
     * 是否使用进度条来呈现加载中占位图
     * 默认使用进度条
     */
    protected open fun isProgressBarLoading(): Boolean = true

    /**
     * 是否可以滑动返回
     */
    override fun isSwipeBackEnable(): Boolean {
        return mIntent.getBooleanExtra(WebConstant.KEY_WEB_SWIPE_BACK, false)
    }

    override fun onDetach() {
        mSonicSession?.destroy()
        mSonicSession = null

        mSonicSessionClient?.destroy()
        mSonicSessionClient = null

        val webView = getWebView()
        val parent = webView?.parent
        if (parent != null) {
            (parent as ViewGroup).removeView(webView)
        }
        webView?.stopLoading()
        webView?.settings?.javaScriptEnabled = false
        webView?.removeAllViews()
        super.onDetach()
    }

    private fun getIntent(bundle: Bundle?): Intent {
        val intent = Intent()
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        return intent
    }

    private fun checkEnableLoad(): Boolean {
        // 加载之前判断一下网络是否已经连接
        if (!NetWorkUtils.isNetworkConnected()) {
            showPageError(getString(R.string.app_network_disconnection))
            return false
        }

        if (TextUtils.isEmpty(mWebUrl)) {
            showPageError(getString(R.string.app_web_empty))
            return false
        }
        return true
    }
}