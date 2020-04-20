package com.fenghuang.caipiaobao.ui.home.live.room

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.round.RoundRelativeLayout
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.HomePresenter
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAdvanceList
import com.fenghuang.caipiaobao.ui.home.data.HomeLivePreResponse
import com.fenghuang.caipiaobao.ui.home.data.UpDateAttention
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.WaveView
import com.hwangjr.rxbus.RxBus


/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-25
 * @ Describe
 *
 */

class LiveRoomAdvanceAdapter(context: Context, val isShowTop: Boolean = false, val isJumpLive: Boolean = false) : BaseRecyclerAdapter<HomeLiveAdvanceList>(context) {
    lateinit var dataChild: LiveRoomAdvanceChildAdapter

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeLiveAdvanceList> {
        return LiveRoomRankHolder(parent)
    }


    inner class LiveRoomRankHolder(parent: ViewGroup) : BaseViewHolder<HomeLiveAdvanceList>(getContext(), parent, R.layout.holder_live_advance) {


        override fun onBindView(context: Context?) {
            context?.apply {
                dataChild = LiveRoomAdvanceChildAdapter(getContext()!!)
                val recycle = findView<RecyclerView>(R.id.rvLiveRoomAdvanceChildren)
                recycle.layoutManager = GridLayoutManager(getContext()!!, 2)
                recycle.adapter = dataChild
            }
        }

        override fun onBindData(data: HomeLiveAdvanceList) {
            setText(R.id.tvAdvanceTitle, data.title)
            dataChild.addAll(data.bean)
        }
    }


    inner class LiveRoomAdvanceChildAdapter(context: Context) : BaseRecyclerAdapter<HomeLivePreResponse>(context) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeLivePreResponse> {
            return LiveRoomAdvanceChildHolder(parent)
        }

        inner class LiveRoomAdvanceChildHolder(parent: ViewGroup) : BaseViewHolder<HomeLivePreResponse>(getContext(), parent, R.layout.holder_live_advance_child) {
            override fun onBindData(data: HomeLivePreResponse) {
                val layoutPar = findView<RoundRelativeLayout>(R.id.advanceRootView).layoutParams
                layoutPar.width = ViewUtils.getScreenWidth() / 2 - 50
                findView<RoundRelativeLayout>(R.id.advanceRootView).layoutParams = layoutPar
                ImageManager.loadImg(data.avatar, findView(R.id.imgLiveRoomAdvanceUserPhoto))
                setText(R.id.tvLiveRoomAdvanceUserName, data.nickname)
                setText(R.id.tvLiveRoomAdvanceUserType, data.name)
                setText(R.id.tvLiveRoomAdvanceUserTime, data.starttime + "~" + data.endtime)
                if (data.isFollow) {
                    findView<RoundRelativeLayout>(R.id.rlAdvanceAttention).delegate.backgroundColor = getColor(R.color.color_EEEEEE)
                    setImageResource(findView(R.id.imgAdvanceAttention), R.mipmap.ic_right_dui)
                } else {
                    findView<RoundRelativeLayout>(R.id.rlAdvanceAttention).delegate.backgroundColor = getColor(R.color.color_FF513E)
                    setImageResource(findView(R.id.imgAdvanceAttention), R.mipmap.ic_add)
                }
                if (data.livestatus == "1") {
                    findView<WaveView>(R.id.circleWave).setInitialRadius(40f)
                    findView<WaveView>(R.id.circleWave).start()
                }
                if (isShowTop) {
                    when (getDataPosition()) {
                        0 -> {
                            if (getData()?.livestatus == "1") setImageResource(findView(R.id.imgTop), R.mipmap.ic_no_1)
                        }
                        1 -> {
                            if (getData()?.livestatus == "1") setImageResource(findView(R.id.imgTop), R.mipmap.ic_no_2)
                        }
                        2 -> {
                            if (getData()?.livestatus == "1") setImageResource(findView(R.id.imgTop), R.mipmap.ic_no_3)
                        }
                    }
                } else setGone(findView<ImageView>(R.id.imgTop))
                setOnClick(R.id.rlAdvanceAttention)
                setOnClick(R.id.imgLiveRoomAdvanceUserPhoto)
            }

            override fun onClick(id: Int) {
                if (id == R.id.rlAdvanceAttention) {
                    if (FastClickUtils.isFastClick()) {
                        val presenter = HomePresenter()
                        presenter.attention(getData()?.aid!!, "")
                        presenter.setSuccessClickListener {
                            if (getData()?.isFollow!!) {
                                findView<RoundRelativeLayout>(R.id.rlAdvanceAttention).delegate.backgroundColor = getColor(R.color.color_FF513E)
                                setImageResource(findView(R.id.imgAdvanceAttention), R.mipmap.ic_add)
                                getData()?.isFollow = false
                                RxBus.get().post(UpDateAttention(false,getData()?.aid?:"0"))
                            } else {
                                findView<RoundRelativeLayout>(R.id.rlAdvanceAttention).delegate.backgroundColor = getColor(R.color.color_EEEEEE)
                                setImageResource(findView(R.id.imgAdvanceAttention), R.mipmap.ic_right_dui)
                                getData()?.isFollow = true
                                RxBus.get().post(UpDateAttention(true,getData()?.aid ?:"0"))
                            }
                        }
                        presenter.setFailClickListener {
                            getContext()?.let { it1 -> GlobalDialog.showError(it1 as Activity, it) }
                        }
                    } else ToastUtils.show("请勿重复点击")
                } else if (id == R.id.imgLiveRoomAdvanceUserPhoto) {
                    if (isJumpLive) {
                        if (getData()?.livestatus == "1") {
                            LaunchUtils.startLive(getContext(), getData()?.aid
                                    ?: "0", getData()?.livestatus ?: "0", getData()?.name
                                    ?: "0", getData()?.avatar ?: "0", getData()?.nickname ?: "0", 0)
                        } else LaunchUtils.startPersonalPage(getContext(), getData()?.aid!!, 2)
                    } else {
                        LaunchUtils.startPersonalPage(getContext(), getData()?.aid!!, 2)
                    }

                }
            }
        }
    }

}


