package com.fenghuang.caipiaobao.ui.mine.children

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenghuang.baselib.base.fragment.BaseNavFragment
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeExpertList
import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.ui.mine.data.MineGroup
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.LaunchUtils
import kotlinx.android.synthetic.main.fragment_content_group.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020/5/29
 * @ Describe
 *
 */
class MineContentGroupFragment : BaseNavFragment() {

    override fun getContentResID() = R.layout.fragment_content_group

    override fun getPageTitle() = "官方交流群"

    override fun isOverridePage() = false

    override fun isSwipeBackEnable() = true

    override fun isShowBackIconWhite() = false

    lateinit var groupAdapter:GroupAdapter

    override fun initContentView() {
        groupAdapter = GroupAdapter(getPageActivity())
        rvGroup.layoutManager = LinearLayoutManager(getPageActivity(),LinearLayoutManager.VERTICAL,false)
        rvGroup.adapter = groupAdapter
    }

    override fun initData() {
        MineApi.getContentGroup {
            onSuccess {
                groupAdapter.addAll(it)
                groupAdapter.setOnItemClickListener { data, _ ->
                    if (FastClickUtils.isFastClick()){
                        LaunchUtils.starGlobalWeb(getPageActivity(),data.title,data.url)
                    }
                }
            }
            onFailed {
                ToastUtils.show("获取失败")
            }
        }
    }


    class GroupAdapter(context: Context) : BaseRecyclerAdapter<MineGroup>(context) {


        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineGroup> {
            return GroupHolder(parent)
        }

        inner class GroupHolder(parent: ViewGroup) : BaseViewHolder<MineGroup>(getContext(), parent, R.layout.holder_group_item) {
            override fun onBindData(data: MineGroup) {
                setText(R.id.tvGroupName,data.title)
                ImageManager.loadImg(data.icon, findView(R.id.imgIcon))
            }

        }
    }
}