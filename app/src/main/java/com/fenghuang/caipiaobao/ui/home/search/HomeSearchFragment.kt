package com.fenghuang.caipiaobao.ui.home.search

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.fenghuang.baselib.base.mvp.BaseMvpFragment
import com.fenghuang.caipiaobao.R
import kotlinx.android.synthetic.main.fragment_search.*
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.widget.round.RoundTextView
import com.fenghuang.caipiaobao.constant.IntentConstant
import com.fenghuang.caipiaobao.ui.home.data.HomeAnchorRecommend
import com.fenghuang.caipiaobao.ui.home.data.HomeAnchorSearch
import com.fenghuang.caipiaobao.ui.home.live.LiveRoomActivity
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.LaunchUtils


/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-07
 * @ Describe  搜索
 *
 */


class HomeSearchFragment : BaseMvpFragment<HomeSearchFragmentPresenter>() {

    private lateinit var resultAdapter: HomeSearchAdapter

    private lateinit var relatedAdapter: HomeSearchAdapter

    override fun attachView() = mPresenter.attachView(this)

    override fun attachPresenter() = HomeSearchFragmentPresenter()

    override fun isOverridePage() = false

    override fun isShowToolBar() = false

    override fun isSwipeBackEnable() = true

    override fun getContentResID() = R.layout.fragment_search

    override fun initContentView() {
        //查询结果
        resultAdapter = HomeSearchAdapter(context!!)
        rvResult.adapter = resultAdapter
        rvResult.layoutManager = GridLayoutManager(context, 2)

        //查询推荐
        relatedAdapter = HomeSearchAdapter(context!!)
        rvRelated.adapter = relatedAdapter
        rvRelated.layoutManager = GridLayoutManager(context, 2)
    }

    override fun initData() {
        mPresenter.getAnchorPop()
    }


    override fun initEvent() {
        tvClose.setOnClickListener {
            pop()
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!TextUtils.isEmpty(s)) {
                    setGone(initRecommend)
                    mPresenter.search(s.toString())
                } else {
                    setVisible(initRecommend)
                    setGone(resultSearch)
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }


    //主播推荐
    fun initAnchorPop(data: List<HomeAnchorRecommend>) {
        if (!data.isNullOrEmpty()){
            //往容器内添加TextView数据
            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(15, 20, 15, 5)
            if (flowLayout != null) {
                flowLayout.removeAllViews()
            }
            for (i in data) {
                val tv = RoundTextView(context)
                tv.setPadding(28, 10, 28, 10)
                tv.text = i.nickname
                tv.maxEms = 10
                tv.setSingleLine()
                tv.textSize = 12f
                tv.setTextColor(getColor(R.color.color_333333))
                tv.delegate.cornerRadius = 15
                tv.delegate.backgroundColor = getColor(R.color.color_F5F7FA)
                tv.layoutParams = layoutParams
                tv.setOnClickListener {
                    if (FastClickUtils.isFastClick()) {
                        val intent = Intent(context, LiveRoomActivity::class.java)
                        intent.putExtra(IntentConstant.LIVE_ROOM_ANCHOR_ID, i.id)
                        intent.putExtra(IntentConstant.LIVE_ROOM_ANCHOR_STATUE, i.live_status)
                        intent.putExtra(IntentConstant.LIVE_ROOM_NAME, i.name)
                        intent.putExtra(IntentConstant.LIVE_ROOM_AVATAR, i.avatar)
                        intent.putExtra(IntentConstant.LIVE_ROOM_NICK_NAME, i.nickname)
                        intent.putExtra(IntentConstant.LIVE_ROOM_ONLINE, i.online)
                        LaunchUtils.startActivity(context, intent)
                    }
                }
                flowLayout.addView(tv, layoutParams)
            }
        }

    }

    //搜索结果
    fun upDateView(data: HomeAnchorSearch) {
        setVisible(resultSearch)
        if (!data.result.isNullOrEmpty()) {
            setGone(tvNoResult)
            setVisible(searchResult)
            rvResult.removeAllViews()
            resultAdapter.clear()
            resultAdapter.notifyDataSetChanged()
            resultAdapter.addAll(data.result)

        }else{
            setVisible(tvNoResult)
            setGone(searchResult)
        }
        if (!data.related.isNullOrEmpty()) {
            rvRelated.removeAllViews()
            relatedAdapter.clear()
            relatedAdapter.notifyDataSetChanged()
            relatedAdapter.addAll(data.related)
        }
    }

}