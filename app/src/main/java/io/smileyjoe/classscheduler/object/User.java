package io.smileyjoe.classscheduler.object;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class User {

    public static final String DB_NAME = "user";
    private static final String DB_KEY_USERNAME = "username";
    private static final String DB_KEY_PHONE_NUMBER = "phone_number";

    private String mUsername;
    private String mPhoneNumber;

    public User(DataSnapshot itemSnapshot){
        setUsername(itemSnapshot.child(DB_KEY_USERNAME).getValue(String.class));
        setPhoneNumber(itemSnapshot.child(DB_KEY_PHONE_NUMBER).getValue(String.class));
    }

    public void save(DatabaseReference dbReference, DatabaseReference.CompletionListener listener){
        HashMap<String, Object> data = new HashMap<>();
        data.put(DB_KEY_USERNAME, getUsername());
        data.put(DB_KEY_PHONE_NUMBER, getPhoneNumber());

        dbReference.updateChildren(data, listener);
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

    @Override
    public String toString() {
        return "User{" +
                "mUsername='" + mUsername + '\'' +
                ", mPhoneNumber='" + mPhoneNumber + '\'' +
                '}';
    }
}
