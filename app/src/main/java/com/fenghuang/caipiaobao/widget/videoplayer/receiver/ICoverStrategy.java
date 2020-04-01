package com.fenghuang.caipiaobao.widget.videoplayer.receiver;

import android.view.ViewGroup;

/**
 * Created by Taurus on 2018/3/17.
 */

public interface ICoverStrategy {

    void addCover(BaseCover cover);
    void removeCover(BaseCover cover);
    void removeAllCovers();
    boolean isContainsCover(BaseCover cover);
    int getCoverCount();
    ViewGroup getContainerView();

}
