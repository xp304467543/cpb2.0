package com.fenghuang.caipiaobao.ui.mine.children

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.utils.FastClickUtils
import kotlinx.android.synthetic.main.fragment_mine_feedback.*
import kotlinx.android.synthetic.main.fragment_mine_presonal.publish_ed_desc
import kotlinx.android.synthetic.main.fragment_mine_presonal.publish_text_num
import java.util.regex.Pattern









/**
 *
 * @ Author  QinTian
 * @ Date  2019/10/9- 14:50
 * @ Describe 用户反馈
 *
 */

class MineFeedBackFragment : BaseMvpFragment<MineFeedBackPresenter>() {

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MineFeedBackPresenter()

    override fun getContentResID() = R.layout.fragment_mine_feedback

    override fun getPageTitle() = getString(R.string.mine_feed_back)

    override fun isSwipeBackEnable() = true

    override fun isShowBackIconWhite() = false

    override fun isOverridePage() = false


    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
        mPresenter.initEditFeedBack(publish_ed_desc, publish_text_num)
        publish_ed_desc.filters = arrayOf(inputFilter)
    }

    override fun initEvent() {

        btSubMit.setOnClickListener {
            if (!TextUtils.isEmpty(publish_ed_desc.text)) {
                if (FastClickUtils.isFastClick()) {
                    mPresenter.subMitAdv(publish_ed_desc.text.toString())
                }
            } else {
                ToastUtils.showInfo("请输入反馈内容")
            }
        }

    }

//    private var inputFilter: InputFilter = object : InputFilter {
//        var pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]|")
//        override fun filter(charSequence: CharSequence, i: Int, i1: Int, spanned: Spanned, i2: Int, i3: Int): CharSequence? {
//            val matcher = pattern.matcher(charSequence)
//            return if (!matcher.find()) {
//                null
//            } else {
//                ToastUtils.showNormal("只能输入汉字,英文，数字")
//                ""
//            }
//        }
//    }


    private  var inputFilter: InputFilter = object : InputFilter {
         var emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)

        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
            val emojiMatcher = emoji.matcher(source)
            if (emojiMatcher.find()) {
                //                    Toast.makeText(MainActivity.this,"不支持输入表情", 0).show();
                ToastUtils.showNormal("不支持输入表情")
                return ""
            }
            return null
        }
    }



}