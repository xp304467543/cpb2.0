package com.fenghuang.caipiaobao.manager

import android.os.Looper
import com.fenghuang.baselib.utils.LogUtils
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * RxJava操作符助手
 */
object RxHelper {

    /**
     * 是否在主线程
     */
    fun isMainThread(): Boolean {
        return Thread.currentThread() == Looper.getMainLooper().thread
    }

    /**
     * 主线程执行
     */
    fun doOnUiThread(task: () -> Unit): Disposable {
        return Flowable
                .just(task)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.invoke()
                }, {
                    LogUtils.e(it)
                })
    }

    /**
     * 子线程执行
     */
    fun doOnIOThread(task: () -> Unit): Disposable {
        return Flowable
                .just(task)
                .observeOn(Schedulers.io())
                .subscribe({
                    it.invoke()
                }, {
                    LogUtils.e(it)
                })
    }


    /**
     * 延时执行，在主线程
     */
    fun doDelay(time: Long, task: () -> Unit): Disposable {
        return Flowable.just(task)
                .delay(time, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.invoke()
                }, {
                    LogUtils.e(it)
                })
    }
}
