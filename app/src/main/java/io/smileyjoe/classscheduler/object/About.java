package io.smileyjoe.classscheduler.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import io.smileyjoe.classscheduler.utils.Utils;

public class About implements Parcelable {

    private static final String DB_NAME = "about";
    private static final String DB_KEY_DESCRIPTION = "description";
    private static final String DB_KEY_EMAIL = "email";
    private static final String DB_KEY_NAME = "name";
    private static final String DB_KEY_PHONE = "phone";
    private static final String DB_KEY_WEBSITE = "website";

    private String mDescription;
    private String mEmail;
    private String mName;
    private String mPhone;
    private String mWebsite;

    public About() {
    }

    public About(DataSnapshot itemSnapshot){
        setDescription(itemSnapshot.child(DB_KEY_DESCRIPTION).getValue(String.class));
        setEmail(itemSnapshot.child(DB_KEY_EMAIL).getValue(String.class));
        setName(itemSnapshot.child(DB_KEY_NAME).getValue(String.class));
        setPhone(itemSnapshot.child(DB_KEY_PHONE).getValue(String.class));
        setWebsite(itemSnapshot.child(DB_KEY_WEBSITE).getValue(String.class));
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public static DatabaseReference getDbReference(){
        return Utils.getDb().getReference(DB_NAME);
    }

    @Override
    public String toString() {
        return "About{" +
                "mDescription='" + mDescription + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mName='" + mName + '\'' +
                ", mPhone='" + mPhone + '\'' +
                ", mWebsite='" + mWebsite + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mDescription);
        dest.writeString(this.mEmail);
        dest.writeString(this.mName);
        dest.writeString(this.mPhone);
        dest.writeString(this.mWebsite);
    }

    protected About(Parcel in) {
        this.mDescription = in.readString();
        this.mEmail = in.readString();
        this.mName = in.readString();
        this.mPhone = in.readString();
        this.mWebsite = in.readString();
    }

    public static final Parcelable.Creator<About> CREATOR = new Parcelable.Creator<About>() {
        @Override
        public About createFromParcel(Parcel source) {
            return new About(source);
        }

        @Override
        public About[] newArray(int size) {
            return new About[size];
        }
    };
}
