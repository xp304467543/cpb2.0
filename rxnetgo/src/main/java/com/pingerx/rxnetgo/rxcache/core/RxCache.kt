package com.pingerx.rxnetgo.rxcache.core

import android.os.Environment
import android.os.StatFs
import com.pingerx.rxnetgo.rxcache.data.CacheResult
import com.pingerx.rxnetgo.rxcache.diskconverter.IDiskConverter
import com.pingerx.rxnetgo.rxcache.diskconverter.SerializableDiskConverter
import com.pingerx.rxnetgo.rxcache.stategy.ICacheStrategy
import com.pingerx.rxnetgo.utils.NetLogger
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.security.MessageDigest


/**
 * @author Pinger
 * @since 2019/2/21 15:10
 *
 * Rx缓存，使用Flowable进行异步操作
 */
class RxCache private constructor(private val cacheCore: CacheCore) {

    /**
     * 同步读取缓存
     * 会阻塞主线程，请在子线程调用
     */
    fun <T> loadSync(key: String, type: Type): CacheResult<T>? {
        return cacheCore.load(getMD5MessageDigest(key), type)
    }

    /**
     * 异步读取缓存
     */
    fun <T> load(key: String, type: Type): Flowable<CacheResult<T>> {
        return load(key, type, BackpressureStrategy.LATEST)
    }

    fun <T> load(key: String, type: Type, backpressureStrategy: BackpressureStrategy): Flowable<CacheResult<T>> {
        return Flowable.create({ flowableEmitter ->
            val load = cacheCore.load<T>(getMD5MessageDigest(key), type)
            if (!flowableEmitter.isCancelled) {
                if (load != null) {
                    flowableEmitter.onNext(load)
                    flowableEmitter.onComplete()
                } else {
                    flowableEmitter.onError(NullPointerException("Not find the key corresponding to the cache"))
                }
            }
        }, backpressureStrategy)
    }

    fun <T> transformFlowable(key: String, type: Type, strategy: ICacheStrategy): FlowableTransformer<T, CacheResult<T>> {
        return FlowableTransformer { flowable -> strategy.execute(this@RxCache, key, flowable, type) }
    }

    fun <T> save(key: String, value: T, target: CacheTarget = CacheTarget.Disk): Flowable<Boolean> {
        return save(key, value, target, BackpressureStrategy.LATEST)
    }

    fun <T> save(key: String, value: T, target: CacheTarget, strategy: BackpressureStrategy): Flowable<Boolean> {
        return Flowable.create({ flowableEmitter ->
            val save = cacheCore.save<Any>(getMD5MessageDigest(key), value, target)
            if (!flowableEmitter.isCancelled) {
                flowableEmitter.onNext(save)
                flowableEmitter.onComplete()
            }
        }, strategy)
    }

    fun containsKey(key: String): Boolean {
        return cacheCore.containsKey(getMD5MessageDigest(key))
    }

    fun remove(key: String): Boolean {
        return cacheCore.remove(getMD5MessageDigest(key))
    }

    @Throws(IOException::class)
    fun clear() {
        cacheCore.clear()
    }

    /**
     * 构造器
     */
    class Builder {
        private var memoryMaxSize: Int = 0
        private var diskMaxSize: Long = 0
        private var appVersion: Int = 0
        private var diskDir: File? = null
        private var diskConverter: IDiskConverter? = null

        /**
         * 不设置,默认为运行内存的8分之1.设置0,或小于0，则不开启内存缓存;
         */
        fun memoryMax(maxSize: Int): Builder {
            this.memoryMaxSize = maxSize
            return this
        }

        /**
         * 不设置，默认为1.需要注意的是,每当版本号改变,缓存路径下存储的所有数据都会被清除掉,所有的数据都应该从网上重新获取.
         */
        fun appVersion(appVersion: Int): Builder {
            this.appVersion = appVersion
            return this
        }

        fun diskDir(directory: File): Builder {
            this.diskDir = directory
            return this
        }

        fun diskConverter(converter: IDiskConverter): Builder {
            this.diskConverter = converter
            return this
        }

        /**
         * 不设置， 默为认50MB.设置0,或小于0，则不开启硬盘缓存;
         */
        fun diskMax(maxSize: Long): Builder {
            this.diskMaxSize = maxSize
            return this
        }

        fun build(): RxCache {
            appVersion = Math.max(1, this.appVersion)
            diskDir?.let {
                if (!it.exists() && !it.mkdirs()) {
                    throw RuntimeException("can't make dirs in " + it.absolutePath)
                }
            }

            if (this.diskConverter == null) {
                this.diskConverter = SerializableDiskConverter()
            }
            if (diskMaxSize == 0L) {
                diskMaxSize = calculateDiskCacheSize(diskDir!!)
            }

            if (memoryMaxSize == 0) memoryMaxSize = DEFAULT_MEMORY_CACHE_SIZE

            var memoryCache: LruMemoryCache? = null
            if (memoryMaxSize > 0) {
                memoryCache = LruMemoryCache(memoryMaxSize)
            }
            var lruDiskCache: LruDiskCache? = null
            if (diskDir != null && diskMaxSize > 0) {
                lruDiskCache = LruDiskCache(diskConverter!!, diskDir!!, appVersion, diskMaxSize)
            }
            val cacheCore = CacheCore(memoryCache, lruDiskCache)
            return RxCache(cacheCore)
        }

        companion object {
            private const val MIN_DISK_CACHE_SIZE = 5 * 1024 * 1024    // 5MB
            private const val MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024   // 50MB
            private val DEFAULT_MEMORY_CACHE_SIZE = (Runtime.getRuntime().maxMemory() / 8).toInt()   //运行内存的8分之1

            private fun calculateDiskCacheSize(dir: File): Long {
                var size: Long = 0
                try {
                    val statFs = StatFs(dir.absolutePath)
                    val available = statFs.blockCountLong * statFs.blockCountLong
                    // Target 2% of the total space.
                    size = available / 50
                } catch (ignored: IllegalArgumentException) {
                    NetLogger.e(ignored)
                }
                // Bound inside min/max size for disk cache.
                return Math.max(Math.min(size, MAX_DISK_CACHE_SIZE.toLong()), MIN_DISK_CACHE_SIZE.toLong())
            }
        }
    }


    private fun getMD5MessageDigest(buffer: String): String {
        val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        try {
            val mdTemp = MessageDigest.getInstance("MD5")
            mdTemp.update(buffer.toByteArray())
            val md = mdTemp.digest()
            val j = md.size
            val str = CharArray(j * 2)
            var k = 0
            for (byte0 in md) {
                str[k++] = hexDigits[byte0.toInt().ushr(4) and 0xf]
                str[k++] = hexDigits[byte0.toInt() and 0xf]
            }
            return String(str)
        } catch (e: Exception) {
            NetLogger.e(e)
            return buffer
        }

    }


    companion object {
        private var sDefaultRxCache: RxCache? = null

        fun getDefault(): RxCache {
            if (sDefaultRxCache == null) {
                sDefaultRxCache = Builder()
                        .appVersion(1)
                        .diskDir(Environment.getDownloadCacheDirectory())
                        .diskConverter(SerializableDiskConverter())
                        .build()
            }
            return sDefaultRxCache!!
        }

        fun initializeDefault(rxCache: RxCache) {
            if (sDefaultRxCache == null) {
                sDefaultRxCache = rxCache
            } else {
                NetLogger.e("You need to initialize it before using the default rxCache and only initialize it once")
            }
        }
    }

}
