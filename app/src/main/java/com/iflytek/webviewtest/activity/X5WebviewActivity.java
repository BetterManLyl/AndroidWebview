package com.iflytek.webviewtest.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.iflytek.webviewtest.R;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @author: ylli10
 * @date: 2018/9/21.
 * Email:ylli10@iflytek.com
 * Description:
 * <p>
 * 腾讯X5Webview使用
 * 官网 https://x5.tencent.com/tbs/index.html
 * demo地址 https://github.com/BetterManLyl/X5WebviewDemo
 */
public class X5WebviewActivity extends AppCompatActivity {

    private static String TAG = X5WebviewActivity.class.getSimpleName();

    private WebView webView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x5webview);
        webView = findViewById(R.id.x5webview);
        initWebview();
    }

    private void initWebview() {

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(false);
        webSettings.setSupportMultipleWindows(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView webView, String s) {
                Log.e(TAG, "onLoadResource: ");
                super.onLoadResource(webView, s);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                Log.e(TAG, "shouldOverrideUrlLoading: " + s);
                return super.shouldOverrideUrlLoading(webView, s);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                Log.e(TAG, "shouldOverrideUrlLoading: ");
                return super.shouldOverrideUrlLoading(webView, webResourceRequest);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                Log.e(TAG, "onPageStarted: " + s);
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                Log.e(TAG, "onPageFinished: " + s);
                super.onPageFinished(webView, s);
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                Log.e(TAG, "onReceivedError: ");
                webView.loadUrl("file:///android_asset/error_page.html");
                super.onReceivedError(webView, i, s, s1);
            }

            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                Log.e(TAG, "onReceivedError: ");
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }

            @Override
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                Log.e(TAG, "onReceivedHttpError: ");
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
                Log.e(TAG, "shouldInterceptRequest: " + s);
                return super.shouldInterceptRequest(webView, s);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                Log.e(TAG, "shouldInterceptRequest: ");
                return super.shouldInterceptRequest(webView, webResourceRequest);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                Log.e(TAG, "onReceivedSslError: ");
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView webView, KeyEvent keyEvent) {
                Log.e(TAG, "shouldOverrideKeyEvent: ");
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return super.shouldOverrideKeyEvent(webView, keyEvent);
            }

            @Override
            public void onReceivedLoginRequest(WebView webView, String s, String s1, String s2) {
                Log.e(TAG, "onReceivedLoginRequest: ");
                super.onReceivedLoginRequest(webView, s, s1, s2);
            }
        });

        webView.loadUrl("http://www.baidu.com");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
