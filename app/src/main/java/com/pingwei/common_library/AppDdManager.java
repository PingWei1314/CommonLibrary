package com.pingwei.common_library;

import android.os.Environment;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;

/**
 * create by nieyuwei 2018/11/16
 */
public class AppDdManager {

    private static AppDdManager sInstance;
    private DbManager.DaoConfig daoCongfig = new DbManager.DaoConfig()
            //设置数据库的名字
            .setDbName("test.db")
            // 设置数据库支持事务
            .setAllowTransaction(true)
            // 设置数据库路径
            .setDbDir(new File(Environment.getExternalStorageDirectory()+"/DB"))
            // 设置数据库版本
            .setDbVersion(0);

    private DbManager mDb;


    private AppDdManager() {

        mDb = x.getDb(daoCongfig);

    }


    public DbManager getDb() {
        return mDb;
    }


    public static AppDdManager getsInstance(){

        if(sInstance == null) {
            synchronized (AppDdManager.class){
                if(sInstance == null) {
                    sInstance = new AppDdManager();
                }
            }

        }
        return sInstance;
    }
}
