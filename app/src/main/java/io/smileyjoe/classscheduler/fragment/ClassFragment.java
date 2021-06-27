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
import io.smileyjoe.classscheduler.object.Schedule;

public class ClassFragment extends Fragment implements ValueEventListener{

    public ClassFragment() {
        super(R.layout.fragment_class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        setDataListener();
    }

    private void setDataListener(){
        FirebaseDatabase
                .getInstance()
                .getReference(Schedule.DB_NAME)
                .addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        ArrayList<Schedule> schedules = new ArrayList<>();

        for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
            schedules.add(new Schedule(itemSnapshot));
        }

        for(Schedule schedule:schedules){
            Log.d("DbThings", "Schedule: " + schedule.toString());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.w("DbThings", "Failed to read value.", error.toException());
    }
}
