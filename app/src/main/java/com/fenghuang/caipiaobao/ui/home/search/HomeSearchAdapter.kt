package com.fenghuang.caipiaobao.ui.home.search

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.NetWorkUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.IntentConstant
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.SearchResult
import com.fenghuang.caipiaobao.ui.home.live.LiveRoomActivity
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.cardview.LCardView
import com.fenghuang.caipiaobao.widget.gif.GifImageView

/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/26- 18:21
 * @ Describe
 *
 */
class HomeSearchAdapter(context: Context) : BaseRecyclerAdapter<SearchResult>(context) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SearchResult> {
        return HotLiveHolder(parent)
    }

    inner class HotLiveHolder(parent: ViewGroup) : BaseViewHolder<SearchResult>(getContext(), parent, R.layout.holder_home_hot_live) {
        override fun onBindData(data: SearchResult) {
            setText(R.id.tvHotLiveTitle, data.name)
            setText(R.id.tvHotLiveIntro, data.live_intro)
            setText(R.id.tvHotLiveName, data.nickname)
            setText(R.id.tvHotLiveNumber, data.online.toString())
            if (data.tags.isNotEmpty()) setText(R.id.tvHotLiveTag, data.tags[0].title)
            ImageManager.loadImg(data.avatar, findView(R.id.ivHotLiveLogo))
            if (data.tags.isNotEmpty()) ImageManager.loadImg(data.tags[0].icon, findView(R.id.ivHotLiveTag))
            if (data.live_status == "1") {
                val ivHotLiveStatus = findView<GifImageView>(R.id.ivHotLiveStatus)
                ivHotLiveStatus.setGifResource(R.drawable.ic_home_live_gif)
                ivHotLiveStatus.play(-1)
                setVisibility(R.id.ivHotLiveStatus, true)
            } else {
                setVisibility(R.id.ivHotLiveStatus, false)
            }
            findView<LCardView>(R.id.cardViewItem).setOnClickListener {
                if (FastClickUtils.isFastClick()) {
                    startLiveRoom(data)
                }
            }
        }
    }

    /**
     * 跳转直播间
     */
    private fun startLiveRoom(data: SearchResult) {
        if (NetWorkUtils.isNetworkConnected()) {
            val intent = Intent(getContext(), LiveRoomActivity::class.java)
            intent.putExtra(IntentConstant.LIVE_ROOM_ANCHOR_ID, data.anchor_id)
            intent.putExtra(IntentConstant.LIVE_ROOM_ANCHOR_STATUE, data.live_status)
            intent.putExtra(IntentConstant.LIVE_ROOM_NAME, data.name)
            intent.putExtra(IntentConstant.LIVE_ROOM_AVATAR, data.avatar)
            intent.putExtra(IntentConstant.LIVE_ROOM_NICK_NAME, data.nickname)
            intent.putExtra(IntentConstant.LIVE_ROOM_ONLINE, data.online)
            intent.putExtra(IntentConstant.LIVE_ROOM_LOTTERY_ID, data.lottery_id)
            LaunchUtils.startActivity(getContext(), intent)
        } else ToastUtils.showError("网络连接已断开")
    }

}