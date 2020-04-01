package com.fenghuang.caipiaobao.widget.videoplayer.player;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.fenghuang.caipiaobao.widget.videoplayer.entity.DataSource;
import com.fenghuang.caipiaobao.widget.videoplayer.event.OnErrorEventListener;
import com.fenghuang.caipiaobao.widget.videoplayer.event.OnPlayerEventListener;


/**
 * Created by Taurus on 2018/3/17.
 */

public interface IPlayer {

    int STATE_END = -2;
    int STATE_ERROR = -1;
    int STATE_IDLE = 0;
    int STATE_INITIALIZED = 1;
    int STATE_PREPARED = 2;
    int STATE_STARTED = 3;
    int STATE_PAUSED = 4;
    int STATE_STOPPED = 5;
    int STATE_PLAYBACK_COMPLETE = 6;

    /**
     * with this method, you can send some params for player init or switch some setting.
     * such as some configuration option (use mediacodec or timeout or reconnect and so on) for decoder init.
     * @param code the code value custom yourself.
     * @param bundle deliver some data if you need.
     */
    void option(int code, Bundle bundle);

    void setDataSource(DataSource dataSource);
    void setDisplay(SurfaceHolder surfaceHolder);
    void setSurface(Surface surface);
    void setVolume(float left, float right);
    void setSpeed(float speed);

    void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener);
    void setOnErrorEventListener(OnErrorEventListener onErrorEventListener);

    void setOnBufferingListener(OnBufferingListener onBufferingListener);

    int getBufferPercentage();

    boolean isPlaying();
    int getCurrentPosition();
    int getDuration();
    int getAudioSessionId();
    int getVideoWidth();
    int getVideoHeight();
    int getState();

    void start();
    void start(int msc);
    void pause();
    void resume();
    void seekTo(int msc);
    void stop();
    void reset();
    void destroy();

}
