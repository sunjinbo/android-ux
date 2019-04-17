package com.android.ux.ux.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class SquareLayout extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private boolean mIsRunning = false;
    private Random mRandom = new Random();

    public SquareLayout(@NonNull Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
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
            final Canvas canvas = getHolder().lockCanvas();
            canvas.drawColor(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
            getHolder().unlockCanvasAndPost(canvas);
            SystemClock.sleep(222);
        }
    }
}
