package com.pingwei.common_library;

import android.app.Application;

import org.xutils.x;

import androidx.multidex.MultiDex;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(getApplicationContext());
        x.Ext.init(this);

    }
}
