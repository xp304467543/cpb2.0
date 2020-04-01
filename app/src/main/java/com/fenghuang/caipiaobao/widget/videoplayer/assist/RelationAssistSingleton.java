package com.fenghuang.caipiaobao.widget.videoplayer.assist;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * @ Author  QinTian
 * @ Date  2020-01-21
 * @ Describe
 */
public class RelationAssistSingleton {
    @SuppressLint("StaticFieldLeak")
    private static RelationAssist mAssist;

    private RelationAssistSingleton() {
    }

    public static RelationAssist getAssist(Context context) {
        if (mAssist == null) {
            synchronized (RelationAssistSingleton.class) {
                if (mAssist == null) {
                    mAssist = new RelationAssist(context);
                }
            }
        }
        return mAssist;
    }

    public static void releaseAssist() {
        if (mAssist != null) {
            mAssist.destroy();
            mAssist = null;
        }
    }
}
