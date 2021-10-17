package io.smileyjoe.classscheduler.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import io.smileyjoe.classscheduler.databinding.FragmentClassBinding;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.object.ScheduleComparator;

public class ClassFragment extends BaseFirebaseFragment<FragmentClassBinding> implements ValueEventListener{

    private ScheduleAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupView();
    }

    private void setupView(){
        mAdapter = new ScheduleAdapter(new ArrayList<>());
        RecyclerView recyclerSchedule = getRoot().recyclerSchedule;
        recyclerSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSchedule.setAdapter(mAdapter);
        recyclerSchedule.addItemDecoration(new HeaderItemDecoration(recyclerSchedule, mAdapter));
    }

    @Override
    protected FragmentClassBinding inflate(LayoutInflater inflater, ViewGroup container, boolean savedInstanceState) {
        return FragmentClassBinding.inflate(inflater, container, savedInstanceState);
    }

    @Override
    protected String getDbName() {
        return Schedule.DB_NAME;
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
