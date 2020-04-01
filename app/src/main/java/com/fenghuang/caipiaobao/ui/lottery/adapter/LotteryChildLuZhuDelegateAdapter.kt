package com.fenghuang.caipiaobao.ui.lottery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.yc.cn.ycbaseadapterlib.BaseViewHolder

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-03
 * @ Describe
 *
 */

open class LotteryChildLuZhuDelegateAdapter(private val mContext: Context, private val mLayoutHelper: LayoutHelper, layoutId: Int, count: Int, viewTypeItem: Int) : DelegateAdapter.Adapter<BaseViewHolder>() {

    private var mCount = -1
    private var mLayoutId = -1
    private var mViewTypeItem = -1

    init {
        this.mCount = count
        this.mLayoutId = layoutId
        this.mViewTypeItem = viewTypeItem
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return mLayoutHelper
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return (if (viewType == mViewTypeItem) {
            BaseViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false))
        } else null)!!
    }

    /**
     * 子类adapter实现
     * @param holder                    holder
     * @param position                  索引
     */
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
    }

    /**
     * 必须重写不然会出现滑动不流畅的情况
     */
    override fun getItemViewType(position: Int): Int {
        return mViewTypeItem
    }

    /**
     * 条目数量
     */
    override fun getItemCount(): Int {
        return mCount
    }
}