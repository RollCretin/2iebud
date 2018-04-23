# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
  -dontoptimize
    -dontpreverify
    -dontwarn cn.jpush.**
    -keep class cn.jpush.** { *; }

    -dontshrink
    -dontoptimize
    -dontwarn com.google.android.maps.**
    -dontwarn android.webkit.WebView
    -dontwarn com.umeng.**
    -dontwarn com.tencent.weibo.sdk.**
    -dontwarn com.facebook.**
    -keep public class javax.**
    -keep public class android.webkit.**
    -dontwarn android.support.v4.**
    -keep enum com.facebook.**
    -keepattributes Exceptions,InnerClasses,Signature
    -keepattributes *Annotation*
    -keepattributes SourceFile,LineNumberTable

    -keep public interface com.facebook.**
    -keep public interface com.tencent.**
    -keep public interface com.umeng.socialize.**
    -keep public interface com.umeng.socialize.sensor.**
    -keep public interface com.umeng.scrshot.**

    -keep public class com.umeng.socialize.* {*;}


    -keep class com.facebook.**
    -keep class com.facebook.** { *; }
    -keep class com.umeng.scrshot.**
    -keep public class com.tencent.** {*;}
    -keep class com.umeng.socialize.sensor.**
    -keep class com.umeng.socialize.handler.**
    -keep class com.umeng.socialize.handler.*
    -keep class com.umeng.weixin.handler.**
    -keep class com.umeng.weixin.handler.*
    -keep class com.umeng.qq.handler.**
    -keep class com.umeng.qq.handler.*
    -keep class UMMoreHandler{*;}
    -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
    -keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
    -keep class im.yixin.sdk.api.YXMessage {*;}
    -keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
    -keep class com.tencent.mm.sdk.** {
       *;
    }
    -keep class com.tencent.mm.opensdk.** {
       *;
    }
    -keep class com.tencent.wxop.** {
       *;
    }
    -keep class com.tencent.mm.sdk.** {
       *;
    }
    -dontwarn twitter4j.**
    -keep class twitter4j.** { *; }

    -keep class com.tencent.** {*;}
    -dontwarn com.tencent.**
    -keep class com.kakao.** {*;}
    -dontwarn com.kakao.**
    -keep public class com.umeng.com.umeng.soexample.R$*{
        public static final int *;
    }
    -keep public class com.linkedin.android.mobilesdk.R$*{
        public static final int *;
    }
    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }

    -keep class com.tencent.open.TDialog$*
    -keep class com.tencent.open.TDialog$* {*;}
    -keep class com.tencent.open.PKDialog
    -keep class com.tencent.open.PKDialog {*;}
    -keep class com.tencent.open.PKDialog$*
    -keep class com.tencent.open.PKDialog$* {*;}
    -keep class com.umeng.socialize.impl.ImageImpl {*;}
    -keep class com.sina.** {*;}
    -dontwarn com.sina.**
    -keep class  com.alipay.share.sdk.** {
       *;
    }

    -keepnames class * implements android.os.Parcelable {
        public static final ** CREATOR;
    }

    -keep class com.linkedin.** { *; }
    -keep class com.android.dingtalk.share.ddsharemodule.** { *; }
    -keepattributes Signature

   -keep class com.alibaba.sdk.android.feedback.impl.FeedbackServiceImpl {*;}
   -keep class com.alibaba.sdk.android.feedback.impl.FeedbackAPI {*;}
   -keep class com.alibaba.sdk.android.feedback.util.IWxCallback {*;}
   -keep class com.alibaba.sdk.android.feedback.util.IUnreadCountCallback{*;}
   -keep class com.alibaba.sdk.android.feedback.FeedbackService{*;}
   -keep public class com.alibaba.mtl.log.model.LogField {public *;}
   -keep class com.taobao.securityjni.**{*;}
   -keep class com.taobao.wireless.security.**{*;}
   -keep class com.ut.secbody.**{*;}
   -keep class com.taobao.dp.**{*;}
   -keep class com.alibaba.wireless.security.**{*;}
   -keep class com.ta.utdid2.device.**{*;}

   -keep class com.alipay.android.app.IAlixPay{*;}
   -keep class com.alipay.android.app.IAlixPay$Stub{*;}
   -keep class com.alipay.android.app.IRemoteServiceCallback{*;}
   -keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
   -keep class com.alipay.sdk.app.PayTask{ public *;}
   -keep class com.alipay.sdk.app.AuthTask{ public *;}
   -keep class com.alipay.sdk.app.H5PayCallback {
       <fields>;
       <methods>;
   }
   -keep class com.alipay.android.phone.mrpc.core.** { *; }
   -keep class com.alipay.apmobilesecuritysdk.** { *; }
   -keep class com.alipay.mobile.framework.service.annotation.** { *; }
   -keep class com.alipay.mobilesecuritysdk.face.** { *; }
   -keep class com.alipay.tscenter.biz.rpc.** { *; }
   -keep class org.json.alipay.** { *; }
   -keep class com.alipay.tscenter.** { *; }
   -keep class com.ta.utdid2.** { *;}
   -keep class com.ut.device.** { *;}

#   定位
       -keep class com.amap.api.location.**{*;}
       -keep class com.amap.api.fence.**{*;}
       -keep class com.autonavi.aps.amapapi.model.**{*;}

    # ProGuard configurations for Bugtags
     -keepattributes LineNumberTable,SourceFile

     -keep class com.bugtags.library.** {*;}
     -dontwarn com.bugtags.library.**
     -keep class io.bugtags.** {*;}
     -dontwarn io.bugtags.**
     -dontwarn org.apache.http.**
     -dontwarn android.net.http.AndroidHttpClient

     # End Bugtags