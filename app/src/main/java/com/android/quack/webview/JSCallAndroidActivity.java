package com.android.quack.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.quack.R;

import java.util.HashMap;
import java.util.Set;

public class JSCallAndroidActivity extends Activity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_call_android);
        mWebView = findViewById(R.id.web_view);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 设置与Js交互的权限
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 设置允许JS弹窗

        // 通过WebView的addJavascriptInterface()进行对象映射
        // 特点：使用简单，仅将Android对象和JS对象映射即可，但存在漏洞较多
        mWebView.addJavascriptInterface(new AndroidJS(), "test"); // AndroidJS类对象映射到js的test对象

        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("file:///android_asset/callAndroid.html");

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, final String message, JsResult result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder b = new AlertDialog.Builder(JSCallAndroidActivity.this);
                        b.setTitle("Alert");
                        b.setMessage(message);
                        b.setCancelable(true);
                        b.create().show();
                    }
                });

                return true;
            }
        });

        // 复写WebViewClient类的shouldOverrideUrlLoading方法
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // 步骤2：根据协议的参数，判断是否是所需要的url
                // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

                Uri uri = Uri.parse(url);
                // 如果url的协议 = 预先约定的 js 协议
                // 就解析往下解析参数
                if ( uri.getScheme().equals("js")) {

                    // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                    // 所以拦截url,下面JS开始调用Android需要的方法
                    if (uri.getAuthority().equals("webview")) {
                        //  步骤3：
                        // 执行JS所需要调用的逻辑
                        System.out.println("js调用了Android的方法");
                        // 可以在协议上带有参数并传递到Android上
                        HashMap<String, String> params = new HashMap<>();
                        Set<String> collection = uri.getQueryParameterNames();
                        for (String n : collection) {
                            params.put(n, uri.getQueryParameter(n));
                        }

                        String msg = "从JS获取参数：";
                        for (String key : params.keySet()) {
                            msg += (key + "=" + params.get(key));
                            msg += ";";
                        }

                        AlertDialog.Builder b = new AlertDialog.Builder(JSCallAndroidActivity.this);
                        b.setTitle("Alert");
                        b.setMessage(msg);
                        b.setCancelable(true);
                        b.create().show();
                    }

                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
      });
    }

    public class AndroidJS extends Object {
        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void hello(final String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder b = new AlertDialog.Builder(JSCallAndroidActivity.this);
                    b.setTitle("Alert");
                    b.setMessage(msg);
                    b.setCancelable(true);
                    b.create().show();
                }
            });
        }
    }
}
