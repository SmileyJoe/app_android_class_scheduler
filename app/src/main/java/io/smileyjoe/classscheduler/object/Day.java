package io.smileyjoe.classscheduler.object;

import android.content.Context;

import androidx.annotation.StringRes;

import io.smileyjoe.classscheduler.R;

public enum Day {
    MONDAY(R.string.title_monday, 0, "monday"),
    TUESDAY(R.string.title_tuesday, 1, "tuesday"),
    WEDNESDAY(R.string.title_wednesday,2, "wednesday"),
    THURSDAY(R.string.title_thursday, 3, "thursday"),
    FRIDAY(R.string.title_friday, 4, "friday"),
    SATURDAY(R.string.title_saturday, 5, "saturday"),
    SUNDAY(R.string.title_sunday, 6, "sunday"),
    UNKNOWN(R.string.title_error, 7, "");

    private @StringRes int mTitleResId;
    private int mPosition;
    private String mDbString;

    Day(int titleResId, int position, String dbString) {
        mTitleResId = titleResId;
        mPosition = position;
        mDbString = dbString;
    }

    public String getTitle(Context context){
        return context.getString(mTitleResId);
    }

    public int getPosition() {
        return mPosition;
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
