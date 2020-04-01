package com.fenghuang.baselib.web

import android.os.Bundle

/**
 * 嵌入到页面里的Fragment，不带导航栏，没有滑动返回
 */
class WebFragment : WebNavFragment() {

    companion object {
        fun newInstance(url: String?): WebFragment {
            val fragment = WebFragment()
            val bundle = Bundle()
            bundle.putString(WebConstant.KEY_WEB_URL, url)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun isProgressBarLoading() = false

    override fun isShowToolBar() = false

    override fun isSwipeBackEnable() = false
}