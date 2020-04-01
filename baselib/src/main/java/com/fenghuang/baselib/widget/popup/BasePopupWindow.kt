package com.fenghuang.baselib.widget.popup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

open class BasePopupWindow(protected var context: Context, private val mLayoutId: Int) : PopupWindow() {

    private var mLayoutView: View? = null

    init {
        mLayoutView = View.inflate(context, mLayoutId, null)
        this.contentView = mLayoutView

        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT

        // this.setFocusable(true);// 可点击
        // 实例化一个ColorDrawable颜色为半透明(半透明遮罩颜色代码#66000000)
        setBackground(Color.TRANSPARENT)
        isOutsideTouchable = true
        isTouchable = true
    }

    protected open fun initView() {}

    /**
     * PopupWindow背景设置
     */
    protected fun setBackground(color: Int) {
        // 实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(color)
        this.setBackgroundDrawable(dw)
    }

    /**
     * 显示在控件正上方
     *
     * @param view     依赖的控件
     * @param marginDp 设置的间距(直接写数字即可，已经做过dp2px转换)
     */
    fun showAtLocationTop(view: View, marginDp: Float) {
        mLayoutView?.apply {
            this.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val popupWidth = this.measuredWidth
            val popupHeight = this.measuredHeight
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.width / 2 - popupWidth / 2, location[1] - popupHeight - dp2px(marginDp))
            update()
        }
    }

    /**
     * 显示在控件正下方
     *
     * @param view     依赖的控件
     * @param marginDp 设置的间距(直接写数字即可，已经做过dp2px转换)
     */
    fun showAtLocationGravityBottom(view: View, marginDp: Float) {
        mLayoutView?.apply {
            this.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val popupWidth = this.measuredWidth
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.width / 2 - popupWidth / 2,
                    location[1] + view.height + dp2px(marginDp))
            update()
        }
    }

    /**
     * 显示在控件下方
     *
     * @param view     依赖的控件
     * @param marginDp 设置的间距(直接写数字即可，已经做过dp2px转换)
     */
    fun showAtLocationBottom(view: View, marginDp: Float) {
        showAsDropDown(view, 0, dp2px(marginDp))
        update()
    }


    /**
     * 显示在控件左方
     *
     * @param view     依赖的控件
     * @param marginDp 设置的间距(直接写数字即可，已经做过dp2px转换)
     */
    fun showAtLocationLeft(view: View, marginDp: Float) {
        mLayoutView?.apply {
            this.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val popupWidth = this.measuredWidth
            val popupHeight = this.measuredHeight
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            showAtLocation(view, Gravity.NO_GRAVITY, location[0] - popupWidth - dp2px(marginDp), location[1] + view.height / 2 - popupHeight / 2)
            update()
        }
    }

    /**
     * 显示在控件右方
     *
     * @param view     依赖的控件
     * @param marginDp 设置的间距(直接写数字即可，已经做过dp2px转换)
     */
    fun showAtLocationRight(view: View, marginDp: Float) {
        mLayoutView?.apply {
            this.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            val popupHeight = this.measuredHeight
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.width + dp2px(marginDp), location[1] + view.height / 2 - popupHeight / 2)
            update()
        }
    }

    /**
     * 通过id获得view
     */
    @Suppress("UNCHECKED_CAST")
    protected fun <T : View> findView(viewId: Int): T {
        if (mLayoutView == null) {
            mLayoutView = LayoutInflater.from(context).inflate(mLayoutId, null)
        }
        val view: View = mLayoutView!!.findViewById(viewId)
        return view as T
    }


    private fun dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
    }

}
