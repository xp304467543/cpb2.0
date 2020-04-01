package com.fenghuang.baselib.base.recycler.header

import android.content.Context
import com.fenghuang.baselib.R
import com.scwang.smartrefresh.layout.header.ClassicsHeader

/**
 * @author Pinger
 * @since 18-12-10 下午2:53
 *
 * 菊花刷新头
 */
class RecyclerClassicsHeader(context: Context) : ClassicsHeader(context) {

    init {
        setPrimaryColorId(R.color.grey_f2)
        mFinishDuration = 150

        setTextSizeTitle(16f)
        setTextTimeMarginTop(3f)

        setDrawableArrowSize(18f)
        setDrawableProgressSize(21f)

        REFRESH_HEADER_PULLING = "下拉刷新"
        REFRESH_HEADER_RELEASE = "释放刷新"
    }
}