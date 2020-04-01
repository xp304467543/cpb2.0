package com.fenghuang.caipiaobao.widget.videoplayer.receiver;


import java.util.Comparator;

/**
 * Created by Taurus on 2018/4/14.
 *
 * Used to manage receivers
 *
 */

public interface IReceiverGroup {

    /**
     * add a onReceiverGroupChangeListener listen Receiver item change.
     * @param onReceiverGroupChangeListener
     */
    void addOnReceiverGroupChangeListener(OnReceiverGroupChangeListener onReceiverGroupChangeListener);

    /**
     * When you don't need onReceiverGroupChangeListener to remove it
     * @param onReceiverGroupChangeListener
     */
    void removeOnReceiverGroupChangeListener(OnReceiverGroupChangeListener onReceiverGroupChangeListener);

    /**
     * add a receiver, you need put a unique key for this receiver.
     * @param key
     * @param receiver
     */
    void addReceiver(String key, IReceiver receiver);

    /**
     * remove a receiver by key.
     * @param key
     */
    void removeReceiver(String key);

    /**
     * sort group data
     * @param comparator
     */
    void sort(Comparator<IReceiver> comparator);

    /**
     * loop all receivers
     * @param onLoopListener
     */
    void forEach(OnLoopListener onLoopListener);

    /**
     * loop all receivers by a receiver filter.
     * @param filter
     * @param onLoopListener
     */
    void forEach(OnReceiverFilter filter, OnLoopListener onLoopListener);

    /**
     * get receiver by key.
     * @param key
     * @param <T>
     * @return
     */
    <T extends IReceiver> T getReceiver(String key);

    /**
     * get the ReceiverGroup group value.
     * @return
     */
    GroupValue getGroupValue();

    /**
     * clean receivers.
     */
    void clearReceivers();

    interface OnReceiverGroupChangeListener{
        void onReceiverAdd(String key, IReceiver receiver);
        void onReceiverRemove(String key, IReceiver receiver);
    }

    interface OnLoopListener{
        void onEach(IReceiver receiver);
    }

    interface OnReceiverFilter{
        boolean filter(IReceiver receiver);
    }

    interface OnGroupValueUpdateListener{
        String[] filterKeys();
        void onValueUpdate(String key, Object value);
    }

}
