package com.fenghuang.baselib.base.activity

/**
 * 可以滑动返回的Activity
 * 默认带有导航栏，可以通过[isShowToolBar]方法移除
 */
open class BaseSwipeBackActivity : BaseNavActivity() {

    /**
     * 支持滑动返回
     */
    final override fun isSwipeBackEnable(): Boolean = true
}