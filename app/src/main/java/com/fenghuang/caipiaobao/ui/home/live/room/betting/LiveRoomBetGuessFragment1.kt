package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter.GuessPlay1Adapter
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryBet
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryReset
import com.fenghuang.caipiaobao.ui.lottery.data.PlayUnitData
import com.fenghuang.caipiaobao.widget.BaseNormalFragment
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_guess2.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/4/22
 * @ Describe 投注列表选项
 *
 */


class LiveRoomBetGuessFragment1 : BaseNormalFragment() {
    private var adapter: GuessPlay1Adapter? = null
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

            rv_guess_play_child?.layoutManager = object : GridLayoutManager(context, 4, LinearLayoutManager.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = GuessPlay1Adapter(play_sec_data)
            rv_guess_play_child.setHasFixedSize(true)
            rv_guess_play_child.adapter = adapter
            rv_guess_play_child.setHasFixedSize(true)
            adapter?.openLoadAnimation(BaseQuickAdapter.SCALEIN)
            adapter?.bindToRecyclerView(rv_guess_play_child)
            adapter?.setOnItemClickListener { _, _, position ->
                if (!play_sec_data[position].isSelected) {
                    play_sec_data[position].isSelected = true
                    adapter?.notifyDataSetChanged()
                } else {
                    play_sec_data[position].isSelected = false
                    adapter?.notifyDataSetChanged()
                }
                RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
            }
        }
    }


    //重置所有状态
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryBet(eventBean: LotteryReset) {
        if (isAdded && eventBean.reset) {
            if (!adapter?.data.isNullOrEmpty()){
                for (it in adapter?.data!!){
                    if (it.isSelected){
                        it.isSelected = false
                    }
                }
                adapter?.notifyDataSetChanged()
            }
        }
    }
}