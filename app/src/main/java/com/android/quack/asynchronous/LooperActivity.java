package com.android.quack.asynchronous;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.LogPrinter;

import com.android.quack.BaseActivity;

public class LooperActivity extends BaseActivity implements Runnable, Handler.Callback {

    private Handler mMainThreadHandler;
    private Handler mBackgroundThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Looper.myLooper().setMessageLogging(new LogPrinter(Log.DEBUG, TAG));

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
        Looper.myLooper().setMessageLogging(new LogPrinter(Log.DEBUG, TAG));

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
        Looper.myLooper().setMessageLogging(null);
        Log.d(TAG, "Quit background thread looper.");
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message.what == 0) {
            addMessage("Thread " + Thread.currentThread().getId() + " received a message.", Color.BLUE);
            mMainThreadHandler.sendEmptyMessageDelayed(0, 1000);
        } else {
            mMainThreadHandler.removeMessages(0);
            Looper.myLooper().setMessageLogging(null);
        }

        return false;
    }
}
