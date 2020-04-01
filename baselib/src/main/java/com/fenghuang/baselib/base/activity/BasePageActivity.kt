package com.fenghuang.baselib.base.activity

import com.fenghuang.baselib.R
import com.fenghuang.baselib.base.fragment.BaseFragment

/**
 * 专门用来填充Fragment的Activity
 */
abstract class BasePageActivity(override val layoutResID: Int = R.layout.base_activity_page) : BaseActivity() {

    final override fun initView() {
        loadRootFragment(R.id.pageContainer, getPageFragment())
        initPageView()
    }

    protected open fun initPageView() {
    }

    abstract fun getPageFragment(): BaseFragment


}