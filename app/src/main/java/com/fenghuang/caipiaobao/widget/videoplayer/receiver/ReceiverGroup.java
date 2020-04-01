package com.fenghuang.caipiaobao.widget.videoplayer.receiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Taurus on 2018/3/17.
 */

public final class ReceiverGroup implements IReceiverGroup{


    //receiver key value collection
    private Map<String, IReceiver> mReceivers;
    //receiver array for loop
    private List<IReceiver> mReceiverArray;
    //receiver items change listener
    private List<OnReceiverGroupChangeListener> mOnReceiverGroupChangeListeners;

    private GroupValue mGroupValue;

    public ReceiverGroup(){
        this(null);
    }

    public ReceiverGroup(GroupValue groupValue){
        mReceivers = new ConcurrentHashMap<>(16);
        mReceiverArray = Collections.synchronizedList(new ArrayList<>());
        mOnReceiverGroupChangeListeners = new CopyOnWriteArrayList<>();
        if(groupValue==null){
            mGroupValue = new GroupValue();
        }else{
            mGroupValue = groupValue;
        }
    }

    @Override
    public void addOnReceiverGroupChangeListener(
            OnReceiverGroupChangeListener onReceiverGroupChangeListener) {
        if(mOnReceiverGroupChangeListeners.contains(onReceiverGroupChangeListener))
            return;
        mOnReceiverGroupChangeListeners.add(onReceiverGroupChangeListener);
    }

    @Override
    public void removeOnReceiverGroupChangeListener(OnReceiverGroupChangeListener onReceiverGroupChangeListener) {
        mOnReceiverGroupChangeListeners.remove(onReceiverGroupChangeListener);
    }

    void callBackOnReceiverAdd(String key, IReceiver receiver){
        for(OnReceiverGroupChangeListener listener:mOnReceiverGroupChangeListeners){
            listener.onReceiverAdd(key, receiver);
        }
    }

    void callBackOnReceiverRemove(String key, IReceiver receiver){
        for(OnReceiverGroupChangeListener listener:mOnReceiverGroupChangeListeners){
            listener.onReceiverRemove(key, receiver);
        }
        receiver.onReceiverUnBind();
    }

    @Override
    public void addReceiver(String key, IReceiver receiver){
        ((BaseReceiver)receiver).setKey(key);
        receiver.bindGroup(this);
        //call back method onReceiverBind().
        receiver.onReceiverBind();
        mReceivers.put(key, receiver);
        mReceiverArray.add(receiver);
        //call back on receiver add
        callBackOnReceiverAdd(key, receiver);
    }

    @Override
    public void removeReceiver(String key) {
        //remove it from map
        IReceiver receiver = mReceivers.remove(key);
        //remove it from array
        mReceiverArray.remove(receiver);
        //call back some methods
        onReceiverRemove(key, receiver);
    }

    private void onReceiverRemove(String key, IReceiver receiver) {
        if(receiver!=null){
            //call back on receiver remove
            callBackOnReceiverRemove(key, receiver);
            //call back method onReceiverUnBind().

        }
    }

    @Override
    public void sort(Comparator<IReceiver> comparator) {
        Collections.sort(mReceiverArray, comparator);
    }

    @Override
    public void forEach(OnLoopListener onLoopListener) {
        forEach(null, onLoopListener);
    }

    @Override
    public void forEach(OnReceiverFilter filter, OnLoopListener onLoopListener) {
            for (int i = 0 ; i <mReceiverArray.size();i++){
                if(filter==null || filter.filter(mReceiverArray.get(i))){
                    onLoopListener.onEach(mReceiverArray.get(i));
                }
            }

    }

    @Override
    public <T extends IReceiver> T getReceiver(String key) {
        if(mReceivers!=null)
            return (T) mReceivers.get(key);
        return null;
    }

    @Override
    public GroupValue getGroupValue() {
        return mGroupValue;
    }

    @Override
    public void clearReceivers(){
        for(IReceiver receiver:mReceiverArray){
            onReceiverRemove(receiver.getKey(), receiver);
        }
        mReceiverArray.clear();
        mReceivers.clear();
    }
}
