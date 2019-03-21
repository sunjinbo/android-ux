package com.android.ux.ux;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.List;

public class MainAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<MainBean.CategoryBean> mData;
    private LayoutInflater mInflater;
    private MainBean.CategoryBean mCategoryBean; // 当为NULL表示该页面为一级页面

    public MainAdapter(Activity activity, MainBean data) {
        mActivity = activity;
        mData = data.all;
        mInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return mCategoryBean == null ? mData.size() : mCategoryBean.category_list.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategoryBean == null ? mData.get(position) : mCategoryBean.category_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_item, parent, false);
            holder = new ViewHolder();

            holder.title = convertView.findViewById(R.id.btn_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mCategoryBean == null) {
            final MainBean.CategoryBean bean = mData.get(position);
            holder.title.setText(bean.category_name);
        } else {
            final String name = mCategoryBean.category_list.get(position).name;
            holder.title.setText(name);
        }

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCategoryBean == null) {
                    if (mData.get(position).category_list.size() > 0) {
                        mCategoryBean = mData.get(position);
                        notifyDataSetChanged();
                    }
                } else {
                    String activity = mCategoryBean.category_list.get(position).activity;
                    if (!TextUtils.isEmpty(activity)) {
                        ComponentName cn = new ComponentName(mActivity, activity);
                        Intent intent = new Intent() ;
                        intent.setComponent(cn) ;
                        mActivity.startActivity(intent);
                    }
                }
            }
        });

        return convertView;
    }

    public boolean canGoBack() {
        if (mCategoryBean != null) {
            mCategoryBean = null;
            notifyDataSetChanged();
            return false;
        }
        return true;
    }

    private class ViewHolder {
        public Button title;
    }

}
