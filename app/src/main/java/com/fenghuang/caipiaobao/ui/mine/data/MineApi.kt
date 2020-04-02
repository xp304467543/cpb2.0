package com.fenghuang.caipiaobao.ui.mine.data

import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.data.api.AllSubscriber
import com.fenghuang.caipiaobao.data.api.ApiSubscriber
import com.fenghuang.caipiaobao.data.api.BaseApi
import com.fenghuang.caipiaobao.data.api.EmptySubscriber
import com.fenghuang.caipiaobao.data.bean.BaseApiBean
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.moments.data.MomentsAnchorListResponse
import com.fenghuang.caipiaobao.ui.moments.data.MomentsHotDiscussResponse
import com.pingerx.rxnetgo.rxcache.CacheMode

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-13
 * @ Describe
 *
 */
object MineApi : BaseApi {


    //用户信息
    private const val USER_INFO = "/index/index"
    //修改用户信息
    private const val USER_INFO_EDIT = "/index/edit"
    //上传头像
    const val USER_UPLOAD_AVATAR = "/index/upload-avatar"
    //意见反馈
    private const val MINE_FEED_BACK = "/api/v1_1/user/user_feedback"
    //获取皮肤列表/详情
    private const val MINE_THEM_SKIN = "/api/common/get_skin_list/"
    //查询Vip等级
    private const val MINE_CHECK_VIP = "/api/v1_1/user/vip_now/"
    //获取余额
    private const val USER_BALANCE = "/index/balance"
    //获取钻石
    private const val USER_DIAMOND = "/api/v1_1/user/diamond_now/"
    //是否设置支付密码
    private const val USER_IS_SET_PAY_PASS = "/index/check-fund-password"
    //验证支付密码
    private const val MINE_VERIFY_PASS_WORD = "/index/verify-fund-password"
    //兑换钻石
    private const val USER_EXCHANGE_DIAMOND = "/api/v1_1/user/exchange_diamond/"
    //获取支付列表
    private const val PAY_TYPE_LIST = "/api/v1/Recharge/getList"
    //银行卡列表
    private const val BANK_LIST = "/index/bank-list"
    //绑定银行卡
    private const val USER_BIND_CARD = "/index/bind-card/"
    //用户银行卡列表
    private const val USER_BANK_LIST = "/index/user-card-list/"
    //用户提现
    private const val USER_DEPOAIT = "/api/v1_1/withdraws/UserDeposit/"
    //关注列表 用户 主播
    private const val USER_ATTENTION  ="/api/v1_1/user/User_follow_list/"
    //关注列表 专家
    private const val USER_ATTENTION_EXPERT  ="/plan/follow-list"
    //余额记录
    private const val USER_BALANCE_LIST  ="/api/v1_1/Recharge/accountChange"
    //打赏记录
    private const val USER_PAY_LIST  = "/api/v1_1/user/user_reward_list_new/"
    //兑换记录
    private const val USER_CHANGE_LIST  = "/api/v1_1/user/diamond_list/"
    //消息中心
    private const val USER_MESSAGE_CENTER  = "/api/v1_1/live/get_notice/"
    //获取新消息通知
    private const val USER_MESSAGE_NEW = "/api/v1_1/live/get_notice_new/"
    //获取某个主播动态
    private const val ANCHOR_LIST = "/api/v1_1/live/get_dynamic/"
    //修改支付密码
    private const val HOME_LIVE_RED_SET_PASS = "/index/set-fund-password/"
    //修改密码
    private const val USER_MODIFY_PASSWORD = "/index/reset-password"
    // 验证码修改  登录/支付密码接口
    private const val SETTING_PASSWORD = "/index/sms-reset-pwd"
    //热门讨论列表 - 单个
    private const val HOT_DISCUSS = "/article/index"
    /**
     * 获取用户信息
     */

    fun getUserInfo(function: ApiSubscriber<MineUserInfoResponse>.() -> Unit) {
        val subscriber = object : ApiSubscriber<MineUserInfoResponse>() {}
        subscriber.function()
        getApiOther().get<MineUserInfoResponse>(HomeApi.getApiOtherUserTest() + USER_INFO)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .subscribe(subscriber)
    }


    /**
     * 修改个人资料
     */
    fun upLoadPersonalInfo(nickname: String, gender: Int, profile: String, function: EmptySubscriber.() -> Unit) {
        val subscriber = EmptySubscriber()
        subscriber.function()
        getApiOther().post<String>(HomeApi.getApiOtherUserTest() + USER_INFO_EDIT)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .params("nickname", nickname)
                .params("gender", gender)
                .params("profile", profile)
                .subscribe(subscriber)
    }

    /**
     * 反馈意见
     */

    fun feedBack(text: String, phone: String, qq: String, email: String, function: ApiSubscriber<BaseApiBean>.() -> Unit) {
        val subscriber = object : ApiSubscriber<BaseApiBean>() {}
        subscriber.function()
        getApi().get<BaseApiBean>(MINE_FEED_BACK)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("text", text)
                .params("phone", phone)
                .params("qq", qq)
                .params("email", email)
                .subscribe(subscriber)
    }

    /**
     * 获取皮肤列表
     */
    fun getThemSKin(id: String = "", page: String = "", limit: String = "", function: ApiSubscriber<List<MineThemSkinResponse>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<MineThemSkinResponse>>() {}
        subscriber.function()
        getApi().get<List<MineThemSkinResponse>>(MINE_THEM_SKIN)
                .params("id", id)
                .params("page", page)
                .params("limit", limit)
                .subscribe(subscriber)
    }

    /**
     * 获取皮肤详情
     */
    fun getThemSKinInfo(id: String = "", function: ApiSubscriber<MineThemSkinInfoResponse>.() -> Unit) {
        val subscriber = object : ApiSubscriber<MineThemSkinInfoResponse>() {}
        subscriber.function()
        getApi().get<MineThemSkinInfoResponse>(MINE_THEM_SKIN)
                .params("id", id)
                .subscribe(subscriber)
    }

    /**
     * 查询Vip等级
     */
    fun getUserVip(function: ApiSubscriber<MineUserVipType>.() -> Unit) {
        val subscriber = object : ApiSubscriber<MineUserVipType>() {}
        subscriber.function()
        getApi().get<MineUserVipType>(MINE_CHECK_VIP)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .subscribe(subscriber)
    }

    /**
     * 查询余额
     */
    fun getUserBalance(function: ApiSubscriber<MineUserBalance>.() -> Unit) {
        val subscriber = object : ApiSubscriber<MineUserBalance>() {}
        subscriber.function()
        getApiOther().get<MineUserBalance>(HomeApi.getApiOtherUserTest() + USER_BALANCE)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .subscribe(subscriber)
    }


    /**
     * 查询钻石
     */
    fun getUserDiamond(function: ApiSubscriber<MineUserDiamond>.() -> Unit) {
        val subscriber = object : ApiSubscriber<MineUserDiamond>() {}
        subscriber.function()
        getApi().get<MineUserDiamond>(USER_DIAMOND)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .subscribe(subscriber)
    }

    /**
     * 是否设置支付密码
     */
    fun getIsSetPayPass(function: EmptySubscriber.() -> Unit){
        val subscriber =  EmptySubscriber()
        subscriber.function()
        getApiOther().get<String>(HomeApi.getApiOtherUserTest() + USER_IS_SET_PAY_PASS)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .subscribe(subscriber)
    }

    /**
     * 验证支付密码
     */
    fun verifyPayPass(passWord:String,function: EmptySubscriber.() -> Unit){
        val subscriber =  EmptySubscriber()
        subscriber.function()
        getApiOther().post<String>(HomeApi.getApiOtherUserTest() + MINE_VERIFY_PASS_WORD)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .params("password",passWord)
                .subscribe(subscriber)
    }

    /**
     * 兑换钻石
     */
    fun getUserChangeDiamond(diamond: String, password: String, function: ApiSubscriber<MineUserDiamond>.() -> Unit) {
        val subscriber = object : ApiSubscriber<MineUserDiamond>() {}
        subscriber.function()
        getApi().post<MineUserDiamond>(USER_EXCHANGE_DIAMOND)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("diamond", diamond)
                .params("password", password)
                .subscribe(subscriber)
    }

    /**
     * 获取 支付通道列表
     */
    fun getPayTypeList(function: ApiSubscriber<List<MinePayTypeList>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<MinePayTypeList>>() {}
        subscriber.function()
        getApi().post<List<MinePayTypeList>>(PAY_TYPE_LIST)
                .headers("token", UserInfoSp.getToken())
                .subscribe(subscriber)
    }

    /**
     * 获取 银行卡列表
     */
    fun getBankList(function: ApiSubscriber<List<MineBankList>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<MineBankList>>() {}
        subscriber.function()
        getApiOther().get<List<MineBankList>>(HomeApi.getApiOtherUserTest() +BANK_LIST)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .subscribe(subscriber)
    }


    /**
     *绑定银行卡
     */

    fun bingBankCard(bank_code: String, province: String, city: String, branch: String, realname: String, card_num: String, fund_password: String, function: EmptySubscriber.() -> Unit) {
        val subscriber = EmptySubscriber()
        subscriber.function()
        getApiOther().post<String>(HomeApi.getApiOtherUserTest() +USER_BIND_CARD)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .params("bank_code", bank_code)
                .params("province", province)
                .params("city", city)
                .params("branch", branch)
                .params("realname", realname)
                .params("card_num", card_num)
                .params("fund_password", fund_password)
                .subscribe(subscriber)
    }


    /**
     * 获取 用户绑定的银行卡列表
     */
    fun getUserBankList(function: ApiSubscriber<List<MineUserBankList>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<MineUserBankList>>() {}
        subscriber.function()
        getApiOther().get<List<MineUserBankList>>(HomeApi.getApiOtherUserTest() +USER_BANK_LIST)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .subscribe(subscriber)
    }


    /**
     * 用户 提现
     */
    fun userGetCashOut(amount: Double, card_name: String, putforwoad_card: String, function: EmptySubscriber.() -> Unit) {
        val subscriber = EmptySubscriber()
        subscriber.function()
        getApi().post<String>(USER_DEPOAIT)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("user_putforwoad", amount)
                .params("card_name", card_name)
                .params("putforwoad_card", putforwoad_card)
                .subscribe(subscriber)
    }

    /**
     * 获取 Tpay充值订单URL
     */
    fun getToPayUrl(amount: Float, channels_id: Int, url: String, function: ApiSubscriber<MinePayUrl>.() -> Unit) {
        val subscriber = object : ApiSubscriber<MinePayUrl>() {}
        subscriber.function()
        getApi().post<MinePayUrl>(url)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("amount", amount)
                .params("channels_id", channels_id)
                .params("returnurl", "t1")
                .subscribe(subscriber)
    }

    /**
     * 获取 关注列表  用户 主播
     */
    fun getAttentionList(type:String, function: ApiSubscriber<List<MineUserAttentionBean>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<MineUserAttentionBean>>() {}
        subscriber.function()
        getApi().get<List<MineUserAttentionBean>>(USER_ATTENTION)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("type", type)
                .subscribe(subscriber)
    }

    /**
     * 获取 关注列表  专家
     */
    fun getAttentionList(function: ApiSubscriber<List<MineExpertBean>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<MineExpertBean>>() {}
        subscriber.function()
        getApiLottery().get<List<MineExpertBean>>(getApiOtherTest()+USER_ATTENTION_EXPERT)
                .headers("token", UserInfoSp.getTokenWithBearer())
                .params("user_id", UserInfoSp.getUserId())
                .subscribe(subscriber)
    }

    /**
     * 余额记录
     */
    fun getBalance(page:Int,function: AllSubscriber.() -> Unit){
        val subscriber = AllSubscriber()
        subscriber.function()
        getApi().get<BaseApiBean>(USER_BALANCE_LIST)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("page", page)
                .params("limit", "10")
                .subscribe(subscriber)
    }

    /**
     * 获取某个主播动态
     */
    fun getAnchorMoments(dynamic_id: String, function: ApiSubscriber<List<MomentsAnchorListResponse>>.() -> Unit) {
        val subscriber = object : ApiSubscriber<List<MomentsAnchorListResponse>>() {}
        subscriber.function()
        getApi()
                .get<List<MomentsAnchorListResponse>>(ANCHOR_LIST)
                .cacheMode(CacheMode.NONE)
                .params("user_id", UserInfoSp.getUserId())
                .params("dynamic_id",dynamic_id)
                .subscribe(subscriber)
    }

    /**
     * 获取热门讨论列表
     */
    fun getHotDiscussSingle(article_id: String, function: ApiSubscriber<MomentsHotDiscussResponse>.() -> Unit) {
        val subscriber = object : ApiSubscriber<MomentsHotDiscussResponse>() {}
        subscriber.function()
        getApiLottery()
                .get<MomentsHotDiscussResponse>(HomeApi.getApiOtherTest() + HOT_DISCUSS)
                .cacheMode(CacheMode.NONE)
                .params("user_id", UserInfoSp.getUserId())
                .params("article_id",article_id)
                .subscribe(subscriber)
    }

    /**
     * 打赏记录
     */
    fun getReward(page:Int,function: AllSubscriber.() -> Unit){
        val subscriber = AllSubscriber()
        subscriber.function()
        getApi().get<BaseApiBean>(USER_PAY_LIST)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("page", page)
                .params("limit", "10")
                .subscribe(subscriber)
    }

    /**
     * 兑换记录
     */
    fun getChange(page:Int,function: AllSubscriber.() -> Unit){
        val subscriber = AllSubscriber()
        subscriber.function()
        getApi().get<BaseApiBean>(USER_CHANGE_LIST)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .params("page", page)
                .params("limit", "10")
                .subscribe(subscriber)
    }

    /**
     * 消息记录
     */
    fun getMessageTips(msg_type:String,function: ApiSubscriber<List<MineMessageCenter>>.() -> Unit){
        val subscriber = object : ApiSubscriber<List<MineMessageCenter>>() {}
        subscriber.function()
        getApi().get<List<MineMessageCenter>>(USER_MESSAGE_CENTER)
                .params("user_id", UserInfoSp.getUserId())
                .params("msg_type", msg_type)
                .subscribe(subscriber)
    }

    /**
     * 是否有新消息
     */
    fun getIsNewMessage(function: ApiSubscriber<MineNewMsg>.() -> Unit){
        val subscriber = object : ApiSubscriber<MineNewMsg>() {}
        subscriber.function()
        getApi().get<MineNewMsg>(USER_MESSAGE_NEW)
                .headers("token", UserInfoSp.getToken())
                .params("user_id", UserInfoSp.getUserId())
                .subscribe(subscriber)
    }

    /**
     * 设置支付密码
     * oldPassword= 老密码（选填） newPassword=新密码  newPasswordRepeat=确认新密码
     */
    fun getSettingPayPassword(oldPassword: String, newPassword: String, function: EmptySubscriber.() -> Unit) {
        val subscriber = EmptySubscriber()
        subscriber.function()
        getApiOther().post<String>(HomeApi.getApiOtherUserTest() +HOME_LIVE_RED_SET_PASS)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .params("old_password", oldPassword)
                .params("new_password", newPassword)
//                .params("new_password_repeat", newPasswordRepeat)
                .subscribe(subscriber)
    }



    /**
     * 修改密码
     */

    fun modifyPassWord(old_password: String, new_password: String, function: EmptySubscriber.() -> Unit) {
        val subscriber = EmptySubscriber()
        subscriber.function()
        getApiOther().post<String>(HomeApi.getApiOtherUserTest() +USER_MODIFY_PASSWORD)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .params("old_password", old_password)
                .params("new_password", new_password)
                .subscribe(subscriber)
    }



    /**
     * 验证码修改  登录/支付密码接口
     */
    fun modifyPassWord(phone: String, captcha: String, new_pwd: String, type: Int, function: EmptySubscriber.() -> Unit) {
        val subscriber = EmptySubscriber()
        subscriber.function()
       getApiOther().post<String>(HomeApi.getApiOtherUserTest() +SETTING_PASSWORD)
                .headers("Authorization", UserInfoSp.getTokenWithBearer())
                .params("phone", phone)
                .params("captcha", captcha)
                .params("new_pwd", new_pwd)
                .params("type", type)
                .subscribe(subscriber)
    }


}