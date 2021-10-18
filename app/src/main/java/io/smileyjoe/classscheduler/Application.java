package io.smileyjoe.classscheduler;

import androidx.appcompat.app.AppCompatDelegate;

import io.smileyjoe.icons.Icon;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Icon.setup(getApplicationContext());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }
}
