package com.pingerx.rxnetgo.rxcache.utils

import android.graphics.Bitmap

import com.pingerx.rxnetgo.utils.HttpUtils

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import java.io.Serializable


/**
 * Implements functions useful to check
 * MemorySizeOf usage.
 *
 * @author Pierre Malarme
 * @version 1.0
 */
object MemorySizeOf {

    /**
     * Function that get the size of an object.
     *
     * @return Size in bytes of the object or -1 if the object
     * is null.
     */
    @Throws(IOException::class)
    fun sizeOf(serial: Serializable?): Int {
        if (serial == null) return 0
        val size: Int
        var baos: ByteArrayOutputStream? = null
        var oos: ObjectOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            oos = ObjectOutputStream(baos)
            oos.writeObject(serial)
            oos.flush()  //缓冲流
            size = baos.size()
        } finally {
            HttpUtils.close(oos)
            HttpUtils.close(baos)
        }
        return size
    }


    fun sizeOf(bitmap: Bitmap?): Int {
        if (bitmap == null) return 0
        val size: Int
        var baos: ByteArrayOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            size = baos.size()
        } finally {
            HttpUtils.close(baos)
        }
        return size
    }

}















