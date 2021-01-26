package com.android.quack.asynchronous;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.quack.R;

public class ServiceActivity extends Activity implements AsyncService.Callback {

    private TextView mCountTextView;
    private AsyncService.AsyncBinder mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        mCountTextView = findViewById(R.id.tv_service_count);
    }

    public void onBindService(View view) {
        Intent intent = new Intent(this, AsyncService.class);
        bindService(intent, mBindServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void onUnbindService(View view) {
        unbindService(mBindServiceConnection);
    }

    public void onStartService(View view) {
        startService(new Intent(this, AsyncService.class));
    }

    public void onStopService(View view) {
        stopService(new Intent(this, AsyncService.class));
    }

    private ServiceConnection mBindServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("srv", "ServiceActivity.onServiceConnected()");
            if (iBinder instanceof AsyncService.AsyncBinder) {
                mBinder = ((AsyncService.AsyncBinder) iBinder);
                if (mBinder != null) {
                    mBinder.getService().setCallback(ServiceActivity.this);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("srv", "ServiceActivity.onServiceDisconnected()");
            if (mBinder != null) {
                mBinder.getService().setCallback(null);
                mBinder = null;
            }
        }
    };

    @Override
    public void onValueChanged(final int value) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCountTextView.setText(value + "");
            }
        });
    }
}
