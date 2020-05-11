package com.fenghuang.caipiaobao.ui.home.live.room.betting

import android.app.Activity
import android.os.Bundle
import android.os.Looper
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fenghuang.baselib.utils.ToastUtils
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.constant.UserInfoSp
import com.fenghuang.caipiaobao.ui.home.data.HomeApi
import com.fenghuang.caipiaobao.ui.home.data.HomeJumpToMine
import com.fenghuang.caipiaobao.ui.home.live.room.betting.adapter.LiveRoomBetAccessAdapter
import com.fenghuang.caipiaobao.ui.lottery.data.*
import com.fenghuang.caipiaobao.utils.AESUtils
import com.fenghuang.caipiaobao.widget.dialog.GlobalTipsDialog
import com.fenghuang.caipiaobao.widget.dialog.LoadingDialog
import com.fenghuang.caipiaobao.widget.dialog.bottom.BottomDialogFragment
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.hwangjr.rxbus.RxBus
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

/**
 *
 * @ Author  QinTian
 * @ Date  2020/4/24
 * @ Describe 投注确认
 *
 */

class LiveRoomBetAccessFragment : BottomDialogFragment() {

    private var totalMoney = 0L


    private var liveRoomBetAccessAdapter: LiveRoomBetAccessAdapter? = null

    private var rvBetAccess: RecyclerView? = null

    private var tvTotalDiamond: TextView? = null

    private var tvBetAccessSubmit: TextView? = null

    private var loadingDialog: LoadingDialog? = null

    private var orderMap: HashMap<String, Any>? = null


    override val layoutResId: Int = R.layout.dialog_fragment_bet_access

    override val resetHeight: Int = ViewUtils.getScreenHeight() * 2 / 3 - ViewUtils.dp2px(160)

    override fun isShowTop() = false

    override fun canceledOnTouchOutside() = true

    override fun initView() {
        rvBetAccess = rootView?.findViewById(R.id.rvBetAccess)
        tvTotalDiamond = rootView?.findViewById(R.id.tvTotalDiamond)
        tvBetAccessSubmit = rootView?.findViewById(R.id.tvBetAccessSubmit)
        initLoading()
    }

    override fun initData() {
        val dataList = arguments?.getParcelableArrayList<LotteryBet>("lotteryBet")
        if (dataList.isNullOrEmpty()) return
        val moneyAll = arguments?.getInt("totalDiamond", 0) ?: 0
        liveRoomBetAccessAdapter = context?.let {
            LiveRoomBetAccessAdapter(it)
        }
        rvBetAccess?.adapter = liveRoomBetAccessAdapter
        rvBetAccess?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        liveRoomBetAccessAdapter?.addAll(dataList)
        totalMoney = (arguments?.getInt("totalDiamond")?:0).toLong()
        tvTotalDiamond?.text = HtmlCompat.fromHtml("总下注钻石: <font color=\"#FF513E\">$totalMoney</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
        liveRoomBetAccessAdapter?.onMoneyChangeListener { money, pos ->
            if (liveRoomBetAccessAdapter?.getAllData().isNullOrEmpty()) return@onMoneyChangeListener
            if (money == ""){
                liveRoomBetAccessAdapter?.getItemData(pos)?.result?.money = "0"
            }else liveRoomBetAccessAdapter?.getItemData(pos)?.result?.money = money
            setMoney()
        }
        tvBetAccessSubmit?.setOnClickListener {
            for (single in liveRoomBetAccessAdapter?.getAllData()!!) {
                if (single.result.money != "") {
                    if (single.result.money.toInt() < 10) {
                        ToastUtils.show("单注投注金额最小为 10")
                        return@setOnClickListener
                    }
                }
            }
            when {
                totalMoney < 10 -> {
                    ToastUtils.show("单注投注金额最小为 10")
                }
                totalMoney > 999999999 -> {
                    ToastUtils.show("投注金额最大为 999999999")
                }
                else -> {
                    //余额不足
                    if (arguments?.getString("diamond") ?: "" != "0x11") {
                        if ((arguments?.getString("diamond")
                                        ?: "0").toDouble() < (totalMoney.toString().toDouble())) {
                            val tips = context?.let { it1 -> GlobalTipsDialog(it1, "您的钻石不足,请兑换钻石", "确定", "取消", "") }
                            tips?.setConfirmClickListener {
                                RxBus.get().post(HomeJumpToMine(true))
                                dismiss()
                            }
                            tips?.show()
                            return@setOnClickListener
                        }
                    }
                    //投注
                    loadingDialog?.show()
                    try {
                        val id = arguments?.getString("lotteryID")
                        val issue = arguments?.getString("issue")
                        val jsonRes = arguments?.getParcelableArrayList<LotteryBet>("lotteryBet")
                        val bean = arrayListOf<BetBean>()
                        if (jsonRes != null) {
                            for (js in jsonRes) {
                                val result = BetBean(js.result.play_sec_name, js.result.play_class_name, js.result.money)
                                bean.add(result)
                            }
                            val followUser = arguments?.getString("followUserId") ?: "0"
                            lotteryBet(id ?: "", issue ?: "", bean, followUser, jsonRes[0].playName)
                        }
                    } catch (e: Exception) {
                        ToastUtils.show(e.message.toString())
                    }

                }
            }
        }
    }


    private fun setMoney() {
        var total = 0L
        for (it in liveRoomBetAccessAdapter?.getAllData()!!) {
            if (it.result.money != "") {
                total += it.result.money.toInt()
            }
        }
        totalMoney = total
        tvTotalDiamond?.text = HtmlCompat.fromHtml("总下注钻石: <font color=\"#FF513E\">$total</font>", HtmlCompat.FROM_HTML_MODE_COMPACT)
    }


    /**
     * 投注 跟投
     * play_bet_follow_user	跟投用户id，默认0为正常投注
     */
    private fun lotteryBet(play_bet_lottery_id: String, play_bet_issue: String, order_detail: ArrayList<BetBean>, play_bet_follow_user: String, playName: String) {
        orderMap = hashMapOf()
        val goon = GsonBuilder().disableHtmlEscaping().create()
        val orderString = goon.toJson(order_detail).toString()
        orderMap!!["play_bet_lottery_id"] = play_bet_lottery_id
        orderMap!!["play_bet_issue"] = play_bet_issue
        orderMap!!["play_bet_follow_user"] = play_bet_follow_user
        orderMap!!["order_detail"] = orderString
        AESUtils.encrypt(UserInfoSp.getRandomStr() ?: "", goon.toJson(orderMap))?.let {
            val builder = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)//表单类型
                    .addFormDataPart("datas", it)
            val requestBody = builder.build()
            val request = Request.Builder()
                    .url(LotteryApi.getBaseUrlMoments() + "/" + HomeApi.getApiOtherTest() + LotteryApi.LOTTERY_BET)
                    .addHeader("Authorization", UserInfoSp.getTokenWithBearer() ?: "")
                    .post(requestBody)
                    .build()
            val client = OkHttpClient.Builder().build()
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val json = JsonParser().parse(response.body()?.string()!!).asJsonObject
                        if (json.get("code").asString == "1") {
                            (context as Activity).runOnUiThread {
                                //投注成功
                                loadingDialog?.dismiss()
                                context?.let { it1 ->
                                    if (UserInfoSp.getUserType() == "1" && (arguments?.getBoolean("isFollow") == false)) {
                                        val dialog = GlobalTipsDialog(it1, "投注成功", "分享方案", "确定", "")
                                        dialog.setConfirmClickListener {
                                            getShareOrder(playName)
                                        }
                                        dialog.show()
                                    } else GlobalTipsDialog(it1, "投注成功", "确定", "", "").show()
                                    RxBus.get().post(LotteryResetDiamond(true))
                                    dismiss()
                                }
                            }

                        } else {
                            (context as Activity).runOnUiThread {
                                loadingDialog?.dismiss()
                                ToastUtils.showError(json.get("msg").asString)
                            }
                        }

                    } catch (e: Exception) {
                        Looper.prepare()
                        loadingDialog?.dismiss()
                        e.printStackTrace()
                        Looper.loop()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    (context as Activity).runOnUiThread {
                        loadingDialog?.dismiss()
                        ToastUtils.showError(e.toString())
                    }
                }
            })
        }

    }

    //拼接分享注单
    fun getShareOrder(name: String) {
        val jsonRes = arguments?.getParcelableArrayList<LotteryBet>("lotteryBet")
        val goon = GsonBuilder().disableHtmlEscaping().create()
        val bean = arrayListOf<BetShareBean>()
//        val typeName = arguments?.getString("lotteryNameType") ?: ""
        if (jsonRes != null) {
            for (js in jsonRes) {
//                typeName + name
                val result = BetShareBean(name, js.result.money, js.result.play_class_cname,
                        js.result.play_class_name, js.result.play_odds, js.result.play_sec_name)
                bean.add(result)
            }
        }
        val json = JSONObject()
        json.put("play_bet_issue", arguments?.getString("issue") ?: "")
        json.put("play_bet_lottery_id", arguments?.getString("lotteryID") ?: "")
        json.put("lottery_cid", arguments?.getString("lotteryName") ?: "")
        json.put("order_detail", goon.toJson(bean))
        RxBus.get().post(LotteryShareBet(true, json))
    }

    fun initLoading() {
            loadingDialog = context?.let { LoadingDialog(it) }
            loadingDialog?.setCanceledOnTouchOutside(false)

    }

    override fun initFragment() {
    }

    companion object {
        fun newInstance(lotteryBet: LotteryBetAccess) = LiveRoomBetAccessFragment().apply {
            arguments = Bundle(1).apply {
                putParcelableArrayList("lotteryBet", lotteryBet.result)
                putInt("totalDiamond", lotteryBet.totalMoney)//每一注金额
                putInt("totalCount", lotteryBet.totalCount) //多少注
                putString("lotteryID", lotteryBet.lotteryID) //ID
                putString("issue", lotteryBet.issue) //ISSUE
                putString("diamond", lotteryBet.diamond) //diamond
                putString("lotteryName", lotteryBet.lotteryName) //lotteryName
                putString("lotteryNameType", lotteryBet.lotteryNameType) //lotteryNameType
                putBoolean("isFollow", lotteryBet.isFollow) //isFollow
                putString("followUserId", lotteryBet.followUserId) //isFollow
            }
        }
    }
}