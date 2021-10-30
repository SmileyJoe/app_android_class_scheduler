package io.smileyjoe.classscheduler.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import io.smileyjoe.classscheduler.object.User;
import io.smileyjoe.classscheduler.utils.Utils;

public class DbUser {

    public interface DataChangedListener{
        void onDataChange(User user);
    }

    private static final String DB_NAME = "user";
    private static final String DB_KEY_USERNAME = "username";
    private static final String DB_KEY_PHONE_NUMBER = "phone_number";
    private static final String DB_KEY_ATTENDING_IDS = "attending";
    private static final String DB_KEY_REGISTERED_IDS = "registered";

    public static User parse(DataSnapshot itemSnapshot){
        User user = new User();
        user.setUsername(itemSnapshot.child(DB_KEY_USERNAME).getValue(String.class));
        user.setPhoneNumber(itemSnapshot.child(DB_KEY_PHONE_NUMBER).getValue(String.class));

        GenericTypeIndicator<ArrayList<Integer>> t = new GenericTypeIndicator<ArrayList<Integer>>(){};

        user.setRegisteredIds(itemSnapshot.child(DB_KEY_REGISTERED_IDS).getValue(t));
        return user;
    }

    public static void updateProfile(User user, DatabaseReference.CompletionListener listener){
        HashMap<String, Object> data = new HashMap<>();
        data.put(DB_KEY_USERNAME, user.getUsername());
        data.put(DB_KEY_PHONE_NUMBER, user.getPhoneNumber());
        getDbReference().updateChildren(data, listener);
    }

    public static void save(User user, DatabaseReference.CompletionListener listener){
        HashMap<String, Object> data = new HashMap<>();
        data.put(DB_KEY_USERNAME, user.getUsername());
        data.put(DB_KEY_PHONE_NUMBER, user.getPhoneNumber());
        data.put(DB_KEY_ATTENDING_IDS, user.getAttendingIds());

        getDbReference().updateChildren(data, listener);
    }

    public static void unregister(User user, Integer id, DatabaseReference.CompletionListener listener){
        if(user != null) {
            boolean removed = user.removeRegisteredId(id);

            if(removed) {
                updateRegistered(user, listener);
            }
        }
    }

    public static void register(User user, Integer id, DatabaseReference.CompletionListener listener){
        if(user != null) {
            boolean added = user.addRegisteredId(id);

            if(added) {
                updateRegistered(user, listener);
            }
        }
    }

    private static void updateRegistered(User user, DatabaseReference.CompletionListener listener){
        HashMap<String, Object> data = new HashMap<>();
        data.put(DB_KEY_REGISTERED_IDS, user.getRegisteredIds());
        getDbReference().updateChildren(data, listener);
    }

    public static DatabaseReference getDbReference(){
        return Utils.getDb().getReference(DB_NAME)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public static void getDbReference(DataChangedListener listener){
        getDbReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listener != null){
                    listener.onDataChange(parse(snapshot));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // do nothing //
            }
        });
    }

    public static boolean isLoggedIn(){
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}
