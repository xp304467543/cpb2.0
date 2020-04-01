package com.fenghuang.caipiaobao.widget.videoplayer.receiver;

/**
 * the state getter for receivers, because receivers dynamic attach,
 * maybe you need get some state on attach.
 *
 * Created by Taurus on 2018/6/8.
 *
 */
public interface StateGetter {

    PlayerStateGetter getPlayerStateGetter();

}
