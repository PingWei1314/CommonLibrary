package com.pingwei.common_library.banner;


import androidx.viewpager.widget.ViewPager;

/**
 * Created by huanghaibin
 * on 16-5-19.
 * 抽象指示器 扩展indicator方法
 */
public interface PagerIndicator extends ViewPager.OnPageChangeListener {


    /**
     * 绑定viewpager
     * bind the viewPager into indicator
     *
     * @param viewPager the ViewPager
     */
    void bindViewPager(ViewPager viewPager);

    /**
     * 绑定viewpager并滑动到初始化位置
     * bind the viewPager into indicator
     *
     * @param viewPager       the ViewPager
     * @param initialPosition initialPosition
     */
    void bindViewPager(ViewPager viewPager, int initialPosition);

    /**
     * 设置当前显示的item
     * the ViewPager Current Item
     *
     * @param currentItem currentItem
     */
    void setCurrentItem(int currentItem);

    /**
     * 设置监听
     * the ViewPager ChangeListener
     *
     * @param listener listener
     */
    void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) ;

    /**
     * 更新数据
     * update the DataSet,invalidate
     */
    void notifyDataSetChanged();



















}
