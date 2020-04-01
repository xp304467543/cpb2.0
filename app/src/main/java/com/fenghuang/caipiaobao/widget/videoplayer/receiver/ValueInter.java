package com.fenghuang.caipiaobao.widget.videoplayer.receiver;

interface ValueInter {

    void putBoolean(String key, boolean value);
    void putBoolean(String key, boolean value, boolean notifyUpdate);

    void putInt(String key, int value);
    void putInt(String key, int value, boolean notifyUpdate);

    void putString(String key, String value);
    void putString(String key, String value, boolean notifyUpdate);

    void putFloat(String key, float value);
    void putFloat(String key, float value, boolean notifyUpdate);

    void putLong(String key, long value);
    void putLong(String key, long value, boolean notifyUpdate);

    void putDouble(String key, double value);
    void putDouble(String key, double value, boolean notifyUpdate);

    void putObject(String key, Object value);
    void putObject(String key, Object value, boolean notifyUpdate);



    <T> T get(String key);

    boolean getBoolean(String key);
    boolean getBoolean(String key, boolean defaultValue);

    int getInt(String key);
    int getInt(String key, int defaultValue);

    String getString(String key);

    float getFloat(String key);
    float getFloat(String key, float defaultValue);

    long getLong(String key);
    long getLong(String key, long defaultValue);

    double getDouble(String key);
    double getDouble(String key, double defaultValue);

}
