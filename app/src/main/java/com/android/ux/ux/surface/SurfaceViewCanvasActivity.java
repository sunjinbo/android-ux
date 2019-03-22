package com.android.ux.ux.surface;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.ux.ux.R;

import java.util.Random;

public class SurfaceViewCanvasActivity extends Activity implements SurfaceHolder.Callback, Runnable {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private boolean mIsRunning = false;
    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view_canvas);
        mRandom = new Random();
        mSurfaceView = findViewById(R.id.surface_view);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mIsRunning = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mIsRunning = false;
    }

    @Override
    public void run() {
        // 可以在一个独立的线程中进行绘制，不会影响主线程
        while (mIsRunning) {
            final Canvas canvas = mSurfaceHolder.lockCanvas();
            canvas.drawColor(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
            mSurfaceHolder.unlockCanvasAndPost(canvas);
            SystemClock.sleep(222);
        }
    }
}
