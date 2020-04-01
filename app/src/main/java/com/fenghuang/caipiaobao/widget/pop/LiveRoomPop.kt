package com.fenghuang.caipiaobao.widget.pop

import android.content.Context
import android.widget.TextView
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.popup.BasePopupWindow
import com.fenghuang.caipiaobao.R


/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-22
 * @ Describe 普通用户 pop
 *
 */

class LiveRoomPop(context: Context) : BasePopupWindow(context,  R.layout.pop_live_room) {

    var tvManageClear: TextView
    var tvClearChat: TextView
    var tvCloseVideo: TextView

    init {
        width = ViewUtils.dp2px(104)
        height =  ViewUtils.dp2px(89)
        tvManageClear = findView(R.id.tvManageClear)
        tvClearChat = findView(R.id.tvClearChat)
        tvCloseVideo = findView(R.id.tvCloseVideo)
    }


}