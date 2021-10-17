package io.smileyjoe.classscheduler.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ViewDetailTextBinding;

public class DetailTextView extends LinearLayout {

    private ViewDetailTextBinding mView;

    public DetailTextView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public DetailTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DetailTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        mView = ViewDetailTextBinding.inflate(LayoutInflater.from(getContext()), this, true);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DetailTextView, 0, 0);

            setTitle(a.getString(R.styleable.DetailTextView_title));
            setContent(a.getString(R.styleable.DetailTextView_content));

            a.recycle();
        }
    }

    public void setTitle(String title){
        mView.textTitle.setText(title);
    }

    public void setContent(String content){
        mView.textContent.setText(content);
    }
}
