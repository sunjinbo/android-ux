package com.android.ux.ux;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.ux.ux.network.HttpActivity;

public class NetworkActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
    }

    public void onHttpClick(View view) {
        startActivity(new Intent(this, HttpActivity.class));
    }

    public void onSocketClick(View view) {

    }
}
