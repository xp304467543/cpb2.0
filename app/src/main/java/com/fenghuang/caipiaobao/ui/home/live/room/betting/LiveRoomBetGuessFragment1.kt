package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter.GuessPlayChild2Adapter
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryBet
import com.fenghuang.caipiaobao.ui.lottery.data.PlayUnitData
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.fragment_guess2.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/4/22
 * @ Describe 投注列表选项
 *
 */


class LiveRoomBetGuessFragment1 : BaseNormalFragment() {
    private var adapter: GuessPlayChild2Adapter? = null
    private var playUnitData: PlayUnitData? = null

    override fun getLayoutRes() = R.layout.fragment_guess2

    override fun initView(rootView: View?) {}

    companion object {
        fun newInstance(playUnitData: PlayUnitData) = LiveRoomBetGuessFragment1().apply {
            arguments = Bundle(1).apply {
                putParcelable("playUnitData", playUnitData)
            }
        }
    }

    override fun onFragmentResume() {
        arguments?.let { playUnitData = it.getParcelable("playUnitData") }
        playUnitData?.apply {
            adapter = context?.let { GuessPlayChild2Adapter(it) }
            rv_guess_play_child?.layoutManager = object : GridLayoutManager(context, 4, LinearLayoutManager.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            rv_guess_play_child.setHasFixedSize(true)
            rv_guess_play_child.layoutAnimation = LayoutAnimationController(AnimationUtils.loadAnimation(context, R.anim.rv_animate))
            rv_guess_play_child.adapter = adapter
            adapter?.addAll(play_sec_data)
            adapter?.setOnItemClickListener { data, position ->
                if (!play_sec_data[position].isSelected) {
                    play_sec_data[position].isSelected = true
                    adapter?.notifyDataSetChanged()
                } else {
                    play_sec_data[position].isSelected = false
                    adapter?.notifyDataSetChanged()
                }
                RxBus.get().post(LotteryBet(data))
            }
        }
    }
}