package com.iflytek.webviewtest;

import android.app.Application;
import android.util.Log;
import android.webkit.WebSettings;

import com.tencent.smtt.sdk.QbSdk;

/**
 * @author: ylli10
 * @date: 2018/9/18.
 * Email:ylli10@iflytek.com
 * Description:
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback callback = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + b);
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), callback);
    }
}
