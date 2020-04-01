package com.pingerx.rxnetgo.rxcache.stategy

import com.pingerx.rxnetgo.rxcache.core.RxCache
import com.pingerx.rxnetgo.rxcache.data.CacheFrom
import com.pingerx.rxnetgo.rxcache.data.CacheResult
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 2019/2/21 18:13
 *
 * 无缓存策略，组装数据结构后直接返回原来的flowable
 */
class NoneStrategy : ICacheStrategy {

    override fun <T> execute(rxCache: RxCache, key: String, source: Flowable<T>, type: Type): Publisher<CacheResult<T>> {
        return source.map { CacheResult(CacheFrom.Remote, key, it) }
    }
}