package com.fenghuang.caipiaobao.ui.mine.children

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.fresco.helper.Phoenix
import com.fenghuang.baselib.base.fragment.BaseContentFragment
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.IntentConstant
import com.fenghuang.caipiaobao.data.bean.BaseApiBean
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MineBillBean
import com.fenghuang.caipiaobao.ui.mine.data.MineBillResponse
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.JsonUtils
import kotlinx.android.synthetic.main.fragment_child_attention.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-04
 * @ Describe 我的关注
 *
 */

class MineBillFragmentChild : BaseContentFragment() {

    var page = 1

    private lateinit var balanceAdapter: BalanceAdapter

    private lateinit var exchangeAdapter: ExchangeAdapter

    private lateinit var rewardAdapter: RewardAdapter

    private lateinit var betRecordAdapter: BetRecordAdapter

    override fun getContentResID() = R.layout.fragment_child_attention

    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }

    override fun initContentView() {
        attentionSmartRefreshLayout.setEnableRefresh(false)//是否启用下拉刷新功能
        attentionSmartRefreshLayout.setEnableLoadMore(false)//是否启用上拉加载功能
        attentionSmartRefreshLayout.setEnableOverScrollBounce(true)//是否启用越界回弹
        attentionSmartRefreshLayout.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        rvAttention.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        when (arguments?.getInt(IntentConstant.LIVE_ROOM_LOTTERY_TYPE, 1)) {
            1 -> {
                balanceAdapter = BalanceAdapter()
                rvAttention.adapter = balanceAdapter
            }
            2 -> {
                betRecordAdapter = BetRecordAdapter()
                rvAttention.adapter = betRecordAdapter
            }
            3 -> {
                exchangeAdapter = ExchangeAdapter()
                rvAttention.adapter = exchangeAdapter
            }
            else -> {
                rewardAdapter = RewardAdapter()
                rvAttention.adapter = rewardAdapter
            }
        }


    }

    override fun initData() {
        initDataAdapter(true)
    }

    private fun initDataAdapter(isFirst: Boolean) {
        showPageLoadingDialog()
        when (arguments?.getInt(IntentConstant.LIVE_ROOM_LOTTERY_TYPE, 1)) {
            1 ->
                MineApi.getBalance(page) {
                    onSuccess {
                        val data = parseResult(it)
                        if (data != null) {
                            if (page == 1) {
                                balanceAdapter.clear()
                                rvAttention.removeAllViews()
                            }
                            if (data.isNotEmpty()) {
                                page++
                                openLoadMore()
                            }
                            balanceAdapter.addAll(data)
                        } else {
                            if (isFirst) {
                                tvHolder.text = "暂无余额记录~ ~"
                                setVisible(tvHolder)
                            }

                        }
                        hidePageLoadingDialog()
                        closeRefresh()
                        openRefresh()
                    }
                    onFailed {
                        if (balanceAdapter.getAllData().isNullOrEmpty() && isFirst) {
                            tvHolder.text = "暂无兑换记录~ ~"
                            setVisible(tvHolder)
                        }
                        GlobalDialog.showError(requireActivity(), it)
                        hidePageLoadingDialog()
                        closeRefresh()
                        openRefresh()
                    }
                }
            2 -> {
                MineApi.betRecord(page) {
                    onSuccess {
                        val data = parseResult(it)
                        if (data != null) {
                            if (page == 1) {
                                betRecordAdapter.clear()
                                rvAttention.removeAllViews()
                            }
                            if (data.isNotEmpty()) {
                                page++
                                openLoadMore()
                            }
                            betRecordAdapter.addAll(data)
                        } else {
                            if (isFirst) {
                                tvHolder.text = "暂无投注记录~ ~"
                                setVisible(tvHolder)
                            }

                        }
                        hidePageLoadingDialog()
                        closeRefresh()
                        openRefresh()
                    }
                    onFailed {
                        if (betRecordAdapter.getAllData().isNullOrEmpty() && isFirst) {
                            tvHolder.text = "暂无投注记录~ ~"
                            setVisible(tvHolder)
                        }
                        GlobalDialog.showError(requireActivity(), it)
                        hidePageLoadingDialog()
                        closeRefresh()
                        openRefresh()
                    }

                }

            }
            3 ->
                MineApi.getReward(page) {
                    onSuccess {
                        val data = parseResult(it)
                        if (data != null) {
                            if (page == 1) {
                                exchangeAdapter.clear()
                                rvAttention.removeAllViews()
                            }
                            if (data.isNotEmpty()) {
                                page++
                                openLoadMore()
                            }
                            exchangeAdapter.addAll(data)
                        } else {
                            if (isFirst) {
                                tvHolder.text = "暂无打赏记录~ ~"
                                setVisible(tvHolder)
                            }
                        }
                        hidePageLoadingDialog()
                        closeRefresh()
                        openRefresh()
                    }
                    onFailed {
                        if (exchangeAdapter.getAllData().isNullOrEmpty() && isFirst) {
                            tvHolder.text = "暂无打赏记录"
                            setVisible(tvHolder)
                        }
                        GlobalDialog.showError(requireActivity(), it)
                        hidePageLoadingDialog()
                        closeRefresh()
                        openRefresh()
                    }
                }
            else ->
                MineApi.getChange(page) {
                    onSuccess {
                        val data = parseResult(it)
                        if (data != null) {
                            if (page == 1) {
                                rewardAdapter.clear()
                                rvAttention.removeAllViews()
                            }
                            if (data.isNotEmpty()) {
                                page++
                                openLoadMore()
                            }
                            rewardAdapter.addAll(data)
                        } else {
                            if (isFirst) {
                                tvHolder.text = "暂无兑换记录~ ~"
                                setVisible(tvHolder)
                            }
                        }
                        hidePageLoadingDialog()
                        closeRefresh()
                        openRefresh()
                    }
                    onFailed {
                        if (rewardAdapter.getAllData().isNullOrEmpty()) {
                            if (isFirst) {
                                tvHolder.text = "暂无兑换记录~ ~"
                                setVisible(tvHolder)
                            }
                        }

                        GlobalDialog.showError(requireActivity(), it)
                        hidePageLoadingDialog()
                        closeRefresh()
                        openRefresh()
                    }
                }
        }
    }


    private fun openRefresh() {
        attentionSmartRefreshLayout.setEnableRefresh(true)
        attentionSmartRefreshLayout.setOnRefreshListener {
            page = 1
            initDataAdapter(false)
        }
    }

    private fun openLoadMore() {
        attentionSmartRefreshLayout.setEnableLoadMore(true)
        attentionSmartRefreshLayout.setOnLoadMoreListener {
            initDataAdapter(false)
        }
    }


    private fun closeRefresh() {
        attentionSmartRefreshLayout.finishRefresh()
        attentionSmartRefreshLayout.finishLoadMore()
    }

    private fun parseResult(result: BaseApiBean): ArrayList<MineBillResponse>? {
        if (!result.data!!.isJsonNull && result.data.toString().length > 5) {
            val res = ArrayList<MineBillResponse>()
            //获取到map，取值
            for (entry in result.data.asJsonObject.entrySet()) {
                val dataList = JsonUtils.fromJson(entry.value, Array<MineBillBean>::class.java)
                res.add(MineBillResponse(entry.key, dataList))

            }
            return res
        }
        return null
    }


    /**
     * 余额记录 adapter
     */
    inner class BalanceAdapter : BaseRecyclerAdapter<MineBillResponse>(getPageActivity()) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineBillResponse> {
            return AnchorHolder(parent)
        }

        inner class AnchorHolder(parent: ViewGroup) : BaseViewHolder<MineBillResponse>(getContext(), parent, R.layout.holder_bill_title) {
            private lateinit var dataChild: ChildAdapter

            override fun onBindView(context: Context?) {
                context?.apply {
                    dataChild = ChildAdapter(0)
                    val recycle = findView<RecyclerView>(R.id.rvBill)
                    recycle.adapter = dataChild
                    recycle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
            }

            override fun onBindData(data: MineBillResponse) {
                setText(R.id.tvTime, data.title)
                dataChild.addAll(data.bean)
            }
        }
    }

    /**
     * 投注记录 adapter
     */
    inner class BetRecordAdapter : BaseRecyclerAdapter<MineBillResponse>(getPageActivity()) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineBillResponse> {
            return RecordHolder(parent)
        }

        inner class RecordHolder(parent: ViewGroup) : BaseViewHolder<MineBillResponse>(getContext(), parent, R.layout.holder_bill_title) {
            private lateinit var dataChild: ChildAdapter
            override fun onBindView(context: Context?) {
                context?.apply {
                    dataChild = ChildAdapter(3)
                    val recycle = findView<RecyclerView>(R.id.rvBill)
                    recycle.adapter = dataChild
                    recycle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
            }

            override fun onBindData(data: MineBillResponse) {
                setText(R.id.tvTime, data.title)
                dataChild.addAll(data.bean)
            }

        }
    }

    /**
     * 兑换记录 adapter
     */
    inner class ExchangeAdapter : BaseRecyclerAdapter<MineBillResponse>(getPageActivity()) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineBillResponse> {
            return UserHolder(parent)
        }

        inner class UserHolder(parent: ViewGroup) : BaseViewHolder<MineBillResponse>(getContext(), parent, R.layout.holder_bill_title) {
            private lateinit var dataChild: ChildAdapter
            override fun onBindView(context: Context?) {
                context?.apply {
                    dataChild = ChildAdapter(1)
                    val recycle = findView<RecyclerView>(R.id.rvBill)
                    recycle.adapter = dataChild
                    recycle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
            }

            override fun onBindData(data: MineBillResponse) {
                setText(R.id.tvTime, data.title)
                dataChild.addAll(data.bean)
            }

        }
    }


    /**
     * 打赏记录 adapter
     */
    inner class RewardAdapter : BaseRecyclerAdapter<MineBillResponse>(getPageActivity()) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineBillResponse> {
            return ExpertHolder(parent)
        }

        inner class ExpertHolder(parent: ViewGroup) : BaseViewHolder<MineBillResponse>(getContext(), parent, R.layout.holder_bill_title) {
            private lateinit var dataChild: ChildAdapter
            override fun onBindView(context: Context?) {
                context?.apply {
                    dataChild = ChildAdapter(2)
                    val recycle = findView<RecyclerView>(R.id.rvBill)
                    recycle.adapter = dataChild
                    recycle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
            }

            override fun onBindData(data: MineBillResponse) {
                setText(R.id.tvTime, data.title)
                dataChild.addAll(data.bean)
            }

        }
    }


    inner class ChildAdapter(var type: Int) : BaseRecyclerAdapter<MineBillBean>(getPageActivity()) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineBillBean> {
            return ExpertHolder(parent)
        }

        inner class ExpertHolder(parent: ViewGroup) : BaseViewHolder<MineBillBean>(getContext(), parent, R.layout.hilder_mine_bill) {
            override fun onBindData(data: MineBillBean) {
                when (type) {
                    0 -> {
                        setText(R.id.tvTime, data.time)
                        when (data.type) {
                            "0" -> {
                                setText(R.id.tvName, "存款")
                                setText(R.id.tvEnd, "+ " + data.amount + " 元")
                                Phoenix.with(findView<SimpleDraweeView>(R.id.imgPhoto)).load(R.mipmap.ic_save)
                            }
                            "1" -> {
                                setText(R.id.tvName, "提现")
                                setText(R.id.tvEnd, "- " + data.amount + " 元")
                                Phoenix.with(findView<SimpleDraweeView>(R.id.imgPhoto)).load(R.mipmap.ic_tixian)
                            }
                            "2" -> {
                                setText(R.id.tvName, "兑换")
                                setText(R.id.tvEnd, "- " + data.amount + " 元")
                                Phoenix.with(findView<SimpleDraweeView>(R.id.imgPhoto)).load(R.mipmap.ic_change)
                            }
                            "3" -> {
                                setText(R.id.tvName, "抢红包")
                                setText(R.id.tvEnd, "+ " + data.amount + " 元")
                                Phoenix.with(findView<SimpleDraweeView>(R.id.imgPhoto)).load(R.mipmap.ic_qianghoongbao)
                            }
                            "4" -> {
                                setText(R.id.tvName, "发红包")
                                setText(R.id.tvEnd, "- " + data.amount + " 元")
                                Phoenix.with(findView<SimpleDraweeView>(R.id.imgPhoto)).load(R.mipmap.ic_fahongbao)
                            }
                        }
                    }
                    1 -> {
                        setText(R.id.tvTime, data.time)
                        setText(R.id.tvName, data.nickname)
                        setText(R.id.tvGiftName, data.giftname + " x" + data.gift_num)
                        setText(R.id.tvGiftPrise, "- " + data.amount + " 钻石")
                        ImageManager.loadImg(data.avatar, findView(R.id.imgPhoto))
                    }
                    2 -> {
                        setText(R.id.tvTime, data.time)
                        setText(R.id.tvName, "兑换")
                        setText(R.id.tvEnd, "+ " + data.get_money + " 钻石")
                        Phoenix.with(findView<SimpleDraweeView>(R.id.imgPhoto)).load(R.mipmap.ic_change)
                    }
                    3 -> {
                        setText(R.id.tvTime, data.issue + "   " + data.time)
                        setText(R.id.tvName, data.lottery_name)
                        setText(R.id.tvGiftName, data.method_name + "  " + data.code + "  " + data.type)
                        setText(R.id.tvGiftPrise, data.amount + " 钻石")
                        if (data.type == "中奖") {
                            Phoenix.with(findView<SimpleDraweeView>(R.id.imgPhoto)).load(R.mipmap.icc_re_bet_get)
                        } else Phoenix.with(findView<SimpleDraweeView>(R.id.imgPhoto)).load(R.mipmap.ic_re_bet)
                    }
                }
            }
        }
    }


    companion object {
        fun newInstance(type: Int?): MineBillFragmentChild {
            val fragment = MineBillFragmentChild()
            val bundle = Bundle()
            bundle.putInt(IntentConstant.LIVE_ROOM_LOTTERY_TYPE, type ?: 0)
            fragment.arguments = bundle
            return fragment
        }
    }
}