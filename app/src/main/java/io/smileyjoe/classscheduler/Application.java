package io.smileyjoe.classscheduler;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.google.firebase.database.FirebaseDatabase;

import io.smileyjoe.classscheduler.utils.Notification;
import io.smileyjoe.classscheduler.utils.Utils;
import io.smileyjoe.icons.Icon;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Icon.setup(getApplicationContext());
        Notification.setup(getApplicationContext());
    }


}
