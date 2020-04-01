package com.fenghuang.caipiaobao.ui.home.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.round.RoundTextView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeLivePreResponse
import com.fenghuang.caipiaobao.widget.WaveView


/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/27- 13:41
 * @ Describe 直播预告
 *
 */

class HomeLivePreviewAdapter(context: Context) : BaseRecyclerAdapter<HomeLivePreResponse>(context) {


    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeLivePreResponse> {
        return HomeLivePreHolder(parent)
    }

    inner class HomeLivePreHolder(parent: ViewGroup) : BaseViewHolder<HomeLivePreResponse>(getContext(), parent, R.layout.holder_home_live_preview) {
        override fun onBindData(data: HomeLivePreResponse) {

            when (getDataPosition()) {
                0 -> {
                    if (data.livestatus == "1") findView<ImageView>(R.id.imgChampion).background = getDrawable(R.mipmap.ic_no_1)
                }
                1 -> {
                    if (data.livestatus == "1") findView<ImageView>(R.id.imgChampion).background = getDrawable(R.mipmap.ic_no_2)
                }
                2 -> {
                    if (data.livestatus == "1") findView<ImageView>(R.id.imgChampion).background = getDrawable(R.mipmap.ic_no_3)
                }
                else -> setGone(findView<ImageView>(R.id.imgChampion))
            }

            setText(R.id.tvLiveNoticeName, data.nickname)
            setText(R.id.tvLiveNoticeGameName, data.name)
            setText(R.id.tvLiveNoticeDate, TimeUtils.longToDateStringTime(data.starttime.toLong()) + "~" + TimeUtils.longToDateStringTime(data.endtime.toLong()))

            ImageManager.loadImg(data.avatar, findView(R.id.ivLiveNoticeLogo))

            if (data.isFollow) {
                findView<RoundTextView>(R.id.tvLiveNoticeAttention).text = "已关注"
                findView<RoundTextView>(R.id.tvLiveNoticeAttention).delegate.backgroundColor = ViewUtils.getColor(R.color.color_f5f5f5)
                findView<RoundTextView>(R.id.tvLiveNoticeAttention).setTextColor(ViewUtils.getColor(R.color.color_DDDDDD))
            } else {
                findView<RoundTextView>(R.id.tvLiveNoticeAttention).text = "关注"
                findView<RoundTextView>(R.id.tvLiveNoticeAttention).delegate.backgroundColor = ViewUtils.getColor(R.color.color_FF513E)
                findView<RoundTextView>(R.id.tvLiveNoticeAttention).setTextColor(ViewUtils.getColor(R.color.white))
            }
            if (data.livestatus == "1") {
                findView<WaveView>(R.id.circleWave).setInitialRadius(50f)
                findView<WaveView>(R.id.circleWave).start()
            }

            findView<ImageView>(R.id.ivLiveNoticeLogo).setOnClickListener {
                listenerAvatar?.invoke(getDataPosition())
            }
            findView<RoundTextView>(R.id.tvLiveNoticeAttention).setOnClickListener {
                listenerAttention?.invoke(getDataPosition())
            }

        }
    }

    //=======================以下为item中的控件点击事件处理===================================

    var listenerAvatar: ((position: Int) -> Unit?)? = null
    var listenerAttention: ((position: Int) -> Unit?)? = null
    fun setAvatarListener(listener: (position: Int) -> Unit) {
        this.listenerAvatar = listener
    }

    fun setAttentionListener(listener: (position: Int) -> Unit) {
        this.listenerAttention = listener
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}