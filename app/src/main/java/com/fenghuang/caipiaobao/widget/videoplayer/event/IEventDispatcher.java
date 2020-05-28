package com.fenghuang.caipiaobao.widget.videoplayer.event;

import android.os.Bundle;
import android.view.MotionEvent;

import com.fenghuang.caipiaobao.widget.videoplayer.receiver.IReceiverGroup;


/**
 * Created by Taurus on 2018/4/15.
 */

public interface IEventDispatcher {
    void dispatchPlayEvent(int eventCode, Bundle bundle);
    void dispatchErrorEvent(int eventCode, Bundle bundle);
    void dispatchReceiverEvent(int eventCode, Bundle bundle);
    void dispatchReceiverEvent(int eventCode, Bundle bundle, IReceiverGroup.OnReceiverFilter onReceiverFilter);
    void dispatchProducerEvent(int eventCode, Bundle bundle, IReceiverGroup.OnReceiverFilter onReceiverFilter);
    void dispatchProducerData(String key, Object data, IReceiverGroup.OnReceiverFilter onReceiverFilter);


    void dispatchTouchEventOnSingleTabUp(MotionEvent event);
    void dispatchTouchEventOnSingleTabConfirm(MotionEvent event);
    void dispatchTouchEventOnDoubleTabUp(MotionEvent event);
    void dispatchTouchEventOnDown(MotionEvent event);
    void dispatchTouchEventOnScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
    void dispatchTouchEventOnEndGesture();
}
