package io.smileyjoe.classscheduler.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.smileyjoe.classscheduler.databinding.FragmentAboutBinding;
import io.smileyjoe.classscheduler.object.About;
import io.smileyjoe.classscheduler.utils.Communication;
import io.smileyjoe.classscheduler.utils.Utils;

public abstract class BaseFirebaseFragment<T extends ViewBinding> extends Fragment implements ValueEventListener, Communication.Listener {

    private T mView;

    protected abstract T inflate(LayoutInflater inflater, ViewGroup container, boolean savedInstanceState);
    protected abstract DatabaseReference getDatabaseReference();

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

        getDatabaseReference().addValueEventListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
    }

    protected T getRoot(){
        return mView;
    }

    @Override
    public void success(int messageResId) {
        if(getActivity() instanceof Communication.Listener){
            ((Communication.Listener) getActivity()).success(messageResId);
        }
    }

    @Override
    public void error(int messageResId) {
        error(getString(messageResId));
    }

    @Override
    public void error(String message) {
        if(getActivity() instanceof Communication.Listener){
            ((Communication.Listener) getActivity()).error(message);
        }
    }
}
