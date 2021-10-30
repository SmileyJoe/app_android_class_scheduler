package io.smileyjoe.classscheduler.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.database.DbUser;
import io.smileyjoe.classscheduler.databinding.FragmentAccountEditBinding;
import io.smileyjoe.classscheduler.object.User;
import io.smileyjoe.classscheduler.utils.Communication;

public class AccountEditFragment extends BaseFirebaseFragment<FragmentAccountEditBinding> implements DatabaseReference.CompletionListener {

    private User mUser;

    @Override
    protected FragmentAccountEditBinding inflate(LayoutInflater inflater, ViewGroup container, boolean savedInstanceState) {
        return FragmentAccountEditBinding.inflate(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getRoot().buttonSave.setOnClickListener(v -> save());
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        mUser = DbUser.parse(snapshot);

        if(getRoot() != null) {
            getRoot().inputUsername.getEditText().setText(mUser.getUsername());
            getRoot().inputPhoneNumber.getEditText().setText(mUser.getPhoneNumber());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    protected DatabaseReference getDatabaseReference() {
        return DbUser.getDbReference();
    }

    private void save(){
        mUser.setUsername(getRoot().inputUsername.getEditText().getText().toString());
        mUser.setPhoneNumber(getRoot().inputPhoneNumber.getEditText().getText().toString());
        DbUser.updateProfile(mUser, this);
    }

    @Override
    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
        if(error == null){
            success(R.string.success_account_update);
            Navigation.findNavController(getRoot().getRoot()).popBackStack();
        } else {
            error(R.string.error_account_update);
        }
    }
}
