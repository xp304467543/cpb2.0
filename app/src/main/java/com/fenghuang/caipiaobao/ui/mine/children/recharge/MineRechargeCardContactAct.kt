package com.fenghuang.caipiaobao.ui.mine.children.recharge

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.activity.BaseNavActivity
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MineRechargeDiamond
import com.fenghuang.caipiaobao.widget.dialog.RechargeSuccessDialog
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomDialogBean
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_card_contact.*
import kotlinx.android.synthetic.main.my_top_bar.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/23
 * @ Describe
 *
 */
class MineRechargeCardContactAct : BaseNavActivity() {

    override fun getPageTitle() = "充值"

    override fun getContentResID() = R.layout.fragment_card_contact

    override fun isSwipeBackEnable() = true

    override fun isShowBackIconWhite() = false

    var adapter = RvAdapter()


    override fun initContentView() {
        setVisible(ivTitleRight)
        ivTitleRight.setBackgroundResource(R.mipmap.ic_tips)
        rvContactCard.adapter = adapter
        rvContactCard.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun initEvent() {
        val content = "1.代理只负责卡密销售，如果您有其他问题，请联系我们在线客服处理。代理如果骚扰或推荐您到其他平台，欢迎举报，举报属实奖励5千元\n\n" +
                "2.点击图标，系统自动复制对应（支付宝，qq、微信）账号，打开对应（支付宝，qq、微信）号，添加好友购买.\n\n" +
                "3.代理人员会提供，账号密码，返回客户端输入账号密码充值。 \n\n" +
                "4.充值完成，提示充值成功，返回钱包页面查看金额。\n\n" +
                "5.代理人员不定期更换，为了避免不必要损失每次支付请到平台提单，以官方为准，谢谢！"
        ivTitleRight.setOnClickListener {
            RechargeSuccessDialog(this, content, 0, "充值说明").show()
        }
    }

    override fun initData() {
        showPageLoadingDialog()
        MineApi.cardList {
            onSuccess {
                adapter.addAll(it)
                hidePageLoadingDialog()
            }
            onFailed {
                ToastUtils.show("获取失败")
                hidePageLoadingDialog()
            }
        }
    }


    inner class RvAdapter : BaseRecyclerAdapter<MineRechargeDiamond>(this) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineRechargeDiamond> {
            return RvAdapterHolder(parent)
        }

        inner class RvAdapterHolder(parent: ViewGroup) : BaseViewHolder<MineRechargeDiamond>(getContext(), parent, R.layout.holder_diamond_recharge) {
            override fun onBindData(data: MineRechargeDiamond) {
                setText(R.id.tvTipTitle, data.name)
                val spannableString = SpannableString("可用额度: " + data.quota)
                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#ff513e")), 6, spannableString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                setText(R.id.tvTipContent, spannableString)
                ImageManager.loadImg(data.contact[0].icon ?: "", findView(R.id.img_1))
                ImageManager.loadImg(data.contact[1].icon ?: "", findView(R.id.img_2))
                ImageManager.loadImg(data.contact[2].icon ?: "", findView(R.id.img_3))
                setText(R.id.tv_1, data.contact[0].title ?: "")
                setText(R.id.tv_2, data.contact[1].title ?: "")
                setText(R.id.tv_3, data.contact[2].title ?: "")
                setOnClick(R.id.lin1)
                setOnClick(R.id.lin2)
                setOnClick(R.id.lin3)
            }

            override fun onClick(id: Int) {
                var type = ""
                when (id) {
                    R.id.lin1 -> {
                        ViewUtils.copyText(getData()?.contact?.get(0)?.value ?: "")
                        type = getData()?.contact?.get(0)?.title ?: ""
                    }
                    R.id.lin2 -> {
                        ViewUtils.copyText(getData()?.contact?.get(1)?.value ?: "")
                        type = getData()?.contact?.get(1)?.title ?: ""
                    }
                    R.id.lin3 -> {
                        ViewUtils.copyText(getData()?.contact?.get(2)?.value ?: "")
                        type = getData()?.contact?.get(2)?.title ?: ""
                    }
                }
                val text = "请前往" + type + "联系"
                val spannableString = SpannableString(text)
                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#ff513e")), 3, type.length+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                RechargeSuccessDialog(this@MineRechargeCardContactAct, "", 0, "已复制联系方式",spannableString).show()
            }
        }
    }
}