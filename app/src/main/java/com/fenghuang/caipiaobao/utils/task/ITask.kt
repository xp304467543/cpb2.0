package com.fenghuang.caipiaobao.utils.task

/**
 *
 * @ Author  QinTian
 * @ Date  2020-02-29
 * @ Describe
 *
 */

interface ITask : Comparable<ITask> {

    //获取任务优先级
    val priority: TaskPriority

    //获取入队次序
    //当优先级相同 按照插入顺序 先入先出 该方法用来标记插入顺序
    var sequence: Int

    //每个任务的状态，就是标记完成和未完成
    val status: Boolean

    //获取每个任务执行的时间
    val duration: Int

    //将该任务插入队列
    fun enqueue()

    //执行具体任务的方法
    fun doTask()

    //任务执行完成后的回调方法
    fun finishTask()

    //设置任务优先级
    fun setPriority(mTaskPriority: TaskPriority): ITask

    //设置每个任务的执行时间，该方法用于任务执行时间确定的情况
    fun setDuration(duration: Int): ITask

    //阻塞任务执行，该方法用于任务执行时间不确定的情况
    @Throws(Exception::class)
    fun blockTask()

    //解除阻塞任务，该方法用于任务执行时间不确定的情况
    fun unLockBlock()
}
