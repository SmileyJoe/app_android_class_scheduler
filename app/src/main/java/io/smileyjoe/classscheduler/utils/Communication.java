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

    public static void success(View view, @StringRes int messageResId){
        success(view, view.getContext().getString(messageResId));
    }

    public static void success(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAnchorView(view).show();
    }

    public static void error(View view, @StringRes int messageResId){
        error(view, view.getContext().getString(messageResId));
    }

    public static void error(View view, String message){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAnchorView(view);

        snackbar.setAction(R.string.ok, v -> snackbar.dismiss());
        snackbar.show();
    }
}
