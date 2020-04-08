package com.fenghuang.caipiaobao.ui.home.live.room

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.fenghuang.caipiaobao.utils.AnimUtils
import com.fenghuang.caipiaobao.utils.task.BaseTask
import com.fenghuang.caipiaobao.widget.spanlite.SpanBuilder
import com.fenghuang.caipiaobao.widget.spanlite.SpanLite
import java.util.*

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-29
 * @ Describe Vip进场队列
 *
 */

class LiveRoomVipEnterTask(var context: Context, var data: String, var view: TextView?) : BaseTask() {

    //如果这个Task的执行时间是不确定的，比如上传图片，那么在上传成功后需要手动调用
    //unLockBlock方法解除阻塞

    @SuppressLint("SetTextI18n")
    override fun doTask() {
        super.doTask()
        val res = data.split(",")
        SpanLite.with(view).append(SpanBuilder.Builder("VIP"+res[0]).drawTextColor("#FF513E").drawTypeFaceBold().drawTypeFaceItalic().build())
                .append(SpanBuilder.Builder(" 贵族 ").drawTextColor("#333333").build())
                .append(SpanBuilder.Builder(res[1]).drawTextColor("#D3904E").build())
                .append(SpanBuilder.Builder(" 驾临直播间 ").drawTextColor("#333333").build()).active()
        view?.let { AnimUtils.getInAnimation(context, it) }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (view != null) {
                    view?.post {
                        AnimUtils.getOutAnimation(context, view!!)
                        unLockBlock()
                        cancel()
                    }
                }
            }
        }, 5000)
    }

    //任务执行完的回调，可以做些释放资源或者埋点之类的操作
    override fun finishTask() {
        super.finishTask()
//        LogUtils.e("LogTask", "--finishTask-" + name);
    }


}