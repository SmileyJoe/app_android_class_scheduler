package io.smileyjoe.classscheduler.utils;

import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;

import java.util.Arrays;
import java.util.List;

public class Animation {

    public static final int ANIM_DURATION_ENTER = 500;
    public static final int ANIM_DURATION_EXIT = 250;

    public enum Type {
        LEFT, TOP, RIGHT, BOTTOM, FADE
    }

    protected List<View> mViews;
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    private Type mTypeEntry;
    private Type mTypeExit;

    public static Animation on(View... views) {
        Animation animation = new Animation();
        animation.mViews = Arrays.asList(views);
        return animation;
    }

    public Animation entry(Type type) {
        mTypeEntry = type;
        return this;
    }

    public Animation exit(Type type) {
        mTypeExit = type;
        return this;
    }

    public Animation screen(int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;
        return this;
    }

    public void enter(){
        for(View view:mViews) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    view.setAlpha(0);
                    ViewPropertyAnimator animator = view.animate()
                            .alpha(1)
                            .setDuration(ANIM_DURATION_ENTER)
                            .setStartDelay(100);
                    switch (mTypeEntry) {
                        case TOP:
                        case BOTTOM:
                            view.setTranslationY(getOffset(mTypeEntry, view));
                            animator.translationY(0);
                            break;
                        case LEFT:
                        case RIGHT:
                            view.setTranslationX(getOffset(mTypeEntry, view));
                            animator.translationX(0);
                            break;
                        case FADE:
                            // do nothing, the view will just fade //
                            break;
                    }
                    animator.start();
                }
            });
        }
    }

    public void exit() {
        for(View view:mViews) {
            if (mTypeExit == null) {
                mTypeExit = mTypeEntry;
            }
            view.setAlpha(1);
            ViewPropertyAnimator animator = view.animate()
                    .alpha(0)
                    .setDuration(ANIM_DURATION_EXIT)
                    .setStartDelay(100);
            switch (mTypeExit) {
                case TOP:
                case BOTTOM:
                    animator.translationY(getOffset(mTypeExit, view));
                    break;
                case LEFT:
                case RIGHT:
                    animator.translationX(getOffset(mTypeExit, view));
                    break;
                case FADE:
                    // do nothing, the view will just fade //
                    break;
            }
            animator.start();
        }
    }

    private float getOffset(Type type, View view) {
        switch (type) {
            case TOP:
                return 0 - view.getY();
            case LEFT:
                return 0 - (view.getX() + view.getWidth());
            case RIGHT:
                return mScreenWidth;
            case BOTTOM:
                return mScreenHeight;
            case FADE:
            default:
                return 0;
        }
    }
}
