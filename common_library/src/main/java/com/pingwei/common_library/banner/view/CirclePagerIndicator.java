package com.pingwei.common_library.banner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.util.AttributeSet;
import android.view.View;

import com.pingwei.common_library.R;
import com.pingwei.common_library.banner.PagerIndicator;

import androidx.viewpager.widget.ViewPager;

/**
 轮播banner的下方圆点 圆点指示器
 *
 * 未选中的小圆分为2种，空心小圆与实心小圆，设置一种模式
 * Created by chandlerbing on 2017/3/14.
 */

public class CirclePagerIndicator extends View implements PagerIndicator {

    //未选中小圆圈的半径
    private float mRadius;
    //选中后变为大圆圈的半径
    private float mIndicatorRadius;
    //未选中的实心小圆圈的画笔
    private final Paint mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
    //未选中的空心小圆圈的画笔
    private final Paint mPaintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    //选中的大圆圈的画笔
    private final Paint mPaintIndicator = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ViewPager mViewPager;
    //外部设置的viewpager的监听，不能直接设置在viewpager上，因为该组件已经使用了
    private ViewPager.OnPageChangeListener mListener;
    //不使用平滑过渡的当前页
    private int mCurrentPage;
    //使用平滑过渡的当前页
    private int mFollowPage;

    //相邻两个页面之间的偏移值，在viewpager的监听中获取 取值范围 0～1 是左边页面偏移的百分比
    private float mPageOffset;
    //是否是在横向居中
    private boolean mCenterHorizontal;

    //指示器是否缓慢移动，不是瞬间跳动，在相邻两个圆点之间会有选中的点缓慢移动
    private boolean mIsFollow;
     //两个圆点之间的距离
    private float mIndicatorSpace;

    //圆点个数
    private int mCount;


    /**
     * 但参数构造方法
     * @param context
     */
    public CirclePagerIndicator(Context context) {
        this(context,null);
    }
    /**
     * 两个参数构造方法，xml中使用
     * @param context
     * @param attrs
     */
    public CirclePagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePagerIndicator);

        mCenterHorizontal = a.getBoolean(R.styleable.CirclePagerIndicator_circle_indicator_centerHorizontal,true);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(a.getColor(R.styleable.CirclePagerIndicator_unselected_fill_circle_indicator_color,0x0000ff));

        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setColor(a.getColor(R.styleable.CirclePagerIndicator_unselected_stroke_circle_indicator_color,0x000000));
        mPaintStroke.setStrokeWidth(a.getDimension(R.styleable.CirclePagerIndicator_unselected_stroke_indicator_width,0));

        mPaintIndicator.setStyle(Paint.Style.FILL);
        mPaintIndicator.setColor(a.getColor(R.styleable.CirclePagerIndicator_selected_indicator_color,0x0000ff));

        mRadius = a.getDimension(R.styleable.CirclePagerIndicator_unselected_indicator_radius,10);
        mIndicatorSpace = a.getDimension(R.styleable.CirclePagerIndicator_indicator_space,20);
        mIndicatorRadius = a.getDimension(R.styleable.CirclePagerIndicator_selected_indicator_radius,13);

        mIsFollow = a.getBoolean(R.styleable.CirclePagerIndicator_circle_indicator_follow,false);
        if(mIndicatorRadius < mRadius) mIndicatorRadius = mRadius;
        a.recycle();



    }

    /**
     * 画
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mViewPager == null) {
            return;
        }
        final int count = mCount;
        if(count == 0) {
            return;
        }
        //view宽度
        int width = getWidth();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        //直径 + 圆之间的间隔
        final float circleAndSpace = 2 * mRadius + mIndicatorSpace;
        //圆心在竖直方向上居中
        final float yOffset = getHeight() / 2;
        //如果不是水平居中，在第一个圆的圆心
        float xOffset = paddingLeft + mRadius;
        // 如果采用水平居中对齐
        if(mCenterHorizontal) {
            //xOffset += ((width - paddingLeft - paddingRight) - (count * circleAndSpace)) / 2.0f;
            //水平居中，则减去圆与空隙，除以2则为第一个圆的左边缘所在，再减去半径则为第一个圆的圆心
            xOffset = (width - count * 2 * mRadius - (count - 1) * mIndicatorSpace) / 2 - mRadius;

        }

        float cX;
        float cY;
        //如果绘制空心圆
        float strokeRadius = mRadius;
        if(mPaintStroke.getStrokeWidth() > 0) {
            strokeRadius -= mPaintStroke.getStrokeWidth() / 2.0f;

        }
        //绘制所有圆点
        for(int i = 0; i < count;i++) {
            //计算下个圆绘制起点偏移量
            cX = xOffset + (i * circleAndSpace);
            cY = yOffset;

            //要么绘制未选中的实心小圆
            if(mPaintFill.getAlpha() > 0) {
                canvas.drawCircle(cX,cY,strokeRadius,mPaintFill);
            }

            //要么绘制 如果绘制未选中的空心小圆
            if(strokeRadius != mRadius) {
                canvas.drawCircle(cX,cY,mRadius,mPaintStroke);
            }
        }
        float cx = (!mIsFollow ? mFollowPage : mCurrentPage) * circleAndSpace;

        //指示器选择缓慢移动
        if(mIsFollow) {
            cx += mPageOffset * circleAndSpace;
        }

        cX = xOffset + cx;
        cY = yOffset;
        //画选中的大圆
        canvas.drawCircle(cX,cY,mIndicatorRadius,mPaintIndicator);

    }

    /**
     * 设置圆点数量
     * @param count
     */
    public void setCount(int count) {
        this.mCount = count;
    }

    /**
     * 绑定view的时候设置监听
     * @param view
     */
    @Override
    public void bindViewPager(ViewPager view) {
        if(mViewPager == view) {
            return;
        }
        if(view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not set adapter");
        }
        mViewPager = view;
        mViewPager.addOnPageChangeListener(this);
        invalidate();

    }

    @Override
    public void bindViewPager(ViewPager view, int initialPosition) {

        bindViewPager(view);
        setCurrentItem(initialPosition);

    }

    /**
     * 设置那一页被选中
     * @param item
     */
    @Override
    public void setCurrentItem(int item) {
        if(mViewPager == null) {
            throw new IllegalStateException("indicator has not bind ViewPager");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        invalidate();

    }

    /**
     * 设置外部viewpager监听
     * @param listener listener
     */
    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {

        mListener = listener;
    }


    /**
     * 重新绘制
     */
    @Override
    public void notifyDataSetChanged() {

        invalidate();
        requestLayout();
    }

    /**
     * viewpager正在滑动
     * @param position  左边page的索引
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        mCurrentPage = mCount == 0 ? mCount : position % mCount;
        mPageOffset = positionOffset;
        //如果指示器跟随viewpager缓慢滑动，那么滚动的时候都绘制界面
        if(mIsFollow) {
            invalidate();
        }
        if(mListener != null) {
            mListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }

    }

    /**
     * 某一页被完全选中
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        mCurrentPage = mCount == 0 ? mCount : position % mCount;
        mFollowPage = mCount == 0 ? mCount : position % mCount;
        invalidate();
        if(mListener != null) {
            mListener.onPageSelected(position);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }

    }

    /**
     * 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));
    }

    /**
     * 获取view宽度
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        int width;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //如果是exactly模式，则采用设定的值
        if((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
            width = specSize;
        //否则的话计算得出
        }else {
            final int count = mCount;
            width = (int)(getPaddingLeft() + getPaddingRight()
            + (count * 2 * mRadius) + (mIndicatorRadius - mRadius) * 2 + (count - 1) * mIndicatorSpace);
            if(specMode == MeasureSpec.AT_MOST) {
                width = Math.min(width,specSize);
            }
        }
        return width;
    }


    /**
     * 获取组件的高度
     * @param measureSpec
     * @return
     */
    private int measureHeight(int measureSpec) {
        int height;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if(specMode == MeasureSpec.EXACTLY) {
            height = specSize;
        }else {
            height = (int)(2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);
            if(specMode == MeasureSpec.AT_MOST) {
                height = Math.min(height,specSize);
            }
        }

        return height;
    }



}
