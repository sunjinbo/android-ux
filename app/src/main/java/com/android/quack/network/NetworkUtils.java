package com.android.quack.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtils {


    private static final String TAG = "NetworkUtils";

    private static final int NET_TYPE_3GWAP = 2;

    private static final int NET_TYPE_3GNET = 3;

    private static final int NET_TYPE_2GWAP = 4;

    private static final int NET_TYPE_2GNET = 5;

    private static final int NETWORK_TYPE_EVDO_B = 12;

    private static final int NETWORK_TYPE_LTE = 13;

    private static final int NETWORK_TYPE_EHRPD = 14;

    private static final int NETWORK_TYPE_HSPAP = 15;

    private static final String CHINA_MOBILE_46000 = "46000";

    private static final String CHINA_MOBILE_46002 = "46002";

    private static final String CHINA_MOBILE_46007 = "46007";

    public static int getNetType(Context context) {
        int netType = -1;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String extraInfo = info.getExtraInfo();
//			LogUtil.d(TAG, "extraInfo=" + extraInfo);
            int type = info.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                netType = 1;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                int subType = info.getSubtype();
                switch (subType) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        netType = get2GApnConfig(extraInfo);
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case NETWORK_TYPE_EVDO_B:
                    case NETWORK_TYPE_LTE:
                    case NETWORK_TYPE_EHRPD:
                    case NETWORK_TYPE_HSPAP:
                        netType = get3GApnConfig(extraInfo);
                }
            }
        }
        return netType;
    }

    private static int get2GApnConfig(String extraInfo) {
        if (extraInfo != null) {
            String info = extraInfo.toLowerCase();
            if (info.contains("wap")) {
                return NET_TYPE_2GWAP;
            }
        }

        return NET_TYPE_2GNET;
    }

    private static int get3GApnConfig(String extraInfo) {
        if (extraInfo != null) {
            String info = extraInfo.toLowerCase();
            if (info.contains("wap")) {
                return NET_TYPE_3GWAP;
            }
        }

        return NET_TYPE_3GNET;
    }

    public static boolean isMobileWap(Context context) {
        int netType = getNetType(context);
        return NET_TYPE_2GWAP == netType || NET_TYPE_3GWAP == netType;
    }

    /**
     * 是否移动sim卡
     *
     * @param context
     * @return
     */
    public static boolean isMobileCMCC(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telephonyManager.getSimOperator();
        if (!TextUtils.isEmpty(operator)
                && (operator.equals(CHINA_MOBILE_46000) || operator.equals(CHINA_MOBILE_46002) || operator
                .equals(CHINA_MOBILE_46007))) {
            return true;
        }

        String subscribeId = telephonyManager.getSubscriberId();
        return !TextUtils.isEmpty(subscribeId)
                && (subscribeId.startsWith(CHINA_MOBILE_46000)
                || subscribeId.startsWith(CHINA_MOBILE_46002)
                || subscribeId.startsWith(CHINA_MOBILE_46007));

    }

    public static boolean isConnectivityNetAvailable(Context context) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected() && info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean ping() {
        Process process = null;
        PingWorker worker = null;
        try {
            process = Runtime.getRuntime().exec("ping -c 3 -w 100 www.baidu.com");
            worker = new PingWorker(process);
            worker.start();
            worker.join(1000 * 3);
            if (worker.exit != null && worker.exit == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            if (worker != null) {
                worker.interrupt();
            }
            Thread.currentThread().interrupt();
            return false;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    static class PingWorker extends Thread {
        private final Process process;
        private Integer exit = -1;

        public PingWorker(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            try {
                exit = process.waitFor();
            } catch (InterruptedException e) {
                return;
            }
        }
    }


    public interface ICouldSurfTheInternet {
        void networkOK();

        void networkError();
    }

    public static boolean isWifiEnable(Context context) {
        // 取得WifiManager对象
        WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * Check whether WIFI is available.
     *
     * @param context Application context
     * @return <code>true</code> if WIFI available, <code>false</code> otherwise
     */
    public static boolean isWifiAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            NetworkInfo networkInfo = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network[] networks = connectivity.getAllNetworks();
                if (networks == null) {
                    return false;
                }
                for (Network network : networks) {
                    networkInfo = connectivity.getNetworkInfo(network);
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        break;
                    } else {
                        networkInfo = null;
                    }
                }
            } else {
                networkInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            }
            return networkInfo != null && networkInfo.isConnected();
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * Check whether network is available.
     *
     * @param context Application context
     * @return <code>true</code> if network available, <code>false</code> otherwise
     */
    public static boolean isNetworkAvailableOld(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network[] networks = connectivity.getAllNetworks();
                if (networks == null) {
                    return false;
                }
                for (Network network : networks) {
                    if (connectivity.getNetworkInfo(network).isConnected()) {
                        return true;
                    }
                }
            } else {
                NetworkInfo[] networkInfoArr = connectivity.getAllNetworkInfo();
                if (networkInfoArr == null) {
                    return false;
                }
                for (NetworkInfo networkInfo : networkInfoArr) {
                    if (networkInfo.isConnected()) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return false;
            }
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前连入的wifi的SSID
     */
    public static String getConnectWifiSsid(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (isWifiAvailable(context)) {
            return wifiInfo.getSSID();
        } else {
            return "";
        }
    }

    /**
     * 获取当前连入的wifi信息,返回的ssid没有""
     */
    public static String getConnectWifiSsidFormat(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (isWifiAvailable(context)) {
            return wifiInfo.getSSID().replace("\"", "");
        } else {
            return "";
        }
    }

    /**
     * 获取当前wifi列表
     */
    public List<ScanResult> getWifiList(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        List<ScanResult> list = wifiManager.getScanResults();
        for (ScanResult scanResult : list) {
            if (!TextUtils.isEmpty(scanResult.SSID.trim())) {
                list.add(scanResult);
            }
        }
        return list;
    }

    public static boolean is5GHz(int freq) {
        return freq > 4900 && freq < 5900;
    }

    public static int getChannelAccordingToFreq(int freq) {
        int channel = -1;
        switch (freq) {
            case 2412:
                channel = 1;
                break;
            case 2417:
                channel = 2;
                break;
            case 2422:
                channel = 3;
                break;
            case 2427:
                channel = 4;
                break;
            case 2432:
                channel = 5;
                break;
            case 2437:
                channel = 6;
                break;
            case 2442:
                channel = 7;
                break;
            case 2447:
                channel = 8;
                break;
            case 2452:
                channel = 9;
                break;
            case 2457:
                channel = 10;
                break;
            case 2462:
                channel = 11;
                break;
            case 2467:
                channel = 12;
                break;
            case 2472:
                channel = 13;
                break;
            case 2484:
                channel = 14;
                break;
            case 5745:
                channel = 149;
                break;
            case 5765:
                channel = 153;
                break;
            case 5785:
                channel = 157;
                break;
            case 5805:
                channel = 161;
                break;
            case 5825:
                channel = 165;
                break;
            default:
                break;
        }
        return channel;
    }

    /**
     * 常用wifi相关的工具管理类
     */
    public static class WifiUtils {
        // 定义WifiManager对象
        private WifiManager mWifiManager;
        // 扫描出的网络连接列表
        private List<ScanResult> mWifiList;
        // 网络连接列表
        private List<WifiConfiguration> mWifiConfiguration;
        // 定义一个WifiLock
        private WifiManager.WifiLock mWifiLock;

        // 构造器
        public WifiUtils(Context context) {
            // 取得WifiManager对象
            mWifiManager = (WifiManager) context.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
        }

        public boolean isWifiEnable() {
            return mWifiManager.isWifiEnabled();
        }

        // 打开WIFI
        public void openWifi() {
            if (!mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(true);
            }
        }

        // 关闭WIFI
        public void closeWifi() {
            if (mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(false);
            }
        }

        // 检查当前WIFI状态
        public int checkState() {
            return mWifiManager.getWifiState();
        }

        // 锁定WifiLock
        public void acquireWifiLock() {
            mWifiLock.acquire();
        }

        // 解锁WifiLock
        public void releaseWifiLock() {
            // 判断时候锁定
            if (mWifiLock.isHeld()) {
                mWifiLock.acquire();
            }
        }

        // 创建一个WifiLock
        public void creatWifiLock() {
            mWifiLock = mWifiManager.createWifiLock("Test");
        }

        // 得到配置好的网络
        public List<WifiConfiguration> getConfigurationList() {
            return mWifiConfiguration;
        }

        public boolean connectConfigurationByNetworkId(int id) {
            return mWifiManager.enableNetwork(id,
                    true);
        }

        // 指定配置好的网络进行连接
        public void connectConfiguration(int index) {
            // 索引大于配置好的网络索引返回
            if (index > mWifiConfiguration.size()) {
                return;
            }
            // 连接配置好的指定ID的网络
            mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                    true);
        }

        public void startScan() {
            mWifiManager.startScan();
            // 得到扫描结果
            mWifiList = mWifiManager.getScanResults();
            // 得到配置好的网络连接
            mWifiConfiguration = mWifiManager.getConfiguredNetworks();
        }

        // 得到网络列表
        public List<ScanResult> getWifiList() {
            return mWifiList;
        }

        // 查看扫描结果
        public StringBuilder lookUpScan() {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < mWifiList.size(); i++) {
                stringBuilder.append("Index_").append(Integer.toString(i + 1)).append(":");
                // 将ScanResult信息转换成一个字符串包
                // 其中把包括：BSSID、SSID、capabilities、frequency、level
                stringBuilder.append((mWifiList.get(i)).toString());
                stringBuilder.append("/n");
            }
            return stringBuilder;
        }

        // 得到MAC地址
        public String getMacAddress() {
            return mWifiManager.getConnectionInfo().getMacAddress();
        }

        // 得到接入点的ssid
        public String getSsid() {
            return mWifiManager.getConnectionInfo().getSSID();
        }

        // 得到接入点的BSSID
        public String getBSSID() {
            return mWifiManager.getConnectionInfo().getBSSID();
        }

        // 得到IP地址
        public int getIPAddress() {
            return mWifiManager.getConnectionInfo().getIpAddress();
        }

        // 得到连接的ID
        public int getNetworkId() {
            return mWifiManager.getConnectionInfo().getNetworkId();
        }

        // 得到WifiInfo的所有信息包
        public String getWifiInfo() {
            return mWifiManager.getConnectionInfo().toString();
        }

        public boolean ping() {
            return mWifiManager.pingSupplicant();
        }

        public WifiConfiguration getWifiConfig(String ssid) {
            for (WifiConfiguration config : mWifiConfiguration) {
                if (config.SSID.equals(ssid)) {
                    return config;
                }
            }
            return null;
        }

        // 添加一个网络并连接
        public boolean addNetwork(WifiConfiguration wcg) {
            int wcgID = mWifiManager.addNetwork(wcg);
            return mWifiManager.enableNetwork(wcgID, true);
        }

        // 断开指定ID的网络
        public void disconnectWifi(int netId) {
            mWifiManager.disableNetwork(netId);
            mWifiManager.disconnect();
        }

        /**
         * 获取相应的ssid的热点的配置信息(连接不上带密码的wifi，需进一步调试)
         *
         * @param type 分3种情况，1、无密码 2、wep方式 3、wpa方式
         */
        public WifiConfiguration createWifiInfo(String SSID, String password, int type) {
            WifiConfiguration config = new WifiConfiguration();
            config.allowedAuthAlgorithms.clear();
            config.allowedGroupCiphers.clear();
            config.allowedKeyManagement.clear();
            config.allowedPairwiseCiphers.clear();
            config.allowedProtocols.clear();
            config.SSID = "\"" + SSID + "\"";
            config.status = WifiConfiguration.Status.ENABLED;

            removeNetwork(SSID);

            if (type == 1) {//无密码方式
                config.wepKeys[0] = "\"" + "\"";
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
            }
            if (type == 2) {//WEP方式
                config.hiddenSSID = true;
                config.wepKeys[0] = "\"" + password + "\"";
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
            }
            if (type == 3) {//WPA方式
                config.hiddenSSID = true;
                config.preSharedKey = "\"" + password + "\"";
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            }
            return config;
        }

        public void removeNetwork(String Ssid) {
            WifiConfiguration tempConfig = this.isExists(Ssid);
            if (tempConfig != null) {
                mWifiManager.removeNetwork(tempConfig.networkId);
            }
        }

        private WifiConfiguration isExists(String SSID) {
            List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
            if (existingConfigs != null) {
                for (WifiConfiguration existingConfig : existingConfigs) {
                    if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                        return existingConfig;
                    }
                }
            }
            return null;
        }
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
