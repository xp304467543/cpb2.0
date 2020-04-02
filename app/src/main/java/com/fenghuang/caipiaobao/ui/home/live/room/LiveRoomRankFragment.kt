package com.fenghuang.caipiaobao.ui.home.live.room

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.IntentConstant
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveRankList
import kotlinx.android.synthetic.main.fragment_child_live_rank.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-22
 * @ Describe 打赏榜
 *
 */

class LiveRoomRankFragment : BaseMvpFragment<LiveRoomRankPresenter>() {


    override fun attachPresenter() = LiveRoomRankPresenter()

    override fun attachView() = mPresenter.attachView(this)

    override fun getLayoutResID() = R.layout.fragment_child_live_rank


    override fun initContentView() {
    }

    override fun initData() {
        mPresenter.getAllData(arguments?.getString(IntentConstant.LIVE_ROOM_ANCHOR_ID)
                ?: "0")
    }

    fun initRankRewardList(data: List<HomeLiveRankList>) {
        val adapter = LiveRoomRankAdapter(context!!)
        rvLiveRankList.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        rvLiveRankList.adapter = adapter
        adapter.addAll(data)

    }


    companion object {
        fun newInstance(anchorId: String): LiveRoomRankFragment {
            val fragment = LiveRoomRankFragment()
            val bundle = Bundle()
            bundle.putString(IntentConstant.LIVE_ROOM_ANCHOR_ID, anchorId)
            fragment.arguments = bundle
            return fragment
        }
    }
}