package com.fenghuang.caipiaobao.ui.moments.childern

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.moments.data.MomentsRecommend


/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe
 *
 */

class MomentsRecommendAdapter(context: Context) : BaseRecyclerAdapter<MomentsRecommend>(context) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MomentsRecommend> {
        return MomentsRecommendHolder(parent)
    }

    inner class MomentsRecommendHolder(parent: ViewGroup) : BaseViewHolder<MomentsRecommend>(getContext(), parent, R.layout.holder_moments_recommend) {
        override fun onBindData(data: MomentsRecommend) {
            ImageManager.loadImg(data.icon, findView(R.id.imgRecommend))
            setText(R.id.tvRecommendName, data.title)
            setText(R.id.tvRecommendContent, data.description)

            setOnClick(R.id.tvDownLoad)
        }


        override fun onClick(id: Int) {
            if (id == R.id.tvDownLoad) {
                try {
                    val uri = Uri.parse(getData()?.url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                } catch (e: Exception) {
                    ToastUtils.show("地址解析异常")
                }

            }
        }

    }
}