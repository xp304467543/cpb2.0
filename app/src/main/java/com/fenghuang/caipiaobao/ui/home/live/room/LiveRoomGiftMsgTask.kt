package com.fenghuang.caipiaobao.ui.home.live.room

import android.annotation.SuppressLint
import com.fenghuang.caipiaobao.ui.home.data.HomeLiveTwentyNewsResponse
import com.fenghuang.caipiaobao.utils.task.BaseTask

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-29
 * @ Describe
 *
 */

class LiveRoomGiftMsgTask( var data: HomeLiveTwentyNewsResponse, var view: LiveRoomChatAdapter) : BaseTask() {

    //如果这个Task的执行时间是不确定的，比如上传图片，那么在上传成功后需要手动调用
    //unLockBlock方法解除阻塞

    val final_num =1

    @SuppressLint("SetTextI18n")
    override fun doTask() {

    }

    //任务执行完的回调，可以做些释放资源或者埋点之类的操作
    override fun finishTask() {
        super.finishTask()
//        LogUtils.e("LogTask", "--finishTask-" + name);
    }



}