package com.android.quack.network;

import android.os.Bundle;

import com.android.quack.BaseActivity;

public class NetworkStatusActivity extends BaseActivity implements NetworkStatusMonitor.Callback {

    private NetworkStatusMonitor mMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMonitor = new NetworkStatusMonitor(this, this);
        mMonitor.register();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMonitor.unregister();
    }

    @Override
    public void onStatusChanged(final NetworkStatusInfo networkStatusInfo) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (networkStatusInfo != null) {
                    addMessage(networkStatusInfo.toString());
                } else {
                    addMessage("no network!");
                }
            }
        });
    }
}
