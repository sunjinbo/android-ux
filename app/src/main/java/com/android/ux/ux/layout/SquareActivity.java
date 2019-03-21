package com.android.ux.ux.layout;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.android.ux.ux.R;

public class SquareActivity extends Activity {

    private View topView;
    private View middleView;
    private View bottomView;

    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);

        topView = findViewById(R.id.top_part);
        middleView = findViewById(R.id.middle_part);
        bottomView = findViewById(R.id.bottom_part);

        displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        Log.d("sun", "dpWidht = " + dpWidth);

        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            LinearLayout.LayoutParams topLayoutParams = (LinearLayout.LayoutParams) topView.getLayoutParams();
            final ValueAnimator animator = ValueAnimator.ofInt(displayMetrics.widthPixels, (int)((float)displayMetrics.widthPixels * 0.66f));
            animator.setDuration(666);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    final int current = (int) animator.getAnimatedValue();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) topView.getLayoutParams();
                    params.width = current;
                    params.height = current;
                    topView.setLayoutParams(params);
                }
            });
            animator.start();
            return false;
        }
    });
}