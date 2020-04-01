package com.fenghuang.baselib.widget.dialog

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.fenghuang.baselib.R

/**
 * 自定义中间体，继承AlertDialog，作为对话框基类
 * 重写对话框的标题
 */
open class MaterialDialog protected constructor(context: Context) : AlertDialog(context) {

    private var mDimAmount: Float = 0.5f

    open class Builder(context: Context) : AlertDialog.Builder(context, R.style.AlertDialog_Material) {

        override fun setTitle(title: CharSequence?): AlertDialog.Builder {
            if (TextUtils.isEmpty(title)) return this

            val titleView = LayoutInflater.from(context).inflate(R.layout.base_dialog_title_view, null)
            setCustomTitle(titleView)
            val tvTitle = titleView.findViewById<TextView>(R.id.baseDialogTitle)
            tvTitle.text = title
            return this
        }
    }

    override fun show() {
        super.show()
        val window = window
        val params = window?.attributes
        window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        // 背景阴影
        if (isSetDimBehind())
            params?.dimAmount = mDimAmount
        else
            params?.dimAmount = 0f
        window?.attributes = params
    }

    /**
     * 是否设置背景阴影
     */
    open fun isSetDimBehind(): Boolean = true

    /**
     * 设置背景阴影层度
     */
    open fun setDimAmount(dimAmount: Float) {
        this.mDimAmount = dimAmount
    }
}