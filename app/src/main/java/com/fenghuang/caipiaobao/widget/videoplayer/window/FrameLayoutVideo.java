package com.fenghuang.caipiaobao.widget.videoplayer.window;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.fenghuang.caipiaobao.R;

/**
 * @ Author  QinTian
 * @ Date  2020/5/26
 * @ Describe
 */
public class FrameLayoutVideo extends FrameLayout {


    public FrameLayoutVideo(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_anchor_data_tab_view, this);
    }
}
