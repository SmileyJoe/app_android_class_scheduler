package io.smileyjoe.classscheduler.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.activity.LoginActivity;
import io.smileyjoe.classscheduler.database.DbUser;
import io.smileyjoe.classscheduler.databinding.FragmentAccountBinding;
import io.smileyjoe.classscheduler.object.About;
import io.smileyjoe.classscheduler.object.User;
import io.smileyjoe.classscheduler.utils.Utils;

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
        getRoot().buttonEdit.setOnClickListener(v -> Navigation.findNavController(getRoot().getRoot()).navigate(R.id.action_account_to_edit));
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        User user = DbUser.parse(snapshot);

        if(getRoot() != null) {
            getRoot().textUsername.setContent(user.getUsername());
            getRoot().textPhoneNumber.setContent(user.getPhoneNumber());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    protected DatabaseReference getDatabaseReference() {
        return DbUser.getDbReference();
    }

    private void signOut(){
        Utils.disableFCM();
        DbUser.newToken(null, null);
        Utils.getAuth()
                .signOut(getContext())
                .addOnCompleteListener(task -> {
                    if(getActivity() instanceof Listener){
                        ((Listener) getActivity()).onLogout();
                    }
                });
    }
}
