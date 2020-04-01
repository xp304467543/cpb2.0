package com.fenghuang.caipiaobao.widget.videoplayer.extension;

import android.os.Bundle;

import com.fenghuang.caipiaobao.widget.videoplayer.receiver.IReceiverGroup;


/**
 *
 * Created by Taurus on 2018/5/27.
 *
 */
public interface DelegateReceiverEventSender {

    void sendEvent(int eventCode, Bundle bundle, IReceiverGroup.OnReceiverFilter receiverFilter);

    void sendObject(String key, Object value, IReceiverGroup.OnReceiverFilter receiverFilter);

}
