package com.fenghuang.baselib.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * 图片处理工具类
 */
object BitmapUtils {


    /**
     * Bitmap保存为文件
     */
    fun saveBitmapToFile(bitmap: Bitmap, path: String): File? {
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(path)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return File(path)
    }

    /**
     * Bitmap 转 bytes
     */
    fun bitmapToBytes(bitmap: Bitmap?): ByteArray? {
        var byteArrayOutputStream: ByteArrayOutputStream? = null
        if (bitmap != null && !bitmap.isRecycled) {
            try {
                byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                if (byteArrayOutputStream.toByteArray() == null) {
                    LogUtils.e("BitmapUtils", "bitmap2Bytes byteArrayOutputStream toByteArray=null")
                }
                return byteArrayOutputStream.toByteArray()
            } catch (e: Exception) {
                LogUtils.e("BitmapUtils", e.toString())
            } finally {
                try {
                    byteArrayOutputStream?.close()
                } catch (var14: IOException) {
                }
            }
            return null
        } else {
            LogUtils.e("BitmapUtils", "bitmap2Bytes bitmap == null or bitmap.isRecycled()")
            return null
        }
    }

    /**
     * 在保证质量的情况下尽可能压缩，不保证压缩到指定字节
     */
    fun compressBitmap(data: ByteArray?, byteCount: Int): ByteArray? {
        var isFinish = false
        if (data != null && data.size > byteCount) {
            val outputStream = ByteArrayOutputStream()
            val tmpBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            var times = 1
            var percentage: Double
            while (!isFinish && times <= 10) {
                percentage = Math.pow(0.8, times.toDouble())
                val compressData = (100.0 * percentage).toInt()
                tmpBitmap.compress(Bitmap.CompressFormat.JPEG, compressData, outputStream)
                if (outputStream.size() < byteCount) {
                    isFinish = true
                } else {
                    outputStream.reset()
                    ++times
                }
            }
            val outputStreamByte = outputStream.toByteArray()
            if (!tmpBitmap.isRecycled) {
                tmpBitmap.recycle()
            }
            if (outputStreamByte.size > byteCount) {
                LogUtils.e(
                        "BitmapUtils",
                        "compressBitmap cannot compress to " + byteCount + ", after compress size=" + outputStreamByte.size
                )
            }
            return outputStreamByte
        }
        return data
    }


    private const val BITMAP_SCALE: Float = 0.4f
    private const val BLUR_RADIUS: Int = 7

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun blur(context: Context, bitmap: Bitmap, scale: Float = BITMAP_SCALE, radius: Int = BLUR_RADIUS): Bitmap {
        //先对图片进行压缩然后再blur
        val inputBitmap: Bitmap = Bitmap.createScaledBitmap(
                bitmap, Math.round(bitmap.width * scale),
                Math.round(bitmap.height * scale), false
        )
        //创建空的Bitmap用于输出
        val outputBitmap: Bitmap = Bitmap.createBitmap(inputBitmap)
        //①、初始化Renderscript
        val rs: RenderScript = RenderScript.create(context)
        //②、Create an Intrinsic Blur Script using the Renderscript
        val theIntrinsic: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        //③、native层分配内存空间
        val tmpIn: Allocation = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut: Allocation = Allocation.createFromBitmap(rs, outputBitmap)
        //④、设置blur的半径然后进行blur
        theIntrinsic.setRadius(radius.toFloat())
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        //⑤、拷贝blur后的数据到java缓冲区中
        tmpOut.copyTo(outputBitmap)
        //⑥、销毁Renderscript
        rs.destroy()
        bitmap.recycle()
        return outputBitmap
    }
}