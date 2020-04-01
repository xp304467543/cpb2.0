package com.fenghuang.caipiaobao.constant

import com.fenghuang.baselib.utils.SpUtils
import com.fenghuang.caipiaobao.ui.mine.data.MineUserBankList
import com.fenghuang.caipiaobao.utils.GlobalDialog
import com.fenghuang.caipiaobao.utils.JsonUtils

/**
 *
 * @ Author  QinTian
 * @ Date  2019/10/26- 15:12
 * @ Describe 缓存个人信息
 *
 */

object UserInfoSp {

    /**
     * 是否登录
     */
    fun putIsLogin(isLogin: Boolean) {
        SpUtils.putBoolean(UserConstant.USER_LOGIN, isLogin)
        if (!isLogin) GlobalDialog.spClear()
    }

    fun getIsLogin(): Boolean {
        return SpUtils.getBoolean(UserConstant.USER_LOGIN)
    }

    /**
     * token
     */
    fun putToken(token: String) {
        SpUtils.putString(UserConstant.TOKEN, token)
        SpUtils.putString(UserConstant.TOKEN_WITH_BEARER, "Bearer $token")
    }

    fun getToken(): String? {
        return SpUtils.getString(UserConstant.TOKEN)
    }

    fun getTokenWithBearer(): String? {
        return SpUtils.getString(UserConstant.TOKEN_WITH_BEARER)
    }

    /**
     * 用户ID
     */

    fun putUserId(id: String) {
        SpUtils.putString(UserConstant.USER_ID, id)
    }

    fun getUserId(): String {
        return SpUtils.getString(UserConstant.USER_ID, "0").toString()
    }

    /**
     * 用户名称 nickName
     */
    fun putUserNickName(nickName: String) {
        SpUtils.putString(UserConstant.USER_NICK_NAME, nickName)
    }

    fun getUserNickName(): String? {
        return SpUtils.getString(UserConstant.USER_NICK_NAME)
    }

    /**
     * 用户名 Name
     */
    fun putUserName(Name: String) {
        SpUtils.putString(UserConstant.USER_NAME, Name)
    }

    fun getUserName(): String? {
        return SpUtils.getString(UserConstant.USER_NAME)
    }

    /**
     * 用户名头像
     */
    fun putUserPhoto(url: String) {
        SpUtils.putString(UserConstant.USER_AVATAR, url)
    }

    fun getUserPhoto(): String? {
        return SpUtils.getString(UserConstant.USER_AVATAR)
    }

    /**
     * 手机号
     */
    fun putUserPhone(phone: String) {
        SpUtils.putString(UserConstant.USER_PHONE, phone)
    }

    fun getUserPhone(): String? {
        return SpUtils.getString(UserConstant.USER_PHONE)
    }


    /**
     * 性别
     */
    fun putUserSex(sex: Int) {
        SpUtils.putInt(UserConstant.USER_SEX, sex)
    }

    fun getUserSex(): Int {
        return SpUtils.getInt(UserConstant.USER_SEX, 1)
    }

    /**
     * 个性签名
     */
    fun putUserProfile(profile: String) {
        SpUtils.putString(UserConstant.USER_PROFILE, profile)
    }

    fun getUserProfile(): String? {
        return SpUtils.getString(UserConstant.USER_PROFILE)
    }

    /**
     * 用户粉丝 关注 点赞
     */
    fun putUserFans(fans: String) {
        SpUtils.putString("UserFans", fans)
    }

    fun getUserFans(): String? {
        return SpUtils.getString("UserFans")
    }

    /**
     * 用户粉丝 关注 点赞
     */
    fun putUserUniqueId(fans: String) {
        SpUtils.putString("UniqueId", fans)
    }

    fun getUserUniqueId(): String? {
        return SpUtils.getString("UniqueId")
    }

    /**
     * 是否提示送礼物信息
     */
    fun putSendGiftTips(boolean: Boolean) {
        SpUtils.putBoolean("GiftTips", boolean)
    }

    fun getSendGiftTips(): Boolean {
        return SpUtils.getBoolean("GiftTips", true)
    }

    /**
     * 是否设置支付密码
     */

    fun putIsSetPayPassWord(boolean: Boolean) {
        SpUtils.putBoolean("PayPassWord", boolean)
    }

    fun getIsSetPayPassWord(): Boolean {
        return SpUtils.getBoolean("PayPassWord", false)
    }

    /**
     * 是否首冲
     */
    fun putIsFirstRecharge(boolean: Boolean) {
        SpUtils.putBoolean("IsFirstRecharge", boolean)
    }

    fun getIsFirstRecharge(): Boolean {
        return SpUtils.getBoolean("IsFirstRecharge", true)
    }

    /**
     * 弹幕开关
     */
    fun putDanMuSwitch(boolean: Boolean) {
        SpUtils.putBoolean("DanMuSwitch", boolean)
    }

    fun getDanMuSwitch(): Boolean {
        return SpUtils.getBoolean("DanMuSwitch", true)
    }

    /**
     * 首页引导图
     */
    fun putMainGuide(boolean: Boolean) {
        SpUtils.putBoolean("MainGuide", boolean)
    }

    fun getMainGuide(): Boolean {
        return SpUtils.getBoolean("MainGuide", false)
    }

    /**
     * 开奖引导图
     */
    fun putOpenCodeGuide(boolean: Boolean) {
        SpUtils.putBoolean("OpenCodeGuide", boolean)
    }

    fun getOpenCodeGuide(): Boolean {
        return SpUtils.getBoolean("OpenCodeGuide", false)
    }

    /**
     * 我的界面引导图
     */
    fun putMineGuide(boolean: Boolean) {
        SpUtils.putBoolean("MineGuide", boolean)
    }

    fun getMineGuide(): Boolean {
        return SpUtils.getBoolean("MineGuide", false)
    }

    /**
     * 关注引导图
     */
    fun putAttentionGuide(boolean: Boolean) {
        SpUtils.putBoolean("AttentionGuide", boolean)
    }

    fun getAttentionGuide(): Boolean {
        return SpUtils.getBoolean("AttentionGuide", false)
    }

    /**
     * 打赏引导图
     */
    fun putRewardnGuide(boolean: Boolean) {
        SpUtils.putBoolean("RewardnGuide", boolean)
    }

    fun getRewardnGuide(): Boolean {
        return SpUtils.getBoolean("RewardnGuide", false)
    }

    /**
     * 直播引导图
     */
    fun putLiveGuide(boolean: Boolean) {
        SpUtils.putBoolean("MineGuide", boolean)
    }

    fun getLiveeGuide(): Boolean {
        return SpUtils.getBoolean("MineGuide", false)
    }

    /**
     * 是否悬浮
     */
    fun isAboveLive(boolean: Boolean) {
        SpUtils.putBoolean("AboveLive", boolean)
    }

    fun getIsAboveLive(): Boolean {
        return SpUtils.getBoolean("AboveLive", false)
    }

    /**
     * 记录Vip等级
     */
    fun setVipLevel(str: String) {
        SpUtils.putString("VipLevel", str)
    }

    fun getVipLevel(): String? {
        return SpUtils.getString("VipLevel", "0")
    }

    /**
     * 记录Vip等级 0-用户 1-主播 2-管理
     */
    fun setUserType(boolean: String) {
        SpUtils.putString("UserType", boolean)
    }

    fun getUserType(): String? {
        return SpUtils.getString("UserType", "0")
    }

    /**
     * 悬浮按钮
     */
    fun putOpenWindow(boolean: Boolean) {
        SpUtils.putBoolean("openWindow", boolean)
    }

    fun getOpenWindow(): Boolean {
        return SpUtils.getBoolean("openWindow", true)
    }

    /**
     * 皮肤选中 1-默认 2-春节
     */
    fun putSkinSelect(int: Int) {
        SpUtils.putInt("SkinSelect", int)
    }

    fun getSkinSelect(): Int {
        return SpUtils.getInt("SkinSelect", 1)
    }

    /**
     * 银行卡信息记录
     */
    fun putSelectBankCard(mineBank: MineUserBankList) {
        SpUtils.putString(UserConstant.USER_BANK_SELECT, JsonUtils.toJson(mineBank))
    }

    fun getSelectBankCard(): MineUserBankList? {
        return if (SpUtils.getString(UserConstant.USER_BANK_SELECT) != "") {
            JsonUtils.fromJson(SpUtils.getString(UserConstant.USER_BANK_SELECT).toString(), MineUserBankList::class.java)
        } else null

    }



}