package io.smileyjoe.classscheduler.object;

import java.util.Comparator;

public class ScheduleComparator implements Comparator<Schedule> {

    @Override
    public int compare(Schedule o1, Schedule o2) {
        int position1 = o1.getDay().getPosition();
        int position2 = o2.getDay().getPosition();

        if (position1 == position2) {
            return 0;
        } else if (position1 > position2) {
            return 1;
        } else {
            return -1;
        }
    }
}
