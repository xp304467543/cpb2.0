package com.fenghuang.caipiaobao.widget.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fenghuang.caipiaobao.R;
import com.fenghuang.caipiaobao.manager.ImageManager;

import java.util.List;

/**
 * @ Author  QinTian
 * @ Date  2020-02-15
 * @ Describe
 */
public class GalleryAdapter extends PagerAdapter {

    private Context mContext;

    private List<String> data;


    public GalleryAdapter(Context context, List<String> result) {
        this.mContext = context;
        this.data = result;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout
                .gallery_item, null);
        SimpleDraweeView poster = view.findViewById(R.id.bggggg);
        ImageManager.INSTANCE.loadImg(data.get(position), poster);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
