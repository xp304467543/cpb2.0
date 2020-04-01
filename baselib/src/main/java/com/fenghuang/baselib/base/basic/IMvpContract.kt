package com.fenghuang.baselib.base.basic

interface IMvpContract {

    interface View : IMvpView

    interface Presenter<V : View> : IMvpPresenter<V>

    interface Model : IMvpModel
}