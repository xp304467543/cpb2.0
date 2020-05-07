package com.fenghuang.caipiaobao.ui.lottery.children

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryChildTypeAdapter
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryComposeUtil
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryConstant
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryCodeTrendResponse
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomWheelViewDialog
import kotlinx.android.synthetic.main.child_fragment_trend.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/2- 15:36
 * @ Describe 走势
 *
 */

class LotteryTrendFragment : BaseMvpFragment<LotteryTrendPresenter>() {


    var numTenYg = "11" //10码 亚冠和走势
    var numTenBase = "1"//10码 号码走势

    var numFiveBase = "1"//5码基本走势
    var numFiveForm = "7"//5码形态走势
    var numFiveTiger = "6"//5码龙虎走势


    var limit = "10"

    var date = TimeUtils.getToday()

    private var typeSelect: String? = null

    private var bottomWheelViewDialog: BottomWheelViewDialog? = null

    private var bottomIssueWheelViewDialog: BottomWheelViewDialog? = null

    private var bottomFormWheelViewDialog: BottomWheelViewDialog? = null

    override fun getContentResID() = R.layout.child_fragment_trend

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LotteryTrendPresenter()

    override fun isOverridePage() = false

    override fun isShowToolBar() = false

    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }


    override fun initContentView() {
        smartRefreshLayoutLotteryTrend.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutLotteryTrend.setEnableLoadMore(false)//是否启用上拉加载功能
        smartRefreshLayoutLotteryTrend.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutLotteryTrend.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        initType()
        initTypeSelect()
        initBottomDialog()
    }

    private fun initType() {
        when (arguments?.getString("lotteryId")!!) {
            "7", "9", "11", "26", "27" -> { //10码
                this.limit = "100"
                this.typeSelect = LotteryConstant.TYPE_17
                tvTrendSelectAll.text = "冠军"
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
                    mPresenter.getTrendData(arguments?.getString("lotteryId")!!, this.numTenYg, this.limit, this.date)
                    tvTrendSelectAll.text = it
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
                    mPresenter.getFormData(arguments?.getString("lotteryId")!!, this.numFiveForm, this.limit, this.date)
                    tvTrendSelectForm.text = it
                    bottomFormWheelViewDialog?.dismiss()
                }

                val list = arrayListOf("第1球", "第2球", "第3球", "第4球", "第5球")
                bottomWheelViewDialog = BottomWheelViewDialog(context!!, list)
                bottomWheelViewDialog?.setConfirmClickListener { position, it ->
                    this.numFiveBase = (position + 1).toString()
                    mPresenter.getTrendData(arguments?.getString("lotteryId")!!, this.numFiveBase, this.limit, this.date)
                    tvTrendSelectAll.text = it
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
                    tvTrendSelectIssue.text = it
                    bottomIssueWheelViewDialog?.dismiss()
                }
            }
        }
    }

    override fun initData() {
        mPresenter.getTrendData(arguments?.getString("lotteryId")!!, "1", this.limit, this.date)
    }

    override fun initEvent() {
        tvToDay.setOnClickListener {
            tvToDay.setTextColor(getColor(R.color.color_FF513E))
            tvYesterday.setTextColor(getColor(R.color.color_333333))
            tvBeforeYesterday.setTextColor(getColor(R.color.color_333333))
            this.date = TimeUtils.getToday()
            getTypeTrendData()
        }
        tvYesterday.setOnClickListener {
            tvToDay.setTextColor(getColor(R.color.color_333333))
            tvYesterday.setTextColor(getColor(R.color.color_FF513E))
            tvBeforeYesterday.setTextColor(getColor(R.color.color_333333))
            date = TimeUtils.getYesterday()
            getTypeTrendData()
        }
        tvBeforeYesterday.setOnClickListener {
            tvToDay.setTextColor(getColor(R.color.color_333333))
            tvYesterday.setTextColor(getColor(R.color.color_333333))
            tvBeforeYesterday.setTextColor(getColor(R.color.color_FF513E))
            date = TimeUtils.getBeforeYesterday()
            getTypeTrendData()
        }

        tvTrendSelectAll.setOnClickListener {
            if (bottomWheelViewDialog != null) {
                bottomWheelViewDialog?.show()
            }
        }

        tvTrendSelectIssue.setOnClickListener {
            if (bottomIssueWheelViewDialog != null) {
                bottomIssueWheelViewDialog?.show()
            }
        }
        tvTrendSelectForm.setOnClickListener {
            if (bottomFormWheelViewDialog != null) {
                bottomFormWheelViewDialog?.show()
            }
        }

    }

    //走势图 号码走势，亚冠和走势筛选
    private fun initTypeSelect() {
        var data: Array<String>? = null
        val value = LinearLayoutManager(getPageActivity(), LinearLayoutManager.HORIZONTAL, false)
        val lotteryTypeAdapter = LotteryChildTypeAdapter(getPageActivity())
        val lotteryID = arguments?.getString("lotteryId")!!
        if (lotteryID == "7" || lotteryID == "9" || lotteryID == "11" || lotteryID == "26" || lotteryID == "27") {
            data = arrayOf(LotteryConstant.TYPE_17, LotteryConstant.TYPE_18)
            setGone(tvTrendSelectIssue)
            tvTrendSelectAll.text = "冠军"
        } else if (lotteryID != "8") {
            data = arrayOf(LotteryConstant.TYPE_19, LotteryConstant.TYPE_20, LotteryConstant.TYPE_21)
            setVisible(tvTrendSelectIssue)
            tvTrendSelectAll.text = "第一球"
        }
        rvTrendSelect.layoutManager = value
        rvTrendSelect.adapter = lotteryTypeAdapter
        if (data != null && data.isNotEmpty()) lotteryTypeAdapter.addAll(data)
        lotteryTypeAdapter.setOnItemClickListener { type, position ->
            if (!ViewUtils.isFastClick()) {
                lotteryTypeAdapter.changeBackground(position)
                setVisible(linTrendLoading)
                this.typeSelect = type
                when (type) {
                    LotteryConstant.TYPE_17 -> {
                        setVisible(tvTrendSelectAll)
                        mPresenter.getTrendData(arguments?.getString("lotteryId")!!, this.numTenBase, this.limit, this.date)
                    }
                    LotteryConstant.TYPE_18 -> {
                        setGone(tvTrendSelectAll)
                        mPresenter.getTrendData(arguments?.getString("lotteryId")!!, this.numTenYg, this.limit, this.date)
                    }
                    LotteryConstant.TYPE_19 -> {
                        setVisible(tvTrendSelectAll)
                        setGone(tvTrendSelectForm)
                        mPresenter.getTrendData(arguments?.getString("lotteryId")!!, this.numFiveBase, this.limit, this.date)
                    }
                    LotteryConstant.TYPE_20 -> {
                        setVisible(tvTrendSelectForm)
                        setGone(tvTrendSelectAll)
                        mPresenter.getFormData(arguments?.getString("lotteryId")!!, this.numFiveForm, this.limit, this.date)
                    }
                    LotteryConstant.TYPE_21 -> {
                        setGone(tvTrendSelectAll)
                        setGone(tvTrendSelectForm)
                        mPresenter.getTrendData(arguments?.getString("lotteryId")!!, this.numFiveTiger, this.limit, this.date)
                    }
                }
                trendScrollView.scrollTo(0, 0)
            }
        }
    }

    // 获取各种走势数据数据
    fun getTypeTrendData() {
        when (typeSelect) {
            LotteryConstant.TYPE_20, "前三形态", "中三形态", "后三形态" ->
                mPresenter.getFormData(arguments?.getString("lotteryId")!!, this.numFiveForm, this.limit, this.date)
            LotteryConstant.TYPE_17 ->
                mPresenter.getTrendData(arguments?.getString("lotteryId")!!, this.numTenBase, this.limit, this.date)
            LotteryConstant.TYPE_18 ->
                mPresenter.getTrendData(arguments?.getString("lotteryId")!!, this.numTenYg, this.limit, this.date)
            LotteryConstant.TYPE_19 ->
                mPresenter.getTrendData(arguments?.getString("lotteryId")!!, this.numFiveBase, this.limit, this.date)
            LotteryConstant.TYPE_21 ->
                mPresenter.getTrendData(arguments?.getString("lotteryId")!!, this.numFiveTiger, this.limit, this.date)
        }

    }


    // 走势图 只需要获取一次数据
    fun initLineChart(data: List<LotteryCodeTrendResponse>?, dataFrom: List<LotteryCodeTrendResponse>?) {
        if (typeSelect != null) {
            LotteryComposeUtil.getNumTrendMap(this.typeSelect!!, data, dataFrom, trendViewHead, trendViewContent, trendContainer, linTrendLoading)
            hidePageLoadingDialog()
        }
    }


    companion object {
        fun newInstance(anchorId: String): LotteryTrendFragment {
            val fragment = LotteryTrendFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", anchorId)
            fragment.arguments = bundle
            return fragment
        }
    }
}