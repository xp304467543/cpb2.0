package com.fenghuang.caipiaobao.ui.mine.children.report

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpActivity
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.mine.data.MineGameReportInfo
import com.fenghuang.caipiaobao.widget.dialog.DataPickDoubleDialog
import kotlinx.android.synthetic.main.act_mine_game_report_more.*
import kotlinx.android.synthetic.main.my_top_bar.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/2
 * @ Describe
 *
 */
class MineGameReportMoreAct : BaseMvpActivity<MineGameReportMorePresenter>() {

    var index = "0"

    var start = TimeUtils.getToday()

    var end = TimeUtils.getToday()

    var lotteryAdapter: ReportAdapter? = null

    var dataDialog: DataPickDoubleDialog? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun getPageTitle() = "彩票报表"

    override fun attachPresenter() = MineGameReportMorePresenter()

    override fun isOverride() = false

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_mine_game_report_more

    override fun initContentView() {
        setVisible(ivTitleRight)
        ivTitleRight.setBackgroundResource(R.mipmap.ic_date)
        lotteryAdapter = ReportAdapter()
        rv_game.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_game.adapter = lotteryAdapter
    }

    override fun initData() {
        mPresenter.getInfo(index, start, end)
    }

    override fun initEvent() {
        tv_start.setOnClickListener {
            index = "0"
            tv_start.delegate.backgroundColor = ViewUtils.getColor(R.color.color_FF513E)
            tv_start.setTextColor(ViewUtils.getColor(R.color.white))
            tv_end.delegate.backgroundColor = ViewUtils.getColor(R.color.white)
            tv_end.setTextColor(ViewUtils.getColor(R.color.color_999999))
            mPresenter.getInfo(index, start, end)
        }
        tv_end.setOnClickListener {
            index = "1"
            tv_end.delegate.backgroundColor = ViewUtils.getColor(R.color.color_FF513E)
            tv_end.setTextColor(ViewUtils.getColor(R.color.white))
            tv_start.delegate.backgroundColor = ViewUtils.getColor(R.color.white)
            tv_start.setTextColor(ViewUtils.getColor(R.color.color_999999))
            mPresenter.getInfo(index, start, end)
        }
        tv_data_1.setOnClickListener {
            tv_data_1.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            tv_data_2.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv_data_3.setTextColor(ViewUtils.getColor(R.color.color_333333))
            start = TimeUtils.getToday()
            mPresenter.getInfo(index, start, end)
        }
        tv_data_2.setOnClickListener {
            tv_data_2.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            tv_data_1.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv_data_3.setTextColor(ViewUtils.getColor(R.color.color_333333))
            start = TimeUtils.get7before()
            mPresenter.getInfo(index, start, end)
        }
        tv_data_3.setOnClickListener {
            tv_data_3.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            tv_data_2.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tv_data_1.setTextColor(ViewUtils.getColor(R.color.color_333333))
            start = TimeUtils.get3MonthBefore()
            mPresenter.getInfo(index, start, end)
        }
        ivTitleRight.setOnClickListener {
            if (dataDialog == null) {
                dataDialog = DataPickDoubleDialog(this, R.style.dialog)
                dataDialog?.setConfirmClickListener { data1, data2 ->
                    mPresenter.getInfo(index, data1, data2)
                    tv_data_3.setTextColor(ViewUtils.getColor(R.color.color_333333))
                    tv_data_2.setTextColor(ViewUtils.getColor(R.color.color_333333))
                    tv_data_1.setTextColor(ViewUtils.getColor(R.color.color_333333))
                    dataDialog?.dismiss()
                }
            } else dataDialog?.show()
        }
    }


    inner class ReportAdapter : BaseRecyclerAdapter<MineGameReportInfo>(this) {

        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineGameReportInfo> {
            return ReportHolder(parent)
        }

        inner class ReportHolder(parent: ViewGroup) : BaseViewHolder<MineGameReportInfo>(getContext(), parent, R.layout.holder_report_lottery) {
            override fun onBindData(data: MineGameReportInfo) {
                setText(R.id.tv_lottery_name, data.lottery_name)
                setText(R.id.tv_lottery_1, data.count)
                setText(R.id.tv_lottery_2, data.amount)
                setText(R.id.tv_lottery_3, data.prize)
                if (index == "0") {
                    setText(R.id.tv_t_1, "钻石注单")
                    setText(R.id.tv_t_2, "下单钻石")
                    setText(R.id.tv_t_3, "中奖钻石")
                } else {
                    setText(R.id.tv_t_1, "下单注量")
                    setText(R.id.tv_t_2, "下单金额")
                    setText(R.id.tv_t_3, "中奖金额")
                }
                ImageManager.loadImg(data.lottery_icon, findView(R.id.imgLottery))

                setOnClick(R.id.tvLookMore)
            }

            override fun onClick(id: Int) {
                if (id == R.id.tvLookMore) {
                    val intent = Intent(this@MineGameReportMoreAct, MineGameReportMoreInfoAct::class.java)
                    intent.putExtra("rLotteryId", getData()?.lottery_id)
                    intent.putExtra("is_bl_play", index)
                    intent.putExtra("startTime", start)
                    intent.putExtra("endTime", end)
                    startActivity(intent)
                }
            }
        }
    }
}