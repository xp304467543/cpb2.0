package com.fenghuang.caipiaobao.ui.mine.children

import androidx.recyclerview.widget.GridLayoutManager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.base.recycler.decorate.GridItemSpaceDecoration
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.mine.data.ChangeSkin
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import kotlinx.android.synthetic.main.fragment_them_skin.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-14
 * @ Describe
 *
 */

class MineFragmentThemSkin : BaseMvpFragment<MineFragmentThemSkinPresenter>() {

    var skinAdapter: MineFragmentThemSkinAdapter? = null

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MineFragmentThemSkinPresenter()

    override fun getContentResID() = R.layout.fragment_them_skin

    override fun isOverridePage() = false

    override fun isSwipeBackEnable() = true

    override fun isRegisterRxBus() = true

    override fun getPageTitle() = getString(R.string.mine_them_skin)

    override fun isShowBackIconWhite() = false

    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
        setVisible(SkinLoadView)
        skinAdapter = MineFragmentThemSkinAdapter(context!!)
        rvThemSkin.layoutManager = GridLayoutManager(context, 2)
        rvThemSkin.adapter = skinAdapter
        if (rvThemSkin.itemDecorationCount == 0) {
            rvThemSkin.addItemDecoration(GridItemSpaceDecoration(2, topAndBottomSpace = ViewUtils.dp2px(0), startAndEndSpace = ViewUtils.dp2px(10), itemSpace = ViewUtils.dp2px(15)))
        }

    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    fun changeSkin(eventBean: ChangeSkin) {
        skinAdapter?.notifyDataSetChanged()

    }

    override fun initData() {
        mPresenter.getSkinList()
    }
}