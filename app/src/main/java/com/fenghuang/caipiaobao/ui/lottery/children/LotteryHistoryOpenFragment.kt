package com.fenghuang.caipiaobao.ui.lottery.children

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.fragment.BaseContentFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryChildTypeAdapter
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryHistoryOpenCodeAdapter
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryConstant
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryCodeHistoryResponse
import kotlinx.android.synthetic.main.child_fragment_history_open.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/2- 15:36
 * @ Describe 历史开奖
 *
 */
class LotteryHistoryOpenFragment : BaseContentFragment() {

    private var firstPage = 1

    override fun getContentResID() = R.layout.child_fragment_history_open

    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }

    var codeAdapter: LotteryHistoryOpenCodeAdapter? = null

    override fun initContentView() {
        smartRefreshLayoutLotteryHistoryCode.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutLotteryHistoryCode.setEnableLoadMore(true)//是否启用上拉加载功能
        smartRefreshLayoutLotteryHistoryCode.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutLotteryHistoryCode.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        codeAdapter = LotteryHistoryOpenCodeAdapter(getPageActivity(), arguments?.getString("lotteryId")!!, LotteryConstant.TYPE_1)
        rvLotteryHistoryCode.adapter = codeAdapter
        rvLotteryHistoryCode.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        initLotteryTypeSelect()

        smartRefreshLayoutLotteryHistoryCode.setOnLoadMoreListener {
            getCodeData(arguments?.getString("lotteryId")!!)
        }
    }

    override fun initData() {
        getCodeData(arguments?.getString("lotteryId")!!)
    }

    //初始化 具体 Type 号码 大小 单双 等切换
    private fun initLotteryTypeSelect() {
        val value = LinearLayoutManager(getPageActivity(), LinearLayoutManager.HORIZONTAL, false)
        val lotteryTypeAdapter = LotteryChildTypeAdapter(getPageActivity())
        val lotteryID = arguments?.getString("lotteryId")!!
        val data = if (lotteryID == "7" || lotteryID == "9" || lotteryID == "11" || lotteryID == "26" || lotteryID == "27") {
            arrayOf(LotteryConstant.TYPE_1, LotteryConstant.TYPE_2, LotteryConstant.TYPE_3, LotteryConstant.TYPE_4)
        } else if (lotteryID == "8") {
            arrayOf(LotteryConstant.TYPE_1, LotteryConstant.TYPE_5, LotteryConstant.TYPE_6)
        } else {
            arrayOf(LotteryConstant.TYPE_1, LotteryConstant.TYPE_2, LotteryConstant.TYPE_3, LotteryConstant.TYPE_7)
        }
        rvTypeSelect.layoutManager = value
        rvTypeSelect.adapter = lotteryTypeAdapter
        lotteryTypeAdapter.addAll(data)
        lotteryTypeAdapter.setOnItemClickListener { data, position ->
            lotteryTypeAdapter.changeBackground(position)
            if (codeAdapter != null) codeAdapter!!.changeDiffType(data)
        }
    }

    //获取历史开奖数据
    private fun getCodeData(id: String) {
        LotteryApi.getLotteryHistoryCode(id, TimeUtils.getToday(), firstPage) {
            onSuccess {
                if (smartRefreshLayoutLotteryHistoryCode != null) smartRefreshLayoutLotteryHistoryCode.finishLoadMore()
                if (it.isNotEmpty()){
                    codeAdapter!!.addAll(it)
                    setGone(tvOpenCodePlaceHolder)
                    firstPage++
                }else if (smartRefreshLayoutLotteryHistoryCode != null){
                    codeAdapter!!.isShowFooter = true
                    smartRefreshLayoutLotteryHistoryCode.setEnableLoadMore(false)
                }
            }
            onFailed {
                if (smartRefreshLayoutLotteryHistoryCode != null) smartRefreshLayoutLotteryHistoryCode.finishLoadMore()
                if (it.getMsg() == "暂无历史开奖记录" && smartRefreshLayoutLotteryHistoryCode != null) {
                    codeAdapter!!.isShowFooter = true
                    smartRefreshLayoutLotteryHistoryCode.setEnableLoadMore(false)
                    if (firstPage == 1) {
                        setVisible(tvHisHolder)
                        setGone(tvOpenCodePlaceHolder)
                    }
                }
            }
        }
    }

    //添加一条数据
    fun addItem(data: LotteryCodeHistoryResponse) {
        if (codeAdapter != null) {
            val list = codeAdapter!!.getAllData().toMutableList()
            list.add(0, data)
            codeAdapter!!.clear()
            codeAdapter!!.addAll(list)
        }
    }

    companion object {
        fun newInstance(anchorId: String): LotteryHistoryOpenFragment {
            val fragment = LotteryHistoryOpenFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", anchorId)
            fragment.arguments = bundle
            return fragment
        }
    }

    // ten 7  9  11  26 27                       1  10                       xg 8

}