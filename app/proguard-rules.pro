# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:

-ignorewarnings

-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment

-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


#DataBinding用到的类
#-keep class com.qixiu.adapter.BindableItemm{*;}
#-keep class com.qixiu.adapter.ItemActionHandler{*;}

#所有实现了BindingItem的类
-keep class * implements com.qixiu.adapter.BindableItem{*;}
-keep class * implements com.qixiu.adapter.ItemActionHandler{*;}

#所有继承ViewDatabinding的类
-keep class * extends android.databinding.ViewDataBinding{*;}

#Gson beans
-keep class com.qixiu.lejia.beans.**{*;}

#WebView Js object
-keepclassmembers class com.qixiu.lejia.core.web.WebActivity$JsBridge{
    <methods>;
}

#保留反射字段
-keepclassmembers class android.support.v4.view.ViewPager{
     private java.lang.Boolean mFirstLayout;
}

#EventBus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

#腾讯SDK
-keep class com.tencent.**{*;}
-dontwarn com.tencent.**

#支付宝SDK
-keep class org.json.alipay.**{*;}
-dontwarn org.json.alipay.**
-keep class com.alipay.** {*;}
-dontwarn com.alipay.**
-keep class com.ta.utdid2.** {*;}
-dontwarn com.ta.utdid2.**
-keep class com.ut.device.** {*;}
-dontwarn com.ut.device.**

#高德地图
-dontwarn com.amap.api.**
-keep class com.amap.api.** { *; }
-dontwarn com.autonavi.**
-keep class com.autonavi.** { *; }

#友盟
-dontwarn com.umeng.**
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}