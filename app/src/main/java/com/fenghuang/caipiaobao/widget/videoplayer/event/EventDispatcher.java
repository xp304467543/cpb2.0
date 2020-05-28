package com.fenghuang.caipiaobao.widget.videoplayer.event;

import android.os.Bundle;
import android.view.MotionEvent;

import com.example.playerlibrary.touch.OnTouchGestureListener;
import com.fenghuang.caipiaobao.widget.videoplayer.utils.DebugLog;
import com.fenghuang.caipiaobao.widget.videoplayer.player.OnTimerUpdateListener;
import com.fenghuang.caipiaobao.widget.videoplayer.receiver.IReceiverGroup;


/**
 * Created by Taurus on 2018/4/14.
 *
 * The event dispatcher of the framework is used to
 * distribute playback events, error events and receiver events.
 *
 */

public final class EventDispatcher implements IEventDispatcher{

    private IReceiverGroup mReceiverGroup;

    public EventDispatcher(IReceiverGroup receiverGroup){
        this.mReceiverGroup = receiverGroup;
    }

    /**
     * dispatch play event
     * @param eventCode
     * @param bundle
     */
    @Override
    public void dispatchPlayEvent(final int eventCode, final Bundle bundle){
        DebugLog.onPlayEventLog(eventCode, bundle);
        switch (eventCode){
            case OnPlayerEventListener.PLAYER_EVENT_ON_TIMER_UPDATE:
                mReceiverGroup.forEach(receiver -> {
                    if(receiver instanceof OnTimerUpdateListener && bundle!=null)
                        ((OnTimerUpdateListener)receiver).onTimerUpdate(
                                bundle.getInt(EventKey.INT_ARG1),
                                bundle.getInt(EventKey.INT_ARG2),
                                bundle.getInt(EventKey.INT_ARG3));
                    receiver.onPlayerEvent(eventCode, bundle);
                });
                break;
            default:
                mReceiverGroup.forEach(receiver -> receiver.onPlayerEvent(eventCode, bundle));
                break;
        }
        recycleBundle(bundle);
    }

    /**
     * dispatch error event
     * @param eventCode
     * @param bundle
     */
    @Override
    public void dispatchErrorEvent(final int eventCode, final Bundle bundle){
        DebugLog.onErrorEventLog(eventCode, bundle);
        mReceiverGroup.forEach(receiver -> receiver.onErrorEvent(eventCode, bundle));
        recycleBundle(bundle);
    }

    @Override
    public void dispatchReceiverEvent(final int eventCode, final Bundle bundle){
        dispatchReceiverEvent(eventCode, bundle, null);
    }

    /**
     * dispatch receivers event
     * @param eventCode
     * @param bundle
     * @param onReceiverFilter
     */
    @Override
    public void dispatchReceiverEvent(final int eventCode, final Bundle bundle, IReceiverGroup.OnReceiverFilter onReceiverFilter) {
        mReceiverGroup.forEach(onReceiverFilter, receiver -> receiver.onReceiverEvent(eventCode, bundle));
        recycleBundle(bundle);
    }

    /**
     * dispatch producer event
     * @param eventCode
     * @param bundle
     * @param onReceiverFilter
     */
    @Override
    public void dispatchProducerEvent(final int eventCode, final Bundle bundle, IReceiverGroup.OnReceiverFilter onReceiverFilter) {
        mReceiverGroup.forEach(onReceiverFilter, receiver -> receiver.onProducerEvent(eventCode, bundle));
        recycleBundle(bundle);
    }

    /**
     * dispatch producer data
     * @param key
     * @param data
     * @param onReceiverFilter
     */
    @Override
    public void dispatchProducerData(final String key, final Object data, IReceiverGroup.OnReceiverFilter onReceiverFilter) {
        mReceiverGroup.forEach(onReceiverFilter, receiver -> receiver.onProducerData(key, data));
    }

    //-----------------------------------dispatch gesture touch event-----------------------------------

    @Override
    public void dispatchTouchEventOnSingleTabUp(final MotionEvent event) {
        filterImplOnTouchEventListener(receiver -> ((OnTouchGestureListener)receiver).onSingleTapUp(event));
    }

    @Override
    public void dispatchTouchEventOnSingleTabConfirm(MotionEvent event) {
        filterImplOnTouchEventListener(receiver -> ((OnTouchGestureListener)receiver).onSingleConfirm(event));
    }

    @Override
    public void dispatchTouchEventOnDoubleTabUp(final MotionEvent event) {
        filterImplOnTouchEventListener(receiver -> ((OnTouchGestureListener)receiver).onDoubleTap(event));
    }

    @Override
    public void dispatchTouchEventOnDown(final MotionEvent event) {
        filterImplOnTouchEventListener(receiver -> ((OnTouchGestureListener)receiver).onDown(event));
    }

    @Override
    public void dispatchTouchEventOnScroll(final MotionEvent e1, final MotionEvent e2,
                                           final float distanceX, final float distanceY) {
        filterImplOnTouchEventListener(receiver -> ((OnTouchGestureListener)receiver).onScroll(e1, e2, distanceX, distanceY));
    }

    @Override
    public void dispatchTouchEventOnEndGesture() {
        filterImplOnTouchEventListener(receiver -> ((OnTouchGestureListener)receiver).onEndGesture());
    }

    private void filterImplOnTouchEventListener(final IReceiverGroup.OnLoopListener onLoopListener){
        mReceiverGroup.forEach(receiver -> receiver instanceof OnTouchGestureListener, onLoopListener);
    }

    private void recycleBundle(Bundle bundle){
        if(bundle!=null)
            bundle.clear();
    }

}
