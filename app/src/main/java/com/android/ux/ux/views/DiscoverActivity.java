package com.android.ux.ux.views;

import android.app.Activity;
import android.os.Bundle;

import com.android.ux.ux.R;

public class DiscoverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        final DiscoverAnimView discoverAnimView = findViewById(R.id.discover_anim_view);
        discoverAnimView.setDecorateImage(getDrawable(R.drawable.discover_decorate));
        discoverAnimView.setDiscoverImage(getDrawable(R.drawable.discover_wifi), false);

        final BindAnimView bindAnimView = findViewById(R.id.bind_anim_view);
        bindAnimView.setBindImage(getDrawable(R.drawable.bind_link));
    }
}
