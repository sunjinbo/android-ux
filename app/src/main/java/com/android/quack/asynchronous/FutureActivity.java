package com.android.quack.asynchronous;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;

import com.android.quack.BaseActivity;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureActivity extends BaseActivity implements Runnable, Callable<String> {

    private boolean mIsDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Thread r = new Thread(this);
        r.start();

        final FutureTask<String> task = new FutureTask<>(this);
        final Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsDestroy = true;
    }

    @Override
    public void run() {
        while (!mIsDestroy) {
            SystemClock.sleep(666);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addMessage("Runnable is ticked!", Color.GREEN);
                }
            });
        }
    }

    @Override
    public String call() throws Exception {
        while (!mIsDestroy) {
            SystemClock.sleep(666);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addMessage("Callable is ticked!", Color.BLUE);
                }
            });
        }

        return null;
    }
}
