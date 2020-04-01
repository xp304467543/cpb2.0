package com.fenghuang.caipiaobao.widget.videoplayer.extension;

/**
 *
 * Created by Taurus on 2018/5/27.
 *
 * The producer of the event. It is usually added by the users themselves,
 * such as the system's power change events or network change events.
 * The framework adds the network change event producer by default.
 *
 */
public interface EventProducer {

    void onAdded();

    void onRemoved();

    ReceiverEventSender getSender();

    void destroy();

}
