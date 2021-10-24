package io.smileyjoe.classscheduler.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import java.util.ArrayList;

import io.smileyjoe.classscheduler.R;

public class LoadingData {

    private class Data{
        private View mView;
        private Drawable mBackground;

        public Data(View view) {
            mView = view;
            mBackground = view.getBackground();
        }

        public View getView() {
            return mView;
        }

        public Drawable getBackground() {
            return mBackground;
        }
    }

    private ArrayList<Data> mData;
    private Drawable mBgTextView;

    private LoadingData(){}

    public static LoadingData init(){
        return new LoadingData();
    }

    public LoadingData add(TextView view, int numberChars){
        view.setText(getEmptyString(numberChars));
        add(view);
        return this;
    }

    private void add(View view){
        if(mData == null){
            mData = new ArrayList<>();
        }
        mData.add(new Data(view));
    }

    private String getEmptyString(int charCount){
        if(charCount > 0) {
            return new String(new char[charCount]).replace("\0", " ");
        } else {
            return null;
        }
    }

    public void update(boolean loading){
        if(loading){
            showLoading();
        } else {
            showNormal();
        }
    }

    private void showLoading(){
        for(Data item:mData){
            View view = item.getView();
            if(view instanceof TextView){
                view.setBackground(getBgTextView(view.getContext()));
            }
        }
    }

    private void showNormal(){
        for(Data item:mData){
            View view = item.getView();
            if(view instanceof TextView){
                view.setBackground(item.getBackground());
            }
        }
    }

    private Drawable getBgTextView(Context context){
        if(mBgTextView == null){
            mBgTextView = ContextCompat.getDrawable(context, R.drawable.bg_text_loading);
        }

        return mBgTextView;
    }

}
