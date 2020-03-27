package com.android.quack.asynchronous;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;

import com.android.quack.BaseActivity;

public class HandlerThreadActivity extends BaseActivity implements Runnable {

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

        new Thread(this).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 释放资源
        mHandlerThread.quit() ;
    }

    @Override
    public void run() {
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    private class MyAsyncTask extends AsyncTask<Void, Integer, Void> {

        public MyAsyncTask() {
        }

        @Override
        protected Void doInBackground(Void... voids) {

            SystemClock.sleep(1000);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addMessage(SystemClock.elapsedRealtime() + " <- AsyncTask");
                }
            });

            if (HandlerThreadActivity.this.isDestroyed() || HandlerThreadActivity.this.isFinishing()) {
                return null;
            }

            MyAsyncTask task = new MyAsyncTask();
            task.execute();

            return null;
        }
    }
}
