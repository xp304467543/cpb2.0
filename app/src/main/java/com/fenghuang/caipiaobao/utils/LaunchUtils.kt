package com.fenghuang.caipiaobao.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.fenghuang.baselib.base.activity.BaseActivity
import com.fenghuang.baselib.base.fragment.BaseFragment
import com.fenghuang.baselib.utils.AppUtils
import com.fenghuang.baselib.utils.NetWorkUtils
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.caipiaobao.constant.IntentConstant
import com.fenghuang.caipiaobao.constant.UserConstant
import com.fenghuang.caipiaobao.ui.WebGlobalActivity
import com.fenghuang.caipiaobao.ui.home.live.LiveRoomActivity
import com.fenghuang.caipiaobao.ui.login.WebActivity
import com.fenghuang.caipiaobao.ui.main.MainActivity
import com.fenghuang.caipiaobao.ui.mine.children.MineAttentionFragment
import com.fenghuang.caipiaobao.ui.mine.children.MineInvestFragment
import com.fenghuang.caipiaobao.ui.mine.children.recharge.MineRechargeActivity
import com.fenghuang.caipiaobao.ui.moments.childern.HotCommentOnActivity
import com.fenghuang.caipiaobao.ui.moments.data.MomentsAnchorListResponse
import com.fenghuang.caipiaobao.ui.personal.AnchorPersonalPage
import com.fenghuang.caipiaobao.ui.personal.ExpertPersonalPage
import com.fenghuang.caipiaobao.ui.personal.UserPersonalPage


/**
 * @author Pinger
 * @since 2018/12/8 19:37
 *
 * 跳转页面的工具类
 */
object LaunchUtils {

    fun startActivity(context: Context?, clazz: Class<*>) {
        context?.startActivity(Intent(context, clazz))
    }

    fun startActivity(context: Context?, intent: Intent) {
        context?.startActivity(intent)
    }

    fun startFragment(context: Context?, fragment: BaseFragment) {
        if (context is BaseActivity) {
            context.start(fragment)
        }
    }

    /**
     * 打开主页
     */
    fun startMain(context: Context?) {
        startActivity(context, MainActivity::class.java)
    }


    /**
     * 打开浏览器
     */
    fun startBrowser(url: String?) {
        AppUtils.startBrowser(url)
    }

    /**
     * 打开应用市场
     */
    fun startAppMarket() {
        AppUtils.startAppMarket()
    }

    /**
     * 跳转到Web页面，跳转Activity
     */
    fun startWebActivity(context: Context?, url: String?, title: String? = null, canBack: Boolean = true) {
//        WebActivity.start(context, url, title, canBack)
    }


    /**
     * 跳转手机自带web
     */
    fun startSystemWeb(context: Context, url: String) {
        val uri = Uri.parse(url)   //设置跳转的网站
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)

    }

    /**
     * 打开个人界面 用户、主播、专家
     */
    fun startPersonalPage(context: Context?, followId: String, type: Int,lotteryId:String ="-1") {
        val intent = when (type) {
            1 -> {
                Intent(context, UserPersonalPage::class.java)
                        .putExtra(UserConstant.FOLLOW_ID, followId)
            }
            2 -> {
                Intent(context, AnchorPersonalPage::class.java)
                        .putExtra(UserConstant.FOLLOW_ID, followId)
            }
            3 -> {
                Intent(context, ExpertPersonalPage::class.java)
                        .putExtra(UserConstant.FOLLOW_ID, followId)
                        .putExtra(UserConstant.FOLLOW_lottery_ID, lotteryId)
            }
            else -> Intent(context, UserPersonalPage::class.java)
                    .putExtra(UserConstant.FOLLOW_ID, followId)

        }

        startActivity(context, intent)

    }

    /**
     * 打开充值、存款
     */
    fun startRechargePage(context: Activity?, index: Int) {
        val intent = Intent(context, MineRechargeActivity::class.java)
                .putExtra("index", index)

        startActivity(context, intent)
    }

    /**
     * 跳转直播
     */
    fun startLive(context: Context?, anchor_id: String, live_status: String, name: String, avatar: String, nickname: String, online: Int,lottery_id:String?="1"){
        if (NetWorkUtils.isNetworkConnected()) {
            try {
                val intent = Intent(context, LiveRoomActivity::class.java)
                intent.putExtra(IntentConstant.LIVE_ROOM_ANCHOR_ID, anchor_id)
                intent.putExtra(IntentConstant.LIVE_ROOM_ANCHOR_STATUE, live_status)
                intent.putExtra(IntentConstant.LIVE_ROOM_NAME, name)
                intent.putExtra(IntentConstant.LIVE_ROOM_AVATAR, avatar)
                intent.putExtra(IntentConstant.LIVE_ROOM_NICK_NAME, nickname)
                intent.putExtra(IntentConstant.LIVE_ROOM_ONLINE, online)
                intent.putExtra(IntentConstant.LIVE_ROOM_LOTTERY_ID, lottery_id)
                startActivity(context, intent)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }else ToastUtils.showError("网络连接已断开")
    }

    /**
     * 跳转公共 联系客服
     */

    fun startCustomer(context: Context?){
        val intent = Intent(context, WebActivity::class.java)
        startActivity(context, intent)
    }
    /**
     * 跳转公共 web
     */
    fun starGlobalWeb(context: Context?,title:String,url:String){
        val intent = Intent(context, WebGlobalActivity::class.java)
        intent.putExtra("title",title)
        intent.putExtra("urlGlobal",url)
        startActivity(context, intent)
    }
    /**
     * 跳转充值
     */
    fun startInvest(context: Context?,amount: Float, id: Int, url: String,isRen:Boolean){
        val intent = Intent(context, MineInvestFragment::class.java)
        intent.putExtra(IntentConstant.MINE_INVEST_AMOUNT, amount)
        intent.putExtra(IntentConstant.MINE_RECHARGE_ID, id)
        intent.putExtra(IntentConstant.MINE_RECHARGE_URL, url)
        intent.putExtra("isRen", isRen)
        startActivity(context, intent)
    }

    /**
     * 跳转 评论主播
     */
    fun jumpAnchor(context: Context?,data: MomentsAnchorListResponse){
        val intent = Intent(context, HotCommentOnActivity::class.java)
        intent.putExtra("responseAnchor",data)
        startActivity(context, intent)
    }

    /**
     * 跳转 关注
     */
    fun jumpAttention(context: Context?){
        val intent = Intent(context, MineAttentionFragment::class.java)
        startActivity(context, intent)
    }

}