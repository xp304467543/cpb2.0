package com.fenghuang.caipiaobao.ui.mine

import android.annotation.SuppressLint
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.hwangjr.rxbus.RxBus
import com.pingerx.rxnetgo.exception.ApiException
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_mine_presonal.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-13
 * @ Describe
 *
 */

class MinePresenter : BaseMvpPresenter<MineFragment>() {


    fun getUserVip() {
        MineApi.getUserVip {
            if (mView.isActive()) {
                onSuccess {
                    UserInfoSp.setVipLevel(it.vip)
                    when (it.vip) {
                        "1" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_1)
                        "2" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_2)
                        "3" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_3)
                        "4" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_4)
                        "5" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_5)
                        "6" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_6)
                        "7" -> mView.imgMineLevel.setBackgroundResource(R.mipmap.vip_7)
                        else -> mView.imgMineLevel.setBackgroundResource(0)
                    }
                    mView.setVisible(mView.imgMineLevel)
                }
                onFailed {
                    mView.setGone(mView.imgMineLevel)
                    UserInfoSp.setVipLevel("0")
                }
            }
        }
    }


    //获取余额
    @SuppressLint("SetTextI18n")
    fun getUserBalance() {
        if (mView.isActive()) {
            MineApi.getUserBalance {
                onSuccess {
                    mView.tvBalance.text = it.balance.toString()
//                    mView.setBalance(it.balance.toString())
                }
                onFailed {
                    GlobalDialog.showError(mView.requireActivity(), it)
                }
            }
        }

    }


    private var getUserDiamondSuccessListener: ((it: String) -> Unit)? = null
    private var getUserDiamondFailedListener: ((it: ApiException) -> Unit)? = null

    fun getUserDiamondSuccessListener(CommentSuccessListener: ((it: String) -> Unit)) {
        getUserDiamondSuccessListener = CommentSuccessListener
    }

    fun getUserDiamondFailedListener(CommentFailedListener: ((it: ApiException) -> Unit)) {
        getUserDiamondFailedListener = CommentFailedListener
    }

    //获取钻石
    fun getUserDiamond() {
        MineApi.getUserDiamond {
            onSuccess {
                RxBus.get().post(it)
                getUserDiamondSuccessListener?.invoke(it.diamond)
            }
            onFailed {
                getUserDiamondFailedListener?.invoke(it)
            }
        }
    }

    //查询是否设置支付密码
    fun getIsSetPayPassWord() {
        MineApi.getIsSetPayPass {
            onSuccess {
                UserInfoSp.putIsSetPayPassWord(true)
            }
        }
    }

    //获取用户信息
    @SuppressLint("SetTextI18n")
    fun getUserInfo() {
        MineApi.getUserInfo {
            onSuccess {
                if (mView.isSupportVisible) {
                    mView.tvMineUserOther.text =it.following + "关注   |   " + it.followers + "粉丝   |   " + it.like + "获赞"
                    if (!UserInfoSp.getUserProfile().isNullOrEmpty() || UserInfoSp.getUserProfile() != "null")
                    mView.tvMineProfile.text = it.profile
                }
            }
        }

    }


    //获取新消息
    fun getNewMsg() {
        MineApi.getIsNewMessage {
            if (mView.isActive()) {
                onSuccess {
                    if (it.msgCount > 0) {
                        mView.setVisible(mView.tvDian)
                    } else {
                        mView.setGone(mView.tvDian)
                    }
                    mView.msg1 = it.countList.`_$0`
                    mView.msg2 = it.countList.`_$2`
                    mView.msg3 = it.countList.`_$3`
                }
            }
        }
    }


}