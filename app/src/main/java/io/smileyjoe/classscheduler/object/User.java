package io.smileyjoe.classscheduler.object;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import io.smileyjoe.classscheduler.utils.Utils;

public class User implements Parcelable {

    private String mId;
    private String mUsername;
    private String mPhoneNumber;
    private HashMap<String, Boolean> mAttendingIds;
    private HashMap<String, Boolean> mRegisteredIds;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
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

    public HashMap<String, Boolean> getAttendingIds() {
        return mAttendingIds;
    }

    public void setAttendingIds(HashMap<String, Boolean> attendingIds) {
        mAttendingIds = attendingIds;
    }

    public HashMap<String, Boolean> getRegisteredIds() {
        return mRegisteredIds;
    }

    public void setRegisteredIds(HashMap<String, Boolean> registeredIds) {
        mRegisteredIds = registeredIds;
    }

    public boolean addAttendingId(Integer idInt){
        String id = Integer.toString(idInt);
        if(mAttendingIds == null){
            mAttendingIds = new HashMap<>();
            mAttendingIds.put(id, true);
            return true;
        } else if(!mAttendingIds.containsKey(id)) {
            mAttendingIds.put(id, true);
            return true;
        }

        return false;
    }

    public boolean removingAttendingId(Integer id){
        if(isAttending(id)){
            mAttendingIds.remove(Integer.toString(id));
            return true;
        } else {
            return false;
        }
    }

    public boolean isAttending(Integer id){
        return checkEvent(mAttendingIds, id);
    }

    public boolean addRegisteredId(Integer idInt){
        String id = Integer.toString(idInt);
        if(mRegisteredIds == null){
            mRegisteredIds = new HashMap<>();
            mRegisteredIds.put(id, true);
            return true;
        } else if(!mRegisteredIds.containsKey(id)) {
            mRegisteredIds.put(id, true);
            return true;
        }

        return false;
    }

    public boolean removeRegisteredId(Integer id){
        if(isRegistered(id)){
            mRegisteredIds.remove(Integer.toString(id));
            return true;
        } else {
            return false;
        }
    }

    public boolean isRegistered(Integer id){
        return checkEvent(mRegisteredIds, id);
    }

    private boolean checkEvent(HashMap<String, Boolean> ids, Integer idInt){
        String id = Integer.toString(idInt);
        if(ids != null && ids.containsKey(id)){
            return true;
        } else {
            return false;
        }
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mUsername);
        dest.writeString(this.mPhoneNumber);
        dest.writeSerializable(this.mAttendingIds);
        dest.writeSerializable(this.mRegisteredIds);
    }

    protected User(Parcel in) {
        this.mId = in.readString();
        this.mUsername = in.readString();
        this.mPhoneNumber = in.readString();
        this.mAttendingIds = (HashMap<String, Boolean>) in.readSerializable();
        this.mRegisteredIds = (HashMap<String, Boolean>) in.readSerializable();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "mId='" + mId + '\'' +
                ", mUsername='" + mUsername + '\'' +
                ", mPhoneNumber='" + mPhoneNumber + '\'' +
                ", mAttendingIds=" + mAttendingIds +
                ", mRegisteredIds=" + mRegisteredIds +
                '}';
    }
}
