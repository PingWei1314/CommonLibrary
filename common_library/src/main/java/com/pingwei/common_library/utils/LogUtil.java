package com.pingwei.common_library.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 输出工具类
 * @author chandler
 * @date 2015/12/28
 */
public class LogUtil {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;

    //正式发布
    public static int LEVEL = VERBOSE;

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd HH:mm:ss");


    public static void v(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }
    public static void d(String tag, String msg) {
        if (LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }
    public static void i(String tag, String msg) {
        if (LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }
    public static void w(String tag, String msg) {
        if (LEVEL <= WARN) {
            Log.w(tag, msg);

        }
    }
    public static void e(String tag, String msg) {
        if (LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void writeFile(String fileName, String content) {
        File dir=new File(Environment.getExternalStorageDirectory()+"/Exception/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory()+"/Exception/",fileName);

        try {
            FileWriter fileWriter = new FileWriter(file,true);

            PrintWriter printWriter = new PrintWriter(fileWriter,true);
            printWriter.println("[" + format.format(new Date())+"]   "+content);
            printWriter.close();
            fileWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeFile(String fileName, Throwable content) {

        File dir=new File(Environment.getExternalStorageDirectory()+"/Exception/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory()+"/Exception/",fileName);

        try {
            FileWriter fileWriter = new FileWriter(file,true);

            PrintWriter printWriter = new PrintWriter(fileWriter,true);
            printWriter.println("*********" + format.format(new Date())+"*********");

            content.printStackTrace(printWriter);
            printWriter.println("--");
            printWriter.println();

            printWriter.close();
            fileWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
