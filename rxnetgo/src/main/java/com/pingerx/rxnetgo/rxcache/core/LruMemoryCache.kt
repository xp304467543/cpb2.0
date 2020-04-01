package com.pingerx.rxnetgo.rxcache.core


import android.graphics.Bitmap
import android.util.LruCache
import com.pingerx.rxnetgo.rxcache.utils.MemorySizeOf
import com.pingerx.rxnetgo.rxcache.utils.Occupy
import com.pingerx.rxnetgo.utils.NetLogger
import java.util.*


/**
 * @author Pinger
 * @since 2019/2/21 19:35
 *
 * LRU内存缓存
 */
class LruMemoryCache(cacheSize: Int) {

    private val memorySizeMap: HashMap<String, Int> = HashMap()
    private val timestampMap: HashMap<String, Long> = HashMap()
    private val occupy: Occupy = Occupy(0, 0, 4)
    private val mCache = object : LruCache<String, Any>(cacheSize) {
        override fun sizeOf(key: String, value: Any): Int {
            var integer = memorySizeMap[key]
            if (integer == null) {
                integer = countSize(value)
                memorySizeMap[key] = integer
            }
            return integer
        }

        override fun entryRemoved(evicted: Boolean, key: String, oldValue: Any, newValue: Any?) {
            super.entryRemoved(evicted, key, oldValue, newValue)
            memorySizeMap.remove(key)
            timestampMap.remove(key)
        }
    }


    @Suppress("UNCHECKED_CAST")
    fun <T> load(key: String): CacheHolder<T>? {
        val value = mCache.get(key) as T
        return if (value != null) {
            CacheHolder(value, timestampMap[key] ?: 0)
        } else null
    }

    fun <T> save(key: String, value: T?): Boolean {
        if (null != value) {
            mCache.put(key, value)
            timestampMap[key] = System.currentTimeMillis()
        }
        return true
    }

    fun containsKey(key: String): Boolean {
        return mCache.get(key) != null
    }

    /**
     * 删除缓存
     */
    fun remove(key: String): Boolean {
        val remove = mCache.remove(key)
        if (remove != null) {
            memorySizeMap.remove(key)
            timestampMap.remove(key)
            return true
        }
        return false
    }

    fun clear() {
        mCache.evictAll()
    }

    private fun countSize(value: Any?): Int {
        if (value == null) {
            return 0
        }
        //  更优良的内存大小算法
        val size: Int = if (value is Bitmap) {
            NetLogger.e("Bitmap")
            MemorySizeOf.sizeOf(value as Bitmap?)
        } else {
            occupy.occupyof(value)
        }
        NetLogger.e("size=$size value=$value")
        return if (size > 0) size else 1
    }

}
