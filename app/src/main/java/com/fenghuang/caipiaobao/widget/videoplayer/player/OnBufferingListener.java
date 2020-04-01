package com.fenghuang.caipiaobao.widget.videoplayer.player;

import android.os.Bundle;

/**
 *
 * on decoder buffering update call back.
 *
 * Created by Taurus on 2018/6/14.
 */
public interface OnBufferingListener {

    void onBufferingUpdate(int bufferPercentage, Bundle extra);

}
