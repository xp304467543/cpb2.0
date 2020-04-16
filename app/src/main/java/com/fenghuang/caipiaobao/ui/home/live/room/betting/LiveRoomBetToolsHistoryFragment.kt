package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryChildTypeAdapter
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryHistoryOpenCodeAdapter
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryConstant
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.child_fragment_history_open.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe 历史开奖
 *
 */

class LiveRoomBetToolsHistoryFragment : BaseNormalFragment() {

    private var firstPage = 1

     private var smartRefreshLayoutLotteryHistoryCode:SmartRefreshLayout?=null

    private var rvLotteryHistoryCode:RecyclerView?=null

    private var rvTypeSelect:RecyclerView?=null


    var codeAdapter: LotteryHistoryOpenCodeAdapter? = null

    override fun getLayoutRes() = R.layout.child_fragment_history_open

    override fun initView(rootView: View?) {
        smartRefreshLayoutLotteryHistoryCode = rootView?.findViewById(R.id.smartRefreshLayoutLotteryHistoryCode)
        rvLotteryHistoryCode  = rootView?.findViewById(R.id.rvLotteryHistoryCode)
        rvTypeSelect = rootView?.findViewById(R.id.rvTypeSelect)
        smartRefreshLayoutLotteryHistoryCode?.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutLotteryHistoryCode?.setEnableLoadMore(true)//是否启用上拉加载功能
        smartRefreshLayoutLotteryHistoryCode?.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutLotteryHistoryCode?.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        codeAdapter = context?.let { LotteryHistoryOpenCodeAdapter(it, arguments?.getString("lotteryId")!!, LotteryConstant.TYPE_1) }
        rvLotteryHistoryCode?.adapter = codeAdapter
        rvLotteryHistoryCode?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        initLotteryTypeSelect()

        smartRefreshLayoutLotteryHistoryCode?.setOnLoadMoreListener {
            getCodeData(arguments?.getString("lotteryId")!!)
        }
    }


    override fun initData() {
        getCodeData(arguments?.getString("lotteryId")!!)
    }

    //初始化 具体 Type 号码 大小 单双 等切换
    private fun initLotteryTypeSelect() {
        val value = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val lotteryTypeAdapter = context?.let { LotteryChildTypeAdapter(it) }
        val lotteryID = arguments?.getString("lotteryId")!!
        val data = if (lotteryID == "7" || lotteryID == "9" || lotteryID == "11" || lotteryID == "26" || lotteryID == "27") {
            arrayOf(LotteryConstant.TYPE_1, LotteryConstant.TYPE_2, LotteryConstant.TYPE_3, LotteryConstant.TYPE_4)
        } else if (lotteryID == "8") {
            arrayOf(LotteryConstant.TYPE_1, LotteryConstant.TYPE_5, LotteryConstant.TYPE_6)
        } else {
            arrayOf(LotteryConstant.TYPE_1, LotteryConstant.TYPE_2, LotteryConstant.TYPE_3, LotteryConstant.TYPE_7)
        }
        rvTypeSelect?.layoutManager = value
        rvTypeSelect?.adapter = lotteryTypeAdapter
        lotteryTypeAdapter?.addAll(data)
        lotteryTypeAdapter?.setOnItemClickListener { data, position ->
            lotteryTypeAdapter.changeBackground(position)
            if (codeAdapter != null) codeAdapter!!.changeDiffType(data)
        }
    }

    //获取历史开奖数据
    private fun getCodeData(id: String) {
        LotteryApi.getLotteryHistoryCode(id, TimeUtils.getToday(), firstPage) {
            onSuccess {
                if (smartRefreshLayoutLotteryHistoryCode != null) smartRefreshLayoutLotteryHistoryCode?.finishLoadMore()
                if (it.isNotEmpty()) {
                    codeAdapter!!.addAll(it)
                    ViewUtils.setGone(tvOpenCodePlaceHolder)
                    firstPage++
                } else if (smartRefreshLayoutLotteryHistoryCode != null) {
                    codeAdapter!!.isShowFooter = true
                    smartRefreshLayoutLotteryHistoryCode?.setEnableLoadMore(false)
                }
            }
            onFailed {
                if (smartRefreshLayoutLotteryHistoryCode != null) smartRefreshLayoutLotteryHistoryCode?.finishLoadMore()
                if (it.getMsg() == "暂无历史开奖记录" && smartRefreshLayoutLotteryHistoryCode != null) {
                    codeAdapter!!.isShowFooter = true
                    smartRefreshLayoutLotteryHistoryCode?.setEnableLoadMore(false)
                    if (firstPage == 1) {
                        ViewUtils.setVisible(tvHisHolder)
                        ViewUtils.setGone(tvOpenCodePlaceHolder)
                    }
                }
            }
        }
    }


    companion object {
        fun newInstance(anchorId: String): LiveRoomBetToolsHistoryFragment {
            val fragment = LiveRoomBetToolsHistoryFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", anchorId)
            fragment.arguments = bundle
            return fragment
        }
    }

}