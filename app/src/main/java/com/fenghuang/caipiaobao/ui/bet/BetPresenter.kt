package com.fenghuang.caipiaobao.ui.bet

import android.annotation.SuppressLint
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.home.data.LineCheck
import com.fenghuang.caipiaobao.utils.NetPingManager
import kotlinx.android.synthetic.main.fragment_bet.*


/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/12- 12:21
 * @ Describe
 *
 */

class BetPresenter : BaseMvpPresenter<BetFragment>() {


    var mLDNetPingService: NetPingManager? = null


    @SuppressLint("SetTextI18n")
    fun getUrl(id: Int) {
        HomeApi.getLotteryUrl {
            onSuccess {
                if (mView.isActive()) {
                    mView.baseUrl = it.betting

                    if (id == 0) {
                        if (it.bettingArr.isNullOrEmpty()) {
                            ToastUtils.show("暂无线路")
                            return@onSuccess
                        }
                    }
                    if (id == 1) {
                        if (it.chessArr.isNullOrEmpty()) {
                            ToastUtils.show("暂无线路")
                            return@onSuccess
                        }
                    }
                    val realUrl: String
                    if (id == 0) {
                        val str = it.bettingArr!![0].indexOf("//")
                        realUrl = it.bettingArr!![0].substring(str + 2, it.bettingArr!![0].length)
                        mView.baseBetWebView.loadUrl(it.bettingArr!![0])
                        mView.lineList = it.bettingArr
                    } else {
                        val str = it.chessArr!![0].indexOf("//")
                        realUrl = it.chessArr!![0].substring(str + 2, it.chessArr!![0].length)
                        mView.baseBetWebView.loadUrl(it.chessArr!![0])
                        mView.lineList = it.chessArr
                    }
                    mView.listCheck = arrayListOf()
                    for ((index, it) in mView.lineList!!.withIndex()) {
                        val check = if (index == 0) {
                            LineCheck(it, true)
                        } else LineCheck(it)
                        mView.listCheck?.add(check)
                    }
                    mLDNetPingService = NetPingManager(mView.context, realUrl, object : NetPingManager.IOnNetPingListener {
                        override fun ontDelay(log: Long) {
                            mLDNetPingService?.release()
                            mView.post {
                                mView.tvLineDelay.text = (log / 5).toString() + "ms"
                                if ((log / 5) > 100) {
                                    mView.setTextColor(R.id.tvLineDelay, ViewUtils.getColor(R.color.colorYellow))
                                } else {
                                    mView.setTextColor(R.id.tvLineDelay, ViewUtils.getColor(R.color.colorGreen))
                                }
                            }
                        }

                        override fun onError() {
                            mLDNetPingService?.release()
                        }

                    })
                    mLDNetPingService?.getDelay()

                }
            }
            onFailed { }
        }
    }


}