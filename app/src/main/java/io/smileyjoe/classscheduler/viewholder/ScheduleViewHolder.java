package io.smileyjoe.classscheduler.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ListRowScheduleBinding;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.icons.Icon;
import io.smileyjoe.icons.view.IconImageView;

public class ScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface Listener{
        void onScheduleClicked(Schedule schedule, ListRowScheduleBinding view);
    }

    private Context mContext;
    private Listener mListener;
    private Schedule mSchedule;
    private ListRowScheduleBinding mView;

    public ScheduleViewHolder(ViewGroup parent, Listener listener) {
        this(ListRowScheduleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    private ScheduleViewHolder(@NonNull ListRowScheduleBinding itemView, Listener listener) {
        super(itemView.getRoot());

        mView = itemView;
        mListener = listener;
        mView.getRoot().setOnClickListener(this);
        mContext = mView.getRoot().getContext();
    }

    public void onBind(Schedule schedule){
        mSchedule = schedule;
        mView.textName.setText(schedule.getName());
        mView.textDescription.setText(schedule.getDescription());
        mView.textTime.setText(schedule.getTimeFormatted(mContext));

        String iconName = schedule.getIconName();

        if(!TextUtils.isEmpty(iconName)) {
            mView.iconMain.reset();
            mView.iconMain.setIcon(schedule.getIconName());
            mView.iconMain.setVisibility(View.VISIBLE);
        } else {
            mView.iconMain.setImageDrawable(null);
            mView.iconMain.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onScheduleClicked(mSchedule, mView);
        }
    }
}
