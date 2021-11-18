package io.smileyjoe.classscheduler.utils;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.smileyjoe.classscheduler.interfaces.OnStorageSaveListener;
import io.smileyjoe.classscheduler.interfaces.OnStorageGetListener;

public class StorageUtil {

    private static final String ROOT_USER = "user/{user_id}/{image}.jpg";
    private static final String KEY_PROFILE_IMAGE = "profile_image";

    private static StorageReference getUserRef(String image){
        return getUserRef(FirebaseAuth.getInstance().getCurrentUser().getUid(), image);
    }

    private static StorageReference getUserRef(String userId, String image){
        String name = ROOT_USER
                .replace("{user_id}", userId)
                .replace("{image}", image);

        return Utils
                .getFirebaseStorage()
                .getReference()
                .child(name);
    }

    public static void getProfileImage(String userId, OnStorageGetListener getListener){
        getUserRef(userId, KEY_PROFILE_IMAGE)
                .getDownloadUrl()
                .addOnSuccessListener(uri -> getListener.onComplete(true, uri, null))
                .addOnFailureListener(e -> getListener.onComplete(false, null, e));
    }

    public static void uploadProfileImage(Uri uri, OnStorageSaveListener saveListener){
        getUserRef(KEY_PROFILE_IMAGE)
                .putFile(uri)
                .addOnFailureListener(exception -> saveListener.onComplete(false, exception))
                .addOnSuccessListener(taskSnapshot -> saveListener.onComplete(true, null));
    }

}
