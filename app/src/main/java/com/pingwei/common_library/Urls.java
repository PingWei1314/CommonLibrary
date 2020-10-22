package com.pingwei.common_library;

/**
 * 后台服务请求 URL
 */
public interface Urls {

    //初始化设备信息请求地址
    String URL_GET_CONFIG = "http://titeam.krray.com:26102/dev/getChildDev";
    //版本号比对 STM32
    String URL_CHECK_STM32_UPDATE = "http://titeam.krray.com:26102/api/IampAppDevice/getStmPath";
    //版本号比对 APP
    String URL_DOWNLOAD_APP = "http://titeam.krray.com:26102/api/IampAppDevice/getAppPath";
    //版本号比对积水结冰
    String URL_CHECK_WATERFREESE_UPDATE = "http://titeam.krray.com:26102/api/IampAppDevice/getwaterfreesePath";
    //版本号比对积水结冰
    String URL_CHECK_PROGRAMCODE_UPDATE = "http://titeam.krray.com:26102/api/IampAppDevice/getProgramPath";
    //上传图片 url 192.168.8.230:6802
    String CAMERA_UPLOAD_URL = "http://titeam.krray.com:26102/file/upload";
    //String CAMERA_UPLOAD_URL = "http://tech.hdsxtech.com:18772/file/upload";
    // String CAMERA_UPLOAD_URL = "http://192.168.1.35:6802/file/upload";

    //雷达版本号对比
    String URL_CHECK_RADAR_UPDATE="http://titeam.krray.com:26102/api/IampAppDevice/getRadarPath";
       //公司
      String url="tcp://ahdcloud.com:20510";
     // String url="tcp://tech.hdsxtech.com:11800";
      String userName="itgsadmin";
      char[] passWord="itgs7788@ahd".toCharArray();
    //公司
   String RMTP_URL = "rtmp://ahdcloud.com:20202/hls/";
    String PING_IP="www.baidu.com";

    //UDP

    String TOPIC_FRONT = "/a1708/ahad";
    String TOPIC_FRONT_SUB = "/ahad/a1708/";
    //语音对讲
    public static String SERVER_HOST= "120.27.26.108";

    public static final int UDP_PORT = 55660;

    /**
     * 江西婺源内网地址
     */
//    //初始化设备信息请求地址
//    String URL_GET_CONFIG = "http://36.20.150.52:9889/dev/getChildDev";
//    //版本号比对 STM32  婺源版本
//    String URL_CHECK_STM32_UPDATE = "http://36.20.150.52:9889/api/IampAppDevice/getStmPath";
//    //版本号比对 APP    婺源版本
//    String URL_DOWNLOAD_APP = "http://36.20.150.52:9889/api/IampAppDevice/getAppPath";
//    //版本号比对积水结冰   婺源版本
//    String URL_CHECK_WATERFREESE_UPDATE = "http://36.20.150.52:9889/api/IampAppDevice/getwaterfreesePath";
//    //版本号比对节目单   婺源版本
//    String URL_CHECK_PROGRAMCODE_UPDATE = "http://36.20.150.52:9889/api/IampAppDevice/getProgramPath";
//    //雷达版本号对比     婺源版本
//    String URL_CHECK_RADAR_UPDATE="http://36.20.150.52:9889/api/IampAppDevice/getRadarPath";
//    //上传图片 url 192.168.8.230:6802
//    String CAMERA_UPLOAD_URL = "http://36.20.150.52:9889/file/upload";
//    String PING_IP="36.20.150.52";
//    //婺源
//    String RMTP_URL = "rtmp://36.20.150.52:1935/live/";
//    //婺源
//    String url="tcp://36.20.150.52:1883";
//    String userName="admin";
//     char[] passWord="pubic".toCharArray();
//     String TOPIC_FRONT = "/hdsx/wy";
//     String TOPIC_FRONT_SUB = "/hdsx/wy/";
//    //语音对讲
//    public static String SERVER_HOST= "120.27.26.108";

    /**
     * 昌九内网地址
     */
//    //初始化设备信息请求地址
//    String URL_GET_CONFIG = "http://36.2.11.160:9890/dev/getChildDev";
//    //版本号比对 STM32  昌九版本
//    String URL_CHECK_STM32_UPDATE = "http://36.2.11.160:9890/api/IampAppDevice/getStmPath";
//    //版本号比对 APP    昌九版本
//    String URL_DOWNLOAD_APP = "http://36.2.11.160:9890/api/IampAppDevice/getAppPath";
//    //版本号比对积水结冰   昌九版本
//    String URL_CHECK_WATERFREESE_UPDATE = "http://36.2.11.160:9890/api/IampAppDevice/getwaterfreesePath";
//    //版本号比对节目单   昌九版本
//    String URL_CHECK_PROGRAMCODE_UPDATE = "http://36.2.11.160:9890/api/IampAppDevice/getProgramPath";
//    //雷达版本号对比     昌九版本
//    String URL_CHECK_RADAR_UPDATE="http://36.2.11.160:9890/api/IampAppDevice/getRadarPath";
//    //上传图片 url 192.168.8.230:6802
//    String CAMERA_UPLOAD_URL = "http://36.2.11.160:9890/file/upload";
//    String PING_IP="36.2.11.160";
//    //昌九
//    String RMTP_URL = "rtmp://36.2.11.160:1935/hls/";
//    //昌九
//    String url="tcp://36.2.11.160:1883";
//    String userName="admin";
//     char[] passWord="public".toCharArray();
//     String TOPIC_FRONT = "/hdsx/itgs";
//     String TOPIC_FRONT_SUB = "/hdsx/itgs/";
//     //语音对讲
//    public static String SERVER_HOST= "36.2.11.160";
}
