package com.fenghuang.caipiaobao.ui.bet

import android.annotation.SuppressLint
import android.os.Handler
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.home.data.LineCheck
import com.fenghuang.caipiaobao.utils.NetPingManager
import kotlinx.android.synthetic.main.fragment_bet.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.regex.Matcher
import java.util.regex.Pattern


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
    fun getUrl() {
        HomeApi.getLotteryUrl {
            onSuccess {
                if (mView.isActive()) {
                    mView.baseUrl = it.betting
                    if (it.bettingArr != null) {
                        val str = it.bettingArr!![0].indexOf("//")
                        val realUrl = it.bettingArr!![0].substring(str + 2, it.bettingArr!![0].length)
                        mView.baseBetWebView.loadUrl(it.bettingArr!![0])
                        mView.lineList = it.bettingArr
                        if (mView.listCheck.isNullOrEmpty()) {
                            mView.listCheck = arrayListOf()
                            for ((index, it) in mView.lineList!!.withIndex()) {
                                val check = if (index == 0) {
                                    LineCheck(it, true)
                                } else LineCheck(it)
                                mView.listCheck?.add(check)
                            }
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
            }
            onFailed { }
        }
    }



}