package com.fenghuang.caipiaobao.widget.videoplayer.receiver;



/**
 *
 * player state getter for Receivers.
 *
 * Created by Taurus on 2018/6/8.
 *
 */
public interface PlayerStateGetter {

    /**
     * get player state code.
     *
     * See also
     * {@link IPlayer#STATE_END}
     * {@link IPlayer#STATE_ERROR}
     * {@link IPlayer#STATE_IDLE}
     * {@link IPlayer#STATE_INITIALIZED}
     * {@link IPlayer#STATE_PREPARED}
     * {@link IPlayer#STATE_STARTED}
     * {@link IPlayer#STATE_PAUSED}
     * {@link IPlayer#STATE_STOPPED}
     * {@link IPlayer#STATE_PLAYBACK_COMPLETE}
     *
     * @return state
     */
    int getState();

    /**
     * get player current play progress.
     * @return
     */
    int getCurrentPosition();

    /**
     * get video duration
     * @return
     */
    int getDuration();

    /**
     * get player buffering percentage.
     * @return
     */
    int getBufferPercentage();

    /**
     * the player is in buffering.
     * @return
     */
    boolean isBuffering();

}
