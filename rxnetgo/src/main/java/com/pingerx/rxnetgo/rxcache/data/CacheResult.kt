package com.pingerx.rxnetgo.rxcache.data

import io.reactivex.functions.Function

/**
 * @author Pinger
 * @since 2019/2/21 18:28
 *
 * 缓存数据包装
 */
data class CacheResult<T>(
        val from: CacheFrom,
        val key: String,
        val data: T? = null,
        private val timestamp: Long = 0) {


    /**
     * 用于map操作符，只想拿CacheResult.data的数据
     */
    class MapFunc<T> : Function<CacheResult<T>, T> {
        override fun apply(t: CacheResult<T>): T? {
            return t.data
        }
    }
}