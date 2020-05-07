package com.fenghuang.caipiaobao.ui.home.live.children

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAdvance
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAdvanceList
import com.fenghuang.caipiaobao.ui.home.data.UpDateAttention
import com.fenghuang.caipiaobao.ui.home.live.room.LiveRoomAdvanceAdapter
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_advance.*
import kotlinx.android.synthetic.main.fragment_child_live_advance.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-09
 * @ Describe
 *
 */

class LiveAdvanceFragment : BaseMvpFragment<LiveAdvanceFragmentPresenter>() {

    var contentAid = ""

    private var headTitleAdapter: HeadTitleAdapter? = null

    private var contentAdapter: LiveRoomAdvanceAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LiveAdvanceFragmentPresenter()

    override fun getContentResID() = R.layout.fragment_advance

    override fun isOverridePage() = false

    override fun getPageTitle() = "直播预告"

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        smartContent.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartContent.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        smartContent.setEnableRefresh(false)//是否启用下拉刷新功能
        smartContent.setEnableLoadMore(false)//是否启用上拉加载功能
        headTitleAdapter = HeadTitleAdapter()
        recyclerViewTitle.adapter = headTitleAdapter
        recyclerViewTitle.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onSupportVisible() {
        if (isActive()) {
            mPresenter.getContent(contentAid)
        }

    }


    fun initTitle(data: MutableList<HomeLiveAdvance>) {
        data.add(0, HomeLiveAdvance("", "全部"))
        headTitleAdapter?.addAll(data)

    }

    fun initAdvanceRecycle(data: ArrayList<HomeLiveAdvanceList>, type: String) {
        contentAdapter = if (type == "") {
            LiveRoomAdvanceAdapter(getPageActivity(), true, isJumpLive = true)
        } else {
            LiveRoomAdvanceAdapter(getPageActivity(), isJumpLive = true)
        }
        if (!data.isNullOrEmpty()) {
            recyclerViewContent.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
            recyclerViewContent.adapter = contentAdapter
            contentAdapter?.addAll(data)
        } else {
            setVisible(emptyHolder)
            contentAdapter?.clear()
        }
    }

    fun setEmpty() {
        contentAdapter?.clear()
        setVisible(emptyHolder)
    }


    inner class HeadTitleAdapter : BaseRecyclerAdapter<HomeLiveAdvance>(getPageActivity()) {
        var clickPosition: Int = 0

        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeLiveAdvance> {
            return HeadHolder(parent)
        }

        inner class HeadHolder(parent: ViewGroup) : BaseViewHolder<HomeLiveAdvance>(getContext(), parent, R.layout.holder_lottery_type) {
            override fun onBindData(data: HomeLiveAdvance) {
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

            override fun onItemClick(data: HomeLiveAdvance) {
                changeBackground(getDataPosition())
                mPresenter.getContent(data.room_id)
                contentAid = data.room_id
            }
        }

        fun changeBackground(position: Int) {
            clickPosition = position
            notifyDataSetChanged()
        }
    }


    //更新关注
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun UpDateAttention(eventBean: UpDateAttention) {
        mPresenter.getContent(contentAid)
    }

}