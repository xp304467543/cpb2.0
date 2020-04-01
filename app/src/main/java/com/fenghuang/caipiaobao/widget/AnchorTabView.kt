package com.fenghuang.caipiaobao.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.round.RoundTextView
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.layout_anchor_data_tab_view.view.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-04
 * @ Describe
 *
 */

class AnchorTabView : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
            context: Context, attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private var mListener: ((position: Int) -> Unit)? = null
    private var position = 0
    private var isAnchorTab = true

    private val tvTabLeft: TextView by lazy { findViewById<TextView>(R.id.tvTabLeft) }
    private val tvTabRight: TextView by lazy { findViewById<TextView>(R.id.tvTabRight) }
    private val roundLeftLine: RoundTextView by lazy { findViewById<RoundTextView>(R.id.roundLeftLine) }
    private val roundRightLine: RoundTextView by lazy { findViewById<RoundTextView>(R.id.roundRightLine) }
    private val rlAnchorTabLeft: RelativeLayout by lazy { findViewById<RelativeLayout>(R.id.rlAnchorTabLeft) }
    private val rlAnchorTabRight: RelativeLayout by lazy { findViewById<RelativeLayout>(R.id.rlAnchorTabRight) }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_anchor_data_tab_view, this)
    }

    /**
     * 选中状态，前面的选中，后面的未选中
     */
    private fun setTabSelectStyle(selectView: TextView, unSelectView: TextView) {
        selectView.setTextColor(ViewUtils.getColor(R.color.color_333333))
        selectView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        unSelectView.setTextColor(ViewUtils.getColor(R.color.color_333333))
        unSelectView.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    }

    /**
     * 选中状态，前面的选中，后面的未选中
     */
    private fun setTabSelectLineVisib(selectView: RoundTextView, unSelectView: RoundTextView) {
        selectView.visibility = View.VISIBLE
        unSelectView.visibility = View.GONE
    }

    fun setOnSelectListener(listener: (position: Int) -> Unit) {
        this.mListener = listener
    }


    fun setRankingTab() {
        initEvent()
        tvTabLeft.textSize = 14f
        tvTabRight.textSize = 14f
        setTabSelectStyle(tvTabLeft, tvTabRight)
    }

    fun setTabText(left: String, right: String) {
        initEvent()
        findViewById<TextView>(R.id.tvTabLeft)?.text = left
        findViewById<TextView>(R.id.tvTabRight)?.text = right
    }

    fun setTabSelect(position: Int) {
        this.position = position
        if (position == 0) {
            setTabSelectStyle(tvTabLeft, tvTabRight)
            setTabSelectLineVisib(roundLeftLine, roundRightLine)
        } else {
            setTabSelectStyle(tvTabRight, tvTabLeft)
            setTabSelectLineVisib(roundRightLine, roundLeftLine)
        }
    }

    fun setTabBackgroundColor(color: Int) {
        tabBack.setBackgroundColor(color)
    }

    private fun initEvent() {
        rlAnchorTabLeft.setOnClickListener {
            if (position != 0) {
                setTabSelectStyle(tvTabLeft, tvTabRight)
                setTabSelectLineVisib(roundLeftLine, roundRightLine)
                position = 0
                mListener?.invoke(position)
            }
        }
        rlAnchorTabRight.setOnClickListener {
            if (position != 1) {
                setTabSelectStyle(tvTabRight, tvTabLeft)
                setTabSelectLineVisib(roundRightLine, roundLeftLine)
                position = 1
                mListener?.invoke(position)
            }
        }
    }
}