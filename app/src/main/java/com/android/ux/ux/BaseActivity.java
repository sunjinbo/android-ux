package com.android.ux.ux;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends Activity {

    protected static final String TAG = "ux";

    private ListView mListView;
    private List<MessageInfo> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mListView = findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
    }

    protected void addMessage(String message, int color) {
        synchronized (this) {
            mData.add(new MessageInfo(message, color));

            Log.d(TAG, message);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    protected void addMessage(String message) {
        addMessage(message, Color.BLACK);
    }

    private BaseAdapter mAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int i) {
            return mData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view;
            if (convertView==null) {
                view = new TextView(BaseActivity.this);
            } else {
                view = convertView;
            }

            if (view instanceof TextView) {
                ((TextView) view).setText(mData.get(i).getMessage());
                ((TextView) view).setTextColor(mData.get(i).getColor());
            }

            return view;
        }
    };

    private static final class MessageInfo {
        private int mColor;
        private String mMessage;

        public MessageInfo(String msg, int color) {
            this.mMessage = msg;
            this.mColor = color;
        }

        public String getMessage() {
            return mMessage;
        }

        public int getColor() {
            return mColor;
        }
    }
}
