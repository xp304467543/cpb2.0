package com.fenghuang.caipiaobao.ui.bet

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.app.hubert.guide.NewbieGuide
import com.app.hubert.guide.model.GuidePage
import com.app.hubert.guide.model.HighLight
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.web.utils.ZpImageUtils
import com.fenghuang.baselib.web.utils.ZpWebChromeClient
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.helper.RxPermissionHelper
import com.fenghuang.caipiaobao.ui.home.data.BetLotteryBean
import com.fenghuang.caipiaobao.ui.home.data.JumpToBuyLottery
import com.fenghuang.caipiaobao.ui.home.data.LineCheck
import com.fenghuang.caipiaobao.ui.home.data.WebSelect
import com.fenghuang.caipiaobao.utils.NetPingManager
import com.fenghuang.caipiaobao.widget.IosBottomListWindow
import com.fenghuang.caipiaobao.widget.pop.LotteryLinePop
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_bet.*
import kotlinx.android.synthetic.main.fragment_child_live_chat.*
import kotlinx.android.synthetic.main.fragment_mine_contant_customer.*
import java.io.File


/**
 *
 * @ Author  QinTian
 * @ Date  2019/8/28- 11:29
 * @ Describe 投注页
 *
 */
open class BetFragment : BaseMvpFragment<BetPresenter>() {

    var currentId = -1

    var isLoad: Boolean = false

    var isloadW1:Boolean = false
    var isloadW2:Boolean = false
    var baseUrl: String? = null

    var linePop: LotteryLinePop? = null

    var lineList: List<String>? = null

    var lineList2: List<String>? = null

    var listCheck: ArrayList<LineCheck>? = null

    var listCheck2: ArrayList<LineCheck>? = null

    var dataBean: BetLotteryBean? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun isOverridePage() = false

    override fun attachPresenter() = BetPresenter()

    override fun getLayoutResID() = R.layout.fragment_bet

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        setStatusBarHeight(statusViewBet)
        initWeb()
    }

    /**
     * 选择web
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun webSelect(eventBean: WebSelect) {
        currentId = eventBean.pos
        if (dataBean == null) {
            mPresenter.getUrl()
        } else {
            if (!isLoad) {
                loadWeb()
            } else {
                if (currentId == 0) {
                    setVisible(baseBetWebView)
                    setGone(qbtWebView)
                    qbtWebView.onPause()
                    baseBetWebView.onResume()
                    loadPing(realUrl1)
                    if (!isloadW1)  {
                        isloadW1 = true
                        baseBetWebView.loadUrl(dataBean?.bettingArr!![0])
                    }
                } else {
                    baseBetWebView.onPause()
                    qbtWebView.onResume()
                    setGone(baseBetWebView)
                    setVisible(qbtWebView)
                    loadPing(realUrl2)
                    if (!isloadW2)   {
                        isloadW2 = true
                        qbtWebView.loadUrl(dataBean?.chessArr!![0])
                    }
                }
            }

        }
    }

    var mLDNetPingService: NetPingManager? = null
    var realUrl1 = ""
    var realUrl2 = ""
     fun loadWeb() {
        isLoad = true
        listCheck = arrayListOf()
        listCheck2 = arrayListOf()
        lineList = dataBean?.bettingArr
        lineList2 = dataBean?.chessArr
         val last0 = dataBean?.bettingArr!![0].split("?")[0]
        val str1 =last0.indexOf("//")
         realUrl1 = last0.substring(str1 + 2, last0.length )
        for ((index, res) in lineList!!.withIndex()) {
            val url = res.substring(str1 + 2, res.split("?")[0].length )
            val check = if (index == 0) {
                LineCheck(url, true)
            } else LineCheck(url)
            listCheck?.add(check)
        }
        val last = dataBean?.chessArr!![0].split("?")[0]
        val str2 = last.indexOf("//")
         realUrl2 = last.substring(str2 + 2, last.length - 1)
        for ((index, res) in lineList2!!.withIndex()) {
            val url = last.substring(str2 + 2, res.split("?")[0].length - 1)
            val check = if (index == 0) {
                LineCheck(url, true)
            } else LineCheck(url)
            listCheck2?.add(check)
        }
        if (currentId == 0) {
            setVisible(baseBetWebView)
            setGone(qbtWebView)
            baseBetWebView.loadUrl(dataBean?.bettingArr!![0])
            loadPing(realUrl1)
            isloadW1 = true
        } else if (currentId == 1) {
            setGone(baseBetWebView)
            setVisible(qbtWebView)
            qbtWebView.loadUrl(dataBean?.chessArr!![0])
            loadPing(realUrl2)
            isloadW2 = true
        }


    }

    private fun loadPing(url:String){
        mLDNetPingService = NetPingManager(context, url, object : NetPingManager.IOnNetPingListener {
            @SuppressLint("SetTextI18n")
            override fun ontDelay(log: Long) {
                mLDNetPingService?.release()
                post {
                    tvLineDelay.text = (log / 5).toString() + "ms"
                    if ((log / 5) > 100) {
                        setTextColor(R.id.tvLineDelay, ViewUtils.getColor(R.color.colorYellow))
                    } else {
                        setTextColor(R.id.tvLineDelay, ViewUtils.getColor(R.color.colorGreen))
                    }
                }
            }

            override fun onError() {
                mLDNetPingService?.release()
            }

        })
        mLDNetPingService?.getDelay()
    }


    override fun initEvent() {
        findView<ImageView>(R.id.betBack).setOnClickListener {
            if (currentId == 0) {
                if (baseBetWebView.canGoBack()) {
                    baseBetWebView.goBack()
                }
            } else {
                if (qbtWebView.canGoBack()) {
                    qbtWebView.goBack()
                }
            }

        }
        findView<ImageView>(R.id.betRefresh).setOnClickListener { mPresenter.getUrl() }

        tvLineOffset.setOnClickListener {
            showLinePop()
        }
        tvLineDelay.setOnClickListener {
            showLinePop()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showLinePop() {
        linePop = if (currentId == 0){
            if (listCheck.isNullOrEmpty())return
            LotteryLinePop(getPageActivity(), listCheck!!)
        }else {
            if (listCheck2.isNullOrEmpty())return
            LotteryLinePop(getPageActivity(), listCheck2!!)
        }
        linePop?.showAtLocationBottom(tvLineDelay, 0f)
        linePop?.setSelectListener { it, pos, ms ->
            tvLineOffset.text = "线路" + (pos + 1)
            tvLineDelay.text = ms + "ms"
            if (ms.toInt() > 100) {
                setTextColor(R.id.tvLineDelay, ViewUtils.getColor(R.color.colorYellow))
            } else {
                setTextColor(R.id.tvLineDelay, ViewUtils.getColor(R.color.colorGreen))
            }

            if (currentId == 0) {
                baseBetWebView.loadUrl(it)
            } else {
                qbtWebView.loadUrl(it)
            }
        }
        linePop?.showAtLocationBottom(tvLineDelay, 10f)

    }


    private var mUploadMsg: ValueCallback<Uri>? = null
    private var mUploadMsgs: ValueCallback<Array<Uri>>? = null
    private fun initWeb() {
        qbtWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {
                    if (url.startsWith("weixin://") || url.startsWith("alipays://") ||
                            url.startsWith("mailto://") || url.startsWith("tel://") || url.startsWith("tel:") || url.startsWith(
                                    "mqq://")) {
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
        qbtWebView.setOpenFileChooserCallBack(object : ZpWebChromeClient.OpenFileChooserCallBack {
            override fun openFileChooserCallBack(uploadMsg: ValueCallback<Uri>, acceptType: String) {
                mUploadMsg = uploadMsg
                checkPermission(0, null)

            }

            override fun showFileChooserCallBack(filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: WebChromeClient.FileChooserParams) {
                if (mUploadMsgs != null) {
                    mUploadMsgs!!.onReceiveValue(null)
                }
                mUploadMsgs = filePathCallback
                checkPermission(1, fileChooserParams)
            }
        })
        baseBetWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                try {
                    if (url.startsWith("weixin://") || url.startsWith("alipays://") ||
                            url.startsWith("mailto://") || url.startsWith("tel://") || url.startsWith("tel:") || url.startsWith(
                                    "mqq://")) {
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
        baseBetWebView.setOpenFileChooserCallBack(object : ZpWebChromeClient.OpenFileChooserCallBack {
            override fun openFileChooserCallBack(uploadMsg: ValueCallback<Uri>, acceptType: String) {
                mUploadMsg = uploadMsg
                checkPermission(0, null)

            }

            override fun showFileChooserCallBack(filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: WebChromeClient.FileChooserParams) {
                if (mUploadMsgs != null) {
                    mUploadMsgs!!.onReceiveValue(null)
                }
                mUploadMsgs = filePathCallback
                checkPermission(1, fileChooserParams)
            }
        })
    }


    /**
     * 选择图片弹框
     */
    val REQUEST_SELECT_FILE_CODE = 100
    private val REQUEST_FILE_CHOOSER_CODE = 101
    private val REQUEST_FILE_CAMERA_CODE = 102

    // 相机拍照返回的图片文件
    private var mFileFromCamera: File? = null

    // 默认图片压缩大小（单位：K）
    val IMAGE_COMPRESS_SIZE_DEFAULT = 400

    // 压缩图片最小高度
    val COMPRESS_MIN_HEIGHT = 900

    // 压缩图片最小宽度
    val COMPRESS_MIN_WIDTH = 675
    var dialog: IosBottomListWindow? = null
    private fun showSelectPictrueDialog(tag: Int, fileChooserParams: WebChromeClient.FileChooserParams?) {
        dialog = IosBottomListWindow(getPageActivity())
        dialog!!
                .setItem("拍照") { takeCameraPhoto() }
                .setItem("相册") {
                    if (tag == 0) {
                        val i = Intent(Intent.ACTION_GET_CONTENT)
                        i.addCategory(Intent.CATEGORY_OPENABLE)
                        i.type = "*/*"
                        startActivityForResult(Intent.createChooser(i, "File Browser"), REQUEST_FILE_CHOOSER_CODE)
                    } else {
                        try {
                            val intent = fileChooserParams?.createIntent()
                            startActivityForResult(intent, REQUEST_SELECT_FILE_CODE)
                        } catch (e: ActivityNotFoundException) {
                            mUploadMsgs = null
                        }

                    }
                }
                .setTitle("选择图片")
                .setCancelButton("取消")
        dialog?.setOnDissMissClickListener {
            if (mUploadMsgs != null) {
                mUploadMsgs?.onReceiveValue(null)
                mUploadMsgs = null
            }
        }
        dialog?.setCancelButtonClickListener {
            if (mUploadMsgs != null) {
                mUploadMsgs?.onReceiveValue(null)
                mUploadMsgs = null
            }
        }
        dialog!!.show()
    }

    private fun takeCameraPhoto() {
        mFileFromCamera = File(activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path + "/" + System.currentTimeMillis() + ".jpg")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imgUrl: Uri
        if (getPageActivity().applicationInfo.targetSdkVersion > Build.VERSION_CODES.M) {
            val authority = "com.fenghuang.caipiaobao.widget.webview.UploadFileProvider"
            imgUrl = FileProvider.getUriForFile(getPageActivity(), authority, mFileFromCamera!!)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            imgUrl = Uri.fromFile(mFileFromCamera)
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUrl)
        startActivityForResult(intent, REQUEST_FILE_CAMERA_CODE)
    }

    //检测权限
    fun checkPermission(tag: Int, fileChooserParams: WebChromeClient.FileChooserParams?) {
        if (RxPermissionHelper.checkPermission(android.Manifest.permission.CAMERA)) {
            showSelectPictrueDialog(tag, fileChooserParams)
        } else {
            if (RxPermissionHelper.request(this, android.Manifest.permission.CAMERA).isDisposed) {

            } else {
                if (mUploadMsgs != null) {
                    mUploadMsgs?.onReceiveValue(null)
                    mUploadMsgs = null
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_SELECT_FILE_CODE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (mUploadMsgs == null) {
                        return
                    }
                    mUploadMsgs?.onReceiveValue(android.webkit.WebChromeClient.FileChooserParams.parseResult(resultCode, data))
                    mUploadMsgs = null
                }
                if (dialog != null) {
                    dialog!!.dismiss()
                }
            }
            REQUEST_FILE_CHOOSER_CODE -> {
                if (mUploadMsg == null) {
                    return
                }
                val result = if (data == null || resultCode != Activity.RESULT_OK) null else data.data
                mUploadMsg?.onReceiveValue(result)
                mUploadMsg = null
            }
            REQUEST_FILE_CAMERA_CODE -> {
                takePictureFromCamera()
                if (dialog != null) {
                    dialog!!.dismiss()
                }
            }
        }
    }


    /**
     * 处理相机返回的图片
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun takePictureFromCamera() {
        if (mFileFromCamera != null && mFileFromCamera?.exists()!!) {
            val filePath = mFileFromCamera?.absolutePath
            // 压缩图片到指定大小
            val imgFile = ZpImageUtils.compressImage(getPageActivity(), filePath, COMPRESS_MIN_WIDTH, COMPRESS_MIN_HEIGHT, IMAGE_COMPRESS_SIZE_DEFAULT)

            val localUri = Uri.fromFile(imgFile)
            val localIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri)
            getPageActivity().sendBroadcast(localIntent)
            val result = Uri.fromFile(imgFile)

            if (mUploadMsg != null) {
                mUploadMsg?.onReceiveValue(Uri.parse(filePath))
                mUploadMsg = null
            }
            if (mUploadMsgs != null) {
                mUploadMsgs?.onReceiveValue(arrayOf(result))
                mUploadMsgs = null
            }
        } else {
            mUploadMsgs?.onReceiveValue(null)
            mUploadMsgs = null
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        baseBetWebView.destroy()
    }


    override fun onBackClick() {
        if (baseBetWebView.canGoBack()) {
            baseBetWebView.goBack()
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (currentId == 0){
            baseBetWebView.onResume()
        }else  qbtWebView.onResume()
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        baseBetWebView.onPause()
        qbtWebView.onPause()
    }

    /**
     * 跳转购彩
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun jumpToBuyLottery(eventBean: JumpToBuyLottery) {
        //引导层
        NewbieGuide.with(this).setLabel("guide2").addGuidePage(GuidePage().setLayoutRes(R.layout.guide_bet)).show()
    }

}