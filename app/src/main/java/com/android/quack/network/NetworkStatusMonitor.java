package com.android.quack.network;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

public class NetworkStatusMonitor {
    private ConnectivityManager mConnectivityManager;
    private Activity mActivity;
    private Callback mCallback;
    NetworkUtils.WifiUtils mWifiUtils;

    public NetworkStatusMonitor(Activity activity, Callback callback) {
        mActivity = activity;
        mCallback = callback;
        mConnectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiUtils = new NetworkUtils.WifiUtils(mActivity.getApplicationContext());
    }

    public void register() {
        if (mActivity != null) {
            mActivity.registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    public void unregister() {
        if (mActivity != null) {
            mActivity.unregisterReceiver(mReceiver);
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (!TextUtils.isEmpty(action) && action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                final NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
                NetworkStatusInfo statusInfo;
                if (info != null) {
                    if (info.getType() == 1) {
                        statusInfo = new NetworkStatusInfo(mWifiUtils.getSsid(), mWifiUtils.getBSSID(), info.getState().ordinal());
                    } else if (info.getType() == 0) {
                        statusInfo = new NetworkStatusInfo(info.getState().ordinal());
                    } else {
                        statusInfo = new NetworkStatusInfo();
                    }
                } else {
                    statusInfo = new NetworkStatusInfo();
                }

                if (mCallback != null) {
                    mCallback.onStatusChanged(statusInfo);
                }
            }
        }
    };

    public interface Callback {
        void onStatusChanged(NetworkStatusInfo networkStatusInfo);
    }
}
