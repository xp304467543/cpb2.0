package com.pingerx.rxnetgo.subscribe

import android.graphics.Bitmap
import android.widget.ImageView
import com.pingerx.rxnetgo.convert.BitmapConvert
import com.pingerx.rxnetgo.subscribe.base.BaseSubscriber
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * @author Pinger
 * @since 18-12-3 下午3:51
 *
 * 图片订阅者，生成bitmap对象
 */
class BitmapSubscriber(
        private val maxWidth: Int = 1000,
        private val maxHeight: Int = 1000,
        private val decodeConfig: Bitmap.Config = Bitmap.Config.ARGB_8888,
        private val scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER_INSIDE) : BaseSubscriber<Bitmap>() {

    @Throws(Exception::class)
    override fun convertResponse(body: ResponseBody?): Bitmap {
        return BitmapConvert(maxWidth, maxHeight, decodeConfig, scaleType).convertResponse(body)
    }

    override fun getType(): Type {
        return Bitmap::class.java
    }
}