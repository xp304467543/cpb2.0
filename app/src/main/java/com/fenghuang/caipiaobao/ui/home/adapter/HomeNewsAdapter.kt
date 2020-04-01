package com.fenghuang.caipiaobao.ui.home.adapter

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.round.RoundTextView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeNewsResponse
import com.fenghuang.caipiaobao.ui.home.news.NewsInfoFragment
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.LaunchUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/31- 15:38
 * @ Describe
 *
 */

class HomeNewsAdapter(context: Context) : BaseRecyclerAdapter<HomeNewsResponse>(context) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeNewsResponse> {
        return HomeNewsHolder(parent)
    }

    inner class HomeNewsHolder(parent: ViewGroup) : BaseViewHolder<HomeNewsResponse>(getContext(), parent, R.layout.holder_home_news) {
        override fun onBindData(data: HomeNewsResponse) {
            setText(R.id.tvTime, data.timegap)
            setText(R.id.tvNewsTitle, data.title)

            if (data.cover_img != null && data.cover_img!!.isNotEmpty()) ImageManager.loadImg(data.cover_img!![0], findView(R.id.imgNews))

            if (data.tag == "1"){
                findView<ImageView>(R.id.imgTagView).background = ViewUtils.getDrawable(R.mipmap.ic_new)
            }
            else {
                findView<ImageView>(R.id.imgTagView).background =ViewUtils.getDrawable(R.mipmap.ic_hot)
            }
            getTagBg(data.type.toString(),findView(R.id.imgTag), data.type_txt.toString())
        }

        override fun onItemClick(data: HomeNewsResponse) {
            if (FastClickUtils.isFastClick()){
                if (data.title!="加载中..."){
                    LaunchUtils.startFragment(getContext(), NewsInfoFragment(data.info_id))
                }

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
}
