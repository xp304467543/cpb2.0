package com.fenghuang.caipiaobao.ui.home.live.room.bet

import android.view.View
import android.widget.TextView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomLottertSeletDialog
import kotlinx.android.synthetic.main.dialog_lottery_select.*
import kotlinx.android.synthetic.main.fragment_live_bet.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-13
 * @ Describe 投主页(直播间)
 *
 */

class LiveRoomBottomBetFragment : BaseNormalFragment(){

    private var opt1SelectedPosition: Int = 0

    override fun getLayoutRes()= R.layout.fragment_live_bet


    private var betToolsListen: ((int: Int) -> Unit)? = null

    fun setBetToolsListen(listener: (int: Int) -> Unit) {
        betToolsListen = listener
    }

    override fun initView(rootView: View?) {
        rootView?.findViewById<TextView>(R.id.tvBetTools)?.setOnClickListener {
            betToolsListen?.invoke(2)
        }
        rootView?.findViewById<TextView>(R.id.tvBetRecord)?.setOnClickListener {
            betToolsListen?.invoke(3)
        }
    }

    override fun initData() {
        val type = LotteryApi.getLotteryType()
        type.onSuccess {
            val title = arrayListOf<String>()
            for (data in it) {
                title.add(data.cname)
            }
            if (!title.isNullOrEmpty()) initDialog(title)
        }
    }


    private fun initDialog(title: ArrayList<String>) {
        tvLotterySelectType?.text = title[0]
        tvLotterySelectType?.setOnClickListener {
                val lotterySelectDialog = BottomLottertSeletDialog(context!!, title)
                tvLotterySelectType?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.select_top, 0)
                lotterySelectDialog.setOnDismissListener {
                    tvLotterySelectType?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.select_bottom, 0)
                }
                lotterySelectDialog.tvWheelSure.setOnClickListener {
                    tvLotterySelectType?.text = lotterySelectDialog.lotteryPickerView.opt1SelectedData as String
                    opt1SelectedPosition = lotterySelectDialog.lotteryPickerView.opt1SelectedPosition
                    lotterySelectDialog.dismiss()
                }
                lotterySelectDialog.lotteryPickerView.opt1SelectedPosition = opt1SelectedPosition
                lotterySelectDialog.show()
        }
    }






}