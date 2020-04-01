package com.fenghuang.caipiaobao.widget.videoplayer.extension;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.playerlibrary.PLog;
import com.fenghuang.caipiaobao.widget.videoplayer.utils.NetworkUtils;
import com.fenghuang.caipiaobao.widget.videoplayer.assist.InterKey;

import java.lang.ref.WeakReference;

/**
 *
 * Created by Taurus on 2018/5/27.
 *
 * Network change event producer, used to send network status change events.
 *
 */
public class NetworkEventProducer extends BaseEventProducer {

    private final String TAG = "NetworkEventProducer";

    private static final int MSG_CODE_NETWORK_CHANGE = 100;

    private Context mAppContext;

    private NetChangeBroadcastReceiver mBroadcastReceiver;

    private int mState;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_CODE_NETWORK_CHANGE:
                    int state = (int) msg.obj;
                    if(mState==state)
                        return;
                    mState = state;
                    ReceiverEventSender sender = getSender();
                    if(sender!=null){
                        sender.sendInt(InterKey.KEY_NETWORK_STATE, mState);
                        PLog.d(TAG,"onNetworkChange : " + mState);
                    }
                    break;
            }
        }
    };

    public NetworkEventProducer(Context context){
        this.mAppContext = context.getApplicationContext();
    }

    private void registerNetChangeReceiver(){
        unregisterNetChangeReceiver();
        if(mAppContext!=null){
            mBroadcastReceiver = new NetChangeBroadcastReceiver(mAppContext, mHandler);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mAppContext.registerReceiver(mBroadcastReceiver, intentFilter);
        }
    }

    private void unregisterNetChangeReceiver(){
        try {
            if(mAppContext!=null && mBroadcastReceiver !=null){
                mAppContext.unregisterReceiver(mBroadcastReceiver);
                mBroadcastReceiver = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAdded() {
        mState = NetworkUtils.getNetworkState(mAppContext);
        registerNetChangeReceiver();
    }

    @Override
    public void onRemoved() {
        destroy();
    }

    public void destroy(){
        if(mBroadcastReceiver !=null)
            mBroadcastReceiver.destroy();
        unregisterNetChangeReceiver();
        mHandler.removeMessages(MSG_CODE_NETWORK_CHANGE);
    }

    public static class NetChangeBroadcastReceiver extends BroadcastReceiver {

        private Handler handler;
        private WeakReference<Context> mContextRefer;

        public NetChangeBroadcastReceiver(Context context, Handler handler){
            mContextRefer = new WeakReference<>(context);
            this.handler = handler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
                handler.removeCallbacks(mDelayRunnable);
                handler.postDelayed(mDelayRunnable, 1000);
            }
        }

        private Runnable mDelayRunnable = new Runnable() {
            @Override
            public void run() {
                if(mContextRefer!=null && mContextRefer.get()!=null){
                    int networkState = NetworkUtils.getNetworkState(mContextRefer.get());
                    Message message = Message.obtain();
                    message.what = MSG_CODE_NETWORK_CHANGE;
                    message.obj = networkState;
                    handler.sendMessage(message);
                }
            }
        };

        public void destroy(){
            handler.removeCallbacks(mDelayRunnable);
        }
    }

}
