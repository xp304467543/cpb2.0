package com.fenghuang.caipiaobao.ui.main

import android.Manifest
import android.graphics.drawable.Drawable
import com.fenghuang.baselib.base.fragment.BaseContentFragment
import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.helper.RxPermissionHelper
import com.fenghuang.caipiaobao.ui.bet.BetFragment
import com.fenghuang.caipiaobao.ui.home.HomeFragment
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.home.data.HomeJumpToMine
import com.fenghuang.caipiaobao.ui.home.data.JumpToBuyLottery
import com.fenghuang.caipiaobao.ui.lottery.LotteryFragment
import com.fenghuang.caipiaobao.ui.mine.MineFragment
import com.fenghuang.caipiaobao.ui.mine.data.ChangeSkin
import com.fenghuang.caipiaobao.ui.moments.MomentsFragment
import com.fenghuang.caipiaobao.widget.dialog.SystemNoticeDialog
import com.fenghuang.caipiaobao.widget.dialog.VersionDialog
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseContentFragment() {

    private val mFragments = arrayListOf<BaseFragment>()

    override fun getContentResID(): Int = R.layout.fragment_main

    override fun isRegisterRxBus() = true

    override fun initContentView() {
        mFragments.add(HomeFragment())
        mFragments.add(LotteryFragment())
        mFragments.add(BetFragment())
        mFragments.add(MomentsFragment())
        mFragments.add(MineFragment())
        loadMultipleRootFragment(R.id.mainContainer, 0,
                mFragments[0], mFragments[1], mFragments[2], mFragments[3], mFragments[4])
        //皮肤
        if (UserInfoSp.getSkinSelect() != 1) {
            skin2()
        }
    }

    override fun initEvent() {
        tabHome.setOnClickListener {
            showHideFragment(mFragments[0])
        }
        tabLotteryOpen.setOnClickListener {
            showHideFragment(mFragments[1])
        }
        rlCenter.setOnClickListener {
            tabBetting.isChecked = true
            showHideFragment(mFragments[2])
        }
        tabFriends.setOnClickListener {
            showHideFragment(mFragments[3])
        }
        tabMine.setOnClickListener {
            showHideFragment(mFragments[4])
        }
    }

    override fun initData() {
        checkDialog()
        getUpDate()
        getNotice()
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }


    /***
     * 回到主页面弹出一些列的窗口
     */
    private fun checkDialog() {
        // 权限弹窗
        RxPermissionHelper.request(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    }
    //更新
    private fun getUpDate() {
        HomeApi.getVersion {
            onSuccess {
                if (it.version_data != null) {
                    val dialog = VersionDialog(getPageActivity())
                    dialog.setContent(it.version_data?.upgradetext!!)
//                    if (it.version_data?.enforce == 1) {
//                        dialog.setCanceledOnTouchOutside(false)
//                        dialog.setCancelable(false)
//                    } else {
//                        dialog.setCanceledOnTouchOutside(true)
//                        dialog.setCancelable(true)
//                    }
                    dialog.setJum(it.version_data?.downloadurl!!)
                    dialog.show()
                }
            }
        }
    }
    //系统公告
    private fun getNotice(){
        HomeApi.getSystemNotice {
            onSuccess {
                if (it.content!=null){
                    val dialog = SystemNoticeDialog(getPageActivity())
                    dialog.setContent(it.content.toString())
                    dialog.show()
                }
            }
        }
    }

    /**
     * 接收Home头像点击事件
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun onClickMine(clickMine: HomeJumpToMine) {
        if (clickMine.jump) {
            tabMine.isChecked = true
            showHideFragment(mFragments[4])
        }
    }

    /**
     * 跳转购彩
     */
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun jumpToBuyLottery(eventBean: JumpToBuyLottery) {
        tabBetting.isChecked = true
        showHideFragment(mFragments[2])
    }

    //换肤
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        when (eventBean.id) {
            1 -> {
                skin()
            }
            2 -> {
                skin2()
            }
            else -> {
            }
        }

    }

    private fun skin() {
        tabHome.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.button_tab_home), null, null)
        tabLotteryOpen.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.button_tab_lottery), null, null)
        tabFriends.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.button_tab_quiz), null, null)
        tabMine.setCompoundDrawablesRelativeWithIntrinsicBounds(null, getDrawable(R.drawable.button_tab_mine), null, null)
        img_protruding.background = getDrawable(R.drawable.ic_tab)
    }

    private fun skin2() {
        val drawable1 = getDrawable(R.mipmap.ic_tab_new_year_1)
        val drawable2 = getDrawable(R.mipmap.ic_tab_new_year_2)
        val drawable3 = getDrawable(R.mipmap.ic_tab_new_year_3)
        val drawable4 = getDrawable(R.mipmap.ic_tab_new_year_4)
        drawable1?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(30))
        drawable2?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(30))
        drawable3?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(30))
        drawable4?.setBounds(0, 0, ViewUtils.dp2px(30), ViewUtils.dp2px(30))
        tabHome.setCompoundDrawables(null, drawable1, null, null)
        tabLotteryOpen.setCompoundDrawables(null, drawable2, null, null)
        tabFriends.setCompoundDrawables(null, drawable3, null, null)
        tabMine.setCompoundDrawables(null, drawable4, null, null)
        img_protruding.background = getDrawable(R.mipmap.ic_tab_new_year_5)

    }


}