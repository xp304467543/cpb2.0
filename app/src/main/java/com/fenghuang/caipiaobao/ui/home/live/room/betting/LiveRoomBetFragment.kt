package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.IntentConstant
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter.LiveBetStateAdapter
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryTypeSelectUtil
import com.fenghuang.caipiaobao.ui.lottery.data.*
import com.fenghuang.caipiaobao.ui.mine.MinePresenter
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.SoundPoolHelper
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

    private var opt1SelectedPosition = 0


    private var userDiamond = "-1"

    private var vpGuss: ViewPager? = null

    private var tabGuss: SlidingTabLayout? = null

    private var currentLotteryId = "1"

    private var nextIssue = ""

    private var isOpenCode = false


    private var selectMoneyList: List<PlayMoneyData>? = null

    private var resultList: ArrayList<LotteryTypeResponse>? = null

    private var liveRoomBetToolsFragment: LiveRoomBetToolsFragment? = null

    private var liveRoomBetRecordFragment: LiveRoomBetRecordFragment? = null

    private var liveRoomBetAccessFragment: LiveRoomBetAccessFragment? = null

    private var viewPagerAdapter: LiveBetStateAdapter? = null

    private var playList = ArrayList<LotteryPlayListResponse>()

    override val layoutResId: Int = R.layout.fragment_live_bet

    override val resetHeight: Int = 0

    private var currentIndex = 0

    private var ivPlaySound: ImageView? = null

    override fun isShowTop(): Boolean = true

    override fun canceledOnTouchOutside(): Boolean = true

    override fun initView() {
        RxBus.get().register(this)

    }

    override fun initData() {
        val id = arguments?.getString(IntentConstant.LIVE_ROOM_LOTTERY_ID) ?: "1"
        currentLotteryId = id
        val type = LotteryApi.getLotteryBetType()
        type.onSuccess {
            val title = arrayListOf<String>()
            resultList = arrayListOf()
            for ((index, data) in it.withIndex()) {
                resultList?.add(data)
                title.add(data.cname)
                if (id == data.lottery_id) {
                    currentIndex = index
                }
            }
            ImageManager.loadImg(it[currentIndex].logo_url, tvRedBall)
            tvLotterySelectType?.text = it[currentIndex].cname
            if (!title.isNullOrEmpty()) initDialog(title, resultList!!)
        }
        getLotteryNewCode(if (id == "") "1" else id)//默认加载重庆时时彩  1
        setTabLayout(if (id == "") "1" else id)
        getUserDiamond()
        getPlayMoney()
    }

    override fun initFragment() {
        ivPlaySound = rootView?.findViewById(R.id.imgPlaySound)
        if (UserInfoSp.getIsPlaySound()) {
            ivPlaySound?.setImageResource(R.mipmap.lb_hong)
        } else ivPlaySound?.setImageResource(R.mipmap.lb_hui)
        ivPlaySound?.setOnClickListener {
            if (UserInfoSp.getIsPlaySound()) {
                UserInfoSp.putIsPlaySound(false)
                ivPlaySound?.setImageResource(R.mipmap.lb_hui)
            } else {
                UserInfoSp.putIsPlaySound(true)
                ivPlaySound?.setImageResource(R.mipmap.lb_hong)
            }
        }
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
        val editText = rootView?.findViewById<EditText>(R.id.etBetPlayMoney)
        editText?.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(str: Editable?) {
                if (str != null && str.isNotEmpty()) {
                    if (str.toString().toLong() < 10) {
                        ToastUtils.show("请输入≥10的整数")
                    }
                    betMoney = if (str.length > 9) {
                        editText.setText(str.substring(0, 9)); //截取前x位
                        editText.requestFocus()
                        editText.setSelection(editText.text.length); //光标移动到最后
                        str.substring(0, 9).toInt()
                    } else {
                        str.toString().toInt()
                    }
                    setTotal()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        rootView?.findViewById<TextView>(R.id.tvBetSubmit)?.setOnClickListener {
            if (!isOpenCode) {
                ToastUtils.show("当前期已封盘或已开奖，请购买下一期")
                return@setOnClickListener
            }
            if (userDiamond != "-1") {
                if (currentName == "二中二" && currentNum != 2) {
                    ToastUtils.show("二中二必须选择2个号码")
                    return@setOnClickListener
                }
                if (currentName == "三中三" && currentNum != 3) {
                    ToastUtils.show("三中三必须选择3个号码")
                    return@setOnClickListener
                }
                if (!betList.isNullOrEmpty() && rootView?.findViewById<EditText>(R.id.etBetPlayMoney)?.text != null) {
                    if (rootView?.findViewById<EditText>(R.id.etBetPlayMoney)?.text.toString() != "" && rootView?.findViewById<EditText>(R.id.etBetPlayMoney)?.text.toString().toInt() >= 10) {
                        if (nextIssue != "") {
                            if (tvUserDiamond.text.toString().isNotEmpty()) {
                                //打开投注确认
                                startToBetConfirm()

                            } else ToastUtils.show("钻石信息获取失败,请重试")
                        } else ToastUtils.show("当前期已封盘或已开奖，请购买下一期")
                    } else ToastUtils.show("投注金额最小为 10")
                } else ToastUtils.show("未选择任何玩法或投注金额,请选择后再提交")


            } else getUserDiamond()
        }
    }

    //投注确认
    private fun startToBetConfirm() {
        if (FastClickUtils.isFastClick()) {
            if (tvDiamond.text.toString() != "") {
                if (tvDiamond.text.toString().toLong() > 999999999) {
                    ToastUtils.show("投注金额最大为 999999999")
                    return
                }
            }
            repeat(betList.size) {
                betList[it].result.money = etBetPlayMoney.text.toString()
            }
            liveRoomBetAccessFragment = LiveRoomBetAccessFragment.newInstance(LotteryBetAccess(betList, betCount, tvDiamond.text.toString().toInt(), currentLotteryId,
                    nextIssue, tvUserDiamond.text.toString(), tvLotterySelectType?.text.toString(), vpGuss?.currentItem?.let { it1 -> viewPagerAdapter?.getPageTitle(it1) }.toString()))
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
                viewPagerAdapter = LiveBetStateAdapter(childFragmentManager, playList)
                vpGuss?.adapter = viewPagerAdapter
                vpGuss?.offscreenPageLimit = 10
                tabGuss?.setViewPager(vpGuss)
                //可用于切换 整合 单码之类的tab时清空
                vpGuss?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {}
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageSelected(position: Int) {
                        currentName = ""
                        currentNum = 0
                        reSetData()
                    }
                })
                rootView?.findViewById<TextView>(R.id.tvReset)?.setOnClickListener {
                    ViewUtils.setGone(bottomBetLayout)
                    if (radioGroupLayout.getChildAt(0) != null)
                        (radioGroupLayout.getChildAt(0) as RadioButton).isChecked = true
                    reSetData()
                    RxBus.get().post(LotteryReset(true))

                }
            }
        }

    }

    //底部弹框
    private fun initDialog(title: ArrayList<String>, list: ArrayList<LotteryTypeResponse>) {

        tvLotterySelectType?.setOnClickListener {
            val lotterySelectDialog = BottomLotterySelectDialog(context!!, title)
            lotterySelectDialog.setCanceledOnTouchOutside(false)
            tvLotterySelectType?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.select_top, 0)
            lotterySelectDialog.setOnDismissListener {
                tvLotterySelectType?.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.select_bottom, 0)
            }
            lotterySelectDialog.tvLotteryWheelSure.setOnClickListener {
                isPlay = false
                if (runnable != null) handler?.removeCallbacks(runnable!!)
                tvLotterySelectType?.text = lotterySelectDialog.lotteryPickerView.opt1SelectedData as String
                opt1SelectedPosition = lotterySelectDialog.lotteryPickerView.opt1SelectedPosition
                currentLotteryId = list[opt1SelectedPosition].lottery_id
                getLotteryNewCode(list[opt1SelectedPosition].lottery_id)
                setTabLayout(list[opt1SelectedPosition].lottery_id)
                ImageManager.loadImg(list[opt1SelectedPosition].logo_url, tvRedBall)
                timer?.cancel()
                timerClose?.cancel()
                if (tvCloseTime != null) tvCloseTime.text = "--:--"
                if (tvOpenTime != null) tvOpenTime!!.text = "--:--"
                reSetData()
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
                    if (it.next_lottery_time.toInt() > 0 && it.next_lottery_end_time > 0) {
                        nextIssue = it.next_issue
                        tvOpenCount?.text = (it.issue + " 期开奖结果   ")
                        countDownTime(it.next_lottery_time, lottery_id)
                        //更新最新开奖数据
                        LotteryTypeSelectUtil.addOpenCode(context!!, linLotteryOpenCode, it.code?.split(","), it.lottery_id)
                        tvOpenCodePlaceHolder.visibility = View.GONE
                        countDownTimerClose(it.next_lottery_end_time)
                        isOpenCode = true
                        if (isPlay) {
                            if (UserInfoSp.getIsPlaySound()) {
                                if (handlerPlay == null) handlerPlay = Handler()
                                handlerPlay?.post {
                                    SoundPoolHelper(context).playSoundWithRedId(R.raw.ring)
                                }
                            }
                        }
                    } else {
                        if (timer != null) timer?.cancel()
                        tvOpenCodePlaceHolder.visibility = View.VISIBLE
                        tvOpenTime.text = "--:--"
                        tvOpenCount.text = ("- - - -" + "期开奖结果   ")
                        tvCloseTime.text = "--:--"
                        getNewResult(it.lottery_id)
                        nextIssue = ""
                    }
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
                isOpenCode = false
                if (tvCloseTime != null) tvCloseTime.text = "--:--"
            }
        }
        if (timerClose != null) timerClose?.start()
    }


    // ===== 开奖倒计时 =====
    var timer: CountDownTimer? = null
    var handler: Handler? = null
    var handlerPlay: Handler? = null
    var runnable: Runnable? = null
    var isPlay = false

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
                    isPlay = true
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
        handler?.postDelayed(runnable, 3000)
    }


    // 这个方法是保证时间两位数据显示，如果为1点时，就为01
    fun dataLong(c: Long): String {
        return if (c >= 10)
            c.toString()
        else "0$c"
    }

    /**
     * 获取钻石
     */
    private fun getUserDiamond() {
        try {
            val presenter = MinePresenter()
            presenter.getUserDiamond()
            presenter.getUserDiamondSuccessListener {
                if (isAdded) {
                    userDiamond = it
                    if (tvUserDiamond != null) tvUserDiamond.text = userDiamond
                }
            }
        } catch (e: Exception) {
            ToastUtils.show(e.toString())
        }

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
                    }
                }
            }
        }
    }

    //重置页面
    fun reSetData() {
        ViewUtils.setGone(bottomBetLayout)
        betCount = 1 //注数
        betDiamond = 10 //钻石数
        betMoney = 10 //投注金额
        etBetPlayMoney.setText("10")
        if (radioGroupLayout.getChildAt(0) != null) {
            try {
                (radioGroupLayout.getChildAt(0) as RadioButton).isChecked = true
            } catch (e: Exception) {
            }
        }
        setTotal()
        betList.clear()
        RxBus.get().post(LotteryReset(true))
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

    private var betDiamond = 10 //钻石数

    private var betMoney = 10 //投注金额

    @SuppressLint("SetTextI18n")
    private fun setTotal() {
        betCount = if (betList.isEmpty()) 1 else betList.size
        tvBetCount.text = "共" + betCount + "注"
        tvDiamond.text = (betMoney * betCount).toString()
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryBet(eventBean: LotteryBet) {
        if (eventBean.result.isSelected) {
            betList.add(eventBean)
            ViewUtils.setVisible(bottomBetLayout)
            betCount = betList.size
        } else {
            try {
                when (eventBean.playName) {
                    "二中二" -> {
                        for (ops in betList) {
                            if (ops.playName == "二中二") betList.remove(ops)
                        }
                    }
                    "三中三" -> {
                        for (ins in betList) {
                            if (ins.playName == "三中三") betList.remove(ins)
                        }
                    }
                    else -> betList.remove(eventBean)
                }

            } catch (e: Exception) {
            }
        }
        betCount = betList.size
        setTotal()
        if (betList.isEmpty()) ViewUtils.setGone(bottomBetLayout)
    }


    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryBet(eventBean: LotteryResetDiamond) {
        getUserDiamond()
    }

    //重置所有状态
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryBet(eventBean: LotteryReset) {
        betList.clear()
        ViewUtils.setGone(bottomBetLayout)
    }

    //二中二 三中三
    var currentName = ""
    var currentNum = 0

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryCurrent(eventBean: LotteryCurrent) {
        currentName = eventBean.name
        currentNum = eventBean.size
    }

    companion object {
        fun newInstance(lotteryId: String) = LiveRoomBetFragment().apply {
            arguments = Bundle(1).apply {
                putString(IntentConstant.LIVE_ROOM_LOTTERY_ID, lotteryId) //isFollow
            }
        }
    }
}