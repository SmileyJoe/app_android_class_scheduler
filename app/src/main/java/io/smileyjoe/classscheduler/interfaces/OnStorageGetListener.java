package io.smileyjoe.classscheduler.interfaces;

import android.net.Uri;

public interface OnStorageGetListener {

    void onComplete(boolean isSuccess, Uri uri, Exception exception);

}
