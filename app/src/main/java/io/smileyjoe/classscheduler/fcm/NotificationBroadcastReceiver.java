package io.smileyjoe.classscheduler.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.database.DbUser;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    private static final String EXTRA_SCHEDULE_ID = "schedule_id";

    public enum Action{
        ATTEND(0, R.string.text_attending),
        NO(0, R.string.text_no);

        @DrawableRes int mIcon;
        @StringRes int mTitle;

        Action(int icon, int title) {
            mIcon = icon;
            mTitle = title;
        }

        public NotificationCompat.Action build(Context context, PendingIntent pendingIntent){
            return (new NotificationCompat.Action.Builder(mIcon, context.getString(mTitle), pendingIntent)).build();
        }
    }

    public static PendingIntent getAttendIntent(Context context, int scheduleId){
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.setAction(Action.ATTEND.name());
        intent.putExtra(EXTRA_SCHEDULE_ID, scheduleId);
        return PendingIntent.getBroadcast(context, scheduleId, intent, 0);
    }

    public static PendingIntent getNoIntent(Context context, int scheduleId){
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.setAction(Action.NO.name());
        intent.putExtra(EXTRA_SCHEDULE_ID, scheduleId);
        return PendingIntent.getBroadcast(context, scheduleId, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Action action = Action.valueOf(intent.getAction());

        switch (action){
            case ATTEND:
                DbUser.getDbReferenceSingle(user -> DbUser.attend(user, intent.getIntExtra(EXTRA_SCHEDULE_ID, 0), null));
                break;
            case NO:
            default:
                break;
        }

        cancelNotification(context, intent.getIntExtra(EXTRA_SCHEDULE_ID, 0));
    }

    /**
     * Taken from
     *
     * https://stackoverflow.com/a/55103286
     *
     * @param context
     * @param notifyId
     */
    private void cancelNotification(Context context, int notifyId) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        boolean cancelSummary = false;
        StatusBarNotification[] statusBarNotifications = notificationManager.getActiveNotifications();
        String groupKey = null;
        String group = null;

        for (StatusBarNotification statusBarNotification : statusBarNotifications) {
            if (notifyId == statusBarNotification.getId()) {
                groupKey = statusBarNotification.getGroupKey();
                group = statusBarNotification.getNotification().getGroup();
                break;
            }
        }

        int counter = 0;
        for (StatusBarNotification statusBarNotification : statusBarNotifications) {
            if (statusBarNotification.getGroupKey().equals(groupKey)) {
                counter++;
            }
        }

        if (counter == 2) {
            cancelSummary = true;
        }

        if (cancelSummary) {
            notificationManager.cancel(Notification.Type.valueOf(group).getGroupId());
        } else {
            notificationManager.cancel(notifyId);
        }
    }
}
