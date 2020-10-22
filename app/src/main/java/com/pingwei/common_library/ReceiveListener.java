package com.pingwei.common_library;

import android.util.Log;

import com.pingwei.common_library.utils.HexaUtil;
import com.pingwei.common_library.utils.SerialPortUtils;

public class ReceiveListener implements SerialPortUtils.OnDataReceiveListener {
    @Override
    public void onDataReceive(byte[] buffer, int size) {
        byte[] temp = new byte[size];
        Log.e("serialport", "onDataReceive: "+buffer.length+","+size );
        for (int i = 0; i < size; i++) {
            temp[i] = buffer[i];
        }
        String message = HexaUtil.byte2HexStrArray(temp);
        Log.e("serial", "onDataReceive: "+message );
    }

}
