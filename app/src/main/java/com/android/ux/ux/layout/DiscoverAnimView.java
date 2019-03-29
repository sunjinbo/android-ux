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


public class DiscoverAnimView extends FrameLayout {

    private ImageView mCircle1ImageView;
    private ImageView mCircle2ImageView;
    private ImageView mCircle3ImageView;
    private ImageView mScanImageView;
    private SquareImageView mDecorateImageView;

    public DiscoverAnimView(Context context) {
        super(context);
        initView(context);
    }

    public DiscoverAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DiscoverAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setDecorateImage(Drawable drawable) {
        if (drawable == null) {
            mDecorateImageView.setVisibility(INVISIBLE);
        } else {
            mDecorateImageView.setVisibility(VISIBLE);
            mDecorateImageView.setImageDrawable(drawable);
        }
    }

    public void setDiscoverImage(Drawable drawable, boolean rotate) {
        if (drawable == null) {
            mScanImageView.setVisibility(INVISIBLE);
        } else {
            mScanImageView.setVisibility(VISIBLE);
            mScanImageView.setImageDrawable(drawable);
            if (rotate) {
                final RotateAnimation scanAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scanAnim.setDuration(2000);
                scanAnim.setInterpolator(new LinearInterpolator());
                scanAnim.setRepeatCount(-1);
                mScanImageView.setAnimation(scanAnim);
                scanAnim.start();
            } else {
                if (mScanImageView.getAnimation() != null) {
                    mScanImageView.getAnimation().cancel();
                }
            }
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        addView(inflater.inflate(R.layout.view_discover_anim, this, false));

        mCircle1ImageView = findViewById(R.id.iv_add_firewall_circle1);
        mCircle2ImageView = findViewById(R.id.iv_add_firewall_circle2);
        mCircle3ImageView = findViewById(R.id.iv_add_firewall_circle3);
        mScanImageView = findViewById(R.id.iv_add_firewall_scan);
        mDecorateImageView = findViewById(R.id.iv_add_firewall_decorate);

        final RotateAnimation circle1Anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        circle1Anim.setDuration(15000);
        circle1Anim.setInterpolator(new LinearInterpolator());
        circle1Anim.setRepeatCount(-1);
        mCircle1ImageView.setAnimation(circle1Anim);
        circle1Anim.start();

        final RotateAnimation circle2Anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        circle2Anim.setDuration(9000);
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
    }
}
