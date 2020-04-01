package com.fenghuang.baselib.base.mvp

import android.os.Bundle
import com.fenghuang.baselib.base.activity.BaseNavActivity
import com.fenghuang.baselib.base.basic.IMvpContract

abstract class BaseMvpActivity<P : IMvpContract.Presenter<*>> : BaseNavActivity(), IMvpContract.View {

    protected open lateinit var mPresenter: P

    override fun isActive(): Boolean = !isDestroyed

    override fun onCreate(savedInstanceState: Bundle?) {
        mPresenter = attachPresenter()
        attachView()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    abstract fun attachView()

    abstract fun attachPresenter(): P
}