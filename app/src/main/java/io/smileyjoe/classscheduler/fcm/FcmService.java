package io.smileyjoe.classscheduler.fcm;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.database.DbUser;
import io.smileyjoe.classscheduler.fcm.NotificationBroadcastReceiver.Action;

public class FcmService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        if(DbUser.isLoggedIn()) {
            DbUser.newToken(token, null);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Notification.Type type = null;

        if(remoteMessage.getData() != null){
            type = Notification.Type.fromDataValue(remoteMessage.getData().getOrDefault("type", null));
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

        if(remoteMessage.getData() != null){
            scheduleId = Integer.parseInt(remoteMessage.getData().getOrDefault("id", "-1"));
        }

        Notification.Builder
                .with(getApplicationContext())
                .notification(remoteMessage.getNotification())
                .type(Notification.Type.CHECK_ATTEND)
                .id(scheduleId)
                .action(Action.ATTEND.build(getApplicationContext(), NotificationBroadcastReceiver.getAttendIntent(getApplicationContext(), scheduleId)))
                .action(Action.NO.build(getApplicationContext(), NotificationBroadcastReceiver.getNoIntent(getApplicationContext(), scheduleId)))
                .show();
    }
}
