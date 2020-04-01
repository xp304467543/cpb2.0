package com.pingerx.rxnetgo.rxcache.core

/**
 * @author Pinger
 * @since 2019/2/21 19:07
 *
 * 缓存的类型，包括内存缓存，磁盘缓存和两个都缓存
 */
enum class CacheTarget {
    Memory,
    Disk,
    MemoryAndDisk;

    fun supportMemory(): Boolean {
        return this == Memory || this == MemoryAndDisk
    }

    fun supportDisk(): Boolean {
        return this == Disk || this == MemoryAndDisk
    }
}
