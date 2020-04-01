package com.fenghuang.caipiaobao.ui.home.live.room

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveRankList
import com.fenghuang.caipiaobao.utils.LaunchUtils
import java.math.BigDecimal
import java.math.RoundingMode

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-28
 * @ Describe
 *
 */

class LiveRoomRankAdapter(context: Context) : BaseRecyclerAdapter<HomeLiveRankList>(context) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeLiveRankList> {
        return LiveRoomRankHolder(parent)
    }

    inner class LiveRoomRankHolder(parent: ViewGroup) : BaseViewHolder<HomeLiveRankList>(getContext(), parent, R.layout.holder_live_rank) {
        override fun onBindData(data: HomeLiveRankList) {

            when (getDataPosition()) {
                0 -> findView<ImageView>(R.id.imgRankLevel).setBackgroundResource(R.mipmap.ic_live_rank_1)
                1 -> findView<ImageView>(R.id.imgRankLevel).setBackgroundResource(R.mipmap.ic_live_rank_2)
                2 -> findView<ImageView>(R.id.imgRankLevel).setBackgroundResource(R.mipmap.ic_live_rank_3)
                else -> setText(R.id.tvRankLevel, (getDataPosition() + 1).toString())
            }
            when (data.vip) {
                "1" -> findView<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v1)
                "2" -> findView<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v2)
                "3" -> findView<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v3)
                "4" -> findView<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v4)
                "5" -> findView<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v5)
                "6" -> findView<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v6)
                "7" -> findView<ImageView>(R.id.imgRankUserVipLevel).setBackgroundResource(R.mipmap.v7)
            }
            ImageManager.loadImg(data.avatar, findView(R.id.imgRankUserPhoto))
            setText(R.id.tvRankUserName, data.nickname)
            val amount = BigDecimal(data.amount)
            if (amount.compareTo(BigDecimal(10000)) == 1) {
                setText(R.id.tvRankRewardNum, BigDecimal(data.amount).divide(BigDecimal(10000), 2, RoundingMode.HALF_DOWN).toString() + "万钻石")
            } else setText(R.id.tvRankRewardNum, data.amount + "钻石")

            setOnClick(R.id.imgRankUserPhoto)
        }

        override fun onClick(id: Int) {
            when (id) {
                R.id.imgRankUserPhoto -> {
                    LaunchUtils.startPersonalPage(getContext(), getData()?.user_id!!,1)
                }
            }
        }
    }

}