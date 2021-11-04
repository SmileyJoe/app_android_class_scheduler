package io.smileyjoe.classscheduler.fcm;

import android.app.Notification;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.database.DbUser;
import io.smileyjoe.classscheduler.fcm.NotificationBroadcastReceiver.Action;

public class FcmService extends FirebaseMessagingService {

    public enum Type{
        CHECK_ATTEND("check_attend", 1000);

        private String mDataValue;
        private int mGroupId;

        Type(String dataValue, int groupId) {
            mDataValue = dataValue;
            mGroupId = groupId;
        }

        public String getDataValue() {
            return mDataValue;
        }

        public int getGroupId() {
            return mGroupId;
        }

        public static Type fromDataValue(String dataValue){
            if(!TextUtils.isEmpty(dataValue)) {
                for (Type type : values()) {
                    if (type.getDataValue().equals(dataValue)) {
                        return type;
                    }
                }
            }

            return null;
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        if(DbUser.isLoggedIn()) {
            DbUser.newToken(token, null);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Type type = null;

        if(remoteMessage.getData() != null){
            type = Type.fromDataValue(remoteMessage.getData().getOrDefault("type", null));
        }

        if(type != null){
            switch (type){
                case CHECK_ATTEND:
                    handleCheckAttend(remoteMessage);
                    break;
            }
        }
    }

    private void handleCheckAttend(RemoteMessage remoteMessage){
        int scheduleId = -1;
        String title = null;
        String body = null;

        if(remoteMessage.getData() != null){
            scheduleId = Integer.parseInt(remoteMessage.getData().getOrDefault("id", "-1"));
        }

        if(remoteMessage.getNotification() != null){
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        }

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body) && scheduleId >= 0) {
            Notification notification = new Notification.Builder(getApplicationContext(), io.smileyjoe.classscheduler.utils.Notification.Channel.GENERAL.getId(getApplicationContext()))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(R.drawable.ic_notification)
                    .addAction(Action.ATTEND.build(getApplicationContext(), NotificationBroadcastReceiver.getAttendIntent(getApplicationContext(), scheduleId)))
                    .addAction(Action.NO.build(getApplicationContext(), NotificationBroadcastReceiver.getNoIntent(getApplicationContext(), scheduleId)))
                    .setGroup(Type.CHECK_ATTEND.name())
                    .build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(scheduleId, notification);

            showGroupSummary(Type.CHECK_ATTEND);
        }
    }

    private void showGroupSummary(Type type){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        NotificationCompat.Builder groupBuilder =
                new NotificationCompat.Builder(getApplicationContext(), io.smileyjoe.classscheduler.utils.Notification.Channel.GENERAL.getId(getApplicationContext()))
                        .setGroupSummary(true)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setGroup(type.name())
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.InboxStyle());

        notificationManager.notify(type.getGroupId(), groupBuilder.build());
    }
}
