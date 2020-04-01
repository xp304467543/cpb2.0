package com.fenghuang.baselib.base.recycler

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.R
import com.fenghuang.baselib.base.fragment.BaseNavFragment
import com.fenghuang.baselib.base.recycler.header.RecyclerClassicsFooter
import com.fenghuang.baselib.base.recycler.header.material.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshHeader
import kotlinx.android.synthetic.main.base_recycler.*

/**
 * 列表页面布局，封装SmartRefreshLayout，方便替换
 */
abstract class BaseRecyclerNavFragment<P : BaseRecyclerPresenter<*>, T> : BaseNavFragment(), BaseRecyclerContract.View {

    protected lateinit var mPresenter: P
    protected lateinit var mAdapter: BaseRecyclerAdapter<T>
    protected var mPage = getStartPage()

    override fun onCreate(savedInstanceState: Bundle?) {
        mPresenter = attachPresenter()
        attachView()
        super.onCreate(savedInstanceState)
    }

    override fun getContentResID(): Int {
        return R.layout.base_recycler
    }

    final override fun initContentView() {
        if (!isEnablePureScrollMode()) {
            smartRefreshLayout?.setRefreshHeader(getRefreshHeader())
            smartRefreshLayout?.setRefreshFooter(getRefreshFooter())
        }

        smartRefreshLayout?.setOnRefreshListener {
            mPage = getStartPage()
            mPresenter.loadData(mPage)
        }
        smartRefreshLayout?.setOnLoadMoreListener {
            mPage += 1
            mPresenter.loadData(mPage)
        }

        recyclerView?.apply {
            this.isVerticalScrollBarEnabled = this@BaseRecyclerNavFragment.isVerticalScrollBarEnabled()
            this.isHorizontalScrollBarEnabled = this@BaseRecyclerNavFragment.isHorizontalScrollBarEnabled()
            this.layoutManager = this@BaseRecyclerNavFragment.getLayoutManager()
            this.setHasFixedSize(true)
            getItemDivider()?.let {
                addItemDecoration(it)
            }
        }
        setSmartLayoutAttrs()
        setPageErrorRetryListener { initData() }
        setAdapter(attachAdapter())
        initPageView()
    }

    override fun initData() {
        mPage = getStartPage()
        if (isShowLoadingPage()) {
            showPageLoading()
        }
        if (isShowLoadingDialog()) {
            showPageLoadingDialog()
        }
        mPresenter.loadData(mPage)
    }

    /**
     * 获取开始加载的起始页，不一定是从0开始，默认从1开始
     */
    fun getStartPage(): Int = 1

    /**
     * 子类生成布局管理器
     */
    protected open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    /**
     * 生成分割线
     */
    protected open fun getItemDivider(): RecyclerView.ItemDecoration? = null

    /**
     * 获取下拉刷新的头部
     */
    protected open fun getRefreshHeader(): RefreshHeader {
        return MaterialHeader(getPageActivity())
    }

    /**
     * 获取刷新底部
     */
    protected open fun getRefreshFooter(): RefreshFooter {
        return RecyclerClassicsFooter(getPageActivity())
    }

    @Deprecated("请使用[showDatas]")
    override fun <T> showContent(datas: List<T>?) {
    }

    /**
     * 展示所有数据
     */
    fun showDatas(datas: List<T>?) {
        if (mPage == getStartPage()) {
            mAdapter.clear()
        }
        if (datas == null || datas.isEmpty()) {
            showPageEmpty()
        } else {
            hideLoading()
            hideRefreshing()
            mAdapter.addAll(datas)
        }
    }

    @Deprecated("请使用[showDatas]")
    override fun <T> showContent(data: T?) {
    }

    /**
     * 展示一个数据
     */
    fun showDatas(data: T?) {
        if (mPage == getStartPage()) {
            mAdapter.clear()
        }
        if (data == null) {
            showPageEmpty()
        } else {
            hideLoading()
            hideRefreshing()
            mAdapter.add(data)
        }
    }

    @Deprecated("请使用[updateData]")
    override fun <T> updateItem(data: T?, position: Int) {
    }

    /**
     * 更新某一条数据
     */
    fun updateData(data: T?, position: Int) {
        if (data != null && mAdapter.getCount() > 0 && position < mAdapter.getCount()) {
            mAdapter.update(data, position)
        }
    }

    @Deprecated("请使用[addData]")
    override fun <T> addItem(data: T?) {
    }


    /**
     * 添加一条数据
     */
    fun addData(data: T?) {
        hideLoading()
        hideRefreshing()
        if (data != null) {
            mAdapter.add(data)
        }
    }


    @Deprecated("请使用[addData]")
    override fun <T> addAll(datas: List<T>?) {
    }

    /**
     * 添加一个集合数据
     */
    fun addDatas(datas: List<T>?) {
        hideLoading()
        hideRefreshing()
        if (datas != null) {
            mAdapter.addAll(datas)
        }
    }

    @Deprecated("请使用[insertData]")
    override fun <T> insertItem(position: Int, data: T?) {
    }

    override fun removeItem(position: Int) {
        if (position < mAdapter.getCount()) {
            mAdapter.remove(position)
        }
    }

    /**
     * 插入一条数据
     */
    fun insertData(position: Int, data: T?) {
        if (position < mAdapter.getCount() && data != null) {
            mAdapter.insert(data, position)
        }
    }

    /**
     * 清除数据
     */
    override fun clear() {
        mAdapter.clear()
    }

    protected open fun getDatas(): List<T> {
        return mAdapter.getAllData()
    }

    /**
     * 结束刷新
     */
    protected fun hideRefreshing(hasMore: Boolean = true) {
        smartRefreshLayout?.finishRefresh()
        if (hasMore) {
            smartRefreshLayout?.finishLoadMore()
        } else {
            smartRefreshLayout?.finishLoadMoreWithNoMoreData()
        }
    }

    /**
     * 当前页面是否激活，有View相关的操作时，做好先做一下判断
     */
    override fun isActive(): Boolean {
        return isAdded
    }

    /**
     * 有没有数据
     */
    protected open fun isEmptyRecycler(): Boolean {
        return mAdapter.itemCount == 0
    }

    abstract fun attachView()

    /**
     * 获取子类Presenter对象
     */
    protected abstract fun attachPresenter(): P

    /**
     * 获取子类适配器对象
     */
    protected open fun attachAdapter(): BaseRecyclerAdapter<T>? = null

    /**
     * 设置适配器
     */
    protected open fun setAdapter(adapter: BaseRecyclerAdapter<T>?) {
        if (adapter != null) {
            mAdapter = adapter
            recyclerView?.adapter = mAdapter
        }
    }

    /**
     * 添加条目间隔
     */
    protected fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        recyclerView?.addItemDecoration(decor)
    }

    /**
     * 设置SmartRefreshLayout相关属性
     */
    protected fun setSmartLayoutAttrs() {
        smartRefreshLayout?.setEnableAutoLoadMore(isEnableAutoLoadMore())
        smartRefreshLayout?.setEnableLoadMore(isEnableLoadMore())
        smartRefreshLayout?.setEnableRefresh(isEnableRefresh())
        smartRefreshLayout?.setEnableOverScrollBounce(isEnableOverScrollBounce())
        smartRefreshLayout?.setEnablePureScrollMode(isEnablePureScrollMode())
        smartRefreshLayout?.setEnableOverScrollDrag(isEnableOverScrollDrag())
        if (isEnableOverScrollDrag()) {
            smartRefreshLayout?.setEnablePureScrollMode(true)
        }
    }


    /**
     * 隐藏正在加载的进度条
     */
    protected fun hideLoading() {
        hidePageLoading()
        hidePageLoadingDialog()
    }

    /**
     * 当展示空视图的时候，可以下拉，但是不可以上拉
     */
    override fun showPageEmpty(msg: String?) {
        hideLoading()
        if (mPage == getStartPage()) {
            hideRefreshing()
            super.showPageEmpty(msg)
        } else {
            // 如果不是第一页，并且没有数据了，就不结束加载更多
            hideRefreshing(false)
        }
    }

    override fun showPageError(msg: String?) {
        // 当加载异常时，要手动去关闭加载进度，这里统一处理
        hideLoading()
        hideRefreshing()

        if (mPage == getStartPage()) {
            super.showPageError(msg)
        } else {
            // 如果已经有数据了，就吐司提示
            showToast(msg ?: getString(R.string.app_loading_error))
        }
    }

    /**
     * 滑动到指定位置
     */
    protected fun scrollToPosition(position: Int = 0) {
        recyclerView?.scrollToPosition(position)
    }

    /**
     * 缓慢滑动到指定位置
     */
    protected fun smoothScrollToPosition(position: Int = 0) {
        recyclerView?.smoothScrollToPosition(position)
    }

    protected fun getRecyclerView(): RecyclerView? {
        return recyclerView
    }

    protected fun getSmartRefreshLayout(): SmartRefreshLayout? {
        return smartRefreshLayout
    }


    /**
     * 竖直滚动条是否可用
     */
    protected open fun isVerticalScrollBarEnabled() = true

    /**
     * 水平滚动条是否可用
     */
    protected open fun isHorizontalScrollBarEnabled() = false

    /**
     * 是否可以自动加载更多，默认可以
     */
    protected open fun isEnableAutoLoadMore() = true

    /**
     * 是否可以加载更多，默认可以
     */
    protected open fun isEnableLoadMore() = true

    /**
     * 是否可以刷新，默认可以
     */
    protected open fun isEnableRefresh() = true

    /**
     * 是否是纯净模式，不展示刷新头和底部，默认false
     */
    protected open fun isEnablePureScrollMode() = false

    /**
     * 刷新时是否可以越界回弹
     */
    protected open fun isEnableOverScrollBounce() = false

    /**
     * 是否可拖拽
     */
    protected open fun isEnableOverScrollDrag() = false

    /**
     * 默认展示加载页面
     */
    protected open fun isShowLoadingPage() = true

    /**
     * 支持滑动返回
     */
    override fun isSwipeBackEnable(): Boolean = true

    /**
     * 是否展示加载对话框
     */
    protected open fun isShowLoadingDialog() = false


    /**
     * 让子类重写，初始化页面
     */
    protected open fun initPageView() {}

    /**
     * 销毁数据和引用
     */
    override fun onDestroy() {
        mPresenter.detachView()
        mAdapter.clear()
        super.onDestroy()
    }

}