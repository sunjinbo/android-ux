package com.android.quack.asynchronous;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class AsyncService extends Service implements Runnable {

    private AsyncBinder mBinder;
    private Callback mCallback;
    private boolean mIsServiceRunning = false;
    private int mCount = 0;

    public AsyncService() {
        Log.d("srv", "AsyncService.AsyncService()");
        mBinder = new AsyncBinder(this);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("srv", "AsyncService.onBind()");
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.d("srv", "AsyncService.onCreate()");
        super.onCreate();
        mIsServiceRunning = true;
        new Thread(this).start();
    }

    @Override
    public int onStartCommand(Intent intent, int startId, int flags) {
        Log.d("srv", "AsyncService.onStartCommand()");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("srv", "AsyncService.onDestroy()");
        super.onDestroy();
        mIsServiceRunning = false;
    }

    @Override
    public void run() {
        while (mIsServiceRunning) {
            SystemClock.sleep(666);
            mCount += 1;
            if (mCallback != null) {
                mCallback.onValueChanged(mCount);
            }
        }
    }

    public class AsyncBinder extends Binder {

        private AsyncService mService;

        public AsyncBinder(AsyncService service) {
            this.mService = service;
        }

        public AsyncService getService() {
            return this.mService;
        }
    }

    public interface Callback {
        void onValueChanged(int value);
    }
}
