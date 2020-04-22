package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryTypeSelectUtil
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryTypeResponse
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomDialogFragment
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomLotterySelectDialog
import kotlinx.android.synthetic.main.dialog_lottery_select.*
import kotlinx.android.synthetic.main.fragment_live_bet.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe 投注
 *
 */

class LiveRoomBetFragment : BottomDialogFragment() {

    private var opt1SelectedPosition: Int = 0

    private var currentLotteryId = "1"

    private var resultList: ArrayList<LotteryTypeResponse>? = null

    private var liveRoomBetToolsFragment: LiveRoomBetToolsFragment? = null

    private var liveRoomBetRecordFragment: LiveRoomBetRecordFragment? = null

    override val layoutResId: Int = R.layout.fragment_live_bet

    override fun isShowTop(): Boolean = true

    override fun canceledOnTouchOutside(): Boolean = false

    override fun initView() {
    }

    override fun initData() {
        val type = LotteryApi.getLotteryBetType()
        type.onSuccess {
            val title = arrayListOf<String>()
            resultList = arrayListOf()
            for (data in it) {
                resultList?.add(data)
                title.add(data.cname)
            }
            if (!title.isNullOrEmpty()) initDialog(title, resultList!!)
        }
        getLotteryNewCode("1")//默认加载重庆时时彩  1
    }

    override fun initFragment() {
        rootView?.findViewById<TextView>(R.id.tvBetTools)?.setOnClickListener {
            liveRoomBetToolsFragment = LiveRoomBetToolsFragment.newInstance(lotteryID = currentLotteryId)
            liveRoomBetToolsFragment?.show(fragmentManager, "LiveRoomBetToolsFragment")
        }
        rootView?.findViewById<TextView>(R.id.tvBetRecord)?.setOnClickListener {
            liveRoomBetRecordFragment = LiveRoomBetRecordFragment()
            liveRoomBetRecordFragment?.show(fragmentManager, "liveRoomBetRecordFragment")
        }
        rootView?.findViewById<ImageView>(R.id.imgBetCLose)?.setOnClickListener {
            dismiss()
        }
    }


    private fun initDialog(title: ArrayList<String>, list: ArrayList<LotteryTypeResponse>) {
        tvLotterySelectType?.text = title[0]
        tvLotterySelectType?.setOnClickListener {
            val lotterySelectDialog = BottomLotterySelectDialog(context!!, title)
            tvLotterySelectType?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.select_top, 0)
            lotterySelectDialog.setOnDismissListener {
                tvLotterySelectType?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.select_bottom, 0)
            }
            lotterySelectDialog.tvLotteryWheelSure.setOnClickListener {
                tvLotterySelectType?.text = lotterySelectDialog.lotteryPickerView.opt1SelectedData as String
                opt1SelectedPosition = lotterySelectDialog.lotteryPickerView.opt1SelectedPosition
                currentLotteryId = list[opt1SelectedPosition].lottery_id
                getLotteryNewCode(currentLotteryId)
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
                if (isVisible) {
                    if (it.next_lottery_time.toInt() > 1) {
                        tvOpenCount?.text = (it.issue + " 期开奖结果   ")
                        countDownTime(it.next_lottery_time, lottery_id)
                        tvCloseTime.text = (TimeUtils.longToDateStringTimeHMS(it.next_lottery_end_time))
                        //更新最新开奖数据
                        LotteryTypeSelectUtil.addOpenCode(context!!, linLotteryOpenCode, it.code?.split(","), it.lottery_id)
                        tvOpenCodePlaceHolder.visibility = View.GONE
                    } else {
                        if (timer != null) timer?.cancel()

                        tvOpenCodePlaceHolder.visibility = View.VISIBLE
                        tvOpenTime.text = "开奖中..."
                        tvOpenCount.text = (it.issue + "期开奖结果   ")

                        getNewResult(it.lottery_id)
                    }
                }
            }
        }
    }


    // ===== 倒计时 =====
    var timer: CountDownTimer? = null

    private fun countDownTime(millisUntilFinished: String, lotteryId: String) {
        if (timer != null) timer?.cancel()
        handler?.removeCallbacks(runnable)
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
                        day > 0 -> tvOpenTime.text = dataLong(day) + "天" + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        hour > 0 -> tvOpenTime.text = dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        else -> tvOpenTime.text = dataLong(minute) + ":" + dataLong(second)
                    }
                }
            }

            override fun onFinish() {
                if (isVisible) {
                    tvOpenTime!!.text = "开奖中..."
                    tvOpenCodePlaceHolder.visibility = View.VISIBLE
                }
                getNewResult(lotteryId)
            }
        }
        if (timer != null) timer?.start()
    }

    var handler: Handler? = null
    var runnable: Runnable? = null

    fun getNewResult(lotteryId: String) {
        handler = Handler()
        runnable = Runnable {
            getLotteryNewCode(lotteryId)
        }
        handler?.postDelayed(runnable, 5000)
    }


    // 这个方法是保证时间两位数据显示，如果为1点时，就为01
    fun dataLong(c: Long): String {
        return if (c >= 10)
            c.toString()
        else "0$c"
    }


    override fun dismiss() {
        super.dismiss()
        handler?.removeCallbacks(runnable)
    }

}