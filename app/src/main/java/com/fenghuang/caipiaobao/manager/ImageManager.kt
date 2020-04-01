package com.fenghuang.caipiaobao.manager


import android.net.Uri
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.fresco.helper.ImageLoader


/**
 * 图片加载管理器，业务层管理
 */
object ImageManager {


    /**
     * 加载图片
     */
    fun loadImg(url: String?, imageView: SimpleDraweeView) {
        ImageLoader.loadImage(imageView, url?.replace("\\", "/"))
    }

    /**
     * 加载Uri
     */
    fun loadUri(uri: Uri?, imageView: SimpleDraweeView){
        ImageLoader.loadImage(imageView,uri)
    }

    /**
     * 加载本地
     */
    fun loadBitmap(imageView: SimpleDraweeView,id:Int) {
        ImageLoader.loadDrawable(imageView, id)
    }


}