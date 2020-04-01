package com.fenghuang.caipiaobao.widget.videoplayer.event;

import android.os.Bundle;


import com.example.playerlibrary.PLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2018/3/17.
 *
 * In order to improve memory performance,
 * the bundle entities passed in the framework
 * come from the bundle buffer pool.
 *
 */

public class BundlePool {

    private static final int POOL_SIZE = 3;

    private static List<Bundle> mPool;

    static {
        mPool = new ArrayList<>();
        for(int i=0;i<POOL_SIZE;i++)
            mPool.add(new Bundle());
    }

    public synchronized static Bundle obtain(){
        for(int i=0;i<POOL_SIZE;i++){
            if(mPool.get(i).isEmpty()){
                return mPool.get(i);
            }
        }
        PLog.w("BundlePool","<create new bundle object>");
        return new Bundle();
    }

}
