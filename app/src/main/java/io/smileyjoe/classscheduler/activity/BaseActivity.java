package io.smileyjoe.classscheduler.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    private T mView;

    protected abstract T inflate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = inflate();
        View view = mView.getRoot();
        setContentView(view);
    }

    public T getView() {
        return mView;
    }
}
