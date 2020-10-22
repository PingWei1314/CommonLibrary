package com.pingwei.common_library.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pingwei.common_library.R;


/**
 * 可以在application的theme中统一设置背景色：titleBarBackground
 * 不要再单独给activity设置标题
 * 公共titlebar
 * Created by chandlerbing on 2017/3/3.
 */

public class BaseTitleBarView extends RelativeLayout {

    //左
    protected RelativeLayout leftLayout;
    protected ImageView leftImage;
    //右
    protected RelativeLayout rightLayout;
    protected ImageView rightImage;
    //中
    protected TextView titleView;

    //根view
    protected RelativeLayout titleLayout;

    public BaseTitleBarView(Context context) {
        this(context,null);
    }

    public BaseTitleBarView(Context context, AttributeSet attrs) {
        this(context,attrs,0);

    }

    public BaseTitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_base_title_bar,this);
        leftLayout = (RelativeLayout) findViewById(R.id.left_layout);
        leftImage = (ImageView) findViewById(R.id.left_image);
        rightLayout = (RelativeLayout) findViewById(R.id.right_layout);
        rightImage = (ImageView) findViewById(R.id.right_image);
        titleView = (TextView) findViewById(R.id.title);
        titleLayout = (RelativeLayout) findViewById(R.id.root);
        parseStyle(context,attrs);

    }

    /**
     * 获取xml文件或theme中定义的属性
     * @param context
     * @param attrs
     */
    private void parseStyle(Context context, AttributeSet attrs) {
        if(attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.BaseTitleBarView);
            //设置标题
            String title = ta.getString(R.styleable.BaseTitleBarView_titleBarTitle);
            if(!TextUtils.isEmpty(title)) {
                titleView.setText(title);
            }


            //左侧
            Drawable leftDrawable = ta.getDrawable(R.styleable.BaseTitleBarView_titleBarLeftImage);
            if(null != leftDrawable) {
                leftImage.setImageDrawable(leftDrawable);
            }else {
                //设置默认返回图标
                leftImage.setImageResource(R.drawable.ic_base_title_back);
            }


            //设置背景  图片或颜色
            Drawable background = ta.getDrawable(R.styleable.BaseTitleBarView_titleBarBackground);
            if(null != background) {
                //替代的setbackground是从21添加的，因此还用这个
                titleLayout.setBackgroundDrawable(background);
            }

            ta.recycle();

        }
    }

    public void setLeftImageResource(int resId) {
        leftImage.setImageResource(resId);
    }

    public void setRightImageResource(int resId) {
        rightImage.setBackgroundResource(resId);
    }

    public void setLeftLayoutClickListener(OnClickListener listener){
        leftLayout.setOnClickListener(listener);
    }

    public void setRightLayoutClickListener(OnClickListener listener){
        rightLayout.setOnClickListener(listener);
    }

    public void setLeftLayoutVisibility(int visibility){
        leftLayout.setVisibility(visibility);
    }

    public void setRightLayoutVisibility(int visibility){
        rightLayout.setVisibility(visibility);
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public void setBackgroundColor(int color){
        titleLayout.setBackgroundColor(color);
    }

    public RelativeLayout getLeftLayout(){
        return leftLayout;
    }

    public RelativeLayout getRightLayout(){
        return rightLayout;
    }

}
