package com.android.quack.list;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.quack.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {

    private ListView mListView;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        mListView = findViewById(R.id.list_view);
        mAdapter = new MyAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    public class MyAdapter extends BaseAdapter {

        private List<String> mData = new ArrayList<>();
        private LayoutInflater mLayoutInflater;

        public MyAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
            for (int i = 0; i < 100; ++i) {
                mData.add(i + "");
            }
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("list", "getView() - position" + position + ", convertView = " + ((convertView != null) ? "convertView" : "null"));
            View view = mLayoutInflater.inflate(R.layout.list_view_item, null);
            Button button = view.findViewById(R.id.btn_title);
            button.setText(mData.get(position));
            return view;
        }
    }
}