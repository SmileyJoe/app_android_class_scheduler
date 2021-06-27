package io.smileyjoe.classscheduler.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.viewholder.ScheduleViewHolder;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleViewHolder> {

    private ArrayList<Schedule> mSchedules;

    public ScheduleAdapter(ArrayList<Schedule> schedules) {
        mSchedules = schedules;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        holder.onBind(getItem(position));
    }

    public Schedule getItem(int position){
        return mSchedules.get(position);
    }

    public void setItems(ArrayList<Schedule> schedules){
        mSchedules = schedules;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSchedules.size();
    }
}
