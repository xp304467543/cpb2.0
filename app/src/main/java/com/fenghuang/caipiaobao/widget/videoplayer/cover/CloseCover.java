package com.fenghuang.caipiaobao.widget.videoplayer.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.playerlibrary.R;
import com.fenghuang.caipiaobao.widget.videoplayer.assist.DataInter;
import com.fenghuang.caipiaobao.widget.videoplayer.receiver.BaseCover;


public class CloseCover extends BaseCover {

    ImageView mCloseIcon,iv_rotate;

    private View view;

    public CloseCover(Context context) {
        super(context);
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        mCloseIcon = findViewById(R.id.iv_close);
        iv_rotate = findViewById(R.id.iv_rotate);
        mCloseIcon.setOnClickListener(v -> notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_CLOSE, null));
        iv_rotate.setOnClickListener(v -> notifyReceiverEvent(DataInter.Event.EVENT_CODE_REQUEST_ROTATE, null));
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();

    }

    @Override
    public View onCreateCoverView(Context context) {
        view =View .inflate(context, R.layout.layout_close_cover, null);
        return view;
    }

    public void rotate(int rotate){
        view.setRotation(rotate);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onErrorEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public void onReceiverEvent(int eventCode, Bundle bundle) {

    }

    @Override
    public int getCoverLevel() {
        return levelMedium(10);
    }
}
