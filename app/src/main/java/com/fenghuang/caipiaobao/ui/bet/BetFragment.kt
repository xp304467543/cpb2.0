package com.fenghuang.caipiaobao.ui.bet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.content.FileProvider
import com.app.hubert.guide.NewbieGuide
import com.app.hubert.guide.model.GuidePage
import com.app.hubert.guide.model.HighLight
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.web.utils.ZpImageUtils
import com.fenghuang.baselib.web.utils.ZpWebChromeClient
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.helper.RxPermissionHelper
import com.fenghuang.caipiaobao.ui.home.data.JumpToBuyLottery
import com.fenghuang.caipiaobao.ui.home.data.LineCheck
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
import java.io.File


/**
 *
 * @ Author  QinTian
 * @ Date  2019/8/28- 11:29
 * @ Describe 投注页
 *
 */
open class BetFragment : BaseMvpFragment<BetPresenter>() {

    var baseUrl: String? = null

    var linePop: LotteryLinePop? = null

    var lineList: List<String>? = null

    var listCheck: ArrayList<LineCheck>? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun isOverridePage() = false

    override fun attachPresenter() = BetPresenter()

    override fun getLayoutResID() = R.layout.fragment_bet

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        setStatusBarHeight(statusViewBet)
    }


    override fun initData() {
        initWeb()
        baseBetWebView.webViewClient = object : WebViewClient() {
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
        mPresenter.getUrl()

    }

    override fun initEvent() {
        findView<ImageView>(R.id.betBack).setOnClickListener {
            if (baseBetWebView.canGoBack()) {
                baseBetWebView.goBack()
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
        if (lineList == null) {
            ToastUtils.show("暂无其他线路")
            return
        }
        if (listCheck.isNullOrEmpty()) {
          return
        }
        if (linePop == null){
            linePop = LotteryLinePop(getPageActivity(), listCheck!!)
            linePop?.showAtLocationBottom(tvLineDelay, 0f)
            linePop?.setSelectListener { it, pos ,ms->
                tvLineOffset.text = "线路"+(pos+1)
                tvLineDelay.text = ms +"ms"
                if (ms.toInt() > 100) {
                    setTextColor(R.id.tvLineDelay, ViewUtils.getColor(R.color.colorYellow))
                } else {
                    setTextColor(R.id.tvLineDelay, ViewUtils.getColor(R.color.colorGreen))
                }
                baseBetWebView.loadUrl(it)
            }
        }else  linePop?.showAtLocationBottom(tvLineDelay, 10f)

    }


    private var mUploadMsg: ValueCallback<Uri>? = null
    private var mUploadMsgs: ValueCallback<Array<Uri>>? = null
    private fun initWeb() {
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
    private fun showSelectPictureDialog(tag: Int, fileChooserParams: WebChromeClient.FileChooserParams?) {
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
        mFileFromCamera = File(Environment.getExternalStorageDirectory().path + "/" + System.currentTimeMillis() + ".jpg")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imgUrl: Uri
        if (getPageActivity().applicationInfo.targetSdkVersion > Build.VERSION_CODES.M) {
            val authority = "com.fenghuang.caipiaobao.ui.widget.mywebview.UploadFileProvider"
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
            showSelectPictureDialog(tag, fileChooserParams)
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
        }
    }


    override fun onBackClick() {
        if (baseBetWebView.canGoBack()) {
            baseBetWebView.goBack()
        }
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