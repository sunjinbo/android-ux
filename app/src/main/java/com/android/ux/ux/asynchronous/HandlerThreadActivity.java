package com.android.ux.ux.asynchronous;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;

import com.android.ux.ux.BaseActivity;

public class HandlerThreadActivity extends BaseActivity {

    private HandlerThread mHandlerThread;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();

        mHandler = new Handler(mHandlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addMessage(SystemClock.elapsedRealtime() + "");
                    }
                });

                mHandler.sendEmptyMessageDelayed(0, 666);

                return false;
            }
        });
        mHandler.sendEmptyMessageDelayed(0, 666);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 释放资源
        mHandlerThread.quit() ;
    }
}
