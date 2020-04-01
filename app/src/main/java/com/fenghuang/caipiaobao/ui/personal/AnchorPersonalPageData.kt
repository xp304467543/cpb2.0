package com.fenghuang.caipiaobao.ui.personal

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.fragment.BaseContentFragment
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.round.RoundTextView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAnchorLiveRecordBean
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAnchorTagListBean
import com.fenghuang.caipiaobao.ui.personal.data.AnchorPageInfoBean
import com.fenghuang.caipiaobao.ui.personal.data.UserPageGift
import kotlinx.android.synthetic.main.child_fragment_anchor_data.*
import kotlin.random.Random

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-01
 * @ Describe 主播资料
 *
 */

class AnchorPersonalPageData(var data : AnchorPageInfoBean) : BaseContentFragment(){

    private lateinit var mHomeAnchorTagAdapter: HomeAnchorTagAdapter
    private lateinit var mHomeAnchorLiveRecordAdapter: HomeAnchorLiveRecordAdapter


    override fun getContentResID() = R.layout.child_fragment_anchor_data


    override fun initContentView() {
        initAnchorTagAdapter()

        initAnchorLiveRecordAdapter()
    }

    override fun initData() {
        if (data.lottery.isNotEmpty()) {
            val sb = StringBuffer()
            data.lottery.forEach {
                sb.append(it.name + "   ")
            }
            setText(R.id.tvAnchorGame, sb.toString())
        }
        if (data.tagList.isNotEmpty()) mHomeAnchorTagAdapter.addAll(data.tagList)
        if (data.giftList.isNotEmpty())
            initAnchorGiftAdapter(data.giftList)
        else {
            setVisible(tvNotReceiveGift)
        }
        setText(R.id.tvAnchorDate, data.duration + "分钟")
        setText(R.id.tvAnchorOpenDate, TimeUtils.longToDateString(data.liveStartTime) + "-" + TimeUtils.longToDateString(data.liveEndTime))
        setText(R.id.anchorGiftNumber, "共 " + data.giftNum + " 件")
        if (data.live_record.isNotEmpty())
            mHomeAnchorLiveRecordAdapter.addAll(data.live_record)
        else {
            setGone(liveRecordingRecycler)
            setVisible(tvNotLiveReceive)
        }
    }


    private fun initAnchorLiveRecordAdapter() {
        mHomeAnchorLiveRecordAdapter = HomeAnchorLiveRecordAdapter(getPageActivity())
        liveRecordingRecycler.adapter = mHomeAnchorLiveRecordAdapter
        val linearLayoutManager = LinearLayoutManager(getPageActivity())
        liveRecordingRecycler.layoutManager = linearLayoutManager
    }

    private fun initAnchorGiftAdapter(giftList: List<UserPageGift>) {
        val color = arrayOf("#FFEFED", "#FFF4E3", "#E9F8FF", "#E9F8FF", "#FFF4E3", "#FFEFED")
        if (!giftList.isNullOrEmpty()) {
            //往容器内添加TextView数据
            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(15, 20, 15, 5)
            if (flowLayoutAnchor != null) {
                flowLayoutAnchor.removeAllViews()
            }
            for (i in giftList) {
                val tv = RoundTextView(getPageActivity())
                tv.setPadding(28, 10, 28, 10)
                val builder = SpannableStringBuilder(i.gift_name + "  ")
                val length = builder.length
                builder.append("x" + i.num)
                builder.setSpan(ForegroundColorSpan(ViewUtils.getColor(R.color.color_FF513E)), length, builder.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                tv.text = builder
                tv.maxEms = 10
                tv.setSingleLine()
                tv.textSize = 12f
                tv.setTextColor(ViewUtils.getColor(R.color.color_333333))
                tv.delegate.cornerRadius = 15
                tv.layoutParams = layoutParams
                tv.delegate.backgroundColor = Color.parseColor(color[Random.nextInt(5)])
                flowLayoutAnchor.addView(tv, layoutParams)
            }
        }
    }

    private fun initAnchorTagAdapter() {
        mHomeAnchorTagAdapter = HomeAnchorTagAdapter(getPageActivity())
        anchorTagRecyclerView.adapter = mHomeAnchorTagAdapter
        val linearLayoutManager = LinearLayoutManager(getPageActivity())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        anchorTagRecyclerView.layoutManager = linearLayoutManager
    }


    /**
     *  desc   : 主播信息直播记录适配器
     */
   inner class HomeAnchorLiveRecordAdapter(context: Context) : BaseRecyclerAdapter<HomeLiveAnchorLiveRecordBean>(context) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeLiveAnchorLiveRecordBean> {
            return LiveRoomGiftHolder(parent)
        }
        inner class LiveRoomGiftHolder(parent: ViewGroup) : BaseViewHolder<HomeLiveAnchorLiveRecordBean>(getContext(), parent, R.layout.holder_anchor_live_record) {
            override fun onBindData(data: HomeLiveAnchorLiveRecordBean) {
                setText(R.id.tvAnchorRecordGameName, data.name + ":")
                setText(R.id.tvAnchorRecordStatus, data.tip)
                setText(R.id.tvAnchorRecordTime, data.startTimeTxt)
//            setText(R.id.tvAnchorRecordDate, TimeUtils.getYearMonthDay(data.startTime))
            }
        }

    }



    /**
     *  desc   : 主播信息标签适配器
     */
    class HomeAnchorTagAdapter(context: Context) : BaseRecyclerAdapter<HomeLiveAnchorTagListBean>(context) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeLiveAnchorTagListBean> {
            return LiveRoomTopHolder(parent)
        }

        inner class LiveRoomTopHolder(parent: ViewGroup) : BaseViewHolder<HomeLiveAnchorTagListBean>(getContext(), parent, R.layout.holder_anchor_tag) {

            override fun onBindData(data: HomeLiveAnchorTagListBean) {
                setText(R.id.tvAnchorTag, data.title)
                ImageManager.loadImg(data.icon, findView(R.id.ivAnchorTag))
            }
        }

    }
}