package io.smileyjoe.classscheduler.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;

public class Schedule implements Parcelable {

    public static final String DB_NAME = "schedule";
    private static final String DB_KEY_ID = "id";
    private static final String DB_KEY_NAME = "name";
    private static final String DB_KEY_DAY = "day";
    private static final String DB_KEY_START = "start";
    private static final String DB_KEY_END = "end";
    private static final String DB_KEY_DESCRIPTION = "description";
    private static final String DB_KEY_DETAILS = "details";

    private Integer mId;
    private String mName;
    private String mDescription;
    private String mDetails;
    private Day mDay;
    private String mTimeStart;
    private String mTimeEnd;

    private Schedule(){}

    public Schedule(DataSnapshot itemSnapshot){
        setId(itemSnapshot.child(DB_KEY_ID).getValue(Integer.class));
        setName(itemSnapshot.child(DB_KEY_NAME).getValue(String.class));
        setDay(itemSnapshot.child(DB_KEY_DAY).getValue(String.class));
        setTimeStart(itemSnapshot.child(DB_KEY_START).getValue(String.class));
        setTimeEnd(itemSnapshot.child(DB_KEY_END).getValue(String.class));
        setDescription(itemSnapshot.child(DB_KEY_DESCRIPTION).getValue(String.class));
        setDetails(itemSnapshot.child(DB_KEY_DETAILS).getValue(String.class));
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
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mName);
        dest.writeString(this.mDescription);
        dest.writeString(this.mDetails);
        dest.writeInt(this.mDay == null ? -1 : this.mDay.ordinal());
        dest.writeString(this.mTimeStart);
        dest.writeString(this.mTimeEnd);
    }

    protected Schedule(Parcel in) {
        this.mId = in.readInt();
        this.mName = in.readString();
        this.mDescription = in.readString();
        this.mDetails = in.readString();
        int tmpMDay = in.readInt();
        this.mDay = tmpMDay == -1 ? null : Day.values()[tmpMDay];
        this.mTimeStart = in.readString();
        this.mTimeEnd = in.readString();
    }

    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {
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
