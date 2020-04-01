package com.fenghuang.baselib.widget.placeholder

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.fenghuang.baselib.R
import com.fenghuang.baselib.utils.ViewUtils
import kotlinx.android.synthetic.main.base_layout_place_holder.view.*

/**
 * 占位图容器，默认不展示自己，调用了相对应的方法才会进行展示
 */

class PlaceholderView : FrameLayout {

    init {
        visibility = View.GONE
        setBackgroundColor(ViewUtils.getColor(R.color.white))
        LayoutInflater.from(context).inflate(R.layout.base_layout_place_holder, this)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
            context: Context, attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    fun showLoading() {
        performViewVisible(isLoading = true, isEmpty = false, isError = false)
    }

    fun showEmpty(msg: String? = null) {
        if (msg?.isNotEmpty() == true) {
            tvEmpty?.text = msg
        }
        performViewVisible(isLoading = false, isEmpty = true, isError = false)
    }

    fun showError(msg: String? = null) {
        if (msg?.isNotEmpty() == true) {
            tvError?.text = msg
        }
        performViewVisible(isLoading = false, isEmpty = false, isError = true)
    }

    fun showContent() {
        visibility = View.GONE
    }

    fun setGoneEmpty() {
        setGoneEmptyContainer()
    }

    /**
     * 隐藏加载进度条，其实就是隐藏了占位容器
     */
    fun hideLoading() {
        showContent()
    }

    /**
     * 统一处理各种状态
     */
    private fun performViewVisible(isLoading: Boolean, isEmpty: Boolean, isError: Boolean) {
        when {
            isLoading -> {
                ViewUtils.setVisible(loadingContainer)
                ViewUtils.setGone(emptyContainer)
                ViewUtils.setGone(errorContainer)
            }
            isEmpty -> {
                ViewUtils.setVisible(emptyContainer)
                ViewUtils.setGone(loadingContainer)
                ViewUtils.setGone(errorContainer)
            }
            isError -> {
                ViewUtils.setVisible(errorContainer)
                ViewUtils.setGone(loadingContainer)
                ViewUtils.setGone(emptyContainer)
            }
        }
        visibility = View.VISIBLE
    }

    /**
     * 单独设置空视图隐藏
     */
    private fun setGoneEmptyContainer() {
        ViewUtils.setGone(emptyContainer)
    }

    /**
     * 设置错误重连的监听
     */
    fun setPageErrorRetryListener(listener: () -> Unit) {
        tvErrorRetry?.visibility = View.VISIBLE
        tvErrorRetry?.setOnClickListener {
            listener.invoke()
        }
    }

    /**
     * 是否正在加载中
     */
    fun isLoading(): Boolean {
        return this.visibility == View.VISIBLE && loadingContainer.visibility == View.VISIBLE
    }
}