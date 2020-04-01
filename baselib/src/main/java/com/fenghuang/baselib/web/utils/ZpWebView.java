package com.fenghuang.baselib.web.utils;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.fenghuang.baselib.utils.ViewUtils;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;


/**
 * @ Author  QinTian
 * @ Date  2019/9/17- 17:19
 * @ Describe
 */
public class ZpWebView extends WebView {

    private ZpWebChromeClient webChromeClient;
    private ProgressView progressView;//进度条
    public ZpWebView(Context context) {
        super(context);
        initWebView();
    }

    public ZpWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    public ZpWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView();
    }

    private void initWebView() {
        //初始化进度条
        progressView = new ProgressView(getContext());
        progressView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtils.INSTANCE.dp2px(2)));
        progressView.setProgress(10);
        //把进度条加到Webview中
        addView(progressView);
        webChromeClient = new ZpWebChromeClient(progressView);
        setWebChromeClient(webChromeClient);
        WebSettings webviewSettings = getSettings();
        // 不支持缩放
        webviewSettings.setSupportZoom(false);
        // 自适应屏幕大小
        webviewSettings.setUseWideViewPort(true);
        webviewSettings.setLoadWithOverviewMode(true);
        String cacheDirPath = getContext().getFilesDir().getAbsolutePath() + "cache/";
        webviewSettings.setAppCachePath(cacheDirPath);
        webviewSettings.setAppCacheEnabled(true);
        webviewSettings.setDomStorageEnabled(true);
        webviewSettings.setAllowFileAccess(true);
        webviewSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        webviewSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webviewSettings.setMixedContentMode(WebSettings.LOAD_NORMAL);
        }    //当加载Html里有https和http混合的时候，5.0以上的手机需要开启混合模式才可以正常加载图片如当html是https的连接但连接里的图片是http的，这时候5.0以上的手机会加载不出来http图片，需要开启此设置才能正常显示图片。
        webviewSettings.setJavaScriptEnabled(true);  //可以夹杂javaScript
        webviewSettings.setJavaScriptCanOpenWindowsAutomatically(true); //设置允许js弹出alert对话框。
        webviewSettings.setDefaultTextEncodingName("utf-8");//文本编码
        webviewSettings.setPluginState(WebSettings.PluginState.ON); //播放视频
        webviewSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webviewSettings.setGeolocationEnabled(true); //设置可以定位

    }


    public void setOpenFileChooserCallBack(ZpWebChromeClient.OpenFileChooserCallBack callBack) {
        webChromeClient.setOpenFileChooserCallBack(callBack);
    }

}
