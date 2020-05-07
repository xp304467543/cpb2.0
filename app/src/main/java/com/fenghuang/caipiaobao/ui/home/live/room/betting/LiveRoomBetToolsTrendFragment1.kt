package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryChildLuZhuAdapter
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryChildTypeAdapter
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryConstant
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryCodeLuZhuResponse
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.JsonUtils
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomDialogBean
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomLotteryDialog
import com.github.ybq.android.spinkit.SpinKitView
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-16
 * @ Describe 露珠
 *
 */

class LiveRoomBetToolsTrendFragment1 : BaseNormalFragment() {

    private var bottomDialog: BottomLotteryDialog? = null

    private var rankList: ArrayList<BottomDialogBean>? = null

    private var luZhuRecycleAdapter: LotteryChildLuZhuAdapter? = null

    private var selectType = LotteryConstant.TYPE_LUZHU_2

    private var rvLuZhuTypeSelect: RecyclerView? = null

    private var rvLotteryLuZhu: RecyclerView? = null

    private var tvSelectAll: TextView? = null

    private var tvToDay: TextView? = null

    private var tvYesterday: TextView? = null

    private var tvBeforeYesterday: TextView? = null

    private var tvLuZhuPlaceHolder: SpinKitView? = null

    private var tvHolder: TextView? = null


    var time = ""//昨天 今天 明天
    override fun getLayoutRes() = R.layout.child_fragment_lu_zhu

    override fun initView(rootView: View?) {
        val smartRefreshLayoutLotteryLuZhuType = rootView?.findViewById<SmartRefreshLayout>(R.id.smartRefreshLayoutLotteryLuZhuType)
        smartRefreshLayoutLotteryLuZhuType?.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutLotteryLuZhuType?.setEnableLoadMore(false)//是否启用上拉加载功能
        smartRefreshLayoutLotteryLuZhuType?.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutLotteryLuZhuType?.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        rvLuZhuTypeSelect = rootView?.findViewById(R.id.rvLuZhuTypeSelect)
        rvLotteryLuZhu = rootView?.findViewById(R.id.rvLotteryLuZhu)
        tvSelectAll = rootView?.findViewById(R.id.tvSelectAll)
        tvToDay = rootView?.findViewById(R.id.tvToDay)
        tvYesterday = rootView?.findViewById(R.id.tvYesterday)
        tvBeforeYesterday = rootView?.findViewById(R.id.tvBeforeYesterday)
        tvLuZhuPlaceHolder = rootView?.findViewById(R.id.tvLuZhuPlaceHolder)
        tvHolder = rootView?.findViewById(R.id.tvHolder)
        initEvent()
        initLotteryTypeSelect()
    }

    override fun initData() {
       getLuZhuData(arguments?.getString("lotteryId")!!, selectType)
        luZhuRecycleAdapter = LotteryChildLuZhuAdapter(context!!, arguments?.getString("lotteryId")!!)
        rvLotteryLuZhu?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvLotteryLuZhu?.adapter = luZhuRecycleAdapter
        rvLotteryLuZhu?.setItemViewCacheSize(10)
    }

    //初始化 具体 Type 号码 大小 单双 等切换
    private fun initLotteryTypeSelect() {
        val data: Array<String>?
        val value = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val lotteryTypeAdapter = context?.let { LotteryChildTypeAdapter(it) }
        val lotteryID = arguments?.getString("lotteryId")!!
        if (lotteryID == "7" || lotteryID == "9" || lotteryID == "11" || lotteryID == "26" || lotteryID == "27") {
            data = arrayOf(LotteryConstant.TYPE_2, LotteryConstant.TYPE_3, LotteryConstant.TYPE_8, LotteryConstant.TYPE_9, LotteryConstant.TYPE_10)
            rankList = arrayListOf(
                    BottomDialogBean("冠军"), BottomDialogBean("亚军"), BottomDialogBean("第三名"),
                    BottomDialogBean("第四名"), BottomDialogBean("第五名"), BottomDialogBean("第六名"),
                    BottomDialogBean("第七名"), BottomDialogBean("第八名"), BottomDialogBean("第九名"),
                    BottomDialogBean("第十名"))
        } else if (lotteryID == "8") {
            data = arrayOf(LotteryConstant.TYPE_2, LotteryConstant.TYPE_3, LotteryConstant.TYPE_12, LotteryConstant.TYPE_5, LotteryConstant.TYPE_15, LotteryConstant.TYPE_16)
            rankList = arrayListOf(BottomDialogBean("正码一"), BottomDialogBean("正码二"), BottomDialogBean("正码三"),
                    BottomDialogBean("正码四"), BottomDialogBean("正码五"), BottomDialogBean("正码六"),
                    BottomDialogBean("特码"))
            tvSelectAll?.text = "筛选号码"
            ViewUtils.setGone(tvToDay)
            ViewUtils.setGone(tvYesterday)
            ViewUtils.setGone(tvBeforeYesterday)
        } else {
            data = arrayOf(LotteryConstant.TYPE_2, LotteryConstant.TYPE_3, LotteryConstant.TYPE_8, LotteryConstant.TYPE_11, LotteryConstant.TYPE_5)
            rankList = arrayListOf(BottomDialogBean("第一球"), BottomDialogBean("第二球"), BottomDialogBean("第三球"),
                    BottomDialogBean("第四球"), BottomDialogBean("第五球"))
            tvSelectAll?.text = "筛选号码"
        }
        rvLuZhuTypeSelect?.layoutManager = value
        rvLuZhuTypeSelect?.adapter = lotteryTypeAdapter
        if (data.isNotEmpty()) lotteryTypeAdapter?.addAll(data)
        lotteryTypeAdapter?.setOnItemClickListener { type, position ->
            if (FastClickUtils.isFastClick()) {
                lotteryTypeAdapter.changeBackground(position)
                selectType = getType(type)
                luZhuRecycleAdapter!!.clearList()
                if (time == "") getLuZhuData(arguments?.getString("lotteryId")!!, getType(type))
                else getLuZhuData(arguments?.getString("lotteryId")!!, getType(type), time)
                when (selectType) {
                    LotteryConstant.TYPE_LUZHU_5, LotteryConstant.TYPE_LUZHU_8, LotteryConstant.TYPE_LUZHU_10 -> ViewUtils.setGone(tvSelectAll)
                    else -> ViewUtils.setVisible(tvSelectAll)
                }
            }
        }
    }

    fun initEvent() {
        tvSelectAll?.setOnClickListener {
            initBottomDialog()
        }
        tvToDay?.setOnClickListener {
            tvToDay?.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            tvYesterday?.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tvBeforeYesterday?.setTextColor(ViewUtils.getColor(R.color.color_333333))
            getLuZhuData(arguments?.getString("lotteryId")!!, selectType)
            time = ""
        }
        tvYesterday?.setOnClickListener {
            tvToDay?.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tvYesterday?.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            tvBeforeYesterday?.setTextColor(ViewUtils.getColor(R.color.color_333333))
            getLuZhuData(arguments?.getString("lotteryId")!!, selectType, TimeUtils.getYesterday())
            time = TimeUtils.getYesterday()
        }
        tvBeforeYesterday?.setOnClickListener {
            tvToDay?.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tvYesterday?.setTextColor(ViewUtils.getColor(R.color.color_333333))
            tvBeforeYesterday?.setTextColor(ViewUtils.getColor(R.color.color_FF513E))
            getLuZhuData(arguments?.getString("lotteryId")!!, selectType, TimeUtils.getBeforeYesterday())
            time = TimeUtils.getBeforeYesterday()
        }
    }

    //设置露珠数据
    private fun getLuZhuView(it: String, type: String) {
        ViewUtils.setGone(tvLuZhuPlaceHolder)
        val bean = JsonUtils.fromJson(it, LotteryCodeLuZhuResponse::class.java)
        if (luZhuRecycleAdapter != null) {
            luZhuRecycleAdapter!!.total = bean.total
            luZhuRecycleAdapter!!.clear()
            luZhuRecycleAdapter!!.type = type
            luZhuRecycleAdapter!!.addAll(bean.data)
            val lotteryID = arguments?.getString("lotteryId")!!
            if (rankList == null) rankList = arrayListOf()
            if (bean.data.isNullOrEmpty()) return
            rankList?.clear()
            if (lotteryID == "7" || lotteryID == "9" || lotteryID == "11" || lotteryID == "26" || lotteryID == "27") {
                for ((index, l1) in bean.data!!.withIndex()) {
                    val be = BottomDialogBean()
                    when (index) {
                        0 -> {
                            be.str = "冠军"
                        }
                        1 -> {
                            be.str = "亚军"
                        }
                        else -> {
                            be.str = "第" + (index + 1) + "名"
                        }
                    }
                    rankList?.add(be)
                }
            } else if (lotteryID == "8") {
                for ((index, l1) in bean.data!!.withIndex()) {
                    val be = BottomDialogBean()
                    when (index) {
                        bean.data!!.size -> {
                            be.str = "特码"
                        }
                        else -> {
                            be.str = "正码" + (index + 1)
                        }
                    }
                    rankList?.add(be)
                }
            } else {
                if (selectType != LotteryConstant.TYPE_LUZHU_11){
                    for ((index, l1) in bean.data!!.withIndex()) {
                        val be = BottomDialogBean()
                        be.str = "第" + (index + 1) + "球"
                        rankList?.add(be)
                    }
                }else{
                    for ((index, l1) in bean.data!!.withIndex()) {
                        val be = BottomDialogBean()
                        be.str = "号码 $index"
                        rankList?.add(be)
                    }
                }

            }
            resetDialog()
        }
    }

    //获取露珠数据
    private fun getLuZhuData(lotteryId: String, typeTitle: String, time: String = TimeUtils.getToday()) {
        ViewUtils.setVisible(tvLuZhuPlaceHolder)
        LotteryApi.getLotteryLuZhu(lotteryId, typeTitle, time) {
            onSuccess {
                if (isAdded) {
                    getLuZhuView(it, typeTitle)
                }
            }
            onFailed {
                if (isAdded) {
                    ViewUtils.setVisible(tvHolder)
                    ViewUtils.setGone(tvLuZhuPlaceHolder)
                }
            }
        }
    }


    //底部选择弹框
    private fun initBottomDialog() {
        if (bottomDialog == null) {
            bottomDialog = context?.let { BottomLotteryDialog(it, rankList) }
            bottomDialog!!.bottomAdapter!!.setOnItemClickListener { data, position ->
                data.isSelect = !data.isSelect
                bottomDialog!!.bottomAdapter!!.notifyItemChanged(position)
            }
            bottomDialog!!.setOnSureClickListener {
                val selectList = ArrayList<Boolean>()
                for (s in it) {
                    selectList.add(s.isSelect)
                }
                luZhuRecycleAdapter!!.notifyHideItem(selectList)
                bottomDialog!!.dismiss()
            }
            bottomDialog!!.show()
        } else bottomDialog!!.show()
    }

    /**
     * reset
     */
    private fun resetDialog() {
        if (rankList.isNullOrEmpty()) return
        bottomDialog = context?.let { BottomLotteryDialog(it, rankList) }
        bottomDialog!!.bottomAdapter!!.setOnItemClickListener { data, position ->
            data.isSelect = !data.isSelect
            bottomDialog!!.bottomAdapter!!.notifyItemChanged(position)
        }
        bottomDialog!!.setOnSureClickListener {
            val selectList = ArrayList<Boolean>()
            for (s in it) {
                selectList.add(s.isSelect)
            }
            luZhuRecycleAdapter!!.notifyHideItem(selectList)
            bottomDialog!!.dismiss()
        }
    }

    private fun getType(typeTitle: String): String {
        return when (typeTitle) {
            LotteryConstant.TYPE_2 -> LotteryConstant.TYPE_LUZHU_2
            LotteryConstant.TYPE_3 -> LotteryConstant.TYPE_LUZHU_3
            LotteryConstant.TYPE_5 -> LotteryConstant.TYPE_LUZHU_5
            LotteryConstant.TYPE_8 -> LotteryConstant.TYPE_LUZHU_8
            LotteryConstant.TYPE_9 -> LotteryConstant.TYPE_LUZHU_9
            LotteryConstant.TYPE_10 -> LotteryConstant.TYPE_LUZHU_10
            LotteryConstant.TYPE_11 -> LotteryConstant.TYPE_LUZHU_11
            LotteryConstant.TYPE_15 -> LotteryConstant.TYPE_LUZHU_15
            LotteryConstant.TYPE_16 -> LotteryConstant.TYPE_LUZHU_16
            LotteryConstant.TYPE_12 -> LotteryConstant.TYPE_LUZHU_12
            else -> "daxiao"
        }

    }


    companion object {
        fun newInstance(lotteryID: String?): LiveRoomBetToolsTrendFragment1 {
            val fragment = LiveRoomBetToolsTrendFragment1()
            val bundle = Bundle()
            bundle.putString("lotteryId", lotteryID)
            fragment.arguments = bundle
            return fragment
        }
    }
}