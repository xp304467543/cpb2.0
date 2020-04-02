package com.fenghuang.caipiaobao.ui.home.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.utils.ViewUtils.getColor
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeExpertList
import com.fenghuang.caipiaobao.utils.LaunchUtils
import java.math.BigDecimal


/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/31- 18:06
 * @ Describe
 *
 */

class ExpertHotAdapter(context: Context) : BaseRecyclerAdapter<HomeExpertList>(context) {


    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HomeExpertList> {
        return ExpertHotHolder(parent)
    }


    inner class ExpertHotHolder(parent: ViewGroup) : BaseViewHolder<HomeExpertList>(getContext(), parent, R.layout.holder_home_expert_hot) {
        override fun onBindData(data: HomeExpertList) {
            setText(R.id.tvExpertName, data.nickname)
            setText(R.id.tvLotteryTpe, data.lottery_name)
            if (data.win_rate != "加载中...") setText(R.id.rtvExpertWinPresent, "胜率 " + BigDecimal(data.win_rate).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal(100)).stripTrailingZeros().toPlainString() + "%")
            else setText(R.id.rtvExpertWinPresent, data.win_rate + "%")
            setText(R.id.rtvExpertWinAdd, data.winning + " 连红")
            if (data.profit_rate != "加载中...") setText(R.id.tvPercentage, BigDecimal(data.profit_rate).setScale(4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal(100)).stripTrailingZeros().toPlainString())
            else setText(R.id.tvPercentage, data.profit_rate)
            ImageManager.loadImg(data.avatar, findView(R.id.expertAvatar))
            setTextList(data, findView(R.id.linTenList))
        }

        override fun onItemClick(data: HomeExpertList) {
            LaunchUtils.startPersonalPage(getContext(),data.id,3,lotteryId = data.lottery_id)
        }
    }

    //修改字体颜色
    fun setTextList(data: HomeExpertList, linearLayout: LinearLayout) {
        when {
            data.last_10_games == null || data.last_10_games!!.isEmpty() -> {
                linearLayout.removeAllViews()
                val textView = TextView(getContext())
                textView.textSize = 14f
                textView.setTextColor(getColor(R.color.color_333333))
                textView.text = "暂无"
                linearLayout.addView(textView)
            }
            else -> {
                linearLayout.removeAllViews()
                val width = (ViewUtils.getScreenWidth() - ViewUtils.dp2px(100)) / 10
                for (result in data.last_10_games!!) {
                    val textView = TextView(getContext())
                    textView.textSize = 14f
                    textView.typeface = Typeface.DEFAULT_BOLD
                    if (result == "1") {
                        textView.text = "胜"
                        textView.setTextColor(getColor(R.color.color_FF513E))
                    } else {
                        textView.text = "负"
                        textView.setTextColor(getColor(R.color.color_999999))
                    }
                    linearLayout.addView(textView)
                    val params = textView.layoutParams as LinearLayout.LayoutParams
                    params.width = width

                }
            }
        }
    }

}