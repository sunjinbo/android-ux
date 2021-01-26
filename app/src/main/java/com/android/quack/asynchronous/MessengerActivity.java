package com.android.quack.asynchronous;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.android.quack.R;

public class MessengerActivity extends Activity {

    private static final int MSG_SAY_HELLO = 1;

    private Messenger mMessenger = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
    }

    public void onBindService(View view) {
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mBindServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void onUnbindService(View view) {
        unbindService(mBindServiceConnection);
    }

    private ServiceConnection mBindServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("srv", "MessengerActivity.onServiceConnected()");
            mMessenger = new Messenger(iBinder);

            try {
                Message msg = Message.obtain(null, MSG_SAY_HELLO, 0, 0);
                mMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("srv", "MessengerActivity.onServiceDisconnected()");
        }
    };
}