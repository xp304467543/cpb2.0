package com.fenghuang.caipiaobao.widget.videoplayer.extension;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * Created by Taurus on 2018/5/27.
 *
 */
public final class ProducerGroup implements IProducerGroup {

    private ReceiverEventSender mEventSender;

    private List<BaseEventProducer> mEventProducers;

    public ProducerGroup(ReceiverEventSender eventSender){
        this.mEventSender = eventSender;
        mEventProducers = new CopyOnWriteArrayList<>();
    }

    @Override
    public void addEventProducer(BaseEventProducer eventProducer) {
        if(!mEventProducers.contains(eventProducer)){
            eventProducer.attachSender(mEventSender);
            mEventProducers.add(eventProducer);
            eventProducer.onAdded();
        }
    }

    @Override
    public boolean removeEventProducer(BaseEventProducer eventProducer) {
        boolean remove = mEventProducers.remove(eventProducer);
        if(eventProducer!=null){
            eventProducer.onRemoved();
            eventProducer.attachSender(null);
        }
        return remove;
    }

    @Override
    public void destroy() {
        for(BaseEventProducer eventProducer : mEventProducers){
            eventProducer.onRemoved();
            eventProducer.destroy();
            eventProducer.attachSender(null);
        }
        mEventProducers.clear();
    }
}
