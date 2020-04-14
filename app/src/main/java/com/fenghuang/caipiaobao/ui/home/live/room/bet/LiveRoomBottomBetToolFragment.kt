package com.fenghuang.caipiaobao.ui.home.live.room.bet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fenghuang.baselib.base.fragment.BaseContentFragment
import com.fenghuang.caipiaobao.R

/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-13
 * @ Describe
 *
 */

class LiveRoomBottomBetToolFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate( R.layout.fragment_live_bet_tool,container,false)
    }
}