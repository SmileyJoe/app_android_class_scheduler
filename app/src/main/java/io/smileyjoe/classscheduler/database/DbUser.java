package io.smileyjoe.classscheduler.database;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import io.smileyjoe.classscheduler.object.User;
import io.smileyjoe.classscheduler.utils.Utils;

public class DbUser {

    public interface DataChangedListener<T>{
        void onDataChange(T item);
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

        user.setRegisteredIds(getMapValue(itemSnapshot.child(DB_KEY_REGISTERED_IDS)));
        user.setAttendingIds(getMapValue(itemSnapshot.child(DB_KEY_ATTENDING_IDS)));
        return user;
    }

    /**
     * Firebase does it's own thing, if a hashmap has number keys, even if they are set
     * as Strings, it will make it an array when it's parsed ... only on some conditions,
     * so there is no way to garuntee that it will come back as a hashmap, or an array.
     * <br/>
     * This will try get it as a hashmap, as it should be, if an exception is thrown, it'll
     * get it as an array, then cycle through and make a map
     *
     * @param snapshot current node
     * @return a hashmap of the node
     */
    private static HashMap<String, Boolean> getMapValue(DataSnapshot snapshot){
        GenericTypeIndicator<HashMap<String, Boolean>> typeMap = new GenericTypeIndicator<HashMap<String, Boolean>>(){};

        try {
            return snapshot.getValue(typeMap);
        } catch (DatabaseException e){
            GenericTypeIndicator<ArrayList<Boolean>> typeArray = new GenericTypeIndicator<ArrayList<Boolean>>(){};

            ArrayList<Boolean> data = snapshot.getValue(typeArray);
            HashMap<String, Boolean> map = new HashMap<>();

            for(int i = 0; i < data.size(); i++){
                Boolean value = data.get(i);

                if(value != null){
                    map.put(Integer.toString(i), value);
                }
            }

            return map;
        }
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

    public static void getUsername(String userId, DataChangedListener<String> listener){
        getDbReference(userId).child(DB_KEY_USERNAME).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listener != null){
                    listener.onDataChange(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(listener != null){
                    listener.onDataChange("Unknown");
                }
            }
        });
    }

    public static DatabaseReference getDbReference(String userId){
        return Utils.getDb().getReference(DB_NAME)
                .child(userId);
    }

    public static DatabaseReference getDbReference(){
        return getDbReference(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public static void getDbReferenceSingle(DataChangedListener<User> listener){
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

        public Updater registered(HashMap<String, Boolean> ids){
            mData.put(DB_KEY_REGISTERED_IDS, ids);
            return this;
        }

        public Updater attending(HashMap<String, Boolean> ids){
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
