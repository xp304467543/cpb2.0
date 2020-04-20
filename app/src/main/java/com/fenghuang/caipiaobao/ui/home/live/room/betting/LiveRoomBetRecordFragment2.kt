package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.round.RoundLinearLayout
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter.LiveRoomRecordAdapter
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe  未结算
 *
 */

class LiveRoomBetRecordFragment2 : BaseNormalFragment() {

    private var index = 1

    private var adapter: LiveRoomRecordAdapter? = null

    private var rvBetRecord: RecyclerView? = null

    private var smBetRecord: SmartRefreshLayout? = null

    private var tvBetRecordHolder: TextView? = null

    private var recordTop: RoundLinearLayout? = null

    override fun getLayoutRes() = R.layout.fragment_live_bet_record_child

    override fun initView(rootView: View?) {
        rvBetRecord = rootView?.findViewById(R.id.rvBetRecord)
        smBetRecord = rootView?.findViewById(R.id.smBetRecord)
        tvBetRecordHolder = rootView?.findViewById(R.id.tvBetRecordHolder)
        recordTop = rootView?.findViewById(R.id.recordTop)
        adapter = context?.let { LiveRoomRecordAdapter(it) }
        rvBetRecord?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvBetRecord?.adapter = adapter
    }

    override fun initData() {
        getResponse()
        smBetRecord?.setOnRefreshListener {
            index = 1
            adapter?.clear()
            rvBetRecord?.removeAllViews()
            getResponse()
        }
        smBetRecord?.setOnLoadMoreListener {
            index++
            getResponse()
        }
    }


    private fun getResponse() {
        val res = LotteryApi.getLotteryBetHistory(2, index)
        res.onSuccess {
            smBetRecord?.finishLoadMore()
            smBetRecord?.finishRefresh()
            if (!it.isNullOrEmpty()) {
                ViewUtils.setVisible(recordTop)
                ViewUtils.setGone(tvBetRecordHolder)
                adapter?.addAll(it)
            } else {
                if (index == 1) {
                    adapter?.clear()
                    rvBetRecord?.removeAllViews()
                    ViewUtils.setVisible(tvBetRecordHolder)
                    smBetRecord?.setEnableAutoLoadMore(false)
                    smBetRecord?.setEnableRefresh(false)
                } else {
                    index--
                    smBetRecord?.setEnableAutoLoadMore(false)
                }
            }

        }
        res.onFailed {
            smBetRecord?.finishLoadMore()
            smBetRecord?.finishRefresh()
            ToastUtils.showError(it.getMsg())
        }

    }

}