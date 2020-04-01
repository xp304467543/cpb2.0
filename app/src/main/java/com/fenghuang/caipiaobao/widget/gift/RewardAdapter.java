package com.fenghuang.caipiaobao.widget.gift;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fenghuang.caipiaobao.R;
import com.fenghuang.caipiaobao.manager.ImageManager;
import com.fenghuang.caipiaobao.widget.gift.anim.AnimUtils;
import com.fenghuang.caipiaobao.widget.gift.anim.NumAnim;
import com.fenghuang.caipiaobao.widget.gift.bean.SendGiftBean;

/**
 * @ Author  QinTian
 * @ Date  2020-02-28
 * @ Describe
 */
@SuppressLint("SetTextI18n")
public class RewardAdapter implements RewardLayout.GiftAdapter<SendGiftBean> {

    private Context mContext;

    public RewardAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View onInit(View view, SendGiftBean bean) {
        SimpleDraweeView giftImage = view.findViewById(R.id.iv_gift_img);
        SimpleDraweeView giftUserPhoto = view.findViewById(R.id.riv_gift_my_avatar);

        final TextView giftNum = view.findViewById(R.id.tv_gift_amount);
        TextView userName = view.findViewById(R.id.tv_user_name);
        TextView giftName = view.findViewById(R.id.tv_gift_name);

        // 初始化数据
        giftNum.setText("x" + bean.getTheSendGiftSize());
        bean.setTheGiftCount(bean.getTheSendGiftSize());
        ImageManager.INSTANCE.loadImg(bean.getGiftImg(), giftImage);
        ImageManager.INSTANCE.loadImg(bean.getUserPhoto(), giftUserPhoto);
        userName.setText(bean.getUserName());
        giftName.setText("送出 " + bean.getGiftSendSizeMore() + "个" + bean.getGiftName());

        return view;
    }

    @Override
    public View onUpdate(View view, SendGiftBean o, SendGiftBean sendGiftBean) {
        SimpleDraweeView giftImage = view.findViewById(R.id.iv_gift_img);
        TextView giftNum = view.findViewById(R.id.tv_gift_amount);

        int showNum = o.getTheGiftCount() + o.getTheSendGiftSize();
        // 刷新已存在的giffie界面数据
        giftNum.setText("x" + showNum);
        ImageManager.INSTANCE.loadImg(o.getGiftImg(), giftImage);
        // 数字刷新动画
        new NumAnim().start(giftNum);
        // 更新累计礼物数量
        o.setTheGiftCount(showNum);
        // 更新其它自定义字段
//              o.setUserName(t.getUserName());
        return view;
    }

    @Override
    public void onKickEnd(SendGiftBean bean) {

    }

    @Override
    public void onComboEnd(SendGiftBean bean) {

    }

    @Override
    public void addAnim(View view) {
        final TextView textView = view.findViewById(R.id.tv_gift_amount);
        ImageView img = view.findViewById(R.id.iv_gift_img);
        // 整个giftview动画
        Animation giftInAnim = AnimUtils.getInAnimation(mContext);
        // 礼物图像动画
        Animation imgInAnim = AnimUtils.getInAnimation(mContext);
        // 首次连击动画
        final NumAnim comboAnim = new NumAnim();
        imgInAnim.setStartTime(500);
        imgInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                textView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setVisibility(View.VISIBLE);
                comboAnim.start(textView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(giftInAnim);
        img.startAnimation(imgInAnim);
    }

    @Override
    public AnimationSet outAnim() {
        return AnimUtils.getOutAnimation(mContext);
    }

    @Override
    public boolean checkUnique(SendGiftBean o, SendGiftBean sendGiftBean) {
        return o.getTheGiftId() == sendGiftBean.getTheGiftId() && o.getTheUserId() == sendGiftBean.getTheUserId() &&
                o.getGiftSendSizeMore().equals(sendGiftBean.getGiftSendSizeMore());
    }

    @Override
    public SendGiftBean generateBean(SendGiftBean bean) {
        try {
            return (SendGiftBean) bean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
