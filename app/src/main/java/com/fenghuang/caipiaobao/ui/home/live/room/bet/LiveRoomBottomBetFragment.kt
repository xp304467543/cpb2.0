package com.fenghuang.caipiaobao.ui.home.live.room.bet

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryTypeSelectUtil
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryTypeResponse
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomLotterySeletDialog
import kotlinx.android.synthetic.main.bottom_dialog_wheel_view.tvWheelSure
import kotlinx.android.synthetic.main.dialog_lottery_select.*
import kotlinx.android.synthetic.main.fragment_live_bet.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-13
 * @ Describe 投主页(直播间)
 *
 */

class LiveRoomBottomBetFragment : BaseNormalFragment() {

    private var opt1SelectedPosition: Int = 0

    override fun getLayoutRes() = R.layout.fragment_live_bet


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
            val list = arrayListOf<LotteryTypeResponse>()
            for (data in it) {
                if (data.cname != "香港彩") {
                    list.add(data)
                    title.add(data.cname)
                }
            }
            if (!title.isNullOrEmpty()) initDialog(title, list)
        }
        getLotteryNewCode("1")//默认加载重庆时时彩
    }


    private fun initDialog(title: ArrayList<String>, list: ArrayList<LotteryTypeResponse>) {
        tvLotterySelectType?.text = title[0]
        tvLotterySelectType?.setOnClickListener {
            val lotterySelectDialog = BottomLotterySeletDialog(context!!, title)
            tvLotterySelectType?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.select_top, 0)
            lotterySelectDialog.setOnDismissListener {
                tvLotterySelectType?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.select_bottom, 0)
            }
            lotterySelectDialog.tvWheelSure.setOnClickListener {
                tvLotterySelectType?.text = lotterySelectDialog.lotteryPickerView.opt1SelectedData as String
                opt1SelectedPosition = lotterySelectDialog.lotteryPickerView.opt1SelectedPosition
                getLotteryNewCode(list[opt1SelectedPosition].lottery_id)
                lotterySelectDialog.dismiss()
            }
            lotterySelectDialog.lotteryPickerView.opt1SelectedPosition = opt1SelectedPosition
            lotterySelectDialog.show()
        }
    }

    //开奖结果
    private fun getLotteryNewCode(lottery_id: String) {
        LotteryApi.getLotteryNewCode(lottery_id) {
            onSuccess {
                if (it.next_lottery_time.toInt() > 1) {
                    tvOpenCount?.text = (it.issue + "期")
                    countDownTime(it.next_lottery_time, lottery_id)
                    tvCloseTime.text = ("封盘" + TimeUtils.getDateToHMSString(it.next_lottery_end_time * 1000))
                    //更新最新开奖数据
                    LotteryTypeSelectUtil.addOpenCode(context!!, linLotteryOpenCode, it.code.split(","), it.lottery_id)
                    tvOpenCodePlaceHolder.visibility = View.GONE
                } else {
                    if (timer != null) timer?.cancel()
                    if (isVisible) {
                        tvOpenCodePlaceHolder.visibility = View.VISIBLE
                        tvOpenTime.text = "开奖中..."
                        tvNext.text = getString(R.string.lottery_next_time)
                        tvOpenCount.text = (it.issue + "期")
                    }

                }
            }
        }
    }


    // ===== 倒计时 =====
    var timer: CountDownTimer? = null

    fun countDownTime(millisUntilFinished: String, lotteryId: String) {
        tvNext.text = getString(R.string.lottery_next_time)
        if (timer != null) timer?.cancel()
        val timeCountDown = millisUntilFinished.toLong() * 1000
        timer = object : CountDownTimer(timeCountDown, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val day: Long = millisUntilFinished / (1000 * 60 * 60 * 24)/*单位 天*/
                val hour: Long = (millisUntilFinished - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)/*单位 时*/
                val minute: Long = (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60)/*单位 分*/
                val second: Long = (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000 /*单位 秒*/
                if (tvCloseTime != null) {
                    when {
                        day > 0 -> tvOpenTime.text = "开奖" + dataLong(day) + "天" + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        hour > 0 -> tvOpenTime.text = "开奖" + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        else -> tvOpenTime.text = "开奖" + dataLong(minute) + ":" + dataLong(second)
                    }
                }
            }

            override fun onFinish() {
                if (isVisible) {
                    tvOpenTime!!.text = "开奖中..."
                    tvOpenCodePlaceHolder.visibility = View.VISIBLE
                }
                //getLotteryNewCode(lotteryId)
            }
        }
        if (timer != null) timer?.start()
    }

    // 这个方法是保证时间两位数据显示，如果为1点时，就为01
    fun dataLong(c: Long): String {
        return if (c >= 10)
            c.toString()
        else "0$c"
    }


}