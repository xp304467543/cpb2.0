package com.fenghuang.caipiaobao.widget.dialog

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.Selection
import android.text.Selection.getSelectionEnd
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.core.view.isVisible
import com.fenghuang.baselib.utils.SoftInputUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.utils.ViewUtils.setGone
import com.fenghuang.baselib.utils.ViewUtils.setVisible
import com.fenghuang.baselib.widget.round.RoundTextView
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.dialog_live_room_chat.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-23
 * @ Describe
 *
 */

class   LiveRoomChatInputDialog(context: Context, styleRes: Int) : Dialog(context, styleRes) {

    private var mSendListener: ((it: String) -> Unit)? = null

    var tvSend: TextView
    var etChat: EditText

    init {
        setContentView(R.layout.dialog_live_room_chat)
        tvSend = findViewById(R.id.tvSendMessage)
        etChat = findViewById(R.id.etLiveRoomChat)
        window!!.setGravity(Gravity.BOTTOM)
        val lp = window!!.attributes
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT  // 宽度
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        dialogEmoticonKeyboards.setupWithEditText(etLiveRoomChat)
        setEdiTextRul(etLiveRoomChat)
        initEvent()
    }


    private fun setEdiTextRul(etLiveRoomChat: EditText) {
        etLiveRoomChat.addTextChangedListener(object : TextWatcher {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! > 196) {
                    ToastUtils.showNormal("最多输入200个字")
                    return
                }
                if (s.isNotEmpty()) {
                    tvSendMessage.background = ViewUtils.getDrawable(R.drawable.button_background)
                    tvSendMessage.setTextColor(ViewUtils.getColor(R.color.white))
                } else {
                    tvSendMessage.background =ViewUtils.getDrawable(R.drawable.button_grey_background)
                    tvSendMessage.setTextColor(ViewUtils.getColor(R.color.grey_95))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var editable = etLiveRoomChat.text
                val len = editable.length
                if (len > 196) {
                    var selEndIndex = getSelectionEnd(editable)
                    val str = editable.toString()
                    //截取新字符串
                    val newStr = str.substring(0, start)
                    etLiveRoomChat.setText(newStr)
                    editable = etLiveRoomChat.text
                    //新字符串的长度
                    val newLen = editable.length
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex)
                }
            }
        })
    }

    private fun initEvent() {
        imgLiveChatEmoji.setOnClickListener {
            toggleKeyboard()
        }
        etLiveRoomChat.setOnTouchListener { _, _ ->
            if (dialogEmoticonKeyboards.isShown) {
                setGone(dialogEmoticonKeyboards)
                SoftInputUtils.showSoftInput(etLiveRoomChat)
            }
            true
        }
        tvSendMessage.setOnClickListener {
            if (etLiveRoomChat.text.isNullOrEmpty()) {
                ToastUtils.show("请输入内容")
                return@setOnClickListener
            }
            mSendListener?.invoke(etLiveRoomChat.text.toString())
        }

    }


    fun showEmojiOrKeyBord(showEmoji: Boolean) {
        if (showEmoji) {
            dialogEmoticonKeyboards.visibility = View.VISIBLE
        } else SoftInputUtils.showSoftInput(etLiveRoomChat)
        show()
    }


    fun initTextChat(str:String){
        etLiveRoomChat.append(str)
    }


    /**
     * 显示表情与隐藏表情
     */
    private fun toggleKeyboard() {
        if (dialogEmoticonKeyboards.isVisible) {
            setGone(dialogEmoticonKeyboards)
            SoftInputUtils.showSoftInput(etLiveRoomChat)
        } else {
            disMissKeyBord()
            etLiveRoomChat.postDelayed({ setVisible(dialogEmoticonKeyboards) }, 100)
        }
    }

    override fun dismiss() {
        disMissKeyBord()
        super.dismiss()
    }

    private fun disMissKeyBord() {
        val view = currentFocus
        if (view is TextView) {
            val mInputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
        }
    }


    fun setSendClickListener(listener: (it: String) -> Unit) {
        mSendListener = listener
    }
}