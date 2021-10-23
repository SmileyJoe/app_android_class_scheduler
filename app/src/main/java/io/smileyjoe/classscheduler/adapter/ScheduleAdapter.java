package io.smileyjoe.classscheduler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ListHeaderScheduleBinding;
import io.smileyjoe.classscheduler.databinding.ListRowScheduleBinding;
import io.smileyjoe.classscheduler.object.Day;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.viewholder.HeaderViewHolder;
import io.smileyjoe.classscheduler.viewholder.ScheduleViewHolder;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HeaderItemDecoration.StickyHeaderInterface<ListHeaderScheduleBinding>{

    public interface Listener extends ScheduleViewHolder.Listener{}

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_SCHEDULE = 1;
    private ArrayList<Schedule> mSchedules;
    private Listener mListener;

    public ScheduleAdapter(ArrayList<Schedule> schedules, Listener listener) {
        mSchedules = schedules;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_HEADER:
                return new HeaderViewHolder(parent.getContext());
            case TYPE_SCHEDULE:
            default:
                return new ScheduleViewHolder(parent, mListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).onBind(getItem(position));
                break;
            case TYPE_SCHEDULE:
            default:
                ((ScheduleViewHolder) holder).onBind(getItem(position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position).isHeader()){
            return TYPE_HEADER;
        } else {
            return TYPE_SCHEDULE;
        }
    }

    public Schedule getItem(int position){
        return mSchedules.get(position);
    }

    public void setItems(ArrayList<Schedule> schedules){
        mSchedules = new ArrayList<>();

        if(schedules != null && schedules.size() > 0) {
            Day day = schedules.get(0).getDay();

            mSchedules.add(new Schedule(day));

            for(Schedule schedule:schedules){
                if(day != schedule.getDay()){
                    day = schedule.getDay();
                    mSchedules.add(new Schedule(day));
                }
                mSchedules.add(schedule);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSchedules.size();
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        int headerPosition = 0;
        do {
            if (this.isHeader(itemPosition)) {
                headerPosition = itemPosition;
                break;
            }
            itemPosition -= 1;
        } while (itemPosition >= 0);
        return headerPosition;
    }

    @Override
    public ListHeaderScheduleBinding getHeaderLayout(ViewGroup parent, int headerPosition) {
        return ListHeaderScheduleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    }

    @Override
    public void bindHeaderData(ListHeaderScheduleBinding header, int headerPosition) {
        HeaderViewHolder holder = new HeaderViewHolder(header);
        holder.onBind(getItem(headerPosition));
    }

    @Override
    public boolean isHeader(int itemPosition) {
        return getItem(itemPosition).isHeader();
    }
}
