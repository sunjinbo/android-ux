package com.android.quack.network;

public class NetworkStatusInfo {
    public enum NetworkType {
        WiFi, CellularNetwork, Other
    }

    private NetworkType mType;
    private String mSsid = "";
    private String mBssid = "";
    private int mState = -1;

    public NetworkStatusInfo() {
        this.mType = NetworkType.Other;
    }

    public NetworkStatusInfo(int state) {
        this.mType = NetworkType.CellularNetwork;
        this.mState = state;
    }

    public NetworkStatusInfo(String ssid, String bssid, int state) {
        this.mType = NetworkType.WiFi;
        this.mSsid = ssid;
        this.mBssid = bssid;
        this.mState = state;
    }

    @Override
    public String toString() {
        return "type:" + mType.toString() + ", ssid:" + mSsid + ", bssid:" + mBssid + ", state:" + mState;
    }
}
