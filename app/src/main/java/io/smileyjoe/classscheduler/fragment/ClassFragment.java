package io.smileyjoe.classscheduler.fragment;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.smileyjoe.classscheduler.R;

public class ClassFragment extends Fragment {

    public ClassFragment() {
        super(R.layout.fragment_class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("schedule");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Log.d("DbThings", "Value is: " + itemSnapshot.child("name").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DbThings", "Failed to read value.", error.toException());
            }
        });
    }
}
