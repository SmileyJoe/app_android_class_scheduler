package io.smileyjoe.classscheduler.object;

public enum Day {
    MONDAY("monday"),
    TUESDAY("tuesday"),
    WEDNESDAY("wednesday"),
    THURSDAY("thursday"),
    FRIDAY("friday"),
    SATURDAY("saturday"),
    SUNDAY("sunday"),
    UNKNOWN("");

    private String mDbString;

    Day(String dbString) {
        mDbString = dbString;
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
