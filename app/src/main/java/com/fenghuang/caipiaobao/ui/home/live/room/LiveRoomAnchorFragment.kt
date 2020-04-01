package com.fenghuang.caipiaobao.ui.home.live.room

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAnchorDynamicBean
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAnchorInfoBean
import com.fenghuang.caipiaobao.utils.LaunchUtils
import kotlinx.android.synthetic.main.fragment_child_live_anchor.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-22
 * @ Describe 主播动态
 *
 */

class LiveRoomAnchorFragment(private val anchorId: String,val liveState:String) : BaseMvpFragment<LiveRoomAnchorPresenter>() {

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LiveRoomAnchorPresenter()

    override fun getLayoutResID() = R.layout.fragment_child_live_anchor

    override fun initContentView() {
        smartRefreshLiveRoomAnchor.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLiveRoomAnchor.setEnableLoadMore(false)//是否启用上拉加载功能
        smartRefreshLiveRoomAnchor.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLiveRoomAnchor.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
    }

    override fun initData() {
        mPresenter.getAnchorInfo(anchorId)
    }


    @SuppressLint("SetTextI18n")
    fun initAnchorInfo(data: HomeLiveAnchorInfoBean) {
        ImageManager.loadImg(data.avatar, imgLiveRoomAnchorPhoto)
        tvLiveRoomAnchorName.text = data.nickname
        tvLiveRoomAnchorInfo.text = "房间号 " + anchorId + " / " + data.fans + "粉丝"
        if (data.sex == "0") {
            imgLiveRoomAnchorSex.setBackgroundResource(R.mipmap.ic_live_anchor_girl)
        } else imgLiveRoomAnchorSex.setBackgroundResource(R.mipmap.ic_live_anchor_boy)
        imgLiveRoomAnchorAge.text = data.age
        imgLiveRoomAnchorLevel.text = data.level
        imgGoInfo.setOnClickListener {
            LaunchUtils.startPersonalPage(getPageActivity(),anchorId,2)
        }
    }

    fun initAnchorNews(data: List<HomeLiveAnchorDynamicBean>) {
        if (data.isNullOrEmpty()){
            setVisible(tvHolder)
        }else{
            val adapter = LiveRoomAnchorAdapter(context!!,liveState)
            rvLiveRoomAnchor.adapter = adapter
            rvLiveRoomAnchor.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
            adapter.addAll(data)
        }
    }

}