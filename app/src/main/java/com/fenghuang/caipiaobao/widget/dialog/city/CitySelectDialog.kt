package com.fenghuang.caipiaobao.widget.dialog.city

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.zyyoona7.picker.OptionsPickerView
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/6- 11:15
 * @ Describe 省 市 区
 *
 */

class CitySelectDialog(context: Context) : Dialog(context) {



    init {
        setContentView(R.layout.dialog_city)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.BOTTOM)
        val lp = window!!.attributes
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT // 宽度
        lp.dimAmount = 0.3f
        window!!.attributes = lp

        val p3List = ArrayList<CityEntity>(1)
        val c3List = ArrayList<List<CityEntity>>(1)
        val d3List = ArrayList<List<List<CityEntity>>>(1)
        ParseHelper.initThreeLevelCityList(context, p3List, c3List, d3List)
        val mOptionsPickerView = findViewById<OptionsPickerView<CityEntity>>(R.id.cityPickerView)
        mOptionsPickerView.setLinkageData(p3List, c3List, d3List)
        mOptionsPickerView.setTextSize(18f, true)
        mOptionsPickerView.setShowDivider(true)
        mOptionsPickerView.setVisibleItems(6)
        mOptionsPickerView.setDividerColor(ViewUtils.getColor(R.color.grey_dd))
        mOptionsPickerView.setLineSpacing(80f)
        mOptionsPickerView.setSelectedItemTextColor(ViewUtils.getColor(R.color.black))
        mOptionsPickerView.setNormalItemTextColor(ViewUtils.getColor(R.color.grey_e6))
        mOptionsPickerView.opt3SelectedPosition = 5
    }

}