package com.fenghuang.caipiaobao.ui.moments.data

import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.data.api.ApiSubscriber
import com.fenghuang.caipiaobao.data.api.BaseApi
import com.fenghuang.caipiaobao.data.api.EmptySubscriber
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveAnchorDynamicBean
import com.pingerx.rxnetgo.rxcache.CacheMode

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-09
 * @ Describe
 *
 */
object MomentsApi : BaseApi {

    //顶部Banner
    private const val TOP_BANNER = "/article/banner"
    //热门讨论列表
    private const val HOT_DISCUSS = "/article/index"
    //主播动态列表
    private const val ANCHOR_LIST = "/api/v1_1//live/get_dynamic"
    //精品推荐
    private const val RECOMMEND_GOOD = "/article/recommended"
    //评论列表
    private const val COMMENT_ON = "/article/comment"
    //评论回复
    private const val COMMENT_REPLY = "/article/publish-comment"
    //点赞
    private const val QUIZ_ARTICLE_LIKE = "/article/like"
    //评论列表 Davis
    private const val COMMENT_LIST ="/api/v1_1/user/get_comment_list/"
    //评论回复 Davis
    private const val COMMENT_ON_DAVIS = "/api/v1_1/live/dynamic_comment/"
    //评论点赞 Davis
    private const val COMMENT_ZAN = "/api/v1_1/live/dynamic_like/"



    /**
     * 获取banner
     */
    fun getTopBanner(function: ApiSubscriber<List<MomentsTopBannerResponse>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<MomentsTopBannerResponse>>() {}
        subscriber.function()
        getApiLottery()
                .get<List<MomentsTopBannerResponse>>(HomeApi.getApiOtherTest() + TOP_BANNER)
                .cacheMode(CacheMode.NONE)
                .subscribe(subscriber)
    }

    /**
     * 获取热门讨论列表
     */
    fun getHotDiscuss(limit: String, page: Int, function: ApiSubscriber<List<MomentsHotDiscussResponse>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<MomentsHotDiscussResponse>>() {}
        subscriber.function()
        getApiLottery()
                .get<List<MomentsHotDiscussResponse>>(HomeApi.getApiOtherTest() + HOT_DISCUSS)
                .cacheMode(CacheMode.NONE)
                .params("user_id", UserInfoSp.getUserId())
                .params("limit", limit)
                .params("page", page)
                .subscribe(subscriber)
    }

    /**
     * 获取所有主播动态
     */
    fun getAnchorMoments(anchor_id: String,limit: String, page: Int, function: ApiSubscriber<List<MomentsAnchorListResponse>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<MomentsAnchorListResponse>>() {}
        subscriber.function()
        getApi()
                .get<List<MomentsAnchorListResponse>>(ANCHOR_LIST)
                .cacheMode(CacheMode.NONE)
                .params("user_id", UserInfoSp.getUserId())
                .params("limit", limit)
                .params("page", page)
                .params("anchor_id",anchor_id)
                .subscribe(subscriber)
    }

    /**
     * 获取某个播动态
     */
    fun getAnchorDynamic(anchorId: String = "", limit: String = "10", page: Int, function: ApiSubscriber<List<MomentsAnchorListResponse>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<MomentsAnchorListResponse>>() {}
        subscriber.function()
        getApi().get<List<MomentsAnchorListResponse>>(HomeApi.HOME_LIVE_ANCHOR_ANCHOR_DYNAMIC)
                .params("anchor_id", anchorId)
                .params("user_id", UserInfoSp.getUserId())
                .params("page", page)
                .params("limit", limit)
                .subscribe(subscriber)
    }

    /**
     * 精品推荐
     */
    fun getRecommend(function: ApiSubscriber<List<MomentsRecommend>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<MomentsRecommend>>() {}
        subscriber.function()
        getApiOther()
                .get<List<MomentsRecommend>>(HomeApi.getApiOtherTest() + RECOMMEND_GOOD)
                .cacheMode(CacheMode.NONE)
                .subscribe(subscriber)
    }


    /**
     * 点赞
     */
    fun getQuizArticleLikeResult(articleId: String, function: EmptySubscriber.() -> Unit) {
        val subscriber = EmptySubscriber()
        subscriber.function()
        getApiLottery()
                .post<String>(HomeApi.getApiOtherTest() + QUIZ_ARTICLE_LIKE)
                .cacheMode(CacheMode.NONE)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .params("article_id", articleId)
                .params("user_id", UserInfoSp.getUserId())
                .subscribe(subscriber)
    }

    /**
     * 评论列表
     */
    fun getCommentOnList(articleId: String, page: Int, function: ApiSubscriber<List<CommentOnResponse>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<CommentOnResponse>>() {}
        subscriber.function()
        getApiLottery()
                .get<List<CommentOnResponse>>(HomeApi.getApiOtherTest() + COMMENT_ON)
                .cacheMode(CacheMode.NONE)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .params("article_id", articleId)
                .params("user_id", UserInfoSp.getUserId())
                .params("page", page)
                .params("limit", "10")
                .subscribe(subscriber)
    }

    /**
     * 评论点赞
     */
    fun setCommentZan(comment_id: String, function: EmptySubscriber.() -> Unit) {
        val subscriber = EmptySubscriber()
        subscriber.function()
        getApiLottery()
                .post<String>(HomeApi.getApiOtherTest() + COMMENT_ZAN)
                .cacheMode(CacheMode.NONE)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .params("comment_id", comment_id)
                .params("user_id", UserInfoSp.getUserId())
                .subscribe(subscriber)
    }

    /**
     * 评论
     */
    fun setCommentReply(article_id: String, reply_id: String, content: String, function: EmptySubscriber.() -> Unit) {
        val subscriber = EmptySubscriber()
        subscriber.function()
        getApiLottery()
                .post<String>(HomeApi.getApiOtherTest() + COMMENT_REPLY)
                .cacheMode(CacheMode.NONE)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .params("user_id", UserInfoSp.getUserId())
                .params("article_id", article_id)
                .params("avatar", UserInfoSp.getUserPhoto().toString())
                .params("user_type", UserInfoSp.getUserType().toString())
                .params("reply_id", reply_id)
                .params("content", content)
                .subscribe(subscriber)
    }

    /**
     * Davis 评论列表 /api/v1_1/user/get_comment_list/
     */

    fun getCommentList(dym_id: String, page: Int,function: ApiSubscriber<List<DavisCommentOnResponse>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<DavisCommentOnResponse>>() {}
        subscriber.function()
        getApi()
                .get<List<DavisCommentOnResponse>>(COMMENT_LIST)
                .cacheMode(CacheMode.NONE)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("dym_id", dym_id)
                .params("page", page)
                .params("limit", "10")
                .subscribe(subscriber)
    }

    /**
     *  Davis 评论or回复
     */
    fun setDavisCommentReplay(dynamic_id: String, comment_id: String, comment_text: String, function: EmptySubscriber.() -> Unit) {
        val subscriber = EmptySubscriber()
        subscriber.function()
        getApi()
                .post<String>(COMMENT_ON_DAVIS)
                .cacheMode(CacheMode.NONE)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("dynamic_id", dynamic_id)
                .params("comment_id", comment_id)
                .params("comment_text", comment_text)
                .subscribe(subscriber)
    }

    /**
     *  Davis 点赞
     *  点赞类型1-动态（默认）2-评论
     */
    fun clickZansDavis(typeClick: String, like_id: String, function: EmptySubscriber.() -> Unit){
        val subscriber = EmptySubscriber()
        subscriber.function()
        getApi()
                .post<String>(COMMENT_ZAN)
                .cacheMode(CacheMode.NONE)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("type", typeClick)
                .params("like_id", like_id)
                .subscribe(subscriber)
    }


}