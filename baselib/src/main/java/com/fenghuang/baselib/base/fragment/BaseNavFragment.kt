package com.fenghuang.baselib.base.fragment

import androidx.appcompat.widget.Toolbar
import com.fenghuang.baselib.R
import kotlinx.android.synthetic.main.base_nav_page.*
import kotlinx.android.synthetic.main.layout_toolbar.*

/**
 * 页面Fragment基类，封装ToolBar和StatusBar，可以不用搭理顶部的标题栏。
 * 另外将页面的各种状态进行统一处理，方便直接调用展示，比如加载中试图，空试图，错误试图等。
 * 还有将平时开发中用到的各种工具类进行封装，提供给子类调用。
 */
abstract class BaseNavFragment : BaseFragment() {

    override fun getLayoutResID(): Int = R.layout.base_nav_page

    final override fun initView() {
        if (!isOverridePage()) {
            // 设置是否展示标题栏
            setShowToolBar(isShowToolBar())

            // 设置状态栏高度
            setStatusBarHeight(statusView, isSetStatusBarHeight())

            // 设置导航栏文字等
            if (isShowToolBar()) {
                // 设置标题
                setPageTitle(getPageTitle())
                // 是否显示图片
                setVisibleTitleImageDown(isShowTitleImage())
                setVisibleTitleLiftLogo(isShowTitleLeftLogo())
                setVisibleTitleRightLogo(isShowTitleRightLogo())
                setVisibleTitleRightSecondLogo(isShowTitleRightSecondLogo())
                // 设置Toolbar标题样式
                mDelegate.setToolbarStyle(isMainPage(), toolbar)

                // 左侧返回按钮
                mDelegate.setBackIcon(toolbar, isMainPage(), isShowBackIcon(), isShowBackIconWhite()) { onBackClick() }
            }

            // 设置填充容器
            mDelegate.setContentView(navLayout, getContentResID())
        }
        // 填充Menu
        mDelegate.inflateMenu(toolbar, getMenuResID()) { onMenuItemSelected(it) }

        // 初始化容器View
        initContentView()
    }

    /**
     * 子类继续填充内容容器布局
     */
    protected open fun getContentResID(): Int = 0

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
     * 获取菜单项资源ID
     */
    protected open fun getMenuResID(): Int = 0

    /**
     * 菜单项点击
     */
    protected open fun onMenuItemSelected(itemId: Int): Boolean = true


    /**
     * 执行返回操作
     * 默认是Fragment弹栈，然后退出Activity
     * 如果栈内只有一个Fragment，则退出Activity
     */
    protected open fun onBackClick() {
        getPageActivity().onBackPressedSupport()
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
     * 是否展示Title文本边的图片，如果设置为false则不展示。
     */
    protected open fun isShowTitleImage(): Boolean = false

    /**
     * 是否展示Title右边的图片，如果设置为false则不展示。
     */
    protected open fun isShowTitleRightLogo(): Boolean = false

    /**
     * 是否展示Title右边的图片，如果设置为false则不展示。
     */
    protected open fun isShowTitleRightSecondLogo(): Boolean = false
    /**
     * 是否展示Title左边的图片，如果设置为false则不展示。
     */
    protected open fun isShowTitleLeftLogo(): Boolean = false


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
    protected open fun isOverridePage() = false

    /**
     * 获取标题栏对象，让子类主动去设置样式
     */
    protected fun getToolBar(): Toolbar? {
        return toolbar
    }

    /**
     * 主动设置页面标题，给子类调用
     * Toolbar的标题默认靠左，这里使用Toolbar包裹一个TextView的形式
     * 呈现居中的标题
     */
    protected fun setPageTitle(title: String?) {
        toolbar?.title = ""
        mDelegate.setPageTitle(tvTitle, title)
    }

    /**
     * 设置ToolBar的展示状态
     * @param isShow 是否展示
     */
    protected fun setShowToolBar(isShow: Boolean) {
        mDelegate.setShowToolBar(appBarLayout, isShow)
    }

    /**
     * 设置Title图片的展示状态
     * @param isShow 是否展示
     */
    protected fun setVisibleTitleImageDown(isShow: Boolean) {
        mDelegate.setShowTitleImage(ivTitleDown, isShow)
    }

    /**
     * 设置Title的图片的展示状态
     * @param isShow 是否展示
     */
    protected fun setVisibleTitleLiftLogo(isShow: Boolean) {
        mDelegate.setShowTitleImage(ivTitleLeft, isShow)
    }

    /**
     * 设置Title的图片的展示状态
     * @param isShow 是否展示
     */
    protected fun setVisibleTitleRightLogo(isShow: Boolean) {
        mDelegate.setShowTitleImage(ivTitleRight, isShow)
    }

    /**
     * 设置Title的图片的展示状态
     * @param isShow 是否展示
     */
    protected fun setVisibleTitleRightSecondLogo(isShow: Boolean) {
        mDelegate.setShowTitleImage(ivTitleRightSecond, isShow)
    }
    // --------------------页面状态的相关处理--------------------
    // --------------------页面状态的相关处理--------------------
    // --------------------页面状态的相关处理--------------------
    /**
     * 展示加载对话框
     * 适用于页面UI已经绘制了，需要再加载数据更新的情况
     */
    open fun showPageLoadingDialog(msg: String? = getString(R.string.app_loading)) {
        mDelegate.showPageLoadingDialog(context, msg)
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
        showEmpty(placeholder, msg)
    }

    open fun setGoneEmpty() {
        setGoneEmpty(placeholder)
    }

    /**
     * 展示加载错误的占位图
     */
    open fun showPageError(msg: String? = null) {
        showError(placeholder, msg)
    }

    /**
     * 设置页面加载错误重连的监听
     */
    open fun setPageErrorRetryListener(listener: () -> Unit) {
        setErrorRetryListener(placeholder, listener)
    }

    /**
     * 展示加载完成，要显示的内容
     */
    open fun showPageContent() {
        mDelegate.showPageContent(placeholder)
    }

}