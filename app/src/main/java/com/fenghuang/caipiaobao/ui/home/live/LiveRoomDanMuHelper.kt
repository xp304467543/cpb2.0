package com.fenghuang.caipiaobao.ui.home.live

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.DynamicDrawableSpan.ALIGN_CENTER
import android.text.style.ForegroundColorSpan
import com.fenghuang.baselib.utils.ViewUtils
import com.fenghuang.caipiaobao.R
import com.fenghuang.caipiaobao.ui.home.data.DanMu
import com.fenghuang.caipiaobao.widget.chat.EmojiconHandler
import com.fenghuang.caipiaobao.widget.chat.bean.EmojiconSpan
import com.fenghuang.caipiaobao.widget.spanlite.CenterAlignImageSpan
import com.fenghuang.caipiaobao.widget.spanlite.SpanBuilder
import com.fenghuang.caipiaobao.widget.videoplayer.utils.BiliDanmukuParser
import master.flame.danmaku.controller.IDanmakuView
import master.flame.danmaku.danmaku.loader.IllegalDataException
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDisplayer
import master.flame.danmaku.danmaku.model.android.BaseCacheStuffer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import java.io.InputStream
import java.util.*
import java.util.regex.Pattern

/**
 *
 * @ Author  QinTian
 * @ Date  2020-03-13
 * @ Describe
 *
 */


class LiveRoomDanMuHelper {


    var mDanmakuView: IDanmakuView? = null
    private var mDanmakuContext: DanmakuContext? = null
    private var mContext: Context? = null
    private var mParser: BaseDanmakuParser? = null
    private var mCacheStufferAdapter: BaseCacheStuffer.Proxy? = null


    @SuppressLint("UseSparseArrays")
    fun initDanMu(danmaku: IDanmakuView, context: Context) {
        this.mContext = context
        mCacheStufferAdapter = object : BaseCacheStuffer.Proxy() {
            override fun releaseResource(danmaku: BaseDanmaku?) {
                // TODO 重要:清理含有ImageSpan的text中的一些占用内存的资源 例如drawable
            }

            override fun prepareDrawing(danmaku: BaseDanmaku?, fromWorkerThread: Boolean) {
                // FIXME 这里只是简单启个线程来加载远程url图片，请使用你自己的异步线程池，最好加上你的缓存池

            }

        }
        // 设置最大显示行数
        val maxLinesPair = HashMap<Int, Int>()
        maxLinesPair[BaseDanmaku.TYPE_SCROLL_RL] = 5 // 滚动弹幕最大显示5行
        // 设置是否禁止重叠
        val overlappingEnablePair = HashMap<Int, Boolean>()
        overlappingEnablePair[BaseDanmaku.TYPE_SCROLL_RL] = true
        overlappingEnablePair[BaseDanmaku.TYPE_FIX_TOP] = true
        mDanmakuView = danmaku
        mDanmakuContext = DanmakuContext.create()
        mDanmakuContext?.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3f)
                ?.setDuplicateMergingEnabled(false)?.setScrollSpeedFactor(1.2f)?.setScaleTextSize(1.2f)
                ?.setCacheStuffer(SpannedCacheStuffer(), mCacheStufferAdapter) // 图文混排使用SpannedCacheStuffer
                //        .setCacheStuffer(new BackgroundCacheStuffer())  // 绘制背景使用BackgroundCacheStuffer
                ?.setMaximumLines(maxLinesPair)
                ?.preventOverlapping(overlappingEnablePair)?.setDanmakuMargin(40)
        if (mDanmakuView != null) {
            //            mParser = createParser(this.getResources().openRawResource(R.raw.comments));
            mParser = createParser(null)
            mDanmakuView?.setCallback(object : master.flame.danmaku.controller.DrawHandler.Callback {
                override fun updateTimer(timer: DanmakuTimer) {}

                override fun drawingFinished() {

                }

                override fun danmakuShown(danmaku: BaseDanmaku) {
                    //                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
                }

                override fun prepared() {
                    mDanmakuView?.start()
                }
            })
            mDanmakuView?.prepare(mParser, mDanmakuContext)
            mDanmakuView?.showFPS(false)
            mDanmakuView?.enableDanmakuDrawingCache(true)
        }
    }

    private fun createParser(stream: InputStream?): BaseDanmakuParser {

        if (stream == null) {
            return object : BaseDanmakuParser() {

                override fun parse(): Danmakus {
                    return Danmakus()
                }
            }
        }

        val loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI)

        try {
            loader.load(stream)
        } catch (e: IllegalDataException) {
            e.printStackTrace()
        }

        val parser = BiliDanmukuParser()
        val dataSource = loader.dataSource
        parser.load(dataSource)
        return parser

    }

    /**
     * 绘制背景(自定义弹幕样式)
     */
    private class BackgroundCacheStuffer : SpannedCacheStuffer() {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        internal val paint = Paint()

        override fun measure(danmaku: BaseDanmaku, paint: TextPaint, fromWorkerThread: Boolean) {
            danmaku.padding = 10  // 在背景绘制模式下增加padding
            super.measure(danmaku, paint, fromWorkerThread)
        }

        public override fun drawBackground(danmaku: BaseDanmaku, canvas: Canvas, left: Float, top: Float) {
            paint.color = -0x7edacf65
            canvas.drawRect(left + 2, top + 2, left + danmaku.paintWidth - 2, top + danmaku.paintHeight - 2, paint)
        }

        override fun drawStroke(danmaku: BaseDanmaku, lineText: String?, canvas: Canvas, left: Float, top: Float, paint: Paint) {
            // 禁用描边绘制
        }
    }


    fun addDanmaku(data: DanMu, isLive: Boolean) {
        val danmaku = mDanmakuContext?.mDanmakuFactory?.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
        if (danmaku == null || mDanmakuView == null) {
            return
        }
        danmaku.text = data.userName + " : " + data.text
        danmaku.padding = 5
        danmaku.priority = 0  // 可能会被各种过滤器过滤并隐藏显示
        danmaku.isLive = isLive
        danmaku.time = mDanmakuView?.currentTime!! + 1200
        danmaku.textSize = 15f * (mParser?.displayer?.density!! - 0.6f)
        danmaku.textColor = Color.WHITE
        danmaku.textShadowColor = Color.GRAY
        // danmaku.underlineColor = Color.GREEN;
//        danmaku.borderColor = Color.GREEN
        mDanmakuView?.addDanmaku(danmaku)

    }

    fun addDanmaKuShowTextAndImage(data: DanMu, islive: Boolean) {
        val danmaku = mDanmakuContext?.mDanmakuFactory?.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
        val spannable = createSpannable(data)
        danmaku?.text = spannable
        danmaku?.padding = 5
        danmaku?.priority = 1  // 一定会显示, 一般用于本机发送的弹幕
        danmaku?.isLive = islive
        danmaku?.time = mDanmakuView?.currentTime!! + 1200
        danmaku?.textSize = 15f * (mParser?.displayer?.density?.minus(0.6f)!!)
        danmaku?.textColor = Color.WHITE
//        danmaku?.textShadowColor = Color.GRAY
//        danmaku?.textShadowColor = 0 // 重要：如果有图文混排，最好不要设置描边(设textShadowColor=0)，否则会进行两次复杂的绘制导致运行效率降低
//        danmaku?.underlineColor = Color.GREEN
        mDanmakuView?.addDanmaku(danmaku)
    }

    private fun createSpannable(data: DanMu): SpannableStringBuilder {
        val content = replaceBlank(data.text)
        when (data.userType) {
            "1" -> {
                val spanBuilder = SpanBuilder.Builder("1").drawImageWidthHeight(mContext, R.mipmap.ic_live_anchor, 100, 44).build()
                spanBuilder.append(" " + data.userName + " : " + content)
                return spanBuilder
            }

            "2" -> {
                val spanBuilder = SpanBuilder.Builder("1").drawImageWidthHeight(mContext, R.mipmap.ic_live_chat_manager, 100, 44).build()
                spanBuilder.append(" " + data.userName + " : " + content)
                return spanBuilder
            }

            else -> {
                val spanBuilder = SpannableStringBuilder()

                val drawable = when (data.vip) {
                    "1" -> {
                        ViewUtils.getDrawable(R.mipmap.v1)
                    }
                    "2" -> {
                        ViewUtils.getDrawable(R.mipmap.v2)
                    }
                    "3" -> {
                        ViewUtils.getDrawable(R.mipmap.v3)
                    }
                    "4" -> {
                        ViewUtils.getDrawable(R.mipmap.v4)
                    }
                    "5" -> {
                        ViewUtils.getDrawable(R.mipmap.v5)
                    }
                    "6" -> {
                        ViewUtils.getDrawable(R.mipmap.v6)
                    }
                    "7" -> {
                        ViewUtils.getDrawable(R.mipmap.v7)
                    }
                    else -> {
                        spanBuilder.append(" " + data.userName + " : ")
                        spanBuilder.append(isHasEmote(content))
                        return spanBuilder
                    }
                }
                drawable?.setBounds(0, 0, 100, 44)
                val span = CenterAlignImageSpan(drawable)
                spanBuilder.append("icon", span, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                spanBuilder.append(" " + data.userName + " : ")
                spanBuilder.append(isHasEmote(content))
                return spanBuilder
            }
        }
    }

    private fun isHasEmote(str: String): SpannableStringBuilder {
        val matcher = EmojiconHandler.CAIPIAOBAO.matcher(str)
        val builder = SpannableStringBuilder(str)
        while (matcher.find()) {
            val key = matcher.group()
            if (EmojiconHandler.fengHuangjisModifiedMap.containsKey(key)) {
                val resId = EmojiconHandler.fengHuangjisModifiedMap[key]
                val emoticonSpan = EmojiconSpan(mContext, resId!!, 50, ALIGN_CENTER, 50)
                builder.setSpan(emoticonSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            if (EmojiconHandler.caiPiaoBaoEmojisModifiedMap.containsKey(key)) {
                val resId = EmojiconHandler.caiPiaoBaoEmojisModifiedMap[key]
                val emoticonSpan = EmojiconSpan(mContext, resId!!, 50, ALIGN_CENTER, 50)
                builder.setSpan(emoticonSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        builder.setSpan(ForegroundColorSpan(ViewUtils.getColor(R.color.white)), 0, str.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return builder
    }

    //去掉换行符
    private fun replaceBlank(src: String?): String {
        var dest = ""
        if (src != null) {
            val pattern = Pattern.compile("\n\\s*")
            val matcher = pattern.matcher(src)
            dest = matcher.replaceAll(" ")
        }
        return dest
    }

    //清除弹幕
    fun clear(){
        mDanmakuView?.clearDanmakusOnScreen()
    }

}