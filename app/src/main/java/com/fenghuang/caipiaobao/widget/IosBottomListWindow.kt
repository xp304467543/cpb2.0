package com.fenghuang.caipiaobao.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R

/**
 *
 * @ Author  QinTian
 * @ Date  2019/10/10- 12:12
 * @ Describe 仿IOS 底部弹框
 *
 */

class IosBottomListWindow(private val activity: Activity) {
    private val shadowMax = 0xa0
    private var majorTitle: TextView? = null
    private var title: TextView? = null
    private val viewGroup: FrameLayout
    private val contentGroup: LinearLayout
    private val menuList: LinearLayout
    private val commonMargin = ViewUtils.dp2px(10f).toInt()
    private val itemMargin = ViewUtils.dp2px(16f).toInt()
    private var contentLayoutHeight = 0

    private var isShow = false

    private var mListener: (() -> Unit)? = null
    private var mCancelButtonListener: (() -> Unit)? = null
    fun setCancelButtonClickListener(listener: () -> Unit) {
        mCancelButtonListener = listener
    }

    fun setOnDissMissClickListener(listener: () -> Unit) {
        mListener = listener
    }


    init {
        viewGroup = initViewGroup()
        contentGroup = initContentGroup()
        menuList = initMenuList()
        viewGroup.addView(contentGroup)
        contentGroup.addView(menuList)
    }

    fun show() {
        if (!isShow) {
            (activity.window.decorView as ViewGroup).addView(viewGroup)
            contentGroup.apply {
                post {
                    contentLayoutHeight = measuredHeight
                    translationY = contentLayoutHeight.toFloat()
                    visibility = View.VISIBLE
                    startAnimator(true)
                }
            }
            isShow = true
        }
    }

    fun dismiss() {
        if (isShow) {
            startAnimator(false, object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    (activity.window.decorView as ViewGroup).removeView(viewGroup)
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    (activity.window.decorView as ViewGroup).removeView(viewGroup)
                }

            })
            isShow = false
        }
    }

    /**
     * 设置主标题
     */
    fun setMajorTitle(text: String): IosBottomListWindow {
        majorTitle = TextView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                val margin = ViewUtils.dp2px(20f).toInt()
                if (title == null) {
                    setMargins(0, margin, 0, margin)
                } else {
                    setMargins(0, margin, 0, 0)
                }
            }
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setTextColor(Color.parseColor("#8f8f8f"))
            typeface = Typeface.DEFAULT_BOLD
            setText(text)
        }
        menuList.addView(majorTitle, 0)
        return this
    }

    /**
     * 设置副标题
     */
    fun setTitle(text: String): IosBottomListWindow {
        title = TextView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                val margin = ViewUtils.dp2px(20f).toInt()
                if (majorTitle == null) {
                    setMargins(0, margin, 0, margin)
                } else {
                    setMargins(0, 0, 0, margin)
                }
            }
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            setTextColor(Color.parseColor("#666666"))
            setText(text)
        }
        if (majorTitle == null) {
            menuList.addView(title, 0)
        } else {
            menuList.addView(title, 1)
        }
        return this
    }

    /**
     * 设置子项
     */
    fun setItem(text: String, textColor: Int = 0, itemClickListener: () -> Unit): IosBottomListWindow {
        val textView = TextView(activity)
        textView.apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setPadding(0, itemMargin, 0, itemMargin)
            }
            gravity = Gravity.CENTER
            setBackgroundResource(R.drawable.bottom_btn_click)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            if (textColor == 0) {
                setTextColor(Color.parseColor("#333333"))
            } else {
                try {
                    setTextColor(textColor)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            setText(text)
            setOnClickListener {
                itemClickListener.invoke()
                dismiss()
            }
        }
        val lineView = View(activity).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1)
            setBackgroundColor(Color.parseColor("#dcdbdf"))
        }
        menuList.addView(lineView)
        menuList.addView(textView)
        return this
    }

    /**
     * 设置按钮
     */
    fun setCancelButton(text: String, textColor: Int = 0): IosBottomListWindow {
        val cancelView = TextView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(commonMargin, 0, commonMargin, commonMargin)
            }
            setPadding(0, itemMargin, 0, itemMargin)
            background = ViewUtils.getDrawable(R.drawable.bottom_btn_click)
            if (textColor == 0) {
                setTextColor(Color.parseColor("#333333"))
            } else {
                try {
                    setTextColor(textColor)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            gravity = Gravity.CENTER
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setText(text)
            typeface = Typeface.DEFAULT_BOLD
            setOnClickListener {
                dismiss()
                mCancelButtonListener?.invoke()
            }
        }
        contentGroup.addView(cancelView)
        return this
    }

    /**
     * 初始化容器
     * 背景阴影容器
     */
    private fun initViewGroup() = FrameLayout(activity).apply {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        setOnClickListener {
            dismiss()
            mListener?.invoke()
        }
    }

    /**
     * 初始化内容容器
     */
    private fun initContentGroup() = LinearLayout(activity).apply {
        layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.BOTTOM
            bottomMargin = ViewUtils.getStatusHeight()
            visibility = View.INVISIBLE
        }
        orientation = LinearLayout.VERTICAL
    }

    /**
     * 初始化菜单列表
     */
    private fun initMenuList() = LinearLayout(activity).apply {
        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT).apply {
            setMargins(commonMargin, 0, commonMargin, commonMargin)
        }
        gravity = Gravity.CENTER_HORIZONTAL
        orientation = LinearLayout.VERTICAL
        background = ViewUtils.getDrawable(R.drawable.shape_bottom_list_menu)
    }

    private fun startAnimator(enterType: Boolean, listener: Animator.AnimatorListener? = null) {
        viewGroup.post {
            ValueAnimator().apply {
                if (enterType) {
                    setFloatValues(contentLayoutHeight.toFloat(), 0f)
                } else {
                    setFloatValues(0f, contentLayoutHeight.toFloat())
                }
                duration = 300
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    val value = it.animatedValue as Float
                    contentGroup.translationY = value
                    setShadow()
                }
                listener?.let {
                    addListener(it)
                }
                start()
            }
        }
    }

    private fun setShadow() {
        val ratio = contentGroup.translationY / contentLayoutHeight
        val shadow = (shadowMax * (1 - ratio)).toInt()
        if (shadow >= 16) {
            viewGroup.setBackgroundColor(Color.parseColor("#${shadow.toString(16)}000000"))
        }
    }

}
