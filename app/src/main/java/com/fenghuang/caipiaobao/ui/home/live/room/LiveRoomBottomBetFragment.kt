package com.fenghuang.caipiaobao.ui.home.live.room

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryTypeResponse
import com.fenghuang.caipiaobao.widget.dialog.bottom.BaseBottomSheetFragment
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomLotterySeletDialog
import kotlinx.android.synthetic.main.dialog_lottery_select.*
import kotlinx.android.synthetic.main.dialog_wheel_view.tvWheelSure

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-07
 * @ Describe 直播间投注
 *
 */

class LiveRoomBottomBetFragment(override val layoutResId: Int = R.layout.dialog_live_bet) : BaseBottomSheetFragment() {

    private var tvOpenCount: TextView? = null
    private var opt1SelectedPosition: Int = 0
    private var tvLotterySelectType: TextView? = null

    override fun initView() {
        rootView?.findViewById<ImageView>(R.id.imgBetCLose)?.setOnClickListener {
            dismiss()
        }
        rootView?.findViewById<View>(R.id.tvLiveBetClose)?.setOnClickListener {
            dismiss()
        }
        tvLotterySelectType = rootView?.findViewById(R.id.tvLotterySelectType)
        tvOpenCount = rootView?.findViewById(R.id.tvOpenCount)
    }


    override fun initData() {
        val type = LotteryApi.getLotteryType()
        type.onSuccess {
            val title = arrayListOf<String>()
            val list = arrayListOf<LotteryTypeResponse>()
            for (data in it) {
                if (data.cname != "香港彩") {
                    list.add(data)
                    title.add(data.cname)
                }
            }
            if (!title.isNullOrEmpty()) initDialog(title, list)
        }
    }

    private fun initDialog(title: ArrayList<String>, list: List<LotteryTypeResponse>) {
        tvLotterySelectType?.setOnClickListener {
            mContext?.let {
                val lortterySelectDialog = BottomLotterySeletDialog(it, title)
                tvLotterySelectType?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.select_top, 0)
                lortterySelectDialog.setOnDismissListener {
                    tvLotterySelectType?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.select_bottom, 0)
                }
                lortterySelectDialog.tvWheelSure.setOnClickListener {
                    tvLotterySelectType?.text = lortterySelectDialog.lotteryPickerView.opt1SelectedData as String
                    opt1SelectedPosition = lortterySelectDialog.lotteryPickerView.opt1SelectedPosition
                    getLotteryNewCode(list[opt1SelectedPosition].lottery_id)
                    lortterySelectDialog.dismiss()
                }
                lortterySelectDialog.lotteryPickerView.opt1SelectedPosition = opt1SelectedPosition
                lortterySelectDialog.show()
            }
        }
    }

    private fun getLotteryNewCode(lottery_id: String) {
        LotteryApi.getLotteryNewCode(lottery_id) {
            onSuccess {
                tvOpenCount?.text = it.issue + "期开奖结果"

            }
        }
    }
}