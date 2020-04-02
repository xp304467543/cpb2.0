package com.fenghuang.caipiaobao.ui.moments.childern

import android.os.Bundle
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

class MomentsAnchorFragment : BaseMvpFragment<MomentsAnchorPresenter>() {

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
        if (!arguments?.getBoolean("isChild")!!) {
            mPresenter.getAnchorList(arguments?.getString("anchor_id")?:"0", true)
        } else mPresenter.getAnchorDynamic(arguments?.getString("anchor_id")?:"0", true)

    }

    override fun initEvent() {
        smartRefreshLayoutAnchorList.setOnRefreshListener {
            this.page = 1
            if (anchorListAdapter != null) anchorListAdapter?.clear()
            if (!arguments?.getBoolean("isChild")!!) {
                mPresenter.getAnchorList(arguments?.getString("anchor_id")?:"0", false)
            } else mPresenter.getAnchorDynamic(arguments?.getString("anchor_id")?:"0", false)
            anchorListAdapter?.isShowFooter = false
        }
        smartRefreshLayoutAnchorList.setOnLoadMoreListener {
            this.page++
            if (!arguments?.getBoolean("isChild")!!) {
                mPresenter.getAnchorList(arguments?.getString("anchor_id")?:"0", false)
            } else mPresenter.getAnchorDynamic(arguments?.getString("anchor_id")?:"0", false)
        }
    }
    companion object{
        fun newInstance(anchor_id: String,isChild:Boolean=false): MomentsAnchorFragment {
            val fragment = MomentsAnchorFragment()
            val bundle = Bundle()
            bundle.putString("anchor_id", anchor_id)
            bundle.putBoolean("isChild", isChild)
            fragment.arguments = bundle
            return fragment
        }
    }
}