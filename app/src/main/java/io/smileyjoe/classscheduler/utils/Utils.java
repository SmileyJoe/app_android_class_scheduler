package io.smileyjoe.classscheduler.utils;

import android.os.Build;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

public class Utils {

    private static final String FIREBASE_EMU_HOST = "10.0.2.2";
    private static final int FIREBASE_EMU_AUTH_HOST = 9099;
    private static final int FIREBASE_EMU_DB_HOST = 9000;

    /**
     * Taken from:
     * https://github.com/flutter/plugins/blob/master/packages/device_info/device_info/android/src/main/java/io/flutter/plugins/deviceinfo/MethodCallHandlerImpl.java#L115-L131
     *
     * @return
     */
    private static boolean isEmulator() {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator");
    }

    public static FirebaseDatabase getDb(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        if(isEmulator()) {
            db.useEmulator(FIREBASE_EMU_HOST, FIREBASE_EMU_DB_HOST);
        }

        return db;
    }

    public static AuthUI getAuth(){
        AuthUI authUi = AuthUI.getInstance();

        if(Utils.isEmulator()) {
            authUi.useEmulator(FIREBASE_EMU_HOST, FIREBASE_EMU_AUTH_HOST);
        }

        return authUi;
    }

    public static void enableFCM(){
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().getToken();
    }

    public static void disableFCM(){
        FirebaseMessaging.getInstance().setAutoInitEnabled(false);
        FirebaseMessaging.getInstance().deleteToken();
    }

}
