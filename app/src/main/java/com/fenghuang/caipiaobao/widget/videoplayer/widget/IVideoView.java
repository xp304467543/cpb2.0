package com.fenghuang.caipiaobao.widget.videoplayer.widget;


import com.fenghuang.caipiaobao.widget.videoplayer.entity.DataSource;
import com.fenghuang.caipiaobao.widget.videoplayer.render.AspectRatio;
import com.fenghuang.caipiaobao.widget.videoplayer.render.IRender;

/**
 * Created by Taurus on 2018/3/17.
 */

public interface IVideoView {

    void setDataSource(DataSource dataSource);

    void setRenderType(int renderType);
    void setAspectRatio(AspectRatio aspectRatio);
    boolean switchDecoder(int decoderPlanId);

    void setVolume(float left, float right);
    void setSpeed(float speed);

    IRender getRender();

    boolean isInPlaybackState();
    boolean isPlaying();
    int getCurrentPosition();
    int getDuration();
    int getAudioSessionId();
    int getBufferPercentage();
    int getState();

    void start();
    void start(int msc);
    void pause();
    void resume();
    void seekTo(int msc);
    void stop();
    void stopPlayback();

}
