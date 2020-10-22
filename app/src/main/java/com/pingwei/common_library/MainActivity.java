package com.pingwei.common_library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pingwei.common_library.mqtt.EmqttConstants;
import com.pingwei.common_library.utils.SerialPortUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {
    public static SerialPortUtils serialPortUtils=new SerialPortUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serialPortUtils.openSerialPort( "/dev/ttyMT1",115200,new ReceiveListener());
        EmqttConstants.connectMQTT(getApplicationContext());

    }

}