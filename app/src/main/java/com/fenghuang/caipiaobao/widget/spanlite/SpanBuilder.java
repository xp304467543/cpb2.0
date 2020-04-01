package com.fenghuang.caipiaobao.widget.spanlite;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;

/**
 * @ Author  QinTian
 * @ Date  2020-02-20
 * @ Describe span建造者模式，职责建造一个子build,便于清晰
 */
public class SpanBuilder {

    private SpannableStringBuilder spanBuilder;
    private String content;

    private SpanBuilder() {
    }

    private SpanBuilder(String content) {
        this.content = content;
        spanBuilder = new SpannableStringBuilder( content );
    }

    public static SpanBuilder  Builder(String content) {
        return new SpanBuilder( content );
    }

    //模糊效果
    public SpanBuilder drawBlurMaskFilter(float radius, BlurMaskFilter.Blur style) {
        Util.setSpanExEx( spanBuilder, Util.drawBlurMaskFilter( radius, style ) );
        return this;
    }

    //图片置换
    public SpanBuilder drawImage(Context context, @DrawableRes int drawableRes) {
        Util.setSpanExEx( spanBuilder, Util.drawImageSpan( context, drawableRes ) );
        return this;
    }

    //图片置换
    public SpanBuilder drawImage(Drawable drawable, int verticalAlignment) {
        Util.setSpanExEx( spanBuilder, Util.drawImageSpan( drawable, verticalAlignment ) );
        return this;
    }
    //图片居中

    public SpanBuilder drawImageCenter(Context context, @DrawableRes int drawableRes) {
        Util.setSpanExEx( spanBuilder, Util.drawImageSpanCenter( context, drawableRes ) );
        return this;
    }
    //图片置换
    public SpanBuilder drawImageDrawable(Context context, @DrawableRes int drawableRes) {
        Util.setSpanExEx( spanBuilder, Util.drawImageDrawableSpan( context, drawableRes ) );
        return this;
    }

    //图片置换
    public SpanBuilder drawImageWidthHeight(Context context, @DrawableRes int drawableRes, int width, int height) {
        Util.setSpanExEx( spanBuilder, Util.drawImageSpanWidthHeight( context, drawableRes, width, height ) );
        return this;
    }

    //设置图片
    public SpanBuilder drawDynamicDrawable(DynamicDrawableSpan dynamicDrawableSpan) {
        Util.setSpanExEx( spanBuilder, dynamicDrawableSpan );
        return this;
    }

    //下划线
    public SpanBuilder drawUnderline() {
        Util.setSpanExEx( spanBuilder, Util.drawUnderlineSpan() );
        return this;
    }

    //删除线
    public SpanBuilder drawStrikethrough() {
        Util.setSpanExEx( spanBuilder, Util.drawStrikethroughSpan() );
        return this;
    }

    //粗体斜体
    public SpanBuilder drawTypeFaceBoldItalic() {
        Util.setSpanExEx( spanBuilder, Util.drawTypeFaceBoldItalic() );
        return this;
    }

    //粗体
    public SpanBuilder drawTypeFaceBold() {
        Util.setSpanExEx( spanBuilder, Util.drawTypeFaceBold() );
        return this;
    }

    //斜体
    public SpanBuilder drawTypeFaceItalic() {
        Util.setSpanExEx( spanBuilder, Util.drawTypeFaceItalic() );
        return this;
    }

    //正常
    public SpanBuilder drawTypeFaceNormal() {
        Util.setSpanExEx( spanBuilder, Util.drawTypeFaceNormal() );
        return this;
    }

    //字体绝对大小
    public SpanBuilder drawTextSizeAbsolute(int size) {
        Util.setSpanExEx( spanBuilder, Util.drawTextSizeAbsolute( size ) );
        return this;
    }

    //字体相对大小
    public SpanBuilder drawTextSizeRelative(int size) {
        Util.setSpanExEx( spanBuilder, Util.drawTextSizeRelative( size ) );
        return this;
    }

    //基于X的缩放
    public SpanBuilder drawScaleX(float proportion) {
        Util.setSpanExEx( spanBuilder, Util.drawScaleXSpan( proportion ) );
        return this;
    }

    //上标
    public SpanBuilder drawSuperscript() {
        Util.setSpanExEx( spanBuilder, Util.drawSuperscriptSpan() );
        return this;
    }

    //下标
    public SpanBuilder drawSubscript() {
        Util.setSpanExEx( spanBuilder, Util.drawSubscriptSpan() );
        return this;
    }

    //设置字体
    @RequiresApi(api = Build.VERSION_CODES.P)
    public SpanBuilder drawTypeface(Typeface typeface) {
        Util.setSpanExEx( spanBuilder, Util.drawTypefaceSpan(typeface) );
        return this;
    }

    //设置字体
    public SpanBuilder drawTypeface(String typeface) {
        Util.setSpanExEx( spanBuilder, Util.drawTypefaceSpan(typeface) );
        return this;
    }

    //文本超链接
    public SpanBuilder drawURL(String url) {
        Util.setSpanExEx( spanBuilder, Util.drawURLSpan(url) );
        return this;
    }

    //设置文字style
    public SpanBuilder drawTextAppearance(Context context, @StyleRes int style) {
        Util.setSpanExEx( spanBuilder, Util.drawTextAppearanceSpan(context, style ) );
        return this;
    }

    //字体颜色
    public SpanBuilder drawTextColor(int color) {
        Util.setSpanExEx( spanBuilder, Util.drawTextColorSpan( color ) );
        return this;
    }

    //字体背景颜色
    public SpanBuilder drawTextBackgroundColor(int color) {
        Util.setSpanExEx( spanBuilder, Util.drawTextBackgroundColorSpan( color ) );
        return this;
    }

    //字体颜色
    public SpanBuilder drawTextColor(String colorString) {
        Util.setSpanExEx( spanBuilder, Util.drawTextColorSpan( colorString ) );
        return this;
    }

    //字体背景颜色
    public SpanBuilder drawTextBackgroundColor(String colorString) {
        Util.setSpanExEx( spanBuilder, Util.drawTextBackgroundColorSpan( colorString ) );
        return this;
    }

    //字体背景颜色
    public SpanBuilder setOnClick(final Listener listener) {

        Util.setSpanExEx( spanBuilder, new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (listener != null) {
                    listener.onClick( widget );
                }
            }

            //去除SpannableString下划线
            public void updateDrawState(TextPaint ds) {
                /**set textColor**/
//                ds.setColor( ds.linkColor );
                /**Remove the underline**/
                ds.setUnderlineText( false );
                if (listener != null) {
                    listener.updateDrawState( ds );
                }
            }
        } );

        return this;
    }

    //用抽象类减少不必要接口
   public abstract static class Listener {
       public abstract void onClick(View widget);

       public void updateDrawState(TextPaint ds) {
        }
    }

    public SpannableStringBuilder build() {
        return spanBuilder;
    }
}
