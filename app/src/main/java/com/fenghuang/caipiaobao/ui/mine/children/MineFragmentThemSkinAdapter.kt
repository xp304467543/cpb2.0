package com.fenghuang.caipiaobao.ui.mine.children

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.base.recycler.BaseRecyclerAdapter
import com.fenghuang.baselib.base.recycler.BaseViewHolder
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.mine.data.ChangeSkin
import com.fenghuang.caipiaobao.ui.mine.data.MineThemSkinResponse
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.hwangjr.rxbus.RxBus

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-14
 * @ Describe
 *
 */

class MineFragmentThemSkinAdapter(context: Context) : BaseRecyclerAdapter<MineThemSkinResponse>(context) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<MineThemSkinResponse> {
        return MineFragmentThemSkinHolder(parent)
    }

    inner class MineFragmentThemSkinHolder(parent: ViewGroup) : BaseViewHolder<MineThemSkinResponse>(getContext(), parent, R.layout.holder_mine_them_skin) {
        override fun onBindData(data: MineThemSkinResponse) {
            ImageManager.loadImg(data.cover, findView(R.id.imgSkin))
            setText(R.id.tvSkinName, data.name)
            setText(R.id.tvSkinContentDescription, data.users + " 人使用")
            if (UserInfoSp.getSkinSelect() == data.id.toInt()) {
                setVisible(findView<LinearLayout>(R.id.linSelect))
            } else setGone(findView<LinearLayout>(R.id.linSelect))
        }

        override fun onItemClick(data: MineThemSkinResponse) {
            if (data.id != "1") {
                LaunchUtils.startFragment(getContext(), MineFragmentThemSkinSelect.newInstance(data.id))
            } else {
                UserInfoSp.putSkinSelect(data.id.toInt())
                RxBus.get().post(ChangeSkin(data.id.toInt()))
                notifyDataSetChanged()
            }
            data.isSelect = !data.isSelect
        }

    }

}