package com.fenghuang.caipiaobao.ui.home.live.children

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.adapter.HomeHotLiveAdapter
import com.fenghuang.caipiaobao.ui.home.data.HomeHotLiveResponse
import com.fenghuang.caipiaobao.ui.home.data.HomeJumpToMine
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAnchor
import com.fenghuang.caipiaobao.ui.home.data.JumpToBuyLottery
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_advance.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-09
 * @ Describe
 *
 */

class LiveAnchorFragment : BaseMvpFragment<LiveAnchorFragmentPresenter>() {

    var page = 1
    var type = "0"


    private var headTitleAdapter: HeadTitleAdapter? = null
    private var contentAdapter: HomeHotLiveAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LiveAnchorFragmentPresenter()

    override fun getContentResID() = R.layout.fragment_advance

    override fun isOverridePage() = false

    override fun getPageTitle() = "全部主播"

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun isRegisterRxBus() = true


    override fun initContentView() {
        smartContent.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartContent.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）

        headTitleAdapter = HeadTitleAdapter()
        recyclerViewTitle.adapter = headTitleAdapter
        recyclerViewTitle.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        //内容
        contentAdapter = HomeHotLiveAdapter(context!!)
        recyclerViewContent.layoutManager = GridLayoutManager(context!!, 2)
        recyclerViewContent.adapter = contentAdapter
    }

    override fun initData() {
        showPageLoadingDialog()
        mPresenter.getAll(page, "0", true)
    }

    fun initTitle(data: Array<HomeLiveAnchor>) {
        headTitleAdapter?.addAll(data)
        smartContent.setOnRefreshListener {
            page = 1
            mPresenter.getAll(page, type, false)
        }
        smartContent.setOnLoadMoreListener {
            mPresenter.getAll(page, type, false)
        }
    }

    fun initAdvanceRecycle(data: Array<HomeHotLiveResponse>) {
        if (data.isEmpty()) {
           if (page == 1) setVisible(emptyHolder)
            smartContent.setEnableRefresh(false)//是否启用下拉刷新功能
            smartContent.setEnableLoadMore(false)//是否启用上拉加载功能
        } else {
            smartContent.setEnableRefresh(true)//是否启用下拉刷新功能
            smartContent.setEnableLoadMore(true)//是否启用上拉加载功能
            setGone(emptyHolder)
            contentAdapter?.addAll(data)
        }

        if (smartContent != null) {
            smartContent.finishRefresh()
            smartContent.finishLoadMore()
        }
    }

    inner class HeadTitleAdapter : BaseRecyclerAdapter<HomeLiveAnchor>(context!!) {
        var clickPosition: Int = 0

        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeLiveAnchor> {
            return HeadHolder(parent)
        }

        inner class HeadHolder(parent: ViewGroup) : BaseViewHolder<HomeLiveAnchor>(getContext(), parent, R.layout.holder_lottery_type) {
            override fun onBindData(data: HomeLiveAnchor) {
                if (clickPosition == getDataPosition()) {
                    findView<TextView>(R.id.tvLotteryType).setTextColor(getColor(R.color.white))
                    findView<TextView>(R.id.tvLotteryType).setBackgroundResource(R.drawable.button_background)
                    findView<TextView>(R.id.tvLotteryType).typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                } else {
                    findView<TextView>(R.id.tvLotteryType).setTextColor(getColor(R.color.color_999999))
                    findView<TextView>(R.id.tvLotteryType).setBackgroundResource(R.drawable.button_grey_background)
                    findView<TextView>(R.id.tvLotteryType).typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                }
                setText(R.id.tvLotteryType, data.name)
            }

            override fun onItemClick(data: HomeLiveAnchor) {
                changeBackground(getDataPosition())
                page = 1
                type = data.type
                contentAdapter?.clear()
                showPageLoadingDialog()
                mPresenter.getAll(page, data.type, false)
            }
        }

        fun changeBackground(position: Int) {
            clickPosition = position
            notifyDataSetChanged()
        }
    }
    /**
     * 跳转购彩
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun jumpToBuyLottery(eventBean: JumpToBuyLottery) {
        if (isAdded){
            pop()
        }
    }

    /**
     * 跳转mine
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun onClickMine(clickMine: HomeJumpToMine) {
        if (isAdded){
            pop()
        }
    }
}