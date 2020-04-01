package com.pingerx.rxnetgo.rxcache.core


import com.jakewharton.disklrucache.DiskLruCache
import com.pingerx.rxnetgo.rxcache.diskconverter.IDiskConverter
import com.pingerx.rxnetgo.utils.NetLogger
import java.io.File
import java.io.IOException
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 2019/2/21 19:16
 *
 * LRU磁盘缓存
 */
class LruDiskCache(private val diskConverter: IDiskConverter, diskDir: File, appVersion: Int, diskMaxSize: Long) {

    private var mDiskLruCache: DiskLruCache = DiskLruCache.open(diskDir, appVersion, 2, diskMaxSize)

    @Throws(IOException::class)
    private fun deleteContents(dir: File) {
        val files = dir.listFiles() ?: throw IOException("not a readable directory: $dir")
        for (file in files) {
            if (file.isDirectory) {
                deleteContents(file)
            }
            if (!file.delete()) {
                throw IOException("failed to delete file: $file")
            }
        }
    }

    fun <T> load(key: String, type: Type): CacheHolder<T> {
        try {
            val snapshot = mDiskLruCache.get(key)
            if (snapshot != null) {
                val source = snapshot.getInputStream(0)
                val value = diskConverter.load<T>(source, type)
                var timestamp: Long = 0
                val string = snapshot.getString(1)
                if (string != null) {
                    timestamp = string.toLong()
                }
                snapshot.close()
                return CacheHolder(value, timestamp)
            }
        } catch (e: IOException) {
            NetLogger.e(e)
        }
        return CacheHolder()
    }

    fun <T> save(key: String, value: T?): Boolean {
        // 如果要保存的值为空,则删除
        if (value == null) {
            return remove(key)
        }
        var edit: DiskLruCache.Editor? = null
        try {
            edit = mDiskLruCache.edit(key)
            val sink = edit!!.newOutputStream(0)
            diskConverter.writer(sink, value)
            val l = System.currentTimeMillis()
            edit.set(1, l.toString())
            edit.commit()
            NetLogger.e("save:  value=$value , status=true")
            return true
        } catch (e: IOException) {
            NetLogger.e(e)
            if (edit != null) {
                try {
                    edit.abort()
                } catch (e1: IOException) {
                    NetLogger.e(e1)
                }
            }
            NetLogger.e("save:  value=$value , status=false")
        }
        return false
    }

    fun containsKey(key: String): Boolean {
        try {
            return mDiskLruCache.get(key) != null
        } catch (e: IOException) {
            NetLogger.e(e)
        }
        return false
    }

    /**
     * 删除缓存
     */
    fun remove(key: String): Boolean {
        try {
            return mDiskLruCache.remove(key)
        } catch (e: IOException) {
            NetLogger.e(e)
        }
        return false
    }

    @Throws(IOException::class)
    fun clear() {
        deleteContents(mDiskLruCache.directory)
    }


}
