package com.fenghuang.caipiaobao.widget.videoplayer.assist;

import android.content.Context;

import com.fenghuang.caipiaobao.widget.videoplayer.cover.CompleteCover;
import com.fenghuang.caipiaobao.widget.videoplayer.cover.ControllerLiveCover;
import com.fenghuang.caipiaobao.widget.videoplayer.cover.ErrorCover;
import com.fenghuang.caipiaobao.widget.videoplayer.cover.LoadingCover;
import com.fenghuang.caipiaobao.widget.videoplayer.receiver.GroupValue;
import com.fenghuang.caipiaobao.widget.videoplayer.receiver.ReceiverGroup;

import static com.fenghuang.caipiaobao.widget.videoplayer.assist.DataInter.ReceiverKey.KEY_COMPLETE_COVER;
import static com.fenghuang.caipiaobao.widget.videoplayer.assist.DataInter.ReceiverKey.KEY_CONTROLLER_live_COVER;
import static com.fenghuang.caipiaobao.widget.videoplayer.assist.DataInter.ReceiverKey.KEY_ERROR_COVER;
import static com.fenghuang.caipiaobao.widget.videoplayer.assist.DataInter.ReceiverKey.KEY_LOADING_COVER;


/**
 * Created by Taurus on 2018/4/18.
 */

public class ReceiverGroupManager {

    private static ReceiverGroupManager i;

    private ControllerLiveCover controllerLiveCover;

    private ReceiverGroupManager() {
    }

    public static ReceiverGroupManager get() {
        if (null == i) {
            synchronized (ReceiverGroupManager.class) {
                if (null == i) {
                    i = new ReceiverGroupManager();
                }
            }
        }
        return i;
    }

//    public ReceiverGroup getLittleReceiverGroup(Context context){
//        return getLiteReceiverGroup(context, null);
//    }
//
//    public ReceiverGroup getLittleReceiverGroup(Context context, GroupValue groupValue){
//        ReceiverGroup receiverGroup = new ReceiverGroup(groupValue);
//        receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(context));
//        receiverGroup.addReceiver(KEY_COMPLETE_COVER, new CompleteCover(context));
//        receiverGroup.addReceiver(KEY_ERROR_COVER, new ErrorCover(context));
//        return receiverGroup;
//    }
//
//    public ReceiverGroup getLiteReceiverGroup(Context context){
//        return getLiteReceiverGroup(context, null);
//    }
//
//    public ReceiverGroup getLiteReceiverGroup(Context context, GroupValue groupValue){
//        ReceiverGroup receiverGroup = new ReceiverGroup(groupValue);
//        receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(context));
//        receiverGroup.addReceiver(KEY_CONTROLLER_COVER, new ControllerCover(context,false));
//        receiverGroup.addReceiver(KEY_COMPLETE_COVER, new CompleteCover(context));
//        receiverGroup.addReceiver(KEY_ERROR_COVER, new ErrorCover(context));
//        return receiverGroup;
//    }


    public ReceiverGroup getLiveReceiverGroup(Context context, String liveState, String avatar, String nickName, String anchorId) {
        return getLiveReceiverGroup(context, null, liveState, avatar, nickName, anchorId);
    }


    public ReceiverGroup getLiveReceiverGroup(Context context, GroupValue groupValue, String liveState, String avatar, String nickName, String anchorId) {
        ReceiverGroup receiverGroup = new ReceiverGroup(groupValue);
        controllerLiveCover = new ControllerLiveCover(context, liveState, avatar, nickName, anchorId);
        receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(context));
        receiverGroup.addReceiver(KEY_CONTROLLER_live_COVER, controllerLiveCover);
        receiverGroup.addReceiver(KEY_COMPLETE_COVER, new CompleteCover(context));
        receiverGroup.addReceiver(KEY_ERROR_COVER, new ErrorCover(context));
        return receiverGroup;
    }




    public ControllerLiveCover getControllerLiveCover() {
        if (controllerLiveCover != null) {
            return controllerLiveCover;
        } else return null;
    }


//    public ReceiverGroup getReceiverGroup(Context context){
//        return getReceiverGroup(context, null);
//    }
//
//    public ReceiverGroup getReceiverGroup(Context context, GroupValue groupValue){
//        ReceiverGroup receiverGroup = new ReceiverGroup(groupValue);
//        receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(context));
//        receiverGroup.addReceiver(KEY_CONTROLLER_COVER, new ControllerCover(context,true));
//        receiverGroup.addReceiver(KEY_GESTURE_COVER, new GestureCover(context));
//        receiverGroup.addReceiver(KEY_COMPLETE_COVER, new CompleteCover(context));
//        receiverGroup.addReceiver(KEY_ERROR_COVER, new ErrorCover(context));
//        return receiverGroup;
//    }

}
