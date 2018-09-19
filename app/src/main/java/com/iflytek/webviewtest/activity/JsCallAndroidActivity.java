package com.iflytek.webviewtest.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.webviewtest.MainActivity;
import com.iflytek.webviewtest.R;
import com.iflytek.webviewtest.utils.FileUtils;
import com.iflytek.webviewtest.utils.GsonUtil;
import com.iflytek.webviewtest.utils.MPermissionUtils;
import com.iflytek.webviewtest.utils.MyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: ylli10
 * @date: 2018/9/14.
 * Email:ylli10@iflytek.com
 * Description:
 * JS调用Android代码
 */
public class JsCallAndroidActivity extends AppCompatActivity {

    private static String TAG = JsCallAndroidActivity.class.getSimpleName();
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_android);
        initView();
    }

    private void initView() {
        webView = findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        WebView.setWebContentsDebuggingEnabled(true);

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        webView.addJavascriptInterface(new MyUtils(this), "android");

        webView.loadUrl("file:///android_asset/index.html");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e(TAG, "shouldOverrideUrlLoading: " + url);
                // 步骤2：根据协议的参数，判断是否是所需要的url
                // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）
                Uri uri = Uri.parse(url);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数,获取资源使用的协议
                if (uri.getScheme().equals("js")) {

                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("webview")) {
                        Toast.makeText(JsCallAndroidActivity.this, "拦截到了新的请求地址", Toast.LENGTH_SHORT).show();
                        //  步骤3：
                        // 执行JS所需要调用的逻辑
                        // 可以在协议上带有参数并传递到Android上
                        HashMap<String, String> params = new HashMap<>();
                        Set<String> collection = uri.getQueryParameterNames();

                        Iterator<String> it = collection.iterator();
                        while (it.hasNext()) {
                            String str = it.next();
                            Log.e(TAG, "参数: " + str + "=" + uri.getQueryParameter(str));
                        }


                    }

                    return false;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "onPageStarted: ");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, "onPageFinished: ");
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.e(TAG, "onReceivedError: ");
                super.onReceivedError(view, request, error);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.e(TAG, "onProgressChanged: " + newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.e(TAG, "onReceivedTitle: " + title);
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {

                // 拦截输入框(原理同方式2)
                // 参数message:代表promt（）的内容（不是url）
                // 参数result:代表输入框的返回值


                Log.e(TAG, "message: " + message);
                // 根据协议的参数，判断是否是所需要的url(原理同方式2)
                // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

                Uri uri = Uri.parse(message);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if (uri.getScheme().equals("js")) {
                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("demo")) {
                        //参数result:代表消息框的返回值(输入值)
                        //①获得一个LayoutInflater对象factory,加载指定布局成相应对象
                        final LayoutInflater inflater = LayoutInflater.from(JsCallAndroidActivity.this);
                        final View myview = inflater.inflate(R.layout.prompt_view, null);
                        //设置TextView对应网页中的提示信息,edit设置来自于网页的默认文字
                        ((TextView) myview.findViewById(R.id.text)).setText(message);
                        ((EditText) myview.findViewById(R.id.edit)).setText(defaultValue);
                        //定义对话框上的确定按钮
                        new AlertDialog.Builder(JsCallAndroidActivity.this)
                                .setTitle("Prompt对话框")
                                .setView(myview)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //单击确定后取得输入的值,传给网页处理
                                        String value = ((EditText) myview.findViewById(R.id.edit)).getText().toString();
                                        result.confirm(value);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.cancel();
                                    }
                                }).show();
                    }
                    return true;
                }
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);
            }
        });

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
                        str = GsonUtil.getInstance().toJson(str);

                        refreshHtml(str);
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
        webView.evaluateJavascript("javascript:takePhotoCallBack('" + jsonS + "')", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.e(TAG, "onReceiveValue: " + value);
                Toast.makeText(JsCallAndroidActivity.this, value, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
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
