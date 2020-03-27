package com.android.quack.asynchronous;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public abstract class TimeoutTask extends TimerTask implements Callable<Object> {

    private Timer mTimer;
    private boolean mIsTimeout = false;

    public TimeoutTask(int timeout) {
        mTimer = new Timer();
        mTimer.schedule(this, timeout);
    }

    @Override
    public void run() {
        Log.d("timeout", "task is timeout!");
        mIsTimeout = true;
    }

    public boolean isTimeout() {
        return mIsTimeout;
    }

    private void cancelTimer() {
        mTimer.cancel();
    }

    @Override
    public Object call() throws Exception {
        doWork();
        cancelTimer();
        return new Object();
    }

    public abstract void doWork();
}
