package io.smileyjoe.classscheduler.database;

import android.text.TextUtils;
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
    private static final String DB_KEY_FCM_TOKEN = "fcm_token";

    public static User parse(DataSnapshot itemSnapshot){
        User user = new User();
        user.setUsername(itemSnapshot.child(DB_KEY_USERNAME).getValue(String.class));
        user.setPhoneNumber(itemSnapshot.child(DB_KEY_PHONE_NUMBER).getValue(String.class));

        GenericTypeIndicator<ArrayList<Integer>> type = new GenericTypeIndicator<ArrayList<Integer>>(){};

        user.setRegisteredIds(itemSnapshot.child(DB_KEY_REGISTERED_IDS).getValue(type));
        user.setAttendingIds(itemSnapshot.child(DB_KEY_ATTENDING_IDS).getValue(type));
        return user;
    }

    public static void updateProfile(User user, DatabaseReference.CompletionListener listener){
        Updater.getInstance().profile(user).update(listener);
    }

    public static void newToken(String token, DatabaseReference.CompletionListener listener){
        if(TextUtils.isEmpty(token)) {
            getDbReference().child(DB_KEY_FCM_TOKEN).removeValue();
        } else {
            Updater.getInstance().fcmToken(token).update(listener);
        }
    }

    public static void unregister(User user, Integer id, DatabaseReference.CompletionListener listener){
        if(user != null) {
            boolean removed = user.removeRegisteredId(id);

            if(removed) {
                Updater updater = new Updater();
                updater.registered(user.getRegisteredIds());

                if(user.isAttending(id)){
                    user.removingAttendingId(id);
                    updater.attending(user.getAttendingIds());
                }

                updater.update(listener);
            }
        }
    }

    public static void register(User user, Integer id, DatabaseReference.CompletionListener listener){
        if(user != null) {
            boolean added = user.addRegisteredId(id);

            if(added) {
                Updater.getInstance().registered(user.getRegisteredIds()).update(listener);
            }
        }
    }

    public static void cancel(User user, Integer id, DatabaseReference.CompletionListener listener){
        if(user != null) {
            boolean removed = user.removingAttendingId(id);

            if(removed) {
                Updater.getInstance().attending(user.getAttendingIds()).update(listener);
            }
        }
    }

    public static void attend(User user, Integer id, DatabaseReference.CompletionListener listener){
        if(user != null) {
            boolean added = user.addAttendingId(id);

            if(added) {
                Updater updater = new Updater();
                updater.attending(user.getAttendingIds());

                if(!user.isRegistered(id)){
                    user.addRegisteredId(id);
                    updater.registered(user.getRegisteredIds());
                }

                updater.update(listener);
            }
        }
    }

    public static DatabaseReference getDbReference(){
        return Utils.getDb().getReference(DB_NAME)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public static void getDbReferenceSingle(DataChangedListener listener){
        getDbReference().addListenerForSingleValueEvent(new ValueEventListener() {
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

    private static class Updater{
        HashMap<String, Object> mData = new HashMap<>();

        public Updater() {
        }

        public static Updater getInstance(){
            return new Updater();
        }

        public Updater registered(ArrayList<Integer> ids){
            mData.put(DB_KEY_REGISTERED_IDS, ids);
            return this;
        }

        public Updater attending(ArrayList<Integer> ids){
            mData.put(DB_KEY_ATTENDING_IDS, ids);
            return this;
        }

        public Updater profile(User user){
            mData.put(DB_KEY_USERNAME, user.getUsername());
            mData.put(DB_KEY_PHONE_NUMBER, user.getPhoneNumber());
            return this;
        }

        public Updater fcmToken(String token){
            mData.put(DB_KEY_FCM_TOKEN, token);
            return this;
        }

        public void update(DatabaseReference.CompletionListener listener){
            getDbReference().updateChildren(mData, listener);
        }
    }
}
