package com.iflytek.webviewtest.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.iflytek.webviewtest.R;

import java.util.HashMap;

/**
 * @author: ylli10
 * @date: 2018/9/14.
 * Email:ylli10@iflytek.com
 * Description:
 * Android调用Js代码
 * <p>
 * 对于Android调用JS代码的方法有2种：
 * 1、通过WebView的loadUrl（）
 * 2、通过WebView的evaluateJavascript（）
 */
public class AndroidCallJsActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = AndroidCallJsActivity.class.getSimpleName();

    private Button btn_load_url, btn_call_js_alert, btn_call_js_with_value,
            btn_get_input_value, btn_pass_value_js;
    private RadioGroup rg;

    private WebView webView;

    private String url = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_call_js);
        initView();
    }

    private void initView() {
        webView = findViewById(R.id.wv_android_js);
        btn_load_url = findViewById(R.id.btn_load_url);
        btn_call_js_alert = findViewById(R.id.btn_call_js_alert);
        btn_call_js_with_value = findViewById(R.id.btn_call_js_with_value);
        btn_get_input_value = findViewById(R.id.btn_get_input_value);
        btn_pass_value_js = findViewById(R.id.btn_pass_value_js);
        rg = findViewById(R.id.rg);

        btn_pass_value_js.setOnClickListener(this);
        btn_get_input_value.setOnClickListener(this);
        btn_call_js_with_value.setOnClickListener(this);
        btn_load_url.setOnClickListener(this);
        btn_call_js_alert.setOnClickListener(this);

        initWebSettings(webView);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "shouldOverrideUrlLoading: " + url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, "onPageFinished: ");
                super.onPageFinished(view, url);

             //   webView.loadUrl("javascript:callJs()");

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "onPageStarted: ");
                super.onPageStarted(view, url, favicon);
            }

        });

        //辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.e(TAG, "onProgressChanged: " + newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AndroidCallJsActivity.this);
                builder.setCancelable(false);
                builder.setTitle("JsAlter");
                builder.setMessage(message);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //这句话不能少，否则webview会卡死
                        result.confirm();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                builder.create().show();

                return true;
                // return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_baidu_url:
                        url = "http://www.baidu.com/";
                        break;
                    case R.id.rb_asset_url:
                        url = "file:///android_asset/index.html";
                        break;
                    case R.id.rb_sdcard_url:
//                        webView.loadUrl("content://com.iflytek.webviewtest"+Environment.getExternalStorageDirectory()+ File.separator + "database"+ File.separator+"taobao.html");
//                        webView.loadUrl("content://com.iflytek.webviewtest/mnt/sdcard/index.html");
//                        webView.loadUrl("file:///mnt/sdcard/index.html");
                        url = "file:///mnt/sdcard/sdcard_html.html";
                        break;
                    case R.id.rb_load_data:

                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * @param method 方法名
     */
    public void loadUrl(String method) {
        // Android版本变量
        final int version = Build.VERSION.SDK_INT;
        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version < 18) {
            webView.loadUrl("javascript:" + method);
        } else {
            webView.evaluateJavascript("javascript:" + method, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果
                    Log.e(TAG, "onReceiveValue: " + value);
                    Toast.makeText(AndroidCallJsActivity.this, value, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initWebSettings(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setUseWideViewPort(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load_url:
                loadWebviewUrl(url);
                break;
            case R.id.btn_call_js_alert:
                // 注意调用的JS方法名要对应上
                // 调用javascript的callJS()方法
                webView.loadUrl("javascript:callJs()");

                break;
            case R.id.btn_call_js_with_value:
                webView.evaluateJavascript("javascript:callJsWithValue()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Toast.makeText(AndroidCallJsActivity.this, value, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_pass_value_js:
                String value = "传值给js";
                //调用js中的函数：getValueFromAndroid(value)
                //注意点：调用js的有参function要记得前后添加单引号（'），不然也是传不过去参数的。
                webView.loadUrl("javascript:getValueFromAndroid('" + value + "')");
                break;
            case R.id.btn_get_input_value:
                webView.evaluateJavascript("javascript:getInputValue()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Toast.makeText(AndroidCallJsActivity.this, value, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            default:
                break;
        }
    }

    private void loadWebviewUrl(String url) {
        if (rg.getCheckedRadioButtonId() == R.id.rb_load_data) {
            String data = "<html><body>Today is a fine day.<br/><b>今天天气很好</b></body></html>";
            webView.loadData(data, "text/html; charset=UTF-8", null);
            return;
        }
        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG, "onKeyDown: " + webView.getUrl());
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack() && !webView.getUrl().contains("error")) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

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
}
