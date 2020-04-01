package com.fenghuang.caipiaobao.ui.personal

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.personal.data.PersonalApi

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-01
 * @ Describe
 *
 */

class UserPersonalPagePresenter : BaseMvpPresenter<UserPersonalPage>(){


    fun getUserInfo(follId:String){
        PersonalApi.getUserPage(follId){
            if (mView.isActive()){
                onSuccess {
                    mView.initInfo(it)
                }
                onFailed { ToastUtils.show(it.getMsg().toString()) }
            }
        }
    }

}