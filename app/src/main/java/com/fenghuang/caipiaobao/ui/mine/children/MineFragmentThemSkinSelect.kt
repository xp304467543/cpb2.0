package com.fenghuang.caipiaobao.ui.mine.children

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.baselib.utils.StatusBarUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.UpDateUserPhoto
import com.fenghuang.caipiaobao.ui.mine.data.ChangeSkin
import com.fenghuang.caipiaobao.ui.mine.data.MineThemSkinInfoResponse
import com.fenghuang.caipiaobao.widget.pagegridview.PageIndicator
import com.fenghuang.caipiaobao.widget.viewpager.GalleryAdapter
import com.fenghuang.caipiaobao.widget.viewpager.ZoomOutPageTransformer
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.thread.EventThread
import com.pingerx.banner.BannerView
import com.pingerx.banner.holder.BannerHolderCreator
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_them_skin_select.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-14
 * @ Describe
 *
 */

class MineFragmentThemSkinSelect : BaseMvpFragment<MineFragmentThemSkinSelectPresenter>() {


    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = MineFragmentThemSkinSelectPresenter()

    override fun getLayoutResID() = R.layout.fragment_them_skin_select

    override fun isOverridePage() = false

    override fun isSwipeBackEnable() = true

    override fun isRegisterRxBus() = true

    override fun isShowBackIconWhite() = false

    override fun initContentView() {
        StatusBarUtils.setStatusBarForegroundColor(getPageActivity(), true)
        setVisible(SkinLoadView)
        if (UserInfoSp.getSkinSelect() == arguments?.getString("skinId")!!.toInt()) {
            btSkinUser.text = "正在使用"
            btSkinUser.background = getDrawable(R.drawable.button_white_background)
            btSkinUser.setTextColor(getColor(R.color.grey_e6))
        }
    }

    override fun initData() {

        mPresenter.getSkin(arguments?.getString("skinId")!!)

    }


    fun upDataBanner(data: MineThemSkinInfoResponse) {
        tvSkinName.text = data.name
        ImageManager.loadImg(data.bg_image, imgSkinBg)
        viewPageSkin.adapter = GalleryAdapter(activity, data.images)
        viewPageSkin.offscreenPageLimit = 3
        val index = (data.images?.size!! / 2)
        viewPageSkin.currentItem = index
        viewPageSkin.setPageTransformer(true, ZoomOutPageTransformer())
        viewPageSkin.addOnPageChangeListener(PageIndicator(context, linDot, data.images?.size!!))
        setGone(SkinLoadView)
    }

    override fun initEvent() {
        imgSkinBack.setOnClickListener {
            pop()
        }
        btSkinUser.setOnClickListener {
            UserInfoSp.putSkinSelect(arguments?.getString("skinId")!!.toInt())
            btSkinUser.text = "正在使用"
            btSkinUser.background = getDrawable(R.drawable.button_white_background)
            btSkinUser.setTextColor(getColor(R.color.grey_e6))
            RxBus.get().post(ChangeSkin(arguments?.getString("skinId")!!.toInt()))
        }
    }



    companion object {
        fun newInstance(skinId: String): MineFragmentThemSkinSelect {
            val fragment = MineFragmentThemSkinSelect()
            val bundle = Bundle()
            bundle.putString("skinId", skinId)
            fragment.arguments = bundle
            return fragment
        }
    }
}