package com.fenghuang.caipiaobao.widget.dialog.bottom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.yokeyword.fragmentation.ISupportFragment


/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-07
 * @ Describe 基类底部弹框
 *
 */

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {
    protected var mContext: Context? = null
    var rootView: View? = null
    protected var dialog: BottomSheetDialog? = null

    protected var mBehavior: BottomSheetBehavior<*>? = null
    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
            //禁止拖拽，
            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                //设置为收缩状态
                mBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {}
    }

    abstract val layoutResId: Int

    val isShowing: Boolean
        get() = dialog != null && dialog!!.isShowing

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onStart() {
        super.onStart()
        mBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        dialog?.window?.setDimAmount(0f)
    }

    override fun onDestroy() {
        super.onDestroy()
        //解除缓存View和当前ViewGroup的关联
        (rootView!!.parent as ViewGroup).removeView(rootView)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //每次打开都调用该方法 类似于onCreateView 用于返回一个Dialog实例
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        if (rootView == null) {
            //缓存下来的View 当为空时才需要初始化 并缓存
            rootView = View.inflate(mContext, layoutResId, null)
            initView()
            initData()
        }
        resetView()
        //设置View重新关联
        dialog!!.setContentView(rootView!!)
        dialog?.setCanceledOnTouchOutside(false)
        mBehavior = BottomSheetBehavior.from(rootView!!.parent as View)
        mBehavior!!.skipCollapsed = true
        mBehavior!!.isHideable = true
        mBehavior!!.setBottomSheetCallback(mBottomSheetBehaviorCallback)
        //圆角边的关键(设置背景透明)
        ((rootView?.parent) as View).setBackgroundColor(Color.TRANSPARENT)
        //重置高度
        if (dialog != null) {
            val bottomSheet = dialog!!.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet!!.layoutParams.height = ViewUtils.getScreenHeight() * 2 / 3

        }
        rootView!!.post { mBehavior!!.peekHeight = rootView!!.height }
        return dialog as BottomSheetDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = super.onCreateView(inflater, container, savedInstanceState)
        if (rootView == null) {
            rootView = inflater.inflate(layoutResId, container, false)
        }
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    /**
     * 初始化View和设置数据等操作的方法
     */
    abstract fun initView()

    abstract fun initData()


    /**
     * 重置的View和数据的空方法 子类可以选择实现
     * 为避免多次inflate 父类缓存rootView
     * 所以不会每次打开都调用[.initView]方法
     * 但是每次都会调用该方法 给子类能够重置View和数据
     */
    fun resetView() {

    }

    /**
     * 使用关闭弹框 是否使用动画可选
     * 使用动画 同时切换界面Aty会卡顿 建议直接关闭
     *
     * @param isAnimation
     */
    fun close(isAnimation: Boolean) {
        if (isAnimation) {
            if (mBehavior != null)
                mBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
            dismiss()
        } else {
            dismiss()
        }
    }
}