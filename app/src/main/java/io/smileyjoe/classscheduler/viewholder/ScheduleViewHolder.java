package io.smileyjoe.classscheduler.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.icons.view.IconImageView;

public class ScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface Listener{
        void onScheduleClicked(Schedule schedule, View view);
    }

    private Context mContext;
    private TextView mTextName;
    private TextView mTextDescription;
    private TextView mTextTime;
    private IconImageView mIconMain;
    private Listener mListener;
    private Schedule mSchedule;
    private View mItemView;

    public ScheduleViewHolder(@NonNull View itemView, Listener listener) {
        super(itemView);

        mItemView = itemView;
        mListener = listener;
        itemView.setOnClickListener(this);
        mContext = itemView.getContext();
        mTextName = itemView.findViewById(R.id.text_name);
        mTextDescription = itemView.findViewById(R.id.text_description);
        mTextTime = itemView.findViewById(R.id.text_time);
        mIconMain = itemView.findViewById(R.id.icon_main);
    }

    public void onBind(Schedule schedule){
        mSchedule = schedule;
        mTextName.setText(schedule.getName());
        mTextDescription.setText(schedule.getDescription());
        mTextTime.setText(schedule.getTimeFormatted(mContext));

        String iconName = schedule.getIconName();

        if(!TextUtils.isEmpty(iconName)) {
            mIconMain.reset();
            mIconMain.setIcon(schedule.getIconName());
            mIconMain.setVisibility(View.VISIBLE);
        } else {
            mIconMain.setImageDrawable(null);
            mIconMain.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onScheduleClicked(mSchedule, mItemView);
        }
    }
}
