package com.fenghuang.caipiaobao.widget.videoplayer.assist;

import android.os.Bundle;

/**
 * Created by Taurus on 2018/5/21.
 * @param <T>
 */
public abstract class BaseEventAssistHandler<T> implements OnEventAssistHandler<T> {

    @Override
    public void onAssistHandle(T assist, int eventCode, Bundle bundle) {
        switch (eventCode){
            case InterEvent.CODE_REQUEST_PAUSE:
                requestPause(assist, bundle);
                break;
            case InterEvent.CODE_REQUEST_RESUME:
                requestResume(assist, bundle);
                break;
            case InterEvent.CODE_REQUEST_SEEK:
                requestSeek(assist, bundle);
                break;
            case InterEvent.CODE_REQUEST_STOP:
                requestStop(assist, bundle);
                break;
            case InterEvent.CODE_REQUEST_RESET:
                requestReset(assist, bundle);
                break;
            case InterEvent.CODE_REQUEST_RETRY:
                requestRetry(assist, bundle);
                break;
            case InterEvent.CODE_REQUEST_REPLAY:
                requestReplay(assist, bundle);
                break;
            case InterEvent.CODE_REQUEST_PLAY_DATA_SOURCE:
                requestPlayDataSource(assist, bundle);
                break;
        }
    }

}
