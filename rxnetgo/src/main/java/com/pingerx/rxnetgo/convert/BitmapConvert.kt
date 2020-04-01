package com.pingerx.rxnetgo.convert

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.pingerx.rxnetgo.convert.base.IConverter
import com.pingerx.rxnetgo.exception.ApiException
import com.pingerx.rxnetgo.exception.NetErrorEngine
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-12-3 下午3:47
 *
 * 图片流转换器，返回的是一张图片的流，将流转换成图片
 * 为防止图片尺寸过大，默认图片的最大宽高为1000
 *
 */
class BitmapConvert(
        private val maxWidth: Int = 1000,
        private val maxHeight: Int = 1000,
        private val decodeConfig: Bitmap.Config = Bitmap.Config.ARGB_8888,
        private val scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_INSIDE) : IConverter<Bitmap> {

    @Throws(Exception::class)
    override fun convertResponse(body: ResponseBody?): Bitmap {
        val bytes = body?.bytes()
        body?.close()
        return if (bytes == null) {
            throw ApiException(code = NetErrorEngine.DATA_ERROR)
        } else {
            parse(bytes)
        }
    }


    @Throws(Exception::class)
    private fun parse(byteArray: ByteArray): Bitmap {
        val decodeOptions = BitmapFactory.Options()
        val bitmap: Bitmap
        if (maxWidth == 0 && maxHeight == 0) {
            decodeOptions.inPreferredConfig = decodeConfig
            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, decodeOptions)
        } else {
            decodeOptions.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, decodeOptions)
            val actualWidth = decodeOptions.outWidth
            val actualHeight = decodeOptions.outHeight

            val desiredWidth = getResizedDimension(maxWidth, maxHeight, actualWidth, actualHeight, scaleType)
            val desiredHeight = getResizedDimension(maxHeight, maxWidth, actualHeight, actualWidth, scaleType)

            decodeOptions.inJustDecodeBounds = false
            decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight)
            val tempBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, decodeOptions)

            if (tempBitmap != null && (tempBitmap.width > desiredWidth || tempBitmap.height > desiredHeight)) {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth, desiredHeight, true)
                tempBitmap.recycle()
            } else {
                bitmap = tempBitmap
            }
        }
        return bitmap
    }


    private fun getResizedDimension(maxPrimary: Int, maxSecondary: Int, actualPrimary: Int, actualSecondary: Int, scaleType: ImageView.ScaleType): Int {

        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary
        }

        // If ScaleType.FIT_XY fill the whole rectangle, ignore ratio.
        if (scaleType == ImageView.ScaleType.FIT_XY) {
            return if (maxPrimary == 0) {
                actualPrimary
            } else {
                maxPrimary
            }
        }

        // If primary is unspecified, scale primary to match secondary's scaling ratio.
        if (maxPrimary == 0) {
            val ratio = maxSecondary.toDouble() / actualSecondary.toDouble()
            return (actualPrimary * ratio).toInt()
        }

        if (maxSecondary == 0) {
            return maxPrimary
        }

        val ratio = actualSecondary.toDouble() / actualPrimary.toDouble()
        var resized = maxPrimary

        // If ScaleType.CENTER_CROP fill the whole rectangle, preserve aspect ratio.
        if (scaleType == ImageView.ScaleType.CENTER_CROP) {
            if (resized * ratio < maxSecondary) {
                resized = (maxSecondary / ratio).toInt()
            }
            return resized
        }

        if (resized * ratio > maxSecondary) {
            resized = (maxSecondary / ratio).toInt()
        }
        return resized
    }

    private fun findBestSampleSize(actualWidth: Int, actualHeight: Int, desiredWidth: Int, desiredHeight: Int): Int {
        val wr = actualWidth.toDouble() / desiredWidth
        val hr = actualHeight.toDouble() / desiredHeight
        val ratio = Math.min(wr, hr)
        var n = 1.0f
        while (n * 2 <= ratio) {
            n *= 2f
        }
        return n.toInt()
    }

    override fun getType(): Type {
        return Bitmap::class.java
    }

}