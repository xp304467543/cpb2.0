package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.annotation.SuppressLint
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter.LiveBetStateAdapter
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryTypeSelectUtil
import com.fenghuang.caipiaobao.ui.lottery.data.*
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomDialogFragment
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomLotterySelectDialog
import com.flyco.tablayout.SlidingTabLayout
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
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

    private var vpGuss: ViewPager? = null

    private var tabGuss: SlidingTabLayout? = null

    private var currentLotteryId = "1"

    private var selectMoneyList: List<PlayMoneyData>? = null

    private var resultList: ArrayList<LotteryTypeResponse>? = null

    private var liveRoomBetToolsFragment: LiveRoomBetToolsFragment? = null

    private var liveRoomBetRecordFragment: LiveRoomBetRecordFragment? = null

    private var liveRoomBetAccessFragment: LiveRoomBetAccessFragment? = null

    private var playList = ArrayList<LotteryPlayListResponse>()

    override val layoutResId: Int = R.layout.fragment_live_bet

    override val resetHeight: Int = 0

    override fun isShowTop(): Boolean = true

    override fun canceledOnTouchOutside(): Boolean = false

    override fun initView() {
        RxBus.get().register(this)
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
        setTabLayout("1")
        getPlayMoney()
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
        rootView?.findViewById<TextView>(R.id.tvBetSubmit)?.setOnClickListener {
            if (betList.isNullOrEmpty()) {
                ToastUtils.show("请选择投注选项")
                return@setOnClickListener
            }
            liveRoomBetAccessFragment = LiveRoomBetAccessFragment()
            liveRoomBetAccessFragment?.show(fragmentManager, "liveRoomBetAccessFragment")
        }
    }


    //彩种选择
    private fun setTabLayout(lottery_id: String) {
        LotteryApi.getGuessPlayList(lottery_id) {
            onSuccess {
                playList.clear()
                playList.addAll(it)
                vpGuss = rootView?.findViewById(R.id.vpGuss)
                tabGuss = rootView?.findViewById(R.id.tabGuss)
                vpGuss?.adapter = LiveBetStateAdapter(childFragmentManager, playList)
                tabGuss?.setViewPager(vpGuss)

            }
        }
    }

    //底部弹框
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
                setTabLayout(list[opt1SelectedPosition].lottery_id)
                timer?.cancel()
                timerClose?.cancel()
                if (tvCloseTime != null) tvCloseTime.text = "--:--"
                if (tvOpenTime != null) tvOpenTime!!.text = "--:--"
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
                    if (it.next_lottery_time.toInt() > 0) {
                        tvOpenCount?.text = (it.issue + " 期开奖结果   ")
                        countDownTime(it.next_lottery_time, lottery_id)
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

                    if (it.next_lottery_end_time > 0) {
                        countDownTimerClose(it.next_lottery_end_time)
                    } else tvCloseTime.text = "--:--"
                }
            }

            onFailed {
                getNewResult(lottery_id)
            }
        }
    }


    //封盘倒计时
    var timerClose: CountDownTimer? = null
    private fun countDownTimerClose(millisUntilFinished: Long) {
        if (timerClose != null) timerClose?.cancel()
        val timeCountDown = millisUntilFinished * 1000
        timerClose = object : CountDownTimer(timeCountDown, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val day: Long = millisUntilFinished / (1000 * 60 * 60 * 24)/*单位 天*/
                val hour: Long = (millisUntilFinished - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)/*单位 时*/
                val minute: Long = (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60)/*单位 分*/
                val second: Long = (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000 /*单位 秒*/
                if (tvCloseTime != null) {
                    when {
                        day > 0 -> tvCloseTime.text = dataLong(day) + "天" + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        hour > 0 -> tvCloseTime.text = dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        else -> tvCloseTime.text = dataLong(minute) + ":" + dataLong(second)
                    }
                }
            }

            override fun onFinish() {
                if (tvCloseTime != null) tvCloseTime.text = "--:--"
            }
        }
        if (timerClose != null) timerClose?.start()
    }


    // ===== 开奖倒计时 =====
    var timer: CountDownTimer? = null
    var handler: Handler? = null
    var runnable: Runnable? = null

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


    /**
     * 快选金额
     */
    @SuppressLint("ResourceType", "SetTextI18n")
    private fun getPlayMoney() {
        if (selectMoneyList.isNullOrEmpty()) {
            LotteryApi.lotteryBetMoney {
                onSuccess {
                    selectMoneyList = it
                    for ((index, res) in it.withIndex()) {
                        val radio = RadioButton(context)
                        radio.buttonDrawable = null
                        radio.background = ViewUtils.getDrawable(R.drawable.lottery_bet_radio)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) radio.setTextColor(context?.getColorStateList(R.drawable.color_radio_bet))
                        radio.textSize = ViewUtils.dp2px(2.5f)
                        radio.gravity = Gravity.CENTER
                        radio.text = res.play_sum_name
                        radio.id = res.play_sum_num
                        if (index == 0) radio.isChecked = true
                        radio.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                etBetPlayMoney.setText(buttonView.id.toString())
                                betMoney = buttonView.id
                                setTotal()
                            }
                        }
                        radioGroupLayout.addView(radio)
                        val params = radio.layoutParams as RadioGroup.LayoutParams
                        params.width = ViewUtils.dp2px(35)
                        params.height = ViewUtils.dp2px(35)
                        params.setMargins(ViewUtils.dp2px(5), 0, ViewUtils.dp2px(5), 0)
                        radio.layoutParams = params
                        betMoney = it[0].play_sum_num
                        betDiamond = betMoney * 2
                        tvBetCount.text = "共" + betCount + "注"
                        etBetPlayMoney.setText(betMoney.toString())
                        tvDiamond.text = (betMoney * betCount * 2).toString()
                    }
                }
            }
        }
    }


    override fun dismiss() {
        super.dismiss()
        handler?.removeCallbacks(runnable)
    }


    override fun onDestroy() {
        super.onDestroy()
        RxBus.get().unregister(this)
    }

    private val betList = arrayListOf<LotteryBet>()

    private var betCount = 1 //注数

    private var betDiamond = -1 //钻石数

    private var betMoney = -1 //投注金额

    @SuppressLint("SetTextI18n")
    private fun setTotal() {
        betCount = if (betList.isEmpty()) 1 else betList.size
        tvBetCount.text = "共" + betCount + "注"
        etBetPlayMoney.setText(betMoney.toString())
        tvDiamond.text = (betMoney * betCount * 2).toString()
    }

    //跳转购彩
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryBet(eventBean: LotteryBet) {
        if (eventBean.result.isSelected) {
            betList.add(eventBean)
            ViewUtils.setVisible(bottomBetLayout)
            betCount = betList.size
        } else betList.remove(eventBean)
        betCount = betList.size
        setTotal()
        if (betList.isEmpty()) ViewUtils.setGone(bottomBetLayout)
    }
}