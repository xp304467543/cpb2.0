package com.fenghuang.caipiaobao.ui.moments.childern

import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.fragment_moments_anchor_list.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe
 *
 */

class MomentsAnchorFragment(var anchor_id: String, var isChild: Boolean = false) : BaseMvpFragment<MomentsAnchorPresenter>() {

    var page = 1

    var anchorListAdapter: MomentsAnchorAdapter? = null

    override fun attachPresenter() = MomentsAnchorPresenter()

    override fun attachView() = mPresenter.attachView(this)

    override fun isShowToolBar() = false

    override fun isOverridePage() = false

    override fun getLayoutResID() = R.layout.fragment_moments_anchor_list

    override fun initContentView() {
        anchorListAdapter = MomentsAnchorAdapter(getPageActivity())
        rvAnchorList.adapter = anchorListAdapter
        rvAnchorList.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
    }

    override fun initData() {
        if (!isChild) {
            mPresenter.getAnchorList(anchor_id, true)
        } else mPresenter.getAnchorDynamic(anchor_id, true)

    }

    override fun initEvent() {
        smartRefreshLayoutAnchorList.setOnRefreshListener {
            this.page = 1
            if (anchorListAdapter != null) anchorListAdapter?.clear()
            if (!isChild) {
                mPresenter.getAnchorList(anchor_id, false)
            } else mPresenter.getAnchorDynamic(anchor_id, false)
            anchorListAdapter?.isShowFooter = false
        }
        smartRefreshLayoutAnchorList.setOnLoadMoreListener {
            this.page++
            if (!isChild) {
                mPresenter.getAnchorList(anchor_id, false)
            } else mPresenter.getAnchorDynamic(anchor_id, false)
        }
    }

}