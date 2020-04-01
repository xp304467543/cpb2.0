package com.fenghuang.caipiaobao.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.XMLReader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * @ Author  QinTian
 * @ Date  2020-03-03
 * 将html设置到textview中的工具类
 * textview有方法 setText(Html.fromHtml(source)),其中source是指定的字符串，包含html标签,用setText(Html.fromhtml(source))可以去除其中的标签，实际上是识别标签，但是这种简单的做法
 * 并不能将其中的图片加载出来
 * 此工具类可以将html代码中的img标签识别并进行图片资源请求
 * 同时可以将img标签进行处理 添加上点击事件
 ————————————————
 */
public class HtmlFromUtils {
    /**
     * 网络请求获取图片
     */
    private static Drawable getImageFromNetwork(String imageUrl) {
        URL myFileUrl = null;
        Drawable drawable = null;
        try {
            myFileUrl = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            drawable = Drawable.createFromStream(is, null);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return drawable;
    }

    private static Drawable drawable;

    /**
     * 将html字符串中的图片加载出来 设置点击事件 然后TextView进行显示
     *
     * @param context
     * @param v
     * @param sources
     */
    public static void setTextFromHtml(final Activity context, final TextView v, final String sources) {
        if (TextUtils.isEmpty(sources) || context == null || v == null)
            return;
        synchronized (HtmlFromUtils.class) {//同步锁
            v.setMovementMethod(LinkMovementMethod.getInstance());//如果想对img标签添加点击事件必须调用这句 使图片可以获取焦点
            v.setText(Html.fromHtml(sources));//默认不处理图片先这样简单设置
            //开启线程加载其中的图片
            new Thread(() -> {
                //Html.fromhtml方法中有一个参数 就是ImageGetter 此类负责加载source中的图片
                Html.ImageGetter imageGetter = source -> {
//                    source = "http://www.dujiaoshou.com/" + source;//source就是img标签中src属性值，相对路径的此处可以对其进行处理添加头部
                    drawable = getImageFromNetwork(source);
                    if (drawable != null) {

                        int w = drawable.getIntrinsicWidth();
                        int h = drawable.getIntrinsicHeight();
                        //对图片大小进行等比例放大 此处宽高可自行调整
                        if (w < h && h > 0) {
                            float scale = (1000.0f / h);
                            w = (int) (scale * w);
                            h = (int) (scale * h);
                        } else if (w > h && w > 0) {
                            float scale = (1000.0f / w);
                            w = (int) (scale * w);
                            h = (int) (scale * h);
                        }

                        drawable.setBounds(0, 0, w, h);
                    } else if (drawable == null) {
                        //bindData();
                        return null;
                    }
                    return drawable;
                };
                //第三个参数 new URLTagHandler(context)负责添加img标签的点击事件
                final CharSequence charSequence = Html.fromHtml(sources, imageGetter, new URLTagHandler(context));
                //在activiy的runOnUiThread方法中更新ui
                context.runOnUiThread(() -> v.setText(charSequence));


            }).start();
        }

    }


    /**
     * 此类负责处理source字符串中的img标签 对其添加点击事件
     */
    private static class URLTagHandler implements Html.TagHandler {

        private Context mContext;

        public URLTagHandler(Context context) {
            mContext = context.getApplicationContext();
        }

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            // 处理标签<img>
            if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
                // 获取长度
                int len = output.length();
                // 获取图片地址
                ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
                String imgURL = images[0].getSource();
                // 使图片可点击并监听点击事件
                output.setSpan(new URLTagHandler.ClickableImage(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        private class ClickableImage extends ClickableSpan {
            private String url;
            private Context context;

            public ClickableImage(Context context, String url) {
                this.context = context;
                this.url = url;
            }

            @Override
            public void onClick(View widget) {
                // 进行图片点击之后的处理
                Toast.makeText(context, "点击图片的地址" + url, Toast.LENGTH_LONG).show();
            }
        }
    }

}
