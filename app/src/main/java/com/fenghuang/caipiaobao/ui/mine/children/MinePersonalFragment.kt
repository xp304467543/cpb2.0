package com.fenghuang.caipiaobao.ui.mine.children

import android.net.Uri
import android.os.Build
import android.text.InputFilter
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.widget.IosBottomListWindow
import kotlinx.android.synthetic.main.fragment_mine_presonal.*
import java.util.regex.Pattern


/**
 *
 * @ Author  QinTian
 * @ Date  2019/10/9- 17:46
 * @ Describe 个人资料
 *
 */

class MinePersonalFragment : BaseMvpFragment<MinePersonalPresenter>() {

    var filePath:String? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MinePersonalPresenter()

    override fun isOverridePage() = false

    override fun getContentResID() = R.layout.fragment_mine_presonal

    override fun getPageTitle() = getString(R.string.mine_contact_personal)

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
        mPresenter.initEditPersonal(publish_ed_desc, publish_text_num)
        setEditTextInputSpace(edUserName)
    }

    override fun initData() {
        mPresenter.getUserInfo()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initEvent() {
        imgSetPhoto.setOnClickListener {
            mPresenter.getPhotoFromPhone(getPageActivity())
            //相机或者相册选择
        }
        btUpLoadUserInfo.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                mPresenter.upLoadPersonalInfo(edUserName.text.toString(), if (edUserSex.text.toString() == "男") 1 else 2, publish_ed_desc.text.toString())
            }
        }

        edUserSex.setOnClickListener {
            IosBottomListWindow(getPageActivity())
                    .setTitle("选择性别")
                    .setItem("男") {
                        edUserSex.text = "男"
                    }
                    .setItem("女") {
                        edUserSex.text = "女"
                    }
                    .setCancelButton("取消")
                    .show()
        }
    }

   fun setImgAvatar(uri: Uri){
       ImageManager.loadUri(uri, findView(R.id.imgUserPhoto))
   }
    /**
     * 禁止EditText输入空格、表情和换行符以及特殊符号&&
     *
     * @param editText EditText输入框
     */
    private fun setEditTextInputSpace(editText: EditText) {
        val emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)

        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            val emojiMatcher = emoji.matcher(source)
            //禁止特殊符号
            val speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
            val pattern = Pattern.compile(speChat)
            val matcher = pattern.matcher(source.toString())


            //禁止输入空格
            if (source == " " || source.toString().contentEquals("\n")) {
                ""
                //禁止输入表情
            } else if (emojiMatcher.find()) {
                ""
            } else if (matcher.find()) {
                ""
            } else {
                null
            }
        }
        editText.filters = arrayOf(filter, InputFilter.LengthFilter(10))
    }

}