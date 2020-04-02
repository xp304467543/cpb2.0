package com.fenghuang.caipiaobao.ui.personal

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.ViewGroup
import android.widget.LinearLayout
import com.fenghuang.baselib.base.mvp.BaseMvpActivity
import com.fenghuang.baselib.utils.TimeUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.baselib.widget.round.RoundTextView
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserConstant
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.HomePresenter
import com.fenghuang.caipiaobao.ui.personal.data.UserPageGift
import com.fenghuang.caipiaobao.ui.personal.data.UserPageResponse
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import kotlinx.android.synthetic.main.fragment_presonal_user.*
import kotlin.random.Random

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-01
 * @ Describe 用户主页
 *
 */
class UserPersonalPage : BaseMvpActivity<UserPersonalPagePresenter>() {

    var attentionNum = 0

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = UserPersonalPagePresenter()

    override fun isSwipeBackEnable() = true

    override fun isOverride() = true

    override val layoutResID = R.layout.fragment_presonal_user


    override fun initContentView() {
        if (intent != null) {
            if (UserInfoSp.getUserId().toString() == intent.getStringExtra(UserConstant.FOLLOW_ID) ?: "0") {
                setGone(btUserAttention)
            }
        }
    }

    override fun initData() {
        if (intent != null) {
            showPageLoadingDialog()
            mPresenter.getUserInfo(intent.getStringExtra(UserConstant.FOLLOW_ID) ?: "0")
        }
    }


    fun initInfo(data: UserPageResponse) {
        attentionNum = data.fans!!
        ImageManager.loadImg(data.avatar, imgUserPhoto)
        tvUserName.text = data.nickname
        tvUserDescription.text = data.profile
        tvAttention.text = data.follow.toString()
        tvGiftNum.text = data.all_gift
        tvZan.text = data.zan
        tvFans.text = data.fans.toString()
        tvUserId.text = data.unique_id
        initGiftView(data.gift)
        when (data.vip) {
            "1" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip_1)
            "2" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip_2)
            "3" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip_3)
            "4" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip_4)
            "5" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip_5)
            "6" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip_6)
            "7" -> imgLevel.background = ViewUtils.getDrawable(R.mipmap.vip_7)
        }
        when {
            data.gender == "1" -> userSex.text = "男"
            data.gender == "0" -> userSex.text = "女"
            else -> userSex.text = "未知"
        }
        tvEnterTime.text = TimeUtils.longToDateString(data.created.toLong())
        if (data.is_follow) {
            btUserAttention.background = ViewUtils.getDrawable(R.drawable.button_grey_background)
            btUserAttention.setTextColor(ViewUtils.getColor(R.color.grey_e6))
            btUserAttention.text = "已关注"
        }
        hidePageLoadingDialog()
    }

    override fun initEvent() {
        imgBack.setOnClickListener {
            finish()
        }
        btUserAttention.setOnClickListener {
            if (FastClickUtils.isFastClick()) {
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(this, false)
                    return@setOnClickListener
                }
                val presenter = HomePresenter()
                presenter.attention("", intent.getStringExtra(UserConstant.FOLLOW_ID)!!)
                presenter.setSuccessClickListener {
                    if (!it) {
                        btUserAttention.background = ViewUtils.getDrawable(R.drawable.button_background)
                        btUserAttention.text = "+ 关注"
                        btUserAttention.setTextColor(ViewUtils.getColor(R.color.white))
                        if (attentionNum > 0) attentionNum -= 1
                        tvFans.text = attentionNum.toString()
                    } else {
                        btUserAttention.background = ViewUtils.getDrawable(R.drawable.button_grey_background)
                        btUserAttention.setTextColor(ViewUtils.getColor(R.color.grey_e6))
                        btUserAttention.text = "已关注"
                        attentionNum += 1
                        tvFans.text = attentionNum.toString()
                    }
                }
                presenter.setFailClickListener {
                    GlobalDialog.ShowError(this, it)
                }
            }
        }

        imgBack.setOnClickListener {
            finish()
        }
    }

    //送出的礼物
    private fun initGiftView(data: List<UserPageGift>?) {
        val color = arrayOf("#FFEFED", "#FFF4E3", "#E9F8FF", "#E9F8FF", "#FFF4E3", "#FFEFED")
        if (!data.isNullOrEmpty()) {
            //往容器内添加TextView数据
            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(15, 20, 15, 5)
            if (flowLayout != null) {
                flowLayout.removeAllViews()
            }
            for (i in data) {
                val tv = RoundTextView(this)
                tv.setPadding(28, 10, 28, 10)
                val builder = SpannableStringBuilder(i.gift_name + "  ")
                val length = builder.length
                builder.append("x" + i.num)
                builder.setSpan(ForegroundColorSpan(ViewUtils.getColor(R.color.color_FF513E)), length, builder.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                tv.text = builder
                tv.maxEms = 10
                tv.setSingleLine()
                tv.textSize = 12f
                tv.setTextColor(ViewUtils.getColor(R.color.color_333333))
                tv.delegate.cornerRadius = 15
                tv.layoutParams = layoutParams
                tv.delegate.backgroundColor = Color.parseColor(color[Random.nextInt(5)])
                flowLayout.addView(tv, layoutParams)
            }
        }
    }


}