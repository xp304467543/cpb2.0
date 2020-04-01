package com.fenghuang.baselib.base.basic

/**
 * MVP的P层基类
 */

interface IMvpPresenter<V : IMvpContract.View> {

    /**
     * 绑定视图
     */
    fun attachView(view: V)

    /**
     * 解绑视图
     */
    fun detachView()
}