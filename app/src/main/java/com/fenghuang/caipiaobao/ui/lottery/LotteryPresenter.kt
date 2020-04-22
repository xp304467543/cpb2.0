package com.fenghuang.caipiaobao.ui.lottery

import android.annotation.SuppressLint
import android.os.Handler
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryTypeSelectUtil
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryExpertPlay
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryNewCode
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryTypeSelect
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.fragment_lottery.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/1- 12:51
 * @ Describe
 *
 */

class LotteryPresenter : BaseMvpPresenter<LotteryFragment>() {


    //获取彩种
    fun getLotteryType() {
        if (mView.isActive()) {
            val uiScope = CoroutineScope(Dispatchers.Main)

            uiScope.launch {

                val getLotteryType = async { LotteryApi.getLotteryType()}

                val resultGetLotteryType = getLotteryType.await()

                resultGetLotteryType.onSuccess {
                    if (it.isNotEmpty()) {
                        mView.lotteryTypeAdapter!!.clear()
                        mView.lotteryTypeAdapter!!.addAll(it)

                    }
                }
            }
        }
    }

    //初始化开奖结果
    var handler: Handler? = null
    var runnable: Runnable? = null
    var isPost = false  //判断是不是第一次 防止多一条数据
    @SuppressLint("SetTextI18n")
    fun getLotteryOpenCode(lotteryId: String) {
        if (handler != null && runnable != null) handler!!.removeCallbacks(runnable!!)
        LotteryApi.getLotteryNewCode(lotteryId) {
            onSuccess {
                if (mView.isActive()) {
                    if (it.next_lottery_time.toInt() > 1) {
                        mView.tvOpenCount.text = it.issue + " 期开奖结果"
                        mView.countDownTime(it.next_lottery_time, lotteryId)
                        //更新最新开奖数据
                        if (isPost) {
                            RxBus.get().post(LotteryNewCode(it))
                            isPost = false
                        } else RxBus.get().post(LotteryExpertPlay(it))
                        when (lotteryId) {
                            "8" -> {
                                val tbList = it.code?.split(",") as ArrayList<String>
                                tbList.add(6, "+")
                                LotteryTypeSelectUtil.addOpenCode(mView.requireActivity(), mView.linLotteryOpenCode, tbList, it.lottery_id)
                            }
                            else -> {
                                LotteryTypeSelectUtil.addOpenCode(mView.requireActivity(), mView.linLotteryOpenCode, it.code?.split(","), it.lottery_id)
                            }
                        }
                        mView.setGone(mView.tvOpenCodePlaceHolder)
                    } else {
                        if (mView.timer != null) mView.timer?.cancel()
                        if (mView.isSupportVisible){
                            mView.tvOpenTime.text = "开奖中..."
                            mView.tvAtNext.text = mView.getString(R.string.lottery_next)
                            mView.tvOpenCount.text = it.issue + " 期开奖结果"
                        }
                        handler = Handler()
                        runnable = Runnable {
                            isPost = true
                            getLotteryOpenCode(it.lottery_id)
                        }
                        if (!isPost)RxBus.get().post(LotteryExpertPlay(it))
                        handler?.postDelayed(runnable, 5000)
                    }
                }
            }
        }
    }
}