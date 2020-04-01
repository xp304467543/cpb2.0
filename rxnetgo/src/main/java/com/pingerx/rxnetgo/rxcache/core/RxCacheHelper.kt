package com.pingerx.rxnetgo.rxcache.core

import com.pingerx.rxnetgo.rxcache.data.CacheFrom
import com.pingerx.rxnetgo.rxcache.data.CacheResult
import com.pingerx.rxnetgo.utils.NetLogger
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Type


/**
 * @author Pinger
 * @since 2019/2/21 19:24
 *
 * Rx缓存助手
 */
object RxCacheHelper {


    fun <T> loadCacheFlowable(rxCache: RxCache, key: String, type: Type, needEmpty: Boolean): Flowable<CacheResult<T>> {
        var flowable = rxCache.load<T>(key, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        if (needEmpty) {
            flowable = flowable.onErrorResumeNext(Function { Flowable.empty() })
        }
        return flowable
    }

    fun <T> loadRemoteFlowable(rxCache: RxCache, key: String, source: Flowable<T>, target: CacheTarget, needEmpty: Boolean): Flowable<CacheResult<T>> {
        var flowable = source
                .map { t ->
                    NetLogger.e("loadRemote result=$t")
                    rxCache.save(key, t, target)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { status -> NetLogger.e("save status => " + status!!) },
                                    { throwable ->
                                        NetLogger.e(throwable)
                                    })
                    CacheResult(CacheFrom.Remote, key, t)
                }
        if (needEmpty) {
            flowable = flowable.onErrorResumeNext(Function { Flowable.empty() })
        }
        return flowable
    }

}
