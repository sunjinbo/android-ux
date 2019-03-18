package com.android.ux.ux;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAsynchronousClick(View view) {
        startActivity(new Intent(this, AsynchronousActivity.class));
    }

    public void onPluginClick(View view) {
        startActivity(new Intent(this, PluginActivity.class));
    }

    public void onWebViewJSClick(View view) {
        startActivity(new Intent(this, WebViewActivity.class));
    }

    public void onNetworkClick(View view) {
        startActivity(new Intent(this, NetworkActivity.class));
    }

    public void onDesignPatternsClick(View view) {
        startActivity(new Intent(this, DesignPatternsActivity.class));
    }

    public void onLayoutClick(View view) {
        startActivity(new Intent(this, LayoutActivity.class));
    }
}
