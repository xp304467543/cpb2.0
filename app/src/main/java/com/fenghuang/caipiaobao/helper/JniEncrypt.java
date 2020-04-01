package com.fenghuang.caipiaobao.helper;

/**
 * Jni加密原始方法，使用So时，需要定义一模一样的方法
 */
public class JniEncrypt {

    static {
        System.loadLibrary("encrypt-lib");
    }

    public native String getNativeKey();
}
