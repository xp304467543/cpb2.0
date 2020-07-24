package com.fenghuang.caipiaobao.ui.mine.children.report

import android.annotation.SuppressLint
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
import com.fenghuang.caipiaobao.widget.dialog.SearchDialog
import kotlinx.android.synthetic.main.fragment_report_2.*
import kotlinx.android.synthetic.main.my_top_bar.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/6/27
 * @ Describe
 *
 */
class ReportFragment2 : BaseMvpFragment<ReportFragment2P>() {

    var adapter: LevelAdapter? = null

    var page = 1

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = ReportFragment2P()

    override fun getLayoutResID() = R.layout.fragment_report_2


    override fun initContentView() {

        adapter = LevelAdapter(getPageActivity())
        rvVip.adapter = adapter
        rvVip.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        smartVip.setOnRefreshListener {
            page = 1
            mPresenter.getVip(page)
        }
        smartVip.setOnLoadMoreListener {
            mPresenter.getVip(page)
        }
    }


    override fun initData() {
        mPresenter.getVip(page)
    }

    override fun initEvent() {

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
                setText(R.id.tvTimeUser, TimeUtils.getYearMonthDay((data.created?.times(1000)) ?: 0))
                setText(R.id.tv1_vip, data.recharge)
                setText(R.id.tv2_vip, data.exchange )
                setText(R.id.tv3_vip, data.brokerage)
                val imgLevel= findView<ImageView>(R.id.imgLevel)
                when (data.level) {
                    "1" -> {
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