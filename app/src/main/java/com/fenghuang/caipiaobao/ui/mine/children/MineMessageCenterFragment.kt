package com.fenghuang.caipiaobao.ui.mine.children

import android.os.Bundle
import android.text.TextUtils
import androidx.core.text.HtmlCompat
import com.fenghuang.baselib.base.fragment.BaseNavFragment
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MineMessageCenter
import com.fenghuang.caipiaobao.ui.moments.childern.MomentsAnchorFragment
import com.fenghuang.caipiaobao.ui.moments.data.MomentsHotDiscussResponse
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.LaunchUtils
import kotlinx.android.synthetic.main.fragment_message_center.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-05
 * @ Describe 消息中心
 *
 */

class MineMessageCenterFragment : BaseNavFragment() {

    private var list1: List<MineMessageCenter>? = null
    private var list2: List<MineMessageCenter>? = null
    private var list3: List<MineMessageCenter>? = null

    override fun isOverridePage() = false

    override fun isShowBackIconWhite() = false

    override fun getPageTitle() = "消息中心"

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.fragment_message_center

    override fun initContentView() {
        val msg1 = arguments?.getString("msg1") ?: "0"
        val msg2 = arguments?.getString("msg2") ?: "0"
        val msg3 = arguments?.getString("msg3") ?: "0"
        if (!TextUtils.isEmpty(msg2) && msg2 != "0") {
            setVisible(tvMessageNum)
            tvMessageNum.text = msg2
        }
        if (!TextUtils.isEmpty(msg3) && msg3 != "0") {
            setVisible(tvMessageNum2)
            tvMessageNum2.text = msg3
        }
        if (!TextUtils.isEmpty(msg1) && msg1 != "0") {
            setVisible(tvMessageNum3)
            tvMessageNum3.text = msg1
        }
    }

    override fun initData() {
        showPageLoadingDialog()
        getSystemMsg()
    }

    private fun getSystemMsg() {
        MineApi.getMessageTips("0") {
            onSuccess {
                if (!it.isNullOrEmpty() && isSupportVisible) {
                    tv3_time.text = it[0].createtime_txt
                    tv3_content.text = HtmlCompat.fromHtml(it[0].content, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    list3 = it
                }
            }
            onFailed {
                GlobalDialog.ShowError(requireActivity(), it)
            }
        }

        MineApi.getMessageTips("2") {
            onSuccess {
                if (!it.isNullOrEmpty() && isSupportVisible) {
                    tv1_time.text = it[0].createtime_txt
                    tv1_content.text = HtmlCompat.fromHtml(it[0].content, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    list1 = it
                }
            }
            onFailed {
                GlobalDialog.ShowError(requireActivity(), it)
            }
        }

        MineApi.getMessageTips("3") {
            onSuccess {
                if (!it.isNullOrEmpty() && isSupportVisible) {
                    tv2_time.text = it[0].createtime_txt
                    tv2_content.text = HtmlCompat.fromHtml(it[0].content, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    list2 = it
                }
            }
            onFailed {
                GlobalDialog.ShowError(requireActivity(), it)
            }
        }
        hidePageLoadingDialog()
    }

    override fun initEvent() {
        lin1.setOnClickListener {
            setGone(tvMessageNum)
            LaunchUtils.startFragment(context, MineMessageCenterInfoFragment.newInstance(0, list1))
        }
        lin2.setOnClickListener {
            setGone(tvMessageNum2)
            LaunchUtils.startFragment(context, MineMessageCenterInfoFragment.newInstance(1, list2))
        }
        lin3.setOnClickListener {
            setGone(tvMessageNum3)
            LaunchUtils.startFragment(context, MineMessageCenterInfoFragment.newInstance(2, list3))
        }
    }


    companion object {
        fun newInstance(msg1: String, msg2: String, msg3: String): MineMessageCenterFragment {
            val fragment = MineMessageCenterFragment()
            val bundle = Bundle()
            bundle.putSerializable("msg1", msg1)
            bundle.putSerializable("msg2", msg2)
            bundle.putSerializable("msg3", msg3)
            fragment.arguments = bundle
            return fragment
        }
    }
}