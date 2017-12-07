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

public class VoiceButton extends CircleImageView implements View.OnClickListener{
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
        this.setOnClickListener(this);
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
                Log.i("info", "onTouchEvent: up");
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        if (isAnimation) {
            returnAnimation();
        }
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
        animSet.setDuration(2000);
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
        ObjectAnimator animator3=ObjectAnimator.ofFloat(this,"translationX",curTranslationX,300f);
        ObjectAnimator animator4=ObjectAnimator.ofFloat(this,"translationY",curTranslationY,400f);
        final AnimatorSet animSet = new AnimatorSet();
        animSet.play(animator1).with(animator2).with(animator3).with(animator4);
        animSet.setDuration(2000);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimation = true;
            }
        });
        animSet.start();
    }
}
