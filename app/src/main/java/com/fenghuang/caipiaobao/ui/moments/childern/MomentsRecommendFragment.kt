package com.fenghuang.caipiaobao.ui.moments.childern

import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.moments.data.MomentsRecommend
import kotlinx.android.synthetic.main.fragment_recommend.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe
 *
 */

class MomentsRecommendFragment : BaseMvpFragment<MomentsRecommendPresenter>() {

    var adapter: MomentsRecommendAdapter? = null

    override fun attachPresenter() = MomentsRecommendPresenter()

    override fun attachView() = mPresenter.attachView(this)

    override fun getLayoutResID() = R.layout.fragment_recommend


    override fun initContentView() {
        adapter = MomentsRecommendAdapter(requireActivity())
        rvRecommend.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        rvRecommend.adapter = adapter
    }

    override fun initData() {
        mPresenter.getData()
    }

    fun upDateRecycle(data: List<MomentsRecommend>) {
        adapter?.addAll(data)
    }

}