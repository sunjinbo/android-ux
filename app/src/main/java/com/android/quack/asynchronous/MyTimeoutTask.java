package com.android.quack.asynchronous;

import android.os.SystemClock;
import android.util.Log;

public class MyTimeoutTask extends TimeoutTask {

    public MyTimeoutTask(int timeout) {
        super(timeout);
    }

    @Override
    public void doWork() {
        SystemClock.sleep(15000);

        if (isTimeout()) {
            Log.d("timeout", "good task!");
        } else {
            Log.d("timeout", "bad task!");
        }
    }
}
