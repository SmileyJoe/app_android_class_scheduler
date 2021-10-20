package io.smileyjoe.classscheduler.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import io.smileyjoe.classscheduler.databinding.FragmentAccountBinding;
import io.smileyjoe.classscheduler.object.About;

public class AccountFragment extends BaseFirebaseFragment<FragmentAccountBinding> {

    public interface Listener{
        void onLogout();
    }

    @Override
    protected FragmentAccountBinding inflate(LayoutInflater inflater, ViewGroup container, boolean savedInstanceState) {
        return FragmentAccountBinding.inflate(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getRoot().buttonSignOut.setOnClickListener(v -> signOut());
    }

    @Override
    protected String getDbName() {
        return About.DB_NAME;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    private void signOut(){
        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(task -> {
                    if(getActivity() instanceof Listener){
                        ((Listener) getActivity()).onLogout();
                    }
                });
    }
}
