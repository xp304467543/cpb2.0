package com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.fenghuang.caipiaobao.ui.home.live.room.betting.LiveRoomBetGuessFragment
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryPlayListResponse

/**
 *
 * @ Author  QinTian
 * @ Date  2020/4/22
 * @ Describe
 *
 */
class LiveBetStateAdapter (fm: FragmentManager, private val playList :ArrayList<LotteryPlayListResponse>) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {

        return LiveRoomBetGuessFragment.newInstance(playList[position])
    }

    override fun getCount(): Int {
        return playList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return playList[position].play_unit_name
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        try {
            super.restoreState(state, loader)
        } catch (e: NullPointerException) {
        }
    }
}