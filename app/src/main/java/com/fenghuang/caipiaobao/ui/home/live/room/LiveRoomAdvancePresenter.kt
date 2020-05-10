package com.fenghuang.caipiaobao.ui.home.live.room

import android.view.View
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAdvanceList
import com.fenghuang.caipiaobao.ui.home.data.HomeLivePreResponse
import com.fenghuang.caipiaobao.utils.JsonUtils
import kotlinx.android.synthetic.main.fragment_child_live_advance.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-28
 * @ Describe
 *
 */

class LiveRoomAdvancePresenter : BaseMvpPresenter<LiveRoomAdvanceFragment>() {

    fun getAllData(type: String) {
        HomeApi.getLiveAdvanceList(type) {
            if (mView.isActive() && mView.isSupportVisible) {
                onSuccess {
                  if ( mView.spLiveAdvanceLoading!=null)  mView.spLiveAdvanceLoading.visibility = View.GONE
                    if (!it.data!!.isJsonNull && it.data.toString().length > 5) {
                        val dataList = ArrayList<HomeLiveAdvanceList>()
                        //获取到map，取值
                        for (entry in it.data.asJsonObject.entrySet()) {
                            val bean = JsonUtils.fromJson(entry.value, Array<HomeLivePreResponse>::class.java)
                            dataList.add(HomeLiveAdvanceList(entry.key, bean))
                        }
                        mView.initAdvanceRecycle(dataList)
                    } else mView.setVisible(mView.tvHolderAd)
                }

                onFailed {
                    mView.spLiveAdvanceLoading.visibility = View.GONE
                    ToastUtils.show(it.getMsg().toString())
                }
            }
        }
    }
}

