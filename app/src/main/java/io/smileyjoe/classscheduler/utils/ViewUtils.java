package io.smileyjoe.classscheduler.utils;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import io.smileyjoe.classscheduler.database.DbUser;

public class ViewUtils {

    public static void requiresLogin(View ...views) {
        int visibility = (FirebaseAuth.getInstance().getCurrentUser() == null) ? View.GONE:View.VISIBLE;

        for(View view:views){
            view.setVisibility(visibility);
        }

    }
}
