package io.smileyjoe.classscheduler.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.object.Schedule;

public class ScheduleViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private TextView mTextName;
    private TextView mTextDescription;
    private TextView mTextTime;

    public ScheduleViewHolder(@NonNull View itemView) {
        super(itemView);

        mContext = itemView.getContext();
        mTextName = itemView.findViewById(R.id.text_name);
        mTextDescription = itemView.findViewById(R.id.text_description);
        mTextTime = itemView.findViewById(R.id.text_time);
    }

    public void onBind(Schedule schedule){
        mTextName.setText(schedule.getName());
        mTextDescription.setText(schedule.getDescription());
        mTextTime.setText(mContext.getString(R.string.text_schedule_time, schedule.getDay().getTitle(mContext), schedule.getTimeStart(), schedule.getTimeEnd()));
    }
}
