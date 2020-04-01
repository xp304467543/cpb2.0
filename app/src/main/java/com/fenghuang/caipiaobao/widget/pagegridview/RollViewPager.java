package com.fenghuang.caipiaobao.widget.pagegridview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.fenghuang.baselib.utils.ToastUtils;

/**
 * @ Author  QinTian
 * @ Date  2020-03-11
 * @ Describe
 */
public class RollViewPager extends ViewPager {
    public RollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    int downX = 0; int downY = 0;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //int downX = 0;        这里赋值downX,downY会一直为0；要转为成员变量
        // int downY = 0;
        //y轴方向需要考虑移动整个模块,让其支持下拉刷新

        //在用系统的事件处理机制之前,先让自定义的viewpager满足我们自己定义的规则
        //viewpager选中最后一个点的时候,由右向左滑动,需要让整个模块进行翻转
        //viewpager选中第一个点的时候,由左向右滑动,需要让整个模块进行翻转
        //其余情况,翻转viewpager中的图片
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();

                if(Math.abs(moveX-downX)<Math.abs(moveY-downY)){
                    //y轴上的偏移量比x轴上的偏移量更多,可能会触发下拉刷新,需要响应事件的是大的模块
                    //请求不拦截触摸事件(不是这样的,要拦截)
                    getParent().requestDisallowInterceptTouchEvent(false);//让viewpager告知其父容器要拦截响应事件
                }else{
                    //x轴偏移量大于y轴偏移量情况
                    if (moveX-downX<0){
                        //由右向左移动,最后一个点,翻转整个模块
                        if (getCurrentItem() == getAdapter().getCount()-1){
                            getParent().requestDisallowInterceptTouchEvent(false);//让viewpager告知其父容器要拦截响应事件
                        }else if (getCurrentItem()<getAdapter().getCount()-1){
                            getParent().requestDisallowInterceptTouchEvent(true);//让viewpager告知其父容器不要拦截响应事件
                        }
                    }else{
                        //由左向右移动,第一个点,翻转整个模块
                        if (getCurrentItem() == 0){
                            getParent().requestDisallowInterceptTouchEvent(false);//让viewpager告知其父容器要拦截响应事件
                        }else if (getCurrentItem()>0){
                            getParent().requestDisallowInterceptTouchEvent(true);//让viewpager告知其父容器不要拦截响应事件
                        }
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
