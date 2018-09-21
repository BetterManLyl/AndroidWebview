package com.iflytek.webviewtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.iflytek.webviewtest.R;

/**
 * @author: ylli10
 * @date: 2018/9/14.
 * Email:ylli10@iflytek.com
 * Description:
 * 首页
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_skip_android_call_js;
    private Button btn_skip_js_call_android;

    private Button btn_load_baidu;
    private Button x5Webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        initView();
    }

    private void initView() {

        btn_skip_android_call_js = findViewById(R.id.btn_skip_android_call_js);
        btn_skip_js_call_android = findViewById(R.id.btn_skip_js_call_android);
        btn_load_baidu = findViewById(R.id.btn_load_baidu);
        x5Webview = findViewById(R.id.btn_x5_webview);
        x5Webview.setOnClickListener(this);
        btn_load_baidu.setOnClickListener(this);
        btn_skip_js_call_android.setOnClickListener(this);
        btn_skip_android_call_js.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_skip_android_call_js:
                startActivity(new Intent(this, AndroidCallJsActivity.class));
                break;
            case R.id.btn_skip_js_call_android:
                startActivity(new Intent(this, JsCallAndroidActivity.class));
                break;
            case R.id.btn_load_baidu:
                startActivity(new Intent(this, BaiduActivity.class));
                break;
            case R.id.btn_x5_webview:
                startActivity(new Intent(this, X5WebviewActivity.class));
                break;
            default:
                break;

        }
    }
}
