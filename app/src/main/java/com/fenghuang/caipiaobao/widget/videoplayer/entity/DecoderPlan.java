package com.fenghuang.caipiaobao.widget.videoplayer.entity;

/**
 * Created by Taurus on 2018/3/17.
 *
 * The entity class of the decoder type.
 *
 */

public class DecoderPlan {

    /**
     * Required field
     * It must be guaranteed that the number is unique.
     */
    private int idNumber;

    /**
     * Required field
     * it's decoder class reference path.
     */
    private String classPath;

    private String tag;
    private String desc;

    public DecoderPlan(int idNumber, String classPath) {
        this(idNumber, classPath, classPath);
    }

    public DecoderPlan(int idNumber, String classPath, String desc) {
        this.idNumber = idNumber;
        this.classPath = classPath;
        this.desc = desc;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "id = " + idNumber +
                ", classPath = " + classPath +
                ", desc = " + desc;
    }
}
