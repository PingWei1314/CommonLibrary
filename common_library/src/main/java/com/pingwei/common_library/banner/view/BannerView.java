package com.pingwei.common_library.banner.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.RequestManager;
import com.pingwei.common_library.R;
import com.pingwei.common_library.banner.BannerClickCallback;
import com.pingwei.common_library.banner.SmoothScroller;
import com.pingwei.common_library.banner.bo.BaseBannerItemInfo;
import com.pingwei.common_library.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 轮播主界面
 * Created by chandlerbing on 2017/3/15.
 */

public class BannerView extends RelativeLayout implements ViewPager.OnPageChangeListener, Runnable {

    private static final String TAG = "BannerView";

    //轮播时间 5s
    private static final int BANNER_INTERVAL_TIME = 5000;

    protected ViewPager mViewPager;
    //下方小圆点
    protected CirclePagerIndicator mIndicator;
    //轮播数据
    protected List<BaseBannerItemInfo> mBanners;
    //轮播adapter
    protected BannerAdapter mAdapter;
    //主线程handler
    protected Handler mHandler;
    //当前第几页 初始化的时候设置为一个较大的值，以便向前翻看。
    protected int mCurrentItem;
    //glide的请求管理器
    protected RequestManager mImageLoader;
    //是否正在滑动
    private boolean isScrolling;

    //点击返回
    private BannerClickCallback mClickCallback;

    //标题
    private TextView mTitleTextView;
    //是否显示标题
    private boolean mIsShowTitle;

    private RelativeLayout mGradientRelt;





    public BannerView(Context context) {
        this(context,null);


    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    /**
     * 初始化
     * @param context
     */
    protected void initViews(Context context) {


        LogUtil.e(TAG,"initViews");

        if(mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());

        }
        mBanners = new ArrayList<>();
        //初始化布局
        LayoutInflater.from(context).inflate(R.layout.view_banner_view,this,true);
        mViewPager = (ViewPager) findViewById(R.id.vp_banner);
        mIndicator = (CirclePagerIndicator) findViewById(R.id.indicator);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mGradientRelt = (RelativeLayout) findViewById(R.id.gradientRelt);

        mAdapter = new BannerAdapter();
        //设置滑动监听
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(mAdapter);
        //圆圈绑定viewpager
        mIndicator.bindViewPager(mViewPager);
        //设置圆圈点数量
        mIndicator.setCurrentItem(mBanners.size());
        //替换viewpager的mscroller字段
        new SmoothScroller(getContext()).bindViewPager(mViewPager);
        //监听是否正在滑动
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isScrolling = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isScrolling = false;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        isScrolling = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isScrolling = true;
                        break;

                }


                return false;
            }
        });


    }

    /**
     * 5s轮播
     */
    @Override
    public void run() {
        mHandler.postDelayed(this,BANNER_INTERVAL_TIME);
        if(isScrolling) {
            return;
        }
        mCurrentItem = mCurrentItem + 1;
        //具有平滑滑动效果
        mViewPager.setCurrentItem(mCurrentItem);

    }

    public void setBanners(List<? extends BaseBannerItemInfo> data, RequestManager loader
            , BannerClickCallback callback, boolean showTitle) {

        LogUtil.e(TAG,"setBanners");

        if(data != null) {

            this.mImageLoader = loader;
            this.mClickCallback = callback;
            this.mIsShowTitle = showTitle;

            if(!mIsShowTitle) {
                mGradientRelt.setBackgroundDrawable(null);
            }


            if(mHandler == null) {
                mHandler = new Handler(Looper.getMainLooper());
            }

            //取消自动翻页
            mHandler.removeCallbacks(this);

            //清除数据
            mBanners.clear();
            mBanners.addAll(data);
            //通知数据改变
            mViewPager.getAdapter().notifyDataSetChanged();
            //设置圆点个数
            mIndicator.setCount(mBanners.size());
            mIndicator.notifyDataSetChanged();
            if (mBanners.size() > 0
                    && mCurrentItem == 0
                    && mIsShowTitle
                    && !TextUtils.isEmpty(mBanners.get(0).getName())) {
                mTitleTextView.setText(mBanners.get(0).getName());
            }

            mCurrentItem = 0;



            //设置currentitem的值
            if(mCurrentItem == 0) {

                //注意只有一个的时候也要展示
                if(mBanners.size() != 1) {
                    mCurrentItem = mBanners.size() * 1000;
                    mViewPager.setCurrentItem(mCurrentItem);
                }
//                else {
//                    mViewPager.setCurrentItem(0);
//                }

            }


            //开始轮播
            if(mBanners.size() > 1) {
                mHandler.postDelayed(this,BANNER_INTERVAL_TIME);
            }


        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        isScrolling = mCurrentItem != position;

    }

    @Override
    public void onPageSelected(int position) {
        isScrolling = false;
        mCurrentItem = position;
        if(mBanners.size() != 0
                && !TextUtils.isEmpty(mBanners.get(position % mBanners.size()).getName())
                && mIsShowTitle) {

            mTitleTextView.setText(mBanners.get(position % mBanners.size()).getName());
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        isScrolling = state != ViewPager.SCROLL_STATE_IDLE;
    }



    /**
     * 轮播adapter
     */
    private class BannerAdapter extends PagerAdapter {

        /**
         * pager数量设置为最大值
         * @return
         */
        @Override
        public int getCount() {

         //   return mBanners.size() == 1 ? 1 : Integer.MAX_VALUE;

            //考虑0 和 1的情况 上式中没有考虑0的情况，导致只有一个图片时不能显示
            return mBanners.size() <= 1 ? mBanners.size() : Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 调用时机，并不是一开始就调用count次该方法，一般只调用三次，再次滑动的时候才会调用
         * 在滑动的时候就开始加载图片了
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            //没有持有view
            BannerItemView view = new BannerItemView(getContext());
            container.addView(view);
            if(mBanners.size() != 0) {
                // position为0，size为1，余数为0
                int p = position % mBanners.size();
                if(p >= 0 && p < mBanners.size()) {
                    //加载图片
                    view.initData(mImageLoader,mBanners.get(p),mClickCallback);
                }
            }
            return view;
        }

        /**
         * 一般滑动的时候就会调用，滑动的时候在不停的创建、销毁，因此某一时间点系统持有的view并不多
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(object instanceof BannerItemView) {
                container.removeView((BannerItemView)object);
            }

        }


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        LogUtil.e(TAG,"onAttachedToWindow");
        if(mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        LogUtil.e(TAG,"onDetachedFromWindow");
        super.onDetachedFromWindow();
        if(mHandler == null) {
            return;
        }
        mHandler.removeCallbacks(this);
        mHandler = null;

    }




}
