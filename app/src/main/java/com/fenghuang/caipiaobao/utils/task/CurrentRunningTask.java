package com.fenghuang.caipiaobao.utils.task;

/**
 * @ Author  QinTian
 * @ Date  2020-02-29
 * @ Describe
 */
public class CurrentRunningTask {
    private static ITask sCurrentShowingTask;

    public static void setCurrentShowingTask(ITask task) {
        sCurrentShowingTask = task;
    }

    public static void removeCurrentShowingTask() {
        sCurrentShowingTask = null;
    }

    public static ITask getCurrentShowingTask() {
        return sCurrentShowingTask;
    }

    public static boolean getCurrentShowingStatus() {
        return sCurrentShowingTask != null && sCurrentShowingTask.getStatus();
    }
}
