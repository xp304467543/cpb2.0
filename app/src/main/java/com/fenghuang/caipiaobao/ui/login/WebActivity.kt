package com.fenghuang.caipiaobao.ui.login

import android.annotation.TargetApi
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.fenghuang.baselib.base.activity.BaseNavActivity
import com.fenghuang.baselib.web.utils.ZpImageUtils
import com.fenghuang.baselib.web.utils.ZpWebChromeClient
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.helper.RxPermissionHelper
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.widget.IosBottomListWindow
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.fragment_mine_contant_customer.*
import java.io.File

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-24
 * @ Describe
 *
 */

class WebActivity : BaseNavActivity() {

    override fun isOverride() = false

    override fun getPageTitle() = getString(R.string.mine_contact_customer)

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.fragment_mine_contant_customer


    override fun onDestroy() {
        super.onDestroy()
        wbContact.destroy()
    }


    override fun initContentView() {
        if (UserInfoSp.getCustomer().isNullOrEmpty()) {
            initSome()
        } else wbContact.loadUrl(UserInfoSp.getCustomer())

        initWeb()
    }

    private fun initSome() {
        HomeApi.getLotteryUrl {
            onSuccess {
                UserInfoSp.putCustomer(it.customer)
                wbContact.loadUrl(it.customer)
            }
            onFailed { }
        }

    }

    private var mUploadMsg: ValueCallback<Uri>? = null
    private var mUploadMsgs: ValueCallback<Array<Uri>>? = null
    private fun initWeb() {
        wbContact.webViewClient = object : WebViewClient() {
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
        wbContact.setOpenFileChooserCallBack(object : ZpWebChromeClient.OpenFileChooserCallBack {
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
    private fun showSelectPicturedDialog(tag: Int, fileChooserParams: WebChromeClient.FileChooserParams?) {
        dialog = IosBottomListWindow(this)
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
        mFileFromCamera = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path + "/" + System.currentTimeMillis() + ".jpg")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val imgUrl: Uri
        if (this.applicationInfo.targetSdkVersion > Build.VERSION_CODES.M) {
            val authority = "com.fenghuang.caipiaobao.widget.webview.UploadFileProvider"
            imgUrl = FileProvider.getUriForFile(this, authority, mFileFromCamera!!)
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
            showSelectPicturedDialog(tag, fileChooserParams)
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
            val imgFile = ZpImageUtils.compressImage(this, filePath, COMPRESS_MIN_WIDTH, COMPRESS_MIN_HEIGHT, IMAGE_COMPRESS_SIZE_DEFAULT)

            val localUri = Uri.fromFile(imgFile)
            val localIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri)
            this.sendBroadcast(localIntent)
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

}