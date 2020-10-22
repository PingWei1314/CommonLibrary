package com.pingwei.common_library.base;

import android.os.Bundle;


import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.pingwei.common_library.utils.MyDialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment {
    private MyDialog dialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dialog = MyDialog.getInstance(getContext());
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = d.getHeight();   //高度设置为屏幕的0.3
        p.width = d.getWidth();    //宽度设置为全屏
        p.alpha=0.5f;
        dialog.getWindow().setAttributes(p);     //设置生效
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
