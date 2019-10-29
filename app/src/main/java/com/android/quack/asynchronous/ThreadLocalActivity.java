package com.android.quack.asynchronous;

import android.app.Activity;
import android.os.Bundle;

import com.android.quack.R;

public class ThreadLocalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_local);
    }
}
