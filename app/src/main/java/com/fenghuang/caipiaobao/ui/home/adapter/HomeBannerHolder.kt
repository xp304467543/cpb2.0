package com.fenghuang.caipiaobao.ui.home.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeBannerResponse
import com.fenghuang.caipiaobao.ui.home.news.HomeBannerJump
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.pingerx.banner.holder.BaseBannerHolder

/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/26- 16:59
 * @ Describe
 *
 */

class HomeBannerHolder(private var isNormalBanner: Boolean, val context: Context) : BaseBannerHolder<HomeBannerResponse> {

    override fun getHolderResId(): Int {
        return R.layout.holder_banner

    }

    override fun onBindData(itemView: View, data: HomeBannerResponse) {
        if (TextUtils.isEmpty(data.image_url)) {
            itemView.findViewById<ImageView>(R.id.imageView).setImageResource(R.mipmap.ic_placeholder)
        } else {
            ImageManager.loadImg(data.image_url, itemView.findViewById(R.id.imageView))
        }
//        itemView.findViewById<TextView>(R.id.textView)?.text = data.title
    }

    override fun onPageClick(itemView: View, position: Int, data: HomeBannerResponse) {
        if (data.url != "") {
            LaunchUtils.startFragment(context, HomeBannerJump.newInstance(data.url, data.title))
        }
    }
}