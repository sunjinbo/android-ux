package com.android.ux.ux.asynchronous;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.android.ux.ux.R;

public class IntentServiceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_service);
    }

    public void onFooClick(View view) {
        AsyncIntentService.startActionFoo(this, "foo", "foo");
    }

    public void onBazClick(View view) {
        AsyncIntentService.startActionBaz(this, "baz", "baz");
    }
}
