package com.fenghuang.caipiaobao.widget.videoplayer.config;

import android.app.Application;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.VcPlayerLog;

public class PlayerLibrary {

    public static boolean ignoreMobile;

    public static void init(Application application){

        AppContextAttach.attach(application);

        //查看log
        VcPlayerLog.enableLog();

        //初始化播放器
        AliVcMediaPlayer.init(application);
    }

}
