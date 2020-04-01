package com.fenghuang.caipiaobao.ui.mine.children.recharge

import android.annotation.SuppressLint
import android.text.TextUtils
import com.fenghuang.baselib.base.activity.BaseNavActivity
import com.fenghuang.baselib.base.fragment.BaseNavFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MineBankList
import com.fenghuang.caipiaobao.ui.mine.data.MineUpDateBank
import com.fenghuang.caipiaobao.widget.dialog.WheelViewDialog
import com.fenghuang.caipiaobao.widget.dialog.city.CityEntity
import com.fenghuang.caipiaobao.widget.dialog.city.CitySelectDialog
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.dialog_city.*
import kotlinx.android.synthetic.main.dialog_wheel_view.*
import kotlinx.android.synthetic.main.dialog_wheel_view.tvWheelSure
import kotlinx.android.synthetic.main.fragment_mine_add_bank_card.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-04
 * @ Describe
 *
 */
class MineAddBankCardActivity : BaseNavActivity() {

    private var province: String? = null
    private var city: String? = null
    var bankListAll: List<MineBankList>? = null
    var bankCode: String = "-1001"
    var dataList: List<MineBankList>? = null


    override fun getContentResID() = R.layout.fragment_mine_add_bank_card

    override fun getPageTitle() = getString(R.string.mine_add_bank_card)

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun isOverride() = false

    val list = arrayListOf<String>()

    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(this@MineAddBankCardActivity, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        StatusBarUtils.setStatusBarForegroundColor(this@MineAddBankCardActivity, true)
    }

    override fun initData() {
        getBankList()
    }

    @SuppressLint("SetTextI18n")
    override fun initEvent() {
        tvOpenCity.setOnClickListener {
            val citySelectDialog = CitySelectDialog(this@MineAddBankCardActivity)
            citySelectDialog.tvWheelSure.setOnClickListener {
                tvOpenCity.text = (citySelectDialog.cityPickerView.opt1SelectedData as CityEntity).name +
                        " " + (citySelectDialog.cityPickerView.opt2SelectedData as CityEntity).name +
                        " " + (citySelectDialog.cityPickerView.opt3SelectedData as CityEntity).name
                province = (citySelectDialog.cityPickerView.opt1SelectedData as CityEntity).name
                city = (citySelectDialog.cityPickerView.opt2SelectedData as CityEntity).name
                citySelectDialog.dismiss()
            }
            citySelectDialog.show()
        }

        bindSubmit.setOnClickListener {
            if (bankCode == "-1001") {
                ToastUtils.showWarning("请选择银行卡")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(tvOpenCity.text)) {
                ToastUtils.showWarning("请选择开户城市")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(etOpenCityOther.text)) {
                ToastUtils.showWarning("请填写开户支行")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(etOpenName.text)) {
                ToastUtils.showWarning("请填写开户姓名")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(etOpenNumber.text)) {
                ToastUtils.showWarning("请填写银行卡号")
                return@setOnClickListener
            }
            if (etOpenNumber.text.length < 15 || etOpenNumber.text.length > 22) {
                ToastUtils.showWarning("请填写正确的15-22位银行卡号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(etOpenPassWord.text)) {
                ToastUtils.showWarning("请填写支付密码")
                return@setOnClickListener
            }

            bindBankCard(bankCode,
                    province.toString(),
                    city.toString(),
                    etOpenCityOther.text.toString(),
                    etOpenName.text.toString(),
                    etOpenNumber.text.toString(), etOpenPassWord.text.toString())
        }
    }

    private fun initWheelView(bankList: List<String>) {
        linPickLayout.setOnClickListener {
            val wheelViewDialog = WheelViewDialog(this@MineAddBankCardActivity, bankList, "请选择银行卡")
            wheelViewDialog.tvWheelSure.setOnClickListener {
                tvUserBankCard.text = wheelViewDialog.wheelView.selectedItemData.toString()
                bankCode = bankListAll?.get(wheelViewDialog.wheelView.selectedItemPosition)?.code.toString()
                wheelViewDialog.dismiss()
            }
            wheelViewDialog.show()
        }
    }


    private fun getBankList() {
        showPageLoadingDialog()
        MineApi.getBankList {
            onSuccess {
                dataList = it
                if (it.isNotEmpty()) {
                    bankListAll = it
                    tvUserBankCard.text = it[it.size / 2].name
                    bankCode = it[it.size / 2].code
                    val banNameList = arrayListOf<String>()
                    for (i in it.iterator()) {
                        banNameList.add(i.name)
                    }
                    initWheelView(banNameList)
                }
                hidePageLoadingDialog()
            }
            onFailed {
                ToastUtils.show(it.getMsg().toString())
                hidePageLoadingDialog()
            }
        }
    }

    private fun bindBankCard(bank_code: String, province: String, city: String, branch: String, realname: String, card_num: String, fund_password: String) {
        showPageLoadingDialog()
        MineApi.bingBankCard(bank_code, province, city, branch, realname, card_num, fund_password) {
            onSuccess {
                ToastUtils.showSuccess("绑定成功")
                RxBus.get().post(MineUpDateBank(true))
                hidePageLoadingDialog()
                finish()
            }
            onFailed {
                hidePageLoadingDialog()
                ToastUtils.show(it.getMsg().toString())

            }
        }
    }
}
