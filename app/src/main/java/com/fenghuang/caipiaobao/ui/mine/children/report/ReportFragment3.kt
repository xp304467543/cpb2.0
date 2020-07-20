package com.fenghuang.caipiaobao.ui.mine.children.report

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.mine.data.MineVipList
import kotlinx.android.synthetic.main.fragment_report_2.smartVip
import kotlinx.android.synthetic.main.fragment_report_3.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment3 : BaseMvpFragment<ReportFragment3P>() {

    var adapter: LevelNextAdapter? = null

    var page = 1

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ReportFragment3P()

    override fun getLayoutResID() = R.layout.fragment_report_3


    override fun initContentView() {

        adapter = LevelNextAdapter(getPageActivity())
        rvReport3.adapter = adapter
        rvReport3.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        smartVip3.setOnRefreshListener {
            page = 1
            mPresenter.getNextVip(page)
        }
        smartVip3.setOnLoadMoreListener {
            mPresenter.getNextVip(page)
        }
    }


    override fun initData() {
        mPresenter.getNextVip(page)
    }


    inner class LevelNextAdapter(context: Context) : BaseRecyclerAdapter<MineVipList>(context) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineVipList> {
            return LevelNextHolder(parent)
        }

        inner class LevelNextHolder(parent: ViewGroup) : BaseViewHolder<MineVipList>(getContext(), parent, R.layout.holder_item_vip) {
            override fun onBindData(data: MineVipList) {
                setText(R.id.tv_title_1,"团队人数")
                setText(R.id.tv_title_2,"投注金额")
                setText(R.id.tv_title_3,"返点金额")
                ImageManager.loadImg(data.avatar, findView(R.id.imgPhotoUser))
                val userLevel = findView<TextView>(R.id.tvNameUser)
                setText(R.id.tvTimeUser, TimeUtils.getYearMonthDay(data.created ?: 0))
                setText(R.id.tv1_vip, data.recharge)
                setText(R.id.tv2_vip, data.exchange )
                setText(R.id.tv3_vip, data.brokerage)
                val imgLevel= findView<ImageView>(R.id.imgLevel)
                when (data.level) {
                    "1","0" -> {
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