package com.fenghuang.caipiaobao.ui.home.live.room

import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAdvanceList
import com.fenghuang.caipiaobao.ui.home.data.UpDateAttention
import com.fenghuang.caipiaobao.ui.home.data.UpDatePreView
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_child_live_advance.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-22
 * @ Describe 预告
 *
 */

class LiveRoomAdvanceFragment(private val anchorId: String, private val type: String) : BaseMvpFragment<LiveRoomAdvancePresenter>() {

    var adapter: LiveRoomAdvanceAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LiveRoomAdvancePresenter()

    override fun getLayoutResID() = R.layout.fragment_child_live_advance

    override fun isRegisterRxBus() = true

    override fun initData() {
        mPresenter.getAllData(type)
    }

    fun initAdvanceRecycle(data: ArrayList<HomeLiveAdvanceList>) {

        adapter = LiveRoomAdvanceAdapter(context!!, true)
        tvLiveRoomAdvance.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
        tvLiveRoomAdvance.adapter = adapter
        adapter?.addAll(data)

    }

    //更新关注
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun UpDateAttention(eventBean: UpDateAttention) {
        if (adapter?.getAllData().isNullOrEmpty()) return
        val bean = adapter?.getAllData()
        if (bean != null) {
            for (i in bean) {
                for (o in i.bean) {
                    if (eventBean.anchorId == o.aid) {
                        o.isFollow = eventBean.boolean
                    }
                }
            }
            adapter?.clear()
            tvLiveRoomAdvance.removeAllViews()
            adapter?.addAll(bean)
        }

    }

    //更新预告
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataUserAvatar(eventBean: UpDatePreView) {
        if (isVisible) {
            adapter?.clear()
            tvLiveRoomAdvance.removeAllViews()
            mPresenter.getAllData(type)
        }

    }


}