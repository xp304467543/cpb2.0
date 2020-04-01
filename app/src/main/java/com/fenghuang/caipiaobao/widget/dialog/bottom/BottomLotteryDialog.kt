package com.fenghuang.caipiaobao.widget.dialog.bottom

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.fenghuang.caipiaobao.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_bottom_lottery.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/1/5- 15:08
 * @ Describe 底部弹框
 *
 */

class BottomLotteryDialog(context: Context, val list: List<BottomDialogBean>?) : BottomSheetDialog(context) {


    var bottomAdapter: BottomDialogAdapter? = null

    private var mSureClickListener: ((data: List<BottomDialogBean>) -> Unit)? = null

    init {
        setContentView(R.layout.dialog_bottom_lottery)
        val root = this.delegate?.findViewById<View>(R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(root)
        behavior.isHideable = false
        this.delegate?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundColor(Color.TRANSPARENT)
        initRecycle()
        initEvent()
    }

    private fun initRecycle() {
        bottomAdapter = BottomDialogAdapter(context)
        val gridLayoutManager = object : GridLayoutManager(context, 4) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        rvRankSelect.adapter = bottomAdapter
        rvRankSelect.layoutManager = gridLayoutManager
        if (list != null && list.isNotEmpty()) {
            bottomAdapter!!.addAll(list)
        }

    }


    private fun initEvent() {
        tvCancel.setOnClickListener {
            dismiss()
        }
        tvConfirm.setOnClickListener {
            mSureClickListener?.invoke(bottomAdapter!!.getAllData())
        }
        tvSelectAll.setOnClickListener {
            for ((index, _) in list!!.withIndex()) {
                list[index].isSelect = true
            }
            bottomAdapter!!.notifyDataSetChanged()
        }
        tvClearAll.setOnClickListener {
            for ((index, _) in list!!.withIndex()) {
                list[index].isSelect = false
            }
            bottomAdapter!!.notifyDataSetChanged()
        }
    }


    /**
     * 确定点击
     */
    fun setOnSureClickListener(listener: (data: List<BottomDialogBean>) -> Unit) {
        this.mSureClickListener = listener
    }

}