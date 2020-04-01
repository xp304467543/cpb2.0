package com.fenghuang.caipiaobao.widget.videoplayer.player;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.playerlibrary.PLog;
import com.fenghuang.caipiaobao.widget.videoplayer.event.OnPlayerEventListener;


/**
 * Created by Taurus on 2018/4/15.
 */

public class TimerCounterProxy {

    private final int MSG_CODE_COUNTER = 1;
    private int counterInterval;

    //indicator is start message.
    private boolean start;

    //indicator is in legal state,
    // eg. maybe in error state.
    private boolean isLegal;

    //proxy state, default use it.
    private boolean useProxy = true;

    private OnCounterUpdateListener onCounterUpdateListener;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_CODE_COUNTER:
                    if(!useProxy || !start || !isLegal)
                        return;
                    if(onCounterUpdateListener!=null)
                        onCounterUpdateListener.onCounter();
                    loopNext();
                    break;
            }
        }
    };

    public TimerCounterProxy(int counterIntervalMs){
        this.counterInterval = counterIntervalMs;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
        if(!useProxy){
            cancel();
            PLog.e("TimerCounterProxy", "Timer Stopped");
        }else{
            start();
            PLog.e("TimerCounterProxy", "Timer Started");
        }
    }

    public void setOnCounterUpdateListener(OnCounterUpdateListener onCounterUpdateListener) {
        this.onCounterUpdateListener = onCounterUpdateListener;
    }

    public void proxyPlayEvent(int eventCode, Bundle bundle){
        boolean needStart = false;
        boolean needCancel = false;
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_DATA_SOURCE_SET:
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_START:
            case OnPlayerEventListener.PLAYER_EVENT_ON_BUFFERING_END:
            case OnPlayerEventListener.PLAYER_EVENT_ON_PAUSE:
            case OnPlayerEventListener.PLAYER_EVENT_ON_RESUME:
            case OnPlayerEventListener.PLAYER_EVENT_ON_SEEK_COMPLETE:
                isLegal = true;
                needStart = true;
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_STOP:
            case OnPlayerEventListener.PLAYER_EVENT_ON_RESET:
            case OnPlayerEventListener.PLAYER_EVENT_ON_DESTROY:
            case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                isLegal = false;
                needCancel = true;
                break;
        }

        if(!useProxy)
            return;
        if(needStart){
            start();
        }
        if(needCancel){
            cancel();
        }

    }

    public void proxyErrorEvent(int eventCode, Bundle bundle){
        isLegal = false;
        cancel();
    }

    public void start(){
        start = true;
        removeMessage();
        mHandler.sendEmptyMessage(MSG_CODE_COUNTER);
    }

    private void loopNext(){
        removeMessage();
        mHandler.sendEmptyMessageDelayed(MSG_CODE_COUNTER, counterInterval);
    }

    public void cancel(){
        start = false;
        removeMessage();
    }

    private void removeMessage() {
        mHandler.removeMessages(MSG_CODE_COUNTER);
    }

    public interface OnCounterUpdateListener{
        void onCounter();
    }

}
