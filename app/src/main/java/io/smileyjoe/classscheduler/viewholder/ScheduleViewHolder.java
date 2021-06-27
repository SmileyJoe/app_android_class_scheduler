package io.smileyjoe.classscheduler.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.object.Schedule;

public class ScheduleViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextName;

    public ScheduleViewHolder(@NonNull View itemView) {
        super(itemView);

        mTextName = itemView.findViewById(R.id.text_name);
    }

    public void onBind(Schedule schedule){
        mTextName.setText(schedule.getName());
    }
}
