package com.fenghuang.caipiaobao.ui.personal

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpActivity
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserConstant
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.HomePresenter
import com.fenghuang.caipiaobao.ui.personal.data.ExpertPageHistory
import com.fenghuang.caipiaobao.ui.personal.data.ExpertPageInfo
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import kotlinx.android.synthetic.main.fragment_presonal_expert.*
import java.math.BigDecimal

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-01
 * @ Describe 专家主页
 *
 */
class ExpertPersonalPage : BaseMvpActivity<ExpertPersonalPagePresenter>() {

    private var limit = "10"

    private lateinit var adapter: ExpertHistoryAdapter

    private var lottery_id = "-1"

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ExpertPersonalPagePresenter()

    override fun isSwipeBackEnable() = true

    override fun isOverride() = true

    override val layoutResID = R.layout.fragment_presonal_expert


    override fun initContentView() {

        adapter = ExpertHistoryAdapter()
        expertHistory.adapter = adapter
        expertHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }


    override fun initData() {
        if (intent != null) {
            showPageLoadingDialog()
            lottery_id = intent.getStringExtra(UserConstant.FOLLOW_lottery_ID)?:"-1"
            if (lottery_id == "-1") mPresenter.getExpertInfo(intent.getStringExtra(UserConstant.FOLLOW_ID)!!,"") else mPresenter.getExpertInfo(intent.getStringExtra(UserConstant.FOLLOW_ID)!!,lottery_id)

        }
    }

    @SuppressLint("SetTextI18n")
    fun initExpert(data: ExpertPageInfo?) {
        if (data != null) {
            if (lottery_id == "-1") lottery_id = data.lottery_id
            mPresenter.getExpertHistory(intent.getStringExtra(UserConstant.FOLLOW_ID)!!, lottery_id, limit)
            mPresenter.getNextTime(lottery_id)
            ImageManager.loadImg(data.avatar, imgUserPhoto)
            tvUserName.text = data.nickname
            tvUserDescription.text = data.profile
            tvExpertAttention.text = data.following
            tvExpertFans.text = data.followers
            tvExpertZan.text = data.like
            tvExpertWinRate.text = BigDecimal(data.win_rate).setScale(4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal(100)).stripTrailingZeros().toPlainString() + " %"
            tvExpertWinPre.text = BigDecimal(data.profit_rate).setScale(4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal(100)).stripTrailingZeros().toPlainString() + " %"
            tvExpertWinAdd.text = data.winning
            tvLotteryName.text = data.lottery_name
            if (data.is_followed == "1") {
                btAttentionExpert.background = ViewUtils.getDrawable(R.drawable.button_grey_background)
                btAttentionExpert.setTextColor(ViewUtils.getColor(R.color.grey_e6))
                btAttentionExpert.text = "已关注"
            }
        }

    }


    override fun initEvent() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb1 -> limit = "10"
                R.id.rb2 -> limit = "30"
                R.id.rb3 -> limit = "60"
                R.id.rb4 -> limit = "100"
            }
            mPresenter.getExpertHistory(intent.getStringExtra(UserConstant.FOLLOW_ID)!!, lottery_id, limit)
        }


        btAttentionExpert.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(this, false)
                    return@setOnClickListener
                }
                val presenter = HomePresenter()
                presenter.attentionExpert(intent.getStringExtra(UserConstant.FOLLOW_ID)!!)
                presenter.setSuccessExpertClickListener {
                    if (!it) {
                        btAttentionExpert.background = ViewUtils.getDrawable(R.drawable.button_blue_background)
                        btAttentionExpert.text = "+ 关注"
                        btAttentionExpert.setTextColor(ViewUtils.getColor(R.color.white))
                    } else {
                        btAttentionExpert.background = ViewUtils.getDrawable(R.drawable.button_grey_background)
                        btAttentionExpert.setTextColor(ViewUtils.getColor(R.color.grey_e6))
                        btAttentionExpert.text = "已关注"
                    }
                    mPresenter.getExpertInfo(intent.getStringExtra(UserConstant.FOLLOW_ID)!!,lottery_id)
                }
                presenter.setFailExpertClickListener {
                    GlobalDialog.showError(this, it)
                }
            }
        }

        imgBack.setOnClickListener {
            finish()
        }
    }


    fun initExpertHistory(data: List<ExpertPageHistory>) {
        if (data.isNullOrEmpty()) tvDescription.text = "暂无历史记录！" else {
            adapter.clear()
            adapter.addAll(data)
            adapter.notifyDataSetChanged()
        }
    }

    // ===== 倒计时 =====
    var timer: CountDownTimer? = null

    fun countDownTime(millisUntilFinished: String, lotteryId: String) {
        tvOpenTimePersonal.text = getString(R.string.lottery_next)
        if (timer != null) timer?.cancel()
        val timeCountDown = millisUntilFinished.toLong() * 1000
        timer = object : CountDownTimer(timeCountDown, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val day: Long = millisUntilFinished / (1000 * 60 * 60 * 24)/*单位 天*/
                val hour: Long = (millisUntilFinished - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)/*单位 时*/
                val minute: Long = (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60)/*单位 分*/
                val second: Long = (millisUntilFinished - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000 /*单位 秒*/
                if (tvOpenTimePersonal != null) {
                    when {
                        day > 0 -> tvOpenTimePersonal.text = "距下次  " + dataLong(day) + "天" + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        hour > 0 -> tvOpenTimePersonal.text = "距下次  " + dataLong(hour) + ":" + dataLong(minute) + ":" + dataLong(second)
                        else -> tvOpenTimePersonal.text = "距下次  " + dataLong(minute) + ":" + dataLong(second)
                    }
                }
            }

            override fun onFinish() {
                tvOpenTimePersonal!!.text = "距下次  ----"
                mPresenter.getNextTime(lotteryId)
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

    inner class ExpertHistoryAdapter : BaseRecyclerAdapter<ExpertPageHistory>(this) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ExpertPageHistory> {
            return ExpertHistoryHolder(parent)
        }

        inner class ExpertHistoryHolder(parent: ViewGroup) : BaseViewHolder<ExpertPageHistory>(getContext(), parent, R.layout.holder_expert_history) {
            override fun onBindData(data: ExpertPageHistory) {
                setText(R.id.tvIssue, data.issue)
                setText(R.id.tvTime, TimeUtils.longToDateStringTime(data.created.toLong()))
                if (data.open_code != "") setText(R.id.tvOpenCode, data.open_code) else setText(R.id.tvOpenCode, "待开奖")
                setText(R.id.tvMethod, data.method)
                setText(R.id.tvCode, data.code)
                val text = when (data.is_right) {
                    "0" -> "未开奖"
                    "1" -> "无"
                    "2" -> "输"
                    "3" -> "赢"
                    else -> "无"
                }
                setText(R.id.tvResult, text)
            }
        }

    }

}