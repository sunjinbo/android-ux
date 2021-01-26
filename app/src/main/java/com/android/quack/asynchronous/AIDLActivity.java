package com.android.quack.asynchronous;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.android.quack.IPlus;
import com.android.quack.R;

public class AIDLActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
    }

    public void onBindService(View view) {
        Intent intent = new Intent(this, AIDLService.class);
        bindService(intent, mBindServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void onUnbindService(View view) {
        unbindService(mBindServiceConnection);
    }

    private ServiceConnection mBindServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("srv", "AIDLActivity.onServiceConnected()");
            IPlus p = IPlus.Stub.asInterface(iBinder);
            try {
                int c = p.plus(3, 4);
                Log.d("srv", "p.plus(3, 4) = " + c);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("srv", "AIDLActivity.onServiceDisconnected()");
        }
    };
}
