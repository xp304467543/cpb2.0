package com.fenghuang.caipiaobao.widget.videoplayer.assist;

import android.view.ViewGroup;

import com.fenghuang.caipiaobao.widget.videoplayer.entity.DataSource;
import com.fenghuang.caipiaobao.widget.videoplayer.event.OnErrorEventListener;
import com.fenghuang.caipiaobao.widget.videoplayer.event.OnPlayerEventListener;
import com.fenghuang.caipiaobao.widget.videoplayer.provider.IDataProvider;
import com.fenghuang.caipiaobao.widget.videoplayer.receiver.IReceiverGroup;
import com.fenghuang.caipiaobao.widget.videoplayer.receiver.OnReceiverEventListener;


/**
 *
 * Created by Taurus on 2018/5/21.
 *
 * The Association for auxiliary view containers and players
 *
 */
public interface AssistPlay {

    void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener);
    void setOnErrorEventListener(OnErrorEventListener onErrorEventListener);
    void setOnReceiverEventListener(OnReceiverEventListener onReceiverEventListener);

    void setOnProviderListener(IDataProvider.OnProviderListener onProviderListener);
    void setDataProvider(IDataProvider dataProvider);
    boolean switchDecoder(int decoderPlanId);

    void setVolume(float left, float right);
    void setSpeed(float speed);

    void setReceiverGroup(IReceiverGroup receiverGroup);

    void attachContainer(ViewGroup userContainer);

    void setDataSource(DataSource dataSource);

    void play();
    void play(boolean updateRender);

    boolean isInPlaybackState();
    boolean isPlaying();
    int getCurrentPosition();
    int getDuration();
    int getAudioSessionId();
    int getBufferPercentage();
    int getState();

    void rePlay(int msc);

    void pause();
    void resume();
    void seekTo(int msc);
    void stop();
    void reset();
    void destroy();

}
