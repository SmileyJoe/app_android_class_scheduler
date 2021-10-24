package io.smileyjoe.classscheduler.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.smileyjoe.classscheduler.R;
import io.smileyjoe.classscheduler.databinding.ListHeaderScheduleBinding;
import io.smileyjoe.classscheduler.object.Schedule;
import io.smileyjoe.classscheduler.utils.LoadingData;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private ListHeaderScheduleBinding mView;
    private LoadingData mLoadingData;

    public HeaderViewHolder(Context context) {
        this(ListHeaderScheduleBinding.inflate(LayoutInflater.from(context)));
    }

    public HeaderViewHolder(ListHeaderScheduleBinding itemView) {
        super(itemView.getRoot());

        mView = itemView;
        mContext = mView.getRoot().getContext();

        mLoadingData = LoadingData.init()
                .add(mView.textHeading, 0);
    }

    public void onBind(Schedule schedule){
        mLoadingData.update(schedule.isEmpty());

        if(!schedule.isEmpty()){
            mView.textHeading.setText(schedule.getDay().getTitle(mContext));
        }
    }
}
