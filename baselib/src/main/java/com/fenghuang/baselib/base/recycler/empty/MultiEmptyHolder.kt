package com.fenghuang.baselib.base.recycler.empty

import android.content.Context
import android.view.ViewGroup
import com.fenghuang.baselib.R
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.base.recycler.multitype.MultiTypeViewHolder
import com.fenghuang.baselib.utils.ViewUtils

class MultiEmptyHolder(private val height: Int = 12, private val color: Int = 0) : MultiTypeViewHolder<RecyclerEmptyBean, MultiEmptyHolder.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup) = ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) : BaseViewHolder<RecyclerEmptyBean>(context, parent, R.layout.base_holder_empty) {
        override fun onBindView(context: Context?) {
            itemView.post {
                val params = itemView.layoutParams
                params.height = ViewUtils.dp2px(height)
                itemView.layoutParams = params
            }
            if (color != 0) {
                itemView.setBackgroundColor(getColor(color))
            }
        }

        override fun onBindData(data: RecyclerEmptyBean) {
        }

    }
}