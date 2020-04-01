package com.fenghuang.caipiaobao.widget.videoplayer.receiver;

import android.view.View;

/**
 * Created by Taurus on 2017/3/24.
 */

public interface ICover {

    //the max cover priority value per level container.
    int LEVEL_MAX = 1 << 5;

    //level low container start value.
    int COVER_LEVEL_LOW     = 0;

    //level medium container start value.
    int COVER_LEVEL_MEDIUM  = 1 << 5;

    //level high container start value.
    int COVER_LEVEL_HIGH    = 1 << 6;

    void setCoverVisibility(int visibility);
    View getView();
    int getCoverLevel();
}
