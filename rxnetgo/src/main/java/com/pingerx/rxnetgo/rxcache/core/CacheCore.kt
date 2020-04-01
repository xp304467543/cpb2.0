package com.pingerx.rxnetgo.rxcache.core

import com.pingerx.rxnetgo.rxcache.data.CacheFrom
import com.pingerx.rxnetgo.rxcache.data.CacheResult
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 2019/2/21 19:03
 *
 * 缓存核心类，封装加载缓存，保存缓存，清除缓存等方法
 */
class CacheCore(private val memory: LruMemoryCache?, private val disk: LruDiskCache?) {

    fun <T> load(key: String, type: Type): CacheResult<T>? {
        val memoryResult = memory?.load<T>(key)
        if (memoryResult?.data != null) {
            return CacheResult(CacheFrom.Memory, key, memoryResult.data, memoryResult.timestamp)
        }
        val diskResult = disk?.load<T>(key, type)
        if (diskResult?.data != null) {
            return CacheResult(CacheFrom.Disk, key, diskResult.data, diskResult.timestamp)
        }
        return null
    }

    /**
     * 保存
     */
    fun <T> save(key: String, value: T?, target: CacheTarget): Boolean {
        if (value == null) {
            var memoryRemove = true
            if (memory != null) {
                memoryRemove = memory.remove(key)
            }
            var diskRemove = true
            if (disk != null) {
                diskRemove = disk.remove(key)
            }
            return memoryRemove && diskRemove
        }
        var save = false
        if (target.supportMemory() && memory != null) {
            save = memory.save(key, value)
        }
        return if (target.supportDisk() && disk != null) {
            disk.save(key, value)
        } else save
    }

    /**
     * 是否包含
     */
    fun containsKey(key: String): Boolean {
        return memory?.containsKey(key) == true || disk?.containsKey(key) == true
    }

    /**
     * 删除缓存
     */
    fun remove(key: String): Boolean {
        val isRemove = memory?.remove(key) ?: false
        return isRemove and (disk?.remove(key) ?: false)
    }

    /**
     * 清空缓存
     */
    fun clear() {
        memory?.clear()
        disk?.clear()
    }
}