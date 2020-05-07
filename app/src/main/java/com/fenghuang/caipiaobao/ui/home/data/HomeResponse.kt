package com.fenghuang.caipiaobao.ui.home.data

import com.google.gson.annotations.SerializedName

/**
 *
 * @ Author  QinTian
 * @ Date  2019/12/26- 16:04
 * @ Describe
 *
 */
// 轮播图
data class HomeBannerResponse(val type: String = "", val title: String = "", val name: String = "", val image_url: String = "", val content: String = "", val url: String = "")

// 公告信息
data class HomeSystemNoticeResponse(val msgtype: String = "", val trgttype: String = "", val trgtuid: String = "", val content: String = "", val action: String = "", val gnrtime: String = "")

// 彩种列表
data class HomeLotteryTypeResponse(@SerializedName("1") var `_$1`: List<HomeTypeListResponse>? = null, @SerializedName("2") var `_$2`: List<HomeTypeListResponse>? = null, @SerializedName("3") var `_$3`: List<HomeTypeListResponse>? = null, @SerializedName("4") var `_$4`: List<HomeTypeListResponse>? = null)

data class HomeTypeListResponse(var type: String? = "", var anchor_id: String? = "", var game_id: String? = "", var name: String? = "", var image: String? = "", var image_pc: String? = "", var live_status: String? = "", var live_intro: String? = "", var online: Int?, var lottery_id: String? = "", var live_status_txt: String? = "")

// 热门直播
data class HomeHotLiveResponse(var anchor_id: String = "", var game_id: String = "", var name: String = "",
                               var live_status: String = "", var nickname: String = "", var avatar: String = "",
                               var tags: List<HomeExpertTags> = arrayListOf(), var live_intro: String = "",var lottery_id:String?="",
                               var red_paper_num: Int, var online: Int?, var daxiu: Boolean?, var background: String?="")

data class HomeExpertTags(var icon: String = "", var title: String = "")

//直播预告
data class HomeLivePreResponse(var aid: String = "", var starttime: String = "", var endtime: String = "", var nickname: String = "", var avatar: String = "", var name: String = "", var isFollow: Boolean, var livestatus: String = "", var lottery_id: String = "", var date: String = "")

//新闻
data class HomeNewsResponse(var id: String = "",
                            var info_id: String = "",
                            var title: String? = "",
                            var type: String? = "",
                            var createtime: String = "",
                            var tag: String? = "",
                            var timegap: String? = "",
                            var type_txt: String? = "",
                            var tag_txt: String? = "",
                            var settype: String? = "",
                            var cover_img: List<String>? = null)

//新闻详情
data class HomeNesInfoResponse(var info_id: String = "", var title: String = "", var cover_img: List<String>? = null,
                               var type: String = "", var createtime: String = "", var tag: String = "",
                               var detail: String = "", var des: String = "", var source: String = "",
                               var settype: String = "", var timegap: String = "", var type_txt: String = "", var tag_txt: String = "")

//广告图
data class HomeAdResponse(
        var type: String = "",
        var title: String? = "",
        var name: String? = "",
        var image_url: String = "",
        var content: String? = "",
        var url: String? = "",
        var image_url_pc: String? = ""
)

//专家列表
data class HomeExpertList(var id: String = "",
                          var nickname: String = "",
                          var avatar: String = "",
                          var win_rate: String = "",
                          var profit_rate: String = "",
                          var winning: String = "",
                          var last_10_games: List<String>? = null,
                          var created: String = "",
                          var lottery_id: String = "",
                          var lottery_name: String = ""
)

//搜索推荐
data class HomeAnchorRecommend(var nickname: String = "", var id: String = "", var name: String?, var live_status: String = "", var avatar: String = "", var online: Int?)


//搜索推荐
data class HomeAnchorSearch(var result: List<SearchResult>?, var related: List<SearchResult>?)

data class SearchResult(var anchor_id: String = "", var nickname: String = "", var avatar: String = "", var live_status: String = "",
                        var online: Int?, var live_intro: String = "", var r_id: String = "", var name: String? = "",
                        var lottery_id: String? = "1", var tagString: String = "", var live_status_txt: String = "", var red_paper_num: String = "",
                        var daxiu: Boolean, var tags: List<Tag>)

data class Tag(var title: String = "", var icon: String = "")

data class BetLotteryBean(var betting: String, var customer: String, var gameUrl: String, var protocol: String,var bettingArr:List<String>?,var app_start_banner:StartBanner?)

data class StartBanner(var type:String?,var image_url:String?)

/**
 * version_data : 版本信息
 * version_data.enforce : 是否强制更新 1是 0-否
 * version_data.version : 客户端版本号
 * version_data.newversion : 新版本号
 * version_data.downloadurl : 下载地址
 * version_data.packagesize : 包大小
 * version_data.upgradetext : 更新内容
 */

data class UpdateData(var version_data: Update? = null)

data class Update(var enforce: Int = 0,
                  var version: String? = null,
                  var newversion: String? = null,
                  var downloadurl: String? = null,
                  var packagesize: String? = null,
                  var upgradetext: String? = null)


//系统公告
data class SystemNotice(var msg_id: String? = null,var msg_type: String? = null,var content: String? = null,var create_time: String? = null
                        ,var createtime_txt: String? = null)

