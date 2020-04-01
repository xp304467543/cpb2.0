package com.fenghuang.caipiaobao.widget.videoplayer.extension;

/**
 *
 * Created by Taurus on 2018/5/27.
 *
 */
public abstract class BaseEventProducer implements EventProducer {

    private ReceiverEventSender mReceiverEventSender;

    void attachSender(ReceiverEventSender receiverEventSender){
        this.mReceiverEventSender = receiverEventSender;
    }

    @Override
    public ReceiverEventSender getSender() {
        return mReceiverEventSender;
    }

}
