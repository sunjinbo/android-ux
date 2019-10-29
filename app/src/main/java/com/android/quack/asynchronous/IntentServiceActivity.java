package com.android.quack.asynchronous;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.android.quack.R;

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
