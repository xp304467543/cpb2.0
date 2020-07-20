package com.fenghuang.caipiaobao.ui.mine.children.report

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.activity.BaseNavActivity
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.StatusBarUtils.setStatusBarHeight
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MineVipList
import kotlinx.android.synthetic.main.act_mine_report_search.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/7/9
 * @ Describe
 *
 */
class MineReportSearchAct : BaseNavActivity() {

    var adapter: LevelAdapter? = null

    override fun isOverride() = false

    override fun isShowBackIconWhite() = false


    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.act_mine_report_search

    override fun isShowToolBar() = false


    override fun initContentView() {
        setStatusBarHeight(stateView)
        adapter = LevelAdapter(this)
        rvSearch.adapter = adapter
        rvSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        etSearchName.setText(intent.getStringExtra("searchName") ?: "")
    }

    override fun initData() {
        if ((intent.getStringExtra("searchName") ?: "").isNotEmpty()) {
            MineApi.getVipLevel(sub_nickname = intent.getStringExtra("searchName")
                    ?: "", page = 1,is_sub = 1) {
                onSuccess {
                    if (!it.isNullOrEmpty()) {
                        setGone(linSearchHolder)
                        adapter?.addAll(it)
                    } else setVisible(linSearchHolder)
                }
                onFailed {
                    adapter?.clear()
                    rvSearch.removeAllViews()
                    setVisible(linSearchHolder)
                }
            }
        }

    }


    inner class LevelAdapter(context: Context) : BaseRecyclerAdapter<MineVipList>(context) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineVipList> {
            return LevelHolder(parent)
        }

        inner class LevelHolder(parent: ViewGroup) : BaseViewHolder<MineVipList>(getContext(), parent, R.layout.holder_item_vip) {
            @SuppressLint("SetTextI18n")
            override fun onBindData(data: MineVipList) {
                ImageManager.loadImg(data.avatar, findView(R.id.imgPhotoUser))
                val userLevel = findView<TextView>(R.id.tvNameUser)
                setText(R.id.tvTimeUser, TimeUtils.getYearMonthDay(data.created ?: 0))
                setText(R.id.tv1_vip, data.recharge)
                setText(R.id.tv2_vip, data.exchange)
                setText(R.id.tv3_vip, data.brokerage)
                val imgLevel = findView<ImageView>(R.id.imgLevel)
                when (data.level) {
                    "1", "0" -> {
                        imgLevel.setBackgroundResource(R.mipmap.ic_v_1)
                    }
                    "2" -> {
                        imgLevel.setBackgroundResource(R.mipmap.ic_v_2)
                    }
                    "3" -> {
                        imgLevel.setBackgroundResource(R.mipmap.ic_v_3)
                    }
                    "4" -> {
                        imgLevel.setBackgroundResource(R.mipmap.ic_v_4)
                    }
                    "5" -> {
                        imgLevel.setBackgroundResource(R.mipmap.ic_v_5)
                    }
                    "6" -> {
                        imgLevel.setBackgroundResource(R.mipmap.ic_v_6)
                    }
                }
                userLevel.text = data.nickname
            }
        }
    }
}