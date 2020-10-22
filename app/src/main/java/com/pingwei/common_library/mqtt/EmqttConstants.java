package com.pingwei.common_library.mqtt;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pingwei.common_library.AppDdManager;
import com.pingwei.common_library.Urls;
import com.pingwei.common_library.utils.LogUtil;
import com.pingwei.common_library.utils.SysContent;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.xutils.DbManager;

import java.io.DataOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EmqttConstants {

     private static Context context;
    private static MqttAndroidClient mClient;
    public static DbManager manager= AppDdManager.getsInstance().getDb();
    public static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static MqttCallbackExtended callback=new MqttCallbackExtended() {
        //连接成功
        @Override
        public void connectComplete(boolean b, String s) {
           Toast.makeText(context,"连接成功", Toast.LENGTH_SHORT).show();
           SysContent.MQTT_STATUS=true;
            subcribe(EmqTopic.getInstance().getControlTopic(),1);
           // LogUtil.writeFile(PreferenceManager.getInstance().getRunTime(),"[EmqttConstants]  connectComPlete: 连接成功");

        }
        //连接断开
        @Override
        public void connectionLost(Throwable throwable) {
            SysContent.MQTT_STATUS=false;
            Toast.makeText(context,"断开连接", Toast.LENGTH_LONG).show();
            //EventBus.getDefault().post(new MessageEvent(SysContent.DISCONNECT,""));
           // LogUtil.writeFile(PreferenceManager.getInstance().getRunTime(),"[EmqttConstants]  connectionLost:断开连接");

        }
        //接受订阅后的信息
        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
            Toast.makeText(context,"订阅下发信息"+s+mqttMessage.getPayload().toString(),Toast.LENGTH_LONG).show();
            Log.e("emqtt", "subcribe: "+"接受到订阅" );
            byte[] contentbyte=mqttMessage.getPayload();
            Log.e("emqtt", "subcribe: "+contentbyte.length+"");
            String content = new String(contentbyte,"UTF-8");
           // LogUtil.writeFile(PreferenceManager.getInstance().getRunTime(),"[EmqttConstants]  messageArrived；  "+content);
          //  LiveDataBus.get().with("show_content").postValue(content);
            Log.e("emqtt", "subcribe: "+content);

        }
        //上行信息发送成功走的方法
        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            Toast.makeText(context,"上行消息发送成功",Toast.LENGTH_LONG).show();
           // LogUtil.writeFile(PreferenceManager.getInstance().getRunTime(),"[EmqttConstants]  deliveryComplete；  上行消息发送成功");

        }
    };

    public static void connectMQTT(Context con){
        context=con;
        String broker = Urls.url;
        String clientId = EmqTopic.getInstance().getDevCode();
        //String clientId = "11111111111";
        mClient = new MqttAndroidClient(context,broker, clientId);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName(Urls.userName);
        connOpts.setPassword(Urls.passWord);
        connOpts.setAutomaticReconnect(true);
        connOpts.setKeepAliveInterval(50);
        connOpts.setConnectionTimeout(180);
        try {
            mClient.connect(connOpts,context,new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    SysContent.MQTT_IS_CONNECT=true;
                    Toast.makeText(context,"通信通道连接成功", Toast.LENGTH_SHORT).show();
                    //subcribe(EmqTopic.getInstance().getControlTopic(),1);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e("emqtt", "onFailure: "+asyncActionToken.toString()+"ex:"+exception.toString());
                   Toast.makeText(context,"通信通道连接失败", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        mClient.setCallback(callback);
    }
    public static void disconnectMQTT(){
        if (mClient!=null&&mClient.isConnected()) {
            try {
                mClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
    //订阅emqtt
    public static void subcribe(String topic, int qos){
        Log.e("emqtt", "subcribe: "+topic );
        if (mClient==null) {
            connectMQTT(context);
        }
        try {
            mClient.subscribe(topic, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                   Toast.makeText(context,"订阅成功", Toast.LENGTH_LONG).show();
                    //LogUtil.writeFile(PreferenceManager.getInstance().getRunTime(),"[EmqttConstants]  subcribe() onSuccess:  订阅成功");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(context,"订阅失败", Toast.LENGTH_LONG).show();
                  //  LogUtil.writeFile(PreferenceManager.getInstance().getRunTime(),"[EmqttConstants]  subcribe() onFailure:  订阅失败");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    //取消订阅emqtt
    public static void unSubcribe(String topic){
        if (mClient==null) {
            connectMQTT(context);
        }
        try {
            mClient.unsubscribe(topic, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(context,"取消订阅成功",Toast.LENGTH_LONG).show();
                 //   LogUtil.writeFile(PreferenceManager.getInstance().getRunTime(),"[EmqttConstants] unSubcribe() onSuccess:   取消订阅成功");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(context,"取消订阅失败",Toast.LENGTH_LONG).show();
                 //   LogUtil.writeFile(PreferenceManager.getInstance().getRunTime(),"[EmqttConstants] unSubcribe() onFailure:   取消订阅失败");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    //重启设备
    public static void reBoot() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("reboot" + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
