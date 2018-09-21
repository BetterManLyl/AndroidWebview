package com.iflytek.webviewtest.jsbridge;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Button;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.iflytek.webviewtest.R;

/**
 * @author: ylli10
 * @date: 2018/9/20.
 * Email:ylli10@iflytek.com
 * Description:
 * 使用JsBridge框架
 * <p>
 * <p>
 * 参考博客
 * https://www.jianshu.com/p/910e058a1d63
 * <p>
 * <p>
 * jsbridge git地址
 * https://github.com/lzyzsd/JsBridge
 */
public class JsBridgeActivity extends AppCompatActivity {

    private Button button;
    private BridgeWebView bridgeWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bridge_webview);

        initView();
    }

    private void initView() {
        button = findViewById(R.id.button);
        bridgeWebView = findViewById(R.id.bridge_webview);
        bridgeWebView.setDefaultHandler(new DefaultHandler());

        bridgeWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack("submitFrom web exe, response data from java");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
