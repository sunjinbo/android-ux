package com.android.ux.ux.surface;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.TextureView;

import com.android.ux.ux.R;

import java.util.Random;

public class TextureViewActivity extends Activity implements TextureView.SurfaceTextureListener {

    private TextureView mTextureView;
    private boolean mIsRunning = false;
    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_texture_view);
        mTextureView = findViewById(R.id.texture_view);
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
}
