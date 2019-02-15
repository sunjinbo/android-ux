package com.android.ux.ux;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.ux.ux.dp.ChainActivity;

public class DesignPatternsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_patterns);
    }

    public void onChainClick(View view) {
        startActivity(new Intent(this, ChainActivity.class));
    }
}
