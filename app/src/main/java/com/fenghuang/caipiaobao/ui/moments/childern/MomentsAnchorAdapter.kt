package com.fenghuang.caipiaobao.ui.moments.childern

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.fresco.helper.photoview.PhotoX
import com.facebook.fresco.helper.photoview.entity.PhotoInfo
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.base.recycler.decorate.GridItemSpaceDecoration
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.moments.data.MomentsAnchorListResponse
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.WaveView
import kotlinx.android.synthetic.main.fragment_presonal_anchor.*
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe 热门讨论适配器
 *
 */

class MomentsAnchorAdapter(context: Context) : BaseRecyclerAdapter<MomentsAnchorListResponse>(context) {

    var isShowFooter = false

    internal enum class ITEM_TYPE {
        PROMOTE,
        FOOTER,
        NORMAL
    }

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MomentsAnchorListResponse> {

        return when {
            viewType === ITEM_TYPE.FOOTER.ordinal -> FooterHolder(parent)
            else -> MomentsAnchorHolder(parent)
        }
    }

    inner class MomentsAnchorHolder(parent: ViewGroup) : BaseViewHolder<MomentsAnchorListResponse>(getContext(), parent, R.layout.holder_moments_hot_discuss) {

        private lateinit var mAdapter: MomentsImgAdapter
        private lateinit var recyclerViewImg: RecyclerView
        private lateinit var layoutManagerImg: GridLayoutManager
        override fun onBindView(context: Context?) {
            context?.apply {
                val recyclerView = findView<RecyclerView>(R.id.rvMomentsDiscussHolderListImg)
                mAdapter = MomentsImgAdapter(context)
                recyclerViewImg = recyclerView
                recyclerViewImg.adapter = mAdapter
                layoutManagerImg = GridLayoutManager(context, 3)
                recyclerViewImg.layoutManager = layoutManagerImg
                recyclerViewImg.addItemDecoration(GridItemSpaceDecoration(3, itemSpace = ViewUtils.dp2px(6), startAndEndSpace = ViewUtils.dp2px(6)))
            }
        }

        override fun onBindData(data: MomentsAnchorListResponse) {
            ImageManager.loadImg(data.avatar, findView(R.id.imgMomentsDiscussHolderPhoto))
            setText(R.id.tvMomentsDiscussHolderName, data.nickname)
            setText(R.id.tvMomentsDiscussHolderTime, TimeUtils.longToDateString(data.create_time).toString())
            setText(R.id.tvMomentsDiscussHolderTitle, data.text)
            setText(R.id.tvHotDiscussHolderDianZan, data.zans)
            setText(R.id.tvLiveHotDiscussHolderReply, data.pls)
            if (data.is_zan) {
                findView<ImageView>(R.id.imgHotDiscussHolderDianZan).background = getDrawable(R.mipmap.ic_yidianzan)
            } else findView<ImageView>(R.id.imgHotDiscussHolderDianZan).background = getDrawable(R.mipmap.ic_dianzan)

            if (!data.media.isNullOrEmpty()) {
                mAdapter.setData(data.media)
                mAdapter.setLayoutManage(layoutManagerImg)
            }else setGone(findView<RecyclerView>(R.id.rvMomentsDiscussHolderListImg))


            if (data.live_status == "1") {
                findView<WaveView>(R.id.circleWave).setInitialRadius(50f)
                findView<WaveView>(R.id.circleWave).start()
            }else{
                setGone(findView<WaveView>(R.id.circleWave))
            }

            setOnClick(findView<ImageView>(R.id.linDianZan))
            setOnClick(findView<ImageView>(R.id.linReply))
            setOnClick(findView<ImageView>(R.id.imgMomentsDiscussHolderPhoto))
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
                            findView<TextView>(R.id.tvHotDiscussHolderDianZan).text = zan
                            findView<ImageView>(R.id.imgHotDiscussHolderDianZan).background = getDrawable(R.mipmap.ic_dianzan)
                        } else {
                            getData()?.is_zan = true
                            val zan = (getData()?.zans?.toInt()?.plus(1)).toString()
                            getData()?.zans = zan
                            findView<TextView>(R.id.tvHotDiscussHolderDianZan).text = zan
                            findView<ImageView>(R.id.imgHotDiscussHolderDianZan).background = getDrawable(R.mipmap.ic_yidianzan)
                        }
                        MomentsAnchorPresenter().clickZan("1", getData()?.dynamic_id.toString())
                    } else ToastUtils.show("请勿重复点击")
                }
                //评论跳转
                R.id.linReply -> {
                    LaunchUtils.jumpAnchor(getContext(), getData()!!)
                }

                R.id.imgMomentsDiscussHolderPhoto -> {
                    when (getData()?.live_status) {
                        "1" -> {
                            LaunchUtils.startLive(getContext(), getData()?.anchor_id!!, getData()?.live_status!!,
                                    "", getData()?.avatar!!, getData()?.nickname!!, 0,"1")
                        }
                        else -> LaunchUtils.startPersonalPage(getContext(), getData()?.anchor_id!!, 2)
                    }

                }
            }

        }

    }

    //图片列表适配器
    inner class MomentsImgAdapter(context: Context) : BaseRecyclerAdapter<String>(context) {
        var dataList: ArrayList<PhotoInfo>? = null
        var layoutManager: GridLayoutManager? = null

        fun setLayoutManage(layoutManager: GridLayoutManager) {
            this.layoutManager = layoutManager
        }

        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<String> {
            return AnchorDynamicImageHolder(parent)
        }

        inner class AnchorDynamicImageHolder(parent: ViewGroup) : BaseViewHolder<String>(getContext(), parent, R.layout.holder_quiz_image_item) {
            override fun onBindData(data: String) {
                ImageManager.loadImg(data, findView(R.id.ivQuizImage))
                dataList = arrayListOf()
                for (image in getAllData()) {
                    val photoInfo = PhotoInfo()
                    photoInfo.originalUrl = image
                    photoInfo.thumbnailUrl = image
                    dataList?.add(photoInfo)
                }
                setOnClick(R.id.ivQuizImage)
            }

            override fun onClick(id: Int) {
                PhotoX.with(getContext())
                        .setLayoutManager(layoutManager)
                        .setPhotoList(dataList)
                        .setCurrentPosition(getDataPosition())
                        .enabledAnimation(false)
                        .enabledDragClose(false)
                        .start()
            }
        }
    }

    inner class FooterHolder(parent: ViewGroup) : BaseViewHolder<MomentsAnchorListResponse>(getContext(), parent, R.layout.recycle_foot) {
        override fun onBindData(data: MomentsAnchorListResponse) {
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            (position == itemCount - 1 && isShowFooter) -> //最后一个,应该加载Footer
                ITEM_TYPE.FOOTER.ordinal
            else -> ITEM_TYPE.NORMAL.ordinal
        }
    }
}