package io.smileyjoe.classscheduler.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.adapter.HeaderItemDecoration;
import io.smileyjoe.classscheduler.adapter.ScheduleAdapter;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.object.ScheduleComparator;

public class ClassFragment extends Fragment implements ValueEventListener{

    private ScheduleAdapter mAdapter;

    public ClassFragment() {
        super(R.layout.fragment_class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView(view);
        setDataListener();
    }

    private void setupView(View view){
        mAdapter = new ScheduleAdapter(new ArrayList<>());
        RecyclerView recyclerSchedule = view.findViewById(R.id.recycler_schedule);
        recyclerSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSchedule.setAdapter(mAdapter);
        recyclerSchedule.addItemDecoration(new HeaderItemDecoration(recyclerSchedule, mAdapter));
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

        Collections.sort(schedules, new ScheduleComparator());

        mAdapter.setItems(schedules);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.w("DbThings", "Failed to read value.", error.toException());
    }
}
