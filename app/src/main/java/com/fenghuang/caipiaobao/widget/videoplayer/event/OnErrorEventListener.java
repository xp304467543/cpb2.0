package com.fenghuang.caipiaobao.widget.videoplayer.event;

import android.os.Bundle;

/**
 * Created by Taurus on 2018/3/17.
 */

public interface OnErrorEventListener {

    int ERROR_EVENT_DATA_PROVIDER_ERROR = -88000;

    //A error that causes a play to terminate
    int ERROR_EVENT_COMMON = -88011;

    int ERROR_EVENT_UNKNOWN = -88012;

    int ERROR_EVENT_SERVER_DIED = -88013;

    int ERROR_EVENT_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = -88014;

    int ERROR_EVENT_IO = -88015;

    int ERROR_EVENT_MALFORMED = -88016;

    int ERROR_EVENT_UNSUPPORTED = -88017;

    int ERROR_EVENT_TIMED_OUT = -88018;

    void onErrorEvent(int eventCode, Bundle bundle);

}
