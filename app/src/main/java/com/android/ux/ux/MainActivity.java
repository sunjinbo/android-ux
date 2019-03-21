package com.android.ux.ux;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

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
        MainBean bean = new MainBean();
        MainBean.CategoryBean surfaceBean = new MainBean.CategoryBean();
        surfaceBean.category_name = "asynchronous";
        MainBean.ActivityBean surfaceViewActivityBean = new MainBean.ActivityBean("Handler+Looper+Message", "com.android.ux.ux.asynchronous.LooperActivity");
        MainBean.ActivityBean textureViewActivityBean = new MainBean.ActivityBean("Runnable+Callable", "com.android.ux.ux.asynchronous.FutureActivity");
        surfaceBean.category_list.add(surfaceViewActivityBean);
        surfaceBean.category_list.add(textureViewActivityBean);
        bean.all.add(surfaceBean);
        return bean;
    }

}
