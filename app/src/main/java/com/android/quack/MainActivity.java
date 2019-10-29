package com.android.quack;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    private ListView mListView;
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.list_view);
        mAdapter = new MainAdapter(this, getDataFromAssets());
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.canGoBack()) {
            finish();
        }
    }

    private MainBean getDataFromAssets() {
        String textString = getTextFromAssets();
        Gson gson = new Gson();
        MainBean bean = gson.fromJson(textString, MainBean.class);
        return bean;
    }

    private String getTextFromAssets() {
        String textString = "";
        AssetManager am = getAssets();
        try {
            InputStream is = am.open("quack.json");

            String code = getCode(is);

            is= am.open("quack.json");

            BufferedReader br = new BufferedReader(new InputStreamReader(is, code));

            String line = br.readLine();
            int i = 0;
            while(line != null){
                textString += line;
                line = br.readLine();
                i++;
                if (i == 200) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return textString;
    }

    public String getCode(InputStream is){
        try {
            BufferedInputStream bin = new BufferedInputStream(is);
            int p;

            p = (bin.read() << 8) + bin.read();

            String code = null;

            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
            }
            is.close();
            return code;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
