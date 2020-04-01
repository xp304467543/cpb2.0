package com.fenghuang.caipiaobao.ui.mine.children

import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.fragment.BaseNavFragment
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MineMessageCenter
import com.fenghuang.caipiaobao.ui.moments.childern.CommentOnFragment
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.fragment_msg_center_info.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-05
 * @ Describe 消息中心
 *
 */

class MineMessageCenterInfoFragment(var type: Int, var data: List<MineMessageCenter>?) : BaseNavFragment() {

    private lateinit var adapter1: Adapter1

    private lateinit var adapter2: Adapter2


    override fun isOverridePage() = false

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.fragment_msg_center_info


    override fun initContentView() {
        msgSmartRefreshLayout.setEnableRefresh(false)//是否启用下拉刷新功能
        msgSmartRefreshLayout.setEnableLoadMore(false)//是否启用上拉加载功能
        msgSmartRefreshLayout.setEnableOverScrollBounce(true)//是否启用越界回弹
        msgSmartRefreshLayout.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）

        when (type) {
            0 -> {
                setPageTitle("互动消息")
                adapter1 = Adapter1()
                rvMsg.adapter = adapter1
                rvMsg.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                if (!data.isNullOrEmpty()) adapter1.addAll(data) else setVisible(tvHolder)
            }
            1 -> {
                setPageTitle("官方消息")
                adapter2 = Adapter2()
                rvMsg.adapter = adapter2
                rvMsg.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                if (!data.isNullOrEmpty()) adapter2.addAll(data) else {
                    tvHolder.text = "当前暂无任何官方消息哦~"
                    setVisible(tvHolder)
                }
            }
            else -> {
                setPageTitle("系统消息")
                adapter2 = Adapter2()
                rvMsg.adapter = adapter2
                rvMsg.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                if (!data.isNullOrEmpty()) adapter2.addAll(data) else {
                    tvHolder.text = "当前暂无任何系统消息哦~"
                    setVisible(tvHolder)
                }
            }
        }
    }

    /**
     * 互动消息 adapter
     */
    inner class Adapter1 : BaseRecyclerAdapter<MineMessageCenter>(getPageActivity()) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineMessageCenter> {
            return AnchorHolder(parent)
        }

        inner class AnchorHolder(parent: ViewGroup) : BaseViewHolder<MineMessageCenter>(getContext(), parent, R.layout.holder_message_center_user) {
            override fun onBindData(data: MineMessageCenter) {
                ImageManager.loadImg(data.media, findView(R.id.imgDes))
                ImageManager.loadImg(data.avatar, findView(R.id.imgPhoto))
                setText(R.id.tvNickName, data.nickname)
                setText(R.id.tvTime, TimeUtils.longToDateStringMDTime(data.create_time.toLong()))
                setText(R.id.content, HtmlCompat.fromHtml(data.content, HtmlCompat.FROM_HTML_MODE_COMPACT))
            }

            override fun onItemClick(data: MineMessageCenter) {
                showPageLoadingDialog()
                if (data.apiType == "1"){
                    MineApi.getAnchorMoments(data.dynamic_id){
                        onSuccess {
                            LaunchUtils.jumpAnchor(context, it[0])
                            hidePageLoadingDialog()
                        }
                        onFailed { ToastUtils.show("获取数据失败")
                            hidePageLoadingDialog()}
                    }
                }else if (data.apiType == "2"){
                    MineApi.getHotDiscussSingle(data.dynamic_id){
                        onSuccess {
                            LaunchUtils.startFragment(context,CommentOnFragment(it))
                            hidePageLoadingDialog()
                        }
                        onFailed { ToastUtils.show("获取数据失败")
                            hidePageLoadingDialog()}
                    }

                }

            }
        }
    }

    /**
     * 官方活动，系统消息 adapter
     */
    inner class Adapter2 : BaseRecyclerAdapter<MineMessageCenter>(getPageActivity()) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineMessageCenter> {
            return UserHolder(parent)
        }

        inner class UserHolder(parent: ViewGroup) : BaseViewHolder<MineMessageCenter>(getContext(), parent, R.layout.holder_message_center) {
            override fun onBindData(data: MineMessageCenter) {
                val webView = findView<WebView>(R.id.wbContent)
                webView.loadDataWithBaseURL(null, data.content, "text/html", "utf-8", null)
                webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                webView.settings.javaScriptEnabled = true
                webView.settings.textZoom = 80
                webView.setBackgroundColor(0)
                setText(R.id.tvTime, data.createtime_txt)
                when (type) {
                    2 -> setImageResource(findView(R.id.imgType), R.mipmap.ic_message_xt)
                    1 -> setImageResource(findView(R.id.imgType), R.mipmap.ic_message_gf)
                }
            }

        }
    }

}