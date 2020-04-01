package com.fenghuang.caipiaobao.ui.lottery.children

import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.caipiaobao.ui.lottery.constant.LotteryConstant
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import kotlinx.android.synthetic.main.child_fragment_history_open.*
import kotlinx.android.synthetic.main.child_fragment_lu_zhu.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-02
 * @ Describe
 *
 */

class LotteryLuZhuFragmentPresenter : BaseMvpPresenter<LotteryLuZhuFragment>() {


    //获取露珠数据
    fun getLuZhuData(lotteryId: String, typeTitle: String,time:String = TimeUtils.getToday()) {
        mView.setVisible(mView.tvLuZhuPlaceHolder)
        LotteryApi.getLotteryLuZhu(lotteryId, typeTitle,time ) {
            onSuccess {
                if (mView.isActive()) {
                    mView.getLuZhuView(it,typeTitle)
                }
            }
            onFailed {
                if (mView.isActive()){
                    mView.setVisible(mView.tvHolder)
                    mView. setGone(mView.tvLuZhuPlaceHolder)
                }
            }
        }
    }


     fun getType(typeTitle: String): String {
        return when (typeTitle) {
            LotteryConstant.TYPE_2 -> LotteryConstant.TYPE_LUZHU_2
            LotteryConstant.TYPE_3 -> LotteryConstant.TYPE_LUZHU_3
            LotteryConstant.TYPE_5 -> LotteryConstant.TYPE_LUZHU_5
            LotteryConstant.TYPE_8 -> LotteryConstant.TYPE_LUZHU_8
            LotteryConstant.TYPE_9 -> LotteryConstant.TYPE_LUZHU_9
            LotteryConstant.TYPE_10 -> LotteryConstant.TYPE_LUZHU_10
            LotteryConstant.TYPE_11 -> LotteryConstant.TYPE_LUZHU_11
            LotteryConstant.TYPE_15 -> LotteryConstant.TYPE_LUZHU_15
            LotteryConstant.TYPE_16 -> LotteryConstant.TYPE_LUZHU_16
            LotteryConstant.TYPE_12 -> LotteryConstant.TYPE_LUZHU_12
            else -> "daxiao"
        }

    }

}