package com.fenghuang.caipiaobao.ui.moments.childern

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Animatable
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.ControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.fresco.helper.photoview.PhotoX
import com.facebook.fresco.helper.photoview.entity.PhotoInfo
import com.facebook.imagepipeline.image.ImageInfo
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.base.recycler.decorate.GridItemSpaceDecoration
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.moments.data.MomentsHotDiscussResponse
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.LaunchUtils
import java.util.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe 热门讨论适配器
 *
 */

class MomentsHotDiscussAdapter(context: Context) : BaseRecyclerAdapter<MomentsHotDiscussResponse>(context) {


    var isShowFooter = false


    internal enum class ITEM_TYPE {
        PROMOTE,
        FOOTER,
        NORMAL
    }


    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MomentsHotDiscussResponse> {


        return when {
            viewType === ITEM_TYPE.FOOTER.ordinal -> FooterHolder(parent)
            else -> MomentsHotDiscussHolder(parent)
        }
    }

    inner class MomentsHotDiscussHolder(parent: ViewGroup) : BaseViewHolder<MomentsHotDiscussResponse>(getContext(), parent, R.layout.holder_moments_hot_discuss) {

        private lateinit var mAdapter: MomentsHotDiscussImgAdapter
        private lateinit var recyclerViewImg: RecyclerView
        private lateinit var layoutManager: GridLayoutManager

        override fun onBindView(context: Context?) {
            context?.apply {
                val recyclerView = findView<RecyclerView>(R.id.rvMomentsDiscussHolderListImg)
                mAdapter = MomentsHotDiscussImgAdapter(this)
                recyclerViewImg = recyclerView
                recyclerViewImg.adapter = mAdapter
                layoutManager = GridLayoutManager(context, 3)
                recyclerViewImg.layoutManager = layoutManager
                recyclerViewImg.addItemDecoration(GridItemSpaceDecoration(3, itemSpace = ViewUtils.dp2px(6), startAndEndSpace = ViewUtils.dp2px(6)))
            }
        }

        override fun onBindData(data: MomentsHotDiscussResponse) {
            ImageManager.loadImg(data.avatar, findView(R.id.imgMomentsDiscussHolderPhoto))
            setText(R.id.tvMomentsDiscussHolderName, data.nickname)
            setText(R.id.tvMomentsDiscussHolderTime, TimeUtils.longToDateString(data.created).toString())
            setText(R.id.tvMomentsDiscussHolderTitle, data.title)
            setText(R.id.tvHotDiscussHolderDianZan, data.like)
            setText(R.id.tvLiveHotDiscussHolderReply, data.comment_nums)
            if (data.is_like == "1") {
                findView<ImageView>(R.id.imgHotDiscussHolderDianZan).background = getDrawable(R.mipmap.ic_yidianzan)
            } else findView<ImageView>(R.id.imgHotDiscussHolderDianZan).background = getDrawable(R.mipmap.ic_dianzan)

            if (data.is_promote == "1") {
                setGone(findView<LinearLayout>(R.id.linDianZan))
                setGone(findView<LinearLayout>(R.id.linReply))
            }


            if (!data.images.isNullOrEmpty()) {
                if (data.is_promote == "1") {
                    layoutManager = GridLayoutManager(getContext(), 1)
                    recyclerViewImg.layoutManager = GridLayoutManager(getContext(), 1)

                    mAdapter.isPromote = true
                    mAdapter.urlWeb = data.url
                    mAdapter.setData(data.images)
                } else mAdapter.setData(data.images)
            }
            mAdapter.setLayoutManage(layoutManager)
            setOnClick(findView<ImageView>(R.id.linDianZan))
            setOnClick(findView<ImageView>(R.id.linReply))
            setOnClick(findView<ImageView>(R.id.imgMomentsDiscussHolderPhoto))
            if (data.gender == 1) {
                findView<ImageView>(R.id.imgAnchorSex).setBackgroundResource(R.mipmap.ic_live_anchor_boy)
            } else if (data.gender == 2) {
                findView<ImageView>(R.id.imgAnchorSex).setBackgroundResource(R.mipmap.ic_live_anchor_girl)
            }
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
                        if (getData()?.is_like == "1") {
                            getData()?.is_like = "0"
                            val zan = (getData()?.like?.toInt()?.minus(1)).toString()
                            getData()?.like = zan
                            findView<TextView>(R.id.tvHotDiscussHolderDianZan).text = zan
                            findView<ImageView>(R.id.imgHotDiscussHolderDianZan).background = getDrawable(R.mipmap.ic_dianzan)
                        } else {
                            getData()?.is_like = "1"
                            val zan = (getData()?.like?.toInt()?.plus(1)).toString()
                            getData()?.like = zan
                            findView<TextView>(R.id.tvHotDiscussHolderDianZan).text = zan
                            findView<ImageView>(R.id.imgHotDiscussHolderDianZan).background = getDrawable(R.mipmap.ic_yidianzan)
                        }
                        MomentsHotDiscussPresenter().setZans(getData()?.id!!)
                    } else ToastUtils.show("请勿重复点击")
                }
                //评论跳转
                R.id.linReply -> {
//                    //未登录
//                    if (!UserInfoSp.getIsLogin()) {
//                        GlobalDialog.notLogged(getContext() as Activity)
//                        return
//                    }
                    LaunchUtils.startFragment(getContext(), CommentOnFragment.newInstance(getData()!!))
                }

                R.id.imgMomentsDiscussHolderPhoto -> {
                    LaunchUtils.startPersonalPage(getContext(), getData()?.user_id!!, 1)
                }
            }
        }
    }

    //图片列表适配器
    inner class MomentsHotDiscussImgAdapter(context: Context, var isPromote: Boolean = false, var urlWeb: String = "") : BaseRecyclerAdapter<String>(context) {

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
                val controller = Fresco.newDraweeControllerBuilder().setOldController(findView<SimpleDraweeView>(R.id.ivQuizImage).controller)
                        .setControllerListener(object : ControllerListener<ImageInfo> {
                            override fun onFailure(id: String?, throwable: Throwable?) {
                            }

                            override fun onRelease(id: String?) {
                            }

                            override fun onSubmit(id: String?, callerContext: Any?) {
                            }

                            override fun onIntermediateImageSet(id: String?, imageInfo: ImageInfo?) {
                            }

                            override fun onIntermediateImageFailed(id: String?, throwable: Throwable?) {
                            }

                            override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                                if (!isPromote) ImageManager.loadImg(data, findView(R.id.ivQuizImage))
                                else adjustSdv(findView(R.id.ivQuizImage), imageInfo?.width!!, imageInfo.height)

                            }
                        })
                        .setUri(Uri.parse(data))
                        .build()

                findView<SimpleDraweeView>(R.id.ivQuizImage).controller = controller
                dataList = arrayListOf()
                for (image in getAllData()) {
                    val photoInfo = PhotoInfo()
                    photoInfo.originalUrl = image
                    photoInfo.thumbnailUrl = image
                    dataList?.add(photoInfo)
                }

                findView<SimpleDraweeView>(R.id.ivQuizImage).setOnClickListener {
                    if (!isPromote) {
                        PhotoX.with(getContext())
                                .setLayoutManager(layoutManager)
                                .setPhotoList(dataList)
                                .setCurrentPosition(getDataPosition())
                                .enabledAnimation(false)
                                .enabledDragClose(false)
                                .start()
                    } else LaunchUtils.startSystemWeb(getContext()!!, urlWeb)

                }
            }

        }
    }

    inner class FooterHolder(parent: ViewGroup) : BaseViewHolder<MomentsHotDiscussResponse>(getContext(), parent, R.layout.recycle_foot) {
        override fun onBindData(data: MomentsHotDiscussResponse) {
        }
    }

    override fun getItemCount(): Int {
        return getAllData().size
    }


    override fun getItemViewType(position: Int): Int {
        return when {
            (position == itemCount - 1 && isShowFooter) -> //最后一个,应该加载Footer
                ITEM_TYPE.FOOTER.ordinal
            getAllData()[position].is_promote == "1" -> {
                ITEM_TYPE.PROMOTE.ordinal
            }
            else -> ITEM_TYPE.NORMAL.ordinal
        }
    }


    private fun adjustSdv(image: SimpleDraweeView, width: Int, height: Int) {
        val params = image.layoutParams as ViewGroup.LayoutParams
        params.height = height
        image.layoutParams = params
    }
}