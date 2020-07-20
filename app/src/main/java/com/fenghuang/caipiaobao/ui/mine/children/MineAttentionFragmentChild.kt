package com.fenghuang.caipiaobao.ui.mine.children

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.fragment.BaseContentFragment
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.HomePresenter
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MineExpertBean
import com.fenghuang.caipiaobao.ui.mine.data.MineUserAttentionBean
import com.fenghuang.caipiaobao.ui.moments.childern.CommentOnFragment
import com.fenghuang.caipiaobao.ui.moments.data.MomentsHotDiscussResponse
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.fenghuang.caipiaobao.widget.WaveView
import kotlinx.android.synthetic.main.fragment_child_attention.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-04
 * @ Describe 我的关注
 *
 */

class MineAttentionFragmentChild() : BaseContentFragment() {

    lateinit var anchorAdapter: AnchorAdapter

    lateinit var userAdapter: UserAdapter

    lateinit var expertAdapter: ExpertAdapter

    override fun getContentResID() = R.layout.fragment_child_attention

    override fun onSupportVisible() {
        super.onSupportVisible()
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
    }



    override fun initContentView() {
        tvHolder.setCompoundDrawablesWithIntrinsicBounds(null, getDrawable(R.mipmap.ic_zanwuiguanzhu), null, null)
        attentionSmartRefreshLayout.setEnableRefresh(false)//是否启用下拉刷新功能
        attentionSmartRefreshLayout.setEnableLoadMore(false)//是否启用上拉加载功能
        attentionSmartRefreshLayout.setEnableOverScrollBounce(true)//是否启用越界回弹
        attentionSmartRefreshLayout.setEnableOverScrollDrag(true)//是否启用越界拖动（仿苹果效果）
        rvAttention.layoutManager = LinearLayoutManager(getPageActivity(), LinearLayoutManager.VERTICAL, false)
        when (arguments?.getInt("AttentionFragmentChildType")) {
            1 -> {
                anchorAdapter = AnchorAdapter()
                rvAttention.adapter = anchorAdapter
            }
            2 -> {
                userAdapter = UserAdapter()
                rvAttention.adapter = userAdapter
            }
            else -> {
                expertAdapter = ExpertAdapter()
                rvAttention.adapter = expertAdapter
            }
        }

    }

    override fun initData() {
        when (arguments?.getInt("AttentionFragmentChildType")) {
            1 -> MineApi.getAttentionList("0") {
                onSuccess {
                    if (!it.isNullOrEmpty()) anchorAdapter.addAll(it) else {
                        setVisible(tvHolder)
                        tvHolder.text = "您还没有关注任何主播哦~"
                    }
                }
            }
            2 -> MineApi.getAttentionList("1") {
                onSuccess {
                    if (!it.isNullOrEmpty()) userAdapter.addAll(it) else {
                        setVisible(tvHolder)
                        tvHolder.text = "您还没有关注任何用户哦~"
                    }

                }
            }
            else -> MineApi.getAttentionList {
                onSuccess {
                    if (!it.isNullOrEmpty()) expertAdapter.addAll(it) else {
                        setVisible(tvHolder)
                        tvHolder.text = "您还没有关注任何专家哦~"
                    }


                }
            }

        }
    }


    /**
     * 主播 adapter
     */
    inner class AnchorAdapter : BaseRecyclerAdapter<MineUserAttentionBean>(getPageActivity()) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineUserAttentionBean> {
            return AnchorHolder(parent)
        }

        inner class AnchorHolder(parent: ViewGroup) : BaseViewHolder<MineUserAttentionBean>(getContext(), parent, R.layout.holder_mine_attention) {
            override fun onBindData(data: MineUserAttentionBean) {
                setVisible(findView<TextView>(R.id.tvEndAttention))
                if (data.live_status == "1") {
                    findView<WaveView>(R.id.circleWave).setInitialRadius(60f)
                    findView<WaveView>(R.id.circleWave).start()
                }
                ImageManager.loadImg(data.avatar, findView(R.id.imgPhoto))
                setText(R.id.tvName, data.nickname)
                setText(R.id.tvSing, data.sign)

                setOnClick(R.id.btnDelete)
                setOnClick(R.id.imgPhoto)
                findView<TextView>(R.id.tvEndAttention).setOnClickListener {
                    if (FastClickUtils.isFastClick()) {
                        LaunchUtils.startLive(getPageActivity(),getData()?.anchor_id!!,getData()?.live_status!!,getData()?.lottery_id?:"",getData()?.avatar!!,getData()?.nickname!!,0
                        ,getData()?.lottery_id?:"1")
                    }
                }
            }

            override fun onClick(id: Int) {
                when (id) {
                    R.id.btnDelete -> {
                        if (FastClickUtils.isFastClick()) {
                            val presenter = HomePresenter()
                            presenter.attention(getData()?.anchor_id!!, "")
                            presenter.setSuccessClickListener {
                                anchorAdapter.remove(getDataPosition())
                            }
                            presenter.setFailClickListener {
                                GlobalDialog.showError(getPageActivity(), it)
                            }
                        }
                    }
                    R.id.imgPhoto -> {
                        if (FastClickUtils.isFastClick()) {
                            LaunchUtils.startPersonalPage(getContext(),getData()?.anchor_id!!,2)
                        }
                    }
                }
            }
        }
    }

    /**
     * 用户 adapter
     */
    inner class UserAdapter : BaseRecyclerAdapter<MineUserAttentionBean>(getPageActivity()) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineUserAttentionBean> {
            return UserHolder(parent)
        }

        inner class UserHolder(parent: ViewGroup) : BaseViewHolder<MineUserAttentionBean>(getContext(), parent, R.layout.holder_mine_attention) {
            override fun onBindData(data: MineUserAttentionBean) {
                ImageManager.loadImg(data.avatar, findView(R.id.imgPhoto))
                setText(R.id.tvName, data.nickname)
                setText(R.id.tvSing, data.sign)
                setOnClick(R.id.btnDelete)
                setOnClick(R.id.imgPhoto)
            }

            override fun onClick(id: Int) {
                when (id) {
                    R.id.btnDelete -> {
                        if (FastClickUtils.isFastClick()) {
                            val presenter = HomePresenter()
                            presenter.attention("", getData()?.user_id!!)
                            presenter.setSuccessClickListener {
                                userAdapter.remove(getDataPosition())
                            }
                            presenter.setFailClickListener {
                                GlobalDialog.showError(getPageActivity(), it)
                            }
                        }
                    }
                    R.id.imgPhoto -> {
                        if (FastClickUtils.isFastClick()) {
                            LaunchUtils.startPersonalPage(getContext(),getData()?.user_id!!,1)
                        }
                    }

                }
            }

        }

    }


    /**
     * 专家 adapter
     */
    inner class ExpertAdapter : BaseRecyclerAdapter<MineExpertBean>(getPageActivity()) {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineExpertBean> {
            return ExpertHolder(parent)
        }

        inner class ExpertHolder(parent: ViewGroup) : BaseViewHolder<MineExpertBean>(getContext(), parent, R.layout.holder_mine_attention) {
            override fun onBindData(data: MineExpertBean) {
                ImageManager.loadImg(data.avatar, findView(R.id.imgPhoto))
                setText(R.id.tvName, data.nickname)
                setText(R.id.tvSing, data.profile)
                setOnClick(R.id.btnDelete)
                setOnClick(R.id.imgPhoto)
            }

            override fun onClick(id: Int) {
                when (id) {
                    R.id.btnDelete -> {
                        if (FastClickUtils.isFastClick()) {
                            val presenter = HomePresenter()
                            presenter.attentionExpert(getData()?.expert_id!!)
                            presenter.setSuccessExpertClickListener {
                                expertAdapter.remove(getDataPosition())
                            }
                            presenter.setFailExpertClickListener {
                                GlobalDialog.showError(getPageActivity(),it)
                            }
                        }
                    }

                    R.id.imgPhoto -> {
                        if (FastClickUtils.isFastClick()) {
                            LaunchUtils.startPersonalPage(getContext(),getData()?.expert_id?:"0",3)
                        }
                    }
                }
            }
        }

    }

    companion object {
        fun newInstance(type:Int): MineAttentionFragmentChild {
            val fragment = MineAttentionFragmentChild()
            val bundle = Bundle()
            bundle.putInt("AttentionFragmentChildType", type)
            fragment.arguments = bundle
            return fragment
        }
    }

}