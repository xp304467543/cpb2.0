package com.fenghuang.caipiaobao.widget.dialog.bottom

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R


/**
 *
 * @ Author  QinTian
 * @ Date  2020-04-14
 * @ Describe
 *
 */

abstract class BottomDialogFragment : DialogFragment() {


    var rootView: View? = null
    var window: Window? = null


    abstract val layoutResId: Int

    /**
     * 初始化View和设置数据等操作的方法
     */
    abstract fun initView()

    abstract fun initData()

    abstract fun initFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(layoutResId, container, false)
            initView()
            initData()
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
    }

    override fun onStart() {
        super.onStart()
        window = dialog.window
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setWindowAnimations(R.style.bottomDialog)
        window?.setLayout(-1, -2)
        window?.setDimAmount(0f)
        val params = window!!.attributes
        params.gravity = Gravity.BOTTOM
        params.width = resources.displayMetrics.widthPixels
        params.height = ViewUtils.getScreenHeight() * 2 / 3
        window?.attributes = params
    }


}