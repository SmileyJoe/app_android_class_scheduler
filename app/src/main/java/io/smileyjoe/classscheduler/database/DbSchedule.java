package io.smileyjoe.classscheduler.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.utils.Utils;

public class DbSchedule {

    private static final String DB_NAME = "schedule";
    private static final String DB_KEY_ID = "id";
    private static final String DB_KEY_NAME = "name";
    private static final String DB_KEY_DAY = "day";
    private static final String DB_KEY_START = "start";
    private static final String DB_KEY_END = "end";
    private static final String DB_KEY_DESCRIPTION = "description";
    private static final String DB_KEY_DETAILS = "details";
    private static final String DB_KEY_ICON = "icon";

    public static Schedule parse(DataSnapshot itemSnapshot){
        Schedule schedule = new Schedule();

        schedule.setEmpty(false);
        schedule.setId(itemSnapshot.child(DB_KEY_ID).getValue(Integer.class));
        schedule.setName(itemSnapshot.child(DB_KEY_NAME).getValue(String.class));
        schedule.setDay(itemSnapshot.child(DB_KEY_DAY).getValue(String.class));
        schedule.setTimeStart(itemSnapshot.child(DB_KEY_START).getValue(String.class));
        schedule.setTimeEnd(itemSnapshot.child(DB_KEY_END).getValue(String.class));
        schedule.setDescription(itemSnapshot.child(DB_KEY_DESCRIPTION).getValue(String.class));
        schedule.setDetails(itemSnapshot.child(DB_KEY_DETAILS).getValue(String.class));
        schedule.setIconName(itemSnapshot.child(DB_KEY_ICON).getValue(String.class));
        schedule.setHeader(false);

        return schedule;
    }

    public static DatabaseReference getDbReference(){
        return Utils.getDb().getReference(DB_NAME);
    }
}
