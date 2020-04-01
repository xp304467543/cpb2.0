package com.fenghuang.caipiaobao.widget.videoplayer.receiver;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Taurus on 2018/3/17.
 */

public interface IReceiver {

    /**
     * bind host group.
     * @param receiverGroup
     */
    void bindGroup(@NonNull IReceiverGroup receiverGroup);

    /**
     * on receiver added to ReceiverGroup.
     */
    void onReceiverBind();

    /**
     * all player event dispatch by this method.
     * @param eventCode
     * @param bundle
     */
    void onPlayerEvent(int eventCode, Bundle bundle);

    /**
     * error event.
     * @param eventCode
     * @param bundle
     */
    void onErrorEvent(int eventCode, Bundle bundle);

    /**
     * bind a state getter.
     * @param stateGetter
     */
    void bindStateGetter(StateGetter stateGetter);

    /**
     * bind the bridge of receivers communication
     * @param onReceiverEventListener
     */
    void bindReceiverEventListener(OnReceiverEventListener onReceiverEventListener);

    /**
     * receivers event.
     * @param eventCode
     * @param bundle
     */
    void onReceiverEvent(int eventCode, Bundle bundle);

    /**
     * you can call this method dispatch private event.
     *
     * @param eventCode
     * @param bundle
     *
     * @return Bundle Return value after the receiver's response, nullable.
     */
    @Nullable
    Bundle onPrivateEvent(int eventCode, Bundle bundle);

    /**
     * producer event call back this method
     * @param eventCode
     * @param bundle
     */
    void onProducerEvent(int eventCode, Bundle bundle);

    /**
     * producer data call back this method
     * @param key
     * @param data
     */
    void onProducerData(String key, Object data);

    /**
     * on receiver destroy.
     * when receiver removed, this method will be callback.
     */
    void onReceiverUnBind();

    /**
     * get the receiver key, when add receiver the key set it.
     * @return
     */
    String getKey();

}
