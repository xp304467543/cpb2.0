package com.fenghuang.caipiaobao.ui.mine.children.recharge

import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.fragment.BaseContentFragment
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MinePayTypeList
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.dialog.InvestDialog
import kotlinx.android.synthetic.main.fragment_mine_charge_item.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-04
 * @ Describe 充值列表
 *
 */

class MineRechargeItemFragment : BaseContentFragment() {


    override fun getContentResID() = R.layout.fragment_mine_charge_item


    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), false)
    }

    override fun initData() {
        getPayTypeList()
    }


    private fun getPayTypeList() {
        showPageLoadingDialog()
        MineApi.getPayTypeList {
            onSuccess {
                if (isSupportVisible) {
                    initAdapter(it)
                }
            }
            onFailed {
                GlobalDialog.ShowError(getPageActivity(),it)
            }
        }
    }

    private fun initAdapter(data: List<MinePayTypeList>) {
        val mineRechargeItemAdapter = MineRechargeItemAdapter(getPageActivity())
        mineRechargeItemAdapter.addAll(data)
        rvRecharges.adapter = mineRechargeItemAdapter
        val value = object : LinearLayoutManager(getPageActivity()) {
            override fun canScrollVertically(): Boolean {
                return true
            }
        }
        rvRecharges.layoutManager = value
        hidePageLoadingDialog()
    }

    /**
     * @ Describe 充值Adapter
     */
    inner class MineRechargeItemAdapter(context: Context) : BaseRecyclerAdapter<MinePayTypeList>(context) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MinePayTypeList> {
            return MineRechargeItemHolder(parent)
        }

        inner class MineRechargeItemHolder(parent: ViewGroup) : BaseViewHolder<MinePayTypeList>(getContext(), parent, R.layout.holder_mine_recharge_item) {
            override fun onBindData(data: MinePayTypeList) {
                setText(R.id.tvBankName, data.channels_type)
                setText(R.id.tvMoneyBorder, "(" + (if ((data.low_money + "") == "null") "" else data.low_money) + " ~ " + (if ((data.low_money + "") == "null") "" else data.high_money) + ")")
                ImageManager.loadImg(data.icon.replace("\\", "/"), findView(R.id.imgBankType))

            }

            override fun onItemClick(data: MinePayTypeList) {
                if (data.pay_type == "rgcz") {
                    LaunchUtils.startInvest(getPageActivity(),0.0, data.id, data.apiroute, true)
                }else {
                    val dialog = getContext()?.let { InvestDialog(it, data.channels_type, "确定", "取消") }
                    dialog?.setConfirmClickListener {
                        judgeMoney(dialog, data)
                    }
                    dialog?.show()
                }
            }

            private fun judgeMoney(dialog: InvestDialog, it: MinePayTypeList) {
                if (!TextUtils.isEmpty(dialog.getText())) {
                    val money = dialog.getText().toDouble()
                    if (it.high_money.toDouble() >= money && it.low_money.toDouble() <= money) {
                        LaunchUtils.startInvest(getPageActivity(),money, it.id, it.apiroute, false)
                        dialog.dismiss()
                    } else ToastUtils.show("充值金额为:" + it.low_money + "~" + it.high_money)

                } else ToastUtils.show("充值金额为:" + it.low_money + "~" + it.high_money)
            }
        }


    }
}