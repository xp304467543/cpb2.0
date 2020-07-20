package com.fenghuang.caipiaobao.ui.home.live.room

import android.annotation.SuppressLint
import com.fenghuang.baselib.utils.LogUtils
import com.fenghuang.caipiaobao.constant.UserInfoSp
import org.json.JSONObject


/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-24
 * @ Describe
 *
 */

@SuppressLint("StaticFieldLeak")
object LiveRoomChaPresenterHelper {

    // 推送
    const val TYPE_SUBSCRIBE = "subscribe"
    // 初始化
    const val TYPE_PUBLISH = "publish"
    // 心跳
    const val TYPE_PING = "ping"
    // 礼物&红包
    const val TYPE_GIFT = "gift"
    //服务端推送
    const val TYPE_SERVER_PUSH = "ServerPush"
    //管理员对直播间聊天室的操作
    const val TYPE_COMMEND = "operation"
    //错误提醒
    const val TYPE_ERROE = "error"

    fun  getSubscribeParams(isReconnect: Boolean, anchorId: String): String {
        val jsonObject = JSONObject()
        jsonObject.put("room_id", anchorId)
        jsonObject.put("user_id", UserInfoSp.getUserId())
        jsonObject.put("type", TYPE_SUBSCRIBE)
        jsonObject.put("userType", UserInfoSp.getUserType())
        jsonObject.put("userName", UserInfoSp.getUserNickName())
        jsonObject.put("vip", UserInfoSp.getVipLevel().toString())
        if (isReconnect) jsonObject.put("rest", true)
        return jsonObject.toString()
    }

    fun getPublishParams(anchorId: String, content: String,isShardOrder:Boolean = false,orders: JSONObject?=null): String {
        val jsonObject = JSONObject()
        jsonObject.put("room_id", anchorId)
        jsonObject.put("user_id", UserInfoSp.getUserId())
        jsonObject.put("type", TYPE_PUBLISH)
        jsonObject.put("userName", UserInfoSp.getUserNickName())
        jsonObject.put("userType", UserInfoSp.getUserType())
        jsonObject.put("avatar", UserInfoSp.getUserPhoto())
        jsonObject.put("vip", UserInfoSp.getVipLevel().toString())
        jsonObject.put("text", content)
        if (isShardOrder){
            jsonObject.put("event", "pushPlan")
            jsonObject.put("orders", orders)
        }
        return jsonObject.toString()
    }

    fun getPingParams(anchorId: String): String {
        val jsonObject = JSONObject()
        jsonObject.put("room_id", anchorId)
        jsonObject.put("user_id", UserInfoSp.getUserId())
        jsonObject.put("type", TYPE_PING)
        jsonObject.put("userName", UserInfoSp.getUserNickName())
        jsonObject.put("userType", UserInfoSp.getUserType())
        jsonObject.put("vip", UserInfoSp.getVipLevel())
        return jsonObject.toString()
    }

    fun getGifParams(anchorId: String, gifType: String, rId: String, giftName: String,
                     giftPrice: String, giftNum: String, text: String,
                     gift_id: String, icon: String): String {
        val jsonObject = JSONObject()
        jsonObject.put("room_id", anchorId)
        jsonObject.put("user_id", UserInfoSp.getUserId())
        jsonObject.put("type", TYPE_GIFT)
        jsonObject.put("userType", UserInfoSp.getUserType())
        jsonObject.put("userName", UserInfoSp.getUserNickName())
        // gift_type: 礼物类型 1-普通 2-表白 3-彩票  4-红包
        if (gifType == "4") {
            jsonObject.put("r_id", rId)
            jsonObject.put("gift_text", text)
        } else if (gifType == "1") {
            jsonObject.put("gift_id", gift_id)
            jsonObject.put("icon", icon)
        }
        jsonObject.put("vip", UserInfoSp.getVipLevel())
        jsonObject.put("gift_type", gifType)
        jsonObject.put("gift_name", giftName)
        jsonObject.put("gift_price", giftPrice)
        jsonObject.put("gift_num", giftNum)
        jsonObject.put("avatar", UserInfoSp.getUserPhoto())
        jsonObject.put("vip", UserInfoSp.getVipLevel())
        return jsonObject.toString()
    }
    //清屏
    fun getManagerCommend(anchorId: String): String{
        val jsonObject = JSONObject()
        jsonObject.put("room_id", anchorId)
        jsonObject.put("type", TYPE_COMMEND)
        jsonObject.put("commend", "clear")
        return jsonObject.toString()
    }


}