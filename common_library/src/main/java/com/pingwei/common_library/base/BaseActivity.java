package com.pingwei.common_library.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pingwei.common_library.utils.ExitAppUtil;
import com.pingwei.common_library.utils.MyDialog;

import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {
    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowStatusBarColor(this, Color.parseColor("#2e9e74"));
        ExitAppUtil.getInstance().addActivity(this);
        dialog = MyDialog.getInstance(this);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = d.getHeight();   //高度设置为屏幕的0.3
        p.width = d.getWidth();    //宽度设置为全屏
        p.alpha=0.5f;
        dialog.getWindow().setAttributes(p);     //设置生效
    }
    /**
     * title默认功能，默认左边是返回
     * @param titlebarView
     * @param title
     */
    public void initBackBar(BaseTitleBarView titlebarView, String title, int rightTv) {
        if (titlebarView != null) {
            titlebarView.setTitle(title);
            titlebarView.setRightImageResource(rightTv);
            titlebarView.setLeftLayoutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }
    }
    public void initBackBar(BaseTitleBarView titlebarView, String title) {
        if (titlebarView != null) {
            titlebarView.setTitle(title);
            titlebarView.setLeftLayoutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }
    }
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(colorResId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showMyDialog() {
        try {

            dialog.show();

        } catch (Exception e) {
            Log.e("gxsuper", e.toString());
        }

    }
    public void hideMyDialog() {

        if (dialog != null && dialog.isShowing())
            dialog.hide();

    }
    }
