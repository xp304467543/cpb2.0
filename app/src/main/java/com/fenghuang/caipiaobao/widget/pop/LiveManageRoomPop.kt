package com.fenghuang.caipiaobao.widget.pop

import android.content.Context
import android.widget.TextView
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.popup.BasePopupWindow
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import kotlinx.android.synthetic.main.dialog_live_room_chat.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-22
 * @ Describe 管理员 pop
 *
 */

class LiveManageRoomPop(context: Context) : BasePopupWindow(context, R.layout.pop_manage_live_room) {

    var tvmanageclearV: TextView
    var tvClearChatV: TextView
    var tvCloseVideoV: TextView

    var click: Boolean = false

    init {
        width = ViewUtils.dp2px(104)
        if (UserInfoSp.getUserType() == "2") height = ViewUtils.dp2px(133)
        else {
            height =  ViewUtils.dp2px(89)

        }
        tvmanageclearV = findView(R.id.tvManageClear_v)
        tvClearChatV = findView(R.id.tvClearChat_v)
        tvCloseVideoV = findView(R.id.tvCloseVideo_v)
        initEvent()
    }
    private var mSendListener: ((it: Boolean) -> Unit)? = null
    private var mClearListener: (() -> Unit)? = null
    private var mManagerClearListener: (() -> Unit)? = null
    fun initEvent() {
        tvCloseVideoV.setOnClickListener {
            if (click) {
                click = false
                tvCloseVideoV.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.mipmap.ic_live_close), null, null, null)
                tvCloseVideoV.text = "收起视频"
            } else {
                click = true
                tvCloseVideoV.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.mipmap.ic_live_open), null, null, null)
                tvCloseVideoV.text = "展开视频"
            }
            mSendListener?.invoke(click)
            dismiss()
        }

        tvClearChatV.setOnClickListener {
            mClearListener?.invoke()
            dismiss()
        }
        tvmanageclearV.setOnClickListener {
            mManagerClearListener?.invoke()
            dismiss()
        }
    }

    //收起视频
    fun setSendClickListener(listener: (it: Boolean) -> Unit) {
        mSendListener = listener
    }
    //直播间超管清屏
    fun setClearClickListener(listener: () -> Unit) {
        mClearListener = listener
    }

    //管理清屏
    fun setManagerClearClickListener(listener: () -> Unit) {
        mManagerClearListener = listener
    }
}