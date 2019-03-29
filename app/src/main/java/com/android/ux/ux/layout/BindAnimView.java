package com.android.ux.ux.layout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.ux.ux.R;

public class BindAnimView extends FrameLayout {

    private ImageView mCircle1ImageView;
    private ImageView mCircle2ImageView;
    private ImageView mCircle3ImageView;
    private ImageView mDot1ImageView;
    private ImageView mDot2ImageView;
    private ImageView mDot3ImageView;
    private ImageView mScanImageView;

    public BindAnimView(Context context) {
        super(context);
        initView(context);
    }

    public BindAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BindAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setBindImage(Drawable drawable) {
        if (drawable == null) {
            mScanImageView.setVisibility(INVISIBLE);
        } else {
            mScanImageView.setVisibility(VISIBLE);
            mScanImageView.setImageDrawable(drawable);
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        addView(inflater.inflate(R.layout.view_bind_anim, this, false));

        mCircle1ImageView = findViewById(R.id.iv_add_firewall_circle1);
        mCircle2ImageView = findViewById(R.id.iv_add_firewall_circle2);
        mCircle3ImageView = findViewById(R.id.iv_add_firewall_circle3);
        mDot1ImageView = findViewById(R.id.iv_add_firewall_dot1);
        mDot2ImageView = findViewById(R.id.iv_add_firewall_dot2);
        mDot3ImageView = findViewById(R.id.iv_add_firewall_dot3);
        mScanImageView = findViewById(R.id.iv_add_firewall_scan);

        final RotateAnimation circle1Anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        circle1Anim.setDuration(6000);
        circle1Anim.setInterpolator(new LinearInterpolator());
        circle1Anim.setRepeatCount(-1);
        mCircle1ImageView.setAnimation(circle1Anim);
        circle1Anim.start();

        final RotateAnimation circle2Anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        circle2Anim.setDuration(6000);
        circle2Anim.setInterpolator(new LinearInterpolator());
        circle2Anim.setRepeatCount(-1);
        mCircle2ImageView.setAnimation(circle2Anim);
        circle2Anim.start();

        final RotateAnimation circle3Anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        circle3Anim.setDuration(6000);
        circle3Anim.setInterpolator(new LinearInterpolator());
        circle3Anim.setRepeatCount(-1);
        mCircle3ImageView.setAnimation(circle3Anim);
        circle3Anim.start();

        final RotateAnimation dot1Anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        dot1Anim.setDuration(9000);
        dot1Anim.setInterpolator(new LinearInterpolator());
        dot1Anim.setRepeatCount(-1);
        mDot1ImageView.setAnimation(dot1Anim);
        dot1Anim.start();

        final RotateAnimation dot2Anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        dot2Anim.setDuration(7200);
        dot2Anim.setInterpolator(new LinearInterpolator());
        dot2Anim.setRepeatCount(-1);
        mDot2ImageView.setAnimation(dot2Anim);
        dot2Anim.start();

        final RotateAnimation dot3Anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        dot3Anim.setDuration(8000);
        dot3Anim.setInterpolator(new LinearInterpolator());
        dot3Anim.setRepeatCount(-1);
        mDot3ImageView.setAnimation(dot3Anim);
        dot3Anim.start();
    }
}
