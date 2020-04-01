package com.fenghuang.caipiaobao.widget.gift.bean;

/**
 * 礼物标识
 *
 * @author zhangyf
 * @date 2018/7/19 0019.
 */
public interface GiftIdentify extends Comparable<GiftIdentify> {

    /**
     * 礼物Id
     * @return
     */
    int getTheGiftId();

    void setTheGiftId(int gid);

    /**
     * 用户Id
     * @return
     */
    int getTheUserId();

    void setTheUserId(int uid);

    /**
     * 礼物累计数
     * @return
     */
    int getTheGiftCount();

    void setTheGiftCount(int count);

    /**
     * 单次礼物赠送数目
     * @return
     */
    int getTheSendGiftSize();

    void setTheSendGiftSize(int size);

    /**
     * 礼物停留时间
     * @return
     */
    long getTheGiftStay();

    void setTheGiftStay(long stay);

    /**
     * 礼物最新一次刷新时间戳
     * @return
     */
    long getTheLatestRefreshTime();

    void setTheLatestRefreshTime(long time);

    /**
     * 礼物索引
     * @return
     */
    int getTheCurrentIndex();

    void setTheCurrentIndex(int index);


    /**
     * 礼物价格
     * @return
     */
    String getGiftPrise();

    void setGiftPris(String prise);


    /**
     * 是否是发送的
     * @return
     */
    Boolean getIsMeSend();

    void setIsMeSend(Boolean isMe);


    /**
     * 礼物名字
     * @return
     */
    String getGiftName();

    /**
     * 礼物url
     * @return
     */
    String getGiftUrl();
}
