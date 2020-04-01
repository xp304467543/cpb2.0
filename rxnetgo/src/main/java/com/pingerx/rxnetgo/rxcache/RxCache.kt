package com.pingerx.rxnetgo.rxcache

import com.google.gson.reflect.TypeToken
import com.pingerx.rxnetgo.rxcache.core.RxCache
import com.pingerx.rxnetgo.rxcache.data.CacheResult
import com.pingerx.rxnetgo.rxcache.stategy.ICacheStrategy
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-12-3 上午11:40
 *
 * RxCache的Kotlin调用，这里不需要内联处理
 * 使用到的Type最好由外部传入，不要使用使用TypeToken
 *
 */

fun <T> RxCache.load(key: String): Flowable<CacheResult<T>> {
    return load(key, object : TypeToken<T>() {}.type, BackpressureStrategy.LATEST)
}

fun <T> RxCache.load(key: String, backpressureStrategy: BackpressureStrategy): Flowable<CacheResult<T>> {
    return load(key, object : TypeToken<T>() {}.type, backpressureStrategy)
}

fun <T> Flowable<T>.rxCache(key: String, type: Type, strategy: ICacheStrategy): Flowable<CacheResult<T>> {
    return this.rxCache(RxCache.getDefault(), key, type, strategy)
}

fun <T> Flowable<T>.rxCache(rxCache: RxCache, key: String, strategy: ICacheStrategy): Flowable<CacheResult<T>> {
    return this.rxCache(rxCache, key, object : TypeToken<T>() {}.type, strategy)
}

fun <T> Flowable<T>.rxCache(rxCache: RxCache, key: String, type: Type, strategy: ICacheStrategy): Flowable<CacheResult<T>> {
    return this.compose<CacheResult<T>>(rxCache.transformFlowable(key, type, strategy))
}

