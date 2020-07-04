package com.fenghuang.caipiaobao.ui.home.data

import android.graphics.pdf.PdfRenderer
import android.os.SystemClock
import android.text.TextUtils
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.data.api.*
import com.fenghuang.caipiaobao.data.bean.BaseApiBean
import com.pingerx.rxnetgo.rxcache.CacheMode
import android.widget.Toast


/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/26- 16:00
 * @ Describe
 *
 */
object HomeApi : BaseApi {

    //轮播图
    private const val HOME_BANNER_LIST = "/api/v1_1/user/get_banner/"

    //系统公告
    private const val HOME_SYSTEM_NOTICE = "/api/v1_1/user/system_notice/"

    //游戏直播列表
    private const val HOME_GAME_LIST = "/api/v1_1/live/get_game_list/"

    //热门直播
    private const val HOME_HOT_LIVE = "/api/v1_1/live/get_hot_list/"

    //直播预告
    private const val HOME_LIVE_PREVIEW = "/api/v1_1/user/anchor_pop/"

    //主播预告
    private const val Home_LIVE_ADVANCE = "/api/v1_1//user/anchor_advance/"

    //最新资讯
    private const val HOME_NEWS = "/api/v1_1/info/getInfoList/"

    //广告图
    private const val HOME_AD = "/api/v1_1/user/get_ad_banner/"

    //主播推荐
    private const val HOME_ANCHOR_RECOMMEND = "/api/v1_1/live/get_expert_list/"

    //专家红单
    private const val HOME_EXPERT_RED = "/plan/expert-list/"

    //初始化直播间
    private const val HOME_INIT_LIVE_ROOM = "/api/v1_1/live/get_live_room/"

    //初始20条消息
    private const val HOME_INIT_TWENTY_NEWS = "/api/v1_1/live/initChat/"

    //初始直播间打赏排行
    private const val HOME_INIT_REWARD_LIST = "/api/v1_1/live/get_reward_list/"

    //主播信息
    private const val HOME_LIVE_ANCHOR_INFO = "api/v1_1/live/get_anchor_info"

    //主播动态
    const val HOME_LIVE_ANCHOR_ANCHOR_DYNAMIC = "/api/v1_1/live/get_anchor_dynamic/"

    //礼物列表
    private const val HOME_LIVE_GIFT_LIST = "/api/v1_1/live/get_gift_list/"

    //资讯
    private const val NEWS_LIST = "/api/v1_1/info/getInfoList/"

    //资讯详情
    private const val NEWS_INFO = "/api/v1_1/info/getInfoDetail/"

    //搜索 主播推荐
    private const val HOME_SEARCH_POP = "/api/v1_1/live/pop_search/"

    //搜索
    private const val HOME_SEARCH = "/api/v1_1/live/search/"

    //管理员清屏
    private const val HOME_MANAGER_CLEAR = "/api/v1_1/live/clear_chat/"

    //禁言
    private const val FORBIDDEN_WORDS = "/api/v1_1/live/ban_words/"

    //发红包
    private const val HOME_LIVE_SEND_RED_ENVELOPE = "/api/v1_1/user/send_red/"

    //直播间红包队列
    private const val HOME_LIVE_RED_RECEIVE_ROOM = "/api/v1_1/live/get_room_red/"

    //抢红包
    private const val HOME_LIVE_RED_RECEIVE = "/api/v1_1/user/receive_red/"

    //直播预告标题
    private const val HOME_LIVE_ADVANCE = "/api/v1_1/live/get_room_list/"

    //关注
    private const val HOME_ATTENTION_ANCHCOR = "/api/v1_1/live/follow/"

    //所有主播
    private const val HOME_ALL_ANCHOR = "/api/v1_1/live/get_all_list/"

    //送礼物
    private const val HOME_LIVE_SEND_GIFT = "/api/v1_1/live/send_gift/"

    //购彩
    private const val LOTTERY_URL = "/api/v1_1/user/jump_to/"

    //关注专家
    private const val FOLLOW_EXPERT = "/plan/follow/"

    //版本更新
    private const val VERSION_UPDATE = "/api/common/init/"

    //系统公告
    private const val SYSTEM_NOTICE = "/api/v1_1/live/get_notice/"

    /**
     * 获取首页轮播图列表
     */
    fun getHomeBannerResult(cacheMode: CacheMode): ApiSubscriber<List<HomeBannerResponse>> {
        val subscriber = object : ApiSubscriber<List<HomeBannerResponse>>() {}
        getApi()
                .get<List<HomeBannerResponse>>(HOME_BANNER_LIST)
                .cacheMode(cacheMode)
                .subscribe(subscriber)
        return subscriber
    }

    /**
     * 获取公告
     */
    fun getHomeSystemNoticeResult(cacheMode: CacheMode): ApiSubscriber<List<HomeSystemNoticeResponse>> {
        val subscriber = object : ApiSubscriber<List<HomeSystemNoticeResponse>>() {}
        getApi()
                .get<List<HomeSystemNoticeResponse>>(HOME_SYSTEM_NOTICE)
                .cacheMode(cacheMode)
                .subscribe(subscriber)
        return subscriber
    }

    /**
     * 获取彩票类型列表  1.彩票 2.红包
     */
    fun getHomeLotteryTypeResult(cacheMode: CacheMode): AllSubscriber {
        val subscriber = AllSubscriber()
        getApi()
                .get<BaseApiBean>(HOME_GAME_LIST)
                .cacheMode(cacheMode)
                .subscribe(subscriber)
        return subscriber
    }

    /**
     * 获取热门推荐
     */
    fun getHomeHotLive(cacheMode: CacheMode): ApiSubscriber<List<HomeHotLiveResponse>> {
        val subscriber = object : ApiSubscriber<List<HomeHotLiveResponse>>() {}
        getApi()
                .get<List<HomeHotLiveResponse>>(HOME_HOT_LIVE)
                .cacheMode(cacheMode)
                .headers("token", UserInfoSp.getToken())
                .subscribe(subscriber)
        return subscriber
    }

    /**
     * 获取直播预告
     */
    fun getHomeLivePreView(cacheMode: CacheMode): EmptySubscriber {
        val subscriber = EmptySubscriber()
        getApi().get<String>(HOME_LIVE_PREVIEW)
                .cacheMode(cacheMode)
                .params("user_id", UserInfoSp.getUserId())
                .subscribe(subscriber)
        return subscriber
    }

    /**
     * 获取资讯
     */
    fun getNews(): ApiSubscriber<List<HomeNewsResponse>> {
        val subscriber = object : ApiSubscriber<List<HomeNewsResponse>>() {}
        getApi()
                .get<List<HomeNewsResponse>>(HOME_NEWS)
                .subscribe(subscriber)
        return subscriber
    }

    /**
     * 获取广告图
     */
    fun getAd(): ApiSubscriber<List<HomeAdResponse>> {
        val subscriber = object : ApiSubscriber<List<HomeAdResponse>>() {}
        getApi()
                .get<List<HomeAdResponse>>(HOME_AD)
                .subscribe(subscriber)
        return subscriber
    }

    /**
     * 主播推荐
     */
    fun getHomeAnchorRecommend(cacheMode: CacheMode): ApiSubscriber<List<HomeHotLiveResponse>> {
        val subscriber = object : ApiSubscriber<List<HomeHotLiveResponse>>() {}
        getApi()
                .get<List<HomeHotLiveResponse>>(HOME_ANCHOR_RECOMMEND)
                .cacheMode(cacheMode)
                .headers("token", UserInfoSp.getToken())
                .subscribe(subscriber)
        return subscriber
    }

    /**
     * 初始化直播间(进入直播间)
     */
    fun enterLiveRoom(anchorId: String = "", client_ip: String = "", function: ApiSubscriber<HomeLiveEnterRoomResponse>.() -> Unit) {
        val subscriber = object : ApiSubscriber<HomeLiveEnterRoomResponse>() {}
        subscriber.function()
        getApi()
                .get<HomeLiveEnterRoomResponse>(HOME_INIT_LIVE_ROOM)
                .params("anchor_id", anchorId)
                .params("user_id", UserInfoSp.getUserId())
                .params("client_ip", client_ip)
                .subscribe(subscriber)
    }

    /**
     * 专家红单
     */
    fun expertRed(): ApiSubscriber<List<HomeExpertList>> {
        val subscriber = object : ApiSubscriber<List<HomeExpertList>>() {}
        getApiLottery()
                .get<List<HomeExpertList>>(getApiOtherTest() + HOME_EXPERT_RED)
                .params("limit", "5")
                .params("page", "1")
                .params("is_recommend", "10")
                .subscribe(subscriber)
        return subscriber
    }

    /**
     * 获取20条消息
     */
    fun getTwentyNews(anchorId: String = ""): ApiSubscriber<List<HomeLiveTwentyNewsResponse>> {
        val subscriber = object : ApiSubscriber<List<HomeLiveTwentyNewsResponse>>() {}
        getApi()
                .get<List<HomeLiveTwentyNewsResponse>>(HOME_INIT_TWENTY_NEWS)
                .params("anchor_id", anchorId)
                .params("user_id", UserInfoSp.getUserId()!!)
                .subscribe(subscriber)
        return subscriber
    }

    /**
     * 初始直播间打赏排行
     */
    fun getRankList(anchorId: String = "", function: ApiSubscriber<List<HomeLiveRankList>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<HomeLiveRankList>>() {}
        subscriber.function()
        getApi().get<List<HomeLiveRankList>>(HOME_INIT_REWARD_LIST)
                .params("anchor_id", anchorId)
                .subscribe(subscriber)
    }

    /**
     * 初始直播间预告
     */
    fun getLiveAdvanceList(type: String = "", function: AllSubscriber.() -> Unit) {
        val subscriber = AllSubscriber()
        subscriber.function()
        getApi().get<BaseApiBean>(Home_LIVE_ADVANCE)
                .params("user_id", UserInfoSp.getUserId())
                .params("type", type)
                .subscribe(subscriber)
    }

    /**
     * 初始主播信息
     */
    fun getLiveAnchorInfo(anchorId: String = "", function: ApiSubscriber<HomeLiveAnchorInfoBean>.() -> Unit) {
        val subscriber = object : ApiSubscriber<HomeLiveAnchorInfoBean>() {}
        subscriber.function()
        getApi().get<HomeLiveAnchorInfoBean>(HOME_LIVE_ANCHOR_INFO)
                .params("anchor_id", anchorId)
                .subscribe(subscriber)
    }

    /**
     * 获取主播动态
     */
    fun getAnchorDynamic(anchorId: String = "", page: String = "1", limit: String = "10", function: ApiSubscriber<List<HomeLiveAnchorDynamicBean>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<HomeLiveAnchorDynamicBean>>() {}
        subscriber.function()
        getApi().get<List<HomeLiveAnchorDynamicBean>>(HOME_LIVE_ANCHOR_ANCHOR_DYNAMIC)
                .params("anchor_id", anchorId)
                .params("user_id", UserInfoSp.getUserId())
                .params("page", page)
                .params("limit", limit)
                .subscribe(subscriber)
    }

    /**
     * 获取礼物列表
     */
    fun getGiftList(function: AllEmptySubscriber.() -> Unit) {
        val subscriber = AllEmptySubscriber()
        subscriber.function()
        getApi().get<String>(HOME_LIVE_GIFT_LIST)
                .headers("token", UserInfoSp.getToken())
                .subscribe(subscriber)
    }

    /**
     * 资讯列表
     */
    fun getNewsList(type: String = "", neednew: String = "", page: String = "1", limit: String = "10", function: ApiSubscriber<List<HomeNewsResponse>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<HomeNewsResponse>>() {}
        subscriber.function()
        getApi().get<List<HomeNewsResponse>>(NEWS_LIST)
                .params("type", type)
                .params("neednew", neednew)
                .params("page", page)
                .params("limit", limit)
                .subscribe(subscriber)
    }

    /**
     * 资讯详情
     */
    fun getNewsInfo(info_id: String, function: ApiSubscriber<List<HomeNesInfoResponse>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<HomeNesInfoResponse>>() {}
        subscriber.function()
        getApi().get<List<HomeNesInfoResponse>>(NEWS_INFO)
                .params("info_id", info_id)
                .subscribe(subscriber)
    }

    /**
     * 搜索主播推荐
     */
    fun getPopAnchor(function: ApiSubscriber<List<HomeAnchorRecommend>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<HomeAnchorRecommend>>() {}
        subscriber.function()
        getApi().get<List<HomeAnchorRecommend>>(HOME_SEARCH_POP)
                .params("limit", "10")
                .subscribe(subscriber)
    }

    /**
     * 搜索主播
     */
    fun getSearchAnchor(search_content: String, function: ApiSubscriber<HomeAnchorSearch>.() -> Unit) {
        val subscriber = object : ApiSubscriber<HomeAnchorSearch>() {}
        subscriber.function()
        getApi().get<HomeAnchorSearch>(HOME_SEARCH)
                .params("search_content", search_content)
                .subscribe(subscriber)
    }

    /**
     * 管理员清屏
     */
    fun managerClear(anchor_id: String, function: AllEmptySubscriber.() -> Unit) {
        val subscriber = AllEmptySubscriber()
        subscriber.function()
        getApi().post<String>(HOME_MANAGER_CLEAR)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("anchor_id", anchor_id)
                .subscribe(subscriber)
    }


    /**
     * 禁言  禁言时间 单位分钟-不传使用后台配置时间 0-永久禁言
     */
    fun forBiddenWords(opertate_user: Int, ban_user: String, room_id: String, ban_time: String, function: EmptySubscriber.() -> Unit) {
        val subscriber = EmptySubscriber()
        subscriber.function()
        val request = getApi().post<String>(FORBIDDEN_WORDS)
        request.headers("token", UserInfoSp.getToken())
                .params("opertate_user", opertate_user)
                .params("ban_user", ban_user)
                .params("ban_time", ban_time)
        if (!TextUtils.isEmpty(room_id)) request.params("room_id", room_id)
        request.subscribe(subscriber)
    }

    /**
     * 发红包
     */
    fun homeLiveSendRedEnvelope(anchorId: String, amount: String, num: String, text: String, password: String, function: ApiSubscriber<HomeLiveRedEnvelopeBean>.() -> Unit) {
        val subscriber = object : ApiSubscriber<HomeLiveRedEnvelopeBean>() {}
        subscriber.function()
        getApi().post<HomeLiveRedEnvelopeBean>(HOME_LIVE_SEND_RED_ENVELOPE)
                .headers("token", UserInfoSp.getToken())
                .params("anchor_id", anchorId)
                .params("user_id", UserInfoSp.getUserId())
                .params("amount", amount)
                .params("num", num)
                .params("text", text)
                .params("password", password)
                .subscribe(subscriber)
    }

    /**
     * 直播间红包队列
     */
    fun homeLiveRedList(anchorId: String, function: ApiSubscriber<List<HomeLiveRedRoom>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<HomeLiveRedRoom>>() {}
        subscriber.function()
        val request = getApi().get<List<HomeLiveRedRoom>>(HOME_LIVE_RED_RECEIVE_ROOM)
                .headers("token", UserInfoSp.getToken())
        if (UserInfoSp.getUserId() != 0) request.params("user_id", UserInfoSp.getUserId())
        request.params("anchor_id", anchorId)
        request.subscribe(subscriber)
    }

    /**
     * 抢红包
     */
    fun homeGetRed(rid: String, function: ApiSubscriber<HomeLiveRedReceiveBean>.() -> Unit) {
        val subscriber = object : ApiSubscriber<HomeLiveRedReceiveBean>() {}
        subscriber.function()
        getApi().post<HomeLiveRedReceiveBean>(HOME_LIVE_RED_RECEIVE)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("rid", rid)
                .subscribe(subscriber)
    }

    /**
     * 直播预告标题
     */
    fun getAdvanceTitle(function: ApiSubscriber<MutableList<HomeLiveAdvance>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<MutableList<HomeLiveAdvance>>() {}
        subscriber.function()
        getApi().post<MutableList<HomeLiveAdvance>>(HOME_LIVE_ADVANCE)
                .subscribe(subscriber)
    }

    /**
     * 主播关注or取关 增加用户关注
     */
    fun attentionAnchorOrUser(anchor_id: String, follow_id: String, function: ApiSubscriber<Attention>.() -> Unit) {
        val subscriber = object : ApiSubscriber<Attention>() {}
        subscriber.function()
        val request = getApi().post<Attention>(HOME_ATTENTION_ANCHCOR)
        request.headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
        if (!TextUtils.isEmpty(anchor_id)) request.params("anchor_id", anchor_id)
        if (!TextUtils.isEmpty(follow_id)) request.params("follow_id", follow_id)
        request.subscribe(subscriber)
    }

    /**
     * 所有主播
     */
    fun getAllAnchor(page: Int, type: String, function: AllSubscriber.() -> Unit) {
        val subscriber = AllSubscriber()
        subscriber.function()
        getApi().get<BaseApiBean>(HOME_ALL_ANCHOR)
                .headers("token", UserInfoSp.getToken())
                .params("page", page)
                .params("type", type)
                .params("limit", "10")
                .subscribe(subscriber)
    }


    /**
     * 送礼物
     */
    fun setGift(userId: Int, anchorId: String, gift_id: String, gift_num: String, function: EmptySubscriber.() -> Unit) {
        val subscriber = EmptySubscriber()
        subscriber.function()
        getApi().post<String>(HOME_LIVE_SEND_GIFT)
                .headers("token", UserInfoSp.getToken())
                .params("anchor_id", anchorId)
                .params("user_id", userId)
                .params("gift_id", gift_id)
                .params("gift_num", gift_num)
                .subscribe(subscriber)
    }


    /**
     * 购彩网址
     */
    fun getLotteryUrl(function: ApiSubscriber<BetLotteryBean>.() -> Unit) {
        val subscriber = object : ApiSubscriber<BetLotteryBean>() {}
        subscriber.function()
        getApi().get<BetLotteryBean>(LOTTERY_URL)
                .subscribe(subscriber)

    }


    /**
     * 专家关注
     */
    fun attentionExpert(expert_id: String, function: AllEmptySubscriber.() -> Unit) {
        val subscriber = AllEmptySubscriber()
        subscriber.function()
        getApiLottery().post<String>(getApiOtherTest() + FOLLOW_EXPERT)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .params("user_id", UserInfoSp.getUserId())
                .params("expert_id", expert_id)
                .subscribe(subscriber)

    }


    /**
     * 获取版本
     */
    fun getVersion(function: ApiSubscriber<UpdateData>.() -> Unit) {
        val subscriber = object : ApiSubscriber<UpdateData>() {}
        subscriber.function()
        getApi()
                .get<UpdateData>(VERSION_UPDATE)
                .params("client_type", "android")
                .params("version", "2.2")
                .subscribe(subscriber)
    }

    /**
     * 系统公告
     */
    fun getSystemNotice(function: ApiSubscriber<SystemNotice>.() -> Unit) {
        val subscriber = object : ApiSubscriber<SystemNotice>() {}
        subscriber.function()
        getApi()
                .get<SystemNotice>(SYSTEM_NOTICE)
                .headers("token", UserInfoSp.getToken())
                .params("page", 1)
                .params("limit", 1)
                .subscribe(subscriber)
    }

}