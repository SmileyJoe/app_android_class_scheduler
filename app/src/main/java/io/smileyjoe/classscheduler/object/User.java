package io.smileyjoe.classscheduler.object;

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

public class User {

    private String mUsername;
    private String mPhoneNumber;
    private ArrayList<Integer> mAttendingIds;
    private ArrayList<Integer> mRegisteredIds;

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

    public ArrayList<Integer> getAttendingIds() {
        return mAttendingIds;
    }

    public void setAttendingIds(ArrayList<Integer> attendingIds) {
        mAttendingIds = attendingIds;
    }

    public ArrayList<Integer> getRegisteredIds() {
        return mRegisteredIds;
    }

    public void setRegisteredIds(ArrayList<Integer> registeredIds) {
        mRegisteredIds = registeredIds;
    }

    public boolean addAttendingId(Integer id){
        if(mAttendingIds == null){
            mAttendingIds = new ArrayList<>();
            mAttendingIds.add(id);
            return true;
        } else if(!mAttendingIds.contains(id)) {
            mAttendingIds.add(id);
            return true;
        }

        return false;
    }

    public boolean isAttending(Integer id){
        return checkEvent(mAttendingIds, id);
    }

    public boolean addRegisteredId(Integer id){
        if(mRegisteredIds == null){
            mRegisteredIds = new ArrayList<>();
            mRegisteredIds.add(id);
            return true;
        } else if(!mRegisteredIds.contains(id)) {
            mRegisteredIds.add(id);
            return true;
        }

        return false;
    }

    public boolean removeRegisteredId(Integer id){
        if(isRegistered(id)){
            mRegisteredIds.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean isRegistered(Integer id){
        return checkEvent(mRegisteredIds, id);
    }

    private boolean checkEvent(ArrayList<Integer> ids, Integer id){
        if(ids != null && ids.contains(id)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "mUsername='" + mUsername + '\'' +
                ", mPhoneNumber='" + mPhoneNumber + '\'' +
                ", mAttendingIds=" + mAttendingIds +
                ", mRegisteredIds=" + mRegisteredIds +
                '}';
    }
}
