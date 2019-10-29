package com.android.quack.surface;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.TextureView;
import android.view.View;

import com.android.quack.R;

import java.util.Random;

public class TextureViewActivity extends Activity implements TextureView.SurfaceTextureListener, View.OnClickListener {

    private TextureView mTextureView;
    private boolean mIsRunning = false;
    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_view);
        mTextureView = findViewById(R.id.texture_view);
        mTextureView.setOnClickListener(this);
        mTextureView.setSurfaceTextureListener(this);
        mRandom = new Random();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mIsRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsRunning) {
                    final Canvas canvas = mTextureView.lockCanvas();
                    canvas.drawColor(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
                    mTextureView.unlockCanvasAndPost(canvas);
                    SystemClock.sleep(222);
                }
            }
        }).start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        mIsRunning = false;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @Override
    public void onClick(View view) {
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat("rotationY", 0.0f, 360.0f, 0.0F);
        PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.5f,1.0f);
        PropertyValuesHolder valuesHolder3 = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.5f,1.0f);
        PropertyValuesHolder valuesHolder4 = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.5f,1.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mTextureView,  valuesHolder,valuesHolder1,valuesHolder3, valuesHolder4);
        objectAnimator.setDuration(5000).start();
    }
}
