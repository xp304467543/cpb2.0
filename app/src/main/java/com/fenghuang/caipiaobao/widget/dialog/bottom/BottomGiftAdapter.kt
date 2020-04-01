package com.fenghuang.caipiaobao.widget.dialog.bottom

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-25
 * @ Describe
 *
 */

class BottomGiftAdapter(private val mViewList: List<View>?) : PagerAdapter() {

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(mViewList!![position])
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = mViewList!![position]
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return mViewList?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}