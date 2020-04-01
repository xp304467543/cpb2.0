package com.fenghuang.baselib.base.activity

import androidx.appcompat.widget.Toolbar
import com.fenghuang.baselib.R
import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.baselib.base.fragment.PlaceholderFragment
import kotlinx.android.synthetic.main.base_nav_page.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * 带有导航栏的Activity，结构为Toolbar,
 */
open class BaseNavActivity(override val layoutResID: Int = R.layout.base_nav_page) : BaseActivity() {

    final override fun initView() {
        if (!isOverride()) {
            // 设置是否展示标题栏
            setShowToolBar(isShowToolBar())

            // 设置状态栏高度
            mDelegate.setStatusBarHeight(statusView, isSetStatusBarHeight())


            // 设置导航栏文字等
            if (isShowToolBar()) {
                // 设置标题
                setPageTitle(getPageTitle())

                // 设置Toolbar标题样式
                mDelegate.setToolbarStyle(isMainPage(), toolbar)

                // 左侧返回按钮
                mDelegate.setBackIcon(toolbar, isMainPage(), isShowBackIcon(), isShowBackIconWhite()) { onBackClick() }

                // 填充Menu
                mDelegate.inflateMenu(toolbar, getMenuResID()) { onMenuItemSelected(it) }
            }

            // 设置填充容器
            if (navLayout.childCount > 0) {
                navLayout.removeAllViews()
            }

            // 设置内容容器的填充方式
            if (getContentResID() == 0) {
                val fragment = getContentFragment()
                // 转移Activity的extras给Fragment
                if (fragment.arguments == null && intent.extras != null) {
                    fragment.arguments = intent.extras
                } else if (fragment.arguments != null && intent.extras != null) {
                    fragment.arguments!!.putAll(intent.extras)
                }
                loadRootFragment(R.id.navLayout, fragment)
            } else {
                layoutInflater.inflate(getContentResID(), navLayout)
            }
        }

        // 初始化容器View
        initContentView()
    }

    /**
     * 子类继续填充内容容器布局
     */
    protected open fun getContentResID(): Int = 0

    /**
     *  子类填充Fragment
     */
    protected open fun getContentFragment(): BaseFragment = PlaceholderFragment()

    /**
     * 给子类初始化View使用
     */
    protected open fun initContentView() {

    }

    /**
     * 获取页面标题，进入页面后会调用该方法获取标题，设置给ToolBar
     * 调用该方法返回Title，则会使用默认的Title样式，如果需要设置样式
     * 请调用setPageTitle()
     */
    protected open fun getPageTitle(): String? = mDelegate.getPageTitle()

    /**
     * 执行返回操作
     * 默认是Fragment弹栈，然后退出Activity
     * 如果栈内只有一个Fragment，则退出Activity
     */
    protected open fun onBackClick() {
        onBackPressedSupport()
    }

    /**
     * 是否设置状态栏高度，如果设置的话，默认会自动调整状态栏的高度。
     * 如果为false则状态栏高度为0，默认状态栏有高度。
     */
    protected open fun isSetStatusBarHeight(): Boolean = true

    /**
     * 是否展示ToolBar，如果设置为false则不展示。
     * 如果不展示标题栏，则状态栏也不会展示。
     */
    protected open fun isShowToolBar(): Boolean = true

    /**
     * 是否可以返回，如果可以则展示返回按钮，并且设置返回事件
     * 默认可以返回
     */
    protected open fun isShowBackIcon(): Boolean = true

    /**
     * 是否展示白色返回图标
     * true返回白色 fleas返回黑色
     */
    protected open fun isShowBackIconWhite(): Boolean = true


    /**
     * 是不是Main页面
     * 如果是的话，ToolBar设置粗体的样式
     */
    protected open fun isMainPage(): Boolean = false

    /**
     * 子类是否需要重写父类的整个页面
     */
    protected open fun isOverride() = false


    /**
     * 获取标题栏对象，让子类主动去设置样式
     */
    protected fun getToolBar(): Toolbar {
        return toolbar
    }

    /**
     * 主动设置页面标题，给子类调用
     */
    protected fun setPageTitle(title: String?) {
        toolbar.title = ""
        mDelegate.setPageTitle(tvTitle, title)
    }

    /**
     * 设置ToolBar的展示状态
     * @param isShow 是否展示
     */
    protected fun setShowToolBar(isShow: Boolean) {
        mDelegate.setShowToolBar(appBarLayout, isShow)
    }

    // --------------------页面状态的相关处理--------------------
    /**
     * 展示加载对话框
     * 适用于页面UI已经绘制了，需要再加载数据更新的情况
     */
    open fun showPageLoadingDialog(msg: String? = getString(R.string.app_loading)) {
        mDelegate.showPageLoadingDialog(this, msg)
    }

    /**
     * 隐藏加载对话框
     */
    open fun hidePageLoadingDialog() {
        mDelegate.hidePageLoadingDialog()
    }

    /**
     * 展示加载中的占位图
     */
    open fun showPageLoading() {
        mDelegate.showPageLoading(placeholder)
    }

    open fun hidePageLoading() {
        mDelegate.hidePageLoading(placeholder)
    }

    /**
     * 展示空数据的占位图
     */
    open fun showPageEmpty(msg: String? = null) {
        mDelegate.showPageEmpty(placeholder, msg)
    }

    /**
     * 展示加载错误的占位图
     */
    open fun showPageError(msg: String? = null) {
        mDelegate.showPageError(placeholder, msg)
    }

    /**
     * 设置页面加载错误重连的监听
     */
    open fun setPageErrorRetryListener(listener: () -> Unit) {
        mDelegate.setPageErrorRetryListener(placeholder, listener)

    }


    /**
     * 展示加载完成，要显示的内容
     */
    open fun showPageContent() {
        mDelegate.showPageContent(placeholder)
    }
}