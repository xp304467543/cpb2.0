package com.fenghuang.caipiaobao.ui.mine.children.recharge

import android.annotation.SuppressLint
import androidx.viewpager.widget.ViewPager
import com.fenghuang.baselib.base.activity.BaseNavActivity
import com.fenghuang.baselib.base.adapter.BaseFragmentPageAdapter
import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MineUpDateMoney
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_mine_recharge.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-04
 * @ Describe 充值提现
 *
 */
class MineRechargeActivity : BaseNavActivity() {

    override val layoutResID = R.layout.fragment_mine_recharge

    override fun isSwipeBackEnable() = true

    override fun isRegisterRxBus() = true

    override fun isShowToolBar() = false

    override fun isOverride() = true


    override fun initData() {
        upDateBalance(true)
        rechargeTabView.setTabText("充值", "提现")
        rechargeTabView.setOnSelectListener {
            viewPager.currentItem = it
        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                rechargeTabView.setTabSelect(position)
            }
        })
        rechargeTabView.setTabSelect(intent.getIntExtra("index", 0))

    }

    private fun setFragmentViewPager() {
        val fragments = arrayListOf<BaseFragment>(
                MineRechargeItemFragment(),
                MineRechargeCashOutFragment.newInstance(tvCountBalance.text.toString())
        )
        val adapter = BaseFragmentPageAdapter(supportFragmentManager, fragments)
        viewPager.adapter = adapter
        viewPager.currentItem = intent.getIntExtra("index", 0)
        viewPager.offscreenPageLimit = fragments.size
    }


    override fun initEvent() {
        imgGoBack.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    fun upDateBalance(isUpdate: Boolean) {
        MineApi.getUserBalance {
            onSuccess {
                tvCountBalance.text = it.balance.toString()
                RxBus.get().post(MineUpDateMoney(it.balance.toString(), true))
                if (isUpdate) setFragmentViewPager()
            }
            onFailed {
                GlobalDialog.showError(this@MineRechargeActivity, it)
            }
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDateUserMoney(event: MineUpDateMoney) {
        if (!event.isUpdate) {
            upDateBalance(false)
        }
    }

}