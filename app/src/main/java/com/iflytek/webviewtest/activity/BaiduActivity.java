package com.iflytek.webviewtest.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.webviewtest.R;

/**
 * @author: ylli10
 * @date: 2018/9/16.
 * Email:ylli10@iflytek.com
 * Description:
 * 加载百度页面
 */
public class BaiduActivity extends AppCompatActivity {

    private static String TAG = BaiduActivity.class.getSimpleName();

    private TextView tv_load_progress, tv_title, tv_load_progress_message;

    private WebView webView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_baidu);
        initWebview();
        initView();

    }

    private void initWebview() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(getApplicationContext());
        webView.setLayoutParams(params);
        LinearLayout linearLayout = findViewById(R.id.root);
        linearLayout.addView(webView);
    }

    private void initView() {
        tv_load_progress = findViewById(R.id.tv_load_progress);
        tv_title = findViewById(R.id.tv_title);
        tv_load_progress_message = findViewById(R.id.tv_load_progress_message);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        //a. 缓存机制：如何将加载过的网页数据保存到本地
        //b. 缓存模式：加载网页时如何读取之前保存到本地的网页缓存
        //前者是保存，后者是读取，请注意区别
        //设置加载缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置缓存机制
        // webSettings.setDomStorageEnabled(true);
        String cacheDirPath = this.getFilesDir().getAbsolutePath() + "/webcache";
        // 1. 设置缓存路径
        webSettings.setAppCachePath(cacheDirPath);
        // 2. 设置缓存大小
        webSettings.setAppCacheMaxSize(20 * 1024 * 1024);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabasePath(cacheDirPath);
        // 设置缓存路径

        webSettings.setDatabaseEnabled(true);

        ///扩大比例的缩放
        webSettings.setUseWideViewPort(true);

        //自适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        //支持缩放，默认为true。
        webSettings.setSupportZoom(true);
        //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setBuiltInZoomControls(true);
        //隐藏原生的缩放控件
        webSettings.setDisplayZoomControls(false);

        webView.loadUrl("https://www.baidu.com");


        //如果不设置WebViewClient，请求会跳转系统浏览器
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "shouldOverrideUrlLoading: " + url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                tv_load_progress_message.setText("加载开始了");
                Log.e(TAG, "onPageStarted: " + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, "onPageFinished: ");
                tv_load_progress_message.setText("加载结束");
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                Log.e(TAG, "onLoadResource: ");
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.e(TAG, "onReceivedError: ");
                view.loadUrl("file:///android_asset/error_page.html");
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.e(TAG, "onReceivedSslError: ");
                super.onReceivedSslError(view, handler, error);
            }
        });


        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, final int newProgress) {
                Log.e(TAG, "onProgressChanged: " + newProgress);
                tv_load_progress.setText("加载进度：" + newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.e(TAG, "onReceivedTitle: " + title);
                tv_title.setText("标题：" + title);
                super.onReceivedTitle(view, title);
            }


        });
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.clearCache(true);
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }

        super.onDestroy();

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
