package io.smileyjoe.classscheduler;

import io.smileyjoe.icons.Icon;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Icon.setup(getApplicationContext());
    }
}
