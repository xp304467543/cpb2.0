package com.fenghuang.caipiaobao.ui.mine

import android.annotation.SuppressLint
import android.content.Intent
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.LoginOut
import com.fenghuang.caipiaobao.ui.home.data.UpDateUserPhoto
import com.fenghuang.caipiaobao.ui.login.LoginActivity
import com.fenghuang.caipiaobao.ui.login.data.LoginSuccess
import com.fenghuang.caipiaobao.ui.mine.children.*
import com.fenghuang.caipiaobao.ui.mine.data.ChangeSkin
import com.fenghuang.caipiaobao.ui.mine.data.MineUserDiamond
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.dialog.DiamondDialog
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * @ Author  QinTian
 * @ Date  2020-02-13
 * @ Describe 我的界面
 */

class MineFragment : BaseMvpFragment<MinePresenter>() {

    //新消息
    var msg1 = ""
    var msg2 = ""
    var msg3 = ""

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MinePresenter()

    override fun getLayoutResID() = R.layout.fragment_mine

    override fun isRegisterRxBus() = true


    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
        ImageManager.loadImg(UserInfoSp.getUserPhoto(), imgMineUserAvatar)
        //皮肤
        if (UserInfoSp.getSkinSelect()!=1){
            skin2()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSupportVisible() {
        if (UserInfoSp.getIsLogin()) {
            tvMineUserNickName.text = UserInfoSp.getUserNickName()
            ImageManager.loadImg(UserInfoSp.getUserPhoto(), imgMineUserAvatar)
            tvMineUserId.text = "ID: " + UserInfoSp.getUserUniqueId()
            val fans = UserInfoSp.getUserFans()?.split(",")
            tvMineUserOther.text = fans!![0] + "关注   |   " + fans[1] + "粉丝   |   " + fans[2] + "获赞"
            if (!UserInfoSp.getUserProfile().isNullOrEmpty() || UserInfoSp.getUserProfile() != "null") tvMineProfile.text = UserInfoSp.getUserProfile()
            setVisible(containerLogin)
            setGone(containerNoLogin)
            mPresenter.getUserVip()
            mPresenter.getUserBalance()
            mPresenter.getUserDiamond()
            mPresenter.getNewMsg()
            if (!UserInfoSp.getIsSetPayPassWord()) mPresenter.getIsSetPayPassWord()
            setVisible(containerSetting)
        } else {
            setGone(containerLogin)
            setGone(containerSetting)
            setVisible(containerNoLogin)
            tvBalance.text = "0.00"
            tvDiamondBalance.text = "0"
        }
    }

    override fun initEvent() {
        //登录
        tvLogin.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                startActivity(Intent(activity, LoginActivity::class.java))

            }
        }
        //存款
        tvDepositMoney.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (FastClickUtils.isFastClick()) {
                LaunchUtils.startRechargePage(requireActivity(), 0)
            }
        }
        //取款
        tvDrawMoney.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (FastClickUtils.isFastClick()) {
                LaunchUtils.startRechargePage(requireActivity(), 1)
            }
        }
        //意见反馈
        containerFeedBack.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (FastClickUtils.isFastClick()) {
                LaunchUtils.startFragment(requireActivity(), MineFeedBackFragment())
            }
        }
        //联系客服
        containerContactCustomer.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                LaunchUtils.startFragment(requireActivity(), MineContactCustomerFragment())
            }
        }
        //主播招募
        containerAnchorGet.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                LaunchUtils.startFragment(requireActivity(), MineAnchorRecruit())
            }
        }
        //主题皮肤
        containerMainSkin.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                LaunchUtils.startFragment(requireActivity(), MineFragmentThemSkin())
            }
        }
        //用户资料
        imgPersonal.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (FastClickUtils.isFastClick()) {
                when {
                    UserInfoSp.getUserType() == "0" -> {
                        LaunchUtils.startPersonalPage(requireActivity(), ""+UserInfoSp.getUserId(),1)
                    }
                    UserInfoSp.getUserType() == "1" -> {
                        LaunchUtils.startPersonalPage(requireActivity(), ""+UserInfoSp.getUserId(),2)
                    }
                    UserInfoSp.getUserType() == "3" -> {
                        LaunchUtils.startPersonalPage(requireActivity(), ""+UserInfoSp.getUserId(),3)
                    }
                    else -> LaunchUtils.startPersonalPage(requireActivity(), ""+UserInfoSp.getUserId(),1)
                }

            }
        }
        imgMineUserAvatar.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (FastClickUtils.isFastClick()) {
                LaunchUtils.startFragment(requireActivity(), MinePersonalFragment())
            }
        }
        //设置
        containerSetting.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (FastClickUtils.isFastClick()) {
                LaunchUtils.startFragment(requireActivity(), MineSettingFragment())
            }
        }
        //钻石兑换
        tvChangeDiamond.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (!UserInfoSp.getIsSetPayPassWord()) {
                GlobalDialog.noSetPassWord(requireActivity())
                return@setOnClickListener
            }
            if (FastClickUtils.isFastClick()) {
                val dialog = DiamondDialog(requireContext(), tvBalance.text.toString())
                dialog.show()
            }
        }
        //刷新余额
        tvRefreshMooney.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (FastClickUtils.isFastClick()) {
                mPresenter.getUserBalance()
            }
        }

        //关注
        tvAttention.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (FastClickUtils.isFastClick()) {
                LaunchUtils.startFragment(requireActivity(), MineAttentionFragment())
            }
        }
        //账单
        containerMineCheck.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (FastClickUtils.isFastClick()) {
                LaunchUtils.startFragment(requireActivity(), MineBillFragment())
            }
        }
        //消息
        containerMessageCenter.setOnClickListener {
            if (!UserInfoSp.getIsLogin()) {
                GlobalDialog.notLogged(requireActivity())
                return@setOnClickListener
            }
            if (FastClickUtils.isFastClick()) {
                setGone(tvNewMsg)
               imgMessage.background = ViewUtils.getDrawable(R.mipmap.ic_mine_massage)
                LaunchUtils.startFragment(requireActivity(), MineMessageCenterFragment.newInstance(msg1, msg2, msg3))
            }
        }

    }

    //更新用户头像
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataUserAvatar(eventBean: UpDateUserPhoto) {
        ImageManager.loadImg(eventBean.img, imgMineUserAvatar)
        UserInfoSp.putUserPhoto(eventBean.img)
    }

    //更新钻石 顺便更新余额
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataMineUserDiamond(eventBean: MineUserDiamond) {
        tvDiamondBalance.text = eventBean.diamond
        mPresenter.getUserBalance()
    }

    //退出登录
    @SuppressLint("SetTextI18n")
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun LoginOut(eventBean: LoginOut) {
        setGone(containerLogin)
        setGone(containerSetting)
        setVisible(containerNoLogin)
        tvBalance.text = "0.00"
        tvDiamondBalance.text = "0"
    }


    //登陆成功
    @SuppressLint("SetTextI18n")
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun upDataMineUserDiamond(eventBean: LoginSuccess) {
        tvMineUserNickName.text = UserInfoSp.getUserNickName()
        ImageManager.loadImg(UserInfoSp.getUserPhoto(), imgMineUserAvatar)
        tvMineUserId.text = "ID: " + UserInfoSp.getUserUniqueId()
        val fans = UserInfoSp.getUserFans()?.split(",")
        tvMineUserOther.text = fans!![0] + "关注   |   " + fans[1] + "粉丝   |   " + fans[2] + "获赞"
        if (!UserInfoSp.getUserProfile().isNullOrEmpty() || UserInfoSp.getUserProfile() != "null") tvMineProfile.text = UserInfoSp.getUserProfile()
        setVisible(containerLogin)
        setGone(containerNoLogin)
        mPresenter.getUserVip()
        mPresenter.getUserBalance()
        mPresenter.getUserDiamond()
        mPresenter.getNewMsg()
        if (!UserInfoSp.getIsSetPayPassWord()) mPresenter.getIsSetPayPassWord()
        setVisible(containerSetting)
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
            else -> {}
        }

    }


    private fun skin(){
        imgChangeDiamon.background = getDrawable(R.mipmap.ic_mine_top)
        imgMessage.background = getDrawable(R.mipmap.ic_mine_massage)
        imgCheck.background = getDrawable(R.mipmap.ic_mine_check_menu)
        imgSkin.background = getDrawable(R.mipmap.ic_mine_skin)
        imgAnchor.background = getDrawable(R.mipmap.ic_mine_anchor_get)
        imgFeedBack.background = getDrawable(R.mipmap.ic_mine_suggest)
        imgCustomer.background = getDrawable(R.mipmap.ic_mine_customer)
        imgSetting.background = getDrawable(R.mipmap.ic_mine_setting)
    }

    private fun skin2(){
        imgChangeDiamon.background = getDrawable(R.mipmap.ic_skin_new_year)
        imgMessage.background = getDrawable(R.mipmap.ic_skin_1)
        imgCheck.background = getDrawable(R.mipmap.ic_skin_2)
        imgSkin.background = getDrawable(R.mipmap.ic_skin_3)
        imgAnchor.background = getDrawable(R.mipmap.ic_skin_4)
        imgFeedBack.background = getDrawable(R.mipmap.ic_skin_5)
        imgCustomer.background = getDrawable(R.mipmap.ic_skin_6)
        imgSetting.background = getDrawable(R.mipmap.ic_skin_7)
    }

}
