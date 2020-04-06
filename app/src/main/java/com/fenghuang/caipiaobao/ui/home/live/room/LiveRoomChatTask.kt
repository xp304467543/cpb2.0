package com.fenghuang.caipiaobao.ui.home.live.room

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveChatChildBean
import com.fenghuang.caipiaobao.utils.AnimUtils
import com.fenghuang.caipiaobao.utils.task.BaseTask
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-29
 * @ Describe
 *
 */

class LiveRoomChatTask(var context: Context, var data: HomeLiveChatChildBean, var view: TextView?) : BaseTask() {

    //如果这个Task的执行时间是不确定的，比如上传图片，那么在上传成功后需要手动调用
    //unLockBlock方法解除阻塞

    @SuppressLint("SetTextI18n")
    override fun doTask() {
        super.doTask()
        view?.text =getLotteryName(data.lottery_id)+ "  " + data.method_cname+ "  " + data.result_c
        view?.let { AnimUtils.getLotteryInAnimation(context, it) }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                view?.post {
                    AnimUtils.getLotteryOutAnimation(context, view!!)
                    unLockBlock()
                    cancel()
                }

            }
        }, 5000)
    }

    //任务执行完的回调，可以做些释放资源或者埋点之类的操作
    override fun finishTask() {
        super.finishTask()
//        LogUtils.e("LogTask", "--finishTask-" + name);
    }


    private fun getLotteryName(id: String): String {
        return when (id) {
            "1" -> "重庆时时彩"
            "7" -> "北京赛车"
            "8" -> "香港彩"
            "9" -> "幸运飞艇"
            "10" -> "澳洲幸运5"
            "11" -> "澳洲幸运10"
            "26" -> "极速赛车"
            "27" -> "欢乐赛车"
            else -> ""
        }
    }

}