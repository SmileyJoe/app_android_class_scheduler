package io.smileyjoe.classscheduler.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ListRowScheduleBinding;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.object.User;
import io.smileyjoe.classscheduler.utils.LoadingData;
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
    private LoadingData mLoadingData;

    public ScheduleViewHolder(ViewGroup parent, Listener listener) {
        this(ListRowScheduleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    private ScheduleViewHolder(@NonNull ListRowScheduleBinding itemView, Listener listener) {
        super(itemView.getRoot());

        mView = itemView;
        mListener = listener;
        mContext = mView.getRoot().getContext();

        mLoadingData = LoadingData.init()
                .add(mView.textName, 24)
                .add(mView.textDescription, 100)
                .add(mView.textTime, 16);
    }

    public void onBind(Schedule schedule, User user){
        mLoadingData.update(schedule.isEmpty());

        if(!schedule.isEmpty()){
            mSchedule = schedule;
            mView.getRoot().setOnClickListener(this);
            mView.textName.setText(schedule.getName());
            mView.textDescription.setText(schedule.getDescription());
            mView.textTime.setText(schedule.getTimeFormatted(mContext));

            String iconName = schedule.getIconName();

            if (!TextUtils.isEmpty(iconName)) {
                mView.iconMain.reset();
                mView.iconMain.setIcon(schedule.getIconName());
                mView.iconMain.setVisibility(View.VISIBLE);
            } else {
                mView.iconMain.setImageDrawable(null);
                mView.iconMain.setVisibility(View.GONE);
            }

            handleStatus(schedule, user);
        }
    }

    public void handleStatus(Schedule schedule, User user){
        @StringRes int statusTitle = 0;
        @ColorRes int statusColor = 0;

        if(user != null){
            if(user.isAttending(schedule.getId())){
                statusTitle = R.string.text_attending;
                statusColor = R.color.colorPrimary;
            } else if(user.isRegistered(schedule.getId())){
                statusTitle = R.string.text_registered;
                statusColor =  R.color.colorAccent;
            }
        }

        if (statusTitle != 0 && statusColor != 0){
            mView.textStatus.setText(statusTitle);
            mView.textStatus.setTextColor(ContextCompat.getColor(mContext, statusColor));
            mView.textStatus.setVisibility(View.VISIBLE);
        } else {
            mView.textStatus.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onScheduleClicked(mSchedule, mView);
        }
    }
}
