@file:Suppress("UNCHECKED_CAST")

package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.web.utils.ZpWebView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryChildTypeAdapter
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryBetRuleResponse
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.google.android.material.tabs.TabLayout
import com.tencent.smtt.sdk.WebSettings

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe 规则
 *
 */

class LiveRoomBetToolsRulesChildFragment : BaseNormalFragment() {

    private var tvBetRule: TextView? = null

    private var rvRuleTypeSelect: RecyclerView? = null


    override fun getLayoutRes() = R.layout.fragment_live_bet_rule

    override fun initView(rootView: View?) {
        tvBetRule = rootView?.findViewById(R.id.tvBetRule)
        rvRuleTypeSelect = rootView?.findViewById(R.id.rvRuleTypeSelect)
    }

    override fun initData() {
        val data = arguments?.getSerializable("RuleContent") as ArrayList<LotteryBetRuleResponse>
        val index = arguments?.getInt("RuleIndex") ?: 0
        if (!data.isNullOrEmpty()) {
            tvBetRule?.text = HtmlCompat.fromHtml(data[index].play_rule_type_data?.get(0)?.play_rule_content
                    ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
            if (tvBetRule != null) {
                val ruleTypeAdapter = context?.let { LotteryChildTypeAdapter(it) }
                rvRuleTypeSelect?.adapter = ruleTypeAdapter
                rvRuleTypeSelect?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                if (!data[index].play_rule_type_data.isNullOrEmpty()) {
                    val list = arrayListOf<String>()
                    for (res in data[index].play_rule_type_data!!) {
                        list.add(res.play_rule_lottery_name ?: "")
                    }
                    ruleTypeAdapter?.addAll(list)
                }
                ruleTypeAdapter?.setOnItemClickListener { _, position ->
                    ruleTypeAdapter.changeBackground(position)
                    tvBetRule?.text = HtmlCompat.fromHtml(data[index].play_rule_type_data?.get(position)?.play_rule_content
                            ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
                }
            }
        }
    }

    companion object {
        fun newInstance(data: ArrayList<LotteryBetRuleResponse>?, index: Int): LiveRoomBetToolsRulesChildFragment {
            val fragment = LiveRoomBetToolsRulesChildFragment()
            val bundle = Bundle()
            bundle.putSerializable("RuleContent", data)
            bundle.putSerializable("RuleIndex", index)
            fragment.arguments = bundle
            return fragment
        }
    }

}       