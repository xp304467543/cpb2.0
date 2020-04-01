package com.fenghuang.caipiaobao.ui.moments.childern

import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.fragment_moments_hot_discuss.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe 热门讨论
 *
 */

class MomentsHotDiscussFragment : BaseMvpFragment<MomentsHotDiscussPresenter>() {

    var page = 1


    var rvDiscussAdapter: MomentsHotDiscussAdapter? = null

    override fun attachPresenter() = MomentsHotDiscussPresenter()

    override fun attachView() = mPresenter.attachView(this)

    override fun isShowToolBar() = false

    override fun isOverridePage() = false

    override fun getLayoutResID() = R.layout.fragment_moments_hot_discuss


    override fun initContentView() {
        rvDiscussAdapter = MomentsHotDiscussAdapter(getPageActivity())
        rvHotDiscuss.adapter = rvDiscussAdapter
        rvHotDiscuss.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
    }

    override fun initData() {
        mPresenter.getHotDiscuss()
    }

    override fun initEvent() {
        smartRefreshLayoutHotDiscuss.setOnRefreshListener {
            this.page = 1
            if (rvDiscussAdapter != null) rvDiscussAdapter?.clear()
            mPresenter.getHotDiscuss()
            rvDiscussAdapter?.isShowFooter = false
        }
        smartRefreshLayoutHotDiscuss.setOnLoadMoreListener {
            this.page++
            mPresenter.getHotDiscuss()
        }
    }


}