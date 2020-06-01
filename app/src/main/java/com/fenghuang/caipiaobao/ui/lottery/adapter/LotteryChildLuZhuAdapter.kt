package com.fenghuang.caipiaobao.ui.lottery.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.round.RoundTextView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryComposeUtil
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryConstant
import com.fenghuang.caipiaobao.widget.VerticalTextView
import com.google.gson.JsonArray
import java.util.*
import kotlin.collections.ArrayList


/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-15
 * @ Describe
 *
 */
class LotteryChildLuZhuAdapter(context: Context, private val lotteryID: String) : BaseRecyclerAdapter<List<List<String>>>(context) {

    var type: String = LotteryConstant.TYPE_LUZHU_2
    var total: JsonArray? = null
    var hideList: ArrayList<Boolean>? = null

    var isRePlay = true

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<List<List<String>>> {
        return LotteryLuZhuHolder(parent)
    }

    inner class LotteryLuZhuHolder(parent: ViewGroup) : BaseViewHolder<List<List<String>>>(getContext(), parent, R.layout.holder_lottery_child_luzhu) {
        override fun onBindData(data: List<List<String>>) {
            LotteryComposeUtil.initTitle(findView(R.id.tvLuZhuTotal), findView(R.id.tvLuZhuBallIndex), lotteryID, type, total, getDataPosition())
            initLuZhuData(findView(R.id.horizontalRecycle), data)
            val param = itemView.layoutParams
            when(UserInfoSp.getSkinSelect()){
                1 -> findView<RoundTextView>(R.id.topLine).delegate.backgroundColor = ViewUtils.getColor(R.color.text_red)
                2 -> findView<RoundTextView>(R.id.topLine).delegate.backgroundColor = ViewUtils.getColor(R.color.text_red)
                3 -> findView<RoundTextView>(R.id.topLine).delegate.backgroundColor = ViewUtils.getColor(R.color.alivc_green)
            }
            if (hideList != null && hideList!!.isNotEmpty()) {
                if (!hideList!![getDataPosition()]) {
                    param.height = 0
                    param.width = 0
                    setGone(itemView)
                } else {
                    param.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                    param.width = ConstraintLayout.LayoutParams.MATCH_PARENT
                    setVisible(itemView)
                }
            }else {
                param.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                param.width = ConstraintLayout.LayoutParams.MATCH_PARENT
                setVisible(itemView)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun initLuZhuData(recyclerView: RecyclerView, data: List<List<String>>) {
        val adapter = LotteryChildLuZhuTestItem(getContext())
        recyclerView.setItemViewCacheSize(300)
        val layout = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layout
        recyclerView.adapter = adapter
        layout.reverseLayout = true//列表翻转
        if (isRePlay){
            Collections.reverse(data) //反向排序
        }
        adapter.addAll(data)
    }


    inner class LotteryChildLuZhuTestItem(context: Context) : BaseRecyclerAdapter<List<String>>(context) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<List<String>> {
            return LotteryChildLuZhuTestItemHolder(parent)
        }

        inner class LotteryChildLuZhuTestItemHolder(parent: ViewGroup) : BaseViewHolder<List<String>>(getContext(), parent, R.layout.holder_lottery_child_item_test_luzhu) {
            override fun onBindData(data: List<String>) {
                val view = findView<VerticalTextView>(R.id.linLuZhuDataItemText)
                for (it in data) {
                    if (it != "") view.setText(it.split("_")[1]) else view.setTextEmpty()
                }
                when {
                    getDataPosition() == 0 -> view.setBackgroundResource(R.drawable.storke_right)
                    getDataPosition() % 2 == 0 -> view.setBackgroundResource(R.drawable.storke_left_grey)
                    else -> view.setBackgroundResource(R.drawable.storke_left_white)
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

    }

    fun notifyHideItem(it: ArrayList<Boolean>) {
        hideList = it
        isRePlay = false
        notifyDataSetChanged()
    }

    fun  clearList(){
        hideList?.clear()
    }

}