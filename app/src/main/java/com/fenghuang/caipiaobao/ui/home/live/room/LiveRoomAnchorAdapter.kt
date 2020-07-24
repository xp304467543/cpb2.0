package com.fenghuang.caipiaobao.ui.home.live.room

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.base.recycler.decorate.GridItemSpaceDecoration
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAnchorDynamicBean
import com.fenghuang.caipiaobao.ui.moments.childern.MomentsAnchorPresenter
import com.fenghuang.caipiaobao.ui.moments.data.MomentsAnchorListResponse
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.LaunchUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-29
 * @ Describe 主播信息动态页适配器
 *
 */

class LiveRoomAnchorAdapter(context: Context,val liveState:String) : BaseRecyclerAdapter<HomeLiveAnchorDynamicBean>(context) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeLiveAnchorDynamicBean> {
        return HomeAnchorDynamicHolder(parent)
    }


    inner class HomeAnchorDynamicHolder(parent: ViewGroup) : BaseViewHolder<HomeLiveAnchorDynamicBean>(getContext(), parent, R.layout.holder_live_room_anchor) {

        private lateinit var mAdapter: AnchorDynamicImageAdapter
        override fun onBindView(context: Context?) {
            context?.apply {
                val recyclerView = findView<RecyclerView>(R.id.rvLiveRoomAnchorHolderListImg)
                mAdapter = AnchorDynamicImageAdapter(context)
                recyclerView.adapter = mAdapter
                recyclerView.layoutManager = GridLayoutManager(context, 3)
                recyclerView.addItemDecoration(GridItemSpaceDecoration(3, itemSpace = ViewUtils.dp2px(6), startAndEndSpace = ViewUtils.dp2px(6)))
            }
        }


        override fun onBindData(data: HomeLiveAnchorDynamicBean) {
            ImageManager.loadImg(data.avatar, findView(R.id.imgLiveRoomAnchorHolderPhoto))
            setText(R.id.tvLiveRoomAnchorHolderName, data.nickname)
            setText(R.id.tvLiveRoomAnchorHolderTime, data.create_time_txt)
            setText(R.id.tvLiveRoomAnchorHolderTitle, data.text)
            setText(R.id.tvLiveRoomAnchorHolderDianZan, data.zans)
            setText(R.id.tvLiveRoomAnchorHolderReply, data.pls)
            if (!data.media.isNullOrEmpty()) mAdapter.setData(data.media)
            if (data.is_zan) {
                findView<ImageView>(R.id.imgLiveRoomAnchorHolderDianZan).background = getDrawable(R.mipmap.ic_yidianzan)
            } else findView<ImageView>(R.id.imgLiveRoomAnchorHolderDianZan).background = getDrawable(R.mipmap.ic_dianzan)

            setOnClick(findView<ImageView>(R.id.linDianZan))
            setOnClick(findView<ImageView>(R.id.linReply))
            setOnClick(findView<ImageView>(R.id.imgLiveRoomAnchorHolderPhoto))
        }

        override fun onClick(id: Int) {
            when (id) {
                //点赞处理
                R.id.linDianZan -> {
                    //未登录
                    if (!UserInfoSp.getIsLogin()) {
                        GlobalDialog.notLogged(getContext() as Activity)
                        return
                    }
                    if (FastClickUtils.isFastClick()) {
                        if (getData()?.is_zan!!) {
                            getData()?.is_zan = false
                            val zan = (getData()?.zans?.toInt()?.minus(1)).toString()
                            getData()?.zans = zan
                            findView<TextView>(R.id.tvLiveRoomAnchorHolderDianZan).text = zan
                            findView<ImageView>(R.id.imgLiveRoomAnchorHolderDianZan).background = getDrawable(R.mipmap.ic_dianzan)
                        } else {
                            getData()?.is_zan = true
                            val zan = (getData()?.zans?.toInt()?.plus(1)).toString()
                            getData()?.zans = zan
                            findView<TextView>(R.id.tvLiveRoomAnchorHolderDianZan).text = zan
                            findView<ImageView>(R.id.imgLiveRoomAnchorHolderDianZan).background = getDrawable(R.mipmap.ic_yidianzan)
                        }
                        MomentsAnchorPresenter().clickZan("1", getData()?.dynamic_id.toString())
                    } else ToastUtils.show("请勿重复点击")
                }
                //评论跳转
                R.id.linReply -> {
                    val ben = MomentsAnchorListResponse(getData()?.anchor_id
                            ?: "0", getData()?.dynamic_id ?: "0"
                            , getData()?.media, getData()?.text ?: "0", getData()?.zans
                            ?: "0"
                            , getData()?.pls ?: "0", getData()?.shares
                            ?: "0", getData()?.avatar ?: "0"
                            ,  liveState, getData()?.create_time
                            ?: 0, getData()?.nickname ?: "0",  getData()?.is_zan ?:false,"",false,getData()?.sex)
                    LaunchUtils.jumpAnchor(getContext(), ben)
                }

                R.id.imgLiveRoomAnchorHolderPhoto -> {
                    if (FastClickUtils.isFastClick()) {
                        LaunchUtils.startPersonalPage(getContext(), getData()?.anchor_id!!, 2)
                    }

                }
            }

        }

    }


    inner class AnchorDynamicImageAdapter(context: Context) : BaseRecyclerAdapter<String>(context) {

        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
            return AnchorDynamicImageHolder(parent)
        }

        inner class AnchorDynamicImageHolder(parent: ViewGroup) : BaseViewHolder<String>(getContext(), parent, R.layout.holder_quiz_image_item) {
            override fun onBindData(data: String) {
                ImageManager.loadImg(data, findView(R.id.ivQuizImage))
                setOnClick(R.id.ivQuizImage)
            }

            override fun onClick(id: Int) {
//                if (id == R.id.ivQuizImage) startFragment(CheckPhotoImgFragment.newInstance((getAllData() as ArrayList<String>), getDataPosition()))
            }
        }
    }
}