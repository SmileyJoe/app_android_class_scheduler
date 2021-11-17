package io.smileyjoe.classscheduler.utils;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

// TODO: Handle all errors basically //
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

    public static void getProfileImage(String userId, OnSuccessListener<Uri> onSuccessListener){
        getUserRef(userId, KEY_PROFILE_IMAGE).getDownloadUrl().addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
    }

    public static void uploadProfileImage(Uri uri, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener){
        Log.d("ImageThings", "uploadProfileImage: " + uri.toString());
        StorageReference storageRef = getUserRef(KEY_PROFILE_IMAGE);

        UploadTask task = storageRef.putFile(uri);

        // Register observers to listen for when the download is done or if it fails
        task.addOnFailureListener(exception -> {
            Log.d("ImageThings", "Failure");
            // Handle unsuccessful uploads
        }).addOnSuccessListener(onSuccessListener)
        .continueWithTask(task12 -> {
            if (!task12.isSuccessful()) {
                throw task12.getException();
            }
            Log.d("ImageThings", "Continue");
            // Continue with the task to get the download URL
            return storageRef.getDownloadUrl();
        }).addOnCompleteListener(task1 -> {
            Log.d("ImageThings", "Complete");
            if (task1.isSuccessful()) {
                Uri downloadUri = task1.getResult();
                Log.d("ImageThings", "Complete: " + downloadUri.toString());
            } else {
                // Handle failures
                // ...
            }
        });
    }

}
