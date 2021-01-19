package com.android.quack.surface;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Choreographer;

import com.android.quack.R;

public class ChoreographerActivity extends Activity {

    private static final String TAG = "choreographer";

    ChorRenderThread mRenderThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choreographer);

        mRenderThread = new ChorRenderThread();
        mRenderThread.start();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();

        // if we get here too quickly, the handler might still be null; not dealing with that
        Handler handler = mRenderThread.getHandler();
        handler.sendEmptyMessage(0);
        mRenderThread = null;
    }

    private static class ChorRenderThread extends Thread implements Choreographer.FrameCallback {
        private volatile Handler mHandler;

        @Override
        public void run() {
            setName("ChorRenderThread");

            Looper.prepare();

            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    Log.d(TAG, "got message, quitting");
                    Looper.myLooper().quit();
                }
            };
            Choreographer.getInstance().postFrameCallback(this);

            Looper.loop();
            Log.d(TAG, "looper quit");
            Choreographer.getInstance().removeFrameCallback(this);
        }

        public Handler getHandler() {
            return mHandler;
        }

        @Override
        public void doFrame(long frameTimeNanos) {
            Log.d(TAG, "doFrame " + frameTimeNanos);
            Choreographer.getInstance().postFrameCallback(this);
        }
    }
}
