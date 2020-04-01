package com.fenghuang.caipiaobao.widget.dialog.bottom

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.playerlibrary.AlivcLiveRoom.ScreenUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAnimatorBean
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveGiftList
import com.fenghuang.caipiaobao.ui.mine.MinePresenter
import com.fenghuang.caipiaobao.utils.NavigationBarUtil
import com.fenghuang.caipiaobao.widget.pagegridview.BottomGiftPageGridView
import com.fenghuang.caipiaobao.widget.pop.LiveGiftNumPop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.hwangjr.rxbus.RxBus
import kotlinx.android.synthetic.main.dialog_chat_bottom_gif.*


/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-25
 * @ Describe 横屏底部礼物栏
 *
 */

class BottomHorGiftWindow(context: Context) : BottomSheetDialog(context) {


    var viewPager: ViewPager? = null
    var liveGiftNumPop: LiveGiftNumPop? = null
    private var pagerAdapter: BottomGiftAdapter? = null
    private var viewList = arrayListOf<BottomGiftPageGridView>()
    private var homeLiveGiftListBean: HomeLiveGiftList? = null

    init {
        setContentView(R.layout.dialog_chat_bottom_hor_gif)
        val root = delegate?.findViewById<View>(R.id.design_bottom_sheet)
//        BottomSheetBehavior.from(root).isHideable = false
        delegate?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundColor(Color.TRANSPARENT)
        initViews()
        iniEvent()
    }

    private fun iniEvent() {
        tvGiftMount.setOnClickListener {
            liveGiftNumPop = LiveGiftNumPop(context)
            liveGiftNumPop?.showAtLocationTop(tvGiftMount, 5f)
            liveGiftNumPop?.getUserDiamondSuccessListener {
                tvGiftMount.text = it
            }
        }
        //送礼物
        tvSvgaGiftSend.setOnClickListener {
            if (homeLiveGiftListBean == null) {
                ToastUtils.show("请选择礼物")
                return@setOnClickListener
            }
            hideLoading()
            RxBus.get().post(HomeLiveAnimatorBean(homeLiveGiftListBean?.id!!, homeLiveGiftListBean?.name!!, homeLiveGiftListBean?.icon!!,
                    UserInfoSp.getUserId(), UserInfoSp.getUserPhoto().toString(), UserInfoSp.getUserNickName().toString(), tvGiftMount.text.toString()))
        }
        tvGiftSend.setOnClickListener {
            if (homeLiveGiftListBean == null) {
                ToastUtils.show("请选择礼物")
                return@setOnClickListener
            }
            hideLoading()
            RxBus.get().post(HomeLiveAnimatorBean(homeLiveGiftListBean?.id!!, homeLiveGiftListBean?.name!!, homeLiveGiftListBean?.icon!!,
                    UserInfoSp.getUserId(), UserInfoSp.getUserPhoto().toString(), UserInfoSp.getUserNickName().toString(), tvGiftMount.text.toString()))
        }
    }


    private fun initViews() {
        viewPager = findViewById(R.id.giftViewPager)
    }


     fun hideLoading() {
         if ( loadingView.visibility == View.VISIBLE){
             loadingView.visibility = View.GONE
         }else  loadingView.visibility = View.VISIBLE
    }

    fun setData(title: List<String>, content: List<List<HomeLiveGiftList>>) {
        if (!title.isNullOrEmpty() && !content.isNullOrEmpty()) {
            for ((index, tabText) in title.withIndex()) {
                chatGifTabView.addTab(chatGifTabView.newTab().setText(tabText))
                val view = BottomGiftPageGridView(context)
                view.setData(content[index])
                view.setOnItemClickListener { position, homeLiveGiftList ->
                    tvGiftMount.text = "1"
                    homeLiveGiftListBean = homeLiveGiftList
                    notifyAllData(homeLiveGiftList.name, homeLiveGiftList)
                }
                viewList.add(view)
            }
            pagerAdapter = BottomGiftAdapter(viewList)
            viewPager?.adapter = pagerAdapter
            viewPager?.offscreenPageLimit = 10
            viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                override fun onPageSelected(position: Int) {
                    if (chatGifTabView.getTabAt(position) != null) chatGifTabView.getTabAt(position)!!.select()
                }

            })
            chatGifTabView.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {}
                override fun onTabUnselected(p0: TabLayout.Tab?) {}
                override fun onTabSelected(p0: TabLayout.Tab?) {
                    viewPager?.currentItem = title.indexOf(p0?.text.toString())
                }

            })


            hideLoading()
        }
    }


    private fun notifyAllData(name: String, homeLiveGiftList: HomeLiveGiftList) {
        if (viewList.isNotEmpty()) {
            for (view in viewList) {
                view.notifyAllData(name)
            }
        }

        when (homeLiveGiftList.grade) {
            "middle", "high" -> {
                countLin.visibility = View.GONE
                tvSvgaGiftSend.visibility = View.VISIBLE
            }
            else -> {
                countLin.visibility = View.VISIBLE
                tvSvgaGiftSend.visibility = View.GONE
            }
        }

    }


    override fun onStart() {
        super.onStart()
        val mBehavior = BottomSheetBehavior.from(delegate?.findViewById<View>(R.id.design_bottom_sheet))
        mBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun show() {
        NavigationBarUtil.focusNotAle(window)
        super.show()
        NavigationBarUtil.hideNavigationBar(window)
        NavigationBarUtil.clearFocusNotAle(window)
    }

}