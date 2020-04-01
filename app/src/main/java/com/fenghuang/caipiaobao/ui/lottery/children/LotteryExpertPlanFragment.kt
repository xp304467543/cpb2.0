package com.fenghuang.caipiaobao.ui.lottery.children

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.fragment.BaseContentFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryExpertPlanFragmentAdapter
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import kotlinx.android.synthetic.main.child_fragment_expert_plan.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/2- 15:36
 * @ Describe 专家计划
 *
 */

class LotteryExpertPlanFragment : BaseContentFragment() {

    var page = 1

    var limit = "10"

    var lottery_id = "-1"

    var issue = "-1"

    var adapter: LotteryExpertPlanFragmentAdapter? = null

    override fun getContentResID() = R.layout.child_fragment_expert_plan


    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }

    override fun initContentView() {
        lotteryExpertPlanSmartRefreshLayout.setOnRefreshListener {
            this.page = 1
            lotteryExpertPlanSmartRefreshLayout.setEnableLoadMore(true)
            getExpertPlanData()
        }
        lotteryExpertPlanSmartRefreshLayout.setOnLoadMoreListener {
            this.page++
            getExpertPlanData()
        }

        adapter = LotteryExpertPlanFragmentAdapter(requireActivity())
        rcExpertPlan.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rcExpertPlan.adapter = adapter
    }

    //获取专家计划
    fun getExpertPlan(lottery_id: String, issue: String) {
        this.lottery_id = lottery_id
        this.issue = issue
        getExpertPlanData()
    }

    private fun getExpertPlanData() {
        if (this.lottery_id != "-1" && this.issue != "-1") {
            LotteryApi.getExpertPlan(this.lottery_id, this.issue, limit, page) {
                if (isAdded){
                    onSuccess {
                        if (lotteryExpertPlanSmartRefreshLayout != null) {
                            lotteryExpertPlanSmartRefreshLayout.finishRefresh()
                            lotteryExpertPlanSmartRefreshLayout.finishLoadMore()
                        }
                        if (adapter != null && it.isNotEmpty()) {
                            adapter?.addAll(it)
                        } else if (lotteryExpertPlanSmartRefreshLayout != null) {
                            adapter!!.isShowFooter = true
                            lotteryExpertPlanSmartRefreshLayout.setEnableLoadMore(false)
                        }
                        setGone(expertPlaceHolder)
                    }
                    onFailed {
                        if (lotteryExpertPlanSmartRefreshLayout != null) {
                            lotteryExpertPlanSmartRefreshLayout.finishRefresh()
                            lotteryExpertPlanSmartRefreshLayout.finishLoadMore()
                        }
                        if (page != 1) page--
                        ToastUtils.show("获取数据失败")
                        setGone(expertPlaceHolder)
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(lotteryId: String): LotteryExpertPlanFragment {
            val fragment = LotteryExpertPlanFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", lotteryId)
            fragment.arguments = bundle
            return fragment
        }
    }
}