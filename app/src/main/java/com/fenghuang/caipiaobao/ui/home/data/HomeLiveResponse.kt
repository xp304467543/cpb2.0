package com.fenghuang.caipiaobao.ui.home.data

import android.os.Parcelable
import com.google.gson.JsonElement
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
import java.io.Serializable

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-25
 * @ Describe
 *
 */

data class HomeLiveEnterRoomResponse(
        val anchor_id: String = "",
        val nickname: String = "",
        val avatar: String = "",
        val istop: String = "",
        val live_status: String? = "",
        val fans: String = "",
        val cover: String = "",
        val liveStartTime: String = "",
        val liveEndTime: String = "",
        val liveStartTimeTxt: String = "",
        val liveEndTimeTxt: String = "",
        val online: String = "",
        val name: String?,
        val base_online: String = "",
        val liveInfo: List<HomeLiveRoomListBean>? = null,
        val red_paper: List<HomeLiveRedRoom>? = null,
        val isFollow: Boolean,
        val lottery_id: String? = "1"
)

data class HomeLiveRoomListBean(var liveUrl: HomeLiveRoomListLiveUrlBean,
                                var type: String)

data class HomeLiveRoomListLiveUrlBean(var fluentPullUrl: String = "",
                                       var hdPullUrl: String = "",
                                       var highPullUrl: String = "",
                                       var originPullUrl: String = "")

// 红包队列
data class HomeLiveRedRoom(var id: String = "",
                           var text: String,
                           var userName: String,
                           var avatar: String
)

// 抢红包
data class HomeLiveRedReceiveBean(var amount: String?,
                                  var send_text: String?,
                                  var send_user_name: String?,
                                  var count: Int?,
                                  var send_user_avatar: String?)

data class HomeLiveTwentyNewsResponse(val type: String = "",
                                      val room_id: String = "",
                                      val user_id: String = "",
                                      val userName: String = "",
                                      val text: String? = "",
                                      var vip: String = "",
                                      val userType: String = "",
                                      val avatar: String? = "",
                                      val sendTime: Long = 0,
                                      val sendTimeTxt: String = "",
                                      val gift_name: String = "",
                                      val gift_num: String = "",
                                      val icon: String = "",
                                      val final_num: Int = 1,
                                      val showTime: Long = 0,
                                      val gift_id: String = "",
                                      val gift_price: String = "",
                                      val gift_type: String = "",
                                      val event: String="",
                                      var orders: JsonElement? = null,
                                      var childIsShow:Boolean = false,
                                      var canFollow:Boolean = false


)


data class HomeLiveRankList(val user_id: String = "",
                            val nickname: String = "",
                            val avatar: String = "",
                            val amount: String = "",
                            val vip: String = "")


data class HomeLiveAdvanceList(val title: String = "", val bean: Array<HomeLivePreResponse>)


data class HomeLiveAnchorInfoBean(var anchor_id: String = "",
                                  var avatar: String = "",
                                  var zan: String = "",
                                  var duration: String = "",
                                  var fans: String = "",
                                  var follow_num: String = "",
                                  var giftList: List<HomeLiveAnchorGiftListBean>,
                                  var giftNum: String = "",
                                  var isFollow: Boolean,
                                  var level: String = "",
                                  var liveStatus: String = "",
                                  var live_record: List<HomeLiveAnchorLiveRecordBean>,
                                  var lottery: List<HomeLiveAnchorLotteryBean>,
                                  var nickname: String = "",
                                  var sex: String = "",
                                  var age: String = "",
                                  var liveStartTime: String = "",
                                  var liveEndTime: String = "",
                                  var sign: String = "",
                                  var tagList: List<HomeLiveAnchorTagListBean>) : Serializable

data class HomeLiveAnchorGiftListBean(var gift_id: String = "",
                                      var icon: String = "") : Serializable

data class HomeLiveAnchorLiveRecordBean(var name: String,
                                        var startTime: String = "",
                                        var startTimeTxt: String,
                                        var tip: String = "") : Serializable

data class HomeLiveAnchorLotteryBean(var name: String = "") : Serializable
data class HomeLiveAnchorTagListBean(var icon: String = "",
                                     var title: String = "") : Serializable


// 主播信息动态数据
data class HomeLiveAnchorDynamicBean(var anchor_id: String = "",
                                     var dynamic_id: String = "",
                                     var text: String = "",
                                     var media: MutableList<String>? = null,
                                     var zans: String = "",
                                     var pls: String = "",
                                     var shares: String = "",
                                     var create_time: Long = 0,
                                     var create_time_txt: String = "",
                                     var time_tip: String,
                                     var nickname: String = "",
                                     var avatar: String = "",
                                     var is_zan: Boolean,
                                     var live_status: String?
)


//直播间消息
data class HomeLiveChatBeanNormal(var position: String = "",
                                  var room_id: String = "",
                                  var sendTime: Long = 0,
                                  var sendTimeTxt: String = "",
                                  var gift_text: String = "",
                                  var dataType: String = "",
                                  var method_cname: String = "",
                                  var result_c: String = "",
                                  var nums: String = "",
                                  var size: String = "",
                                  var type: String = "",
                                  var text: String = "",
                                  var color: String = "",
                                  var commend: String = "",
                                  var isMe: Boolean,
                                  var user_id: String = "",
                                  var userType: String = "",
                                  var userName: String? = "",
                                  var gift_id: String = "",
                                  var gift_type: String = "",
                                  var gift_name: String = "",
                                  var gift_price: String = "",
                                  var gift_num: String = "",
                                  var isAnchor: String = "0",
                                  var vip: String = "",
                                  var icon: String = "",
                                  var r_id: String = "",
                                  var avatar: String = "",
                                  var code: String = "",
                                  var msg: String = "",
                                  var data: JsonElement? = null,
                                  var event:String = "",
                                  var orders: JsonElement? = null

)

//全局Socket
data class AllSocket(var type:String?,var client_id:String?,var dataType:String?,var data:DataRes?)

data class DataRes(val msg_id:String?, var msg:String?,var is_win:Boolean)

@Parcelize
data class HomeLiveChatChildBean(var id: String = "", var lottery_id: String = "", var method_cname: String = "",
                                 var result_c: String = "", var nums: String = "", var updated: String = "") : Parcelable


@Parcelize
data class HomeLiveChatOpenIssue(var end_sale_time: Long?, var issue: String = "") : Parcelable
@Parcelize
data class HomeLiveChatChildBeanNormal(var online: String = "") : Parcelable

//礼物列表
data class HomeLiveGiftList(var id: String = "", var type: String = "", var name: String = "",
                            var amount: String = "", var icon: String = "", var grade: String = "")


//礼物动画Rx
data class HomeLiveAnimatorBean(var gift_id: String,
                                var git_name: String,
                                var gift_icon: String,
                                var user_id: String,
                                var user_icon: String,
                                var user_name: String, var giftCount: String)

//礼物大动画Rx
data class HomeLiveBigAnimatorBean(var gift_id: String,
                                   var git_name: String,
                                   var gift_icon: String,
                                   var user_id: String,
                                   var user_icon: String,
                                   var user_name: String, var giftCount: String)

//新闻
data class HomeNewsBean(var info_id: String = "", var title: String = "", var type: String = "",
                        var createtime: String = "", var tag: String = "", var settype: String = "",
                        var des: String = "", var timegap: String = "", var type_txt: String = "",
                        var tag_txt: String = "", var cover_img: List<String>? = null
)

//关注取关
data class Attention(var isFollow: Boolean)

// 发送红包
data class HomeLiveRedEnvelopeBean(var rid: String)

//直播预告
data class HomeLiveAdvance(var room_id: String = "", var name: String = "")

//全部主播
data class HomeLiveAnchor(var type: String = "", var name: String = "")