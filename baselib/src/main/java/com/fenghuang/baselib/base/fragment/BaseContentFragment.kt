package com.fenghuang.baselib.base.fragment

/**
 *
 * 没有导航栏的Fragment页面，专门用来填充页面
 */
abstract class BaseContentFragment : BaseNavFragment() {

    /**
     * 禁用ToolBar的相关操作,如果要使用Toolbar,请使用其他相关Fragment
     */
    final override fun isShowToolBar(): Boolean = false

    final override fun isShowBackIcon(): Boolean {
        return super.isShowBackIcon()
    }

    final override fun getPageTitle(): String? {
        return super.getPageTitle()
    }
}