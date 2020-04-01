package com.fenghuang.caipiaobao.ui.home.live.room

import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveRankList
import kotlinx.android.synthetic.main.fragment_child_live_rank.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-22
 * @ Describe 打赏榜
 *
 */

class LiveRoomRankFragment(private val anchorId: String) : BaseMvpFragment<LiveRoomRankPresenter>() {


    override fun attachPresenter() = LiveRoomRankPresenter()

    override fun attachView() = mPresenter.attachView(this)

    override fun getLayoutResID() = R.layout.fragment_child_live_rank


    override fun initContentView() {
    }

    override fun initData() {
        mPresenter.getAllData(anchorId)
    }

    fun initRankRewardList(data: List<HomeLiveRankList>) {
        val adapter = LiveRoomRankAdapter(context!!)
        rvLiveRankList.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        rvLiveRankList.adapter = adapter
        adapter.addAll(data)

    }
}