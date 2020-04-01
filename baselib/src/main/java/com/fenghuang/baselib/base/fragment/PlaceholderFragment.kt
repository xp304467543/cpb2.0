package com.fenghuang.baselib.base.fragment

import android.os.Bundle
import com.fenghuang.baselib.R
import com.fenghuang.baselib.utils.StatusBarUtils
import kotlinx.android.synthetic.main.base_fragment_placeholder.*


/**
 * 用于占位的Fragment
 */
open class PlaceholderFragment : BaseNavFragment() {

    companion object {
        private const val INTENT_TITLE: String = "INTENT_TITLE"
        private const val INTENT_DESC: String = "INTENT_DESC"
        private const val INTENT_TOOL_BAR: String = "INTENT_TOOL_BAR"
        private const val INTENT_MAIN_PAGE: String = "INTENT_MAIN_PAGE"
        private const val PLACEHOLDER: String = "PLACEHOLDER"
        private const val STATUS_BAR_IS_BLACK: String = "PLACEHOLDER"

        @JvmStatic
        fun newInstance(
                title: String = "暂无数据",
                description: String = "暂无数据",
                isShowToolbar: Boolean = true,
                isMainPage: Boolean = false,
                placeholder: Int = 0,
                statusBarIsBlack: Boolean = false
        ): BaseFragment {
            val bundle = Bundle()
            bundle.putString(INTENT_TITLE, title)
            bundle.putString(INTENT_DESC, description)
            bundle.putBoolean(INTENT_TOOL_BAR, isShowToolbar)
            bundle.putBoolean(INTENT_MAIN_PAGE, isMainPage)
            bundle.putInt(PLACEHOLDER, placeholder)
            bundle.putBoolean(STATUS_BAR_IS_BLACK, statusBarIsBlack)
            val fragment = PlaceholderFragment()
            fragment.arguments = bundle
            return fragment
        }

        fun newInstance(placeholder: Int): BaseFragment {
            val bundle = Bundle()
            bundle.putInt(PLACEHOLDER, placeholder)
            val fragment = PlaceholderFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getContentResID(): Int {
        return R.layout.base_fragment_placeholder
    }

    override fun getPageTitle(): String? {
        return arguments?.getString(INTENT_TITLE)
    }

    override fun isShowToolBar(): Boolean = arguments?.getBoolean(INTENT_TOOL_BAR) ?: true

    override fun isMainPage(): Boolean = arguments?.getBoolean(INTENT_MAIN_PAGE) ?: false

    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), arguments?.getBoolean(STATUS_BAR_IS_BLACK)
                ?: false)
        val placeholder = arguments?.getInt(PLACEHOLDER)
        if (placeholder != null && placeholder != 0) {
            ivPlaceholder.setImageResource(placeholder)
        }
    }
}