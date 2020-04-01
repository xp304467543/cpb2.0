package com.fenghuang.caipiaobao.ui.mine.children.recharge

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.activity.BaseNavActivity
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MineSaveBank
import com.fenghuang.caipiaobao.ui.mine.data.MineUpDateBank
import com.fenghuang.caipiaobao.ui.mine.data.MineUserBankList
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_mine_bank_card_list.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-04
 * @ Describe 用户银行卡列表
 *
 */

class MineUserBankCardListActivity : BaseNavActivity() {

    var mineItemAdapter: MineUserBankCardListAdapter? = null


    override fun getContentResID() = R.layout.fragment_mine_bank_card_list

    override fun getPageTitle() = getString(R.string.mine_card_list)

    override fun isShowBackIconWhite() = false

    override fun isSwipeBackEnable() = true

    override fun isOverride() = false

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(this@MineUserBankCardListActivity, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        StatusBarUtils.setStatusBarForegroundColor(this@MineUserBankCardListActivity, false)
    }

    override fun initData() {
        getBankList()
    }


    override fun initEvent() {
        rlAddBank.setOnClickListener {
            val intent = Intent(this@MineUserBankCardListActivity, MineAddBankCardActivity::class.java)
            startActivity(intent)
        }
    }


    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun saveUserBankSelect(event: MineSaveBank) {
        UserInfoSp.putSelectBankCard(event.data)
        finish()

    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDateUserBankSelect(event: MineUpDateBank) {
        getBankList()
    }


    @SuppressLint("SetTextI18n")
    fun getBankList() {
        showPageLoadingDialog()
        MineApi.getUserBankList {
            onSuccess {
                mineItemAdapter = MineUserBankCardListAdapter(this@MineUserBankCardListActivity, intent.getStringExtra("cardID")!!.toString())
                mineItemAdapter?.addAll(it)
                rvCardList.adapter = mineItemAdapter
                val value = LinearLayoutManager(this@MineUserBankCardListActivity)
                rvCardList.layoutManager = value
                hidePageLoadingDialog()
            }
            onFailed {
                ToastUtils.show(it.getMsg().toString())
                hidePageLoadingDialog()
            }
        }
    }



    inner class MineUserBankCardListAdapter(context: Context, var cardId: String) : BaseRecyclerAdapter<MineUserBankList>(context) {

        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineUserBankList> {
            return MineBankCardListHolder(parent)
        }

        inner class MineBankCardListHolder(parent: ViewGroup) : BaseViewHolder<MineUserBankList>(getContext(), parent, R.layout.holder_mine_bank_card_list) {
            override fun onBindData(data: MineUserBankList) {
                setText(R.id.tvBankName, data.bank_name)
                setText(R.id.tvBankCode, "尾号" + data.card_num.substring(data.card_num.length - 4, data.card_num.length) + "储蓄卡")
                ImageManager.loadImg(data.bank_img, findView(R.id.imgBank))
                if (clickPosition != -1) {
                    if (clickPosition == getDataPosition()) {
                        findView<ImageView>(R.id.cbCard).background = ViewUtils.getDrawable(R.mipmap.ic_mine_checked)
                        RxBus.get().post(MineSaveBank(data))
                    } else findView<ImageView>(R.id.cbCard).background = ViewUtils.getDrawable(R.mipmap.ic_mine_check)
                }

                if (cardId == data.card_num){
                    findView<ImageView>(R.id.cbCard).background = ViewUtils.getDrawable(R.mipmap.ic_mine_checked)
                }else findView<ImageView>(R.id.cbCard).background = ViewUtils.getDrawable(R.mipmap.ic_mine_check)


            }

            override fun onItemClick(data: MineUserBankList) {
                singleChoose(getDataPosition())
            }

        }

        //这个是checkbox的Hashmap集合
        private var clickPosition: Int = -1

        /**
         * 单选
         *
         * @param position
         */
        fun singleChoose(position: Int) {
            clickPosition = position
            notifyDataSetChanged()
        }
    }
}