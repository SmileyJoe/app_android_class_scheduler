package io.smileyjoe.classscheduler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.logging.Handler;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ListHeaderScheduleBinding;
import io.smileyjoe.classscheduler.databinding.ListRowScheduleBinding;
import io.smileyjoe.classscheduler.object.Day;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.object.User;
import io.smileyjoe.classscheduler.viewholder.HeaderViewHolder;
import io.smileyjoe.classscheduler.viewholder.ScheduleViewHolder;

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HeaderItemDecoration.StickyHeaderInterface<ListHeaderScheduleBinding>{

    public interface Listener extends ScheduleViewHolder.Listener{}
    public interface DataListener{
        void onLoadingChanged(boolean isLoading);
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_SCHEDULE = 1;
    private ArrayList<Schedule> mSchedules;
    private Listener mListener;
    private DataListener mDataListener;
    private boolean mIsLoading = false;
    private User mUser;

    public ScheduleAdapter(Listener listener, DataListener dataListener) {
        mListener = listener;
        mDataListener = dataListener;
        setLoading(true);
    }

    public void setLoading(boolean loading) {
        if(mIsLoading != loading) {
            mIsLoading = loading;
            if(mDataListener != null){
                mDataListener.onLoadingChanged(loading);
            }
        }
    }

    public void setUser(User user) {
        mUser = user;
        notifyDataSetChanged();
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
                ((ScheduleViewHolder) holder).onBind(getItem(position), mUser);
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
        if(mIsLoading){
            if(position == 0){
                Schedule schedule = new Schedule();
                schedule.setHeader(true);
                return schedule;
            } else {
                return new Schedule();
            }

        } else {
            return mSchedules.get(position);
        }
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

        setLoading(false);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mIsLoading){
            return 12;
        } else if(mSchedules == null){
            return 0;
        } else {
            return mSchedules.size();
        }
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
