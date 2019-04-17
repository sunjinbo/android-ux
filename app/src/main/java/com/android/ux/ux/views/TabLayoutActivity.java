package com.android.ux.ux.views;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ux.ux.R;

public class TabLayoutActivity extends Activity {

    private MyTabLayout mTableLayout;
    private MyTabLayout mCustomTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);

        mTableLayout = findViewById(R.id.tab_layout);
        mTableLayout.setUnboundedRipple(true);

        mCustomTableLayout = findViewById(R.id.custom_tab_layout);
        mCustomTableLayout.setUnboundedRipple(true);

        LayoutInflater layoutInflater = getLayoutInflater();
        for (int i = 0; i < 10; ++i) {
            View customView = layoutInflater.inflate(R.layout.view_custom_tab, null);
            TextView textView = customView.findViewById(R.id.tab);
            textView.setText("tab" + (i + 1) + "");
            textView.setBackgroundColor(Color.argb(255, i * 20 + 10, 128, i * 10 + 10));
            TabLayout.Tab tab = mTableLayout.newTab();
            tab.setText("tab" + (i + 1) + "");
//            tab.setCustomView(customView);
            mTableLayout.addTab(tab);
        }

        for (int i = 0; i < 10; ++i) {
            View customView = layoutInflater.inflate(R.layout.view_custom_tab, null);
            TextView textView = customView.findViewById(R.id.tab);
            textView.setText("tab" + (i + 1) + "");
            textView.setBackgroundColor(Color.argb(255, i * 20 + 10, 128, i * 10 + 10));
            TabLayout.Tab tab = mCustomTableLayout.newTab();
            tab.setCustomView(customView);
            mCustomTableLayout.addTab(tab);
        }
    }
}
