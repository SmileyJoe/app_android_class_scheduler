package io.smileyjoe.classscheduler.object;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.utils.Utils;

public class Schedule implements Parcelable {

    private Integer mId;
    private String mName;
    private String mDescription;
    private String mDetails;
    private Day mDay;
    private String mTimeStart;
    private String mTimeEnd;
    private boolean mIsHeader;
    private String mIconName;
    private boolean mIsEmpty;
    private ArrayList<User> mAttendingUsers;
    private ArrayList<User> mRegisteredUsers;

    public Schedule(){
        setEmpty(true);
    }

    public Schedule(Day day){
        setDay(day);
        setHeader(true);
        setEmpty(false);
    }

    public Integer getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getDetails() {
        return mDetails;
    }

    public Day getDay() {
        return mDay;
    }

    public String getTimeStart() {
        return mTimeStart;
    }

    public String getTimeEnd() {
        return mTimeEnd;
    }

    public String getTimeFormatted(Context context){
        return context.getString(R.string.text_schedule_time, getTimeStart(), getTimeEnd());
    }

    public boolean isHeader() {
        return mIsHeader;
    }

    public String getIconName() {
        return mIconName;
    }

    public boolean isEmpty() {
        return mIsEmpty;
    }

    public ArrayList<User> getAttendingUsers() {
        return mAttendingUsers;
    }

    public ArrayList<User> getRegisteredUsers() {
        return mRegisteredUsers;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

    public void setDay(Day day) {
        mDay = day;
    }

    public void setDay(String dbDay){
        setDay(Day.fromDb(dbDay));
    }

    public void setTimeStart(String timeStart) {
        mTimeStart = timeStart;
    }

    public void setTimeEnd(String timeEnd) {
        mTimeEnd = timeEnd;
    }

    public void setHeader(boolean header) {
        mIsHeader = header;
    }

    public void setIconName(String iconName) {
        mIconName = iconName;
    }

    public void setEmpty(boolean empty) {
        mIsEmpty = empty;
    }

    public void setAttendingUsers(ArrayList<User> attendingUsers) {
        mAttendingUsers = attendingUsers;
    }

    public void setRegisteredUsers(ArrayList<User> registeredUsers) {
        mRegisteredUsers = registeredUsers;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mDetails='" + mDetails + '\'' +
                ", mDay=" + mDay +
                ", mTimeStart='" + mTimeStart + '\'' +
                ", mTimeEnd='" + mTimeEnd + '\'' +
                ", mIsHeader=" + mIsHeader +
                ", mIconName='" + mIconName + '\'' +
                ", mIsEmpty=" + mIsEmpty +
                ", mAttendingUsers=" + mAttendingUsers +
                ", mRegisteredUsers=" + mRegisteredUsers +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mDescription);
        dest.writeString(this.mDetails);
        dest.writeInt(this.mDay == null ? -1 : this.mDay.ordinal());
        dest.writeString(this.mTimeStart);
        dest.writeString(this.mTimeEnd);
        dest.writeByte(this.mIsHeader ? (byte) 1 : (byte) 0);
        dest.writeString(this.mIconName);
        dest.writeByte(this.mIsEmpty ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.mAttendingUsers);
        dest.writeTypedList(this.mRegisteredUsers);
    }

    protected Schedule(Parcel in) {
        this.mId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mName = in.readString();
        this.mDescription = in.readString();
        this.mDetails = in.readString();
        int tmpMDay = in.readInt();
        this.mDay = tmpMDay == -1 ? null : Day.values()[tmpMDay];
        this.mTimeStart = in.readString();
        this.mTimeEnd = in.readString();
        this.mIsHeader = in.readByte() != 0;
        this.mIconName = in.readString();
        this.mIsEmpty = in.readByte() != 0;
        this.mAttendingUsers = in.createTypedArrayList(User.CREATOR);
        this.mRegisteredUsers = in.createTypedArrayList(User.CREATOR);
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel source) {
            return new Schedule(source);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };
}
