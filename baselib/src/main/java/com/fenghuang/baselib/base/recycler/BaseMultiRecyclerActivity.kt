package com.fenghuang.baselib.base.recycler

abstract class BaseMultiRecyclerActivity<P : BaseRecyclerPresenter<*>> : BaseMultiRecyclerNavActivity<P>() {

    override fun isShowToolBar(): Boolean = false

    override fun isSwipeBackEnable(): Boolean = false
}