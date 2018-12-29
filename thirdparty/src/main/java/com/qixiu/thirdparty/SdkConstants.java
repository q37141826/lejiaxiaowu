package com.qixiu.thirdparty;

/**
 * Created by Long on 2018/5/2
 */
public class SdkConstants {

    public static final String QQ_APP_ID = "1106870162";

//    public static final String WX_APP_ID = "wxa42d2ca484d9d68d";//旧的
    public static final String WX_APP_ID = "wx0f3d771fb29aeea4";//新的

//    public static final String WX_APP_SECRET = "eb483744e2ca44b1f456f0dddbe47249";//旧的
    public static final String WX_APP_SECRET = "86d207fda81d12f93811c87df4f97823";//新的

    //高德地图key
    public static final String MAP_KEY = "d7fa18ae9db4e684829c49db44b06b0a";

    //友盟KEY
    public static final String UMENG_KEY = "5b10f70df43e48760b0001a3";
   //极光推送KEY
    public static final String JPUSH_KEY = "19a0fc245e220e51e7a89cc3";


    /**
     * 微信登录返回
     */
    public static final int WX_TYPE_LOGIN = 1;

    SdkConstants() {
//        Tencent
    }


    /*
    //当用户使用自有账号登录时，可以这样统计：
    MobclickAgent.onProfileSignIn("userID");
    //当用户使用第三方账号（如新浪微博）登录时，可以这样统计：
    MobclickAgent.onProfileSignIn("WB"，"userID");
    //登出
    MobclickAgent.onProfileSignOff();*/

}
