package com.fenghuang.caipiaobao.utils;

import android.content.Context;
import android.util.Log;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.util.ArrayList;

/**
 * @ Author  QinTian
 * @ Date  2020-02-27
 * @ Describe
 */
public class SvgaUtils {
    private Context context;
    private ArrayList<String> stringList;
    private SVGAImageView svgaImage;
    private SVGAParser parser;

    public SvgaUtils(Context context, SVGAImageView svgaImage) {
        this.context = context;
        this.svgaImage = svgaImage;
    }

    /**
     * 初始化数据
     */
    public void initAnimator() {
        parser = new SVGAParser(context);
        stringList = new ArrayList<>();
        //监听大动画的控件周期
        svgaImage.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
                Log.e("setCallback", "onPause");
            }

            @Override
            public void onFinished() {
                //当动画结束，如果数组容器大于0，则移除容器第一位的数据，轮询播放动画。
                if (stringList != null && stringList.size() > 0) {
                    stringList.remove(0);
                    //如果移除之后的容器大于0，则开始展示新一个的大动画
                    if (stringList != null && stringList.size() > 0) {
                        try {
                            parseSVGA();//解析加载动画
                        } catch (Exception e) {

                        }
                    } else {
                        stopSVGA();
                    }
                } else {
                    stopSVGA();
                }
            }

            @Override
            public void onRepeat() {
                Log.e("setCallback", "onRepeat=" + stringList.size());
                stopSVGA();
            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }

    public static void i(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.e(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.e(tag, msg);
    }

    /**
     * 显示动画
     */
    public void startAnimator(String svgaName, SVGAImageView svgaImage) {
//        svgaImage.setVisibility(View.VISIBLE);
        stringList.add(stringList.size(), svgaName + ".svga");
        //如果礼物容器列表的数量是1，则解析动画，如果数量不是1，则此处不解析动画，在上一个礼物解析完成之后加载再动画
        if (stringList.size() == 1) {
            parseSVGA();
        }
    }

    /**
     * 解析加载动画
     */
    private void parseSVGA() {
        if (stringList.size() > 0) {
            try {
                parser.parse(stringList.get(0), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                        //解析动画成功，到这里才真正的显示动画
                        SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                        svgaImage.setImageDrawable(drawable);
                        svgaImage.startAnimation();
                    }

                    @Override
                    public void onError() {
                        //如果动画数组列表大于0,移除第一位的动画,继续循环解析
                        if (stringList.size() > 0) {
                            stringList.remove(0);
                            parseSVGA();
                        } else {
                            stopSVGA();
                        }
                    }
                });
            } catch (Exception e) {
            }
        } else {
            stopSVGA();
        }
    }

    /**
     * 停止动画
     */
    private void stopSVGA() {
        if (svgaImage.isAnimating() && stringList.size() == 0) {
            svgaImage.stopAnimation();
//            svgaImage.setVisibility(View.GONE);
        }
    }
}