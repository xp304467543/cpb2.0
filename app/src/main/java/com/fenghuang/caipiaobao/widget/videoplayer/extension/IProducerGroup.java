package com.fenghuang.caipiaobao.widget.videoplayer.extension;

/**
 *
 * Created by Taurus on 2018/5/27.
 *
 * To manage multiple event producers
 *
 */
public interface IProducerGroup {

    void addEventProducer(BaseEventProducer eventProducer);

    boolean removeEventProducer(BaseEventProducer eventProducer);

    void destroy();

}
