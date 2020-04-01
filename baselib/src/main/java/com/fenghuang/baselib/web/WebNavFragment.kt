package com.fenghuang.baselib.web

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.webkit.ValueCallback
import android.webkit.WebView
import android.widget.ProgressBar
import com.fenghuang.baselib.R
import com.fenghuang.baselib.utils.AppUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.web.sonic.SonicSessionClientImpl
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.base_fragment_web.*


/**
 * h5页面,支持两种进度加载展示，支持滑动返回，支持h5内部返回
 */
open class WebNavFragment : BaseWebFragment() {

    override fun getContentResID(): Int {
        return R.layout.base_fragment_web
    }

    companion object {
        private const val FILE_CHOOSER_RESULT_CODE = 1001

        fun newInstance(url: String?, title: String? = null, webBack: Boolean = true, swipeBack: Boolean = true): WebNavFragment {
            val fragment = WebNavFragment()
            val bundle = Bundle()
            bundle.putString(WebConstant.KEY_WEB_URL, url)
            bundle.putString(WebConstant.KEY_WEB_TITLE, title)
            bundle.putBoolean(WebConstant.KEY_WEB_BACK, webBack)
            bundle.putBoolean(WebConstant.KEY_WEB_SWIPE_BACK, swipeBack)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var uploadMessage: ValueCallback<Uri>? = null
    private var uploadMessageAboveL: ValueCallback<Array<Uri>>? = null

    override fun getWebView(): WebView? = baseWebView

    override fun getProgressBar(): ProgressBar? = baseWebProgressBar

    override fun getMenuResID(): Int = R.menu.menu_web

    override fun onMenuItemSelected(itemId: Int): Boolean {
        when (itemId) {
            R.id.web_copy -> {
                if (context != null && getWebView() != null) {
                    ViewUtils.copyText(getWebUrl()!!)
                    ToastUtils.showSuccess(getString(R.string.app_copy_success))
                } else {
                    ToastUtils.showSuccess(getString(R.string.app_copy_failed))
                }
            }
            R.id.web_refresh -> onReload()
            R.id.web_open_in_browser -> AppUtils.startBrowser(getWebUrl())
            R.id.web_share -> {
                AppUtils.shareText(getWebUrl())
            }
        }
        return super.onMenuItemSelected(itemId)
    }

    @SuppressLint("AddJavascriptInterface")
    override fun addWebJsInteract(sonicSessionClient: SonicSessionClientImpl?, intent: Intent?, webView: WebView?) {
    }

    private fun openImageChooserActivity() {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "image/*"
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == uploadMessage && null == uploadMessageAboveL) return
            val result = if (data == null || resultCode != RESULT_OK) null else data.data
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data)
            } else if (uploadMessage != null) {
                uploadMessage?.onReceiveValue(result)
                uploadMessage = null
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onActivityResultAboveL(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || uploadMessageAboveL == null)
            return
        var results: Array<Uri> = emptyArray()
        if (resultCode == RESULT_OK) {
            if (intent != null) {
                val dataString = intent.dataString
                val clipData = intent.clipData
                if (clipData != null) {
                    for (i in 0 until clipData.itemCount) {
                        val item = clipData.getItemAt(i)
                        results[i] = item.uri
                    }
                }
                if (dataString != null)
                    results = arrayOf(Uri.parse(dataString))
            }
        }
        uploadMessageAboveL?.onReceiveValue(results)
        uploadMessageAboveL = null
    }

    /**
     * 拦截URL
     */
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (view == null || TextUtils.isEmpty(url)) {
            return false
        }
        // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
        if (url!!.startsWith("weixin://wap/pay?")) {  // 微信支付
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.data = Uri.parse(url)
            startActivity(intent)
            return true

        } else if (url.endsWith(".apk")) {   // apk下载，跳转到浏览器去下载
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addCategory("android.intent.category.DEFAULT")
            intent.addCategory("android.intent.category.BROWSABLE")
            startActivity(intent)
            return true

        } else if (url.startsWith("http") || url.startsWith("https")) {  // 正常的网页就在app内部加载
            view.loadUrl(url)
            return true
        }
        return false
    }

    /**
     * webView中打开文件选择弹窗
     */
    override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>) {
        uploadMessageAboveL = filePathCallback
        openImageChooserActivity()
    }

    override fun onShowFileChooser(filePathCallback: ValueCallback<Uri>) {
        uploadMessage = filePathCallback
        openImageChooserActivity()
    }

    override fun getSmartRefreshLayout(): SmartRefreshLayout? {
        return smartRefreshLayout
    }

}