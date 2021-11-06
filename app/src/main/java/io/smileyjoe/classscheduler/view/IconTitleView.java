package io.smileyjoe.classscheduler.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ViewDetailTextBinding;
import io.smileyjoe.classscheduler.databinding.ViewIconTitleBinding;

public class IconTitleView extends ConstraintLayout {

    private ViewIconTitleBinding mView;
    private String mTitleDefault;

    public IconTitleView(Context context) {
        super(context);
        init(null);
    }

    public IconTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public IconTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs){
        mView = ViewIconTitleBinding.inflate(LayoutInflater.from(getContext()), this, true);

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IconTitleView, 0, 0);

            setIcon(a.getString(R.styleable.IconTitleView_icon_name));
            setTitle(a.getString(R.styleable.IconTitleView_icon_title));

            a.recycle();
        }
    }

    public void setTitle(String title){
        mView.textTitle.setText(title);

        if(TextUtils.isEmpty(mTitleDefault)){
            mTitleDefault = title;
        }
    }

    public void setIcon(String icon){
        mView.imageIcon.setIcon(icon);
    }

    public String getTitleDefault() {
        return mTitleDefault;
    }
}
