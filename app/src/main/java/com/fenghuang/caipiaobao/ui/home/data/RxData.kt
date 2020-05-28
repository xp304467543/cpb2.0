package com.fenghuang.caipiaobao.ui.home.data

/**
 *
 * @ Author  QinTian
 * @ Date  2020-01-22
 * @ Describe
 *
 */

data class HomeJumpToMine(var jump:Boolean)

data class UpDateUserPhoto(var img:String)

data class IsFirstRecharge(var res:Boolean)

//进场提醒 只给自己提醒
data class EnterVip(var vip:String)


//视频的打开或收起
data class LiveVideoClose(var closeOrOpen:Boolean)

//跟新预告关注
data class UpDatePreView(var boolean: Boolean)

//退出登录 设置回默认值
data class LoginOut(var loginOut:Boolean)

//直播间人数
data class OnLineInfo(var online:Int?)

data class LineCheck(var url:String,var boolean:Boolean = false)

//礼物
data class Gift(var gift:String?)


//横屏小红包提示
data class RedTips(var boolean:Boolean)

//横屏小红包点击
data class RedPaperClick(var  click:Boolean)


//礼物连数
data class GiftClickNum(var clickNum:String)

//接收横屏弹幕
data class DanMu(var userName:String,var userType:String,var text:String,var vip:String)

//横屏发弹幕
data class SendDanMu(var content:String)

//更新横屏钻石
data class UpDataHorDiamon(var boolean: Boolean)

//直播间@功能
data class LiveCallPersonal(var name:String)

//跳转购彩nt
data class JumpToBuyLottery(var index: Int)

//关注更新
data class UpDateAttention(var boolean: Boolean,var anchorId:String)


//web
data class WebSelect(var pos:Int)
