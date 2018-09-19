package com.iflytek.webviewtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.iflytek.webviewtest.utils.FileUtils;
import com.iflytek.webviewtest.utils.GsonUtil;
import com.iflytek.webviewtest.utils.MPermissionUtils;
import com.iflytek.webviewtest.utils.MyUtils;

import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private WebView webView;
    private Button btn_call_js, btn_baidu, btn_index;
    private String url = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    @SuppressLint("JavascriptInterface")
    private void initView() {

        url = "file:///android_asset/index.html";

        webView = findViewById(R.id.webview);
        btn_call_js = findViewById(R.id.btn_call_js);
        btn_baidu = findViewById(R.id.btn_baidu);
        btn_index = findViewById(R.id.btn_index);
        btn_baidu.setOnClickListener(this);
        btn_index.setOnClickListener(this);
        btn_call_js.setOnClickListener(this);

        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        ////设置自适应屏幕，两者合用
        //webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        // webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //设置chrome浏览器可调试
        WebView.setWebContentsDebuggingEnabled(true);
        webView.loadUrl(url);

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        webView.addJavascriptInterface(new MyUtils(this), "android");


        //作用：辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。
        webView.setWebChromeClient(new WebChromeClient() {

            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.e(TAG, "onProgressChanged: " + newProgress);
                super.onProgressChanged(view, newProgress);
            }

            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.e(TAG, "onReceivedTitle: " + title);
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            // 由于设置了弹窗检验调用结果,所以需要支持js对话框
            // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
            // 通过设置WebChromeClient对象处理JavaScript的对话框
            //设置响应js 的Alert()函数
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                Log.e(TAG, "onJsAlert: " + message);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Alert");
                builder.setMessage(message);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });

                builder.setCancelable(false);
                builder.create().show();
                return true;

                // return super.onJsAlert(view,url,message,result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Log.e(TAG, "onJsConfirm: ");

                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.e(TAG, "onJsPrompt: ");
                Uri uri = Uri.parse(message);
                if (uri.getScheme().equals("js")) {
                    if (uri.getAuthority().equals("demo")) {
                        Log.e(TAG, "js调用了Android的方法");
                        // 可以在协议上带有参数并传递到Android上
                        HashMap<String, String> params = new HashMap<>();
                        Set<String> collection = uri.getQueryParameterNames();

                        //参数result:代表消息框的返回值(输入值)
                        result.confirm("js调用了Android的方法成功啦");
                    }
                    return true;
                }
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                Log.e(TAG, "onJsBeforeUnload: ");

                return super.onJsBeforeUnload(view, url, message, result);
            }
        });

        //处理各种通知 & 请求事件
        webView.setWebViewClient(new WebViewClient() {

            //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "shouldOverrideUrlLoading: " + url);
                view.loadUrl(url);
                return true;
            }

            //设定加载开始的操作
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "onPageStarted: " + url);
                super.onPageStarted(view, url, favicon);
            }

            //设定加载结束的操作
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, "onPageFinished: ");
                super.onPageFinished(view, url);
            }

            //设定加载资源的操作
            @Override
            public void onLoadResource(WebView view, String url) {
                Log.e(TAG, "onLoadResource: ");
                super.onLoadResource(view, url);
            }

            //加载页面的服务器出现错误时（如404）调用。
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.e(TAG, "onReceivedError: 加载失败");
//                url = "file:///android_asset/error_page.html";
//                view.loadUrl(url);

                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.e(TAG, "onReceivedSslError: ");
                super.onReceivedSslError(view, handler, error);
            }
        });
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //表示按返回键时的操作
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        webView.goBack();
                        //后退
                        //前进
                        //webview.goForward();
                        //已处理
                        return true;
                    } else {
                        MainActivity.super.onBackPressed();
                    }
                }
                return false;
            }

        });
    }


    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call_js:
                //  webView.loadUrl("javascript:callJs()");

                //android 调用js
                webView.evaluateJavascript("javascript:callJs()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //接收js返回的结果 value
                        Log.e(TAG, "onReceiveValue: " + value);
                    }
                });
                break;
            case R.id.btn_baidu:
                url = "http://www.baidu.com";
                webView.loadUrl(url);

                break;
            case R.id.btn_index:
                url = "file:///android_asset/index.html";
                webView.loadUrl(url);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == MyUtils.TAKE_PHOTO_REQUEST_CODE) {
                if (intent != null) {
                    Uri uri = intent.getData();
                    if (uri == null) {
                        Bundle bundle = intent.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        Bitmap compressBitmap = FileUtils.compressImage(bitmap);
                        Log.d(TAG, "bitmapToBase64被调了...");
                        String str = FileUtils.bitmapToBase64(compressBitmap);
                        String jsonS = GsonUtil.getInstance().toJson(str);
                        refreshHtml(jsonS);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }


    /**
     * 刷新页面
     *
     * @param jsonS
     */
    private void refreshHtml(String jsonS) {
        //  webView.loadUrl("javascript:takePhotoCallBack(" + jsonS + ")");
        webView.evaluateJavascript("javascript:takePhotoCallBack(" + jsonS + ")", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
