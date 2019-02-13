package com.android.ux.ux;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.ux.ux.webview.AndroidCallJSActivity;
import com.android.ux.ux.webview.JSCallAndroidActivity;

public class WebViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
    }

    // Android去调用JS的代码
    public void onAndroidCallJSClick(View view) {
        startActivity(new Intent(this, AndroidCallJSActivity.class));
    }

    // JS去调用Android的代码
    public void onJSCallAndroidClick(View view) {
        startActivity(new Intent(this, JSCallAndroidActivity.class));
    }
}
