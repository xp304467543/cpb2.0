package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter.GuessPlay1Adapter
import com.fenghuang.caipiaobao.ui.lottery.data.*
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

     var moreSelect: ArrayList<Bet2Bean>? = null

     var threeSelect: ArrayList<Bet2Bean>? = null
    private var singleSelect1: ArrayList<Bet2Bean>? = null
    private var singleSelect2: ArrayList<Bet2Bean>? = null
    private var singleSelect3: ArrayList<Bet2Bean>? = null
    private var singleSelect4: ArrayList<Bet2Bean>? = null
    private var singleSelect5: ArrayList<Bet2Bean>? = null

    override fun getLayoutRes() = R.layout.fragment_guess2

    override fun initView(rootView: View?) {

    }

    companion object {
        fun newInstance(playUnitData: PlayUnitData) = LiveRoomBetGuessFragment1().apply {
            arguments = Bundle(1).apply {
                putParcelable("playUnitData", playUnitData)
            }
        }
    }


    override fun onFragmentResume() {
        if (moreSelect != null) moreSelect?.clear()
        if (threeSelect != null) threeSelect?.clear()
        arguments?.let { playUnitData = it.getParcelable("playUnitData") }
        playUnitData?.apply {

            rv_guess_play_child?.layoutManager = object : GridLayoutManager(context, 4, LinearLayoutManager.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            RxBus.get().post(LotteryCurrent(this.play_sec_cname,0))
            adapter = GuessPlay1Adapter(play_sec_data)
            rv_guess_play_child.adapter = adapter
            rv_guess_play_child.setHasFixedSize(true)
            adapter?.openLoadAnimation(BaseQuickAdapter.SCALEIN)
            adapter?.bindToRecyclerView(rv_guess_play_child)
            adapter?.setOnItemClickListener { _, _, position ->
                when (this.play_sec_cname) {
                    "二中二" -> {
                        if (moreSelect == null) moreSelect = arrayListOf()
                        if (!play_sec_data[position].isSelected) {
                            moreSelect?.add(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            if (moreSelect?.size!! > 2) {
                                moreSelect?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                                ToastUtils.show("二中二最多选2个号码")
                            } else {
                                play_sec_data[position].isSelected = true
                                adapter?.notifyDataSetChanged()
                                if (moreSelect?.size == 2) {
                                    val data = PlaySecData(moreSelect!![0].play_class_cname + "," + moreSelect!![1].play_class_cname, play_sec_data[position].play_class_id, play_sec_data[position].play_sec_name,
                                            moreSelect!![0].play_class_name + "," + moreSelect!![1].play_class_name, play_sec_data[position].play_odds, isSelected = true)
                                    val bean = LotteryBet(data, this.play_sec_cname)
                                    RxBus.get().post(bean)
                                }
                            }
                        } else {
                            moreSelect?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            play_sec_data[position].isSelected = false
                            adapter?.notifyDataSetChanged()
                            RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
                        }
                        RxBus.get().post(LotteryCurrent(this.play_sec_cname,moreSelect?.size?:0))
                    }
                    "三中三" -> {
                        if (threeSelect == null) threeSelect = arrayListOf()
                        if (!play_sec_data[position].isSelected) {
                            threeSelect?.add(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            if (threeSelect?.size!! > 3) {
                                threeSelect?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                                ToastUtils.show("三中三最多选3个号码")
                            } else {
                                play_sec_data[position].isSelected = true
                                adapter?.notifyDataSetChanged()
                                if (threeSelect?.size == 3) {
                                    val data = PlaySecData(threeSelect!![0].play_class_cname + "," + threeSelect!![1].play_class_cname + "," + threeSelect!![2].play_class_cname, play_sec_data[position].play_class_id, play_sec_data[position].play_sec_name,
                                            threeSelect!![0].play_class_name + "," + threeSelect!![1].play_class_name + "," + threeSelect!![2].play_class_name, play_sec_data[position].play_odds, isSelected = true)
                                    val bean = LotteryBet(data, this.play_sec_cname)
                                    RxBus.get().post(bean)
                                }
                            }
                        } else {
                            threeSelect?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            play_sec_data[position].isSelected = false
                            adapter?.notifyDataSetChanged()
                            RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
                        }
                        RxBus.get().post(LotteryCurrent(this.play_sec_cname,threeSelect?.size?:0))
                    }
                    "一中一" -> {
                        if (singleSelect1 == null) singleSelect1 = arrayListOf()
                        if (!play_sec_data[position].isSelected) {
                            singleSelect1?.add(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            if (singleSelect1?.size!! > 1) {
                                singleSelect1?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                                ToastUtils.show("一中一最多选1个号码")
                            } else {
                                play_sec_data[position].isSelected = true
                                adapter?.notifyDataSetChanged()
                                RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
                            }
                        } else {
                            singleSelect1?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            play_sec_data[position].isSelected = false
                            adapter?.notifyDataSetChanged()
                            RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
                        }
                    }
                    "一中二" -> {
                        if (singleSelect2 == null) singleSelect2 = arrayListOf()
                        if (!play_sec_data[position].isSelected) {
                            singleSelect2?.add(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            if (singleSelect2?.size!! > 1) {
                                singleSelect2?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                                ToastUtils.show("一中二最多选1个号码")
                            } else {
                                play_sec_data[position].isSelected = true
                                adapter?.notifyDataSetChanged()
                                RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
                            }
                        } else {
                            singleSelect2?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            play_sec_data[position].isSelected = false
                            adapter?.notifyDataSetChanged()
                            RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
                        }

                    }
                    "一中三" -> {
                        if (singleSelect3 == null) singleSelect3 = arrayListOf()
                        if (!play_sec_data[position].isSelected) {
                            singleSelect3?.add(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            if (singleSelect3?.size!! > 1) {
                                singleSelect3?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                                ToastUtils.show("一中三最多选1个号码")
                            } else {
                                play_sec_data[position].isSelected = true
                                adapter?.notifyDataSetChanged()
                                RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
                            }
                        } else {
                            singleSelect3?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            play_sec_data[position].isSelected = false
                            adapter?.notifyDataSetChanged()
                            RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
                        }

                    }
                    "一中四" -> {
                        if (singleSelect4 == null) singleSelect4 = arrayListOf()
                        if (!play_sec_data[position].isSelected) {
                            singleSelect4?.add(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            if (singleSelect4?.size!! > 1) {
                                singleSelect4?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                                ToastUtils.show("一中四最多选1个号码")
                            } else {
                                play_sec_data[position].isSelected = true
                                adapter?.notifyDataSetChanged()
                                RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
                            }
                        } else {
                            singleSelect4?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            play_sec_data[position].isSelected = false
                            adapter?.notifyDataSetChanged()
                            RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
                        }

                    }
                    "一中五" -> {
                        if (singleSelect5 == null) singleSelect5 = arrayListOf()
                        if (!play_sec_data[position].isSelected) {
                            singleSelect5?.add(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            if (singleSelect5?.size!! > 1) {
                                singleSelect5?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                                ToastUtils.show("一中五最多选1个号码")
                            } else {
                                play_sec_data[position].isSelected = true
                                adapter?.notifyDataSetChanged()
                                RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
                            }
                        } else {
                            singleSelect5?.remove(Bet2Bean(play_sec_data[position].play_class_cname, play_sec_data[position].play_class_name))
                            play_sec_data[position].isSelected = false
                            adapter?.notifyDataSetChanged()
                            RxBus.get().post(LotteryBet(play_sec_data[position], this.play_sec_cname))
                        }

                    }

                    else -> {
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
        }
    }

    data class Bet2Bean(var play_class_cname: String, var play_class_name: String)


    //重置所有状态
    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun lotteryBet(eventBean: LotteryReset) {
        if (isAdded && eventBean.reset) {
            reset()
        }

    }

    fun reset(){
        if (moreSelect != null) moreSelect?.clear()
        if (threeSelect != null) threeSelect?.clear()
        if (singleSelect1 != null) singleSelect1?.clear()
        if (singleSelect2 != null) singleSelect2?.clear()
        if (singleSelect3 != null) singleSelect3?.clear()
        if (singleSelect4 != null) singleSelect4?.clear()
        if (singleSelect5 != null) singleSelect5?.clear()
            if (moreSelect != null) moreSelect?.clear()
            if (!adapter?.data.isNullOrEmpty()) {
                for (it in adapter?.data!!) {
                    if (it.isSelected) {
                        it.isSelected = false
                    }
                }
                adapter?.notifyDataSetChanged()
            }
    }
}