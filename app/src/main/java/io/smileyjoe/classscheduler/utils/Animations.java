package io.smileyjoe.classscheduler.utils;

import android.app.Activity;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

public class Animations {
    private ArrayList<Animation> mAnimations = new ArrayList<>();
    private int mScreenWidth = 0;
    private int mScreenHeight = 0;

    public static Animations with(Activity activity){
        Animations animations = new Animations();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        animations.mScreenHeight = displayMetrics.heightPixels;
        animations.mScreenWidth = displayMetrics.widthPixels;
        return animations;
    }

    public Animations addAnimation(Animation animation){
        animation.screen(mScreenWidth, mScreenHeight);
        mAnimations.add(animation);
        return this;
    }

    public Animations enter(){
        for(Animation animation:mAnimations){
            animation.enter();
        }

        return this;
    }

    public void exit(){
        for(Animation animation:mAnimations){
            animation.exit();
        }
    }

    public void exit(Activity activity){
        exit();

        new Handler().postDelayed(() -> {
            activity.finish();
        }, Animation.ANIM_DURATION_EXIT - 100);
    }

}
