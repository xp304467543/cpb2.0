package com.fenghuang.caipiaobao.widget.videoplayer.player;

import android.os.Bundle;

import com.fenghuang.caipiaobao.widget.videoplayer.event.BundlePool;
import com.fenghuang.caipiaobao.widget.videoplayer.event.EventKey;
import com.fenghuang.caipiaobao.widget.videoplayer.event.OnErrorEventListener;
import com.fenghuang.caipiaobao.widget.videoplayer.event.OnPlayerEventListener;


/**
 * Created by Taurus on 2018/3/17.
 *
 * The base class of the decoding scheme,
 * the decoder you access needs to inherit from this class.
 *
 */

public abstract class BaseInternalPlayer implements IPlayer {

    private int mCurrentState = STATE_IDLE;

    private OnPlayerEventListener mOnPlayerEventListener;
    private OnErrorEventListener mOnErrorEventListener;
    private OnBufferingListener mOnBufferingListener;

    private int mBufferPercentage;

    @Override
    public void option(int code, Bundle bundle) {
        //not handle
    }

    @Override
    public final void setOnBufferingListener(OnBufferingListener onBufferingListener) {
        this.mOnBufferingListener = onBufferingListener;
    }

    @Override
    public final void setOnPlayerEventListener(OnPlayerEventListener onPlayerEventListener) {
        this.mOnPlayerEventListener = onPlayerEventListener;
    }

    @Override
    public final void setOnErrorEventListener(OnErrorEventListener onErrorEventListener) {
        this.mOnErrorEventListener = onErrorEventListener;
    }

    protected final void submitPlayerEvent(int eventCode, Bundle bundle){
        if(mOnPlayerEventListener!=null)
            mOnPlayerEventListener.onPlayerEvent(eventCode, bundle);
    }

    protected final void submitErrorEvent(int eventCode, Bundle bundle){
        if(mOnErrorEventListener!=null)
            mOnErrorEventListener.onErrorEvent(eventCode, bundle);
    }

    protected final void submitBufferingUpdate(int bufferPercentage, Bundle extra){
        mBufferPercentage = bufferPercentage;
        if(mOnBufferingListener!=null)
            mOnBufferingListener.onBufferingUpdate(bufferPercentage, extra);
    }

    protected final void updateStatus(int status){
        this.mCurrentState = status;
        Bundle bundle = BundlePool.obtain();
        bundle.putInt(EventKey.INT_DATA, status);
        submitPlayerEvent(OnPlayerEventListener.PLAYER_EVENT_ON_STATUS_CHANGE, bundle);
    }

    @Override
    public int getBufferPercentage() {
        return mBufferPercentage;
    }

    @Override
    public final int getState() {
        return mCurrentState;
    }
}
