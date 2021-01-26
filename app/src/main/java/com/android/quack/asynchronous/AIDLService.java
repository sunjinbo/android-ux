package com.android.quack.asynchronous;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.View;

import com.android.quack.IPlus;

public class AIDLService extends Service {
    public AIDLService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new AIDLBinder();
    }

    public class AIDLBinder extends IPlus.Stub {

        @Override
        public int plus(int a, int b) throws RemoteException {
            return a + b;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    }
}
