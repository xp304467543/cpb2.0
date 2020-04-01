package com.fenghuang.caipiaobao.ui.home

import android.widget.ImageView
import com.fenghuang.baselib.base.mvp.BaseMvpPresenter
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.manager.ImageManager
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.home.data.HomeLivePreResponse
import com.fenghuang.caipiaobao.ui.home.data.UpDatePreView

import com.fenghuang.caipiaobao.ui.mine.data.MineApi
import com.fenghuang.caipiaobao.utils.FastClickUtils
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.LaunchUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hwangjr.rxbus.RxBus
import com.pingerx.rxnetgo.exception.ApiException
import com.pingerx.rxnetgo.rxcache.CacheMode
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.my_top_bar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/26- 14:47
 * @ Describe
 *
 */
class HomePresenter : BaseMvpPresenter<HomeFragment>() {


    //获取首页数据
    fun getAllData(cacheMode: CacheMode) {
        if (mView.isActive()) {

            val uiScope = CoroutineScope(Dispatchers.Main)

            uiScope.launch {


                val getHomeBannerResult = async {
                    HomeApi.getHomeBannerResult(cacheMode)
                }

                val getHomeSystemNoticeResult = async {
                    HomeApi.getHomeSystemNoticeResult(cacheMode)
                }

                val getHomeLotteryTypeResult = async {
                    HomeApi.getHomeLotteryTypeResult(cacheMode)
                }

                val getHomeHotLive = async {
                    HomeApi.getHomeHotLive(cacheMode)
                }

                val getNews = async {
                    HomeApi.getNews()
                }

                val getAd = async {
                    HomeApi.getAd()
                }

                val getHomeAnchorRecommend = async {
                    HomeApi.getHomeAnchorRecommend(cacheMode)
                }

                val getHomeLivePreView = async {
                    HomeApi.getHomeLivePreView(cacheMode)
                }

                val expertRed = async {
                    HomeApi.expertRed()
                }

                val resultGetHomeBannerResult = getHomeBannerResult.await()
                val resultGetHomeSystemNoticeResult = getHomeSystemNoticeResult.await()
                val resultGetHomeLotteryTypeResult = getHomeLotteryTypeResult.await()
                val resultGetHomeHotLive = getHomeHotLive.await()
                val resultGetNews = getNews.await()
                val resultGetAd = getAd.await()
                val resultGetHomeAnchorRecommend = getHomeAnchorRecommend.await()
                val resultExpertRed = expertRed.await()
                val resultGetHomeLivePreView = getHomeLivePreView.await()


                resultGetHomeBannerResult.onSuccess { mView.upDateBanner(it) }

                resultGetHomeSystemNoticeResult.onSuccess { mView.upDateSystemNotice(it) }

                resultGetHomeLotteryTypeResult.onSuccess {
                    mView.upDateLotteryType(it)
                }

                resultGetHomeHotLive.onSuccess {
                    mView.hotLiveAdapter?.clear()
                    mView.rvHotLive.removeAllViews()
                    if (it.size > 6) mView.hotLiveAdapter?.addAll(it.subList(0, 6)) else mView.hotLiveAdapter?.addAll(it)
                }

                resultGetNews.onSuccess {
                    mView.newsAdapter?.clear()
                    if (it.size > 3) mView.newsAdapter?.addAll(it.subList(0, 3)) else mView.newsAdapter?.addAll(it)
                }

                resultGetAd.onSuccess { ImageManager.loadImg(it[0].image_url, mView.imgAd) }

                resultGetHomeAnchorRecommend.onSuccess {
                    mView.anchorRecommendAdapter?.clear()
                    if (it.size > 6) mView.anchorRecommendAdapter?.addAll(it.subList(0, 6)) else mView.anchorRecommendAdapter?.addAll(it)
                }

                resultExpertRed.onSuccess {
                    mView.expertHotAdapter?.clear()
                    if (it.size > 4) mView.expertHotAdapter?.addAll(it.subList(0, 4)) else mView.expertHotAdapter?.addAll(it)
                }

                resultGetHomeLivePreView.onSuccess { getLivePreView(it) }

                mView.homeSmartRefreshLayout.finishRefresh()

            }

        }

    }

    /**
     * 单独更新预告
     */
    fun upDatePreView() {
        HomeApi.getHomeLivePreView(CacheMode.NONE)
                .onSuccess {
                    if (mView.isActive()) {
                        getLivePreView(it)
                    }
                }
    }


    /**
     * 直播预告 特殊的
     */
    private fun getLivePreView(result: String) {
        if (result.length > 10) {
            //将JSON的String 转成一个JsonArray对象
            val jsonArray = JsonParser().parse(result).asJsonArray
            //遍历JsonArray
            val userBeanList = arrayListOf<HomeLivePreResponse>()
            for (data in jsonArray) {
                userBeanList.add(Gson().fromJson(data, HomeLivePreResponse::class.java))
            }
            mView.rvLivePreview.removeAllViews()
            mView.preLiveAdapter?.clear()
            mView.preLiveAdapter?.addAll(userBeanList)
            mView.preLiveAdapter?.setAvatarListener {
                if (FastClickUtils.isFastClick()) {
                    if (userBeanList[it].livestatus == "1") {
                        LaunchUtils.startLive(mView.requireActivity(), userBeanList[it].aid,
                                userBeanList[it].livestatus,
                                userBeanList[it].name, userBeanList[it].avatar, userBeanList[it].nickname, 0)
                    } else {
                        LaunchUtils.startPersonalPage(mView.requireActivity(), userBeanList[it].aid, 2)
                    }
                }

            }
            //关注点击
            mView.preLiveAdapter!!.setAttentionListener {
                if (!UserInfoSp.getIsLogin()) {
                    GlobalDialog.notLogged(mView.requireActivity())
                } else {
                    if (FastClickUtils.isFastClick()) {
                        attention(userBeanList[it].aid, "")
                    } else ToastUtils.show("请勿重复点击")

                }
            }
        }
    }


    //获取新消息
    fun getNewMsg() {
        MineApi.getIsNewMessage {
            onSuccess {
                if (it.msgCount > 0) {
                    mView.setVisible(mView.topDian)
                } else {
                    mView.setGone(mView.topDian)
                }
                mView.msg1 = it.countList.`_$0`
                mView.msg2 = it.countList.`_$2`
                mView.msg3 = it.countList.`_$3`
            }
        }
    }

    private var attentionSuccessListen: ((boolean: Boolean) -> Unit)? = null
    private var attentionFailListen: ((e: ApiException) -> Unit)? = null
    fun attention(anchor_id: String, follow_id: String) {
        HomeApi.attentionAnchorOrUser(anchor_id = anchor_id, follow_id = follow_id) {
            onSuccess {
                attentionSuccessListen?.invoke(it.isFollow)
                RxBus.get().post(UpDatePreView(true))
            }
            onFailed { attentionFailListen?.invoke(it) }
        }
    }

    fun setSuccessClickListener(listener: (boolean: Boolean) -> Unit) {
        attentionSuccessListen = listener
    }

    fun setFailClickListener(listener: (e: ApiException) -> Unit) {
        attentionFailListen = listener
    }

    /**
     * 关注专家
     */
    private var attentionExpertSuccessListen: ((boolean: Boolean) -> Unit)? = null
    private var attentionExpertFailListen: ((e: ApiException) -> Unit)? = null
    fun attentionExpert(expert_id: String) {
        HomeApi.attentionExpert(expert_id) {
            onSuccess {
                val json = JsonParser().parse(it).asJsonObject.get("is_followed").asString
                attentionExpertSuccessListen?.invoke((json == "1"))
            }
            onFailed { attentionExpertFailListen?.invoke(it) }
        }
    }

    fun setSuccessExpertClickListener(listener: (boolean: Boolean) -> Unit) {
        attentionExpertSuccessListen = listener
    }

    fun setFailExpertClickListener(listener: (e: ApiException) -> Unit) {
        attentionExpertFailListen = listener
    }

}