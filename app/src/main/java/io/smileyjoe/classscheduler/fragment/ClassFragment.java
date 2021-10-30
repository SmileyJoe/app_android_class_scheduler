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
import io.smileyjoe.classscheduler.utils.LoadingLinearLayoutManager;

public class ClassFragment extends BaseFirebaseFragment<FragmentClassBinding> implements ValueEventListener, ScheduleAdapter.DataListener{

    public interface Listener extends ScheduleAdapter.Listener{
        void onClassDataLoaded();
    }

    private ScheduleAdapter mAdapter;
    private Listener mListener;
    private boolean mFirstLoad = true;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getActivity() instanceof Listener){
            mListener = (Listener) getActivity();
        }

        setupView();
    }

    private void setupView(){
        mAdapter = new ScheduleAdapter(mListener, this);
        RecyclerView recyclerSchedule = getRoot().recyclerSchedule;
        recyclerSchedule.setAdapter(mAdapter);
        recyclerSchedule.addItemDecoration(new HeaderItemDecoration(recyclerSchedule, mAdapter));
    }

    @Override
    protected FragmentClassBinding inflate(LayoutInflater inflater, ViewGroup container, boolean savedInstanceState) {
        return FragmentClassBinding.inflate(inflater, container, savedInstanceState);
    }

    @Override
    protected DatabaseReference getDatabaseReference() {
        return Schedule.getDbReference();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(mAdapter != null) {
            ArrayList<Schedule> schedules = new ArrayList<>();

            for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                schedules.add(new Schedule(itemSnapshot));
            }

            Collections.sort(schedules, new ScheduleComparator());

            mAdapter.setItems(schedules);
        }

        if(mFirstLoad){
            mFirstLoad = false;

            if(mListener != null){
                mListener.onClassDataLoaded();
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.w("DbThings", "Failed to read value.", error.toException());
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        if(getRoot() != null && getRoot().recyclerSchedule != null) {
            if (isLoading) {
                getRoot().recyclerSchedule.setLayoutManager(new LoadingLinearLayoutManager(getContext()));
            } else {
                getRoot().recyclerSchedule.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }
    }
}
