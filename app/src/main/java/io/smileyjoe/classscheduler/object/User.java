package io.smileyjoe.classscheduler.object;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import io.smileyjoe.classscheduler.utils.Utils;

public class User {

    private static final String DB_NAME = "user";
    private static final String DB_KEY_USERNAME = "username";
    private static final String DB_KEY_PHONE_NUMBER = "phone_number";

    private String mUsername;
    private String mPhoneNumber;

    public User(DataSnapshot itemSnapshot){
        setUsername(itemSnapshot.child(DB_KEY_USERNAME).getValue(String.class));
        setPhoneNumber(itemSnapshot.child(DB_KEY_PHONE_NUMBER).getValue(String.class));
    }

    public void save(DatabaseReference.CompletionListener listener){
        HashMap<String, Object> data = new HashMap<>();
        data.put(DB_KEY_USERNAME, getUsername());
        data.put(DB_KEY_PHONE_NUMBER, getPhoneNumber());

        getDbReference().updateChildren(data, listener);
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public static DatabaseReference getDbReference(){
        return Utils.getDb().getReference(DB_NAME)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    @Override
    public String toString() {
        return "User{" +
                "mUsername='" + mUsername + '\'' +
                ", mPhoneNumber='" + mPhoneNumber + '\'' +
                '}';
    }
}
