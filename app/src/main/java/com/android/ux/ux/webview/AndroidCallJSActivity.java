package com.android.ux.ux.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.ux.ux.MainActivity;
import com.android.ux.ux.R;

public class AndroidCallJSActivity extends Activity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_call_js);
        mWebView = findViewById(R.id.web_view);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 设置与Js交互的权限
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 设置允许JS弹窗

        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("file:///android_asset/callJS.html");

        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
        // WebView只是载体，内容的渲染需要使用WebViewChromeClient类去实现
        // 通过设置WebChromeClient对象处理JavaScript的对话框
        // 设置响应JS的Alert()函数
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(AndroidCallJSActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

        });
    }

    // 通过WebView的loadUrl()
    // 特点：方便简洁，但效率低，获取JS的返回值较为麻烦
    public void onLoadUrlClick(View view) {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                // 注意调用的JS方法名要对应上
                // 调用javascript的callJS()方法
                mWebView.loadUrl("javascript:callJS()");
            }
        });
    }

    // 通过WebView的evaluateJavascript()
    // 效率高，可以方便获取JS的返回值，仅支持Android4.4以上
    public void onEvaluateJavascriptClick(View view) {
        mWebView.evaluateJavascript("javascript:callJS()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                // 此处为 js 返回的结果
            }
        });
    }
}
