package com.pingwei.common_library.xrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.pingwei.common_library.R;
import com.pingwei.common_library.xrecyclerview.progressindicator.AVLoadingIndicatorView;

import java.util.Date;

/**
 *
 * 自定义view 带箭头的刷新头部
 * 带箭头 刷新时间 标题
 * 通过不断改变该控件的高度实现滑动效果
 */
public class ArrowRefreshHeader extends LinearLayout implements BaseRefreshHeader {
    //header容器类,加载xml布局
	private LinearLayout mContainer;
    //箭头imageview
	private ImageView mArrowImageView;
    //进度动画的容器类
	private SimpleViewSwitcher mProgressBar;
    //下拉状态显示
	private TextView mStatusTextView;
	private int mState = STATE_NORMAL;
    //刷新时间显示
	private TextView mHeaderTimeView;
    //箭头旋转动画
	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;
	//旋转动画时间
	private static final int ROTATE_ANIM_DURATION = 180;
    //测量的本身高度,为0吧,一开始?
	public int mMeasuredHeight;
    //move方法次数
  //  private int mMoveCount = 0;

	public ArrowRefreshHeader(Context context) {
		super(context);
		initView();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ArrowRefreshHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

    /**
     * 初始化高度并获取测量高度值
     */
	private void initView() {
		// 初始情况，设置下拉刷新view高度为
		mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(
				R.layout.listview_header, null);
        //参数
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //设置margin 值
        lp.setMargins(0, 0, 0, 0);
        //为该控件设置布局参数
		this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);
        //高度测量出来竟然不是0,同wrap_content测量出来的是一样的，设置不同数值的测量值都是一样的
		addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT,0));
		//内部内容重心底部 底部与顶部是一样的
        setGravity(Gravity.BOTTOM);

		mArrowImageView = (ImageView)findViewById(R.id.listview_header_arrow);
		mStatusTextView = (TextView)findViewById(R.id.refresh_status_textview);

        //init the progress view 进度动画设置
		mProgressBar = (SimpleViewSwitcher)findViewById(R.id.listview_header_progressbar);
        //进度动画框架
        AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(getContext());
        //设置进度动画颜色
        progressView.setIndicatorColor(0xffB5B5B5);
        //指定进度动画类型  :点组成的圆圈再隐藏
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        //将动画添加到容器类中
        mProgressBar.setView(progressView);

        //逆时针旋转180度动画,箭头变为向上
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
        //顺时针旋转180度动画,箭头变为向下
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
		
		mHeaderTimeView = (TextView)findViewById(R.id.last_refresh_time);
		//测量本身
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		//测量竟然是160
        mMeasuredHeight = getMeasuredHeight();
        Log.e("arrowrefreshheader view","measuredHeight:" + mMeasuredHeight);
    }

    /**
     * 设置进度动画的类型
     * @param style
     */
    public void setProgressStyle(int style) {
        //系统动画类型
        if(style == ProgressStyle.SysProgress){
            mProgressBar.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
        }else{
            AVLoadingIndicatorView progressView = new  AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
            progressView.setIndicatorId(style);
            mProgressBar.setView(progressView);
        }
    }

    /**
     * 设置箭头图片
     * @param resid
     */
    public void setArrowImageView(int resid){
        mArrowImageView.setImageResource(resid);
    }

    /**
     * 设置refreshheader状态,
     * @param state
     */
	public void setState(int state) {
        //如果状态相同,则不用转变
		if (state == mState) return ;
        //改变箭头图片跟进度动画
		if (state == STATE_REFRESHING) {	// 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
            //由刷新中变为刷新结束
		} else if(state == STATE_DONE) {
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {	// 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		//改变文字
		switch(state){
            case STATE_NORMAL:
                if (mState == STATE_RELEASE_TO_REFRESH) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }
                mStatusTextView.setText(R.string.listview_header_hint_normal);
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mStatusTextView.setText(R.string.listview_header_hint_release);
                }
                break;
            case     STATE_REFRESHING:
                mStatusTextView.setText(R.string.refreshing);
                break;
            //改变标题文字
            case    STATE_DONE:
                mStatusTextView.setText(R.string.refresh_done);
                break;
            default:
		}
		
		mState = state;
	}

    public int getState() {
        return mState;
    }

    /**
     * 下拉刷新结束
     */
    @Override
	public void refreshComplete(){
        //刷新结束时间
        mHeaderTimeView.setText(friendlyTime(new Date()));
        //改变状态、标题、图片
        setState(STATE_DONE);
        //200ms 结束
        new Handler().postDelayed(new Runnable(){
            public void run() {
                reset();
            }
        }, 200);
	}

    /**
     * 改变该控件的高度
     * @param height
     */
	public void setVisibleHeight(int height) {
		if (height < 0) height = 0;
		LayoutParams lp = (LayoutParams) mContainer .getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

    /**
     * 获取refresh header 可见高度
     * @return
     */
	public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        Log.e("下拉刷新高度:",lp.height + "");
		return lp.height;
	}

    /**
     * 通过设置高度达到移动距离的效果
     * @param delta
     */
    @Override
    public void onMove(float delta) {

     //   mMoveCount++;
        //在lineractivity例子中一开始的时候 getvisibleheight为0 delta为measure的高度
        if(getVisibleHeight() > 0 || delta > 0) {

        //    Log.e("onmove次数：",mMoveCount + "");
            //重新设置高度 delta有正负之分
            setVisibleHeight((int) delta + getVisibleHeight());

            Log.e("onMove","状态值：" + mState);
            //在linearactivity例子中，setrefreshing为true，mstate状态为2
            if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > mMeasuredHeight) {
                    setState(STATE_RELEASE_TO_REFRESH);
                }else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    /**
     * 滚动到0或者滚动到下拉刷新组件的高度
     * 并判断是否在freshing状态（从release_to_refresh到refreshing）
     * @return
     */
    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) // not visible.
            isOnRefresh = false;

        //可见高度大于下拉刷新的高度
        if(getVisibleHeight() > mMeasuredHeight &&  mState < STATE_REFRESHING){
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        //如果状态为正在刷新和高度正好等于下拉刷新的高度
        if (mState == STATE_REFRESHING && height <=  mMeasuredHeight) {
            //return;
        }
        int destHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mState == STATE_REFRESHING) {
            destHeight = mMeasuredHeight;
        }
        //如果为refreshing则滚动到下拉刷新组件的高度，如果不是（例如下滑高度不到下拉刷新的高度），
        //则滚动到0
        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    /**
     * 将下拉刷新组件滑动除去并设置状态为normal
     */
    public void reset() {
        smoothScrollTo(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                 //设置箭头图片、进度条、标题
                setState(STATE_NORMAL);
            }
        }, 500);
    }

    /**
     * 平缓滑动，destheight为下拉刷新组件的高度
     * @param destHeight
     */
    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                //设置高度，从可见高度到0，以动画的形式平滑滑动
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public static String friendlyTime(Date time) {
        //获取time距离当前的秒数
        int ct = (int)((System.currentTimeMillis() - time.getTime())/1000);

        if(ct == 0) {
            return "刚刚";
        }

        if(ct > 0 && ct < 60) {
            return ct + "秒前";
        }

        if(ct >= 60 && ct < 3600) {
            return Math.max(ct / 60,1) + "分钟前";
        }
        if(ct >= 3600 && ct < 86400)
            return ct / 3600 + "小时前";
        if(ct >= 86400 && ct < 2592000){ //86400 * 30
            int day = ct / 86400 ;
            return day + "天前";
        }
        if(ct >= 2592000 && ct < 31104000) { //86400 * 30
            return ct / 2592000 + "月前";
        }
        return ct / 31104000 + "年前";
    }

}