package com.fenghuang.caipiaobao.ui.lottery

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryTypeAdapter
import com.fenghuang.caipiaobao.ui.lottery.children.LotteryBaseFragment
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryJumpToLive
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryTypeResponse
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryTypeSelect
import com.fenghuang.caipiaobao.ui.mine.data.ChangeSkin
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_lottery.*
import kotlinx.android.synthetic.main.my_top_bar.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/1- 12:50
 * @ Describe
 *
 */
class LotteryFragment : BaseMvpFragment<LotteryPresenter>() {

    var lotteryId = ""


    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LotteryPresenter()

    override fun isRegisterRxBus() = true

    override fun isShowBackIcon() = false


    override fun getLayoutResID() = R.layout.fragment_lottery

    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }

    override fun initContentView() {
        tvTitle.text =  "开奖"
        setStatusBarHeight(stateViewLottery)
        initBaseView()
        //皮肤
        if (UserInfoSp.getSkinSelect()!=1){
            skin2()
        }
    }

    private fun initBaseView() {
        initLotteryType()
        tvAtNext.text = "加载中..."
        tvOpenCount.text = "加载中..."
        //加载底部
        loadRootFragment(R.id.childContainer, LotteryBaseFragment.newInstance("-999", "-999"))
        mPresenter.getLotteryType()
    }

    override fun initEvent() {
        imgVideo.setOnClickListener {
            if (FastClickUtils.isFastClick()){
                if (lotteryId!="") RxBus.get().post(LotteryJumpToLive(lotteryId))
            }

        }
    }

    // ===== 彩种类型 =====
    var lotteryTypeAdapter: LotteryTypeAdapter? = null

    private fun initLotteryType() {
        val it: List<LotteryTypeResponse>
        it = ArrayList()
        for (index in 1..6) {
            it.add(LotteryTypeResponse(cname = "加载中.."))
        }
        lotteryTypeAdapter = LotteryTypeAdapter(getPageActivity())
        val value = LinearLayoutManager(getPageActivity(), LinearLayoutManager.HORIZONTAL, false)
        rvLotteryType.layoutManager = value
        rvLotteryType.adapter = lotteryTypeAdapter
        lotteryTypeAdapter!!.addAll(it)
        lotteryTypeAdapter!!.setOnItemClickListener { data, position ->
            lotteryId = data.lottery_id
            lotteryTypeAdapter!!.changeBackground(position)
            if (!data.cname.contains("加载中")) setPageTitle(data.cname)
            setVisible(tvOpenCodePlaceHolder)
            mPresenter.getLotteryOpenCode(data.lottery_id)
            //加载底部
            RxBus.get().post(LotteryTypeSelect(data.lottery_id))
        }
    }

    // ===== 倒计时 =====
    var timer: CountDownTimer? = null

    fun countDownTime(millisUntilFinished: String, lotteryId: String) {
        tvAtNext.text = getString(R.string.lottery_next)
        if (timer != null) timer?.cancel()
        val timeCountDown = millisUntilFinished.toLong() * 1000
        timer = object : CountDownTimer(timeCountDown, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val day: Long = millisUntilFinished / (1000 * 60 * 60 * 24)/*单位 天*/
                val hour: Long = (millisUntilFinished - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)/*单位 时*/
                val minute: Long = (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60)/*单位 分*/
                val second: Long = (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000 /*单位 秒*/
                if (tvOpenTime != null) {
                    when {
                        day > 0 -> tvOpenTime.text = dataLong(day) + "天" + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        hour > 0 -> tvOpenTime.text = dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        else -> tvOpenTime.text = dataLong(minute) + ":" + dataLong(second)
                    }
                }
            }

            override fun onFinish() {
                if (isSupportVisible) {
                    tvOpenTime!!.text = "开奖中..."
                    setVisible(tvOpenCodePlaceHolder)
                }
                mPresenter.getLotteryOpenCode(lotteryId)
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


    //换肤
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        when (eventBean.id) {
            1 -> {
                skin()
            }
            2 -> {
                skin2()
            }
        }
    }

    private fun skin(){
        topContainer.background = getDrawable(R.color.white)
        tvTitle.setTextColor(getColor(R.color.black))
    }

    private fun skin2(){
        topContainer.background = getDrawable(R.mipmap.ic_skin_new_year_top)
        tvTitle.setTextColor(getColor(R.color.white))
    }
}