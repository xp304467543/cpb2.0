package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.utils.ViewUtils.getColor
import com.fenghuang.baselib.utils.ViewUtils.setGone
import com.fenghuang.baselib.utils.ViewUtils.setVisible
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryChildTypeAdapter
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryComposeUtil
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryConstant
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryCodeTrendResponse
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomWheelViewDialog
import com.fenghuang.caipiaobao.widget.trendview.TrendViewHeadType
import com.fenghuang.caipiaobao.widget.trendview.TrendViewType
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.child_fragment_trend.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe 走势
 *
 */

class LiveRoomBetToolsTrendFragment2 : BaseNormalFragment() {

    private var numTenYg = "11" //10码 亚冠和走势
    private var numTenBase = "1"//10码 号码走势

    private var numFiveBase = "1"//5码基本走势
    private var numFiveForm = "7"//5码形态走势
    private var numFiveTiger = "6"//5码龙虎走势


    var limit = "10"

    var date = TimeUtils.getToday()

    private var typeSelect: String? = null

    private var bottomWheelViewDialog: BottomWheelViewDialog? = null

    private var bottomIssueWheelViewDialog: BottomWheelViewDialog? = null

    private var bottomFormWheelViewDialog: BottomWheelViewDialog? = null

    private var rvTrendSelect: RecyclerView? = null

    private var tvTrendSelectAll: TextView? = null

    private var tvTrendSelectForm: TextView? = null

    private var tvTrendSelectIssue: TextView? = null

    private var tvToDay: TextView? = null

    private var tvYesterday: TextView? = null

    private var tvBeforeYesterday: TextView? = null

    private var tvHolder: TextView? = null

    private var trendContainer:LinearLayout? = null

    private var trendViewHead: TrendViewHeadType? =null

    private var trendScrollView:ScrollView?=null

    private var trendViewContent: TrendViewType? =null

    private var linTrendLoading:LinearLayout? =null


    override fun getLayoutRes()= R.layout.child_fragment_trend

    override fun initView(rootView: View?) {
        val smartRefreshLayoutLotteryTrend = rootView?.findViewById<SmartRefreshLayout>(R.id.smartRefreshLayoutLotteryTrend)
        smartRefreshLayoutLotteryTrend?.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutLotteryTrend?.setEnableLoadMore(false)//是否启用上拉加载功能
        smartRefreshLayoutLotteryTrend?.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutLotteryTrend?.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        rvTrendSelect = rootView?.findViewById(R.id.rvTrendSelect)
        tvTrendSelectAll = rootView?.findViewById(R.id.tvTrendSelectAll)
        tvTrendSelectForm = rootView?.findViewById(R.id.tvTrendSelectForm)
        tvTrendSelectIssue = rootView?.findViewById(R.id.tvTrendSelectIssue)
        tvToDay = rootView?.findViewById(R.id.tvToDay)
        tvYesterday = rootView?.findViewById(R.id.tvYesterday)
        tvBeforeYesterday = rootView?.findViewById(R.id.tvBeforeYesterday)
        tvHolder = rootView?.findViewById(R.id.tvHolder)
        trendContainer = rootView?.findViewById(R.id.trendContainer)
        trendViewHead = rootView?.findViewById(R.id.trendViewHead)
        trendScrollView = rootView?.findViewById(R.id.trendScrollView)
        trendViewContent = rootView?.findViewById(R.id.trendViewContent)
        linTrendLoading = rootView?.findViewById(R.id.linTrendLoading)
        initEvent()
        initType()
        initTypeSelect()
        initBottomDialog()
    }


    private fun initType() {
        when (arguments?.getString("lotteryId")!!) {
            "7", "9", "11", "26", "27" -> { //10码
                this.limit = "100"
                this.typeSelect = LotteryConstant.TYPE_17
                tvTrendSelectAll?.text = "冠军"
            }
            "1", "10" -> { //5码
                this.limit = "10"
                this.typeSelect = LotteryConstant.TYPE_19
            }
        }
    }


    private fun initBottomDialog() {
        when (arguments?.getString("lotteryId")!!) {
            "7", "9", "11", "26", "27" -> {  //10码
                val list = arrayListOf("冠军", "第二名", "第三名", "第四名", "第五名", "第六名", "第七名", "第八名", "第九名", "第十名")
                bottomWheelViewDialog = BottomWheelViewDialog(context!!, list)
                bottomWheelViewDialog?.setConfirmClickListener { position, it ->
                    this.numTenBase = (position + 1).toString()
                  getTrendData(arguments?.getString("lotteryId")!!, this.numTenYg, this.limit, this.date)
                    tvTrendSelectAll?.text = it
                    bottomWheelViewDialog?.dismiss()
                }
            }
            "1", "10" -> { //5码
                val listForm = arrayListOf("前三形态", "中三形态", "后三形态")
                bottomFormWheelViewDialog = BottomWheelViewDialog(context!!, listForm)
                bottomFormWheelViewDialog?.setConfirmClickListener { position, it ->
                    this.typeSelect = it
                    when (position) {
                        0 -> {
                            this.numFiveForm = "7"
                        }
                        1 -> {
                            this.numFiveForm = "8"
                        }
                        2 -> {
                            this.numFiveForm = "9"
                        }
                    }
                  getFormData(arguments?.getString("lotteryId")!!, this.numFiveForm, this.limit, this.date)
                    tvTrendSelectForm?.text = it
                    bottomFormWheelViewDialog?.dismiss()
                }

                val list = arrayListOf("第1球", "第2球", "第3球", "第4球", "第5球")
                bottomWheelViewDialog = BottomWheelViewDialog(context!!, list)
                bottomWheelViewDialog?.setConfirmClickListener { position, it ->
                    this.numFiveBase = (position + 1).toString()
                  getTrendData(arguments?.getString("lotteryId")!!, this.numFiveBase, this.limit, this.date)
                    tvTrendSelectAll?.text = it
                    bottomWheelViewDialog?.dismiss()
                }

                val list2 = arrayListOf("近10期", "近30期", "近60期", "近90期")
                bottomIssueWheelViewDialog = BottomWheelViewDialog(context!!, list2)
                bottomIssueWheelViewDialog?.setConfirmClickListener { position, it ->
                    when (position) {
                        0 -> this.limit = "10"
                        1 -> this.limit = "30"
                        2 -> this.limit = "60"
                        3 -> this.limit = "90"
                    }
                    getTypeTrendData()
                    tvTrendSelectIssue?.text = it
                    bottomIssueWheelViewDialog?.dismiss()
                }
            }
        }
    }

    override fun initData() {
      getTrendData(arguments?.getString("lotteryId")!!, "1", this.limit, this.date)
    }

    fun initEvent(){
        tvToDay?.setOnClickListener {
            tvToDay?.setTextColor(getColor(R.color.color_FF513E))
            tvYesterday?.setTextColor(getColor(R.color.color_333333))
            tvBeforeYesterday?.setTextColor(getColor(R.color.color_333333))
            this.date = TimeUtils.getToday()
            getTypeTrendData()
        }
        tvYesterday?.setOnClickListener {
            tvToDay?.setTextColor(getColor(R.color.color_333333))
            tvYesterday?.setTextColor(getColor(R.color.color_FF513E))
            tvBeforeYesterday?.setTextColor(getColor(R.color.color_333333))
            date = TimeUtils.getYesterday()
            getTypeTrendData()
        }
        tvBeforeYesterday?.setOnClickListener {
            tvToDay?.setTextColor(getColor(R.color.color_333333))
            tvYesterday?.setTextColor(getColor(R.color.color_333333))
            tvBeforeYesterday?.setTextColor(getColor(R.color.color_FF513E))
            date = TimeUtils.getBeforeYesterday()
            getTypeTrendData()
        }

        tvTrendSelectAll?.setOnClickListener {
            if (bottomWheelViewDialog != null) {
                bottomWheelViewDialog?.show()
            }
        }

        tvTrendSelectIssue?.setOnClickListener {
            if (bottomIssueWheelViewDialog != null) {
                bottomIssueWheelViewDialog?.show()
            }
        }
        tvTrendSelectForm?.setOnClickListener {
            if (bottomFormWheelViewDialog != null) {
                bottomFormWheelViewDialog?.show()
            }
        }
    }



    //走势图 号码走势，亚冠和走势筛选
    private fun initTypeSelect() {
        var data: Array<String>? = null
        val value = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val lotteryTypeAdapter = context?.let { LotteryChildTypeAdapter(it) }
        val lotteryID = arguments?.getString("lotteryId")!!
        if (lotteryID == "7" || lotteryID == "9" || lotteryID == "11" || lotteryID == "26" || lotteryID == "27") {
            data = arrayOf(LotteryConstant.TYPE_17, LotteryConstant.TYPE_18)
            setGone(tvTrendSelectIssue)
            tvTrendSelectAll?.text = "冠军"
        } else if (lotteryID != "8") {
            data = arrayOf(LotteryConstant.TYPE_19, LotteryConstant.TYPE_20, LotteryConstant.TYPE_21)
            setVisible(tvTrendSelectIssue)
            tvTrendSelectAll?.text = "第一球"
        }
        rvTrendSelect?.layoutManager = value
        rvTrendSelect?.adapter = lotteryTypeAdapter
        if (data != null && data.isNotEmpty()) lotteryTypeAdapter?.addAll(data)
        lotteryTypeAdapter?.setOnItemClickListener { type, position ->
            if (!ViewUtils.isFastClick()) {
                lotteryTypeAdapter.changeBackground(position)
                setVisible(linTrendLoading)
                this.typeSelect = type
                when (type) {
                    LotteryConstant.TYPE_17 -> {
                        setVisible(tvTrendSelectAll)
                      getTrendData(arguments?.getString("lotteryId")!!, this.numTenBase, this.limit, this.date)
                    }
                    LotteryConstant.TYPE_18 -> {
                        setGone(tvTrendSelectAll)
                      getTrendData(arguments?.getString("lotteryId")!!, this.numTenYg, this.limit, this.date)
                    }
                    LotteryConstant.TYPE_19 -> {
                        setVisible(tvTrendSelectAll)
                        setGone(tvTrendSelectForm)
                      getTrendData(arguments?.getString("lotteryId")!!, this.numFiveBase, this.limit, this.date)
                    }
                    LotteryConstant.TYPE_20 -> {
                        setVisible(tvTrendSelectForm)
                        setGone(tvTrendSelectAll)
                      getFormData(arguments?.getString("lotteryId")!!, this.numFiveForm, this.limit, this.date)
                    }
                    LotteryConstant.TYPE_21 -> {
                        setGone(tvTrendSelectAll)
                        setGone(tvTrendSelectForm)
                      getTrendData(arguments?.getString("lotteryId")!!, this.numFiveTiger, this.limit, this.date)
                    }
                }
                trendScrollView?.scrollTo(0, 0)
            }
        }
    }

    // 获取各种走势数据数据
    private fun getTypeTrendData() {
        when (typeSelect) {
            LotteryConstant.TYPE_20, "前三形态", "中三形态", "后三形态" ->
              getFormData(arguments?.getString("lotteryId")!!, this.numFiveForm, this.limit, this.date)
            LotteryConstant.TYPE_17 ->
              getTrendData(arguments?.getString("lotteryId")!!, this.numTenBase, this.limit, this.date)
            LotteryConstant.TYPE_18 ->
              getTrendData(arguments?.getString("lotteryId")!!, this.numTenYg, this.limit, this.date)
            LotteryConstant.TYPE_19 ->
              getTrendData(arguments?.getString("lotteryId")!!, this.numFiveBase, this.limit, this.date)
            LotteryConstant.TYPE_21 ->
              getTrendData(arguments?.getString("lotteryId")!!, this.numFiveTiger, this.limit, this.date)
        }

    }


    // 走势图 只需要获取一次数据
    private fun initLineChart(data: List<LotteryCodeTrendResponse>?, dataFrom: List<LotteryCodeTrendResponse>?) {
        if (typeSelect != null) {
            LotteryComposeUtil.getNumTrendMap(this.typeSelect!!, data, dataFrom, trendViewHead, trendViewContent, trendContainer, linTrendLoading)
        }
    }



    private fun getTrendData(lotteryId: String, num: String = "1", limit: String = "10", date: String = TimeUtils.getToday()) {
        LotteryApi.getLotteryTrend(lotteryId, num, limit, date) {
            if (isAdded) {
                onSuccess {
                    if (!it.isNullOrEmpty()){
                        setGone(linTrendLoading)
                        setGone(tvHolder)
                        initLineChart(it, null)
                    } else {
                        setGone(linTrendLoading)
                        setVisible(tvHolder)
                    }
                }
                onFailed {
                    ToastUtils.show(it.getMsg().toString())
                    setGone(linTrendLoading)
                    setVisible(tvHolder)
                }
            }

        }
    }

    //形态走势  形态
    private fun getFormData(lotteryId: String, num: String = "7", limit: String = "10", date: String = TimeUtils.getToday()) {
        LotteryApi.getLotteryTrend(lotteryId, num, limit, date) {
            if (isAdded) {
                onSuccess {
                    if (!it.isNullOrEmpty()) {
                        when (num) {
                            "7" -> getComposeData(lotteryId, num = "10", limit = limit, date = date, result = it)
                            "8" -> getComposeData(lotteryId, num = "11", limit = limit, date = date, result = it)
                            "9" -> getComposeData(lotteryId, num = "12", limit = limit, date = date, result = it)
                        }

                    }
                }
                onFailed {
                    ToastUtils.show(it.getMsg().toString())
                    setGone(linTrendLoading)
                }
            }

        }
    }

    //形态走势  组合类型
    private fun getComposeData(lotteryId: String, num: String, limit: String, date: String, result: List<LotteryCodeTrendResponse>) {
        LotteryApi.getLotteryTrend(lotteryId, num, limit, date) {
            onSuccess {
                if (!it.isNullOrEmpty()) initLineChart(result, it)
            }
            onFailed {
                ToastUtils.show(it.getMsg().toString())
                setGone(linTrendLoading)
            }
        }
    }


    companion object {
        fun newInstance(lotteryID: String?): LiveRoomBetToolsTrendFragment2 {
            val fragment = LiveRoomBetToolsTrendFragment2()
            val bundle = Bundle()
            bundle.putString("lotteryId", lotteryID)
            fragment.arguments = bundle
            return fragment
        }
    }
}