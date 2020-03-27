package com.android.quack.network;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.quack.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketActivity extends BaseActivity {

    private static final String TAG = "socket";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String responseString = scan_http_by_socket("192.168.3.25", 80, "");
                Log.d(TAG, responseString);
            }
        }).start();
    }

    public String scan_http_by_socket(String host, int port, String param) {
        StringBuilder result = new StringBuilder();
        Socket socket = null;
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 0);
            socket.setSoTimeout(0);

            param = TextUtils.isEmpty(param) ? "/" : param;
            String requestString = String.format("GET %s HTTP/1.1\r\nHost: %s\r\n\r\n", param, host);

            OutputStream os = socket.getOutputStream();
            os.write(requestString.getBytes());
            os.flush();

            InputStream is = socket.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            int count = 3;
            while (((len = is.read(bytes)) != -1) && count > 0) {
                count -= 1;
                baos.write(bytes, 0, len);
                result.append(new String(baos.toByteArray()));
                Log.d(TAG, result.toString());
            }

            addMessage(result.toString());

            socket.close();
            socket = null;
        } catch (Exception e) {
            addMessage(e.getMessage());
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                    socket = null;
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return result.toString();
    }
}
