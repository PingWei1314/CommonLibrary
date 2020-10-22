package com.pingwei.common_library.mqtt;

import android.text.TextUtils;

import com.pingwei.common_library.Urls;


public class EmqTopic {
    private static final EmqTopic instance = new EmqTopic();
   //private String devCode= Config.getInstance().getDevCode();
    private String devCode= "170820200101001";
    public static EmqTopic getInstance() {
        return instance;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }
    public String getMonitorUpdateTopic() {
        if ( !TextUtils.isEmpty(devCode)) {
            return  Urls.TOPIC_FRONT + "/" + devCode + "/monitor/update";
        } else {
            return "";
        }
    }
    public String getAlarmUpdateTopic() {
        if ( !TextUtils.isEmpty(devCode)) {
            return Urls.TOPIC_FRONT+ "/" + devCode + "/alarm/update";
        } else {
            return "";
        }
    }
    public String getControlTopic() {
        if ( !TextUtils.isEmpty(devCode)) {
            return Urls.TOPIC_FRONT_SUB+devCode+"/control/get";
        } else {
            return "";
        }
    }
    public String getCarAndDevChild() {
        if ( !TextUtils.isEmpty(devCode)) {
            return Urls.TOPIC_FRONT+"/"+devCode+"/monitor/series/update";
        } else {
            return "";
        }
    }

    public String getNoticeTopic() {

        if ( !TextUtils.isEmpty(devCode)) {
            return Urls.TOPIC_FRONT+"/"+devCode+"/file/update";


        } else {
            return "";
        }
    }
}
