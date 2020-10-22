package com.pingwei.common_library.banner;

import android.content.Context;
import android.widget.Scroller;

import java.lang.reflect.Field;

import androidx.viewpager.widget.ViewPager;

/**
 * 平滑滑动 比scroller 多做的事情就是设置了滑动时间

 * Created by chandlerbing on 2017/3/15.
 */

public class SmoothScroller extends Scroller {

    private int mDuration = 1200;

    public SmoothScroller(Context context) {
        super(context);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy,mDuration);
    }
    /**
     * 替换viewpager的scroller换为该对象
     * @param viewPager
     */
    public void bindViewPager(ViewPager viewPager) {

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager,this);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

















}
