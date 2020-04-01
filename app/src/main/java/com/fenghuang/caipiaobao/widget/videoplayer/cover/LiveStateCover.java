package com.fenghuang.caipiaobao.widget.videoplayer.cover;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fenghuang.caipiaobao.R;
import com.fenghuang.caipiaobao.manager.ImageManager;
import com.fenghuang.caipiaobao.widget.videoplayer.receiver.BaseCover;


public class LiveStateCover extends BaseCover {

    SimpleDraweeView mCloseIcon;
    String avatar;

    public LiveStateCover(Context context,String avatar) {
        super(context);
        this.avatar = avatar;
    }

    @Override
    public void onReceiverBind() {
        super.onReceiverBind();
        mCloseIcon = findViewById(R.id.imgAnchorAvatar);
        if (avatar!=null) ImageManager.INSTANCE.loadImg(avatar,mCloseIcon);
    }

    @Override
    public void onReceiverUnBind() {
        super.onReceiverUnBind();

    }

    @Override
    public View onCreateCoverView(Context context) {
        return View.inflate(context, R.layout.layout_state_cover, null);
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
