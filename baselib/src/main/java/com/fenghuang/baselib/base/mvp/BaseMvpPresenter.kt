package com.fenghuang.baselib.base.mvp

import com.fenghuang.baselib.base.basic.IMvpContract


/**
 * MVP的Presenter层
 */
abstract class BaseMvpPresenter<V : IMvpContract.View> : IMvpContract.Presenter<V> {

    /**
     * MVP中的View层引用
     */
    protected lateinit var mView: V

    override fun attachView(view: V) {
        this.mView = view
    }

    override fun detachView() {
    }

    /**
     * 消费所有事件
     */
    fun dispose() {

    }
}