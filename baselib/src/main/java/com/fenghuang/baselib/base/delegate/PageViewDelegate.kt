package com.fenghuang.baselib.base.delegate

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.fenghuang.baselib.R
import com.fenghuang.baselib.app.BaseApplication
import com.fenghuang.baselib.base.activity.BaseActivity
import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.baselib.utils.AppUtils
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.dialog.MaterialConfirmDialog
import com.fenghuang.baselib.widget.dialog.MaterialLoadingDialog
import com.fenghuang.baselib.widget.dialog.MaterialTipsDialog
import com.fenghuang.baselib.widget.placeholder.PlaceholderView
import com.google.android.material.appbar.AppBarLayout

/**
 *
 * 页面View的代理，处理View的统一事件
 */
class PageViewDelegate(private val view: View?) {

    fun getContext(): Context? {
        return view?.context
    }

    fun <T : View> findView(id: Int): T {
        if (view == null) throw NullPointerException("PageViewDelegate：rootView not be null.")
        val resultView = view.findViewById<T?>(id)
        if (resultView == null) {
            throw NullPointerException("PageViewDelegate：findView result is null. please check the layoutRes")
        } else {
            return resultView
        }
    }

    fun setOnClick(view: View?, listener: View.OnClickListener) {
        view?.setOnClickListener(listener)
    }

    fun setOnClick(@IdRes id: Int, listener: View.OnClickListener) {
        setOnClick(findView<View>(id), listener)
    }

    fun setGone(@IdRes id: Int) {
        setGone(findView<View>(id))
    }

    fun setGone(view: View?) {
        if (view?.visibility != View.GONE) {
            view?.visibility = View.GONE
        }
    }

    fun setVisible(@IdRes id: Int) {
        setVisible(findView<View>(id))
    }

    fun setVisible(view: View?) {
        if (view?.visibility != View.VISIBLE) {
            view?.visibility = View.VISIBLE
        }
    }

    fun setVisibility(@IdRes id: Int, visibility: Int) {
        setVisibility(findView<View>(id), visibility)
    }

    fun setVisibility(view: View?, visibility: Int) {
        view?.visibility = visibility
    }

    fun setVisibility(id: Int, isVisible: Boolean) {
        setVisibility(findView<View>(id), isVisible)
    }

    fun setVisibility(view: View?, isVisible: Boolean) {
        if (isVisible) setVisible(view)
        else setGone(view)
    }

    fun setText(@IdRes id: Int, text: CharSequence?) {
        setText(findView<TextView>(id), text)
    }

    fun setText(@IdRes id: Int, @StringRes resId: Int) {
        setText(findView<TextView>(id), resId)
    }

    fun setText(textView: TextView?, @StringRes resId: Int) {
        setText(textView, getString(resId))
    }

    fun setText(textView: TextView?, text: CharSequence?) {
        textView?.text = text
    }

    fun setTextColor(textView: TextView?, @ColorInt color: Int) {
        textView?.setTextColor(color)
    }

    fun setTextColor(id: Int, @ColorInt color: Int) {
        findView<TextView>(id).setTextColor(color)
    }

    fun setTextSize(id: Int, size: Float) {
        findView<TextView>(id).textSize = size
    }

    fun setTextSize(textView: TextView?, size: Float) {
        textView?.textSize = size
    }

    fun setImageResource(imageView: ImageView?, @DrawableRes resId: Int) {
        imageView?.setImageResource(resId)
    }

    fun setImageBitmap(imageView: ImageView?, bitmap: Bitmap?) {
        imageView?.setImageBitmap(bitmap)
    }

    fun setImageDrawable(imageView: ImageView?, drawable: Drawable?) {
        imageView?.setImageDrawable(drawable)
    }

    fun showToast(content: String?) {
        ToastUtils.showToast(content)
    }

    fun showToast(@StringRes resId: Int) {
        showToast(getString(resId))
    }

    fun showLongToast(content: String?) {
        ToastUtils.showLong(content)
    }

    fun showLongToast(@StringRes resId: Int) {
        showLongToast(getString(resId))
    }

    fun setBackgroundColor(view: View?, @ColorInt color: Int) {
        view?.setBackgroundColor(color)
    }

    fun getString(@StringRes resId: Int): String? {
        return AppUtils.getString(resId)
    }

    fun showTipsDialog(context: Context?, msg: String, title: String?, confirmText: String?) {
        context?.apply {
            MaterialTipsDialog.Builder(this, msg, title, confirmText).show()
        }
    }

    fun showConfirmDialog(context: Context?, msg: String, title: String?, confirmText: String?, listener: DialogInterface.OnClickListener?) {
        context?.apply {
            MaterialConfirmDialog.Builder(this, msg, title, confirmText, listener).show()
        }
    }

    fun isFullScreen(): Boolean {
        return !isPortScreen()
    }

    fun isPortScreen(): Boolean {
        return ViewUtils.isPort()
    }

    fun setFullScreen(activity: Activity?, isFullScreen: Boolean) {
        val orientation = if (isFullScreen) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        activity?.requestedOrientation = orientation
    }

    fun startActivity(context: Context?, clazz: Class<*>) {
        context?.apply {
            startActivity(Intent(this, clazz))
        }
    }

    fun startActivity(context: Context?, intent: Intent) {
        context?.apply {
            startActivity(intent)
        }
    }

    fun startFragment(context: Context?, fragment: BaseFragment) {
        if (context is BaseActivity) {
            context.start(fragment)
        }
    }

    fun getApplication(): BaseApplication {
        return BaseApplication.getApplication()
    }

    fun getColor(@ColorRes id: Int): Int {
        return ViewUtils.getColor(id)
    }

    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ViewUtils.getDrawable(id)
    }


    // ----------------------- 页面状态处理相关 ---------------------
    // ----------------------- 页面状态处理相关 ---------------------
    // ----------------------- 页面状态处理相关 ---------------------
    private var mLoadingDialog: AlertDialog? = null

    fun showPageLoadingDialog(context: Context?, msg: String? = getString(R.string.app_loading)) {
        if (mLoadingDialog?.isShowing == true) {
            return
        }
        context?.let {
            mLoadingDialog = MaterialLoadingDialog.Builder(context).show(msg)
        }
    }

    fun hidePageLoadingDialog() {
        if (mLoadingDialog?.isShowing == true) {
            mLoadingDialog?.dismiss()
            mLoadingDialog = null
        }
    }

    fun showPageLoading(placeholder: PlaceholderView?) {
        placeholder?.showLoading()
    }

    fun hidePageLoading(placeholder: PlaceholderView?) {
        if (placeholder?.isLoading() == true) {
            placeholder.hideLoading()
        }
    }

    fun showPageEmpty(placeholder: PlaceholderView?, msg: String?) {
        placeholder?.showEmpty(msg)
    }


    fun showPageError(placeholder: PlaceholderView?, msg: String?) {
        placeholder?.showError(msg)
    }

    fun setPageErrorRetryListener(placeholder: PlaceholderView?, listener: () -> Unit) {
        placeholder?.setPageErrorRetryListener(listener)
    }


    fun showPageContent(placeholder: PlaceholderView?) {
        placeholder?.showContent()
    }

    fun setGoneEmpty(placeholder: PlaceholderView?) {
        placeholder?.setGoneEmpty()
    }

    // ----------------------- 页面标题处理相关 ---------------------
    // ----------------------- 页面标题处理相关 ---------------------
    // ----------------------- 页面标题态处理相关 ---------------------

    private var mPageTitle: String? = null

    fun getPageTitle(): String? = mPageTitle

    fun setPageTitle(tvTitle: TextView?, title: String?) {
        if (mPageTitle != title) {
            mPageTitle = title
        }
        tvTitle?.text = title
    }

    fun setShowToolBar(appBarLayout: AppBarLayout?, isShow: Boolean) {
        ViewUtils.setVisible(appBarLayout, isShow)
    }

    fun setShowTitleImage(image: ImageView?, isShow: Boolean) {
        ViewUtils.setVisible(image, isShow)
    }

    fun setStatusBarHeight(statusView: View?, isSetStatus: Boolean) {
        if (isSetStatus) {
            StatusBarUtils.setStatusBarHeight(statusView)
        }
    }

    fun setToolbarStyle(isMainPage: Boolean, toolbar: Toolbar?) {
        val toolbarTitleStyle = if (isMainPage) {
            R.style.ToolbarMainTextAppearance
        } else {
            R.style.ToolbarTextAppearance
        }
        toolbar?.setTitleTextAppearance(toolbar.context, toolbarTitleStyle)
    }

    fun setBackIcon(toolbar: Toolbar?, mainPage: Boolean, backIcon: Boolean, backColorIcon: Boolean, backClick: () -> Unit) {
        if (backIcon && !mainPage && toolbar != null) {
            toolbar.navigationIcon = ViewUtils.getImageThemeDrawable(toolbar.context, if (backColorIcon) R.mipmap.ic_arrow_back_white else R.mipmap.ic_arrow_back_black, R.attr.toolbarForeground)
            toolbar.setNavigationOnClickListener {
                backClick.invoke()
            }
        }
    }

    fun setContentView(navLayout: FrameLayout?, layResID: Int) {
        if (navLayout?.childCount ?: 0 > 0) {
            navLayout?.removeAllViews()
        }
        if (layResID != 0 && navLayout != null) {
            LayoutInflater.from(navLayout.context).inflate(layResID, navLayout)
        }
    }

    fun inflateMenu(toolbar: Toolbar?, menuResID: Int, itemClick: (itemId: Int) -> Boolean) {
        if (menuResID != 0) {
            toolbar?.inflateMenu(menuResID)
            toolbar?.setOnMenuItemClickListener {
                itemClick.invoke(it.itemId)
            }
        }
    }
}