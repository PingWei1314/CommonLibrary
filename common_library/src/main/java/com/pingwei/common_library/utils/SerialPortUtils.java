package com.pingwei.common_library.utils;

import android.os.SystemClock;
import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import android_serialport_api.SerialPort;

public class SerialPortUtils {
    private final String TAG = "emqtt";
    public boolean serialPortStatus = false; //是否打开串口标志
    public String data_;
    public boolean threadStatus; //线程状态，为了安全终止线程

    public SerialPort serialPort = null;
    public InputStream inputStream = null;
    public OutputStream outputStream = null;


    /**
     * 打开串口
     * @return serialPort串口对象
     */
    public SerialPort openSerialPort(String path,int baudrate,OnDataReceiveListener listener){
        try {
            serialPort = new SerialPort(new File(path),baudrate,0);
            this.serialPortStatus = true;
            threadStatus = false; //线程状态
            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            setOnDataReceiveListener(listener);
            new ReadThread().start(); //开始线程监控是否有数据要接收
        } catch (IOException e) {
            return serialPort;
        }
        return serialPort;
    }
    /**
     * 关闭串口
     */
    public void closeSerialPort(){
        try {
            inputStream.close();
            outputStream.close();

            this.serialPortStatus = false;
            this.threadStatus = true; //线程状态
            serialPort.close();
        } catch (IOException e) {
            Log.e(TAG, "closeSerialPort: 关闭串口异常："+e.toString());
            return;
        }
        Log.d(TAG, "closeSerialPort: 关闭串口成功");
    }
    /**
     * 发送串口指令（字符串）
     * @param data String数据指令
     */
    public void sendSerialPort(String data){


    }


    byte[] temp;
    /**
     * 单开一线程，来读数据
     */
    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            //判断进程是否在运行，更安全的结束进程
            byte[] buffer = new byte[1024];
            int size; //读取数据的大小
            while (!threadStatus) {
                //Log.d(TAG, "进入线程run");
                //64   1024
                try {
                    if (inputStream != null) {
                        size = inputStream.read(buffer);
                        if (size > 0) {
                            if (buffer[0] == (byte) 0xFE && buffer[size - 1] == (byte) 0xBE) {
                                Log.e(TAG, "run: " );
                                if (onDataReceiveListener != null)
                                    Log.e(TAG, "run: "+size );
                                onDataReceiveListener.onDataReceive(buffer, size);
                            } else {
                                if (buffer[0] == (byte) 0xFE) {
                                    temp = new byte[size];
                                    for (int i = 0; i < size; i++) {
                                        temp[i] = buffer[i];
                                    }
                                }else if (buffer[size - 1] == (byte) 0xBE) {
                                    byte[] dd = new byte[temp.length + size];
                                    for (int i = 0; i < temp.length; i++) {
                                        dd[i] = temp[i];
                                    }
                                    for (int i = 0; i < size; i++) {
                                        dd[temp.length + i] = buffer[i];
                                    }
                                    if (onDataReceiveListener != null)
                                        onDataReceiveListener.onDataReceive(dd, dd.length);
                                }else {
                                    byte[] dd = new byte[temp.length + size];
                                    for (int i = 0; i < temp.length; i++) {
                                        dd[i] = temp[i];
                                    }
                                    for (int i = 0; i < size; i++) {
                                        dd[temp.length + i] = buffer[i];
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //这是写了一监听器来监听接收数据
    public OnDataReceiveListener onDataReceiveListener = null;
    public static interface OnDataReceiveListener {
        public void onDataReceive(byte[] buffer, int size);
    }
    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }
}
