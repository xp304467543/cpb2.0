package com.fenghuang.caipiaobao.ui.mine.children.report

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpActivity
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryBetHistoryResponse
import com.fenghuang.caipiaobao.widget.dialog.DataPickDoubleDialog
import kotlinx.android.synthetic.main.act_mine_game_report_more_info.*
import kotlinx.android.synthetic.main.my_top_bar.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/3
 * @ Describe
 *
 */
class MineGameReportMoreInfoAct : BaseMvpActivity<MineGameReportMoreInfoPresenter>() {


    var dataDialog: DataPickDoubleDialog? = null


    var currentSel = "0" //默认钻石

    var index = 1

    var pos = 2

    var state = 0

    var lotteryId = ""

    var st = ""
    var et = ""

    var adapter: LiveRoomRecordAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun getPageTitle() = "注单详情"

    override fun attachPresenter() = MineGameReportMoreInfoPresenter()

    override fun isOverride() = false

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_mine_game_report_more_info


    override fun initContentView() {
        setVisible(ivTitleRight)
        ivTitleRight.setBackgroundResource(R.mipmap.ic_date)
        adapter = LiveRoomRecordAdapter()
        rvGameReportInfo.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvGameReportInfo.adapter = adapter
        lotteryId = intent.getStringExtra("rLotteryId") ?: "0"
        currentSel = intent.getStringExtra("is_bl_play") ?: "0"
    }

    override fun initData() {
        mPresenter.getResponse(state, lotteryId, st, et, currentSel)
        smBetRecord_1?.setOnRefreshListener {
            index = 1
            adapter?.clear()
            mPresenter.getResponse(state, lotteryId, st, et, currentSel)
        }
        smBetRecord_1?.setOnLoadMoreListener {
            index++
            mPresenter.getResponse(state, lotteryId, st, et, currentSel)
        }
    }

    override fun initEvent() {
        tv_all.setOnClickListener {
            tv_all.setBackgroundResource(R.drawable.button_background)
            tv_01.setBackgroundResource(R.drawable.button_grey_background)
            tv_02.setBackgroundResource(R.drawable.button_grey_background)
            tv_all.setTextColor(ViewUtils.getColor(R.color.white))
            tv_01.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv_02.setTextColor(ViewUtils.getColor(R.color.color_333333))
            state = 0
            pos = 2
            index = 1
            adapter?.clear()
            mPresenter.getResponse(state, lotteryId, st, et, currentSel)
        }
        tv_01.setOnClickListener {
            tv_01.setBackgroundResource(R.drawable.button_background)
            tv_all.setBackgroundResource(R.drawable.button_grey_background)
            tv_02.setBackgroundResource(R.drawable.button_grey_background)
            tv_01.setTextColor(ViewUtils.getColor(R.color.white))
            tv_all.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv_02.setTextColor(ViewUtils.getColor(R.color.color_333333))
            state = 1
            pos = 1
            index = 1
            adapter?.clear()
            mPresenter.getResponse(state, lotteryId, st, et, currentSel)
        }
        tv_02.setOnClickListener {
            tv_02.setBackgroundResource(R.drawable.button_background)
            tv_01.setBackgroundResource(R.drawable.button_grey_background)
            tv_all.setBackgroundResource(R.drawable.button_grey_background)
            tv_02.setTextColor(ViewUtils.getColor(R.color.white))
            tv_01.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv_all.setTextColor(ViewUtils.getColor(R.color.color_333333))
            state = 2
            pos = 0
            index = 1
            adapter?.clear()
            mPresenter.getResponse(state, lotteryId, st, et, currentSel)
        }
        ivTitleRight.setOnClickListener {
            if (dataDialog == null) {
                dataDialog = DataPickDoubleDialog(this, R.style.dialog)
                dataDialog?.setConfirmClickListener { data1, data2 ->
                    index = 1
                    st = data1
                    et = data2
                    adapter?.clear()
                    mPresenter.getResponse(state, lotteryId, data1, data2, currentSel)
                    dataDialog?.dismiss()
                }
            } else dataDialog?.show()
        }
    }


    inner class LiveRoomRecordAdapter : BaseRecyclerAdapter<LotteryBetHistoryResponse>(this) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<LotteryBetHistoryResponse> {
            return LiveRoomRecordHolder(parent)
        }

        inner class LiveRoomRecordHolder(parent: ViewGroup) : BaseViewHolder<LotteryBetHistoryResponse>(getContext(), parent, R.layout.holder_bet_history) {
            override fun onBindData(data: LotteryBetHistoryResponse) {
                setText(R.id.tvBetTime, TimeUtils.longToDateString(data.play_bet_time ?: 0))
                setText(R.id.tvBetName, data.play_bet_lottery_name)
                setText(R.id.tvBetIssue, data.play_bet_issue + " 期")
                setText(R.id.tvBetCodeName, data.play_sec_name)
                setText(R.id.tvBetCode, data.play_class_name)
                setText(R.id.tvBetOdds, data.play_odds)
                when (pos) {
                    1 -> {
                        setText(R.id.tvBetMoney, data.play_bet_sum)
                        setTextColor(R.id.tvBetMoney, ViewUtils.getColor(R.color.color_333333))
                    }
                    0 -> {
                        setText(R.id.tvBetMoney, data.play_bet_score)
                        if (data.play_bet_score?.contains("+")!!) {
                            setTextColor(R.id.tvBetMoney, ViewUtils.getColor(R.color.color_FF513E))
                        } else setTextColor(R.id.tvBetMoney, ViewUtils.getColor(R.color.color_333333))
                    }
                    2 -> {
                        if (data.play_bet_score == "0") {
                            setText(R.id.tvBetMoney, data.play_bet_sum)
                            setTextColor(R.id.tvBetMoney, ViewUtils.getColor(R.color.color_333333))
                        } else {
                            setText(R.id.tvBetMoney, data.play_bet_score)
                            if (data.play_bet_score?.contains("+")!!) {
                                setTextColor(R.id.tvBetMoney, ViewUtils.getColor(R.color.color_FF513E))
                            } else setTextColor(R.id.tvBetMoney, ViewUtils.getColor(R.color.color_333333))
                        }
                    }
                    else -> {
                        setText(R.id.tvBetMoney, data.play_bet_sum)
                        setTextColor(R.id.tvBetMoney, ViewUtils.getColor(R.color.color_333333))
                    }
                }
            }
        }
    }
}