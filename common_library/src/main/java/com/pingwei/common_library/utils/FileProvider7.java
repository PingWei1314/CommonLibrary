package com.pingwei.common_library.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;

import androidx.core.content.FileProvider;

/**
 * http://blog.csdn.net/lmj623565791/article/details/72859156
 *
 *
 * Created by chandlerbing on 2017/6/15.
 */

public class FileProvider7 {

    /**
     * 获取兼容 7.0的uri,因为7.0不允许出现file://，改为content://.
     * @param context
     * @param file
     * @return
     */
    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if(Build.VERSION.SDK_INT >= 24) {
            fileUri = getUriForFile24(context,file);
        }else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }


    public static Uri getUriForFile24(Context context, File file) {

        Uri fileUri = FileProvider.getUriForFile(context
                , context.getPackageName() + ".android7.fileprovider",file);
        return fileUri;
    }

    public static void setIntentDataAndType(Context context
            , Intent intent
            , String type
            , File file
            , boolean writeAble) {

        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(context, file), type);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }


    }

    /**
     *
     * @param context
     * @param intent
     * @param type
     * @param uri  在7.0的时候必须传递的是 content://格式的，一般uri.fromFile（）会返回 file://格式的.
     * @param writeAble
     */
    public static void setIntentDataAndType(Context context
            , Intent intent
            , String type
            , Uri uri
            , boolean writeAble) {

        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(uri, type);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(uri, type);
        }


    }
}
