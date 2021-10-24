package io.smileyjoe.classscheduler.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.smileyjoe.classscheduler.databinding.FragmentAboutBinding;
import io.smileyjoe.classscheduler.object.About;

public abstract class BaseFirebaseFragment<T extends ViewBinding> extends Fragment implements ValueEventListener {

    private T mView;

    protected abstract T inflate(LayoutInflater inflater, ViewGroup container, boolean savedInstanceState);

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mView = inflate(inflater, container, false);
        return mView.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDataListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
    }

    protected void setDataListener(){
        FirebaseDatabase
                .getInstance()
                .getReference(getDbName())
                .addValueEventListener(this);
    }

    protected String getDbName(){
        return "";
    }

    protected T getRoot(){
        return mView;
    }
}
