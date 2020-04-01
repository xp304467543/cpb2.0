package com.fenghuang.caipiaobao.ui.home.live

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.home.data.*
import com.fenghuang.caipiaobao.ui.home.live.room.LiveRoomChaPresenterHelper
import com.fenghuang.caipiaobao.ui.login.data.LoginApi
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.JsonUtils
import com.fenghuang.caipiaobao.widget.dialog.PassWordDialog
import com.google.gson.JsonParser
import com.hwangjr.rxbus.RxBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-16
 * @ Describe
 *
 */
class LiveRoomPresenter : BaseMvpPresenter<LiveRoomActivity>() {


    fun getAllData(anchorID: String) {
        if (mView.isActive()) {
            HomeApi.enterLiveRoom(anchorId = anchorID){
                onSuccess {
                    mView.initAttention(it)
                    mView.hidePageLoadingDialog()
                    mView.attention(it.isFollow)
                    getIsFirstRecharge()
                    getUserVip()
                }
                onFailed {
                    mView.hidePageLoadingDialog()
                }
            }
        }
    }

    /**
     * 获取首冲
     */
    private fun getIsFirstRecharge() {
        LoginApi.getIsFirstRecharge(UserInfoSp.getUserId()) {
            onSuccess {
                if (it.isfirst == "0") {
                    UserInfoSp.putIsFirstRecharge(true)
                    RxBus.get().post(IsFirstRecharge(true))
                } else {
                    UserInfoSp.putIsFirstRecharge(false)
                    RxBus.get().post(IsFirstRecharge(false))
                }
            }
            onFailed {
                GlobalDialog.ShowError(mView, it)
                RxBus.get().post(IsFirstRecharge(true))
            }
        }
    }

    //vip等级
    private fun getUserVip() {
        MineApi.getUserVip {
            if (mView.isActive()) {
                onSuccess {
                    UserInfoSp.setVipLevel(it.vip)
                    RxBus.get().post(EnterVip(it.vip))
                }
                onFailed {
                    UserInfoSp.setVipLevel("0")
                }
            }
        }
    }

    //获取礼物列表
    fun getGiftList() {
        HomeApi.getGiftList {
            if (mView.isActive()) {
                onSuccess { result ->
                    val json = JsonParser().parse(result).asJsonObject
                    val typeList = json.get("typeList").asJsonObject
                    val data = json.get("data").asJsonObject
                    val type = arrayListOf<String>()
                    val content = ArrayList<List<HomeLiveGiftList>>()
                    repeat(typeList.size()) {
                        val realPosition = (it + 1).toString()
                        type.add(typeList.get(realPosition).asString)
                        val res = data.get(realPosition).asJsonArray
                        val bean = arrayListOf<HomeLiveGiftList>()
                        for (op in res) {
                            val beanData = JsonUtils.fromJson(op, HomeLiveGiftList::class.java)
                            bean.add(beanData)
                        }
                        content.add(bean)
                    }
                    if (mView.bottomHorGiftWindow != null) {
                        mView.bottomHorGiftWindow!!.setData(type, content)
                    }
                }
                onFailed {
                    GlobalDialog.ShowError(mView, it)
                }
            }
        }
    }


    //发红包
    fun homeLiveSendRedEnvelope(anchorId: String, amount: String, num: String, text: String, password: String, passWordDialog: PassWordDialog) {
        HomeApi.homeLiveSendRedEnvelope(anchorId, amount, num, text, password) {
            onSuccess {
                //通知socket
                RxBus.get().post(Gift(LiveRoomChaPresenterHelper.getGifParams(anchorId, "4", it.rid, "", amount, num, text, "", "")))
                passWordDialog.showOrHideLoading()
                passWordDialog.dismiss()
                ToastUtils.show("红包发送成功")
            }
            onFailed {
                passWordDialog.showOrHideLoading()
                passWordDialog.dismiss()
                GlobalDialog.ShowError(mView, it,true)
            }
        }
    }



}