package com.fenghuang.caipiaobao.widget.gift.bean;

/**
 * Created by zhangyf on 2017/3/30.
 */

public class SendGiftBean extends BaseGiftBean {

    /**
     * 用户id
     */
    private int userId;
    /**
     * 礼物id
     */
    private int giftId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userPhoto;
    /**
     * 礼物名称
     */
    private String giftName;
    /**
     * 礼物本地图片也可以定义为远程url
     */
    private String giftImg;
    /**
     * 礼物持续时间
     */
    private long giftStayTime;

    /**
     * 单次礼物数目
     */
    private int giftSendSize = 1;

    /**
     * 是否自己发的
     */
    private Boolean isMe = false;

    /**
     * 礼物价格
     */
    private String giftPrise;

    /**
     * 单次多个礼物数目
     */
    private String giftSendSizeMore = "1";

    public SendGiftBean() {
    }

    public SendGiftBean(int userId, int giftId, String userName, String giftName, String giftImg, String userPhoto, String giftSendSizeMore, long time) {
        this.userId = userId;
        this.giftId = giftId;
        this.userName = userName;
        this.giftName = giftName;
        this.giftImg = giftImg;
        this.giftStayTime = time;
        this.userPhoto = userPhoto;
        this.giftSendSizeMore = giftSendSizeMore;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getGiftName() {
        return giftName;
    }

    @Override
    public String getGiftUrl() {
        return giftImg;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftImg() {
        return giftImg;
    }

    public void setGiftImg(String giftImg) {
        this.giftImg = giftImg;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public Boolean getMe() {
        return isMe;
    }

    public void setMe(Boolean me) {
        isMe = me;
    }

    public String getGiftPrise() {
        return giftPrise;
    }

    @Override
    public void setGiftPris(String prise) {
        this.giftPrise = prise;
    }

    @Override
    public Boolean getIsMeSend() {
        return isMe;
    }

    @Override
    public void setIsMeSend(Boolean isMe) {
        this.isMe = isMe;
    }

    public void setGiftPrise(String giftPrise) {
        this.giftPrise = giftPrise;
    }

    @Override
    public int getTheGiftId() {
        return giftId;
    }

    @Override
    public void setTheGiftId(int gid) {
        this.giftId = gid;
    }

    @Override
    public int getTheUserId() {
        return userId;
    }

    @Override
    public void setTheUserId(int uid) {
        this.userId = uid;
    }

    @Override
    public int getTheSendGiftSize() {
        return giftSendSize;
    }

    @Override
    public void setTheSendGiftSize(int size) {
        giftSendSize = size;
    }

    @Override
    public long getTheGiftStay() {
        return giftStayTime;
    }

    @Override
    public void setTheGiftStay(long stay) {
        giftStayTime = stay;
    }


    public String getGiftSendSizeMore() {
        return giftSendSizeMore;
    }

    public void setGiftSendSizeMore(String giftSendSizeMore) {
        this.giftSendSizeMore = giftSendSizeMore;
    }

    @Override
    public String toString() {
        return "SendGiftBean{" +
                "userId=" + userId +
                ", giftId=" + giftId +
                ", userName='" + userName + '\'' +
                ", userPhoto='" + userPhoto + '\'' +
                ", giftName='" + giftName + '\'' +
                ", giftImg='" + giftImg + '\'' +
                ", giftStayTime=" + giftStayTime +
                ", giftSendSize=" + giftSendSize +
                '}';
    }
}
