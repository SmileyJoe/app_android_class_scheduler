package io.smileyjoe.classscheduler.object;

import android.content.Context;

import androidx.annotation.StringRes;

import io.smileyjoe.classscheduler.R;

public enum Day {
    MONDAY(R.string.title_monday, "monday"),
    TUESDAY(R.string.title_tuesday, "tuesday"),
    WEDNESDAY(R.string.title_wednesday, "wednesday"),
    THURSDAY(R.string.title_thursday, "thursday"),
    FRIDAY(R.string.title_friday, "friday"),
    SATURDAY(R.string.title_saturday, "saturday"),
    SUNDAY(R.string.title_sunday, "sunday"),
    UNKNOWN(R.string.title_error, "");

    private @StringRes int mTitleResId;
    private String mDbString;

    Day(int titleResId, String dbString) {
        mTitleResId = titleResId;
        mDbString = dbString;
    }

    public String getTitle(Context context){
        return context.getString(mTitleResId);
    }

    public static Day fromDb(String dbString){
        for(Day day:values()){
            if(day.mDbString.equalsIgnoreCase(dbString)){
                return day;
            }
        }

        return UNKNOWN;
    }
}
