package com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryBet


/**
 *
 * @ Author  QinTian
 * @ Date  2020/4/26
 * @ Describe
 *
 */

class LiveRoomBetAccessAdapter(context: Context, var mooney: String) : BaseRecyclerAdapter<LotteryBet>(context) {

    var now = "-1"

    private var onMoneyChangeListener: ((it: String, pos: Int) -> Unit)? = null

    private var textWatcher: TextWatcher? = null

    fun onMoneyChangeListener(OnMoneyChangeListener: ((it: String, pos: Int) -> Unit)) {
        onMoneyChangeListener = OnMoneyChangeListener
    }


    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LotteryBet> {

        return LiveRoomBetAccessHolder(parent)
    }

    inner class LiveRoomBetAccessHolder(parent: ViewGroup) : BaseViewHolder<LotteryBet>(getContext(), parent, R.layout.holder_bet_access) {
        override fun onBindData(data: LotteryBet) {
            val edit = findView<EditText>(R.id.tvBetPlayMoney)
            setText(R.id.tvBetPlayName, data.playName)
            setText(R.id.tvBetPlayType, data.result.play_class_cname)
            setText(R.id.tvBetPlayOdds, data.result.play_odds.toString())
            textWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }
            //1、为了避免TextWatcher在第2步被调用，提前将他移除。
            if (edit.tag is TextWatcher) {
                edit.removeTextChangedListener(edit.tag as TextWatcher)
            }

            // 第2步：移除TextWatcher之后，设置EditText的Text。
            if (now != "-1") {
                edit.setText(now)
                edit.setSelection(now.length)
            } else edit.setText(mooney)


            val watcher: TextWatcher = object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    if (!TextUtils.isEmpty(editable.toString()) && editable.toString().toInt() < 10) {
                        ToastUtils.show("请输入≥10的整数")
                    }
                    now = editable.toString()
                    onMoneyChangeListener?.invoke(now, getDataPosition())
                }
            }
            edit.addTextChangedListener(watcher)
            edit.tag = watcher
        }
    }


}