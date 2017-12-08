package com.example.qgchat.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.qgchat.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/12/6.
 */

public class VoiceButton extends CircleImageView{
    private static final String TAG = "info";
    private boolean isAnimation=false;

    public VoiceButton(Context context) {
        super(context);
        initView();
    }

    public VoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VoiceButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        this.bringToFront();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setImageResource(R.mipmap.voice_full);
                Log.i("info", "onTouchEvent: down");
                break;
            case MotionEvent.ACTION_UP:
                setImageResource(R.mipmap.voice_empty);
                if (isAnimation) {
                    returnAnimation();
                } else {
                    startAnimation();
                }
                Log.i("info", "onTouchEvent: up");
                break;
        }
        return super.onTouchEvent(event);
    }

    private void returnAnimation() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(this, "scaleY", 0.5f,1f );
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(this, "scaleX", 0.5f,1f);
        float curTranslationX = this.getTranslationX();
        float curTranslationY = this.getTranslationY();
        ObjectAnimator animator3=ObjectAnimator.ofFloat(this,"translationX",curTranslationX,0);
        ObjectAnimator animator4=ObjectAnimator.ofFloat(this,"translationY",curTranslationY,0);
        final AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1).with(animator2).with(animator3).with(animator4);
        animSet.setDuration(600);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = false;
            }
        });
        animSet.start();
    }

    public void startAnimation() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(this, "scaleY", 1f,0.5f );
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(this, "scaleX", 1f,0.5f);
        float curTranslationX = this.getTranslationX();
        float curTranslationY = this.getTranslationY();
        float Y = getBottom()-dip2px(getContext(),120);
        float X = getRight()-dip2px(getContext(),120);
        Log.i(TAG, "startAnimation: "+X);
        Log.i(TAG, "startAnimation: "+Y);
        ObjectAnimator animator3=ObjectAnimator.ofFloat(this,"translationX",curTranslationX,X);
        ObjectAnimator animator4=ObjectAnimator.ofFloat(this,"translationY",curTranslationY,Y);
        final AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1).with(animator2).with(animator3).with(animator4);
        animSet.setDuration(800);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = true;
            }
        });
        animSet.start();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        Log.i(TAG, "dip2px: "+scale);
        return (int) (dpValue * scale + 0.5f);
    }
}
