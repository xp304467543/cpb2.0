package com.fenghuang.caipiaobao.ui.mine.children.report

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MineLevelList
import com.fenghuang.caipiaobao.ui.mine.data.MineThemSkinResponse
import com.fenghuang.caipiaobao.widget.dialog.GlobalTipsDialog
import com.fenghuang.caipiaobao.widget.dialog.ReportBottomDialog
import com.fenghuang.caipiaobao.widget.dialog.ReportDialog
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_report_4.*
import org.json.JSONObject

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment4 : BaseMvpFragment<ReportFragment4P>() {

    var state = ""

    var reportBottomDialog:ReportBottomDialog?=null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ReportFragment4P()

    override fun getLayoutResID() = R.layout.fragment_report_4

    var adapter : LevelAdapter?=null

    override fun onSupportVisible() {
        mPresenter.getCode()
    }

    override fun initContentView() {
        adapter = LevelAdapter(getPageActivity())
        levelList.adapter = adapter
        levelList.layoutManager =object :LinearLayoutManager(getPageActivity(), VERTICAL, false){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
    }

    override fun initEvent() {
        tvGetReportCode.setOnClickListener {
            when (state) {
                "10" -> {
                    reportBottomDialog?.show()
                }
                "9" -> {
                    GlobalTipsDialog(getPageActivity(),
                            "推广申请", "确定", "",
                            "您提交的推广申请，小姐姐们正在\n\n努力审核，请稍等").show()
                }
                else -> {
                    val dialog = ReportDialog(getPageActivity())
                    dialog.setConfirmClickListener {
                        MineApi.supportCode(it) {
                            onSuccess {
                                if (isActive()) {
                                    GlobalTipsDialog(getPageActivity(),
                                            "推广申请", "确定", "",
                                            "您提交的推广申请，小姐姐们正在\n\n努力审核，请稍等").show()
                                    state = "9"
                                }
                            }
                            onFailed { ext ->
                                GlobalTipsDialog(getPageActivity(),
                                        "推广申请", "确定", "",
                                        ext.getMsg() ?: "申请失败").show()
                            }
                        }
                    }
                    dialog.show()
                }
            }
        }
    }


    fun initDialog(num:String,url:String){
        reportBottomDialog = ReportBottomDialog(getPageActivity(),num,url)
    }

    inner class LevelAdapter(context: Context) : BaseRecyclerAdapter<MineLevelList>(context) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineLevelList> {
            return LevelHolder(parent)
        }

        inner class LevelHolder(parent: ViewGroup) : BaseViewHolder<MineLevelList>(getContext(), parent, R.layout.holder_item_level) {
            @SuppressLint("SetTextI18n")
            override fun onBindData(data: MineLevelList) {
                val name = findView<TextView>(R.id.tvLevelName)
                when (data.level) {
                    "1" -> {
                        name.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.ic_v_1), null, null, null)
                    }
                    "2" -> {
                        name.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.ic_v_2), null, null, null)
                    }
                    "3" -> {
                        name.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.ic_v_3), null, null, null)
                    }
                    "4" -> {
                        name.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.ic_v_4), null, null, null)
                    }
                    "5" -> {
                        name.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.ic_v_5), null, null, null)
                    }
                    "6" -> {
                        name.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.mipmap.ic_v_6), null, null, null)
                    }
                }
                name.text = data.invitee_num+"人"
                if (data.level == "1") {
                    setText(R.id.tvLevelContent, "成功邀请一人立即到账" + data.reward?.toInt() + "元-返现比例" + (data.rebate
                            ?: 0.1) * 100 + "%")
                } else {
                    setText(R.id.tvLevelContent, "成功邀请立即到账" + data.reward?.toInt() + "元-返现比例" + (data.rebate
                            ?: 0.1) * 100 + "%")
                }

            }
        }
    }
}


