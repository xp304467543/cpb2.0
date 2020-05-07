package com.fenghuang.caipiaobao.widget.pop

import android.content.Context
import android.os.Handler
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.popup.BasePopupWindow
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.data.LineCheck
import com.fenghuang.caipiaobao.utils.PingUtil

import com.fenghuang.caipiaobao.widget.SwitchButtonRed


/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/7
 * @ Describe
 *
 */

class LotteryLinePop(context: Context, listLine: List<LineCheck>) : BasePopupWindow(context, R.layout.pop_line_select) {

    private var lotteryLineView: RecyclerView


    private var getSelectListener: ((it: String,pos:Int,ms:String) -> Unit)? = null


    fun setSelectListener(GetSelectListener: ((it: String,pos:Int,ms:String) -> Unit)) {
        getSelectListener = GetSelectListener
    }


    init {
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        lotteryLineView = findView(R.id.lotteryLine)
        lotteryLineView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = LineAdapter(context)
        adapter.addAll(listLine)
        lotteryLineView.adapter = adapter
    }

    inner class LineAdapter(context: Context) : BaseRecyclerAdapter<LineCheck>(context) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LineCheck> {
            return LineHolder(parent)
        }

        inner class LineHolder(parent: ViewGroup) : BaseViewHolder<LineCheck>(context, parent, R.layout.holder_line) {
            override fun onBindData(data: LineCheck) {
                val check = findView<SwitchButtonRed>(R.id.lineSwitch)
                check.isChecked = data.boolean
                val tvMs = findView<TextView>(R.id.tvLineMs)
                Handler().post {
                    if (tvMs.text.toString().isEmpty()) {
                        val s1 = PingUtil.getAvgRTT(data.url)
                        setText(R.id.tvLineMs, s1.toString() + "ms")
                        if (s1 > 100) {
                            setTextColor(R.id.tvLineMs, ViewUtils.getColor(R.color.colorYellow))
                        } else {
                            setTextColor(R.id.tvLineMs, ViewUtils.getColor(R.color.colorGreen))
                        }
                    }
                }

                setText(R.id.tvLine, "线路 " + (getDataPosition() + 1))


                findView<SwitchButtonRed>(R.id.lineSwitch).setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        for ((index, result) in getAllData().withIndex()) {
                            result.boolean = index == getDataPosition()
                        }
                        Handler().postDelayed({ notifyDataSetChanged() }, 200)
                        val text = tvMs.text.toString()
                        getSelectListener?.invoke(data.url,getDataPosition(),text.substring(0,text.length-2))
                    }
                }
            }

        }
    }
}