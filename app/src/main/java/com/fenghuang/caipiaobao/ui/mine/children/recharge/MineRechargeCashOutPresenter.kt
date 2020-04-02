package com.fenghuang.caipiaobao.ui.mine.children.recharge

import android.annotation.SuppressLint
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MinePassWordTime
import com.fenghuang.caipiaobao.ui.mine.data.MineUpDateMoney
import com.fenghuang.caipiaobao.ui.mine.data.MineUserBankList
import com.fenghuang.caipiaobao.utils.JsonUtils
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.dialog.PassWordDialog
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.fragment_mine_cash_out.*
import java.math.BigDecimal


/**
 *
 * @ Author  QinTian
 * @ Date  2019/10/11- 16:02
 * @ Describe
 *
 */

class MineRechargeCashOutPresenter : BaseMvpPresenter<MineRechargeCashOutFragment>() {


    var mineUserBank: MineUserBankList? = null

    @SuppressLint("SetTextI18n")
    fun getBankList() {
        mView.showPageLoadingDialog()
        MineApi.getUserBankList {
            onSuccess {
                if (mView.isActive()) {
                    if (it.isNotEmpty()) {
                        mView.setGone(R.id.rlAddBankItem)
                        mView.setVisibility(R.id.rlBankItem, true)
                        mineUserBank = UserInfoSp.getSelectBankCard()
                        val intent = Intent(mView.requireContext(),MineUserBankCardListActivity::class.java)
                        if (mineUserBank != null) {
                            ImageManager.loadImg(mineUserBank?.bank_img, mView.imgBankItem)
                            mView.tvBankNameItem.text = mineUserBank?.bank_name
                            mView.tvBankCodeItem.text = "尾号" + mineUserBank?.card_num?.substring(mineUserBank?.card_num?.length!! - 4, mineUserBank?.card_num!!.length) + "储蓄卡"

                        } else {
                            ImageManager.loadImg(it[0].bank_img, mView.imgBankItem)
                            mView.tvBankNameItem.text = it[0].bank_name
                            mView.tvBankCodeItem.text = "尾号" + it[0].card_num.substring(it[0].card_num.length - 4, it[0].card_num.length) + "储蓄卡"
                            mineUserBank = it[0]

                        }
                        mView.rlBankItem.setOnClickListener {
                            intent.putExtra("cardID",mineUserBank?.card_num)
                            mView.requireContext().startActivity(intent)
                        }
                    } else {
                        mView.setGone(R.id.rlBankItem)
                        mView.setVisibility(R.id.rlAddBankItem, true)
                    }
                }
                mView.hidePageLoadingDialog()
            }
            onFailed {
                ToastUtils.show(it.getMsg().toString())
                mView.hidePageLoadingDialog()
            }
        }
    }

    fun getCashOutMoney() {
        if (!TextUtils.isEmpty(mView.etGetMoneyToBank.text)) {
            val money = BigDecimal(mView.etGetMoneyToBank.text.toString())
            val balance = BigDecimal(mView.arguments?.getString("balance") ?: "0")
            if (balance.compareTo(money) != -1) {
                if (money.compareTo(BigDecimal(50)) != -1) {
                    initDialog()
                } else ToastUtils.showError("提现金额不能小于50.00元")
            } else ToastUtils.showError("余额不足")
        } else ToastUtils.showError("请输入提现金额")

    }

    lateinit var dialog: PassWordDialog
    //密码输入框
    private fun initDialog() {
        dialog = PassWordDialog(mView.requireContext(), ViewUtils.getScreenWidth(), ViewUtils.dp2px(156))
        dialog.setTextWatchListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 6) {
                    //验证支付密码
                    mView.showPageLoadingDialog()
                    verifyPayPassWord(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        dialog.show()
    }

    //验证支付密码
    fun verifyPayPassWord(passWord: String) {
        MineApi.verifyPayPass(passWord) {
            onSuccess {
                if (mView.isActive()) {
                    userCashOut()
                }
            }
            onFailed {
                mView.hidePageLoadingDialog()
                if (it.getCode() == 1002) {
                    dialog.showTipsText(it.getMsg().toString() + "," + "您还有" + JsonUtils.fromJson(it.getDataCode().toString(), MinePassWordTime::class.java).remain_times.toString() + "次机会")
                    dialog.clearText()
                } else {
                    dialog.showTipsText(it.getMsg().toString())
                }
            }
        }
    }


    //取款 提现
    private fun userCashOut() {
        if (mineUserBank != null) {
            MineApi.userGetCashOut(mView.etGetMoneyToBank.text.toString().toDouble(), mineUserBank?.realname!!, mineUserBank?.card_num!!) {
                onSuccess {
                    if (mView.isActive()) {
                        ToastUtils.showSuccess("提现成功")
                        RxBus.get().post(MineUpDateMoney("",false))
                        dialog.dismiss()
                    }
                }
                onFailed {
                    ToastUtils.show(it.getMsg().toString())
                    dialog.dismiss()
                    mView.hidePageLoadingDialog()
                }
            }
        } else ToastUtils.showError("请选择银行卡")
        mView.hidePageLoadingDialog()

    }

}