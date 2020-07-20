package com.fenghuang.caipiaobao.widget.pop

import android.content.Context
import android.widget.TextView
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.popup.BasePopupWindow
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.home.data.LiveAnimClose
import com.hwangjr.rxbus.RxBus
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
    var tvClear_anim: TextView
    var click: Boolean = false
    var clickAnim: Boolean = false

    init {
        width = ViewUtils.dp2px(104)
        height = if (UserInfoSp.getUserType() == "2") ViewUtils.dp2px(177)
        else {
            ViewUtils.dp2px(133)

        }
        tvmanageclearV = findView(R.id.tvManageClear_v)
        tvClearChatV = findView(R.id.tvClearChat_v)
        tvCloseVideoV = findView(R.id.tvCloseVideo_v)
        tvClear_anim= findView(R.id.tvClear_anim)
        initEvent()
    }
    private var mSendListener: ((it: Boolean) -> Unit)? = null
    private var mClearListener: (() -> Unit)? = null
    private var mManagerClearListener: (() -> Unit)? = null
    private var mClearAnimListener: (() -> Unit)? = null

    fun initEvent() {
        tvCloseVideoV.setOnClickListener {
            if (click) {
                click = false
                tvCloseVideoV.setCompoundDrawablesWithIntrinsicBounds(ViewUtils.getDrawable(R.mipmap.ic_live_close), null, null, null)
                tvCloseVideoV.text = "收起视频"
            } else {
                click = true
                tvCloseVideoV.setCompoundDrawablesWithIntrinsicBounds(ViewUtils.getDrawable(R.mipmap.ic_live_open), null, null, null)
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
        tvClear_anim.setOnClickListener {
            if (UserInfoSp.getIsShowAnim()){
                tvClear_anim.text = "显示特效"
                UserInfoSp.putIsShowAnim(false)
            }else{
                tvClear_anim.text = "隐藏特效"
                UserInfoSp.putIsShowAnim(true)
            }
            RxBus.get().post(LiveAnimClose(UserInfoSp.getIsShowAnim()))
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

    //清除动画
    fun setClearAnimClickListener(listener: () -> Unit) {
        mClearAnimListener = listener
    }
}