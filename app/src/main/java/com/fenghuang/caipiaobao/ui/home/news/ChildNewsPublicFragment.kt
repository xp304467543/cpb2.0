package com.fenghuang.caipiaobao.ui.home.news

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.fragment.BaseContentFragment
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.widget.round.RoundTextView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.IntentConstant
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.home.data.HomeNewsResponse
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.LaunchUtils
import kotlinx.android.synthetic.main.fragment_news_child.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-02
 * @ Describe 资讯页面
 *
 */

class ChildNewsPublicFragment : BaseContentFragment() {

    lateinit var adapter: NewsAdapter

    override fun getContentResID() = R.layout.fragment_news_child

    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }


    override fun initContentView() {
        setVisible(pageLoading)
        adapter = NewsAdapter()
        rvNewsPublish.adapter = adapter
        rvNewsPublish.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
    }

    override fun initData() {
        getNewsList()
    }

    private fun getNewsList() {
        HomeApi.getNewsList(type = arguments?.getString(IntentConstant.LIVE_ROOM_LOTTERY_TYPE,"1")?:"1") {
            onSuccess {
                if (it.isNullOrEmpty()) setVisible(noData) else adapter.addAll(it)
                setGone(pageLoading)
            }
            onFailed {
                ToastUtils.show(it.getMsg().toString())
                setGone(pageLoading)
            }
        }
    }


    internal enum class ITEM_TYPE {
        HOLDER_1,
        HOLDER_2,
        HOLDER_3,
        HOLDER_4
    }

    inner class NewsAdapter : BaseRecyclerAdapter<HomeNewsResponse>(getPageActivity()) {


        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeNewsResponse> {
            return when (viewType) {
                ITEM_TYPE.HOLDER_1.ordinal -> NewsHolder1(parent)
                ITEM_TYPE.HOLDER_2.ordinal -> NewsHolder2(parent)
                ITEM_TYPE.HOLDER_3.ordinal -> NewsHolder3(parent)
                else -> NewsHolder4(parent)
            }
        }

        inner class NewsHolder1(parent: ViewGroup) : BaseViewHolder<HomeNewsResponse>(getContext(), parent, R.layout.holder_public_news1) {
            override fun onBindData(data: HomeNewsResponse) {
                setText(R.id.tvTitle_h1, data.title)
                setText(R.id.tvTime_h1, data.timegap)
                if (!data.cover_img.isNullOrEmpty()) ImageManager.loadImg(data.cover_img!![0], findView(R.id.contentImg_h1))
                data.type?.let { getTagBg(it, findView(R.id.tvTag_h1), data.type_txt.toString()) }
            }

            override fun onItemClick(data: HomeNewsResponse) {
                if (FastClickUtils.isFastClick()){
                    LaunchUtils.startFragment(getContext(), NewsInfoFragment.newInstance(data.info_id))
                }

            }
        }

        inner class NewsHolder2(parent: ViewGroup) : BaseViewHolder<HomeNewsResponse>(getContext(), parent, R.layout.holder_public_news2) {
            override fun onBindData(data: HomeNewsResponse) {
                setText(R.id.tvTitle_h2, data.title)
                setText(R.id.tvTime_h2, data.timegap)
                if (!data.cover_img.isNullOrEmpty() && data.cover_img?.size!! >= 2) {
                    ImageManager.loadImg(data.cover_img!![0], findView(R.id.img_1))
                    ImageManager.loadImg(data.cover_img!![1], findView(R.id.img_2))
                } else if (data.cover_img?.size == 1) {
                    ImageManager.loadImg(data.cover_img!![0], findView(R.id.img_1))
                }
                data.type?.let { getTagBg(it, findView(R.id.tvTag_h2), data.type_txt.toString()) }
            }

            override fun onItemClick(data: HomeNewsResponse) {
                LaunchUtils.startFragment(getContext(), NewsInfoFragment.newInstance(data.info_id))
            }
        }

        inner class NewsHolder3(parent: ViewGroup) : BaseViewHolder<HomeNewsResponse>(getContext(), parent, R.layout.holder_public_news3) {
            override fun onBindData(data: HomeNewsResponse) {
                setText(R.id.tvTitle_h3, data.title)
                setText(R.id.tvTime_h3, data.timegap)
                data.type?.let { getTagBg(it, findView(R.id.tvTag_h3), data.type_txt.toString()) }
            }

            override fun onItemClick(data: HomeNewsResponse) {
                LaunchUtils.startFragment(getContext(), NewsInfoFragment.newInstance(data.info_id))
            }
        }

        inner class NewsHolder4(parent: ViewGroup) : BaseViewHolder<HomeNewsResponse>(getContext(), parent, R.layout.holder_public_news4) {
            override fun onBindData(data: HomeNewsResponse) {
                setText(R.id.tvTitle_h4, data.title)
                setText(R.id.tvTime_h4, data.timegap)
                data.type?.let { getTagBg(it, findView(R.id.tvTag_h4), data.type_txt.toString()) }
            }

            override fun onItemClick(data: HomeNewsResponse) {
                LaunchUtils.startFragment(getContext(), NewsInfoFragment.newInstance(data.info_id))
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when {
                getItemData(position).settype == "1" -> ITEM_TYPE.HOLDER_1.ordinal
                getItemData(position).settype == "2" -> ITEM_TYPE.HOLDER_2.ordinal
                getItemData(position).settype == "3" -> ITEM_TYPE.HOLDER_3.ordinal
                else -> ITEM_TYPE.HOLDER_4.ordinal
            }
        }

        fun getTagBg(type: String, view: RoundTextView, text: String) {
            when (type) {
                "1" -> {
                    view.delegate.backgroundColor = Color.parseColor("#EDE5F9")
                    view.setTextColor(Color.parseColor("#D444F3"))
                }
                "2" -> {
                    view.delegate.backgroundColor = Color.parseColor("#FFF9E8")
                    view.setTextColor(Color.parseColor("#FD8208"))
                }
                "3" -> {
                    view.delegate.backgroundColor = Color.parseColor("#F5F7FA")
                    view.setTextColor(Color.parseColor("#79818C"))
                }
                "4" -> {
                    view.delegate.backgroundColor = Color.parseColor("#FFECE8")
                    view.setTextColor(Color.parseColor("#FF513E"))
                }
            }
            view.text = text
        }

    }


    companion object {
        fun newInstance(type: String): ChildNewsPublicFragment {
            val fragment = ChildNewsPublicFragment()
            val bundle = Bundle()
            bundle.putString(IntentConstant.LIVE_ROOM_LOTTERY_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }
}