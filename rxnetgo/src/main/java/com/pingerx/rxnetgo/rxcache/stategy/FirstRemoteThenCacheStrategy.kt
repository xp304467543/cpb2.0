package com.pingerx.rxnetgo.rxcache.stategy

import com.pingerx.rxnetgo.rxcache.core.CacheTarget
import com.pingerx.rxnetgo.rxcache.core.RxCache
import com.pingerx.rxnetgo.rxcache.core.RxCacheHelper
import com.pingerx.rxnetgo.rxcache.data.CacheResult
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import java.lang.reflect.Type
import java.util.*

/**
 * @author Pinger
 * @since 2019/2/21 18:19
 *
 * 先读取远程请求，并且进行缓存
 */
class FirstRemoteThenCacheStrategy : ICacheStrategy {

    override fun <T> execute(rxCache: RxCache, key: String, source: Flowable<T>, type: Type): Publisher<CacheResult<T>> {
        val cache = RxCacheHelper.loadCacheFlowable<T>(rxCache, key, type, true)
        val remote = RxCacheHelper.loadRemoteFlowable(rxCache, key, source, CacheTarget.Disk, false)
        return Flowable
                .concatDelayError(Arrays.asList(remote, cache))
                .take(1)
    }
}