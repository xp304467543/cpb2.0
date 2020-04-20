package com.fenghuang.caipiaobao.ui.mine.children

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils.getColor
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.UpDateUserPhoto
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.widget.IosBottomListWindow
import com.google.gson.JsonParser
import com.hwangjr.rxbus.RxBus
import com.sl.utakephoto.crop.CropOptions
import com.sl.utakephoto.exception.TakeException
import com.sl.utakephoto.manager.ITakePhotoResult
import com.sl.utakephoto.manager.UTakePhoto
import kotlinx.android.synthetic.main.fragment_mine_presonal.*
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException


/**
 *
 * @ Author  QinTian
 * @ Date  2019/10/15- 18:09
 * @ Describe
 *
 */

class MinePersonalPresenter : BaseMvpPresenter<MinePersonalFragment>() {


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getPhotoFromPhone(activity: Activity) {
        if (mView.isActive()) {
            IosBottomListWindow(activity)
                    .setTitle("添加头像")
                    .setItem("拍摄", getColor(R.color.black)) {
                        openOp(1)
                    }
                    .setItem("从手机相册选择") {
                        openOp(2)
                    }
                    .setCancelButton("取消")
                    .show()
        }

    }


    private fun openOp(mode: Int) {
        val cropOptions = CropOptions.Builder()
                .setAspectX(1).setAspectY(1)
                .setOutputX(100).setOutputY(1)
                .setWithOwnCrop(true)//使用系统裁剪还是自带裁剪
                .create()
        when (mode) {
            1 -> {
                UTakePhoto.with(mView).openCamera().setCrop(cropOptions).build(object : ITakePhotoResult {
                    override fun takeSuccess(uriList: MutableList<Uri>?) {
                        val uri = uriList?.get(0)!!
                        mView.setImgAvatar(uriList[0])
                        val bitmap = BitmapFactory.decodeStream(mView.requireActivity().contentResolver.openInputStream(uri))
                        upLoadPersonalAvatar(bitmap)
                    }

                    override fun takeFailure(ex: TakeException?) {
                    }

                    override fun takeCancel() {
                    }

                })
            }

            2 -> {
                UTakePhoto.with(mView).openAlbum().setCrop(cropOptions).build(object : ITakePhotoResult {
                    override fun takeSuccess(uriList: MutableList<Uri>?) {
                        val uri = uriList?.get(0)!!
                        mView.setImgAvatar(uriList[0])
                        val bitmap = BitmapFactory.decodeStream(mView.requireActivity().contentResolver.openInputStream(uri))
                        upLoadPersonalAvatar(bitmap)

                    }

                    override fun takeFailure(ex: TakeException?) {
                    }

                    override fun takeCancel() {
                    }

                })
            }
        }
    }


    //输入限制
    fun initEditPersonal(editText: EditText, textView: TextView) {
        val num = 0
        val mMaxNum = 50
        editText.addTextChangedListener(object : TextWatcher {
            //记录输入的字数
            var wordNum: CharSequence? = null
            var selectionStart: Int = 0
            var selectionEnd: Int = 0

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wordNum = s
            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val number = num + s!!.length
                textView.text = "$number/50"
                selectionStart = editText.selectionStart
                selectionEnd = editText.selectionEnd
                //判断大于最大值
                if (wordNum!!.length > mMaxNum) {
                    s.delete(selectionStart - 1, selectionEnd)
                    val tempSelection = selectionEnd
                    editText.text = s
                    editText.setSelection(tempSelection)
                    ToastUtils.showInfo("最多输入50字")
                }
            }
        })
    }

    //上传个人资料
    fun upLoadPersonalInfo(nickName: String, gender: Int, profile: String) {
        MineApi.upLoadPersonalInfo(nickName, gender, profile) {
            mView.showPageLoadingDialog()
            onSuccess {
                mView.hidePageLoadingDialog()
                UserInfoSp.putUserNickName(nickName)
                UserInfoSp.putUserSex(gender)
                UserInfoSp.putUserProfile(profile)
                ToastUtils.showSuccess("信息修改成功")
                mView.pop()
            }
            onFailed {
                mView.hidePageLoadingDialog()
                GlobalDialog.showError(mView.requireActivity(), it)
            }
        }
    }

    //上传个人头像
    fun upLoadPersonalAvatar(bitmap: Bitmap) {
        mView.showPageLoadingDialog()
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
                .addFormDataPart("avatar", "data:image/png;base64," + bitMapToBase64(bitmap))
        val requestBody = builder.build()
        val request = Request.Builder()
                .url(MineApi.getBaseUrlMe() + "/" + MineApi.getApiOtherUserTest() + MineApi.USER_UPLOAD_AVATAR)
                .addHeader("Authorization", UserInfoSp.getTokenWithBearer())
                .post(requestBody)
                .build()
        val client = OkHttpClient.Builder().build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (mView.isActive()) {
                    try {
                        val json = JsonParser().parse(response.body()?.string()!!).asJsonObject
                        if (json.get("code").asInt == 1) {
                            Looper.prepare()
                            mView.hidePageLoadingDialog()
                            ToastUtils.showSuccess("头像修改成功")
                            RxBus.get().post(UpDateUserPhoto(json.get("data").asJsonObject.get("avatar").asString))
                            Looper.loop()
                        } else {
                            Looper.prepare()
                            mView.hidePageLoadingDialog()
                            ToastUtils.show(json.get("msg").asString)
                            Looper.loop()
                        }
                    } catch (e: Exception) {
                        mView.hidePageLoadingDialog()
                        e.printStackTrace()
                        Looper.prepare()
                        ToastUtils.show("服务器异常,上传失败")
                        Looper.loop()
                    }
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                Looper.prepare()
                mView.hidePageLoadingDialog()
                ToastUtils.showError(e.toString())
                Looper.loop()
            }
        })
    }


    private fun bitMapToBase64(bitmap: Bitmap): String {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)//第二个入参表示图片压缩率，如果是100就表示不压缩
        val bytes = bos.toByteArray()
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }


    //获取用户信息
    fun getUserInfo() {
        mView.showPageLoadingDialog()
        MineApi.getUserInfo {
            onSuccess {
                if (mView.isActive()) {
                    UserInfoSp.putUserName(it.username)
                    UserInfoSp.putUserPhoto(it.avatar)
                    UserInfoSp.putUserNickName(it.nickname)
                    UserInfoSp.putUserSex(it.gender)
                    UserInfoSp.putUserPhone(it.phone)
                    UserInfoSp.putUserProfile(it.profile)
                    ImageManager.loadImg(it.avatar, mView.imgUserPhoto)
                    mView.edUserName.setText(it.nickname)
                    when {
                        it.gender == 1 -> mView.edUserSex.text = "男"
                        it.gender == 0 -> mView.edUserSex.text = "女"
                        else -> mView.edUserSex.text = "未知"
                    }
                    mView.publish_ed_desc.setText(it.profile)
                }
                mView.hidePageLoadingDialog()
            }
            onFailed {
                mView.hidePageLoadingDialog()
                GlobalDialog.showError(mView.requireActivity(), it)
            }
        }

    }

}