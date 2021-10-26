package io.smileyjoe.classscheduler.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.card.MaterialCardView;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ViewBannerBinding;

public class Banner extends MaterialCardView {

    private ViewBannerBinding mView;

    public Banner(@NonNull Context context) {
        super(context);
        init(null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        mView = ViewBannerBinding.inflate(LayoutInflater.from(getContext()), this, true);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Banner, 0, 0);

            setMessage(a.getString(R.styleable.Banner_message));
            setButtonPositive(a.getString(R.styleable.Banner_button_positive), null);
            setButtonNegative(a.getString(R.styleable.Banner_button_negative), null);

            a.recycle();
        }
    }

    public void setMessage(@StringRes int message){
        setMessage(getContext().getString(message));
    }

    public void setMessage(String message){
        mView.textMessage.setText(message);
    }

    public void setButtonPositive(@StringRes int text, OnClickListener listener){
        setButtonPositive(getContext().getString(text), listener);
    }

    public void setButtonPositive(String text, OnClickListener listener){
        setupButton(mView.buttonPositive, text, listener);
    }

    public void setButtonNegative(@StringRes int text, OnClickListener listener){
        setButtonNegative(getContext().getString(text), listener);
    }

    public void setButtonNegative(String text, OnClickListener listener){
        setupButton(mView.buttonNegative, text, listener);
    }

    private void setupButton(Button button, String text, OnClickListener listener){
        button.setText(text);

        if(listener != null){
            button.setOnClickListener(listener);
        }
    }
}
