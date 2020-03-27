package com.android.quack.asynchronous;

import android.app.Activity;
import android.os.Bundle;

import com.android.quack.R;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolActivity extends Activity {

    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_local);
        cachedThreadPool.submit((Callable<Object>)(new MyTimeoutTask(6000)));
        cachedThreadPool.submit((Callable<Object>)(new MyTimeoutTask(30000)));
        cachedThreadPool.submit((Callable<Object>)(new MyTimeoutTask(3000)));
        cachedThreadPool.submit((Callable<Object>)(new MyTimeoutTask(20000)));
        cachedThreadPool.submit((Callable<Object>)(new MyTimeoutTask(1000)));
    }
}
