package com.fenghuang.caipiaobao.ui.home.news

import android.annotation.SuppressLint
import androidx.core.text.HtmlCompat
import com.fenghuang.baselib.base.fragment.BaseNavFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.tencent.smtt.sdk.WebSettings
import kotlinx.android.synthetic.main.fragment_news_info.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-02
 * @ Describe 直播
 *
 */

class NewsInfoFragment(var infoId: String) : BaseNavFragment() {


    override fun getContentResID() = R.layout.fragment_news_info

    override fun isOverridePage() = false

    override fun isSwipeBackEnable() = true

    override fun getPageTitle() = "最新资讯"

    override fun isShowBackIconWhite() = false

    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }


    override fun initData() {
        getNewsInfo()
    }

    @SuppressLint("SetTextI18n", "SetJavaScriptEnabled")
    private fun getNewsInfo() {
        HomeApi.getNewsInfo(infoId) {
            onSuccess {
                if (!it.isNullOrEmpty()) {
                    if (isSupportVisible){
                        tvNewsTitle.text = it[0].title
                        tvNewsInfo.text = it[0].source + "   " + it[0].timegap + "    " + TimeUtils.longToDateStringTime(it[0].createtime.toLong())
                        web_copy.loadDataWithBaseURL(null, it[0].detail,"text/html","utf-8", null)
                        web_copy.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                        web_copy.settings.javaScriptEnabled = true
                        web_copy.setBackgroundColor(0)
                    }
                } else ToastUtils.show("暂无内容")
            }
            onFailed {
                ToastUtils.show(it.getMsg().toString())
            }
        }
    }
}