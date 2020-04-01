package com.fenghuang.caipiaobao.ui.lottery.children

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryChildLuZhuAdapter
import com.fenghuang.caipiaobao.ui.lottery.adapter.LotteryChildTypeAdapter
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryConstant
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryCodeLuZhuResponse
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.JsonUtils
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomLotteryDialog
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomDialogBean
import kotlinx.android.synthetic.main.child_fragment_lu_zhu.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/2- 15:36
 * @ Describe 路珠
 *
 */

class LotteryLuZhuFragment : BaseMvpFragment<LotteryLuZhuFragmentPresenter>() {

    var bottomDialog: BottomLotteryDialog? = null

    var rankList: List<BottomDialogBean>? = null

    var luZhuRecycle: RecyclerView? = null

    var luZhuRecycleAdapter: LotteryChildLuZhuAdapter? = null

    var selectType = LotteryConstant.TYPE_LUZHU_2

    var time = ""//昨天 今天 明天


    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = LotteryLuZhuFragmentPresenter()

    override fun getContentResID() = R.layout.child_fragment_lu_zhu

    override fun isShowToolBar() = false

    override fun isOverridePage() = false

    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }

    override fun initContentView() {
        smartRefreshLayoutLotteryLuZhuType.setEnableRefresh(false)//是否启用下拉刷新功能
        smartRefreshLayoutLotteryLuZhuType.setEnableLoadMore(false)//是否启用上拉加载功能
        smartRefreshLayoutLotteryLuZhuType.setEnableOverScrollBounce(true)//是否启用越界回弹
        smartRefreshLayoutLotteryLuZhuType.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        initLotteryTypeSelect()
    }

    override fun initData() {
        mPresenter.getLuZhuData(arguments?.getString("lotteryId")!!, selectType)
        luZhuRecycle = rvLotteryLuZhu
        luZhuRecycleAdapter = LotteryChildLuZhuAdapter(context!!, arguments?.getString("lotteryId")!!)
        luZhuRecycle!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        luZhuRecycle!!.adapter = luZhuRecycleAdapter
        luZhuRecycle!!.setItemViewCacheSize(10)
    }


    //初始化 具体 Type 号码 大小 单双 等切换
    private fun initLotteryTypeSelect() {
        val data: Array<String>?
        val value = LinearLayoutManager(getPageActivity(), LinearLayoutManager.HORIZONTAL, false)
        val lotteryTypeAdapter = LotteryChildTypeAdapter(getPageActivity())
        val lotteryID = arguments?.getString("lotteryId")!!
        if (lotteryID == "7" || lotteryID == "9" || lotteryID == "11" || lotteryID == "26" || lotteryID == "27") {
            data = arrayOf(LotteryConstant.TYPE_2, LotteryConstant.TYPE_3, LotteryConstant.TYPE_8, LotteryConstant.TYPE_9, LotteryConstant.TYPE_10)
            rankList = listOf(
                    BottomDialogBean("冠军"), BottomDialogBean("亚军"), BottomDialogBean("第三名"),
                    BottomDialogBean("第四名"), BottomDialogBean("第五名"), BottomDialogBean("第六名"),
                    BottomDialogBean("第七名"), BottomDialogBean("第八名"), BottomDialogBean("第九名"),
                    BottomDialogBean("第十名"))
        } else if (lotteryID == "8") {
            data = arrayOf(LotteryConstant.TYPE_2, LotteryConstant.TYPE_3, LotteryConstant.TYPE_12, LotteryConstant.TYPE_5, LotteryConstant.TYPE_15, LotteryConstant.TYPE_16)
            rankList = listOf(BottomDialogBean("正码一"), BottomDialogBean("正码二"), BottomDialogBean("正码三"),
                    BottomDialogBean("正码四"), BottomDialogBean("正码五"), BottomDialogBean("正码六"),
                    BottomDialogBean("特码"))
            tvSelectAll.text = "筛选号码"
            setGone(tvToDay)
            setGone(tvYesterday)
            setGone(tvBeforeYesterday)
        } else {
            data = arrayOf(LotteryConstant.TYPE_2, LotteryConstant.TYPE_3, LotteryConstant.TYPE_8, LotteryConstant.TYPE_11, LotteryConstant.TYPE_5)
            rankList = listOf(BottomDialogBean("第一球"), BottomDialogBean("第二球"), BottomDialogBean("第三球"),
                    BottomDialogBean("第四球"), BottomDialogBean("第五球"))
            tvSelectAll.text = "筛选号码"
        }
        rvLuZhuTypeSelect.layoutManager = value
        rvLuZhuTypeSelect.adapter = lotteryTypeAdapter
        if (data.isNotEmpty()) lotteryTypeAdapter.addAll(data)
        lotteryTypeAdapter.setOnItemClickListener { type, position ->
            if (FastClickUtils.isFastClick()) {
                lotteryTypeAdapter.changeBackground(position)
                selectType = mPresenter.getType(type)

                if (time == "") mPresenter.getLuZhuData(arguments?.getString("lotteryId")!!,  mPresenter.getType(type))
                else mPresenter.getLuZhuData(arguments?.getString("lotteryId")!!,  mPresenter.getType(type), time)
                when (selectType) {
                    LotteryConstant.TYPE_LUZHU_5, LotteryConstant.TYPE_LUZHU_8, LotteryConstant.TYPE_LUZHU_10 -> setGone(tvSelectAll)
                    else -> setVisible(tvSelectAll)
                }
            }
        }
    }


    override fun initEvent() {
        tvSelectAll.setOnClickListener {
            if (bottomDialog == null) {
                bottomDialog = BottomLotteryDialog(getPageActivity(), rankList)
                bottomDialog!!.bottomAdapter!!.setOnItemClickListener { data, position ->
                    data.isSelect = !data.isSelect
                    bottomDialog!!.bottomAdapter!!.notifyItemChanged(position)
                }
                bottomDialog!!.setOnSureClickListener {
                    val selectList: MutableList<Boolean> = ArrayList()
                    for (s in it) {
                        selectList.add(s.isSelect)
                    }
                    luZhuRecycleAdapter!!.notifyHideItem(selectList)
                    bottomDialog!!.dismiss()
                }
                bottomDialog!!.show()
            } else bottomDialog!!.show()
        }
        tvToDay.setOnClickListener {

            tvToDay.setTextColor(getColor(R.color.color_FF513E))
            tvYesterday.setTextColor(getColor(R.color.color_333333))
            tvBeforeYesterday.setTextColor(getColor(R.color.color_333333))
            mPresenter.getLuZhuData(arguments?.getString("lotteryId")!!, selectType)
            time = ""
        }
        tvYesterday.setOnClickListener {

            tvToDay.setTextColor(getColor(R.color.color_333333))
            tvYesterday.setTextColor(getColor(R.color.color_FF513E))
            tvBeforeYesterday.setTextColor(getColor(R.color.color_333333))
            mPresenter.getLuZhuData(arguments?.getString("lotteryId")!!, selectType, TimeUtils.getYesterday())
            time = TimeUtils.getYesterday()
        }
        tvBeforeYesterday.setOnClickListener {

            tvToDay.setTextColor(getColor(R.color.color_333333))
            tvYesterday.setTextColor(getColor(R.color.color_333333))
            tvBeforeYesterday.setTextColor(getColor(R.color.color_FF513E))
            mPresenter.getLuZhuData(arguments?.getString("lotteryId")!!, selectType, TimeUtils.getBeforeYesterday())
            time = TimeUtils.getBeforeYesterday()
        }
    }

    //设置露珠数据
    fun getLuZhuView(it: String, type: String) {
        setGone(tvLuZhuPlaceHolder)
        val bean = JsonUtils.fromJson(it, LotteryCodeLuZhuResponse::class.java)
        if (luZhuRecycleAdapter != null) {
            luZhuRecycleAdapter!!.total = bean.total
            luZhuRecycleAdapter!!.clear()
            luZhuRecycleAdapter!!.type = type
            luZhuRecycleAdapter!!.addAll(bean.data)
        }
    }

    //更新露珠数据
    fun upDateLuZhu() {
        setVisible(tvLuZhuPlaceHolder)
        mPresenter.getLuZhuData(arguments?.getString("lotteryId")!!, selectType)
    }


    companion object {
        fun newInstance(anchorId: String): LotteryLuZhuFragment {
            val fragment = LotteryLuZhuFragment()
            val bundle = Bundle()
            bundle.putString("lotteryId", anchorId)
            fragment.arguments = bundle
            return fragment
        }
    }
}