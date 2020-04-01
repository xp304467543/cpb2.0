package com.fenghuang.baselib.base.recycler

abstract class BaseRecyclerPresenter<V : BaseRecyclerContract.View> : BaseRecyclerContract.Presenter<V> {

    /**
     * MVP中的View层引用
     */
    protected lateinit var mView: V

    final override fun attachView(view: V) {
        this.mView = view
    }

    final override fun detachView() {
        dispose()
        onRelease()
    }


    /**
     * 销毁所有的请求
     */
    protected fun dispose() {

    }

    /**
     * 销毁页面
     */
    protected open fun onRelease() {

    }

}