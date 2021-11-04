package io.smileyjoe.classscheduler.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.StringRes;

import io.smileyjoe.classscheduler.R;

public class Notification {

    public enum Channel{
        GENERAL(R.string.notification_channel_general_name, R.string.notification_channel_general_description, R.string.notification_channel_general_id);

        private @StringRes int mName;
        private @StringRes int mDescription;
        private @StringRes int mId;

        Channel(int name, int description, int id) {
            mName = name;
            mDescription = description;
            mId = id;
        }

        public String getName(Context context){
            return context.getString(mName);
        }

        public String getDescription(Context context){
            return context.getString(mDescription);
        }

        public String getId(Context context){
            return context.getString(mId);
        }
    }

    public static void setup(Context context){
        for(Channel channel:Channel.values()){
            createNotificationChannel(context, channel);
        }
    }

    private static void createNotificationChannel(Context context, Channel channel) {
        CharSequence name = channel.getName(context);
        String description = channel.getDescription(context);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel = new NotificationChannel(channel.getId(context), name, importance);
        notificationChannel.setDescription(description);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }

}
