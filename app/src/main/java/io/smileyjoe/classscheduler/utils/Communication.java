package io.smileyjoe.classscheduler.utils;

import android.view.View;

import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;

import io.smileyjoe.classscheduler.R;

public class Communication {

    public interface Listener{
        void success(@StringRes int messageResId);
        void error(@StringRes int messageResId);
        void error(String message);
    }

    public static void success(View view, @StringRes int messageResId, boolean anchor){
        success(view, view.getContext().getString(messageResId), anchor);
    }

    public static void success(View view, String message, boolean anchor){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);

        if(anchor) {
            snackbar.setAnchorView(view);
        }

        snackbar.show();
    }

    public static void error(View view, @StringRes int messageResId, boolean anchor){
        error(view, view.getContext().getString(messageResId), anchor);
    }

    public static void error(View view, String message, boolean anchor){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);

        if(anchor) {
            snackbar.setAnchorView(view);
        }

        snackbar.setAction(R.string.ok, v -> snackbar.dismiss());
        snackbar.show();
    }

    public static void success(Listener listener, @StringRes int messageResId){
        if(listener != null){
            listener.success(messageResId);
        }
    }

    public static void error(Listener listener, @StringRes int messageResId){
        if(listener != null){
            listener.error(messageResId);
        }
    }

    public static void error(Listener listener, String message){
        if(listener != null){
            listener.error(message);
        }
    }
}
