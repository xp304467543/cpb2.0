package com.example.playerlibrary;

import android.util.Log;

/**
 * Created by Taurus on 2018/3/17.
 */

public class PLog {

    public static boolean LOG_OPEN = true;

    public static void d(String tag, String message){
        if(!LOG_OPEN)
            return;
        Log.d(tag,message);
    }

    public static void w(String tag, String message){
        if(!LOG_OPEN)
            return;
        Log.w(tag,message);
    }

    public static void e(String tag, String message){
        if(!LOG_OPEN)
            return;
        Log.e(tag,message);
    }

}
