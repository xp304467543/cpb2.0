package com.fenghuang.caipiaobao.ui.home.live.room
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.lottery.data.LotteryApi
import com.fenghuang.caipiaobao.widget.dialog.bottom.BaseBottomSheetFragment
/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-07
 * @ Describe 直播间投注
 *
 */

class LiveRoomBottomBetFragment(override val layoutResId: Int = R.layout.dialog_live_bet) : BaseBottomSheetFragment() {

    var spanner: Spinner? = null

    override fun initView() {
        rootView?.findViewById<ImageView>(R.id.imgBetCLose)?.setOnClickListener {
            dismiss()
        }
        rootView?.findViewById<View>(R.id.tvLiveBetClose)?.setOnClickListener {
            dismiss()
        }
        spanner = rootView?.findViewById(R.id.spLotteryType)
    }


    override fun initData() {
        val type = LotteryApi.getLotteryType()
        type.onSuccess {
            val title = arrayListOf<String>()
            for (data in it) {
                title.add(data.cname)
            }
          if (!title.isNullOrEmpty())  initSpanner(title)
        }
    }

    private fun initSpanner(data: List<String>) {
        val spAdapter = mContext?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, data) }
        spAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spanner?.adapter = spAdapter

    }

}