package com.fenghuang.caipiaobao.ui.home.live.children

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAdvanceList
import com.fenghuang.caipiaobao.ui.home.data.HomeLivePreResponse
import com.fenghuang.caipiaobao.utils.JsonUtils
import kotlinx.android.synthetic.main.fragment_advance.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-09
 * @ Describe
 *
 */

class LiveAdvanceFragmentPresenter : BaseMvpPresenter<LiveAdvanceFragment>() {


    fun getTitle() {
        HomeApi.getAdvanceTitle {
            if (mView.isActive()) {
                onSuccess { mView.initTitle(it) }
                onFailed { ToastUtils.show(it.getMsg().toString()) }
            }

        }
    }

    fun getContent(type:String){
        mView.setVisible(mView.holder)
        HomeApi.getLiveAdvanceList(type = type){
            onSuccess {
                mView.setGone(mView.holder)
                mView.setGone(mView.emptyHolder)
                if (!it.data!!.isJsonNull && it.data.toString().length > 5) {
                    val dataList = ArrayList<HomeLiveAdvanceList>()
                    //获取到map，取值
                    for (entry in it.data.asJsonObject.entrySet()) {
                        val bean = JsonUtils.fromJson(entry.value, Array<HomeLivePreResponse>::class.java)
                        dataList.add(HomeLiveAdvanceList(entry.key, bean))
                    }
                    mView.initAdvanceRecycle(dataList,type)
                }else {
                    mView.setEmpty()
                }
            }
            onFailed {
                mView.setGone(mView.holder)
            }
        }
    }

}