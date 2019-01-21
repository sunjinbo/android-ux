package com.android.ux.ux.asynchronous;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.android.ux.ux.BaseActivity;

public class LooperActivity extends BaseActivity implements Runnable, Handler.Callback {

    private Handler mMainThreadHandler;
    private Handler mBackgroundThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainThreadHandler = new Handler(this);
        mMainThreadHandler.sendEmptyMessageDelayed(0, 1000);

        new Thread(this).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainThreadHandler.sendEmptyMessage(1);
        mBackgroundThreadHandler.sendEmptyMessage(1);
    }

    @Override
    public void run() {
        Looper.prepare();
        mBackgroundThreadHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 0) {
                    addMessage("Thread " + Thread.currentThread().getId() + " received a message.", Color.RED);
                    mBackgroundThreadHandler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    Looper.myLooper().quitSafely();
                }
                return false;
            }
        });
        mBackgroundThreadHandler.sendEmptyMessageDelayed(0, 1000);
        Looper.loop();
        Log.d(TAG, "Quit background thread looper.");
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.what == 0) {
            addMessage("Thread " + Thread.currentThread().getId() + " received a message.", Color.BLUE);
            mMainThreadHandler.sendEmptyMessageDelayed(0, 1000);
        } else {
            mMainThreadHandler.removeMessages(0);
        }

        return false;
    }
}
